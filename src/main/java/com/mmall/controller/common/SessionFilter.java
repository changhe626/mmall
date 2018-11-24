package com.mmall.controller.common;

import com.mmall.common.Const;
import com.mmall.pojo.User;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisSharedPoolUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SessionFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request1 = (HttpServletRequest) request;
        HttpServletResponse response1 = (HttpServletResponse) response;
        String loginCookie = CookieUtil.readLoginCookie(request1);
        if(StringUtils.isNotEmpty(loginCookie)){
            //logintoken不为空,符合条件,取user
            String s = RedisSharedPoolUtil.get(loginCookie);
            User user = JsonUtil.string2Bean(s, User.class);
            if(user!=null){
                //user不为空,重置session时间
                RedisSharedPoolUtil.expire(loginCookie, Const.SESSION_EXTIME);

            }
        }
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
