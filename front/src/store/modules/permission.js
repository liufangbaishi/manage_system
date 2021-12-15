import { constantRoutes } from '@/router'
import { getNav } from '@/api/user'
import Layout from '@/layout/index'
import ParentView from '@/components/ParentView'

const state = {
  permissions: [],
  routes: [],
  addRoutes: []
}

const mutations = {
  SET_PERMISSIONS: (state, permissions) => {
    state.permissions = permissions
  },
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
  }
}

const actions = {
  generateRoutes({ commit }, roles) {
    return new Promise(resolve => {
      // 向后端请求路由数据
      getNav().then(res => {
        const { data } = res
        const sdata = JSON.parse(JSON.stringify(data.nav))
        const sidebarRoutes = filterAsyncRouter(sdata)
        sidebarRoutes.push({ path: '*', redirect: '/404', hidden: true })
        commit('SET_PERMISSIONS', data.authorities)
        commit('SET_ROUTES', sidebarRoutes)
        resolve(sidebarRoutes)
      })
    })
  }
}

// 遍历后台传来的路由字符串，转换为组件对象
function filterAsyncRouter(asyncRouterMap, lastRouter = false, type = false) {
  return asyncRouterMap.filter(route => {
    route.meta = { title: route.menuName, icon: route.icon }
    if (route.component) {
      // Layout ParentView 组件特殊处理
      if (route.component === 'Layout') {
        route.component = Layout
      } else if (route.component === 'ParentView') {
        route.component = ParentView
      } else {
        route.component = loadView(route.component)
      }
    }
    if (route.children != null && route.children && route.children.length) {
      route.children = filterAsyncRouter(route.children, route, type)
    } else {
      delete route['children']
      delete route['redirect']
    }
    return true
  })
}

export const loadView = (view) => {
  return (resolve) => require([`@/views/${view}`], resolve)
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
