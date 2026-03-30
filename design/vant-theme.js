/**
 * Snail 项目 Vant 4 主题配置
 * 
 * 使用方法：
 * 1. 在 vite.config.js 中配置 styleVars
 * 2. 或在 main.js 中动态设置 CSS 变量
 */

// ============================================
// Vant 主题色配置
// ============================================

export const vantThemeConfig = {
  // 基础颜色
  blue: '#4D8076',           // 主色调
  green: '#52C41A',          // 成功色
  yellow: '#FFB800',         // 警告色
  red: '#FF6B6B',            // 错误色
  
  // 文字颜色
  textColor: '#333333',
  textColorSecondary: '#999999',
  textColorDisabled: '#CCCCCC',
  
  // 背景颜色
  backgroundColor: '#F5F5F5',
  backgroundColorLight: '#FFFFFF',
  
  // 边框颜色
  borderColor: '#E8E8E8',
  borderColorLight: '#F0F0F0',
  
  // 激活状态颜色
  activeColor: '#4D8076',
  activeOpacity: 0.1,
  
  // 禁用状态颜色
  disabledColor: '#CCCCCC',
  
  // 圆角
  borderRadius: '8px',
  borderRadiusLg: '12px',
  borderRadiusXl: '16px',
  
  // 间距
  padding: '16px',
  paddingSm: '8px',
  paddingXs: '4px',
  
  // 阴影
  boxShadow: '0 2px 8px rgba(0, 0, 0, 0.06)',
  boxShadowLg: '0 8px 32px rgba(0, 0, 0, 0.12)',
};

// ============================================
// Vant CSS 变量完整配置
// ============================================

export const vantCssVars = {
  // Color Palette
  'van-blue': '#4D8076',
  'van-green': '#52C41A',
  'van-yellow': '#FFB800',
  'van-red': '#FF6B6B',
  'van-orange': '#FF9500',
  'van-white': '#FFFFFF',
  'van-gray-1': '#F7F8FA',
  'van-gray-2': '#F2F3F5',
  'van-gray-3': '#EBEDF0',
  'van-gray-4': '#DCDEE0',
  'van-gray-5': '#C8C9CC',
  'van-gray-6': '#969799',
  'van-gray-7': '#646566',
  'van-gray-8': '#323233',
  
  // Text Color
  'van-text-color': '#333333',
  'van-text-color-secondary': '#999999',
  'van-text-color-disabled': '#CCCCCC',
  'van-text-color-placeholder': '#CCCCCC',
  
  // Background
  'van-background-color': '#F5F5F5',
  'van-background-color-light': '#FFFFFF',
  
  // Border
  'van-border-color': '#E8E8E8',
  'van-border-color-light': '#F0F0F0',
  
  // Button
  'van-button-primary-background-color': '#4D8076',
  'van-button-primary-border-color': '#4D8076',
  'van-button-primary-text-color': '#FFFFFF',
  'van-button-primary-background-color-on-activated': '#3D6B63',
  'van-button-default-background-color': '#FFFFFF',
  'van-button-default-border-color': '#E8E8E8',
  'van-button-default-text-color': '#333333',
  'van-button-plain-background-color': '#FFFFFF',
  'van-button-plain-border-color': '#4D8076',
  'van-button-plain-text-color': '#4D8076',
  'van-button-border-radius': '8px',
  'van-button-normal-font-size': '14px',
  'van-button-normal-padding': '10px 24px',
  'van-button-normal-line-height': '1.5',
  
  // Cell
  'van-cell-background-color': '#FFFFFF',
  'van-cell-text-color': '#333333',
  'van-cell-border-color': '#F0F0F0',
  'van-cell-padding': '12px 16px',
  'van-cell-title-font-size': '14px',
  'van-cell-label-font-size': '12px',
  'van-cell-label-color': '#999999',
  
  // Field (Input)
  'van-field-background-color': '#FFFFFF',
  'van-field-text-color': '#333333',
  'van-field-placeholder-text-color': '#CCCCCC',
  'van-field-border-color': '#E8E8E8',
  'van-field-border-radius': '8px',
  'van-field-input-font-size': '14px',
  'van-field-label-font-size': '14px',
  'van-field-label-color': '#333333',
  
  // Card
  'van-card-background-color': '#FFFFFF',
  'van-card-border-radius': '12px',
  'van-card-padding': '16px',
  'van-card-title-color': '#333333',
  'van-card-title-font-size': '16px',
  'van-card-price-color': '#4D8076',
  
  // Tabbar
  'van-tabbar-background-color': '#FFFFFF',
  'van-tabbar-item-text-color': '#999999',
  'van-tabbar-item-active-text-color': '#4D8076',
  'van-tabbar-item-font-size': '10px',
  'van-tabbar-icon-size': '24px',
  'van-tabbar-height': '50px',
  
  // NavBar
  'van-nav-bar-background-color': '#FFFFFF',
  'van-nav-bar-text-color': '#333333',
  'van-nav-bar-title-text-color': '#333333',
  'van-nav-bar-title-font-size': '16px',
  'van-nav-bar-height': '44px',
  'van-nav-bar-icon-color': '#333333',
  'van-nav-bar-icon-size': '24px',
  'van-nav-bar-border-color': '#F0F0F0',
  
  // Tag
  'van-tag-primary-color': '#4D8076',
  'van-tag-primary-background-color': 'rgba(77, 128, 118, 0.1)',
  'van-tag-success-color': '#52C41A',
  'van-tag-success-background-color': 'rgba(82, 196, 26, 0.1)',
  'van-tag-warning-color': '#FFB800',
  'van-tag-warning-background-color': 'rgba(255, 184, 0, 0.1)',
  'van-tag-danger-color': '#FF6B6B',
  'van-tag-danger-background-color': 'rgba(255, 107, 107, 0.1)',
  'van-tag-border-radius': '4px',
  'van-tag-font-size': '12px',
  'van-tag-padding': '4px 10px',
  
  // Badge
  'van-badge-color': '#FFFFFF',
  'van-badge-background-color': '#FF6B6B',
  'van-badge-font-size': '10px',
  'van-badge-size': '16px',
  
  // Switch
  'van-switch-background-color': '#E8E8E8',
  'van-switch-on-background-color': '#4D8076',
  'van-switch-size': '20px',
  'van-switch-node-size': '16px',
  
  // Loading
  'van-loading-text-color': '#999999',
  'van-loading-spinner-color': '#4D8076',
  
  // Empty
  'van-empty-description-color': '#999999',
  'van-empty-description-font-size': '14px',
  
  // Toast
  'van-toast-text-color': '#FFFFFF',
  'van-toast-background-color': 'rgba(0, 0, 0, 0.7)',
  'van-toast-border-radius': '8px',
  'van-toast-font-size': '14px',
  'van-toast-line-height': '1.5',
  
  // Dialog
  'van-dialog-background': '#FFFFFF',
  'van-dialog-title-font-size': '16px',
  'van-dialog-title-color': '#333333',
  'van-dialog-content-font-size': '14px',
  'van-dialog-content-color': '#666666',
  'van-dialog-confirm-button-text-color': '#4D8076',
  'van-dialog-cancel-button-text-color': '#999999',
  'van-dialog-border-radius': '16px',
  
  // Popup
  'van-popup-background-color': '#FFFFFF',
  'van-popup-border-radius': '16px',
  
  // Picker
  'van-picker-background': '#FFFFFF',
  'van-picker-toolbar-title-color': '#333333',
  'van-picker-toolbar-confirm-button-color': '#4D8076',
  'van-picker-toolbar-cancel-button-color': '#999999',
  
  // Calendar
  'van-calendar-background-color': '#FFFFFF',
  'van-calendar-header-text-color': '#333333',
  'van-calendar-selected-day-background-color': '#4D8076',
  'van-calendar-selected-day-text-color': '#FFFFFF',
  
  // Slider
  'van-slider-active-background': '#4D8076',
  'van-slider-inactive-background': '#E8E8E8',
  'van-slider-bar-height': '4px',
  'van-slider-button-size': '20px',
  
  // Checkbox & Radio
  'van-checkbox-icon-size': '20px',
  'van-checkbox-checked-icon-color': '#4D8076',
  'van-radio-checked-icon-color': '#4D8076',
  
  // NoticeBar
  'van-notice-bar-background-color': 'rgba(77, 128, 118, 0.1)',
  'van-notice-bar-text-color': '#4D8076',
  
  // Search
  'van-search-background-color': '#F5F5F5',
  'van-search-content-background-color': '#FFFFFF',
  'van-search-input-text-color': '#333333',
  'van-search-input-placeholder-text-color': '#CCCCCC',
  
  // Steps
  'van-steps-finish-text-color': '#4D8076',
  'van-steps-finish-icon-color': '#4D8076',
  
  // Collapse
  'van-collapse-item-background-color': '#FFFFFF',
  'van-collapse-item-content-background-color': '#F7F8FA',
  'van-collapse-item-content-text-color': '#666666',
  
  // Divider
  'van-divider-border-color': '#E8E8E8',
  'van-divider-text-color': '#999999',
  
  // Image
  'van-image-border-radius': '8px',
  
  // Avatar
  'van-avatar-background-color': '#4D8076',
  'van-avatar-text-color': '#FFFFFF',
  'van-avatar-small-size': '32px',
  'van-avatar-normal-size': '40px',
  'van-avatar-large-size': '64px',
  'van-avatar-border-radius': '50%',
};

// ============================================
// Vite 配置示例
// ============================================

/**
 * Vite 配置示例 (vite.config.js)
 * 
 * import { defineConfig } from 'vite';
 * import vue from '@vitejs/plugin-vue';
 * import Components from 'unplugin-vue-components/vite';
 * import { VantResolver } from 'unplugin-vue-components/resolvers';
 * 
 * export default defineConfig({
 *   plugins: [
 *     vue(),
 *     Components({
 *       resolvers: [VantResolver()],
 *     }),
 *   ],
 *   css: {
 *     preprocessorOptions: {
 *       scss: {
 *         additionalData: `
 *           @use "@/styles/vant-theme.scss" as *;
 *         `,
 *       },
 *     },
 *   },
 * });
 */

// ============================================
// SCSS 变量文件内容
// ============================================

export const vantThemeScss = `
// Snail 项目 Vant 主题变量覆盖
// 文件：src/styles/vant-theme.scss

// 主色调
$van-blue: #4D8076;
$van-green: #52C41A;
$van-yellow: #FFB800;
$van-red: #FF6B6B;

// 文字颜色
$van-text-color: #333333;
$van-text-color-secondary: #999999;
$van-text-color-disabled: #CCCCCC;
$van-text-color-placeholder: #CCCCCC;

// 背景颜色
$van-background-color: #F5F5F5;
$van-background-color-light: #FFFFFF;

// 边框颜色
$van-border-color: #E8E8E8;
$van-border-color-light: #F0F0F0;

// 按钮
$van-button-primary-background-color: #4D8076;
$van-button-primary-border-color: #4D8076;
$van-button-primary-background-color-on-activated: #3D6B63;
$van-button-border-radius: 8px;

// 卡片
$van-card-border-radius: 12px;

// 弹窗
$van-dialog-border-radius: 16px;
$van-popup-border-radius: 16px;

// 标签
$van-tag-border-radius: 4px;

// 输入框
$van-field-border-radius: 8px;
`;

// ============================================
// 动态设置 CSS 变量函数
// ============================================

/**
 * 动态设置 Vant 主题 CSS 变量
 * 适用于运行时主题切换
 */
export function setVantTheme(themeVars) {
  const root = document.documentElement;
  
  Object.entries(themeVars).forEach(([key, value]) => {
    root.style.setProperty(`--${key}`, value);
  });
}

/**
 * 初始化 Snail 主题
 */
export function initSnailTheme() {
  setVantTheme(vantCssVars);
}

export default vantThemeConfig;
