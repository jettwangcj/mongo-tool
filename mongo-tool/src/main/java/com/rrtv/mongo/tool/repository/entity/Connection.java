package com.rrtv.mongo.tool.repository.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @Classname Connection
 * @Description
 * @Date 2022/7/12 11:27
 * @Created by wangchangjiu
 */

@Data
@Document(value = "connection")
public class Connection implements Serializable {

    @Id
    private String id;

    private String name;

    private String hosts;

    private String url;

}
