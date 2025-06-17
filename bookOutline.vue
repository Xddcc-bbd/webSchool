<template>
  <div class="book-management-container">
    <!-- 图书展示卡片 -->
    <div class="book-card">
      <!-- 图书封面图片 -->
      <div class="book-cover">
        <el-image 
          style="width: 250px; height: 300px" 
          :src="currentBookImage" 
          fit="fill"
          :preview-src-list="previewImages"
        />
      </div>
      
      <!-- 图书信息 -->
      <div class="book-info">
        <div class="info-row">
          <span class="label">书名：</span>
          <span class="value">{{currentBook.bookName}}</span>
        </div>
        <div class="info-row">
          <span class="label">作者：</span>
          <span class="value">{{ currentBook.writer }}</span>
        </div>
        <div class="info-row">
          <span class="label">出版日期：</span>
          <span class="value">{{ currentBook.pubDay }}</span>
        </div>
        <div class="info-row">
          <span class="label">ISBN号：</span>
          <span class="value">{{ currentBook.isbn}}</span>
        </div>
      </div>
    </div>
    
    <!-- 分页控件 -->
    <div class="pagination-container">
      <el-pagination 
        background 
        layout="prev, pager, next" 
        :total="books.length"
        :page-size="1"
        :current-page="currentPage"
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import axios from 'axios'

// 图书数据
const books = ref([])
const currentPage = ref(1)

// 获取图书数据
const fetchBooks = async () => {
  try {
    const response = await axios.get("http://localhost:8080/get-all")
    books.value = response.data
  } catch (error) {
    console.error('获取图书数据失败:', error)
  }
}

// 当前显示的图书
const currentBook = computed(() => {
  return books.value[currentPage.value - 1] || {
    isbn: '',
    bookName: '',
    writer: '',
    pubDay: ''
  }
})

// 当前图书封面图片（根据页码选择pic1-pic5）
const currentBookImage = computed(() => {
  const picIndex = (currentPage.value - 1) % 5 + 1 // 循环使用1-5的图片
  return `/public/pic${picIndex}.jpg`
})

// 所有预览图片（用于点击放大）
const previewImages = computed(() => {
  return Array.from({length: 5}, (_, i) => `/public/pic${i+1}.jpg`)
})

// 处理分页变化
const handlePageChange = (page) => {
  currentPage.value = page
}

// 初始化加载数据
onMounted(() => {
  fetchBooks()
})
</script>

<style scoped>
.book-management-container {
  max-width: 900px;
  margin: 0 auto;
  padding: 20px;
}

h1 {
  text-align: center;
  color: #333;
  margin-bottom: 30px;
}

.section-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.book-card {
  display: flex;
  margin: 20px 0;
  padding: 60px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.book-cover {
  margin-right: 30px;
  padding: 60px;
}

.book-info {
  flex: 1;
}

.info-row {
  margin-bottom: 15px;
  font-size: 16px;
}

.label {
  font-weight: bold;
  color: #666;
  display: inline-block;
  width: 80px;
}

.value {
  color: #333;
}

.pagination-container {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}
</style>