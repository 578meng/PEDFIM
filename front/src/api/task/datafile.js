import request from '@/utils/request'

// 查询数据集列表
export function listDatafile(query) {
  return request({
    url: '/task/datafile/list',
    method: 'get',
    params: query
  })
}

// 查询数据集详细
export function getDatafile(fileId) {
  return request({
    url: '/task/datafile/' + fileId,
    method: 'get'
  })
}

// 新增数据集
export function addDatafile(data) {
  return request({
    url: '/task/datafile',
    method: 'post',
    data: data
  })
}

// 修改数据集
export function updateDatafile(data) {
  return request({
    url: '/task/datafile',
    method: 'put',
    data: data
  })
}

// 删除数据集
export function delDatafile(fileId) {
  return request({
    url: '/task/datafile/' + fileId,
    method: 'delete'
  })
}
