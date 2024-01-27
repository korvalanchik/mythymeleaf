package com.mythymeleaf;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;



@WebServlet(name = "timezoneServlet", value = "/time")
public class TimeZoneServlet extends HttpServlet {
    private String message;
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

        message = "UTC+2";
    }


    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        Context context = new Context(request.getLocale(), Map.of("user", "Vasya"));
        engine.process("usertimezone", context, response.getWriter());
        response.getWriter().close();


//
//        // Hello
//        PrintWriter out = response.getWriter();
//        out.println("<html><body>");
//        out.println("<h1>" + message + "</h1>");
//        out.println("</body></html>");
    }

    @Override
    public void destroy() {
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