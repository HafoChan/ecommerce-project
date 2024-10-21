package com.sohan.user_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import jakarta.servlet.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.Instant;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class FilterConfig {

    @Bean
    @Order(1)
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeClientInfo(true);
        filter.setIncludeQueryString(true);
        filter.setIncludeHeaders(true);
        return filter;
    }

    @Component
    @Log4j2
    @Order(2)
    public static class ResponseLoggingFilterConfig implements Filter {
        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {
            Instant start = Instant.now();

            HttpServletResponse httpResponse = (HttpServletResponse) response;
            chain.doFilter(request, response);

            var statusCode = ((HttpServletResponse) response).getStatus();
            Instant end = Instant.now();
            long duration = java.time.Duration.between(start, end).toMillis();

            log.info("Request status code: {}", statusCode);
            log.info("Request completed in {} ms", duration);
        }
    }
}


