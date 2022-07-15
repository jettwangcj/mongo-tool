package com.rrtv.mongo.tool.vo.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @Classname SendSqlRequest
 * @Description
 * @Date 2022/7/14 17:47
 * @Created by wangchangjiu
 */
@Data
public class SendSqlRequest implements Serializable {

    private String sql;

}
