# Snail Illustrations - 插图资源

## 📁 目录结构

```
illustrations/
├── empty-states/     # 空状态插图
│   ├── no-data.svg
│   ├── no-network.svg
│   └── no-permission.svg
├── onboarding/       # 引导插图
│   ├── welcome-1.svg
│   ├── welcome-2.svg
│   └── welcome-3.svg
└── banners/          # 横幅插图
    ├── header-bg.svg
    └── promo-banner.svg
```

## 🎨 插图规范

### 风格
- 简洁扁平风格
- 温暖友好的配色
- 符合品牌调性

### 尺寸
- 空状态：200x200px
- 引导页：300x300px
- 横幅：根据需求定制

### 格式
- SVG（首选，可缩放）
- PNG（备用，透明背景）

## 📦 使用场景

### 空状态插图
- 列表为空时
- 无网络连接时
- 无权限访问时

### 引导插图
- 首次启动应用
- 新功能介绍
- 用户引导流程

### 横幅插图
- 页面顶部装饰
- 活动宣传
- 节日主题

## 🎯 使用示例

```vue
<template>
  <div class="empty-state">
    <img src="@/assets/illustrations/empty-states/no-data.svg" alt="无数据" />
    <p>暂无数据</p>
  </div>
</template>
```

---

*插图资源 - Snail Project*
