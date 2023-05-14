package com.scommunity.config;

import com.scommunity.controller.interceptor.AlphaInterceptor;
import com.scommunity.controller.interceptor.LoginTicketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author sss
 * @version 1.0
 * @date 2023/5/12 11:03
 */
/**拦截器的配置类
 * */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**配置，对测试的拦截器进行配置
     * */
    @Autowired
    private AlphaInterceptor alphaInterceptor;

    /**对刚才写的拦截器（LoginTicketInterceptor）进行配置
     * */
    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(alphaInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg")
                .addPathPatterns("/register", "/login");


        //拦截处理静态请求之外的全部的请求
        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");
    }


}
