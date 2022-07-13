package com.rrtv.mongo.tool.repository;

import com.rrtv.mongo.tool.repository.entity.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Classname ConnectionRepository
 * @Description
 * @Date 2022/7/12 13:52
 * @Created by wangchangjiu
 */
public interface ConnectionRepository extends JpaRepository<Connection, Long>, JpaSpecificationExecutor<Connection> {
}
