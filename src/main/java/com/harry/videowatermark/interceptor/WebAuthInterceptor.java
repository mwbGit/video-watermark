package com.harry.videowatermark.interceptor;

import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;


@Slf4j
@Configuration
public class WebAuthInterceptor extends HandlerInterceptorAdapter {
    private static final Logger accessLog = LoggerFactory.getLogger(WebAuthInterceptor.class);
    private static long TOTAL_ACCESS = 0;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        accessLog.info("---access---total_access:{},ip: {} ,uri:{},params:{}", TOTAL_ACCESS++, getIpAddress(request), request.getRequestURI(), getBody(request));
        return true;
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public String getBody(HttpServletRequest request) {
        try {
            Map<String, String[]> paramMap = request.getParameterMap();
            if (MapUtil.isEmpty(paramMap)) {
                return request.getQueryString();
            }
            StringBuilder builder = new StringBuilder();
            String[] vals;
            for (Map.Entry entry : paramMap.entrySet()) {
                vals = (String[]) entry.getValue();
                builder.append(entry.getKey());
                builder.append("=");
                if (vals != null && vals.length > 0) {
                    builder.append(vals.length == 1 ? vals[0] : Arrays.toString(vals));
                }
                builder.append("&");
            }
            return builder.toString().substring(0, builder.length() - 1);
        } catch (Exception e) {
            log.error("WebAuthInterceptor.getBody err", e);
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println("abcd".substring(0,"abcd".length()-1));
    }
}
