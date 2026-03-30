<template>
  <div class="invite-home">
    <van-nav-bar title="邀请好友" />
    
    <!-- 邀请卡片 -->
    <van-card
      class="invite-card"
      :thumb="inviteImage"
      title="邀请好友赚积分"
      desc="每邀请一位好友注册，即可获得 100 积分"
    >
      <template #footer>
        <van-button type="primary" @click="copyInviteCode">复制邀请码</van-button>
      </template>
    </van-card>

    <!-- 邀请码展示 -->
    <van-cell-group title="我的邀请码" class="mt-16">
      <van-cell center>
        <template #title>
          <span class="invite-code">{{ inviteCode }}</span>
        </template>
        <template #right-icon>
          <van-button size="small" @click="copyInviteCode">复制</van-button>
        </template>
      </van-cell>
    </van-cell-group>

    <!-- 邀请记录 -->
    <van-cell-group title="邀请记录" class="mt-16">
      <van-list
        v-model:loading="loading"
        :finished="finished"
        @load="loadRecords"
      >
        <van-cell
          v-for="record in records"
          :key="record.id"
          :title="`邀请人：${record.inviteeUsername}`"
          :label="`时间：${formatTime(record.createdAt)}`"
          :value="`+${record.points} 积分`"
        />
      </van-list>
    </van-cell-group>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getInviteCode, getInviteRecords } from '@/api/invite'
import { showToast } from 'vant'

const inviteCode = ref('')
const inviteImage = 'https://placeholder.co/200x200?text=Invite'
const records = ref([])
const loading = ref(false)
const finished = ref(false)

onMounted(async () => {
  const data = await getInviteCode()
  inviteCode.value = data.code
})

const loadRecords = async () => {
  const data = await getInviteRecords()
  records.value = [...records.value, ...data.list]
  loading.value = false
  finished.value = data.finished
}

const copyInviteCode = () => {
  navigator.clipboard.writeText(inviteCode.value)
  showToast('邀请码已复制')
}

const formatTime = (time: string) => {
  return new Date(time).toLocaleString('zh-CN')
}
</script>

<style scoped>
.invite-card {
  margin: 16px;
}

.mt-16 {
  margin-top: 16px;
}

.invite-code {
  font-size: 24px;
  font-weight: bold;
  color: #07c160;
  letter-spacing: 4px;
}
</style>
