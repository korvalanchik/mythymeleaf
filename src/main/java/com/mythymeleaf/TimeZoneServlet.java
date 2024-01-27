package com.mythymeleaf;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Objects;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

@WebServlet(name = "timezoneServlet", urlPatterns = {"/", "/time"})
public class TimeZoneServlet extends HttpServlet {
    private static final TemplateEngine engine = new TemplateEngine();

    @Override
    public void init() {
        FileTemplateResolver resolver = new FileTemplateResolver();

        resolver.setPrefix(getPrefix());
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);
    }


    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        String username = (session != null) ? (String) session.getAttribute("username") : null;

        Context context = new Context(request.getLocale());
        context.setVariable("username", username);
        engine.process("usertimezone", context, response.getWriter());


//        response.setContentType("text/html");
//        Context context = new Context(request.getLocale(), Map.of("user", "Vasya"));
//        engine.process("usertimezone", context, response.getWriter());
        response.getWriter().close();

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");

        if (username != null && !username.trim().isEmpty()) {
            req.getSession().setAttribute("username", username);
        }

        resp.sendRedirect("/time");
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    private static String getPrefix() {
        String path = Objects.requireNonNull(TimeZoneServlet
                        .class
                        .getResource(""))
                .toString()
                .replace("file:/", "");
        Path prefix = Path.of(path + "../../../../templates").normalize();
        return URLDecoder.decode(prefix.toString(), StandardCharsets.UTF_8) + "\\";
    }

}