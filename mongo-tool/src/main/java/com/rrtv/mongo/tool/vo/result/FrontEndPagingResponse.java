package com.rrtv.mongo.tool.vo.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Administrator
 */
@Data
public class FrontEndPagingResponse<T> {

    @ApiModelProperty(value = "页列表内容")
    private List<T> content;

    @ApiModelProperty(value = "总行数")
    private long totalElements;
}
