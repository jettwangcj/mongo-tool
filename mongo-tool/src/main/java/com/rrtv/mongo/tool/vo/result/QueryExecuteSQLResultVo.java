package com.rrtv.mongo.tool.vo.result;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class QueryExecuteSQLResultVo extends ExecuteSQLResultVo {

    private List<Map> data;

    private List<String> columns;

    public static QueryExecuteSQLResultVo success(List<String> columns, List<Map> data){
        QueryExecuteSQLResultVo resultVo = new QueryExecuteSQLResultVo();
        resultVo.setSuccess(true);
        resultVo.setColumns(columns);
        resultVo.setData(data);
        resultVo.setSqlType(1);
        return resultVo;
    }

    public static QueryExecuteSQLResultVo fail(String failMessage){
        QueryExecuteSQLResultVo resultVo = new QueryExecuteSQLResultVo();
        resultVo.setSuccess(false);
        resultVo.setFailMessage(failMessage);
        resultVo.setSqlType(1);
        return resultVo;
    }

}
