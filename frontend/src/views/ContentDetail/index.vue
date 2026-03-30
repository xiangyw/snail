<template>
  <div class="content-detail">
    <van-nav-bar
      title="内容详情"
      left-text="返回"
      left-arrow
      @click-left="router.go(-1)"
    />
    
    <!-- 加载状态 -->
    <van-loading v-if="loading" class="loading" />
    
    <!-- 内容详情 -->
    <div v-else-if="content" class="content-wrapper">
      <div class="content-header">
        <h1 class="title">{{ content.title }}</h1>
        <div class="meta">
          <span class="type">{{ contentTypeText[content.type] }}</span>
          <span class="visibility">{{ visibilityText[content.visibility] }}</span>
          <span class="date">{{ formatDate(content.createdAt!) }}</span>
        </div>
      </div>
      
      <div class="content-body">
        <div v-if="content.type === 'IMAGE'" class="media-content">
          <img 
            v-for="(img, index) in extractMediaUrls(content.content, 'image')" 
            :key="index" 
            :src="img" 
            alt="内容图片"
            class="content-image"
            @click="previewImage(img)"
          />
        </div>
        
        <div v-else-if="content.type === 'VIDEO'" class="media-content">
          <video 
            v-for="(video, index) in extractMediaUrls(content.content, 'video')" 
            :key="index" 
            :src="video" 
            controls 
            class="content-video"
          />
        </div>
        
        <div 
          v-else 
          class="text-content" 
          v-html="formatContent(content.content)"
        ></div>
      </div>
      
      <div class="content-footer">
        <div class="stats">
          <span>浏览: {{ content.viewCount || 0 }}</span>
          <span>点赞: {{ content.likeCount || 0 }}</span>
          <span>评论: {{ content.commentCount || 0 }}</span>
        </div>
        
        <div class="actions">
          <van-button 
            icon="like-o" 
            :type="isLiked ? 'primary' : 'default'"
            @click="toggleLike"
          >
            {{ isLiked ? '已点赞' : '点赞' }} {{ content.likeCount || 0 }}
          </van-button>
          
          <van-button 
            icon="comment-o" 
            type="default"
            @click="goToComments"
          >
            评论 {{ content.commentCount || 0 }}
          </van-button>
          
          <van-button 
            icon="share" 
            type="default"
            @click="showShareActionSheet = true"
          >
            分享
          </van-button>
        </div>
      </div>
      
      <!-- 编辑/删除按钮（仅对作者显示） -->
      <div v-if="isOwner" class="owner-actions">
        <van-button 
          type="primary" 
          size="small" 
          @click="editContent"
        >
          编辑
        </van-button>
        <van-button 
          type="danger" 
          size="small" 
          @click="deleteContent"
        >
          删除
        </van-button>
      </div>
    </div>
    
    <!-- 错误状态 -->
    <van-empty v-else-if="error" description="内容不存在或已被删除" />
    
    <!-- 分享弹窗 -->
    <van-action-sheet 
      v-model:show="showShareActionSheet" 
      :actions="shareActions" 
      @select="onShareSelect" 
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { showToast, showConfirmDialog } from 'vant';
import { 
  Content, 
  getContentById, 
  deleteContent as deleteContentApi
} from '@/api/content';
import { formatDate } from '@/utils/format';
import { useAuthStore } from '@/stores/auth';

const router = useRouter();
const route = useRoute();

// 数据
const content = ref<Content | null>(null);
const loading = ref(true);
const error = ref(false);
const isLiked = ref(false);
const showShareActionSheet = ref(false);

// 导入认证store
import { useAuthStore } from '@/stores/auth';
const authStore = useAuthStore();

// 从认证store获取当前用户ID
const currentUserId = computed(() => authStore.userId);

// 计算属性 - 判断是否为内容所有者
const isOwner = computed(() => {
  return content.value?.userId === currentUserId.value;
});

// 在组件挂载时初始化认证状态
onMounted(() => {
  authStore.hydrate();
  loadContent();
});

// 类型文本映射
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

const shareActions = [
  { name: '微信', icon: 'wechat' },
  { name: '朋友圈', icon: 'share' },
  { name: '复制链接', icon: 'link' },
  { name: '更多', icon: 'more-o' }
];

// 方法
const loadContent = async () => {
  const id = Number(route.params.id);
  if (isNaN(id)) {
    error.value = true;
    loading.value = false;
    return;
  }

  try {
    const response = await getContentById(id);
    content.value = response.data;
  } catch (err) {
    console.error('加载内容失败:', err);
    error.value = true;
  } finally {
    loading.value = false;
  }
};

const extractMediaUrls = (contentStr: string, type: 'image' | 'video') => {
  if (!contentStr) return [];
  
  const regex = type === 'image' 
    ? /!\[图片\]\(([^)]+)\)/g 
    : /!\[视频\]\(([^)]+)\)/g;
    
  const urls: string[] = [];
  let match;
  
  while ((match = regex.exec(contentStr)) !== null) {
    urls.push(match[1]);
  }
  
  return urls;
};

const formatContent = (contentStr: string) => {
  if (!contentStr) return '';
  
  // 替换图片标记为实际的图片标签
  let formatted = contentStr.replace(/!\[图片\]\(([^)]+)\)/g, '');
  
  // 替换视频标记
  formatted = formatted.replace(/!\[视频\]\(([^)]+)\)/g, '');
  
  // 将换行符转换为<br>
  formatted = formatted.replace(/\n/g, '<br>');
  
  return formatted;
};

const previewImage = (imgUrl: string) => {
  // 图片预览逻辑
  console.log('Preview image:', imgUrl);
};

const toggleLike = () => {
  isLiked.value = !isLiked.value;
  // 实际的点赞/取消点赞API调用
  console.log('Toggle like for content:', content.value?.id);
};

const goToComments = () => {
  // 跳转到评论页
  console.log('Go to comments for content:', content.value?.id);
};

const onShareSelect = (action: any) => {
  showShareActionSheet.value = false;
  console.log('Share action:', action.name);
};

const editContent = () => {
  if (content.value?.id) {
    router.push(`/content/edit/${content.value.id}`);
  }
};

const deleteContent = async () => {
  try {
    await showConfirmDialog({
      title: '确认删除',
      message: '您确定要删除这个内容吗？此操作不可恢复。',
    });

    if (content.value?.id) {
      await deleteContentApi(content.value.id);
      showToast('删除成功');
      router.push('/content/list'); // 返回内容列表
    }
  } catch {
    // 用户取消了操作
  }
};

// 初始化
onMounted(() => {
  loadContent();
});
</script>

<style scoped>
.content-detail {
  min-height: 100vh;
  background-color: #f7f8fa;
}

.content-wrapper {
  padding: 16px;
}

.content-header {
  background: white;
  padding: 16px;
  border-radius: 8px;
  margin-bottom: 16px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.title {
  margin: 0 0 12px 0;
  font-size: 20px;
  color: #323233;
  line-height: 1.4;
}

.meta {
  display: flex;
  gap: 12px;
  font-size: 12px;
  color: #969799;
}

.meta .type,
.meta .visibility {
  padding: 2px 6px;
  border-radius: 4px;
  background-color: #f2f3f5;
}

.content-body {
  background: white;
  padding: 16px;
  border-radius: 8px;
  margin-bottom: 16px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.text-content {
  color: #323233;
  line-height: 1.6;
  word-wrap: break-word;
}

.media-content {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.content-image {
  width: 100%;
  border-radius: 8px;
  cursor: pointer;
}

.content-video {
  width: 100%;
  border-radius: 8px;
}

.content-footer {
  background: white;
  padding: 16px;
  border-radius: 8px;
  margin-bottom: 16px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.stats {
  display: flex;
  justify-content: space-around;
  margin-bottom: 16px;
  color: #969799;
  font-size: 14px;
}

.actions {
  display: flex;
  gap: 10px;
  justify-content: center;
}

.owner-actions {
  display: flex;
  gap: 12px;
  padding: 0 16px 20px;
  justify-content: center;
}

.loading {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 40px;
}
</style>
</template>