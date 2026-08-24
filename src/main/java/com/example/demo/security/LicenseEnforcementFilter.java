package com.example.demo.security;

import com.example.demo.service.LicenseService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@Order(1)
public class LicenseEnforcementFilter implements Filter {

    @Autowired
    private LicenseService licenseService;

    private static final List<String> EXEMPT_PATHS = Arrays.asList(
        "/licenca", "/api/v1/licenca", "/login", "/auth",
        "/css", "/js", "/images", "/webjars", "/favicon.ico", "/logo.png",
        "/h2-console", "/error", "/admin/licenciador"
    );

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String path = request.getRequestURI();

        // Allow exempt paths (license portal, login, static assets)
        for (String exempt : EXEMPT_PATHS) {
            if (path.startsWith(exempt)) {
                chain.doFilter(req, res);
                return;
            }
        }

        // Validate license
        if (!licenseService.isLicencaValida()) {
            response.sendRedirect("/licenca?expired=true");
            return;
        }

        chain.doFilter(req, res);
    }
}