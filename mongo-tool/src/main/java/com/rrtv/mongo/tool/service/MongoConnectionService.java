package com.rrtv.mongo.tool.service;

import com.rrtv.mongo.tool.vo.request.SaveConnectionRequest;
import com.rrtv.mongo.tool.vo.result.ConnectionVo;
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
     *  分页获取连接
     * @return
     */
    List<ConnectionVo> queryConnectionList();

    /**
     *  删除 连接
     * @param id
     */
    void deleteConnection(Long id);

    /**
     *  获取数据库
     * @param connectionId
     * @return
     */
    DataBaseVo queryDataBases(Long connectionId);
}
