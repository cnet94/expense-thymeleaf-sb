package org.aturkov.expense.config;

import org.aturkov.expense.mapper.item.ItemDTOMapper;
import org.aturkov.expense.service.item.ItemService;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SidebarInterceptor implements HandlerInterceptor {

    private final ItemService itemService;
    private final ItemDTOMapper itemDTOMapper;

    public SidebarInterceptor(ItemService itemService, ItemDTOMapper itemDTOMapper) {
        this.itemService = itemService;
        this.itemDTOMapper = itemDTOMapper;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            modelAndView.addObject("items", itemDTOMapper.convertCollection(itemService.findItems()));
        }
    }
}
