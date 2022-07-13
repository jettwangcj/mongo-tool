package com.rrtv.mongo.tool.service.impl;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoIterable;
import com.rrtv.mongo.tool.repository.ConnectionRepository;
import com.rrtv.mongo.tool.repository.entity.Connection;
import com.rrtv.mongo.tool.service.MongoConnectionService;
import com.rrtv.mongo.tool.vo.request.SaveConnectionRequest;
import com.rrtv.mongo.tool.vo.result.ConnectionVo;
import com.rrtv.mongo.tool.vo.result.DataBaseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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


    @Override
    public void createConnection(SaveConnectionRequest request) {
        ConnectionString connectionString = new ConnectionString(request.getUrl());
        List<String> hosts = connectionString.getHosts();
        String database = connectionString.getDatabase();
        Connection connection;
        if(request.getId() != null){
            connection = repository.findById(request.getId()).orElseThrow(() -> new RuntimeException("资源不存在"));
        } else {
            connection = new Connection();
            connection.setAuthorityKey(UUID.randomUUID().toString().replace("-", ""));
        }
        connection.setDatabase(database);
        connection.setUrl(request.getUrl());
        connection.setHosts(String.join(",", hosts));
        repository.save(connection);
    }


    @Override
    public List<ConnectionVo> queryConnectionList() {
        List<Connection> connections = repository.findAll();
        return CollectionUtils.isEmpty(connections) ? new ArrayList<>() : connections.stream().map(item -> {
            ConnectionVo connectionVo = new ConnectionVo();
            BeanUtils.copyProperties(item, connectionVo);
            return connectionVo;
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteConnection(Long id) {
        repository.deleteById(id);
    }

    @Override
    public DataBaseVo queryDataBases(Long connectionId) {

        Connection connection = repository.findById(connectionId).orElseThrow(() -> new RuntimeException("资源不存在"));
        MongoClient mongoClient = MongoClients.create(connection.getUrl());
        List<String> databaseNames = new ArrayList<>();




          // mongoClient.listDatabaseNames()

        return DataBaseVo.builder()
                .connectionId(connectionId)
                .authorityKey(connection.getAuthorityKey())
                .databaseNames(databaseNames)
                .build();
    }


}
