import { config } from '@vue/test-utils'
import { createPinia } from 'pinia'
import { createRouter, createWebHistory } from 'vue-router'
import { vi } from 'vitest'

// 全局注册 Pinia
const pinia = createPinia()
config.global.plugins.push(pinia)

// 全局注册 Vue Router
const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', component: { template: '<div>Home</div>' } },
    { path: '/login', component: { template: '<div>Login</div>' } },
    { path: '/mall', component: { template: '<div>Mall</div>' } },
    { path: '/tasks', component: { template: '<div>Tasks</div>' } },
    { path: '/product/:id', component: { template: '<div>Product Detail</div>' } },
    { path: '/search', component: { template: '<div>Search</div>' } },
    { path: '/more', component: { template: '<div>More</div>' } },
  ],
})
config.global.plugins.push(router)

// Mock Vant 组件
config.global.stubs = {
  VanNavBar: { template: '<div class="van-nav-bar"><slot></slot></div>' },
  VanSearch: { template: '<div class="van-search"><slot></slot></div>' },
  VanNoticeBar: { template: '<div class="van-notice-bar"><slot></slot></div>' },
  VanGrid: { template: '<div class="van-grid"><slot></slot></div>' },
  VanGridItem: { template: '<div class="van-grid-item"><slot></slot></div>' },
  VanSkeleton: { template: '<div class="van-skeleton"><slot></slot></div>' },
  VanList: { template: '<div class="van-list"><slot></slot></div>' },
  VanCard: { template: '<div class="van-card"><slot></slot></div>' },
  VanForm: { template: '<form class="van-form"><slot></slot></form>' },
  VanField: { template: '<div class="van-field"><slot></slot></div>' },
  VanButton: { template: '<button class="van-button"><slot></slot></button>' },
  VanEmpty: { template: '<div class="van-empty"><slot></slot></div>' },
  VanTabbar: { template: '<div class="van-tabbar"><slot></slot></div>' },
  VanTabbarItem: { template: '<div class="van-tabbar-item"><slot></slot></div>' },
  VanSwipe: { template: '<div class="van-swipe"><slot></slot></div>' },
  VanSwipeItem: { template: '<div class="van-swipe-item"><slot></slot></div>' },
}

// Mock showToast
vi.mock('vant', async () => {
  const actual = await vi.importActual('vant')
  return {
    ...actual,
    showToast: vi.fn(),
    showSuccessToast: vi.fn(),
    showFailToast: vi.fn(),
    showLoadingToast: vi.fn(),
  }
})
