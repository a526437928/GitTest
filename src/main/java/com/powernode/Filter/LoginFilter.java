package com.powernode.Filter;

import com.powernode.settings.pojo.TblUser;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class LoginFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String requestURI = request.getRequestURI();
        TblUser user = (TblUser) request.getSession().getAttribute("USER");

        if (requestURI.contains("login") || requestURI.contains("fonts") || requestURI.endsWith(".js") || requestURI.endsWith(".css") || requestURI.contains("image")) {
            chain.doFilter(request, response);
        } else {
            if (user != null) {
                chain.doFilter(request, response);
            } else {
                response.sendRedirect( "/login.html");

            }
        }

    }

    public void init(FilterConfig config) throws ServletException {

    }

}
