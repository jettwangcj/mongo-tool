package com.rrtv.mongo.tool.service.impl;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.rrtv.mongo.tool.repository.ConnectionRepository;
import com.rrtv.mongo.tool.repository.MongoDatabaseRepository;
import com.rrtv.mongo.tool.repository.entity.Connection;
import com.rrtv.mongo.tool.service.MongoConnectionService;
import com.rrtv.mongo.tool.vo.request.SaveConnectionRequest;
import com.rrtv.mongo.tool.vo.result.DataBaseTreeVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Classname MongoConnectionServiceImpl
 * @Description
 * @Date 2022/7/8 17:50
 * @Created by wangchangjiu
 */
@Service
public class MongoConnectionServiceImpl implements MongoConnectionService {

    @Autowired
    private ConnectionRepository repository;

    @Autowired
    private MongoDatabaseRepository databaseRepository;

    @Override
    public void createConnection(SaveConnectionRequest request) {
        ConnectionString connectionString = new ConnectionString(request.getUrl());
        List<String> hosts = connectionString.getHosts();
        Connection connection;
        if(request.getId() != null){
            connection = repository.findById(request.getId()).orElseThrow(() -> new RuntimeException("资源不存在"));
        } else {
            connection = new Connection();
        }
        connection.setName(request.getName());
        connection.setUrl(request.getUrl());
        connection.setHosts(String.join(",", hosts));
        repository.save(connection);
    }


    private List<DataBaseTreeVo> queryConnectionList() {
        List<Connection> connections = repository.findAll();
        return CollectionUtils.isEmpty(connections) ? new ArrayList<>() : connections.stream().map(item -> new DataBaseTreeVo(item.getId(), item.getName(), false)).collect(Collectors.toList());
    }

    @Override
    public void deleteConnection(String id) {
        repository.deleteById(id);
    }

    private List<DataBaseTreeVo> queryDataBases(String connectionId) {
        Connection connection = repository.findById(connectionId).orElseThrow(() -> new RuntimeException("资源不存在"));
        MongoClient mongoClient = MongoClients.create(connection.getUrl());
        List<DataBaseTreeVo> databases = new ArrayList<>();
        mongoClient.listDatabaseNames().forEach(item -> databases.add(new DataBaseTreeVo(connection.getId(), item, false)));
        mongoClient.close();
        return databases;
    }

    @Override
    public List<DataBaseTreeVo> queryDataBaseTrees(Integer level, String connectionId, String dataBaseName) {
        if(level == 0){
            // 获取连接
            return this.queryConnectionList();
        } else if(level == 1){
            // 获取数据库
            if(connectionId == null){
                throw new RuntimeException("连接参数不能为空");
            }
            return this.queryDataBases(connectionId);
        } else if (level == 2){
            if(connectionId == null || StringUtils.isEmpty(dataBaseName)){
                throw new RuntimeException("连接参数或者数据库不能为空");
            }

            Connection connection = repository.findById(connectionId).orElseThrow(() -> new RuntimeException("资源不存在"));
            databaseRepository.asyncCreateMongoDataBase(connection.getId(), connection.getUrl(), dataBaseName);

            // 获取文档
            return this.queryDocument(connection, dataBaseName);
        }
        return null;
    }

    private List<DataBaseTreeVo> queryDocument(Connection connection, String dataBaseName) {
        MongoClient mongoClient = MongoClients.create(connection.getUrl());
        MongoDatabase database = mongoClient.getDatabase(dataBaseName);
        List<DataBaseTreeVo> documents = new ArrayList<>();
        database.listCollectionNames().forEach(item -> documents.add(new DataBaseTreeVo(connection.getId(), item, true)));
        mongoClient.close();
        return documents;
    }


}
