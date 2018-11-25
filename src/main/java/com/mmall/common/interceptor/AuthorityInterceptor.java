package com.mmall.common.interceptor;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisSharedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle");
        //请求中的controller的方法名
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        //解析handlermethod
        String method = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBean().getClass().getSimpleName();
        //解析参数,具体的key,value,打印日志
        Map<String, String[]> parameterMap = request.getParameterMap();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String key = entry.getKey();
            String value = Arrays.toString(entry.getValue());
            sb.append(key);
            sb.append("=");
            sb.append(value);
        }
        ///账号,密码,不打印在日志中,为了安全起见
        log.info("请求的类是{} ,方法是{},参数是:{}",className,method,sb.toString());


        User user=null;
        String loginCookie = CookieUtil.readLoginCookie(request);
        if (StringUtils.isNotEmpty(loginCookie)) {
            String s = RedisSharedPoolUtil.get(loginCookie);
            user = JsonUtil.string2Bean(s, User.class);
        }
        if(user==null || (user.getRole().intValue()!= Const.Role.ROLE_ADMIN)){
            PrintWriter writer = response.getWriter();
            //不调用controller中方法

            //这里添加reset,否则出现异常,getWriter()
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");

            //富文本的上传的返回的特殊处理....

            if(user==null){
                if(StringUtils.equals(className,"ProductManageController") && StringUtils.equals(method,"richtextImgUpload")){
                    HashMap<Object, Object> map = Maps.newHashMap();
                    map.put("success", false);
                    map.put("msg", "请登录管理员");
                    writer.print(JsonUtil.obj2String(map));
                }else {
                    writer.print(JsonUtil.obj2String(ServerResponse.createByErrorMessage("用户未登录")));
                }
                writer.print(JsonUtil.obj2String(ServerResponse.createByErrorMessage("用户未登录")));
            }else {
                if(StringUtils.equals(className,"ProductManageController") && StringUtils.equals(method,"richtextImgUpload")){
                    HashMap<Object, Object> map = Maps.newHashMap();
                    map.put("success", false);
                    map.put("msg", "请登录管理员");
                    writer.print(JsonUtil.obj2String(map));
                }else {
                    writer.print(JsonUtil.obj2String(ServerResponse.createByErrorMessage("无权限操作")));
                }
            }
            writer.flush();
            writer.close();
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("afterCompletion");
    }
}
