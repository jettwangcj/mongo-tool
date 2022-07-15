package com.rrtv.mongo.tool.repository;

import com.rrtv.mongo.tool.repository.entity.Connection;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Classname ConnectionRepository
 * @Description
 * @Date 2022/7/12 13:52
 * @Created by wangchangjiu
 */
public interface ConnectionRepository extends MongoRepository<Connection,String> {
}
