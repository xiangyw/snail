# Snail 项目设计系统规范

> 版本：1.0.0  
> 最后更新：2026-03-30  
> 适用框架：Vue 3 + Vant 4

---

## 一、色彩方案 (Color System)

### 1.1 主色 (Primary Colors)

| 色值 | 名称 | 用途 |
|------|------|------|
| `#4D8076` | Snail Green | 品牌主色、主要按钮、激活状态 |
| `#3D6B63` | Snail Green Dark | 主色深色变体、hover 状态 |
| `#6BA89E` | Snail Green Light | 主色浅色变体、背景装饰 |

### 1.2 辅色 (Secondary Colors)

| 色值 | 名称 | 用途 |
|------|------|------|
| `#F5F5F5` | Background Light | 页面背景、卡片背景 |
| `#FFFFFF` | White | 卡片内容区、弹窗背景 |
| `#E8E8E8` | Border Light | 分割线、边框 |
| `#999999` | Text Secondary | 次要文字、提示文字 |
| `#333333` | Text Primary | 主要文字、标题 |

### 1.3 功能色 (Functional Colors)

| 色值 | 名称 | 用途 |
|------|------|------|
| `#FF6B6B` | Error | 错误提示、删除操作 |
| `#FFB800` | Warning | 警告提示、待处理状态 |
| `#52C41A` | Success | 成功提示、完成状态 |
| `#1890FF` | Info | 信息提示、链接颜色 |

### 1.4 渐变色 (Gradients)

```css
/* 主色渐变 - 用于重要按钮和横幅 */
.snail-gradient-primary {
  background: linear-gradient(135deg, #4D8076 0%, #6BA89E 100%);
}

/* 背景渐变 - 用于页面顶部装饰 */
.snail-gradient-bg {
  background: linear-gradient(180deg, #F5F9F8 0%, #FFFFFF 100%);
}
```

---

## 二、字体规范 (Typography)

### 2.1 字体家族

```css
:root {
  --font-family-base: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 
                      'Helvetica Neue', Arial, 'PingFang SC', 
                      'Hiragino Sans GB', 'Microsoft YaHei', sans-serif;
  --font-family-number: 'DIN Alternate', 'Roboto Mono', monospace;
}
```

### 2.2 字号规范

| 级别 | 字号 | 行高 | 字重 | 用途 |
|------|------|------|------|------|
| H1 | 24px | 32px | 600 | 页面标题 |
| H2 | 20px | 28px | 600 | 模块标题 |
| H3 | 18px | 26px | 600 | 卡片标题 |
| H4 | 16px | 24px | 500 | 小标题 |
| Body | 14px | 22px | 400 | 正文内容 |
| Caption | 12px | 18px | 400 | 辅助说明、时间戳 |
| Small | 10px | 14px | 400 | 标签、徽章 |

### 2.3 文字颜色

```css
--text-primary: #333333;      /* 主要文字 */
--text-secondary: #999999;    /* 次要文字 */
--text-disabled: #CCCCCC;     /* 禁用文字 */
--text-link: #4D8076;         /* 链接文字 */
--text-inverse: #FFFFFF;      /* 反色文字 */
```

---

## 三、间距规范 (Spacing)

### 3.1 基础间距单位

基础单位：`4px`

| 代号 | 值 | 用途 |
|------|-----|------|
| xs | 4px | 紧凑间距、图标与文字 |
| sm | 8px | 小组件内部间距 |
| md | 16px | 标准间距、卡片内边距 |
| lg | 24px | 大间距、模块间距 |
| xl | 32px | 超大间距、页面边距 |
| xxl | 48px | section 间距 |

### 3.2 页面边距

```css
/* 移动端页面标准边距 */
.page-padding {
  padding-left: 16px;
  padding-right: 16px;
}

/* 卡片内边距 */
.card-padding {
  padding: 16px;
}

/* 列表项间距 */
.list-item-margin {
  margin-bottom: 12px;
}
```

### 3.3 组件间距规范

| 组件 | 内边距 | 外边距 |
|------|--------|--------|
| 按钮 | 8px 16px | - |
| 卡片 | 16px | 12px |
| 列表项 | 12px 16px | 0 |
| 表单字段 | 16px | 0 |
| 弹窗 | 20px | - |

---

## 四、组件样式 (Component Styles)

### 4.1 按钮 (Button)

```css
/* 主要按钮 */
.btn-primary {
  background-color: #4D8076;
  color: #FFFFFF;
  border-radius: 8px;
  padding: 10px 24px;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.2s ease;
}

.btn-primary:hover {
  background-color: #3D6B63;
}

.btn-primary:active {
  background-color: #2F554E;
}

/* 次要按钮 */
.btn-secondary {
  background-color: #FFFFFF;
  color: #4D8076;
  border: 1px solid #4D8076;
  border-radius: 8px;
  padding: 10px 24px;
  font-size: 14px;
  font-weight: 500;
}

/* 文字按钮 */
.btn-text {
  background: none;
  color: #4D8076;
  padding: 8px 12px;
  font-size: 14px;
}
```

### 4.2 卡片 (Card)

```css
.snail-card {
  background: #FFFFFF;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  border: 1px solid #F0F0F0;
}

.snail-card-header {
  font-size: 16px;
  font-weight: 600;
  color: #333333;
  margin-bottom: 12px;
}
```

### 4.3 输入框 (Input)

```css
.snail-input {
  border: 1px solid #E8E8E8;
  border-radius: 8px;
  padding: 10px 12px;
  font-size: 14px;
  color: #333333;
  background: #FFFFFF;
  transition: border-color 0.2s ease;
}

.snail-input:focus {
  border-color: #4D8076;
  outline: none;
}

.snail-input::placeholder {
  color: #CCCCCC;
}
```

### 4.4 标签 (Tag)

```css
.snail-tag {
  display: inline-flex;
  align-items: center;
  padding: 4px 10px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
}

.snail-tag-primary {
  background: rgba(77, 128, 118, 0.1);
  color: #4D8076;
}

.snail-tag-success {
  background: rgba(82, 196, 26, 0.1);
  color: #52C41A;
}

.snail-tag-warning {
  background: rgba(255, 184, 0, 0.1);
  color: #FFB800;
}
```

### 4.5 头像 (Avatar)

```css
.snail-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, #4D8076 0%, #6BA89E 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #FFFFFF;
  font-weight: 600;
  font-size: 16px;
}

.snail-avatar-sm {
  width: 32px;
  height: 32px;
  font-size: 14px;
}

.snail-avatar-lg {
  width: 64px;
  height: 64px;
  font-size: 24px;
}
```

---

## 五、圆角规范 (Border Radius)

| 组件 | 圆角值 |
|------|--------|
| 按钮 | 8px |
| 卡片 | 12px |
| 输入框 | 8px |
| 弹窗 | 16px |
| 标签 | 4px |
| 头像 | 50% |
| 图片 | 8px |

---

## 六、阴影规范 (Shadows)

```css
/* 轻阴影 - 用于卡片 */
.shadow-sm {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

/* 中阴影 - 用于悬浮元素 */
.shadow-md {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

/* 重阴影 - 用于弹窗、下拉菜单 */
.shadow-lg {
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
}
```

---

## 七、动效规范 (Animation)

### 7.1 过渡时间

| 类型 | 时长 | 用途 |
|------|------|------|
| Fast | 150ms | 小状态变化、hover |
| Normal | 250ms | 标准过渡、颜色变化 |
| Slow | 350ms | 大元素移动、页面切换 |

### 7.2 缓动函数

```css
--ease-in-out: cubic-bezier(0.4, 0, 0.2, 1);
--ease-out: cubic-bezier(0, 0, 0.2, 1);
--ease-in: cubic-bezier(0.4, 0, 1, 1);
--ease-bounce: cubic-bezier(0.68, -0.55, 0.265, 1.55);
```

---

## 八、响应式断点 (Responsive Breakpoints)

| 断点 | 宽度 | 适用设备 |
|------|------|----------|
| xs | < 375px | 小屏手机 |
| sm | ≥ 375px | 标准手机 |
| md | ≥ 768px | 平板 |
| lg | ≥ 1024px | 桌面 |
| xl | ≥ 1440px | 大屏桌面 |

---

## 九、无障碍规范 (Accessibility)

- 文字对比度至少达到 WCAG AA 标准 (4.5:1)
- 所有可交互元素需有 focus 状态
- 图片需添加 alt 描述
- 表单字段需关联 label
- 颜色不能作为唯一的信息传达方式
