import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('@/views/Home/index.vue')
    },
    {
      path: '/live',
      name: 'live',
      component: () => import('@/views/Live/index.vue')
    },
    {
      path: '/mall',
      name: 'mall',
      component: () => import('@/views/Mall/index.vue')
    },
    {
      path: '/user',
      name: 'user',
      component: () => import('@/views/User/index.vue')
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/Login/index.vue')
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('@/views/Register/index.vue')
    },
    {
      path: '/content/list',
      name: 'contentList',
      component: () => import('@/views/ContentList/index.vue')
    },
    {
      path: '/content/publish',
      name: 'contentPublish',
      component: () => import('@/views/ContentPublish/index.vue')
    },
    {
      path: '/content/detail/:id',
      name: 'contentDetail',
      component: () => import('@/views/ContentDetail/index.vue'),
      props: true
    },
    {
      path: '/content/edit/:id',
      name: 'contentEdit',
      component: () => import('@/views/ContentEdit/index.vue'),
      props: true
    },
    {
      path: '/admin',
      name: 'admin',
      component: () => import('@/views/Admin/Dashboard.vue')
    },
    {
      path: '/messages',
      name: 'messages',
      component: () => import('@/views/Message/index.vue')
    },
    {
      path: '/invite',
      name: 'invite',
      component: () => import('@/views/Invite/Home.vue')
    },
    {
      path: '/stats',
      name: 'stats',
      component: () => import('@/views/Stats/Dashboard.vue')
    }
  ]
})

export default router
