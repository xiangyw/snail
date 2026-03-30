<template>
  <div class="admin-dashboard">
    <van-nav-bar title="后台管理" />
    
    <!-- 数据概览 -->
    <van-grid :column-num="4" :border="false" class="stats-grid">
      <van-grid-item :value="stats.userCount" text="用户数" icon="friends-o" />
      <van-grid-item :value="stats.contentCount" text="内容数" icon="photo-o" />
      <van-grid-item :value="stats.orderCount" text="订单数" icon="shopping-cart-o" />
      <van-grid-item :value="stats.revenue" text="收入" icon="money-o" />
    </van-grid>

    <!-- 用户管理 -->
    <van-cell-group title="用户管理" class="mt-16">
      <van-field
        v-model="searchUser"
        placeholder="搜索用户"
        right-icon="search"
        @search="searchUsers"
      />
      <van-list
        v-model:loading="userLoading"
        :finished="userFinished"
        @load="loadUsers"
      >
        <van-cell
          v-for="user in userList"
          :key="user.id"
          :title="user.username"
          :label="user.phone"
        >
          <template #right-icon>
            <van-tag :type="user.role === 'ADMIN' ? 'primary' : 'default'">
              {{ user.role }}
            </van-tag>
          </template>
        </van-cell>
      </van-list>
    </van-cell-group>

    <!-- 内容审核 -->
    <van-cell-group title="内容审核" class="mt-16">
      <van-list
        v-model:loading="contentLoading"
        :finished="contentFinished"
        @load="loadContents"
      >
        <van-cell
          v-for="content in contentList"
          :key="content.id"
          :title="content.title"
          :label="`作者：${content.author}`"
        >
          <template #right-icon>
            <div class="audit-actions">
              <van-button size="mini" type="success" @click="auditContent(content.id, 'PASS')">通过</van-button>
              <van-button size="mini" type="danger" @click="auditContent(content.id, 'REJECT')">拒绝</van-button>
            </div>
          </template>
        </van-cell>
      </van-list>
    </van-cell-group>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getUserList, auditContent, getStats } from '@/api/admin'

const stats = reactive({
  userCount: 0,
  contentCount: 0,
  orderCount: 0,
  revenue: 0
})

const searchUser = ref('')
const userList = ref([])
const userLoading = ref(false)
const userFinished = ref(false)

const contentList = ref([])
const contentLoading = ref(false)
const contentFinished = ref(false)

onMounted(async () => {
  const data = await getStats()
  stats.userCount = data.userCount
  stats.contentCount = data.contentCount
  stats.orderCount = data.orderCount
  stats.revenue = data.revenue
})

const loadUsers = async () => {
  const data = await getUserList()
  userList.value = [...userList.value, ...data.list]
  userLoading.value = false
  userFinished.value = data.finished
}

const searchUsers = async () => {
  userList.value = []
  userFinished.value = false
  loadUsers()
}

const loadContents = async () => {
  // TODO: 实现内容加载
  contentLoading.value = false
  contentFinished.value = true
}

const auditContent = async (id: number, action: string) => {
  await auditContent(id, action)
}
</script>

<style scoped>
.stats-grid {
  background: #fff;
}

.mt-16 {
  margin-top: 16px;
}

.audit-actions {
  display: flex;
  gap: 8px;
}
</style>
