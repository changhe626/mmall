package com.mmall.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CookieUtil {
    private static final String COOKIE_DOMAIN=".onyx.com";
    private static final String COOKIE_NAME="mmall_login_token";


    public static void writeLoginToken(HttpServletResponse response,String token){
        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setDomain(COOKIE_DOMAIN);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        //单位是秒
        //如果这个maxage不设置就不写入硬盘,只在内存中,当前页有效
        cookie.setMaxAge(60*60*24*365);
        log.info("write cookie name {},value {}",cookie.getName(),cookie.getValue());
        response.addCookie(cookie);
    }


    public static String  readLoginCookie(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for (Cookie cookie : cookies) {
                log.info("start read cookie name {},value {}",cookie.getName(),cookie.getValue());
                if(COOKIE_NAME.equals(cookie.getName())){
                    log.info("return cookie name {},value {}",cookie.getName(),cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


    public static void deleteToken(HttpServletRequest request,HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for (Cookie cookie : cookies) {
                if(COOKIE_NAME.equals(cookie.getName())){
                    cookie.setMaxAge(0);
                    cookie.setDomain("/");
                    cookie.setDomain(COOKIE_DOMAIN);
                    log.info("delete cookie name {},value {}",cookie.getName(),cookie.getValue());
                    response.addCookie(cookie);
                    return;
                }
            }
        }
    }



}
