import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount, flushPromises } from '@vue/test-utils'
import Home from '../views/Home/index.vue'

describe('Home 组件测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('应该正确渲染页面结构', () => {
    const wrapper = mount(Home)
    
    expect(wrapper.find('.home-page').exists()).toBe(true)
  })

  it('组件应该有正确的初始状态', () => {
    const wrapper = mount(Home)
    
    expect(wrapper.vm.searchValue).toBe('')
    expect(wrapper.vm.loading).toBe(false)
    expect(wrapper.vm.finished).toBe(false)
    expect(wrapper.vm.products).toEqual([])
  })

  it('应该定义 notice 数据', () => {
    const wrapper = mount(Home)
    
    expect(wrapper.vm.notice).toBeTruthy()
  })

  it('onSearch 方法应该存在', () => {
    const wrapper = mount(Home)
    
    expect(typeof wrapper.vm.onSearch).toBe('function')
  })

  it('onLoad 方法应该存在', () => {
    const wrapper = mount(Home)
    
    expect(typeof wrapper.vm.onLoad).toBe('function')
  })

  it('goToDetail 方法应该存在', () => {
    const wrapper = mount(Home)
    
    expect(typeof wrapper.vm.goToDetail).toBe('function')
  })

  it('onSearch - 空搜索不跳转', async () => {
    const wrapper = mount(Home)
    const originalPush = wrapper.vm.$router.push
    wrapper.vm.$router.push = vi.fn()
    
    wrapper.vm.searchValue = ''
    wrapper.vm.onSearch()
    
    expect(wrapper.vm.$router.push).not.toHaveBeenCalled()
    
    wrapper.vm.$router.push = originalPush
  })

  it('onSearch - 有搜索词时跳转', async () => {
    const wrapper = mount(Home)
    const pushSpy = vi.fn()
    wrapper.vm.$router.push = pushSpy
    
    wrapper.vm.searchValue = '测试商品'
    wrapper.vm.onSearch()
    
    expect(pushSpy).toHaveBeenCalled()
  })

  it('onLoad 应该加载商品数据', async () => {
    const wrapper = mount(Home)
    
    wrapper.vm.onLoad()
    
    // 等待 setTimeout
    await new Promise(resolve => setTimeout(resolve, 1100))
    await flushPromises()
    
    expect(wrapper.vm.products.length).toBeGreaterThan(0)
    expect(wrapper.vm.loading).toBe(false)
  })

  it('goToDetail 应该调用 router.push', async () => {
    const wrapper = mount(Home)
    const pushSpy = vi.fn()
    wrapper.vm.$router.push = pushSpy
    
    wrapper.vm.goToDetail(123)
    
    expect(pushSpy).toHaveBeenCalledWith('/product/123')
  })

  it('商品列表加载后应该设置 finished 状态', async () => {
    const wrapper = mount(Home)
    
    // 模拟多次加载
    for (let i = 0; i < 5; i++) {
      wrapper.vm.onLoad()
      await new Promise(resolve => setTimeout(resolve, 1100))
      await flushPromises()
    }
    
    // 当商品数量 >= 10 时应该设置 finished
    expect(wrapper.vm.finished).toBe(true)
  })
})
