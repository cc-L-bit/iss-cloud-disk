package com.iss.cloud.disk.configure;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginHandlerInterceptor implements HandlerInterceptor {
    private final static Logger logger = Logger.getLogger(LoginHandlerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object user = request.getSession().getAttribute("user");
        logger.info("request请求地址path : " + request.getServletPath() + ",request请求地址 uri:"+ request.getRequestURI());
        try {
            if (user == null) {
                request.setAttribute("msg", "无权限请先登录");
                request.getRequestDispatcher("/").forward(request, response);
                return false;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
