package com.rrtv.mongo.tool.vo.result;

import lombok.Data;

import java.io.Serializable;

/**
 * @Classname ConnectionResultVo
 * @Description
 * @Date 2022/7/12 10:51
 * @Created by wangchangjiu
 */
@Data
public class ConnectionResultVo implements Serializable {

    private boolean success;

    private String failMessage;

}
