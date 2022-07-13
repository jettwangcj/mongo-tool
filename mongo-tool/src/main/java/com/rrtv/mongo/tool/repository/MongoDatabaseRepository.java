package com.rrtv.mongo.tool.repository;

import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Classname MongoDatabaseRepository
 * @Description
 * @Date 2022/7/8 14:00
 * @Created by wangchangjiu
 */
@Component
public class MongoDatabaseRepository {

    private static final Map<String, MongoDatabaseFactory> MONGO_CLIENT_DATABASE_FACTORY_CACHE = new ConcurrentHashMap<>();

    public MongoDatabaseFactory getOne(){
        return Optional.ofNullable(MONGO_CLIENT_DATABASE_FACTORY_CACHE.get(0)).orElse(getLocal());
    }

    private MongoDatabaseFactory getLocal(){
        return new SimpleMongoClientDatabaseFactory("mongodb://localhost:270/local");
    }

    public MongoDatabaseFactory get(String databaseKey){
      return MONGO_CLIENT_DATABASE_FACTORY_CACHE.get(databaseKey);
    }


    @PostConstruct
    public void loadData(){

      //  MONGO_CLIENT_DATABASE_FACTORY_CACHE.put("key1", new SimpleMongoClientDatabaseFactory("mongodb://localhost:27017/local"));
        MONGO_CLIENT_DATABASE_FACTORY_CACHE.put("key2", new SimpleMongoClientDatabaseFactory("mongodb://bokbok-content:monodbcontent2022@s-t4ndc1567f52f9a4.mongodb.singapore.rds.aliyuncs.com:3717,s-t4n6d78ce4ad2224.mongodb.singapore.rds.aliyuncs.com:3717/bokbok-content"));
        MONGO_CLIENT_DATABASE_FACTORY_CACHE.put("key3", new SimpleMongoClientDatabaseFactory("mongodb://bokbok-user:monodbuser2022@s-t4n3b6379cabf4e4.mongodb.singapore.rds.aliyuncs.com:3717,s-t4n60bd3cfe61af4.mongodb.singapore.rds.aliyuncs.com:3717/bokbok-user"));
    }

}
