package com.rrtv.mongo.tool.repository.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Classname Connection
 * @Description
 * @Date 2022/7/12 11:27
 * @Created by wangchangjiu
 */

@Data
@Entity
@Table(name = "connection")
public class Connection implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "authority_key")
    private String authorityKey;

    @Column(name = "hosts")
    private String hosts;

    @Column(name = "data_base")
    private String database;

    @Column(name = "url")
    private String url;

}
