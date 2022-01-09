# vue-admin-template

> 这是一个极简的 vue admin 管理后台。

## Build Setup

```bash
# 克隆项目
git clone https://github.com/PanJiaChen/vue-admin-template.git

# 进入项目目录
cd vue-admin-template

# 安装依赖
npm install

# 建议不要直接使用 cnpm 安装以来，会有各种诡异的 bug。可以通过如下操作解决 npm 下载速度慢的问题
npm install --registry=https://registry.npm.taobao.org

# 启动服务
npm run dev
```

浏览器访问 [http://localhost:9528](http://localhost:9528)

## 发布

```bash
# 构建测试环境
npm run build:stage

# 构建生产环境
npm run build:prod
```

## 部署
### 1. 前端请求
```js
url: /auth/auth/getCaptcha
这里的第一个auth相当于baseURL，在nginx的配置中
proxy_pass http://manage/;
将请求http://192.168.0.108/auth/auth/getCaptcha
转换为http://192.168.0.102:8080/auth/getCaptcha
proxy_pass http://manage/manage/;
将请求http://192.168.0.108/auth/auth/getCaptcha
转换为http://192.168.0.102:8080/manage/auth/getCaptcha
```
### 2. nginx配置
```js
// 后端地址
 upstream manage {
  server 192.168.0.102:8080;
}
server {
  listen       80;
  server_name  localhost;

  #charset koi8-r;

  #access_log  logs/host.access.log  main;

  location / {
    root   /app/dist;
    try_files $uri $uri/ /index.html;
    index  index.html index.htm;
  }

  location /auth/ {
    proxy_set_header Host $http_host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header REMOTE-HOST $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    // proxy_pass http://manage/;    // 这里manage后面的/不能去掉，如果去掉了后端请求地址就会不对
    proxy_pass http://manage/manage/; // 如果后端有context-path写在这里
  }
}
```

## 学习
#### 1. 路由守卫
```js
beforeEach 首先根据token判断，跳转页面

router.beforeEach(async(to, from, next) => {
  const hasToken = getToken()
  if (hasToken) {
    next({ path: '/' })
  } else {
    next()
  }
})
```

#### 2. 公共方法
2.1 在utils中写一个公共方法，例如：
```js
export function test() {
  return 'a'
}
```
> 1. 常规使用
>  - 在需要调用的文件中引入 import { test } from '@/utils'
>  - 在代码中直接调用 test()

> 2. prototype
> - 在main.js中引入 import { parseTime } from '@/utils'
> - 在main.js中 Vue.prototype.parseTime = parseTime
> - 在代码中调用 this.parseTime()

2.2 全局常量
```js
定义
// 用户状态
export const statusDict = [
  { label: '正常', value: '0' },
  { label: '停用', value: '1' }
]
// 性别
export const sexDict = [
  { label: '男', value: '0' },
  { label: '女', value: '1' },
  { label: '未知', value: '2' }
]
使用
import { sexDict, statusDict } from '@/utils/consts'
data() {
  return {
    // 下拉框
    statusDict: statusDict,
    sexDict: sexDict,
  }
}
在template中使用statusDict，sexDict
```

#### 3. 全局组件
> 1. 常规使用
>  - 在需要调用的文件中引入 import
>  - 在components中添加
>  - 在template代码中使用

> 2. Vue.component
> - 在main.js中引入 import RightToolbar from '@/components/RightToolbar'
> - 在main.js中 Vue.component('RightToolbar', RightToolbar) // 挂载组件
> - 在代码中调用 <right-toolbar />

>3. 封装提示框
>  - 在main.js中引入 import modal from '@/plugins/modal'
>  - Vue.prototype.$modal = modal
>  - 在代码中调用 this.$modal.xxx()

#### 4. 自定义指令directive
判断权限
> - 在main.js中引入
> - 在@/directive/permission/hasPermission.js中写入逻辑
> - Vue.directive('hasPermission', hasPermission)

#### 5. store vuex状态管理
1. state： 变量定义 
2. mounted：给变量set值
3. actions：提供给外部的方法
```js
// 在actions中调用mounted的方法
commit('RESET_STATE', 参数)
// 在actions中调用actions的方法
dispatch('addVisitedView', 参数)
// 外部调用actions的方法
this.$store.dispatch('tagsView/addView', 参数)
```
4. export
```js
export default {
  namespaced: true,
  state,
  mutations,
  actions
}
// index.js
const store = new Vuex.Store({
  modules: {
    app,
    permission,
    tagsView,
    settings,
    user
  },
  getters
})
```
