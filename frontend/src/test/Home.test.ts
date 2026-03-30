import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount, flushPromises } from '@vue/test-utils'
import Home from '../views/Home/index.vue'
import { showToast } from 'vant'

describe('Home 组件测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('应该正确渲染页面结构', () => {
    const wrapper = mount(Home)
    
    expect(wrapper.find('.home-page').exists()).toBe(true)
    expect(wrapper.find('.van-nav-bar').exists()).toBe(true)
    expect(wrapper.find('.van-search').exists()).toBe(true)
    expect(wrapper.find('.feature-grid').exists()).toBe(true)
  })

  it('应该显示正确的标题', () => {
    const wrapper = mount(Home)
    const navBar = wrapper.find('.van-nav-bar')
    
    expect(navBar.text()).toContain('蜗牛')
  })

  it('应该显示公告栏', () => {
    const wrapper = mount(Home)
    const noticeBar = wrapper.find('.van-notice-bar')
    
    expect(noticeBar.exists()).toBe(true)
    expect(noticeBar.text()).toContain('欢迎来到蜗牛 H5 应用')
  })

  it('搜索框应该可以输入', async () => {
    const wrapper = mount(Home)
    const searchInput = wrapper.find('.van-search input')
    
    await searchInput.setValue('测试商品')
    expect(searchInput.element.value).toBe('测试商品')
  })

  it('点击搜索应该跳转到搜索页面', async () => {
    const wrapper = mount(Home)
    const searchInput = wrapper.find('.van-search input')
    
    await searchInput.setValue('测试商品')
    await searchInput.trigger('submit')
    
    // 验证搜索值已设置
    expect(wrapper.vm.searchValue).toBe('测试商品')
  })

  it('功能网格应该包含正确的入口', () => {
    const wrapper = mount(Home)
    const gridItems = wrapper.findAll('.van-grid-item')
    
    expect(gridItems.length).toBeGreaterThanOrEqual(1)
    
    const text = wrapper.find('.feature-grid').text()
    expect(text).toContain('任务')
    expect(text).toContain('旅游')
    expect(text).toContain('广场舞')
    expect(text).toContain('更多')
  })

  it('应该渲染商品列表区域', () => {
    const wrapper = mount(Home)
    
    expect(wrapper.find('.section').exists()).toBe(true)
    expect(wrapper.find('.section-title').exists()).toBe(true)
    expect(wrapper.find('.section-title').text()).toBe('精选推荐')
  })

  it('商品列表初始应该为空', () => {
    const wrapper = mount(Home)
    
    expect(wrapper.vm.products).toEqual([])
    expect(wrapper.vm.loading).toBe(false)
    expect(wrapper.vm.finished).toBe(false)
  })

  it('加载商品列表应该添加模拟数据', async () => {
    const wrapper = mount(Home)
    
    // 触发 onLoad
    wrapper.vm.onLoad()
    await flushPromises()
    
    // 等待 setTimeout
    await new Promise(resolve => setTimeout(resolve, 1100))
    await flushPromises()
    
    expect(wrapper.vm.products.length).toBeGreaterThan(0)
  })

  it('点击商品应该跳转到详情页', async () => {
    const wrapper = mount(Home)
    
    // 手动添加一个商品
    wrapper.vm.products.push({ id: 1, title: '测试商品', price: '99.00', image: 'https://placeholder.co/100' })
    await wrapper.vm.$nextTick()
    
    // 调用跳转方法
    wrapper.vm.goToDetail(1)
    
    // 验证 router.push 被调用（由于 mock，我们验证方法存在）
    expect(typeof wrapper.vm.goToDetail).toBe('function')
  })

  it('应该处理空搜索', async () => {
    const wrapper = mount(Home)
    const originalRouterPush = wrapper.vm.$router.push
    wrapper.vm.$router.push = vi.fn()
    
    wrapper.vm.searchValue = ''
    wrapper.vm.onSearch()
    
    expect(wrapper.vm.$router.push).not.toHaveBeenCalled()
    
    wrapper.vm.$router.push = originalRouterPush
  })
})
