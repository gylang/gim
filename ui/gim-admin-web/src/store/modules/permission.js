import {constantRoutes} from '@/router'
import {getRouters} from '@/api/menu'
import Layout from '@/layout/index'
import ParentView from '@/components/ParentView';

const json = [
  {
    "name": "System",
    "path": "/system",
    "hidden": false,
    "redirect": "noRedirect",
    "component": "Layout",
    "alwaysShow": true,
    "meta": {
      "title": "系统管理",
      "icon": "system",
      "noCache": false
    },
    "children": [
      {
        "name": "User",
        "path": "user",
        "hidden": false,
        "component": "system/user/index",
        "meta": {
          "title": "用户管理",
          "icon": "user",
          "noCache": false
        }
      },
      {
        "name": "Role",
        "path": "role",
        "hidden": false,
        "component": "system/role/index",
        "meta": {
          "title": "角色管理",
          "icon": "peoples",
          "noCache": false
        }
      },
      {
        "name": "Menu",
        "path": "menu",
        "hidden": false,
        "component": "system/menu/index",
        "meta": {
          "title": "菜单管理",
          "icon": "tree-table",
          "noCache": false
        }
      },
      {
        "name": "Dept",
        "path": "dept",
        "hidden": false,
        "component": "system/dept/index",
        "meta": {
          "title": "部门管理",
          "icon": "tree",
          "noCache": false
        }
      },
      {
        "name": "Post",
        "path": "post",
        "hidden": false,
        "component": "system/post/index",
        "meta": {
          "title": "岗位管理",
          "icon": "post",
          "noCache": false
        }
      },
      {
        "name": "Dict",
        "path": "dict",
        "hidden": false,
        "component": "system/dict/index",
        "meta": {
          "title": "字典管理",
          "icon": "dict",
          "noCache": false
        }
      },
      {
        "name": "Config",
        "path": "config",
        "hidden": false,
        "component": "system/config/index",
        "meta": {
          "title": "参数设置",
          "icon": "edit",
          "noCache": false
        }
      },
      {
        "name": "Notice",
        "path": "notice",
        "hidden": false,
        "component": "system/notice/index",
        "meta": {
          "title": "通知公告",
          "icon": "message",
          "noCache": false
        }
      },
      {
        "name": "Log",
        "path": "log",
        "hidden": false,
        "redirect": "noRedirect",
        "component": "ParentView",
        "alwaysShow": true,
        "meta": {
          "title": "日志管理",
          "icon": "log",
          "noCache": false
        },
        "children": [
          {
            "name": "Operlog",
            "path": "operlog",
            "hidden": false,
            "component": "monitor/operlog/index",
            "meta": {
              "title": "操作日志",
              "icon": "form",
              "noCache": false
            }
          },
          {
            "name": "Logininfor",
            "path": "logininfor",
            "hidden": false,
            "component": "monitor/logininfor/index",
            "meta": {
              "title": "登录日志",
              "icon": "logininfor",
              "noCache": false
            }
          }
        ]
      }
    ]
  },
  {
    "name": "Monitor",
    "path": "/monitor",
    "hidden": false,
    "redirect": "noRedirect",
    "component": "Layout",
    "alwaysShow": true,
    "meta": {
      "title": "系统监控",
      "icon": "monitor",
      "noCache": false
    },
    "children": [
      {
        "name": "Online",
        "path": "online",
        "hidden": false,
        "component": "monitor/online/index",
        "meta": {
          "title": "在线用户",
          "icon": "online",
          "noCache": false
        }
      },
      {
        "name": "Job",
        "path": "job",
        "hidden": false,
        "component": "monitor/job/index",
        "meta": {
          "title": "定时任务",
          "icon": "job",
          "noCache": false
        }
      },
      {
        "name": "Druid",
        "path": "druid",
        "hidden": false,
        "component": "monitor/druid/index",
        "meta": {
          "title": "数据监控",
          "icon": "druid",
          "noCache": false
        }
      },
      {
        "name": "Server",
        "path": "server",
        "hidden": false,
        "component": "monitor/server/index",
        "meta": {
          "title": "服务监控",
          "icon": "server",
          "noCache": false
        }
      },
      {
        "name": "Cache",
        "path": "cache",
        "hidden": false,
        "component": "monitor/cache/index",
        "meta": {
          "title": "缓存监控",
          "icon": "redis",
          "noCache": false
        }
      }
    ]
  },
  {
    "name": "Tool",
    "path": "/tool",
    "hidden": false,
    "redirect": "noRedirect",
    "component": "Layout",
    "alwaysShow": true,
    "meta": {
      "title": "系统工具",
      "icon": "tool",
      "noCache": false
    },
    "children": [
      {
        "name": "Build",
        "path": "build",
        "hidden": false,
        "component": "tool/build/index",
        "meta": {
          "title": "表单构建",
          "icon": "build",
          "noCache": false
        }
      },
      {
        "name": "Gen",
        "path": "gen",
        "hidden": false,
        "component": "tool/gen/index",
        "meta": {
          "title": "代码生成",
          "icon": "code",
          "noCache": false
        }
      },
      {
        "name": "Swagger",
        "path": "swagger",
        "hidden": false,
        "component": "tool/swagger/index",
        "meta": {
          "title": "系统接口",
          "icon": "swagger",
          "noCache": false
        }
      }
    ]
  },
  {
    "name": "Http://ruoyi.vip",
    "path": "http://ruoyi.vip",
    "hidden": false,
    "component": "Layout",
    "meta": {
      "title": "若依官网",
      "icon": "guide",
      "noCache": false
    }
  }
];

const permission = {
  state: {
    routes: [],
    addRoutes: [],
    defaultRoutes: [],
    topbarRouters: [],
    sidebarRouters: []
  },
  mutations: {
    SET_ROUTES: (state, routes) => {
      state.addRoutes = routes
      state.routes = constantRoutes.concat(routes)
    },
    SET_DEFAULT_ROUTES: (state, routes) => {
      state.defaultRoutes = constantRoutes.concat(routes)
    },
    SET_TOPBAR_ROUTES: (state, routes) => {
      // 顶部导航菜单默认添加统计报表栏指向首页
      const index = [{
        path: 'index',
        meta: {title: '统计报表', icon: 'dashboard'}
      }]
      state.topbarRouters = routes.concat(index);
    },
    SET_SIDEBAR_ROUTERS: (state, routes) => {
      state.sidebarRouters = routes
    },
  },
  actions: {
    // 生成路由
    GenerateRoutes({commit}) {
      return new Promise(resolve => {
        // 向后端请求路由数据
        const sdata = JSON.parse(JSON.stringify(json))
        const rdata = JSON.parse(JSON.stringify(json))
        const sidebarRoutes = filterAsyncRouter(sdata)
        const rewriteRoutes = filterAsyncRouter(rdata, false, true)
        rewriteRoutes.push({path: '*', redirect: '/404', hidden: true})
        commit('SET_ROUTES', rewriteRoutes)
        commit('SET_SIDEBAR_ROUTERS', constantRoutes.concat(sidebarRoutes))
        commit('SET_DEFAULT_ROUTES', sidebarRoutes)
        commit('SET_TOPBAR_ROUTES', sidebarRoutes)
        resolve(rewriteRoutes)
      })
    }

  }
}

// 遍历后台传来的路由字符串，转换为组件对象
function filterAsyncRouter(asyncRouterMap, lastRouter = false, type = false) {
  return asyncRouterMap.filter(route => {
    if (type && route.children) {
      route.children = filterChildren(route.children)
    }
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

function filterChildren(childrenMap, lastRouter = false) {
  var children = []
  childrenMap.forEach((el, index) => {
    if (el.children && el.children.length) {
      if (el.component === 'ParentView') {
        el.children.forEach(c => {
          c.path = el.path + '/' + c.path
          if (c.children && c.children.length) {
            children = children.concat(filterChildren(c.children, c))
            return
          }
          children.push(c)
        })
        return
      }
    }
    if (lastRouter) {
      el.path = lastRouter.path + '/' + el.path
    }
    children = children.concat(el)
  })
  return children
}

export const loadView = (view) => { // 路由懒加载
  return (resolve) => require([`@/views/${view}`], resolve)
}

export default permission
