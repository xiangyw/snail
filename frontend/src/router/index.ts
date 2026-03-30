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
    }
  ]
})

export default router
