#!/usr/bin/env python3
"""
根据 XMind 分析结果生成需求文档 - 改进版
"""

import json
import os

def load_analysis():
    """加载分析结果"""
    base_dir = "/home/openclaw/.openclaw/workspace/projects/snail/docs/requirements"
    with open(os.path.join(base_dir, 'analysis_result.json'), 'r', encoding='utf-8') as f:
        return json.load(f)

def get_topics_by_file(analysis, filename):
    """获取指定文件的所有主题"""
    file_data = analysis.get(filename, {})
    return file_data.get('topics', [])

def generate_requirements_doc(analysis):
    """生成完整需求文档"""
    
    doc = """# 🐌 蜗牛项目需求文档

> 本文档基于 XMind 需求图谱整理，包含 APP、官方网站、平台后台、商户后台的完整需求说明。

**版本**: 1.0  
**日期**: 2026-03-30  
**来源**: XMind 需求图谱

---

## 目录

1. [项目概述](#项目概述)
2. [APP 端需求](#app 端需求)
3. [官方网站需求](#官方网站需求)
4. [平台后台管理系统](#平台后台管理系统)
5. [商户后台管理系统](#商户后台管理系统)

---

## 项目概述

蜗牛项目是一个综合性生活服务平台，主要包含以下模块：

- **APP 端**：移动端应用（本需求聚焦 H5 App）
- **官方网站**：Web 端展示
- **平台后台管理系统**：总平台运营管理
- **商户后台管理系统**：商户自主管理

---

## APP 端需求

> 来源：蜗牛 app.xmind / 蜗牛 all.xmind

"""
    
    # 从蜗牛 all.xmind 提取 APP 需求
    all_topics = get_topics_by_file(analysis, '蜗牛 all.xmind')
    
    # 找到 APP 相关的所有主题
    app_topics = [t for t in all_topics if 'APP' in t['path'] or 'Android' in t['path'] or 'iOS' in t['path']]
    
    # 按层级组织
    current_section = None
    current_feature = None
    
    for topic in app_topics:
        path = topic['path']
        title = topic['title']
        level = path.count(' > ')
        
        if level == 2 and title in ['Android', 'iOS', 'H5']:
            doc += f"\n### {title} 端\n"
            current_section = title
        elif level == 3 and current_section:
            doc += f"\n#### {title}\n"
            current_feature = title
        elif level >= 4 and current_feature:
            indent = "  " * (level - 4)
            doc += f"{indent}- {title}\n"
    
    doc += """
---

## 官方网站需求

> 来源：蜗牛 all.xmind

"""
    
    # 提取官方网站相关需求
    website_topics = [t for t in all_topics if '官方网站' in t['path'] or '官网' in t['path']]
    
    for topic in website_topics:
        level = topic['path'].count(' > ')
        title = topic['title']
        
        if level == 2:
            doc += f"\n### {title}\n"
        elif level >= 3:
            indent = "  " * (level - 3)
            doc += f"{indent}- {title}\n"
    
    doc += """
---

## 平台后台管理系统

> 来源：蜗牛 backend.xmind / 蜗牛 all.xmind

"""
    
    backend_topics = get_topics_by_file(analysis, '蜗牛 backend.xmind')
    
    current_module = None
    for topic in backend_topics:
        path = topic['path']
        title = topic['title']
        level = path.count(' > ')
        
        if '平台后台' in path:
            if level == 2:
                doc += f"\n### {title}\n"
                current_module = title
            elif level == 3 and current_module:
                doc += f"\n#### {title}\n"
            elif level >= 4:
                indent = "  " * (level - 4)
                doc += f"{indent}- {title}\n"
    
    doc += """
---

## 商户后台管理系统

> 来源：蜗牛 backend.xmind / 蜗牛 all.xmind

"""
    
    for topic in backend_topics:
        path = topic['path']
        title = topic['title']
        level = path.count(' > ')
        
        if '商户后台' in path:
            if level == 2:
                doc += f"\n### {title}\n"
            elif level == 3:
                doc += f"\n#### {title}\n"
            elif level >= 4:
                indent = "  " * (level - 4)
                doc += f"{indent}- {title}\n"
    
    doc += """
---

## 附录：完整功能清单

### APP 功能清单

"""
    
    # 列出所有 APP 功能
    for topic in app_topics:
        if topic['path'].count(' > ') >= 3:
            doc += f"- {topic['path']}\n"
    
    doc += """
### 后台功能清单

"""
    
    for topic in backend_topics:
        if topic['path'].count(' > ') >= 2:
            doc += f"- {topic['path']}\n"
    
    doc += """
---

*文档结束*
"""
    
    return doc

def generate_h5_spec(analysis):
    """生成 H5 App 技术规格书"""
    
    # 提取 APP 相关需求
    all_topics = get_topics_by_file(analysis, '蜗牛 all.xmind')
    app_topics = [t for t in all_topics if 'APP' in t['path']]
    
    # 收集所有功能点
    features = {}
    for topic in app_topics:
        level = topic['path'].count(' > ')
        if level == 3:  # 功能模块级别
            features[topic['title']] = []
        elif level == 4 and features:
            last_key = list(features.keys())[-1]
            features[last_key].append(topic['title'])
    
    spec = f"""# 🐌 蜗牛 H5 App 技术规格书

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

"""
    
    for module, items in features.items():
        spec += f"\n### 2.x {module}\n\n"
        for i, item in enumerate(items, 1):
            spec += f"{i}. {item}\n"
        spec += "\n"
    
    spec += """
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
"""
    
    return spec

def generate_mvp_scope(analysis):
    """生成 MVP 范围定义"""
    
    # 提取需求用于 MVP 规划
    all_topics = get_topics_by_file(analysis, '蜗牛 all.xmind')
    
    mvp = """# 🐌 蜗牛 H5 App - MVP 范围定义

> 第一阶段最小可行产品 (MVP) 功能范围

**版本**: 1.0  
**日期**: 2026-03-30  
**目标周期**: 4-6 周

---

## 1. MVP 目标

快速验证核心业务模式，上线可用版本，收集用户反馈。

### 1.1 核心目标

- ✅ 验证用户需求
- ✅ 跑通核心流程
- ✅ 收集运营数据
- ✅ 为后续迭代提供依据

### 1.2 成功标准

| 指标 | 目标值 |
|------|--------|
| 功能完整度 | P0 功能 100% |
| 首屏加载时间 | < 3s |
| 兼容性 | 主流浏览器正常 |
| 阻塞性 Bug | 0 个 |

---

## 2. 功能优先级

### P0 - 必须有 (MVP 核心)

> 没有这些功能，产品无法运行

#### 2.1 用户系统
- [ ] 注册（手机号 + 验证码）
- [ ] 登录（账号密码/验证码）
- [ ] 退出登录
- [ ] 个人中心（基础信息展示）
- [ ] 个人资料编辑

#### 2.2 首页
- [ ] 天气显示（定位城市）
- [ ] 搜索框（全局搜索）
- [ ] 公告展示（滚动/列表）
- [ ] 功能入口（网格布局）

#### 2.3 任务系统
- [ ] 任务列表（分类展示）
- [ ] 任务详情
- [ ] 任务完成提交
- [ ] 任务奖励发放
- [ ] 任务记录查询

#### 2.4 积分/钱包
- [ ] 积分余额显示
- [ ] 积分明细列表
- [ ] 积分获取记录
- [ ] 积分消耗记录
- [ ] 提现申请（基础）

#### 2.5 内容发布
- [ ] 发布入口
- [ ] 文本输入
- [ ] 图片上传（多图）
- [ ] 发布提交
- [ ] 审核状态展示

#### 2.6 后台管理 (基础版)
- [ ] 用户列表/搜索
- [ ] 用户详情
- [ ] 内容审核（通过/拒绝）
- [ ] 基础数据统计

---

### P1 - 应该有 (第一迭代)

> 重要但可延后 1-2 周

#### 3.1 社交功能
- [ ] 关注/取消关注
- [ ] 粉丝列表
- [ ] 关注列表
- [ ] 点赞功能
- [ ] 评论功能
- [ ] 消息通知（站内）

#### 3.2 搜索优化
- [ ] 搜索结果页
- [ ] 搜索历史
- [ ] 热门搜索
- [ ] 搜索建议

#### 3.3 推广系统
- [ ] 邀请好友
- [ ] 邀请码生成
- [ ] 邀请奖励
- [ ] 推广数据统计

#### 3.4 用户体验
- [ ] 启动页
- [ ] 引导页
- [ ] 新手任务
- [ ] 意见反馈

---

### P2 - 可以有 (后续迭代)

> 锦上添花的功能

#### 4.1 电商功能
- [ ] 商品浏览
- [ ] 商品详情
- [ ] 购物车
- [ ] 订单管理
- [ ] 支付集成
- [ ] 物流查询

#### 4.2 直播功能
- [ ] 直播列表
- [ ] 直播观看
- [ ] 直播互动（点赞/评论）
- [ ] 直播回放

#### 4.3 高级功能
- [ ] 旅游服务
- [ ] 广场舞社区
- [ ] 老年大学课程
- [ ] 直播开播功能

#### 4.4 商户功能
- [ ] 商户入驻
- [ ] 商户后台
- [ ] 商品管理
- [ ] 订单处理

---

## 3. 技术 MVP

### 3.1 必须实现

| 技术 | 要求 | 优先级 |
|------|------|--------|
| PWA 基础 | Service Worker 缓存核心资源 | P0 |
| 微信适配 | JSSDK 集成、分享、支付 | P0 |
| 响应式 | 320px-768px 全适配 | P0 |
| HTTPS | 全站加密 | P0 |
| 性能优化 | 首屏 < 3s | P0 |
| 错误监控 | Sentry 接入 | P0 |

### 3.2 可延后

| 技术 | 说明 | 延后版本 |
|------|------|----------|
| 推送通知 | Web Push / 模板消息 | v1.1 |
| 完整 PWA | 离线可用、添加到桌面 | v1.1 |
| 多语言 | i18n 支持 | v2.0 |
| SSR | 服务端渲染 | v2.0 |
| 小程序 | 独立项目 | - |

---

## 4. 排除范围

> 明确本次不做的功能，避免范围蔓延

- ❌ 原生 Android/iOS App
- ❌ 微信小程序（独立项目）
- ❌ PC 端 Web
- ❌ 复杂 AI 功能
- ❌ 区块链/虚拟货币
- ❌ 第三方登录（微信除外）
- ❌ 视频上传/播放
- ❌ 即时通讯（IM）

---

## 5. 里程碑计划

### 5.1 时间规划

```
Week 1-2: 基础框架 + 用户系统 + 首页
Week 3-4: 任务系统 + 积分系统 + 内容发布
Week 5:   后台管理 + 联调测试
Week 6:   Bug 修复 + 性能优化 + 上线
```

### 5.2 里程碑

| 里程碑 | 时间 | 交付物 | 验收标准 |
|--------|------|--------|----------|
| M1 | 第 2 周末 | 用户系统 + 首页 | 可注册登录、首页展示正常 |
| M2 | 第 4 周末 | 任务 + 积分 + 发布 | 核心流程跑通 |
| M3 | 第 5 周末 | 后台 + 联调 | 后台可审核、数据正常 |
| M4 | 第 6 周末 | 正式上线 | 无阻塞 Bug、性能达标 |

### 5.3 人员配置

| 角色 | 人数 | 职责 |
|------|------|------|
| 前端开发 | 2 | H5 开发、微信适配 |
| 后端开发 | 2 | API 开发、数据库 |
| UI 设计 | 1 | 界面设计、切图 |
| 测试 | 1 | 测试用例、Bug 跟踪 |
| 产品 | 1 | 需求跟进、验收 |

---

## 6. 风险管理

### 6.1 技术风险

| 风险 | 概率 | 影响 | 应对措施 |
|------|------|------|----------|
| 微信兼容问题 | 中 | 高 | 提前测试、准备降级方案 |
| 性能不达标 | 中 | 中 | 预留优化时间、监控 |
| 第三方服务不稳定 | 低 | 高 | 选择可靠服务商、容错 |

### 6.2 进度风险

| 风险 | 概率 | 影响 | 应对措施 |
|------|------|------|----------|
| 需求变更 | 高 | 中 | 严格控制范围、变更评审 |
| 人员变动 | 低 | 高 | 文档完善、知识共享 |
| 外部依赖延期 | 中 | 中 | 提前沟通、备选方案 |

---

## 7. 验收标准

### 7.1 功能验收

- [ ] P0 功能 100% 完成
- [ ] 核心流程可完整跑通
- [ ] 无阻塞性 Bug
- [ ] 后台可正常审核管理

### 7.2 性能验收

- [ ] 首屏加载 < 3s (4G 网络)
- [ ] Lighthouse 性能分 > 80
- [ ] 页面流畅无卡顿
- [ ] 图片加载正常

### 7.3 兼容性验收

| 平台 | 测试设备 | 验收标准 |
|------|----------|----------|
| iOS | iPhone 8+/12+/15+ | 功能正常、UI 正常 |
| Android | 华为/小米/OPPO/vivo | 功能正常、UI 正常 |
| 微信 | iOS/Android 微信 | 功能正常、分享正常 |

### 7.4 安全验收

- [ ] HTTPS 正常
- [ ] 登录态安全
- [ ] 接口签名验证
- [ ] XSS/CSRF 防护

---

## 8. 后续规划

### v1.1 (上线后 2 周)
- 推送通知
- 完整 PWA 支持
- 用户体验优化

### v1.2 (上线后 4 周)
- 社交功能完善
- 推广系统
- 数据分析

### v2.0 (上线后 8 周)
- 电商功能
- 商户入驻
- 直播功能

---

*文档版本：1.0*  
*最后更新：2026-03-30*
"""
    
    return mvp

def main():
    base_dir = "/home/openclaw/.openclaw/workspace/projects/snail/docs/requirements"
    
    print("加载分析结果...")
    analysis = load_analysis()
    
    # 生成完整需求文档
    print("生成 REQUIREMENTS.md...")
    req_doc = generate_requirements_doc(analysis)
    with open(os.path.join(base_dir, 'REQUIREMENTS.md'), 'w', encoding='utf-8') as f:
        f.write(req_doc)
    print(f"  REQUIREMENTS.md: {len(req_doc)} 字符")
    
    # 生成 H5 技术规格书
    print("生成 H5_SPEC.md...")
    h5_spec = generate_h5_spec(analysis)
    with open(os.path.join(base_dir, 'H5_SPEC.md'), 'w', encoding='utf-8') as f:
        f.write(h5_spec)
    print(f"  H5_SPEC.md: {len(h5_spec)} 字符")
    
    # 生成 MVP 范围定义
    print("生成 MVP_SCOPE.md...")
    mvp_doc = generate_mvp_scope(analysis)
    with open(os.path.join(base_dir, 'MVP_SCOPE.md'), 'w', encoding='utf-8') as f:
        f.write(mvp_doc)
    print(f"  MVP_SCOPE.md: {len(mvp_doc)} 字符")
    
    print("\n✅ 文档生成完成!")

if __name__ == "__main__":
    main()
