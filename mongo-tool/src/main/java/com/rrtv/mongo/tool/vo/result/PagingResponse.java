package com.rrtv.mongo.tool.vo.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Administrator
 */
@Data
public class PagingResponse<T> {

    @ApiModelProperty(value = "页列表内容")
    private List<T> content;

    @ApiModelProperty(value = "分页信息")
    private PageableBean pageable;

    @ApiModelProperty(value = "最后一页")
    private boolean last;

    @ApiModelProperty(value = "总行数")
    private long totalElements;

    @ApiModelProperty(value = "总页数")
    private int totalPages;

    @ApiModelProperty(value = "当前页数")
    private int number;

    @ApiModelProperty(value = "一页行数")
    private int size;

    @ApiModelProperty(value = "第一页")
    private boolean first;

    @ApiModelProperty(value = "排序情况")
    private SortBean sort;

    @ApiModelProperty(value = "页实际行数")
    private int numberOfElements;


    @Data
    public static class PageableBean {

        @ApiModelProperty(value = "排序简要信息")
        private SortBean sort;

        @ApiModelProperty(value = "偏移行数")
        private long offset;

        @ApiModelProperty(value = "当前页数")
        private int pageNumber;

        @ApiModelProperty(value = "一页行数")
        private int pageSize;

        @ApiModelProperty(value = "未分页")
        private boolean unpaged;

        @ApiModelProperty(value = "已分页")
        private boolean paged;
    }

    @Data
    public static class SortBean {

        @ApiModelProperty(value = "未排序")
        private boolean unsorted;

        @ApiModelProperty(value = "已排序")
        private boolean sorted;
    }

    public static <T> PagingResponse<T> toPagingResponse(Page<T> page) {

        PagingResponse<T> pagingResponse = new PagingResponse<>();
        pagingResponse.setContent(page.getContent());
        pagingResponse.setLast(page.isLast());
        pagingResponse.setTotalElements(page.getTotalElements());
        pagingResponse.setTotalPages(page.getTotalPages());
        pagingResponse.setNumber(page.getNumber());
        pagingResponse.setSize(page.getSize());
        pagingResponse.setFirst(page.isFirst());
        pagingResponse.setNumberOfElements(page.getNumberOfElements());

        Pageable pageable = page.getPageable();

        SortBean sortBean = new SortBean();
        sortBean.setSorted(pageable.getSort().isSorted());
        sortBean.setUnsorted(pageable.getSort().isUnsorted());

        pagingResponse.setSort(sortBean);

        PageableBean pageableBean = new PageableBean();
        pageableBean.setSort(sortBean);

        pageableBean.setOffset(pageable.getOffset());
        pageableBean.setPageNumber(pageable.getPageNumber());
        pageableBean.setPageSize(pageable.getPageSize());
        pageableBean.setUnpaged(pageable.isUnpaged());
        pageableBean.setPaged(pageable.isPaged());

        pagingResponse.setPageable(pageableBean);

        return pagingResponse;
    }

}


