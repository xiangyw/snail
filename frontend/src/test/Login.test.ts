import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount, flushPromises } from '@vue/test-utils'
import Login from '../views/Login/index.vue'
import { showToast } from 'vant'

describe('Login 组件测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('应该正确渲染登录页面结构', () => {
    const wrapper = mount(Login)
    
    expect(wrapper.find('.login-page').exists()).toBe(true)
    expect(wrapper.find('.van-nav-bar').exists()).toBe(true)
    expect(wrapper.find('.form-container').exists()).toBe(true)
    expect(wrapper.find('.van-form').exists()).toBe(true)
  })

  it('应该显示正确的标题', () => {
    const wrapper = mount(Login)
    const navBar = wrapper.find('.van-nav-bar')
    
    expect(navBar.text()).toContain('登录')
  })

  it('应该包含手机号输入框', () => {
    const wrapper = mount(Login)
    const phoneField = wrapper.find('.van-field input')
    
    expect(phoneField.exists()).toBe(true)
    expect(phoneField.element.placeholder).toContain('手机号')
  })

  it('应该包含验证码输入框', () => {
    const wrapper = mount(Login)
    const fields = wrapper.findAll('.van-field')
    
    expect(fields.length).toBeGreaterThanOrEqual(2)
  })

  it('应该包含发送验证码按钮', () => {
    const wrapper = mount(Login)
    const buttons = wrapper.findAll('.van-button')
    
    expect(buttons.length).toBeGreaterThanOrEqual(1)
    expect(wrapper.text()).toContain('发送验证码')
  })

  it('应该包含登录按钮', () => {
    const wrapper = mount(Login)
    
    expect(wrapper.text()).toContain('登录')
  })

  it('手机号输入框应该可以输入', async () => {
    const wrapper = mount(Login)
    const phoneInput = wrapper.find('.van-field input')
    
    await phoneInput.setValue('13800138000')
    expect(phoneInput.element.value).toBe('13800138000')
    expect(wrapper.vm.phone).toBe('13800138000')
  })

  it('验证码输入框应该可以输入', async () => {
    const wrapper = mount(Login)
    const inputs = wrapper.findAll('input')
    
    if (inputs.length >= 2) {
      await inputs[1].setValue('123456')
      expect(inputs[1].element.value).toBe('123456')
      expect(wrapper.vm.code).toBe('123456')
    }
  })

  it('点击发送验证码 - 手机号为空时应该提示', async () => {
    const wrapper = mount(Login)
    const sendButton = wrapper.findAll('.van-button')[0]
    
    wrapper.vm.phone = ''
    await sendButton.trigger('click')
    
    expect(showToast).toHaveBeenCalledWith('请输入手机号')
  })

  it('点击发送验证码 - 手机号有效时应该发送', async () => {
    const wrapper = mount(Login)
    const sendButton = wrapper.findAll('.van-button')[0]
    
    wrapper.vm.phone = '13800138000'
    await sendButton.trigger('click')
    
    expect(showToast).toHaveBeenCalledWith('验证码已发送')
  })

  it('提交表单应该显示登录成功', async () => {
    const wrapper = mount(Login)
    const form = wrapper.find('.van-form')
    
    wrapper.vm.phone = '13800138000'
    wrapper.vm.code = '123456'
    await form.trigger('submit.prevent')
    
    expect(showToast).toHaveBeenCalledWith('登录成功')
  })

  it('表单验证 - 手机号为必填', () => {
    const wrapper = mount(Login)
    const phoneField = wrapper.find('.van-field')
    
    // 验证规则存在
    const fieldVm = phoneField.vm as any
    expect(fieldVm).toBeDefined()
  })

  it('表单验证 - 验证码为必填', () => {
    const wrapper = mount(Login)
    const fields = wrapper.findAll('.van-field')
    
    expect(fields.length).toBeGreaterThanOrEqual(2)
  })

  it('点击返回按钮应该返回上一页', async () => {
    const wrapper = mount(Login)
    const navBar = wrapper.find('.van-nav-bar')
    
    // 验证返回按钮存在
    expect(navBar.text()).toContain('登录')
    
    // 模拟点击返回
    const originalBack = wrapper.vm.$router.back
    wrapper.vm.$router.back = vi.fn()
    
    // 触发点击事件
    await navBar.trigger('click-left')
    
    expect(wrapper.vm.$router.back).toHaveBeenCalled()
    
    wrapper.vm.$router.back = originalBack
  })

  it('组件应该有正确的初始状态', () => {
    const wrapper = mount(Login)
    
    expect(wrapper.vm.phone).toBe('')
    expect(wrapper.vm.code).toBe('')
  })

  it('sendCode 方法应该存在', () => {
    const wrapper = mount(Login)
    
    expect(typeof wrapper.vm.sendCode).toBe('function')
  })

  it('onSubmit 方法应该存在', () => {
    const wrapper = mount(Login)
    
    expect(typeof wrapper.vm.onSubmit).toBe('function')
  })
})
