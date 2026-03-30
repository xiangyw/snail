<template>
  <div class="content-list">
    <h2>内容列表</h2>
    
    <!-- 内容筛选 -->
    <div class="filter-bar">
      <van-button 
        :type="currentTab === 'all' ? 'primary' : 'default'" 
        @click="switchTab('all')"
      >
        全部
      </van-button>
      <van-button 
        :type="currentTab === 'my' ? 'primary' : 'default'" 
        @click="switchTab('my')"
      >
        我的
      </van-button>
    </div>
    
    <!-- 加载状态 -->
    <van-loading v-if="loading" class="loading" />
    
    <!-- 内容列表 -->
    <div v-else class="content-items">
      <div 
        v-for="item in contents" 
        :key="item.id" 
        class="content-item"
        @click="goToDetail(item.id!)"
      >
        <div class="content-header">
          <h3>{{ item.title }}</h3>
          <span class="content-type">{{ contentTypeText[item.type] }}</span>
        </div>
        <div class="content-preview" v-html="truncateText(item.content, 100)"></div>
        <div class="content-meta">
          <span class="view-count">浏览: {{ item.viewCount || 0 }}</span>
          <span class="like-count">点赞: {{ item.likeCount || 0 }}</span>
          <span class="comment-count">评论: {{ item.commentCount || 0 }}</span>
          <span class="date">{{ formatDate(item.createdAt!) }}</span>
        </div>
      </div>
    </div>
    
    <!-- 空状态 -->
    <van-empty v-if="!loading && contents.length === 0" description="暂无内容" />
    
    <!-- 发布按钮 -->
    <van-button 
      class="publish-btn" 
      type="primary" 
      icon="plus" 
      @click="goToPublish"
      round
    >
      发布内容
    </van-button>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { Content, getContentList, getMyContentList } from '@/api/content';
import { formatDate } from '@/utils/format';

const router = useRouter();

// 数据
const contents = ref<Content[]>([]);
const loading = ref(false);
const currentTab = ref<'all' | 'my'>('all');

// 类型文本映射
const contentTypeText = {
  TEXT: '文字',
  IMAGE: '图片',
  VIDEO: '视频',
  AUDIO: '音频',
  LINK: '链接'
};

// 方法
const truncateText = (text: string, maxLength: number) => {
  if (!text) return '';
  return text.length > maxLength ? text.substring(0, maxLength) + '...' : text;
};

const switchTab = async (tab: 'all' | 'my') => {
  currentTab.value = tab;
  await loadContents();
};

const loadContents = async () => {
  loading.value = true;
  try {
    if (currentTab.value === 'my') {
      const response = await getMyContentList();
      contents.value = response.data;
    } else {
      const response = await getContentList();
      contents.value = response.data;
    }
  } catch (error) {
    console.error('加载内容失败:', error);
    // 这里应该显示错误提示
  } finally {
    loading.value = false;
  }
};

const goToDetail = (id: number) => {
  router.push(`/content/detail/${id}`);
};

const goToPublish = () => {
  router.push('/content/publish');
};

// 初始化
onMounted(() => {
  loadContents();
});
</script>

<style scoped>
.content-list {
  padding: 16px;
  min-height: 100vh;
  background-color: #f7f8fa;
}

.filter-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 16px;
}

.content-items {
  margin-bottom: 80px; /* 为底部按钮留出空间 */
}

.content-item {
  background: white;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 12px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  cursor: pointer;
}

.content-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.content-header h3 {
  margin: 0;
  font-size: 16px;
  color: #323233;
  flex: 1;
  margin-right: 10px;
}

.content-type {
  font-size: 12px;
  color: #1989fa;
  background-color: #ecf9ff;
  padding: 2px 6px;
  border-radius: 4px;
}

.content-preview {
  color: #646566;
  font-size: 14px;
  line-height: 1.5;
  margin-bottom: 12px;
}

.content-meta {
  display: flex;
  justify-content: space-between;
  color: #969799;
  font-size: 12px;
}

.publish-btn {
  position: fixed;
  bottom: 20px;
  right: 20px;
  width: 60px;
  height: 60px;
  z-index: 100;
}

.loading {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 40px;
}
</style>
</template>