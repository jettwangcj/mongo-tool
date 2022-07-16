package com.rrtv.mongo.tool.vo.result;

import lombok.Data;

import java.io.Serializable;

/**
 * @Classname ExecuteSQLResultVo
 * @Description
 * @Date 2022/7/15 18:12
 * @Created by wangchangjiu
 */
@Data
public abstract class ExecuteSQLResultVo implements Serializable {

    private Integer sqlType;

    private boolean success;

    private String failMessage;

}
