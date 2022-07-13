package com.rrtv.mongo.tool.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Classname InterceptorConfig
 * @Description
 * @Date 2022/7/8 11:21
 * @Created by wangchangjiu
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private MongoInterceptor mongoInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(mongoInterceptor).addPathPatterns("/query/**");
    }

}
