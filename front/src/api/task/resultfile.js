import request from '@/utils/request'

// 查询结果集列表
export function listResultfile(query) {
  return request({
    url: '/task/resultfile/list',
    method: 'get',
    params: query
  })
}

// 查询结果集详细
export function getResultfile(fileId) {
  return request({
    url: '/task/resultfile/' + fileId,
    method: 'get'
  })
}

// 新增结果集
export function addResultfile(data) {
  return request({
    url: '/task/resultfile',
    method: 'post',
    data: data
  })
}

// 修改结果集
export function updateResultfile(data) {
  return request({
    url: '/task/resultfile',
    method: 'put',
    data: data
  })
}

// 删除结果集
export function delResultfile(fileId) {
  return request({
    url: '/task/resultfile/' + fileId,
    method: 'delete'
  })
}
