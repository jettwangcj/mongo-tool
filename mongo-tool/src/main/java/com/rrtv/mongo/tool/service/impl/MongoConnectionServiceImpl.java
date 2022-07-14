package com.rrtv.mongo.tool.service.impl;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.rrtv.mongo.tool.repository.ConnectionRepository;
import com.rrtv.mongo.tool.repository.MongoDatabaseRepository;
import com.rrtv.mongo.tool.repository.entity.Connection;
import com.rrtv.mongo.tool.service.MongoConnectionService;
import com.rrtv.mongo.tool.vo.request.SaveConnectionRequest;
import com.rrtv.mongo.tool.vo.result.ConnectionVo;
import com.rrtv.mongo.tool.vo.result.DataBaseTreeVo;
import com.rrtv.mongo.tool.vo.result.DataBaseVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            connection.setAuthorityKey(UUID.randomUUID().toString().replace("-", ""));
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
    public void deleteConnection(Long id) {
        repository.deleteById(id);
    }

    private List<DataBaseTreeVo> queryDataBases(Long connectionId) {
        Connection connection = repository.findById(connectionId).orElseThrow(() -> new RuntimeException("资源不存在"));
        MongoClient mongoClient = MongoClients.create(connection.getUrl());
        List<DataBaseTreeVo> databases = new ArrayList<>();
        mongoClient.listDatabaseNames().forEach(item -> databases.add(new DataBaseTreeVo(connection.getId(), item, false)));
        return databases;
    }

    @Override
    public List<DataBaseTreeVo> queryDataBaseTrees(Integer level, Long connectionId, String dataBaseName) {
        if(level == 0){
            // 获取连接
            return this.queryConnectionList();
        } else if(level == 1){
            // 获取数据库
            if(connectionId == null){
                throw new RuntimeException("连接参数不能为空");
            }

            databaseRepository.load

            return this.queryDataBases(connectionId);
        } else if (level == 2){
            if(connectionId == null || StringUtils.isEmpty(dataBaseName)){
                throw new RuntimeException("连接参数或者数据库不能为空");
            }
            // 获取文档
            return this.queryDocument(connectionId, dataBaseName);

        }

        return null;
    }

    private List<DataBaseTreeVo> queryDocument(Long connectionId, String dataBaseName) {
        Connection connection = repository.findById(connectionId).orElseThrow(() -> new RuntimeException("资源不存在"));
        MongoClient mongoClient = MongoClients.create(connection.getUrl());
        MongoDatabase database = mongoClient.getDatabase(dataBaseName);
        List<DataBaseTreeVo> documents = new ArrayList<>();
        database.listCollectionNames().forEach(item -> documents.add(new DataBaseTreeVo(connection.getId(), item, true)));
        return documents;
    }


}
