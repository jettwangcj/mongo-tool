package com.rrtv.mongo.tool.vo.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Administrator
 */
@Data
public class FrontEndPagingRequest {

    @ApiModelProperty(value = "第几页;最小值:0", required = true)
    @NotNull
    @Min(0)
    private Integer page;

    @ApiModelProperty(value = "一页展示多少行;最小值:1", required = true)
    @NotNull
    @Min(1)
    private Integer size;

    @ApiModelProperty(value = "排序字段和升降序")
    @Valid
    private List<PagingSort.Order> orders;


    public FrontEndPagingRequest() {

        this(PagingRequest.Constants.PAGE, PagingRequest.Constants.SIZE, null);
    }

    public FrontEndPagingRequest(int page, int size) {

        this(page, size, null);
    }

    public FrontEndPagingRequest(int page, int size, List<PagingSort.Order> orders) {

        this.page = page;
        this.size = size;
        this.orders = orders;
    }

    public static class Constants {

        public static final Integer PAGE = 0;

        public static final Integer SIZE = 10;

        private Constants() {
        }
    }
}
