# Snail 项目设计中心

> 🐌 Snail Task Management - UI 设计资源汇总  
> 版本：1.0.0 | 更新日期：2026-03-30

---

## 📁 文件目录

| 文件 | 说明 | 大小 |
|------|------|------|
| [`design-system.md`](./design-system.md) | 设计系统规范 - 色彩/字体/间距/组件 | 7KB |
| [`page-prototypes.md`](./page-prototypes.md) | 页面原型说明 - 首页/任务页/个人中心 | 14KB |
| [`vant-theme.js`](./vant-theme.js) | Vant 主题配置 - CSS 变量/SCSS 变量 | 10KB |
| [`design-assets.md`](./design-assets.md) | 设计资源指南 - Logo/图标/图片规范 | 9KB |

---

## 🎨 快速开始

### 1. 查看设计规范

```bash
# 打开设计规范文档
cat design-system.md
```

**核心内容：**
- 主色：`#4D8076` (Snail Green)
- 字体：系统默认无衬线字体
- 间距：4px 基准单位
- 圆角：8px (按钮/输入框), 12px (卡片)

### 2. 应用 Vant 主题

```javascript
// 在 main.js 中
import { initSnailTheme } from './design/vant-theme';

// 初始化主题
initSnailTheme();
```

```scss
// 或在 styles/vant-theme.scss 中引入
@use "@/design/vant-theme.scss" as *;
```

### 3. 参考页面原型

打开 `page-prototypes.md` 查看：
- 首页布局结构
- 任务页交互说明
- 个人中心设计

### 4. 使用设计资源

参考 `design-assets.md` 获取：
- Logo 使用规范
- 图标尺寸和颜色
- 图片尺寸要求

---

## 🎯 设计原则

1. **简约高效** - 减少视觉干扰，聚焦任务本身
2. **一致性强** - 统一的色彩、字体、间距规范
3. **易于使用** - 符合移动端用户习惯
4. **可扩展** - 模块化设计，便于后续迭代

---

## 📋 开发检查清单

### 开发前
- [ ] 阅读完整设计规范文档
- [ ] 配置 Vant 主题变量
- [ ] 准备图标资源

### 开发中
- [ ] 使用规范色值 (不要硬编码)
- [ ] 遵循间距规范 (4px 倍数)
- [ ] 组件样式保持一致

### 开发后
- [ ] 检查颜色对比度
- [ ] 验证触摸目标尺寸 (≥44px)
- [ ] 测试不同屏幕尺寸

---

## 🔧 技术栈

- **框架**: Vue 3
- **UI 库**: Vant 4
- **样式**: SCSS + CSS Variables
- **图标**: IconPark / Remix Icon
- **构建**: Vite

---

## 📞 设计协作

- **设计工具**: Figma
- **资源交付**: 本目录
- **问题反馈**: 创建 Issue 或联系设计负责人

---

## 📝 更新日志

### v1.0.0 (2026-03-30)
- ✨ 创建设计系统规范
- ✨ 完成页面原型设计
- ✨ 配置 Vant 主题
- ✨ 制定设计资源规范

---

**🐌 Snail Design Team**
