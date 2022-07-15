package com.rrtv.mongo.tool.interceptor;

import com.rrtv.mongo.tool.context.MongoContextHolder;
import com.rrtv.mongo.tool.repository.MongoDatabaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Classname MongoDBInterceptor
 * @Description
 * @Date 2022/7/8 11:06
 * @Created by wangchangjiu
 */
@Component
public class MongoInterceptor implements HandlerInterceptor {

    @Autowired
    private MongoDatabaseRepository repository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");

        MongoDatabaseFactory mongoDatabaseFactory = repository.get(token);
        MongoContextHolder.setMongoDatabaseThreadLocal(mongoDatabaseFactory);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex){
        MongoContextHolder.removeMongoDatabaseFactory();
    }

}
