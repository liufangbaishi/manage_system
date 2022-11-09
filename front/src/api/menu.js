// 查询用户列表
import request from '@/utils/request'

export function listMenu(data) {
  return request({
    url: '/auth/menu/getMenuList',
    method: 'post',
    data
  })
}
