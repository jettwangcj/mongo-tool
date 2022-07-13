package com.rrtv.mongo.tool.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.springframework.data.mongodb.MongoDatabaseFactory;

/**
 * @Classname MongoContext
 * @Description
 * @Date 2022/7/8 11:15
 * @Created by wangchangjiu
 */
public class MongoContextHolder {

    private static final ThreadLocal<MongoDatabaseFactory> MONGO_DATABASE_THREAD_LOCAL = new TransmittableThreadLocal<>();

    public static void setMongoDatabaseThreadLocal(MongoDatabaseFactory factory) {
        MONGO_DATABASE_THREAD_LOCAL.set(factory);
    }

    public static MongoDatabaseFactory getMongoDatabaseFactory(){
        return MONGO_DATABASE_THREAD_LOCAL.get();
    }

    public static void removeMongoDatabaseFactory(){
        MONGO_DATABASE_THREAD_LOCAL.remove();
    }

}
