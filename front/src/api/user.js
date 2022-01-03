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
export function getInfo() {
  return request({
    url: '/auth/auth/getCurrentUser',
    method: 'get'
  })
}
export function getNav() {
  return request({
    url: '/auth/auth/nav',
    method: 'get'
  })
}
export function logout() {
  return request({
    url: '/auth/auth/logout',
    method: 'post'
  })
}

// 更改用户自己信息
export function updateUserProfile(data) {
  return request({
    url: '/auth/user/updateSelf',
    method: 'post',
    data
  })
}

// 更改密码
export function updateUserPwd(data) {
  return request({
    url: '/auth/user/updateUserPwd',
    method: 'post',
    params: data
  })
}

// 修改用户头像
export function uploadAvatar(data) {
  return request({
    url: '/auth/user/uploadAvatar',
    method: 'post',
    data
  })
}

// 查询用户列表
export function listUser(data) {
  return request({
    url: '/auth/user/getUserList',
    method: 'post',
    data
  })
}

// 查询用户详细
export function getUser(userId) {
  return request({
    url: '/auth/user/getUser/' + userId,
    method: 'get'
  })
}

// 新增用户
export function addUser(data) {
  return request({
    url: '/auth/user/saveUser',
    method: 'post',
    data: data
  })
}

// 修改用户
export function updateUser(data) {
  return request({
    url: '/auth/user/updateUser',
    method: 'post',
    data: data
  })
}

// 删除用户  批量删除
export function delUser(userId) {
  return request({
    url: '/auth/user/delUser/' + userId,
    method: 'get'
  })
}

// 用户密码重置
export function resetUserPwd(userId, password) {
  const data = {
    userId,
    password
  }
  return request({
    url: '/auth/user/resetPwd',
    method: 'post',
    data
  })
}

// 用户状态修改
export function changeUserStatus(userId, status) {
  const data = {
    userId,
    status
  }
  return request({
    url: '/auth/user/changeStatus',
    method: 'post',
    data
  })
}

// 查询授权角色
export function getAuthRole(userId) {
  return request({
    url: '/auth/user/getRoleByUser/' + userId,
    method: 'get'
  })
}

// 保存授权角色
export function updateAuthRole(data) {
  return request({
    url: '/manage/user/authRole',
    method: 'post',
    data
  })
}
