package com.rrtv.mongo.tool.vo.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @Classname SaveConnectionRequest
 * @Description
 * @Date 2022/7/12 11:05
 * @Created by wangchangjiu
 */

@Data
public class SaveConnectionRequest implements Serializable {

    private Long id;

    private String name;

    private String url;

}
