package com.rrtv.mongo.tool.controller;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.rrtv.mongo.tool.service.MongoConnectionService;
import com.rrtv.mongo.tool.vo.request.SaveConnectionRequest;
import com.rrtv.mongo.tool.vo.result.*;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Classname MongoConnectionController
 * @Description
 * @Date 2022/7/8 16:26
 * @Created by wangchangjiu
 */

@Slf4j
@RestController
@RequestMapping("/connection")
public class MongoConnectionController {

    @Autowired
    private MongoConnectionService mongoConnectionService;


    @ApiOperation(value = "获取连接列表")
    @GetMapping("/trees")
    public ResultResponse<List<DataBaseTreeVo>> getDataBaseTrees(@RequestParam("level") Integer level,
                                                                 @RequestParam(value = "connectionId", required = false) String connectionId,
                                                                 @RequestParam(value = "dataBaseName", required = false) String dataBaseName) {
        return ResultResponse.success(mongoConnectionService.queryDataBaseTrees(level, connectionId, dataBaseName));
    }

    @ApiOperation(value = "获取连接列表")
    @GetMapping("/table/data")
    public ResultResponse<List<DataBaseTreeVo>> getDataBaseTrees(@RequestParam(value = "connectionId") String connectionId,
                                                                 @RequestParam(value = "documentName") String documentName) {

        return ResultResponse.success();
    }


    @ApiOperation(value = "删除mongo的连接")
    @PostMapping("/delete")
    public ResultResponse<String> deleteConnection(String id) {
        mongoConnectionService.deleteConnection(id);
        return ResultResponse.success();
    }


    @ApiOperation(value = "创建mongo的连接")
    @PostMapping("/create")
    public ResultResponse<String> createConnection(@RequestBody SaveConnectionRequest request) {
        mongoConnectionService.createConnection(request);
        return ResultResponse.success();
    }


    @ApiOperation(value = "测试mongo的连接")
    @GetMapping("/test")
    public ResultResponse<ConnectionResultVo> testConnection(@RequestParam(value = "url") String url) {
        boolean success = true;
        log.info("测试mongo的连接:{}", url);
        ConnectionResultVo resultVo = new ConnectionResultVo();
        try {
            MongoClient mongoClient = MongoClients.create(url);
            mongoClient.startSession();
        }catch (Exception ex) {
            success = false;
            resultVo.setFailMessage(ex.getMessage());
        }
        resultVo.setSuccess(success);
        return ResultResponse.success(resultVo);
    }






}
