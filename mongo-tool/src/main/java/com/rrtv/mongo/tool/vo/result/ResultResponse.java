package com.rrtv.mongo.tool.vo.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Administrator
 */
@Data
public final class ResultResponse<T> {

    @ApiModelProperty(value = "响应码 默认(正常: \"00000\") 参照《Java 开发手册》泰山版及其以后版本 错误码列表")
    private String code = Constants.SUCCESS_CODE;

    @ApiModelProperty(value = "响应信息")
    private String msg = Constants.SUCCESS_MESSAGE;

    @ApiModelProperty(value = "返回结果")
    private T data;

    private ResultResponse() {
    }

    private ResultResponse(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <R> ResultResponse<R> success() {

        return success(null);
    }

    public static <R> ResultResponse<R> success(R data) {

        return new ResultResponse<>(Constants.SUCCESS_CODE, Constants.SUCCESS_MESSAGE, data);
    }

    public static <R> ResultResponse<R> failure() {

        return failure(Constants.FAILURE_CODE, Constants.FAILURE_MESSAGE);
    }

    public static <R> ResultResponse<R> failure(String errorCode, String msg) {

        return new ResultResponse<>(errorCode, msg, null);
    }

    public static class Constants {

        public static final String SUCCESS_MESSAGE = "Success";

        public static final String SUCCESS_CODE = "00000";

        public static final String FAILURE_MESSAGE = "FAILURE";

        public static final String FAILURE_CODE = "B0001";

        private Constants() {
        }
    }
}
