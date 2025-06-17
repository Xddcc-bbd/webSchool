<template>
  <div class="book-management">
    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-form :inline="true" :model="searchForm" class="demo-form-inline">
        <el-form-item label="ISBN号">
          <el-input v-model="searchForm.isbn" placeholder="ISBN号" clearable />
        </el-form-item>
        <el-form-item label="书名">
          <el-input v-model="searchForm.bookName" placeholder="书名" clearable />
        </el-form-item>
        <el-form-item label="作者">
          <el-input v-model="searchForm.writer" placeholder="作者" clearable />
        </el-form-item>
        <el-form-item label="出版日期">
          <el-input v-model="searchForm.pubDay" placeholder="出版日期" clearable />
        </el-form-item>
        <el-form-item>
          
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <!-- 图书表格 -->
    <el-table 
      :data="pagedBooks" 
      style="width: 100%"
      border
      stripe
      :header-cell-style="{
        background: '#f5f7fa',
        fontWeight: 'bold',
        color: '#333'
      }"
    >
      <el-table-column fixed prop="isbn" label="ISBN号" width="180" align="center"/>
      <el-table-column prop="bookName" label="书名" min-width="220" show-overflow-tooltip/>
      <el-table-column prop="writer" label="作者" width="150" align="center"/>
      <el-table-column prop="pubDay" label="出版日期" width="150" align="center"/>
      <el-table-column label="操作" width="180" align="center">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row)">修改</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row.isbn)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <!-- 分页和添加按钮 -->
    <div class="pagination-container">
      <el-button type="success" @click="showAddDialog">添加新书</el-button>
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="filteredBooks.length"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
      />
    </div>

    <!-- 添加图书对话框 -->
    <el-dialog v-model="addDialogVisible" title="添加新书" width="30%">
      <el-form :model="addForm" :rules="rules" ref="addFormRef" label-width="100px">
        <el-form-item label="ISBN号" prop="isbn">
          <el-input v-model="addForm.isbn" />
        </el-form-item>
        <el-form-item label="书名" prop="bookName">
          <el-input v-model="addForm.bookName" />
        </el-form-item>
        <el-form-item label="作者" prop="writer">
          <el-input v-model="addForm.writer" />
        </el-form-item>
        <el-form-item label="出版日期" prop="pubDay">
          <el-input v-model="addForm.pubDay" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAddForm">确认</el-button>
      </template>
    </el-dialog>

    <!-- 修改图书对话框 -->
    <el-dialog v-model="editDialogVisible" title="修改图书信息" width="30%">
      <el-form :model="editForm" :rules="rules" ref="editFormRef" label-width="100px">
        <el-form-item label="ISBN号" prop="isbn">
          <el-input v-model="editForm.isbn" />
        </el-form-item>
        <el-form-item label="书名" prop="bookName">
          <el-input v-model="editForm.bookName" />
        </el-form-item>
        <el-form-item label="作者" prop="writer">
          <el-input v-model="editForm.writer" />
        </el-form-item>
        <el-form-item label="出版日期" prop="pubDay">
          <el-input v-model="editForm.pubDay" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEditForm">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'

interface Book {
  isbn: string
  bookName: string
  writer: string
  pubDay?: string
}

// 数据状态
const books = ref<Book[]>([])
const searchedBooks = ref<Book[]>([]) // 新增：存储搜索结果
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)

// 搜索表单
const searchForm = reactive({
  isbn: '',
  bookName: '',
  writer: '',
  pubDay: ''
})

// 表单验证规则
const rules = reactive<FormRules>({
  isbn: [{ required: true, message: '请输入ISBN号', trigger: 'blur' }],
  bookName: [{ required: true, message: '请输入书名', trigger: 'blur' }],
  writer: [{ required: true, message: '请输入作者', trigger: 'blur' }]
})

// 添加相关
const addDialogVisible = ref(false)
const addFormRef = ref<FormInstance>()
const addForm = reactive<Book>({
  isbn: '',
  bookName: '',
  writer: '',
  pubDay: ''
})

// 修改相关
const editDialogVisible = ref(false)
const editFormRef = ref<FormInstance>()
const editForm = reactive<Book>({
  isbn: '',
  bookName: '',
  writer: '',
  pubDay: ''
})

// 获取图书数据
const fetchBooks = async () => {
  try {
    loading.value = true
    const response = await axios.get("http://localhost:8080/get-all")
    books.value = response.data
    searchedBooks.value = response.data // 初始化时显示所有数据
  } catch (error) {
    ElMessage.error('获取图书数据失败')
    console.error(error)
  } finally {
    loading.value = false
  }
}

// 搜索功能
const filteredBooks = computed(() => {
  if (!searchForm.isbn && !searchForm.bookName && !searchForm.writer && !searchForm.pubDay) {
    return books.value
  }
  return books.value.filter(book => 
    (searchForm.isbn ? book.isbn.includes(searchForm.isbn) : true) &&
    (searchForm.bookName ? book.bookName.includes(searchForm.bookName) : true) &&
    (searchForm.writer ? book.writer.includes(searchForm.writer) : true) &&
    (searchForm.pubDay ? (book.pubDay?.includes(searchForm.pubDay) ?? false) : true)
  )
})

// 分页数据
const pagedBooks = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredBooks.value.slice(start, end)
})

// 搜索方法
const handleSearch = () => {
  if (!searchForm.isbn && !searchForm.bookName && !searchForm.writer && !searchForm.pubDay) {
    searchedBooks.value = books.value // 没有搜索条件时显示全部
  } else {
    searchedBooks.value = books.value.filter(book => 
      (searchForm.isbn ? book.isbn.includes(searchForm.isbn) : true) &&
      (searchForm.bookName ? book.bookName.includes(searchForm.bookName) : true) &&
      (searchForm.writer ? book.writer.includes(searchForm.writer) : true) &&
      (searchForm.pubDay ? (book.pubDay?.includes(searchForm.pubDay) ?? false) : true)
    )
  }
  currentPage.value = 1 // 搜索时重置到第一页
}

// 重置方法
const resetSearch = () => {
  searchForm.isbn = ''
  searchForm.bookName = ''
  searchForm.writer = ''
  searchForm.pubDay = ''
  searchedBooks.value = books.value
  currentPage.value = 1
}

// 显示添加对话框
const showAddDialog = () => {
  addDialogVisible.value = true
  addFormRef.value?.resetFields()
}

// 提交添加表单
const submitAddForm = async () => {
  try {
    await addFormRef.value?.validate()
    
    const { data } = await axios.post("http://localhost:8080/addbook", addForm)
    
    if (data > 0) {
      ElMessage.success('添加成功')
      addDialogVisible.value = false
      fetchBooks()
    } else {
      ElMessage.error('添加失败')
    }
  } catch (error) {
    console.error('添加图书错误:', error)
  }
}

// 显示编辑对话框
const handleEdit = (row: Book) => {
  editDialogVisible.value = true
  Object.assign(editForm, row)
}

// 提交修改表单
const submitEditForm = async () => {
  try {
    await editFormRef.value?.validate()
    
    const { data } = await axios.post("http://localhost:8080/upbook", editForm)
    
    if (data > 0) {
      ElMessage.success('修改成功')
      editDialogVisible.value = false
      fetchBooks()
    }
  } catch (error) {
    console.error('修改图书错误:', error)
  }
}

// 删除图书
const handleDelete = (isbn: string) => {
  ElMessageBox.confirm('确认删除该图书吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    axios.get(`http://localhost:8080/delbook?isbn=${isbn}`)
      .then(res => {
        if (res.data > 0) {
          ElMessage.success('删除成功')
          fetchBooks()
        } else {
          ElMessage.error('删除失败')
        }
      })
  }).catch(() => {})
}

// 分页处理
const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
}

const handlePageChange = (page: number) => {
  currentPage.value = page
}

// 初始化加载数据
onMounted(() => {
  fetchBooks()
})
</script>

<style scoped>
.book-management {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.search-bar {
  margin-bottom: 20px;
}

.search-bar .el-form {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.search-bar .el-form-item {
  margin-bottom: 0;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.el-table {
  margin-top: 20px;
}

/* 表格单元格样式 */
:deep(.el-table td.el-table__cell),
:deep(.el-table th.el-table__cell) {
  padding: 12px 0;
}

/* 响应式调整 */
@media (max-width: 992px) {
  .search-bar .el-form {
    flex-direction: column;
  }
  
  .pagination-container {
    flex-direction: column;
    gap: 10px;
  }
}
</style>