package com.mythymeleaf;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
        Path prefix = null;
        try {
            prefix = (Paths.get(TimeZoneServlet
                            .class
                            .getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .toURI()))
                    .getParent();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        assert prefix != null;
        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix(prefix + "/templates/");
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
        String lastTimeZone = null;
        String getTimeZone = request.getParameter("timezone");
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if ("lastTimeZone".equals(cookie.getName())) {
                    if (getTimeZone != null && !getTimeZone.trim().isEmpty()) {
                        cookie.setValue(getTimeZone.replace(" ", "+"));
                        response.addCookie(cookie);
                    }
                    lastTimeZone = cookie.getValue();
                    break;
                }
            }
        }
        if (lastTimeZone == null) {
            lastTimeZone = (getTimeZone == null) ? "UTC" : getTimeZone;
            Cookie newCookie = new Cookie("lastTimeZone", lastTimeZone);
            newCookie.setMaxAge(900); // 15 minutes
            response.addCookie(newCookie);
        }

        String initTime = getTime(lastTimeZone, getTimeZone);

        Context context = new Context(request.getLocale());
        context.setVariable("username", username);
        context.setVariable("lastTimeZone", lastTimeZone);
        context.setVariable("initTime", initTime);

        engine.process("usertimezone", context, response.getWriter());

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

    private static String getTime(String lastTimeZone, String getTimeZone) {
        ZoneId zone;
        if (getTimeZone == null || getTimeZone.trim().isEmpty()) {
            zone = ZoneId.of(lastTimeZone.replace(" ", "+"));
        } else {
            zone = ZoneId.of(getTimeZone.replace(" ", "+"));
        }
        ZonedDateTime timeZone = ZonedDateTime.now(zone);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dtf.format(timeZone);
    }
}
