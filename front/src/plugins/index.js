import tab from '@/plugins/tab'
import modal from '@/plugins/modal'

export default {
  install(Vue) {
    // 页签操作
    Vue.prototype.$tab = tab
    // 模态框对象
    Vue.prototype.$modal = modal
  }
}
