# 🐌 Snail H5 App - 技术规格书

**版本**: 1.0  
**创建日期**: 2026-03-30  
**定位**: 移动端 H5 应用（非原生 App）

---

## 📱 产品定位

- **类型**: 移动端 H5 Web 应用
- **目标平台**: 移动浏览器 + 微信内置浏览器
- **分发方式**: URL 访问，可添加到主屏幕
- **技术路线**: PWA (渐进式 Web 应用)

---

## 🎯 核心优势

| 对比项 | H5 App | 原生 App |
|--------|--------|----------|
| 开发成本 | ✅ 低（一套代码） | ❌ 高（多端开发） |
| 更新方式 | ✅ 即时生效 | ❌ 需应用商店审核 |
| 分发渠道 | ✅ URL 即可 | ❌ 应用商店限制 |
| 功能访问 | ⚠️ 受限 | ✅ 完整系统 API |
| 性能 | ⚠️ 中等 | ✅ 最优 |

---

## 🛠️ 技术栈调整

### 前端 (H5 优先)

| 技术领域 | 选型 | 说明 |
|----------|------|------|
| 框架 | Vue 3 + Vite | 轻量快速 |
| 语言 | TypeScript | 类型安全 |
| UI 库 | Vant 4 | 移动端组件库 |
| 状态管理 | Pinia | 轻量状态管理 |
| 路由 | Vue Router 4 | 单页应用路由 |
| HTTP | Axios | API 请求 |
| PWA | vite-plugin-pwa | 离线缓存、主屏幕 |
| 适配 | postcss-px-to-viewport | 移动端适配 |
| 微信 SDK | weixin-js-sdk | 微信分享、支付 |

### 新增依赖

```json
{
  "dependencies": {
    "vant": "^4.8.0",
    "weixin-js-sdk": "^1.6.0",
    "@vant/use": "^1.6.0"
  },
  "devDependencies": {
    "vite-plugin-pwa": "^0.19.0",
    "workbox-window": "^7.0.0",
    "postcss-px-to-viewport": "^1.1.1",
    "autoprefixer": "^10.4.0",
    "postcss": "^8.4.0"
  }
}
```

---

## 📐 移动端适配方案

### 视口适配

```javascript
// vite.config.ts
import pxtoviewport from 'postcss-px-to-viewport'

export default {
  css: {
    postcss: {
      plugins: [
        pxtoviewport({
          viewportWidth: 375, // 设计稿宽度
          viewportUnit: 'vw',
          unitPrecision: 6,
          propList: ['*'],
        }),
      ],
    },
  },
}
```

### 响应式断点

```css
/* 移动端优先 */
.container {
  padding: 16px;
}

/* 平板 */
@media (min-width: 768px) {
  .container {
    max-width: 750px;
    margin: 0 auto;
  }
}

/* 桌面 */
@media (min-width: 1024px) {
  .container {
    max-width: 1200px;
  }
}
```

---

## 🔧 PWA 配置

### vite-plugin-pwa

```typescript
// vite.config.ts
import { VitePWA } from 'vite-plugin-pwa'

export default defineConfig({
  plugins: [
    VitePWA({
      registerType: 'autoUpdate',
      includeAssets: ['favicon.ico', 'apple-touch-icon.png'],
      manifest: {
        name: '蜗牛',
        short_name: '蜗牛',
        description: '蜗牛 H5 应用',
        theme_color: '#ffffff',
        background_color: '#ffffff',
        display: 'standalone',
        scope: '/',
        start_url: '/',
        icons: [
          {
            src: 'pwa-192x192.png',
            sizes: '192x192',
            type: 'image/png'
          },
          {
            src: 'pwa-512x512.png',
            sizes: '512x512',
            type: 'image/png'
          }
        ]
      },
      workbox: {
        globPatterns: ['**/*.{js,css,html,ico,png,svg}'],
        runtimeCaching: [
          {
            urlPattern: /^https:\/\/api\.example\.com\/.*/i,
            handler: 'NetworkFirst',
            options: {
              cacheName: 'api-cache',
              expiration: {
                maxEntries: 100,
                maxAgeSeconds: 60 * 60 * 24 // 24 小时
              }
            }
          }
        ]
      }
    })
  ]
})
```

---

## 💬 微信集成

### 微信 JS-SDK

```typescript
// src/utils/wechat.ts
import wx from 'weixin-js-sdk'

export function initWechat(config: {
  appId: string
  timestamp: number
  nonceStr: string
  signature: string
}) {
  wx.config({
    debug: false,
    appId: config.appId,
    timestamp: config.timestamp,
    nonceStr: config.nonceStr,
    signature: config.signature,
    jsApiList: [
      'updateAppMessageShareData',
      'updateTimelineShareData',
      'chooseWXPay',
      'getLocation',
      'openLocation'
    ]
  })

  wx.ready(() => {
    console.log('微信 SDK 初始化成功')
  })

  wx.error((res) => {
    console.error('微信 SDK 初始化失败', res)
  })
}

// 分享配置
export function configShare(data: {
  title: string
  desc: string
  link: string
  imgUrl: string
}) {
  wx.updateAppMessageShareData({
    title: data.title,
    desc: data.desc,
    link: data.link,
    imgUrl: data.imgUrl,
    success: () => {
      console.log('分享成功')
    }
  })

  wx.updateTimelineShareData({
    title: data.title,
    link: data.link,
    imgUrl: data.imgUrl,
    success: () => {
      console.log('朋友圈分享成功')
    }
  })
}

// 微信支付
export function wechatPay(payConfig: {
  timeStamp: number
  nonceStr: string
  package: string
  signType: string
  paySign: string
}) {
  return new Promise((resolve, reject) => {
    wx.chooseWXPay({
      timestamp: payConfig.timeStamp,
      nonceStr: payConfig.nonceStr,
      package: payConfig.package,
      signType: payConfig.signType,
      paySign: payConfig.paySign,
      success: (res) => {
        if (res.errMsg === 'chooseWXPay:ok') {
          resolve(res)
        } else {
          reject(res)
        }
      },
      cancel: reject,
      fail: reject
    })
  })
}
```

---

## 📦 项目结构 (H5 优化)

```
frontend/
├── public/
│   ├── pwa-192x192.png      # PWA 图标
│   ├── pwa-512x512.png
│   └── manifest.json        # PWA manifest
├── src/
│   ├── components/
│   │   ├── common/          # 通用组件
│   │   │   ├── NavBar.vue   # 导航栏
│   │   │   ├── TabBar.vue   # 底部标签栏
│   │   │   └── Loading.vue  # 加载组件
│   │   └── business/        # 业务组件
│   ├── views/
│   │   ├── Home/            # 首页
│   │   ├── Live/            # 直播间
│   │   ├── Mall/            # 商城
│   │   ├── User/            # 个人中心
│   │   └── Login/           # 登录页
│   ├── stores/
│   ├── router/
│   ├── utils/
│   │   ├── api.ts           # API 客户端
│   │   ├── wechat.ts        # 微信 SDK
│   │   └── adapter.ts       # 适配工具
│   ├── styles/
│   │   └── variables.css    # 主题变量
│   └── App.vue
└── vite.config.ts
```

---

## 🚀 性能优化

### 1. 代码分割

```typescript
// router/index.ts
const routes = [
  {
    path: '/',
    component: () => import('@/views/Home/index.vue')
  },
  {
    path: '/live',
    component: () => import('@/views/Live/index.vue')
  }
]
```

### 2. 图片优化

```vue
<template>
  <img 
    :src="imageUrl" 
    loading="lazy"
    :alt="alt"
  />
</template>
```

### 3. 按需加载 Vant

```typescript
// main.ts
import { createApp } from 'vue'
import { Button, Field, Cell } from 'vant'
import 'vant/lib/index.css'

const app = createApp(App)
app.use(Button).use(Field).use(Cell)
```

---

## 🔐 安全考虑

### 1. HTTPS 强制

所有生产环境必须使用 HTTPS

### 2. 微信域名备案

- 业务域名需备案
- JS 安全域名配置
- 支付域名配置

### 3. Token 安全

```typescript
// Token 存储
localStorage.setItem('token', encryptedToken)

// 请求拦截
axios.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})
```

---

## 📱 兼容性目标

| 平台 | 最低版本 |
|------|----------|
| iOS Safari | 12+ |
| Android Chrome | 80+ |
| 微信内置浏览器 | 最新 |
| UC 浏览器 | 最新 |
| QQ 浏览器 | 最新 |

---

## 📋 检查清单

### 开发前
- [ ] 微信公众账号申请
- [ ] 微信支付商户号申请
- [ ] 域名备案
- [ ] HTTPS 证书配置

### 开发中
- [ ] 移动端适配测试
- [ ] 微信 SDK 集成
- [ ] PWA 配置
- [ ] 性能优化

### 上线前
- [ ] 多浏览器兼容性测试
- [ ] 压力测试
- [ ] 安全审计
- [ ] 灰度发布计划

---

*本文档随项目进展持续更新*
