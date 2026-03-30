<template>
  <div class="login-page">
    <van-nav-bar title="登录" left-arrow @click-left="$router.back()" />
    
    <div class="form-container">
      <van-form @submit="onSubmit">
        <van-field
          v-model="phone"
          name="phone"
          placeholder="请输入手机号"
          :rules="[{ required: true, message: '请填写手机号' }]"
        />
        <van-field
          v-model="code"
          center
          clearable
          placeholder="请输入验证码"
          :rules="[{ required: true, message: '请填写验证码' }]"
        >
          <template #button>
            <van-button size="small" type="primary" @click="sendCode">
              发送验证码
            </van-button>
          </template>
        </van-field>
        <div style="margin: 16px;">
          <van-button round block type="primary" native-type="submit">
            登录
          </van-button>
        </div>
      </van-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'

const router = useRouter()
const phone = ref('')
const code = ref('')

const sendCode = () => {
  if (!phone.value) {
    showToast('请输入手机号')
    return
  }
  showToast('验证码已发送')
}

const onSubmit = () => {
  showToast('登录成功')
  router.back()
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  background: #fff;
}

.form-container {
  padding: 24px 16px;
}
</style>
