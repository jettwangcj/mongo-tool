package com.rrtv.mongo.tool.vo.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Classname DataBaseVo
 * @Description
 * @Date 2022/7/13 10:02
 * @Created by wangchangjiu
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataBaseVo implements Serializable {

    private Long connectionId;

    private String authorityKey;

    private List<String> databaseNames;

}
