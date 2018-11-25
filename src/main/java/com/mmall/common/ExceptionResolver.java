package com.mmall.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class ExceptionResolver  implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        log.error("{} exception",request.getRequestURI(),e);
        MappingJacksonJsonView jsonView = new MappingJacksonJsonView();
        ModelAndView view = new ModelAndView(jsonView);
        //当时用jackson2.x 的时候,使用MappingJacksonJson2View
        view.addObject("status",ResponseCode.ERROR.getCode());
        view.addObject("msg","接口异常,请联系管理员");
        view.addObject("data",e.toString());
        return view;
    }
}
