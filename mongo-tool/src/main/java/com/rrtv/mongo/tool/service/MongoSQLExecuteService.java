package com.rrtv.mongo.tool.service;

import com.rrtv.mongo.tool.vo.result.ExecuteSQLResultVo;

/**
 * @Classname MongoSQLExecuteService
 * @Description
 * @Date 2022/7/15 16:58
 * @Created by wangchangjiu
 */
public interface MongoSQLExecuteService {

    ExecuteSQLResultVo executeSql(String sql);

}
