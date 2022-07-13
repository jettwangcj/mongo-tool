package com.rrtv.mongo.tool.service;

import com.rrtv.mongo.tool.vo.request.SaveConnectionRequest;
import com.rrtv.mongo.tool.vo.result.ConnectionVo;
import com.rrtv.mongo.tool.vo.result.DataBaseTreeVo;
import com.rrtv.mongo.tool.vo.result.DataBaseVo;
import com.rrtv.mongo.tool.vo.result.FrontEndPagingResponse;

import java.util.List;

/**
 * @Classname MongoConnectionService
 * @Description
 * @Date 2022/7/8 17:50
 * @Created by wangchangjiu
 */
public interface MongoConnectionService {

    /**
     *  创建连接
     * @param request
     */
    void createConnection(SaveConnectionRequest request);

    /**
     *  删除 连接
     * @param id
     */
    void deleteConnection(Long id);


    /**
     *  获取连接/数据库/集合 tree
     * @param level
     * @param connectionId
     * @return
     */
    List<DataBaseTreeVo> queryDataBaseTrees(Integer level, Long connectionId, String dataBaseName);
}
