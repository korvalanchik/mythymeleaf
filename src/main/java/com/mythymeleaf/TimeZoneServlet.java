package com.mythymeleaf;

import java.io.*;
import java.util.Map;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

@WebServlet(name = "timezoneServlet", value = "/time")
public class TimeZoneServlet extends HttpServlet {
    private String message;
    private TemplateEngine engine = new TemplateEngine();
    public void init() {
        engine = new TemplateEngine();
        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix("D:/JavaHomeWork/MyThymeleaf/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);

        message = "UTC+2";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        Context context = new Context(request.getLocale(), Map.of("user", "Vasya"));
        engine.process("usertimezone", context, response.getWriter());
        response.getWriter().close();;


//        response.setContentType("text/html");
//
//        // Hello
//        PrintWriter out = response.getWriter();
//        out.println("<html><body>");
//        out.println("<h1>" + message + "</h1>");
//        out.println("</body></html>");
    }

    public void destroy() {
    }
}