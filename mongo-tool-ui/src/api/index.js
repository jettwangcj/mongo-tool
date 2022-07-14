import http from '@/http'

export function testConnection(params) {
    return http({
        url: '/connection/test',
        method: 'get',
        params
    })
}


export function createConnection(params) {
    return http({
        url: '/connection/create',
        method: 'post',
        params
    })
}

export function getDataBaseTrees(params) {
  return http({
    url: '/connection/trees',
    method: 'get',
    params
  })
}

export function getTableData(params) {
  return http({
    url: '/connection/table/data',
    method: 'get',
    params
  })
}


// 获取指定表的字段名
export function getFieldNames(params) {
  return http({
    url: '/query/field',
    method: 'get',
    params
  })
}

// 发送SQL语句
export function sendSQL(params) {
  return http({
    url: '/ext',
    method: 'post',
    params
  })
}
