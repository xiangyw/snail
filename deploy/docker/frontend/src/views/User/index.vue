<template>
  <div class="user-page">
    <van-nav-bar title="我的" />
    <van-cell-group inset>
      <van-cell title="登录/注册" is-link to="/login" />
    </van-cell-group>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped>
.user-page {
  min-height: 100vh;
  background: #f7f8fa;
}
</style>
