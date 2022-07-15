package com.rrtv.mongo.tool.config;

import com.rrtv.mongo.tool.repository.MongoDatabaseRepository;
import com.rrtv.mongo.tool.template.DynamicMongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @Classname MongoConfig
 * @Description
 * @Date 2022/7/8 14:10
 * @Created by wangchangjiu
 */
@Configuration
public class MongoConfig {

    @Bean
    public MongoDatabaseFactory mongoDatabaseFactory(@Autowired MongoDatabaseRepository repository){
        return repository.getLocal();
    }

    @Bean
    public MongoTemplate mongoTemplate(@Autowired MongoDatabaseFactory mongoDatabaseFactory){
        return new DynamicMongoTemplate(mongoDatabaseFactory);
    }

}
