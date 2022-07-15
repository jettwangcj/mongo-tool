package com.rrtv.mongo.tool.service.impl;

import com.rrtv.SQLToMongoTemplate;
import com.rrtv.exception.SqlParserException;
import com.rrtv.mongo.tool.service.MongoSQLExecuteService;
import com.rrtv.util.SqlCommonUtil;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Classname MongoSQLExecuteServiceImpl
 * @Description
 * @Date 2022/7/15 16:59
 * @Created by wangchangjiu
 */

@Service
public class MongoSQLExecuteServiceImpl implements MongoSQLExecuteService {

    @Autowired
    private SQLToMongoTemplate sqlToMongoTemplate;

    @Override
    public void executeSql(String sql) {

        sqlToMongoTemplate.selectList(sql, Map.class);

        PlainSelect plainSelect = SqlCommonUtil.parserSelectSql(sql);



    }



}
