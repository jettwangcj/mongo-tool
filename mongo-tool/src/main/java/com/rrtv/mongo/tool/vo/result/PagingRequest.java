package com.rrtv.mongo.tool.vo.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Administrator
 */
@Data
public class PagingRequest {

    @ApiModelProperty(value = "第几页;最小值:0", required = true)
    private Integer page;

    @ApiModelProperty(value = "一页展示多少行;最小值:1", required = true)
    private Integer size;

    @ApiModelProperty(value = "排序条件")
    private PagingSort sort;


    public PagingRequest() {

        this(Constants.PAGE, Constants.SIZE, null);
    }

    public PagingRequest(int page, int size) {

        this(page, size, null);
    }

    public PagingRequest(int page, int size, PagingSort sort) {

        this.page = page;
        this.size = size;
        this.sort = sort;
    }

    public static class Constants {

        public static final Integer PAGE = 0;

        public static final Integer SIZE = 10;

        private Constants() {
        }
    }

    public static PagingRequest toPagingRequest(FrontEndPagingRequest frontEndPagingRequest) {
        PagingRequest pagingRequest = new PagingRequest();
        pagingRequest.setPage(frontEndPagingRequest.getPage());
        pagingRequest.setSize(frontEndPagingRequest.getSize());
        if (frontEndPagingRequest.getOrders() == null || frontEndPagingRequest.getOrders().isEmpty()) {

            return pagingRequest;
        }
        PagingSort pagingSort = new PagingSort();
        pagingSort.setOrders(frontEndPagingRequest.getOrders());
        pagingRequest.setSort(pagingSort);

        return pagingRequest;
    }
}
