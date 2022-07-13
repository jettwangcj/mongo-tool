import http from '@/http'

export function getDataBases(params) {
  return http({
    url: '/connection/database',
    method: 'get',
    params
  })
}

export function getAllConnection() {
  return http({
    url: '/connection/list',
    method: 'get',
  })
}

// 获取所有表名称
export function getTreeData(params) {
  return http({
    url: '/query/tables',
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
