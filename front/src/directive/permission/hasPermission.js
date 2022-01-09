import store from '@/store'

export default {
  inserted(el, binding, vnode) {
    const { value } = binding
    const allPermission = '*:*:*'
    const permission = store.getters && store.getters.permissions

    if (value && value instanceof Array && value.length > 0) {
      const permissionFlag = value
      const hasPerm = permission.some(permission => {
        return permissionFlag.includes(permission) || allPermission === permission
      })
      // 没有权限
      if (!hasPerm) {
        // 移除
        el.parentNode && el.parentNode.removeChild(el)
        // 禁用
        // el.disabled = true
        // el.classList.add('is-disabled')
      }
    } else {
      throw new Error(`请设置操作权限标签值`)
    }
  }
}
