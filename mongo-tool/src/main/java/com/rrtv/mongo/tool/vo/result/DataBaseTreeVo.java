package com.rrtv.mongo.tool.vo.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Classname DataBaseTreeVo
 * @Description
 * @Date 2022/7/13 16:53
 * @Created by wangchangjiu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataBaseTreeVo implements Serializable {

    private String connectionId;

    private String name;

    private String url;

    private boolean leafed;

}
