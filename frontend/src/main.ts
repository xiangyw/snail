import { createApp } from 'vue'
import { createPinia } from 'pinia'

// Vant 样式
import 'vant/lib/index.css'

// 全局样式
import './assets/styles/main.css'

import App from './App.vue'
import router from './router'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)

app.mount('#app')
