package org.aturkov.expense.config;

import org.aturkov.expense.config.resolvers.SimplePaginationParamsHandlerMethodArgumentResolver;
import org.aturkov.expense.mapper.item.ItemDTOMapper;
import org.aturkov.expense.mapper.other.StringToLocalDateTimeConverter;
import org.aturkov.expense.service.item.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    private final ItemService itemService;
    private final ItemDTOMapper itemDTOMapper;

    @Autowired
    public WebConfig(ItemService itemService, ItemDTOMapper itemDTOMapper) {
        this.itemService = itemService;
        this.itemDTOMapper = itemDTOMapper;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToLocalDateTimeConverter());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SidebarInterceptor(itemService, itemDTOMapper));
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new SimplePaginationParamsHandlerMethodArgumentResolver());
    }
}
