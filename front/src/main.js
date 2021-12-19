import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import locale from 'element-ui/lib/locale/lang/zh-CN' // 引入中文

import '@/styles/index.scss' // global css

import App from './App'
import store from './store'
import router from './router'
import plugins from './plugins' // plugins

import '@/icons' // icon
import '@/permission' // permission control

import RightToolbar from '@/components/RightToolbar' // 引用自定义全局组件
import { parseTime, addDateRange } from '@/utils' // 全局引用方法

Vue.use(plugins)
Vue.component('RightToolbar', RightToolbar) // 挂载组件
Vue.prototype.parseTime = parseTime
Vue.prototype.addDateRange = addDateRange

// set ElementUI lang to EN
Vue.use(ElementUI, { locale })

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
