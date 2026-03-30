<template>
  <div class="gift-panel">
    <div class="gift-panel__trigger" @click="showPanel = true">
      <van-icon name="gift-o" />
      <span>礼物</span>
    </div>
    
    <van-popup v-model:show="showPanel" position="bottom" round class="gift-popup">
      <div class="gift-popup__content">
        <div class="gift-popup__header">
          <span>选择礼物</span>
          <van-icon name="cross" @click="showPanel = false" />
        </div>
        
        <div class="gift-list">
          <div
            v-for="gift in giftList"
            :key="gift.id"
            class="gift-item"
            :class="{ 'gift-item--selected': selectedGiftId === gift.id }"
            @click="selectGift(gift.id)"
          >
            <div class="gift-item__image">
              <img :src="gift.icon" :alt="gift.name" />
            </div>
            <div class="gift-item__name">{{ gift.name }}</div>
            <div class="gift-item__price">{{ gift.price }} 积分</div>
          </div>
        </div>
        
        <div class="gift-panel__quantity" v-if="selectedGiftId">
          <van-stepper v-model="quantity" min="1" max="99" />
        </div>
        
        <div class="gift-popup__footer">
          <van-button
            type="primary"
            block
            round
            :loading="sending"
            @click="sendGift"
          >
            赠送{{ selectedGift ? `(${selectedGift.price * quantity} 积分)` : '' }}
          </van-button>
        </div>
      </div>
    </van-popup>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { showToast } from 'vant'
import { getGiftList, sendGift as apiSendGift } from '@/api/gift'

interface Gift {
  id: number
  name: string
  icon: string
  price: number
}

const props = defineProps<{
  roomId: number
}>()

const emit = defineEmits<{
  sendGift: [giftId: number, quantity: number]
}>()

const showPanel = ref(false)
const giftList = ref<Gift[]>([])
const selectedGiftId = ref<number | null>(null)
const quantity = ref(1)
const sending = ref(false)

const selectedGift = computed(() => {
  return giftList.value.find(g => g.id === selectedGiftId.value)
})

const loadGifts = async () => {
  try {
    const res = await getGiftList()
    giftList.value = res.data.list || []
  } catch (error) {
    showToast('加载礼物列表失败')
  }
}

const selectGift = (giftId: number) => {
  selectedGiftId.value = giftId
}

const sendGift = async () => {
  if (!selectedGiftId.value) {
    showToast('请选择礼物')
    return
  }
  
  sending.value = true
  try {
    await apiSendGift({
      roomId: props.roomId,
      giftId: selectedGiftId.value,
      quantity: quantity.value
    })
    emit('sendGift', selectedGiftId.value, quantity.value)
    showPanel.value = false
    showToast('赠送成功')
  } catch (error) {
    showToast('赠送失败')
  } finally {
    sending.value = false
  }
}

loadGifts()
</script>

<style scoped>
.gift-panel__trigger {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  padding: 8px 12px;
  color: #fff;
  cursor: pointer;
}

.gift-popup {
  height: 60vh;
}

.gift-popup__content {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.gift-popup__header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  font-size: 16px;
  font-weight: 500;
  border-bottom: 1px solid #eee;
}

.gift-list {
  flex: 1;
  overflow-y: auto;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
  padding: 16px;
}

.gift-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 12px 8px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
}

.gift-item:hover {
  background: #f5f5f5;
}

.gift-item--selected {
  background: #e8f3ff;
  border: 2px solid #1989fa;
}

.gift-item__image {
  width: 48px;
  height: 48px;
  margin-bottom: 8px;
}

.gift-item__image img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.gift-item__name {
  font-size: 12px;
  color: #333;
  margin-bottom: 4px;
  text-align: center;
}

.gift-item__price {
  font-size: 12px;
  color: #ff4d4f;
}

.gift-panel__quantity {
  padding: 16px;
  text-align: center;
  border-top: 1px solid #eee;
}

.gift-popup__footer {
  padding: 16px;
  border-top: 1px solid #eee;
}
</style>
