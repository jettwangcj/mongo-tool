package com.rrtv.mongo.tool.controller;

import com.rrtv.SQLToMongoTemplate;
import com.rrtv.annotation.EnableSqlToMongoMapper;
import com.rrtv.mongo.tool.service.MongoSQLExecuteService;
import com.rrtv.mongo.tool.vo.request.SendSqlRequest;
import com.rrtv.mongo.tool.vo.result.ResultResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Classname MongoQueryController
 * @Description
 * @Date 2022/7/12 14:29
 * @Created by wangchangjiu
 */

@Slf4j
@RestController
@RequestMapping("/sql")
public class MongoExecuteController {

    @Autowired
    private MongoSQLExecuteService mongoSQLExecuteService;

    @ApiOperation(value = "删除mongo的连接")
    @PostMapping("/send")
    public ResultResponse<List<Map>> sendSql(@RequestBody SendSqlRequest request) {
        mongoSQLExecuteService.executeSql(request.getSql());
        return ResultResponse.success();
    }


}
