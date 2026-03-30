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
    { path: '/tasks/:id', component: { template: '<div>Task Detail</div>' } },
  ],
})
config.global.plugins.push(router)

// Mock Vant 组件 - 使用全局 stubs
config.global.stubs = {
  VanNavBar: {
    template: '<div class="van-nav-bar"><slot></slot><slot name="left"></slot><slot name="right"></slot></div>',
    props: ['title', 'leftArrow', 'fixed', 'placeholder', 'border'],
  },
  VanSearch: {
    template: '<div class="van-search"><input type="text" :placeholder="placeholder" :value="modelValue" @input="$emit(\'update:modelValue\', $event.target.value)"/><slot></slot></div>',
    props: ['modelValue', 'placeholder', 'shape', 'showAction'],
    emits: ['update:modelValue', 'search'],
  },
  VanNoticeBar: {
    template: '<div class="van-notice-bar"><slot></slot>{{ text }}</div>',
    props: ['text', 'mode', 'leftIcon'],
  },
  VanGrid: {
    template: '<div class="van-grid"><slot></slot></div>',
    props: ['columnNum', 'border'],
  },
  VanGridItem: {
    template: '<div class="van-grid-item"><slot></slot>{{ text }}</div>',
    props: ['icon', 'text', 'to'],
  },
  VanSkeleton: {
    template: '<div class="van-skeleton"><slot v-if="!loading"></slot><div v-else class="skeleton-placeholder">Loading...</div></div>',
    props: ['loading', 'row'],
  },
  VanList: {
    template: '<div class="van-list"><slot></slot></div>',
    props: ['loading', 'finished', 'finishedText'],
    emits: ['load'],
  },
  VanCard: {
    template: '<div class="van-card"><slot></slot><div>{{ title }}</div><div>{{ price }}</div></div>',
    props: ['title', 'price', 'thumb'],
    emits: ['click'],
  },
  VanForm: {
    template: '<form class="van-form" @submit.prevent="$emit(\'submit\')"><slot></slot></form>',
    emits: ['submit'],
  },
  VanField: {
    template: '<div class="van-field"><input :placeholder="placeholder" :value="modelValue" @input="$emit(\'update:modelValue\', $event.target.value)"/><slot name="button"></slot></div>',
    props: ['modelValue', 'placeholder', 'name', 'rules', 'center', 'clearable'],
    emits: ['update:modelValue'],
  },
  VanButton: {
    template: '<button class="van-button" :type="type" :size="size" :nativeType="nativeType" @click="$emit(\'click\')"><slot></slot></button>',
    props: ['type', 'size', 'nativeType', 'round', 'block'],
    emits: ['click'],
  },
  VanEmpty: {
    template: '<div class="van-empty"><slot></slot><div class="van-empty__description">{{ description }}</div></div>',
    props: ['description', 'image'],
  },
  VanTabbar: {
    template: '<div class="van-tabbar"><slot></slot></div>',
  },
  VanTabbarItem: {
    template: '<div class="van-tabbar-item"><slot></slot></div>',
    props: ['icon', 'name'],
  },
  VanTabs: {
    template: '<div class="van-tabs"><div class="van-tabs__wrap"><slot name="nav"></slot></div><div class="van-tabs__content"><slot></slot></div></div>',
    props: ['modelValue'],
    emits: ['update:modelValue', 'change'],
  },
  VanTab: {
    template: '<div class="van-tab"><slot></slot>{{ title }}</div>',
    props: ['title', 'name'],
  },
  VanCell: {
    template: '<div class="van-cell"><div class="van-cell__title"><slot name="title">{{ title }}</slot></div><div class="van-cell__value"><slot name="right-icon"></slot><slot name="value">{{ value }}</slot></div></div>',
    props: ['title', 'label', 'value', 'isLink'],
    emits: ['click'],
  },
  VanTag: {
    template: '<span class="van-tag" :class="type"><slot></slot></span>',
    props: ['type'],
  },
  VanSwipe: {
    template: '<div class="van-swipe"><slot></slot></div>',
  },
  VanSwipeItem: {
    template: '<div class="van-swipe-item"><slot></slot></div>',
  },
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
