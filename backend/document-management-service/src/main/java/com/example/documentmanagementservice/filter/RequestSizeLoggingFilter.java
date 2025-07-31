package com.example.documentmanagementservice.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RequestSizeLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest httpRequest) {
            int contentLength = httpRequest.getContentLength(); // -1 if unknown
            long contentLengthLong = httpRequest.getContentLengthLong(); // better for large files

            System.out.println("📦 Request Content-Length: " + contentLengthLong + " bytes");
            System.out.println("📦 Request Content-Length: " + (contentLengthLong / (1024.0 * 1024.0)) + " MB");
        }

        chain.doFilter(request, response);
    }
}
