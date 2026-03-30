<template>
  <van-config-provider :theme-vars="themeVars">
    <router-view v-slot="{ Component }">
      <transition name="fade" mode="out-in">
        <component :is="Component" />
      </transition>
    </router-view>
    <van-tabbar v-if="showTabbar" route>
      <van-tabbar-item to="/" icon="home-o">首页</van-tabbar-item>
      <van-tabbar-item to="/live" icon="play-circle-o">直播</van-tabbar-item>
      <van-tabbar-item to="/mall" icon="shopping-cart-o">商城</van-tabbar-item>
      <van-tabbar-item to="/user" icon="user-o">我的</van-tabbar-item>
    </van-tabbar>
  </van-config-provider>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

// 主题变量
const themeVars = {
  primaryColor: '#07c160',
  backgroundColor: '#f7f8fa'
}

// 是否显示底部标签栏
const showTabbar = computed(() => {
  const hideTabbarRoutes = ['/login', '/register']
  return !hideTabbarRoutes.includes(route.path)
})
</script>

<style>
/* 页面过渡动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* 安全区域适配 */
#app {
  padding-bottom: env(safe-area-inset-bottom);
}
</style>
