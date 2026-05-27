package com.code.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class LoginInterceptor implements HandlerInterceptor {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        
        // 放行选项请求
        if ("OPTIONS".equals(method)) {
            return true;
        }
        
        // 放行登录接口
        if (uri.contains("/admin/login")) {
            return true;
        }
        
        // 放行树形查询接口（游客可看）
        if (uri.contains("/folder/tree") && "GET".equals(method)) {
            return true;
        }
        
        // 放行复制接口（游客可复制，会统计）
        if (uri.contains("/solution/copy") && "POST".equals(method)) {
            return true;
        }
        
        // 以下接口需要登录（编辑、删除、新增操作）
        if (uri.contains("/add") || uri.contains("/edit") || uri.contains("/delete") || 
            uri.contains("/update") || uri.contains("/remove") ||
            ("POST".equals(method) && !uri.contains("/copy"))) {
            
            Object admin = request.getSession().getAttribute("admin");
            
            if (admin == null) {
                response.setStatus(401);
                response.setContentType("application/json;charset=UTF-8");
                Map<String, Object> result = new HashMap<>();
                result.put("code", 401);
                result.put("msg", "请先登录管理员账号");
                result.put("data", null);
                PrintWriter writer = response.getWriter();
                writer.write(objectMapper.writeValueAsString(result));
                writer.flush();
                writer.close();
                return false;
            }
        }
        
        return true;
    }
}