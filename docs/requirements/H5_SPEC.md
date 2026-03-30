# 🐌 蜗牛 H5 App 技术规格书

> 本文档定义 H5 App 的技术实现规范，适配移动端浏览器和微信内置浏览器。

**版本**: 1.0  
**日期**: 2026-03-30  
**目标平台**: 移动端浏览器 + 微信内置浏览器

---

## 1. 技术选型

### 1.1 前端框架

| 技术 | 选型 | 说明 |
|------|------|------|
| 框架 | Vue 3 / React 18 | 根据团队技术栈选择 |
| 构建工具 | Vite | 快速开发和构建 |
| UI 库 | Vant UI / NutUI | 移动端组件库 |
| 状态管理 | Pinia / Redux | 全局状态管理 |
| 路由 | Vue Router / React Router | 单页应用路由 |
| HTTP 客户端 | Axios | API 请求 |

### 1.2 PWA 支持

- **Service Worker**: 离线缓存、资源预加载
- **Web App Manifest**: 添加到桌面支持
- **Push API**: 推送通知（需浏览器支持）
- **Cache API**: 静态资源缓存

### 1.3 适配要求

| 平台 | 最低版本 | 目标版本 | 备注 |
|------|----------|----------|------|
| iOS Safari | 12+ | 15+ | 全面屏适配 |
| Android Chrome | 80+ | 100+ | 主流安卓机型 |
| 微信内置浏览器 | 7.0+ | 8.0+ | X5 内核适配 |
| 其他 WebView | Android 5.0+ | - | 兼容模式 |

---

## 2. 功能模块详细设计


### 2.x 注册登陆-本机号码一键登录



### 2.x 安装启动页介绍3页



### 2.x 启动页广告 5秒



### 2.x 首页

1. 天气/搜索框
2. 公告
3. 功能入口区域
4. 商品/广告区域
5. 视频/直播区域


### 2.x 商城：商城/积分商城

1. 搜索框
2. 购物车
3. 功能栏
4. 品类栏
5. 精选推荐商品
6. 商品列表


### 2.x 语音搜索

1. 搜索结果页


### 2.x 消息

1. 上-类别
2. 下-消息列表


### 2.x 我的 /用户主页

1. 资料
2. 关注
3. 粉丝
4. 收藏
5. 点赞
6. 蜗牛币-他人主页不显示
7. 蜗牛豆-他人主页不显示
8. 实名认证-他人主页不显示
9. 客服中心-他人主页不显示
10. 主播申请-他人主页不显示
11. 成为商户-他人主页不显示
12. 好友邀请-他人主页不显示
13. 设置-他人主页不显示
14. 视频列表
15. 点赞列表
16. 收藏列表


### 2.x 推送消息

1. 系统通知
2. 直播通知


### 2.x 同Android



### 2.x 提供相应的API



---

## 3. 微信适配规范

### 3.1 微信 SDK 集成

```javascript
// 微信 JSSDK 配置示例
wx.config({
  debug: false,
  appId: 'xxx',
  timestamp: xxx,
  nonceStr: 'xxx',
  signature: 'xxx',
  jsApiList: ['updateAppMessageShareData', 'chooseImage', 'getLocation']
});
```

### 3.2 必须适配的功能

| 功能 | 微信 API | 降级方案 |
|------|----------|----------|
| 分享 | wx.updateAppMessageShareData | 原生分享 |
| 支付 | wx.chooseWXPay | 其他支付 |
| 图片上传 | wx.chooseImage | input file |
| 地理位置 | wx.getLocation | HTML5 Geolocation |
| 扫码 | wx.scanQRCode | 不支持 |
| 录音 | wx.startRecord | MediaRecorder API |

### 3.3 注意事项

1. **避免使用 alert()**: 使用自定义弹窗组件
2. **图片上传**: 优先使用微信选择器
3. **页面跳转**: 使用微信跳转 API 保持登录态
4. **用户信息**: 需用户主动授权获取
5. **支付回调**: 使用微信统一回调机制

---

## 4. PWA 特性实现

### 4.1 离线访问

```javascript
// Service Worker 缓存策略
self.addEventListener('fetch', event => {
  event.respondWith(
    caches.match(event.request)
      .then(response => response || fetch(event.request))
  );
});
```

**缓存资源**:
- 核心页面 HTML
- CSS/JS  bundle
- 常用图片资源
- API 响应数据（可选）

### 4.2 推送通知

**实现方案**:
1. Web Push API（支持浏览器）
2. 微信模板消息（微信内）
3. 短信通知（备选）

### 4.3 添加到桌面

**manifest.json 配置**:
```json
{
  "name": "蜗牛",
  "short_name": "蜗牛",
  "start_url": "/",
  "display": "standalone",
  "background_color": "#ffffff",
  "theme_color": "#000000",
  "icons": [
    {
      "src": "/icon-192.png",
      "sizes": "192x192",
      "type": "image/png"
    }
  ]
}
```

---

## 5. 性能优化

### 5.1 加载性能目标

| 指标 | 目标值 | 测量方式 |
|------|--------|----------|
| 首屏加载 (FCP) | < 1.5s | Lighthouse |
| 可交互时间 (TTI) | < 3.5s | Lighthouse |
| 资源体积 | < 500KB | 压缩后 |
| 请求数量 | < 50 | 首屏 |

### 5.2 优化策略

**资源优化**:
- [ ] 代码分割 (Code Splitting)
- [ ] 路由懒加载
- [ ] 图片懒加载 + WebP 格式
- [ ] Gzip/Brotli 压缩
- [ ] CDN 加速

**渲染优化**:
- [ ] 虚拟列表 (长列表场景)
- [ ] 防抖节流 (搜索、滚动)
- [ ] 避免强制同步布局
- [ ] 使用 CSS transform 替代 position

**缓存策略**:
- [ ] HTTP 缓存头配置
- [ ] Service Worker 缓存
- [ ] LocalStorage 数据缓存
- [ ] IndexedDB 大量数据存储

---

## 6. 安全规范

### 6.1 认证安全

```javascript
// JWT Token 管理
const auth = {
  getToken: () => localStorage.getItem('token'),
  setToken: (token) => localStorage.setItem('token', token),
  refreshToken: async () => {
    // 刷新 token 逻辑
  },
  isExpired: (token) => {
    // 检查 token 过期
  }
};
```

**要求**:
- Token 存储使用 HttpOnly Cookie (推荐) 或加密 Storage
- Token 过期自动刷新
- 敏感操作需二次验证
- 登录态异地检测

### 6.2 数据安全

- [ ] 全站 HTTPS
- [ ] 敏感数据加密存储
- [ ] 输入内容 XSS 过滤
- [ ] CSRF Token 验证
- [ ] 接口参数签名

### 6.3 接口安全

```javascript
// 请求拦截器 - 添加签名
axios.interceptors.request.use(config => {
  config.headers['X-Signature'] = generateSignature(config);
  config.headers['X-Timestamp'] = Date.now();
  return config;
});
```

---

## 7. 项目结构

```
snail-h5/
├── public/
│   ├── index.html
│   ├── manifest.json
│   └── sw.js                 # Service Worker
├── src/
│   ├── assets/               # 静态资源
│   │   ├── images/
│   │   ├── fonts/
│   │   └── styles/
│   ├── components/           # 公共组件
│   │   ├── common/
│   │   ├── business/
│   │   └── layout/
│   ├── views/                # 页面组件
│   │   ├── home/
│   │   ├── task/
│   │   ├── profile/
│   │   └── ...
│   ├── store/                # 状态管理
│   │   ├── modules/
│   │   └── index.js
│   ├── router/               # 路由配置
│   │   ├── index.js
│   │   └── routes.js
│   ├── api/                  # API 接口
│   │   ├── request.js
│   │   └── modules/
│   ├── utils/                # 工具函数
│   │   ├── auth.js
│   │   ├── wechat.js
│   │   └── ...
│   ├── directives/           # 自定义指令
│   ├── hooks/                # 组合式 API
│   ├── App.vue
│   └── main.js
├── tests/                    # 测试文件
├── .eslintrc.js
├── .prettierrc
├── vite.config.js
└── package.json
```

---

## 8. 开发规范

### 8.1 代码规范

- **ESLint**: 代码质量检查
- **Prettier**: 代码格式化
- **TypeScript**: 类型检查（推荐）
- **Commitlint**: 提交信息规范

### 8.2 Git 工作流

```
main          - 主分支（生产）
├── release/*  - 发布分支
├── develop    - 开发分支
│   ├── feature/*  - 功能分支
│   └── bugfix/*   - 修复分支
```

### 8.3 命名规范

| 类型 | 规范 | 示例 |
|------|------|------|
| 组件 | PascalCase | UserProfile.vue |
| 文件 | kebab-case | user-profile.vue |
| 变量 | camelCase | userName |
| 常量 | UPPER_SNAKE | API_BASE_URL |
| CSS 类 | kebab-case | user-profile |

---

## 9. 测试要求

### 9.1 测试类型

- [ ] 单元测试 (Jest/Vitest)
- [ ] 组件测试 (Vue Test Utils)
- [ ] E2E 测试 (Cypress/Playwright)
- [ ] 性能测试 (Lighthouse)
- [ ] 兼容性测试 (BrowserStack)

### 9.2 覆盖率要求

| 类型 | 最低覆盖率 |
|------|-----------|
| 语句覆盖率 | 80% |
| 分支覆盖率 | 70% |
| 函数覆盖率 | 80% |

---

## 10. 部署规范

### 10.1 环境配置

| 环境 | 域名 | 用途 |
|------|------|------|
| 开发 | dev.snail.com | 开发调试 |
| 测试 | test.snail.com | 测试验证 |
| 预发 | staging.snail.com | 预发布 |
| 生产 | snail.com | 线上 |

### 10.2 CI/CD 流程

```
代码提交 → 代码检查 → 单元测试 → 构建 → 部署 → 通知
```

### 10.3 监控告警

- [ ] 错误监控 (Sentry)
- [ ] 性能监控 (Web Vitals)
- [ ] 业务监控 (自定义埋点)
- [ ] 告警通知 (钉钉/企业微信)

---

*文档版本：1.0*  
*最后更新：2026-03-30*
