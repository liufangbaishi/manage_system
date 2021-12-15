import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/auth/auth/login',
    method: 'post',
    data
  })
}
export function getCaptcha() {
  return request({
    url: '/auth/auth/getCaptcha',
    method: 'get'
  })
}

export function getInfo(token) {
  return request({
    url: '/auth/auth/getCurrentUser',
    method: 'get'
  })
}

export function getNav(token) {
  return request({
    url: '/auth/auth/nav',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: '/vue-admin-template/user/logout',
    method: 'post'
  })
}
