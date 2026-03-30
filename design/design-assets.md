# Snail 项目设计资源指南

> 版本：1.0.0  
> 最后更新：2026-03-30

---

## 一、Logo 设计建议

### 1.1 Logo 概念

**Snail (蜗牛)** 品牌理念：
- 🐌 **稳健可靠** - 蜗牛虽慢但坚持不懈
- 🏠 **安全感** - 蜗牛壳代表保护和归属
- 🌱 **持续成长** - 蜗牛轨迹代表进步和积累

### 1.2 Logo 设计方案

#### 方案 A：图形 + 文字组合

```
    ┌─────────┐
    │  🐌     │  ← 蜗牛图形 (左侧)
    │  Snail  │  ← 品牌文字 (右侧)
    └─────────┘
```

- 图形：简约线条蜗牛轮廓
- 文字：无衬线字体，字重 Medium
- 颜色：主色 #4D8076
- 比例：图形：文字 = 1 : 2

#### 方案 B：纯图形 Logo

```
    ┌─────┐
    │ 🐌  │  ← 蜗牛壳螺旋图形
    └─────┘
```

- 使用蜗牛壳的螺旋形状作为核心元素
- 抽象化、几何化处理
- 适用于小尺寸场景 (favicon、头像)

#### 方案 C：字母 Logo

```
    ┌─────────┐
    │   S     │  ←  stylized "S" 形似蜗牛壳
    └─────────┘
```

- 将字母 S 设计成蜗牛壳螺旋形状
- 现代简约风格
- 易于识别和记忆

### 1.3 Logo 使用规范

#### 最小尺寸
- 横版 Logo：最小宽度 120px
- 纯图形：最小 32px × 32px
- 字母 Logo：最小 24px × 24px

#### 安全边距
```
    ←─── 1x ───→
  ┌──────────────┐ ↑
  │              │ 1x
  │    LOGO      │ ↓
  └──────────────┘
  ←─── 1x ───→
```
安全边距 = Logo 高度的 1/4

#### 背景使用
- 浅色背景：使用深色 Logo (#4D8076)
- 深色背景：使用白色 Logo (#FFFFFF)
- 复杂背景：添加白色/深色底色块

#### 禁止事项
- ❌ 不要拉伸或变形 Logo
- ❌ 不要改变 Logo 颜色 (除非规范允许)
- ❌ 不要在 Logo 上添加效果 (阴影、渐变等)
- ❌ 不要将 Logo 放置在对比度不足的背景上

### 1.4 Logo 文件输出

| 格式 | 用途 | 尺寸 |
|------|------|------|
| SVG | Web、打印 | 矢量 |
| PNG | 应用、Web | 512px, 256px, 128px, 64px, 32px |
| ICO | Favicon | 32px, 16px |
| PDF | 打印物料 | A4, A3 |

---

## 二、图标使用规范

### 2.1 图标库推荐

#### 主图标库：IconPark (字节出品)
- 风格：线性图标，现代简约
- 数量：2000+ 图标
- 许可：免费商用
- 网址：https://iconpark.oceanengine.com/

#### 备选图标库
- **Remix Icon** - 开源，风格统一
- **Phosphor Icons** - 设计精美，多字重
- **Tabler Icons** - 开源，数量多

### 2.2 图标风格规范

```
✅ 推荐风格：
- 线性图标 (Stroke: 1.5px)
- 圆角端点
- 简约几何造型
- 统一视觉重量

❌ 避免风格：
- 填充图标 (除非特殊场景)
- 过于复杂的细节
- 圆角和直角混用
- 不同风格混用
```

### 2.3 图标尺寸规范

| 场景 | 尺寸 | 使用示例 |
|------|------|----------|
| 导航栏图标 | 24px | TabBar、NavBar |
| 列表图标 | 20px | Cell 左侧图标 |
| 按钮图标 | 18px | 按钮内图标 |
| 功能图标 | 24px | FAB、操作按钮 |
| 空状态图标 | 80px | Empty 状态 |
| 装饰图标 | 32px-48px | 页面装饰 |

### 2.4 图标颜色规范

| 状态 | 颜色 | 用途 |
|------|------|------|
| 默认 | #999999 | 未选中、次要图标 |
| 激活 | #4D8076 | 选中、主要操作 |
| 成功 | #52C41A | 完成状态 |
| 警告 | #FFB800 | 待处理、注意 |
| 错误 | #FF6B6B | 删除、错误 |
| 禁用 | #CCCCCC | 不可用状态 |

### 2.5 常用图标清单

#### 导航类
```
home-fill      → 首页
tasks-fill     → 任务
chart-fill     → 统计
user-fill      → 我的
```

#### 操作类
```
plus           → 新建
edit           → 编辑
delete         → 删除
check          → 完成
close          → 关闭
search         → 搜索
filter         → 筛选
sort           → 排序
```

#### 状态类
```
clock          → 待处理
playing        → 进行中
checked        → 已完成
exclamation    → 逾期
```

#### 功能类
```
calendar       → 日期
tag            → 标签
attachment     → 附件
share          → 分享
settings       → 设置
notification   → 通知
```

### 2.6 图标使用示例 (Vue)

```vue
<template>
  <!-- IconPark 图标 -->
  <IconPark name="home-fill" :size="24" color="#4D8076" />
  
  <!-- Remix Icon -->
  <remix-icon name="task-line" size="20" />
  
  <!-- SVG 直接使用 -->
  <svg class="icon" viewBox="0 0 24 24">
    <path d="..." fill="currentColor" />
  </svg>
</template>

<style scoped>
.icon {
  width: 24px;
  height: 24px;
  color: #4D8076;
}
</style>
```

---

## 三、图片尺寸规范

### 3.1 用户头像

| 类型 | 尺寸 | 用途 |
|------|------|------|
| 小头像 | 32px × 32px | 列表、评论 |
| 中头像 | 40px × 40px | 导航栏、卡片 |
| 大头像 | 64px × 64px | 个人中心 |
| 超大头像 | 80px × 80px | 个人信息页 |
| 上传尺寸 | 200px × 200px | 原始上传 |

**格式要求：**
- 格式：JPG / PNG / WebP
- 比例：1:1 正方形
- 文件大小：< 200KB
- 圆角：50% (圆形)

### 3.2 任务附件图片

| 类型 | 尺寸 | 用途 |
|------|------|------|
| 缩略图 | 120px × 120px | 列表预览 |
| 预览图 | 400px × 400px | 点击预览 |
| 原图 | 保持原尺寸 | 下载查看 |

**格式要求：**
- 格式：JPG / PNG / WebP / GIF
- 比例：自适应 (最大边 1200px)
- 文件大小：< 2MB
- 圆角：8px

### 3.3 空状态/引导图

| 类型 | 尺寸 | 用途 |
|------|------|------|
| 空状态图 | 200px × 200px | 列表为空 |
| 引导插图 | 240px × 240px | 新手引导 |
| Banner 图 | 750px × 300px | 首页横幅 |

**格式要求：**
- 格式：PNG (透明背景) / SVG
- 风格：与品牌一致的插画风格
- 颜色：使用品牌色系

### 3.4 响应式图片策略

```html
<!-- 使用 srcset 响应式加载 -->
<img 
  src="image-400.jpg"
  srcset="image-200.jpg 200w,
          image-400.jpg 400w,
          image-800.jpg 800w"
  sizes="(max-width: 400px) 200px,
         (max-width: 800px) 400px,
         800px"
  alt="描述文字"
/>
```

### 3.5 图片优化建议

#### 压缩策略
- WebP 优先 (比 JPG 小 30%)
- 质量设置：80-85
- 使用 tinypng.com 或 squoosh.app 压缩

#### 懒加载
```vue
<template>
  <van-image 
    lazy-load
    src="image.jpg"
    width="100%"
  />
</template>
```

#### CDN 使用
```javascript
// 图片 URL 处理
const imageUrl = (path, size = '400') => {
  return `https://cdn.snail.app/images/${size}/${path}`;
};
```

---

## 四、设计资源文件结构

```
snail-design/
├── logo/
│   ├── logo-horizontal.svg       # 横版 Logo
│   ├── logo-horizontal.png       # 横版 PNG
│   ├── logo-icon.svg             # 图标 Logo
│   ├── logo-icon.png             # 图标 PNG
│   └── favicon.ico               # 网站图标
│
├── icons/
│   ├── navigation/               # 导航图标
│   ├── actions/                  # 操作图标
│   ├── status/                   # 状态图标
│   └── features/                 # 功能图标
│
├── illustrations/
│   ├── empty-states/             # 空状态插图
│   ├── onboarding/               # 引导插图
│   └── banners/                  # 横幅插图
│
├── templates/
│   ├── figma/                    # Figma 模板
│   ├── sketch/                   # Sketch 模板
│   └── xd/                       # Adobe XD 模板
│
└── guidelines/
    ├── design-system.md          # 设计规范
    ├── page-prototypes.md        # 页面原型
    ├── vant-theme.js             # 主题配置
    └── design-assets.md          # 资源指南 (本文件)
```

---

## 五、设计工具推荐

### 5.1 设计工具
- **Figma** - 主设计工具，协作方便
- **Sketch** - macOS 专用，插件丰富
- **Adobe XD** - Adobe 生态集成

### 5.2 资源网站
- **Unsplash** - 免费高质量图片
- **Undraw** - 免费开源插图
- **IconPark** - 字节开源图标库
- **Google Fonts** - 免费字体

### 5.3 在线工具
- **TinyPNG** - 图片压缩
- **Squoosh** - Google 图片优化
- **Coolors** - 配色方案生成
- **Font Pair** - 字体搭配

---

## 六、设计交付清单

### 6.1 开发交付物

- [ ] Logo 源文件 (SVG + PNG 多尺寸)
- [ ] 图标文件 (SVG 格式)
- [ ] 设计系统文档
- [ ] 页面原型图
- [ ] 主题配置文件
- [ ] 切图资源 (WebP + PNG)

### 6.2 验收标准

- [ ] 所有颜色符合设计规范
- [ ] 所有尺寸符合规范要求
- [ ] 图标风格统一
- [ ] 图片已优化压缩
- [ ] 文件命名规范
- [ ] 文档完整清晰

---

## 七、版本历史

| 版本 | 日期 | 更新内容 |
|------|------|----------|
| 1.0.0 | 2026-03-30 | 初始版本，完整设计资源规范 |
