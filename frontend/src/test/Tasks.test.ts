import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount, flushPromises } from '@vue/test-utils'
import Tasks from '../views/Tasks/index.vue'

describe('Tasks 组件测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('应该正确渲染页面结构', () => {
    const wrapper = mount(Tasks)
    
    expect(wrapper.find('.tasks-page').exists()).toBe(true)
  })

  it('组件应该有正确的初始状态', () => {
    const wrapper = mount(Tasks)
    
    expect(wrapper.vm.activeTab).toBe('all')
    expect(wrapper.vm.loading).toBe(false)
    expect(wrapper.vm.finished).toBe(false)
    expect(wrapper.vm.taskList).toEqual([])
  })

  it('onTabChange 方法应该存在', () => {
    const wrapper = mount(Tasks)
    
    expect(typeof wrapper.vm.onTabChange).toBe('function')
  })

  it('onLoad 方法应该存在', () => {
    const wrapper = mount(Tasks)
    
    expect(typeof wrapper.vm.onLoad).toBe('function')
  })

  it('goToDetail 方法应该存在', () => {
    const wrapper = mount(Tasks)
    
    expect(typeof wrapper.vm.goToDetail).toBe('function')
  })

  it('切换标签页应该重置列表', async () => {
    const wrapper = mount(Tasks)
    
    // 先添加一些数据
    wrapper.vm.taskList.push({ id: 1, title: '测试任务', status: 'doing' })
    expect(wrapper.vm.taskList.length).toBe(1)
    
    // 切换标签
    wrapper.vm.onTabChange('doing')
    
    expect(wrapper.vm.taskList).toEqual([])
    expect(wrapper.vm.finished).toBe(false)
    expect(wrapper.vm.loading).toBe(true)
  })

  it('onLoad 应该加载任务数据', async () => {
    const wrapper = mount(Tasks)
    
    wrapper.vm.onLoad()
    
    // 等待 setTimeout
    await new Promise(resolve => setTimeout(resolve, 1100))
    await flushPromises()
    
    expect(wrapper.vm.taskList.length).toBeGreaterThan(0)
    expect(wrapper.vm.loading).toBe(false)
  })

  it('加载的任务应该包含正确的字段', async () => {
    const wrapper = mount(Tasks)
    
    wrapper.vm.onLoad()
    await new Promise(resolve => setTimeout(resolve, 1100))
    await flushPromises()
    
    const firstTask = wrapper.vm.taskList[0]
    expect(firstTask).toHaveProperty('id')
    expect(firstTask).toHaveProperty('title')
    expect(firstTask).toHaveProperty('description')
    expect(firstTask).toHaveProperty('status')
  })

  it('goToDetail 应该调用 router.push', async () => {
    const wrapper = mount(Tasks)
    const pushSpy = vi.fn()
    wrapper.vm.$router.push = pushSpy
    
    wrapper.vm.goToDetail(123)
    
    expect(pushSpy).toHaveBeenCalledWith('/tasks/123')
  })

  it('任务状态应该正确设置', async () => {
    const wrapper = mount(Tasks)
    
    wrapper.vm.onLoad()
    await new Promise(resolve => setTimeout(resolve, 1100))
    await flushPromises()
    
    const tasks = wrapper.vm.taskList
    expect(tasks.some(t => t.status === 'doing')).toBe(true)
    expect(tasks.some(t => t.status === 'done')).toBe(true)
  })

  it('activeTab 应该是响应式数据', () => {
    const wrapper = mount(Tasks)
    
    wrapper.vm.activeTab = 'doing'
    expect(wrapper.vm.activeTab).toBe('doing')
    
    wrapper.vm.activeTab = 'done'
    expect(wrapper.vm.activeTab).toBe('done')
  })

  it('任务列表应该支持添加多个任务', async () => {
    const wrapper = mount(Tasks)
    
    // 模拟多次加载
    wrapper.vm.onLoad()
    await new Promise(resolve => setTimeout(resolve, 1100))
    await flushPromises()
    
    const initialLength = wrapper.vm.taskList.length
    expect(initialLength).toBeGreaterThan(0)
    
    // 再次加载
    wrapper.vm.onLoad()
    await new Promise(resolve => setTimeout(resolve, 1100))
    await flushPromises()
    
    expect(wrapper.vm.taskList.length).toBeGreaterThan(initialLength)
  })

  it('组件应该使用 vue-router', () => {
    const wrapper = mount(Tasks)
    
    expect(wrapper.vm.$router).toBeDefined()
  })
})
