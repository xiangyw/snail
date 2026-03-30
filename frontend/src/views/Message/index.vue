<template>
  <div class="message-list">
    <van-nav-bar title="消息中心" left-arrow @click-left="$router.back()" />
    
    <!-- 消息分类 -->
    <van-tabs v-model:active="activeTab">
      <van-tab title="全部">
        <MessageList :type="'all'" />
      </van-tab>
      <van-tab title="未读">
        <MessageList :type="'unread'" />
      </van-tab>
      <van-tab title="系统">
        <MessageList :type="'system'" />
      </van-tab>
    </van-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import MessageList from '@/components/MessageList.vue'

const activeTab = ref(0)
</script>

<style scoped>
.message-list {
  min-height: 100vh;
  background: #f7f8fa;
}
</style>
