package com.mythymeleaf;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;

@WebFilter(value = "/time")
public class ValidateFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String timeZone = req.getParameter("timezone");
        if ((timeZone == "") ||
                (timeZone == null) ||
                (Pattern.matches("^UTC(?:[+,-][0-1]?[0-8])?$",timeZone.replace(' ', '+')))) { //
            chain.doFilter(req, res);
        } else {
            res.setStatus(400);
            res.setContentType("text/html; charset=utf-8");
            PrintWriter out = res.getWriter();
            out.println("<html><head><title>HTTP Status 400 - Invalid timezone</title><head>");
            out.println("<body>");
            out.println("<h1>HTTP Status 400 - Invalid timezone</h1>");
            out.println("<h3>Valid time zone from UTC-18 to UTC+18</h3>");
            out.println("</body></html>");
            out.close();
        }
    }


}
