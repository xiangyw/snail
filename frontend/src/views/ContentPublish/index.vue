<template>
  <div class="content-publish">
    <van-nav-bar
      :title="isEditMode ? '编辑内容' : '发布内容'"
      left-text="取消"
      :right-text="isEditMode ? '更新' : '发布'"
      left-arrow
      @click-left="handleCancel"
      @click-right="handleSubmit"
    />
    
    <div class="publish-form">
      <!-- 标题输入 -->
      <van-field
        v-model="formData.title"
        label="标题"
        placeholder="请输入标题"
        maxlength="100"
        show-word-limit
        :rules="[{ required: true, message: '请填写标题' }]"
      />
      
      <!-- 内容类型选择 -->
      <van-cell-group inset>
        <van-cell title="内容类型" :value="contentTypeText[formData.type]" is-link @click="showTypePicker = true" />
      </van-cell-group>
      
      <!-- 内容输入 -->
      <van-field
        v-if="formData.type === 'TEXT'"
        v-model="formData.content"
        rows="6"
        autosize
        type="textarea"
        maxlength="5000"
        placeholder="请输入内容..."
        show-word-limit
        :rules="[{ required: true, message: '请输入内容' }]"
      />
      
      <!-- 图片上传 -->
      <div v-if="formData.type === 'IMAGE'" class="upload-section">
        <van-uploader 
          v-model="imageFiles" 
          multiple 
          :max-count="9"
          :after-read="onImageUpload"
        />
      </div>
      
      <!-- 视频上传 -->
      <div v-if="formData.type === 'VIDEO'" class="upload-section">
        <van-uploader 
          v-model="videoFiles" 
          :max-count="1"
          accept="video/*"
          :after-read="onVideoUpload"
        />
      </div>
      
      <!-- 可见性设置 -->
      <van-cell-group inset>
        <van-cell title="可见性" :value="visibilityText[formData.visibility]" is-link @click="showVisibilityPicker = true" />
      </van-cell-group>
    </div>
    
    <!-- 类型选择弹窗 -->
    <van-action-sheet v-model:show="showTypePicker" :actions="typeActions" @select="onTypeSelect" />
    
    <!-- 可见性选择弹窗 -->
    <van-action-sheet v-model:show="showVisibilityPicker" :actions="visibilityActions" @select="onVisibilitySelect" />
    
    <!-- 提交状态 -->
    <van-loading v-if="submitting" class="submit-loading" />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { showToast, showConfirmDialog } from 'vant';
import { Content, createContent, updateContent, uploadFile, getContentById } from '@/api/content';

const router = useRouter();
const route = useRoute();

// 检查是否为编辑模式
const isEditMode = !!route.params.id;
const contentId = Number(route.params.id);

// 表单数据
const formData = reactive<Content>({
  title: '',
  content: '',
  type: 'TEXT',
  visibility: 'PUBLISHED'
} as Content);

// 文件上传
const imageFiles = ref<any[]>([]);
const videoFiles = ref<any[]>([]);

// 控制器
const showTypePicker = ref(false);
const showVisibilityPicker = ref(false);
const submitting = ref(false);

// 选项配置
const contentTypeText: Record<string, string> = {
  TEXT: '文字',
  IMAGE: '图片',
  VIDEO: '视频',
  AUDIO: '音频',
  LINK: '链接'
};

const visibilityText: Record<string, string> = {
  DRAFT: '草稿',
  PUBLISHED: '已发布',
  ARCHIVED: '已归档',
  DELETED: '已删除'
};

const typeActions = [
  { name: '文字', value: 'TEXT' },
  { name: '图片', value: 'IMAGE' },
  { name: '视频', value: 'VIDEO' },
  { name: '音频', value: 'AUDIO' },
  { name: '链接', value: 'LINK' }
];

const visibilityActions = [
  { name: '草稿', value: 'DRAFT' },
  { name: '已发布', value: 'PUBLISHED' },
  { name: '已归档', value: 'ARCHIVED' },
  { name: '已删除', value: 'DELETED' }
];

// 方法
const onTypeSelect = (action: { value: string }) => {
  formData.type = action.value as any;
  showTypePicker.value = false;
};

const onVisibilitySelect = (action: { value: string }) => {
  formData.visibility = action.value as any;
  showVisibilityPicker.value = false;
};

const onImageUpload = async (file: any) => {
  if (file.file) {
    try {
      const response = await uploadFile(file.file, 'image');
      // 在这里我们可以保存上传后的文件URL
      // 对于图片，我们可能需要将其添加到内容中
      if (!formData.content) {
        formData.content = '';
      }
      formData.content += `\n![图片](${response.data.url})`;
    } catch (error) {
      console.error('图片上传失败:', error);
      showToast('图片上传失败');
    }
  }
};

const onVideoUpload = async (file: any) => {
  if (file.file) {
    try {
      const response = await uploadFile(file.file, 'video');
      // 对于视频，我们也添加到内容中
      if (!formData.content) {
        formData.content = '';
      }
      formData.content += `\n![视频](${response.data.url})`;
    } catch (error) {
      console.error('视频上传失败:', error);
      showToast('视频上传失败');
    }
  }
};

const validateForm = () => {
  if (!formData.title.trim()) {
    showToast('请输入标题');
    return false;
  }
  
  if (formData.type === 'TEXT' && !formData.content.trim()) {
    showToast('请输入内容');
    return false;
  }
  
  if ((formData.type === 'IMAGE' || formData.type === 'VIDEO') && !formData.content.trim()) {
    showToast('请至少上传一个媒体文件');
    return false;
  }
  
  return true;
};

const handleSubmit = async () => {
  if (!validateForm()) {
    return;
  }
  
  submitting.value = true;
  
  try {
    let result;
    if (isEditMode && contentId) {
      // 编辑模式
      result = await updateContent(contentId, formData);
      showToast('更新成功');
      router.push(`/content/detail/${contentId}`);
    } else {
      // 新建模式
      result = await createContent(formData);
      showToast('发布成功');
      router.push(`/content/detail/${result.data.id}`);
    }
  } catch (error) {
    console.error('操作失败:', error);
    showToast(isEditMode ? '更新失败，请重试' : '发布失败，请重试');
  } finally {
    submitting.value = false;
  }
};

// 加载编辑内容
const loadContentForEdit = async () => {
  if (isEditMode && contentId) {
    try {
      const response = await getContentById(contentId);
      const contentData = response.data;
      
      // 更新表单数据
      formData.title = contentData.title || '';
      formData.content = contentData.content || '';
      formData.type = contentData.type || 'TEXT';
      formData.visibility = contentData.visibility || 'PUBLISHED';
    } catch (error) {
      console.error('加载内容失败:', error);
      showToast('加载内容失败');
      router.back();
    }
  }
};

const handleCancel = async () => {
  if (formData.title || formData.content) {
    try {
      await showConfirmDialog({
        title: '确认取消',
        message: '您确定要取消发布吗？未保存的内容将会丢失。',
      });
      router.back();
    } catch {
      // 用户取消了操作
    }
  } else {
    router.back();
  }
};

// 初始化
onMounted(() => {
  if (isEditMode) {
    loadContentForEdit();
  }
});
</script>

<style scoped>
.content-publish {
  min-height: 100vh;
  background-color: #f7f8fa;
}

.publish-form {
  padding-top: 10px;
}

.upload-section {
  padding: 16px;
  background: white;
  margin: 10px 16px;
  border-radius: 8px;
}

.submit-loading {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
</style>
</template>