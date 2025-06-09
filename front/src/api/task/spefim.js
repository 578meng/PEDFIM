import request from '@/utils/request'

// 查询SPEFIM列表
export function listSpefim(query) {
  return request({
    url: '/task/spefim/list',
    method: 'get',
    params: query
  })
}

// 查询SPEFIM详细
export function getSpefim(taskId) {
  return request({
    url: '/task/spefim/' + taskId,
    method: 'get'
  })
}

// 新增SPEFIM
export function addSpefim(data) {
  return request({
    url: '/task/spefim',
    method: 'post',
    data: data
  })
}

// 修改SPEFIM
export function updateSpefim(data) {
  return request({
    url: '/task/spefim',
    method: 'put',
    data: data
  })
}

// 删除SPEFIM
export function delSpefim(taskId) {
  return request({
    url: '/task/spefim/' + taskId,
    method: 'delete'
  })
}
