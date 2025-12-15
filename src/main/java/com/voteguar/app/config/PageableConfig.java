package com.voteguar.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class PageableConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();

        resolver.setMaxPageSize(20);

        resolver.setFallbackPageable(org.springframework.data.domain.PageRequest.of(0, 10,
                org.springframework.data.domain.Sort.by("title").ascending()));

        resolvers.add(resolver);
    }
}