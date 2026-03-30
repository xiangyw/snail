<template>
  <div class="home-page">
    <van-nav-bar title="蜗牛" />
    
    <!-- 搜索栏 -->
    <van-search
      v-model="searchValue"
      placeholder="搜索商品/直播"
      shape="round"
      @search="onSearch"
    />

    <!-- 公告 -->
    <van-notice-bar
      v-if="notice"
      :text="notice"
      left-icon="volume-o"
      mode="closeable"
      class="notice-bar"
    />

    <!-- 功能入口 -->
    <van-grid :column-num="4" :border="false" class="feature-grid">
      <van-grid-item icon="todo-list-o" text="任务" to="/tasks" />
      <van-grid-item icon="photo-o" text="旅游" />
      <van-grid-item icon="friends-o" text="广场舞" />
      <van-grid-item icon="cluster-o" text="更多" to="/more" />
    </van-grid>

    <!-- 推荐商品 -->
    <div class="section">
      <div class="section-title">精选推荐</div>
      <van-skeleton :row="3" :loading="loading">
        <van-list
          v-model:loading="loading"
          :finished="finished"
          finished-text="没有更多了"
          @load="onLoad"
        >
          <van-card
            v-for="item in products"
            :key="item.id"
            :price="item.price"
            :title="item.title"
            :thumb="item.image"
            @click="goToDetail(item.id)"
          />
        </van-list>
      </van-skeleton>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'

const router = useRouter()

const searchValue = ref('')
const notice = ref('欢迎来到蜗牛 H5 应用！')
const loading = ref(false)
const finished = ref(false)
const products = ref<any[]>([])

const onSearch = () => {
  if (searchValue.value) {
    router.push({ path: '/search', query: { q: searchValue.value } })
  }
}

const onLoad = () => {
  // TODO: 调用 API 加载商品列表
  setTimeout(() => {
    products.value.push(
      { id: 1, title: '测试商品 1', price: '99.00', image: 'https://placeholder.co/100' },
      { id: 2, title: '测试商品 2', price: '199.00', image: 'https://placeholder.co/100' }
    )
    loading.value = false
    if (products.value.length >= 10) {
      finished.value = true
    }
  }, 1000)
}

const goToDetail = (id: number) => {
  router.push(`/product/${id}`)
}
</script>

<style scoped>
.home-page {
  padding-bottom: 50px;
}

.notice-bar {
  margin: 8px;
}

.feature-grid {
  padding: 16px 0;
  background: #fff;
}

.section {
  background: #fff;
  margin-top: 8px;
  padding: 16px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 12px;
}
</style>
