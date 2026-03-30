# Snail Icons - 图标库

## 📁 目录结构

```
icons/
├── navigation/    # 导航图标
│   ├── home.svg
│   ├── tasks.svg
│   ├── mall.svg
│   └── user.svg
├── actions/       # 操作图标
│   ├── edit.svg
│   ├── delete.svg
│   ├── share.svg
│   └── download.svg
├── status/        # 状态图标
│   ├── success.svg
│   ├── warning.svg
│   ├── error.svg
│   └── info.svg
└── features/      # 功能图标
    ├── live.svg
    ├── chat.svg
    ├── gift.svg
    └── stats.svg
```

## 🎨 图标规范

### 尺寸
- 标准尺寸：24x24px
- 小尺寸：16x16px
- 大尺寸：32x32px, 48x48px

### 颜色
- 主色：#4D8076
- 辅色：#07c160
- 警告：#ff976a
- 危险：#ee0a24

### 样式
- 线条粗细：2px
- 圆角：2px
- 风格：简洁线性

## 📦 使用方式

### SVG 直接使用
```html
<svg class="icon" viewBox="0 0 24 24">
  <path d="..." />
</svg>
```

### Vue 组件
```vue
<template>
  <icon name="home" size="24" color="#4D8076" />
</template>
```

## 🔗 图标来源

- 主要使用 [IconPark](https://iconpark.oceanengine.com/)
- 部分自定义图标
- Vant 内置图标

---

*图标库 - Snail Project*
