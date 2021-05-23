import { createRouter, createWebHistory } from 'vue-router'
import routerAccount from '../libs/router-account'
import routerFx from '../libs/router-fx'
import routerGenerator from '../libs/router-generator'
import { store } from "../libs/store";

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../pages/Home.vue')
  },
  {
    path: '/error',
    name: 'Error',
    component: () => import('../pages/Error.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../pages/Login.vue')
  },
  {
    path: '/logout',
    name: 'Logout',
    component: () => import('../pages/Logout.vue'),
    beforeEnter: (to, from, next) => {
      if (from.name == null) {
        location.href = process.env.BASE_URL
        return
      }
      if (confirm('ログアウトしますか？')) {
        next()
      } else {
        next(false)
      }
    }
  },
  ...routerAccount,
  ...routerFx,
  ...routerGenerator
]

export const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

const nonAuthNameList = ["Home", "Error", "Login", "AccountCreate", "AccountActivation", "Logout"];

router.beforeEach((to, from, next) => {
  if (nonAuthNameList.indexOf(to.name) >= 0) {
    next();
  } else {
    if (store.getters.isLogin) {
      next();
    } else {
      alert("ログインしてください！");
      router.push("/login?ref=" + to.path);
    }
  }
});