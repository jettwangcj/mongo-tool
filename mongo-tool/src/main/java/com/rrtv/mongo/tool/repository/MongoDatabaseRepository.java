package com.rrtv.mongo.tool.repository;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.rrtv.mongo.tool.repository.entity.Connection;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

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

    @Value("${spring.data.mongodb.uri}")
    private String connectionString;

    private  MongoDatabaseFactory localDatabaseFactory = null;

  /*  @Autowired
    private ConnectionRepository connectionRepository;*/

    private static final Map<String, MongoDatabaseFactory> MONGO_CLIENT_DATABASE_FACTORY_CACHE = new ConcurrentHashMap<>();

    public MongoDatabaseFactory getLocal() {
        if (StringUtils.isEmpty(connectionString)) {
            throw new RuntimeException("连接Mongo信息未加载。。。");
        }
        MongoClient mongoClient;
        String database;
        try {
            mongoClient = MongoClients.create(connectionString);
            database = new ConnectionString(connectionString).getDatabase();
        } catch (Exception ex) {
            throw new RuntimeException("Mongo 连接错误：" + ex);
        }
        localDatabaseFactory = new SimpleMongoClientDatabaseFactory(mongoClient, database);
        return localDatabaseFactory;
    }

    public MongoDatabaseFactory get(String databaseKey) {
        if(StringUtils.isEmpty(databaseKey)){
            // 默认配置文件的
            return this.localDatabaseFactory;
        }
        return MONGO_CLIENT_DATABASE_FACTORY_CACHE.get(databaseKey);
    }


    public void loadData() {

        //  MONGO_CLIENT_DATABASE_FACTORY_CACHE.put("key1", new SimpleMongoClientDatabaseFactory("mongodb://localhost:27017/local"));
       /* MONGO_CLIENT_DATABASE_FACTORY_CACHE.put("key2", new SimpleMongoClientDatabaseFactory("mongodb://bokbok-content:monodbcontent2022@s-t4ndc1567f52f9a4.mongodb.singapore.rds.aliyuncs.com:3717,s-t4n6d78ce4ad2224.mongodb.singapore.rds.aliyuncs.com:3717/bokbok-content"));
        MONGO_CLIENT_DATABASE_FACTORY_CACHE.put("key3", new SimpleMongoClientDatabaseFactory("mongodb://bokbok-user:monodbuser2022@s-t4n3b6379cabf4e4.mongodb.singapore.rds.aliyuncs.com:3717,s-t4n60bd3cfe61af4.mongodb.singapore.rds.aliyuncs.com:3717/bokbok-user"));*/
    }

    /**
     *  异步创建 Mongo DatabaseFactory
     * @param connectionId
     * @param url
     * @param dataBaseName
     */
    @Async
    public synchronized void asyncCreateMongoDataBase(String connectionId, String url, String dataBaseName) {
        String key = connectionId.concat("#").concat(dataBaseName);
        if(MONGO_CLIENT_DATABASE_FACTORY_CACHE.containsKey(connectionId) || MONGO_CLIENT_DATABASE_FACTORY_CACHE.containsKey(key)){
            return;
        }
        MongoClient mongoClient;
        String database = dataBaseName;
        try {
            mongoClient = MongoClients.create(url);
            if(StringUtils.isEmpty(database)){
                database = new ConnectionString(url).getDatabase();
            }
        } catch (Exception ex) {
            throw new RuntimeException("Mongo 连接错误：" + ex);
        }
        MONGO_CLIENT_DATABASE_FACTORY_CACHE.put(connectionId.concat("#").concat(database), new SimpleMongoClientDatabaseFactory(mongoClient, database));
    }
}
