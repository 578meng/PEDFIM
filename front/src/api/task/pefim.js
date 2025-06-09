import request from '@/utils/request'

// 查询PEFIM列表
export function listPefim(query) {
  return request({
    url: '/task/pefim/list',
    method: 'get',
    params: query
  })
}

// 查询PEFIM详细
export function getPefim(taskId) {
  return request({
    url: '/task/pefim/' + taskId,
    method: 'get'
  })
}

// 新增PEFIM
export function addPefim(data) {
  return request({
    url: '/task/pefim',
    method: 'post',
    data: data
  })
}

// 修改PEFIM
export function updatePefim(data) {
  return request({
    url: '/task/pefim',
    method: 'put',
    data: data
  })
}

// 删除PEFIM
export function delPefim(taskId) {
  return request({
    url: '/task/pefim/' + taskId,
    method: 'delete'
  })
}
