package com.rrtv.mongo.tool.vo.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.domain.Sort;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
@Data
public class PagingSort {

    @ApiModelProperty(value = "排序字段和升降序")
    @Valid
    private List<Order> orders;

    @Data
    public static class Order {

        @ApiModelProperty(value = "排序方向", required = true)
        @NotNull
        private PagingSort.Direction direction;

        @ApiModelProperty(value = "排序字段", required = true)
        @NotNull
        @NotBlank
        private String property;

    }

    public enum Direction {
        /**
         * 升序
         */
        ASC,
        /**
         * 降序
         */
        DESC
    }

    public static Sort toSpringSort(PagingSort pagingSort) {

        if (pagingSort == null) {

            return Sort.unsorted();
        }
        if (pagingSort.orders == null || pagingSort.orders.isEmpty()) {

            return Sort.unsorted();
        }

        List<Sort.Order> orders = new ArrayList<>();
        for (PagingSort.Order item : pagingSort.orders) {

            Sort.Order sortOrder = new Sort.Order(Sort.Direction.fromString(item.direction.name()), item.property);
            orders.add(sortOrder);
        }

        return Sort.by(orders);
    }
}
