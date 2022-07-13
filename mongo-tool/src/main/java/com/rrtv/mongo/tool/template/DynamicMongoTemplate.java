package com.rrtv.mongo.tool.template;

import com.mongodb.client.MongoDatabase;
import com.rrtv.mongo.tool.context.MongoContextHolder;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @Classname DynamicMongoTemplate
 * @Description
 * @Date 2022/7/8 11:12
 * @Created by wangchangjiu
 */
public class DynamicMongoTemplate extends MongoTemplate {

    public DynamicMongoTemplate(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

    @Override
    protected MongoDatabase doGetDatabase() {
        return MongoContextHolder.getMongoDatabaseFactory().getMongoDatabase();
    }

}
