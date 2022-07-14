package com.rrtv.mongo.tool.controller;

import com.rrtv.mongo.tool.vo.result.ResultResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname MongoQueryController
 * @Description
 * @Date 2022/7/12 14:29
 * @Created by wangchangjiu
 */

@Slf4j
@RestController
@RequestMapping("/sql")
public class MongoQueryController {

    @ApiOperation(value = "删除mongo的连接")
    @PostMapping("/send")
    public ResultResponse<String> sendSql() {
        return ResultResponse.success();
    }


}
