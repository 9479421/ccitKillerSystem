import Vue from "vue";
import Router from "vue-router";

Vue.use(Router);

export default new Router({
  mode: 'hash',
  routes: [
    {
      path: "/",
      name: "BasicLayout",
      hideInMenu: true,
      component: () => import("../layouts/BasicLayout.vue"),
      children: [
        {
          path: "/",
          redirect: "/ibs"
        },
        {
          path: "/ibs",
          name: "i博思作业一键满分",
          icon: "codepen-circle",
          component: () => import("../components/ibs")
        },
        {
          path: "/ibs_qd",
          name: "i博思自动签到",
          icon: "codepen-circle",
          component: () => import("../components/ibs_qd")
        },
        {
          path: "/card",
          name: "疫情自动打卡",
          icon: "ant-design",
          component: () => import("../components/card")
        },
        {
          path: "/qndxx",
          name: "自动青年大学习",
          icon: "vertical-align-middle",
          component: () => import("../components/qndxx")
        },
        {
          path: "/xyw",
          name: "校园网自动连接",
          icon: "align-right",
          component: () => import("../components/xyw")
        },
        {
          path: "/keep",
          name: "刷keep跑步",
          icon: "windows",
          component: () => import("../components/keep")
        },
        {
          path: "/search",
          name: "题斗罗",
          icon: "alibaba",
          component: () => import("../components/search")
        },
        {
          path: "/wk",
          name: "刷网课",
          icon: "codepen-circle",
          children:[     
            {
              isSub: true,
              path:'/swk/ibs',
              name:'i博思刷课',
              icon:"codepen-circle",
              component: () => import("../components/swk/ibs")
            },
            {
              isSub: true,
              path:'/swk/zjymooc',
              name:'职J云MOOC刷课',
              icon:"codepen-circle",
              component: () => import("../components/swk/zjymooc")
            },
            {
              isSub: true,
              path:'/swk/xxt',
              name:'学X通刷课',
              icon:"codepen-circle",
              component: () => import("../components/swk/xxt")
            },
            {
              isSub: true,
              path:'/swk/sxkt',
              name:'随行课堂',
              icon:"codepen-circle",
              component: () => import("../components/swk/sxkt")
            },
          ]
        }, 
      ]
    }
  ]
});

