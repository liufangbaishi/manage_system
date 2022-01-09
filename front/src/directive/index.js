import hasRole from './permission/hasRole'
import hasPermission from './permission/hasPermission'

const install = function(Vue) {
  Vue.directive('hasRole', hasRole)
  Vue.directive('hasPermission', hasPermission)
}

if (window.Vue) {
  window['hasRole'] = hasRole
  window['hasPermission'] = hasPermission
  Vue.use(install); // eslint-disable-line
}

export default install
