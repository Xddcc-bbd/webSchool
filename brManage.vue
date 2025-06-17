<template>
  <div class="borrow-management">
    <!-- 借阅记录表格 -->
    <el-table 
      :data="filteredRecords" 
      style="width: 100%" 
      border
      stripe
      :header-cell-style="{
        background: '#f5f7fa',
        fontWeight: 'bold',
        color: '#333'
      }"
    >
      <el-table-column 
        prop="isbn" 
        label="图书ISBN" 
        width="180"
        align="center"
      />
      <el-table-column 
        prop="bookName" 
        label="书名" 
        min-width="190"
        show-overflow-tooltip
      />
      <el-table-column 
        prop="userID" 
        label="用户ID" 
        width="120"
        align="center"
      />
      <el-table-column 
        prop="outDay" 
        label="借出日期" 
        width="150"
        align="center"
      />
      <el-table-column 
        prop="backDay" 
        label="归还日期" 
        width="150"
        align="center"
      />
      <el-table-column 
        label="状态" 
        width="100"
        align="center"
      >
        <template #default="scope">
          <el-tag 
            :type="scope.row.backDay ? 'success' : 'warning'"
            size="small"
          >
            {{ scope.row.backDay ? '已归还' : '借阅中' }}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>
    
    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="totalRecords"
        :page-sizes="[5, 10, 20]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
      />
    </div>
    <div class="charts-container">
      <!--饼图-->
      <div class="chart-item">
        <v-chart :option="chartOption" autoresize />
      </div>
      <!--棒状图-->
      <div class="chart-item">
        <v-chart :option="chartOption2" autoresize />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { use } from 'echarts/core'
import { PieChart, BarChart } from 'echarts/charts'
import { TitleComponent, TooltipComponent } from 'echarts/components'
import { CanvasRenderer } from 'echarts/renderers'

use([PieChart, TitleComponent, TooltipComponent, CanvasRenderer])
use([BarChart, TitleComponent, TooltipComponent, CanvasRenderer])

const chartOption = ref({
  title: { text: '图书归还占比', left: 'center' },
  tooltip: { trigger: 'item' },
  series: [{
    type: 'pie',
    data: [
      { value: 47, name: '未归还' },
      { value: 53, name: '已归还' }
    ]
  }]
})

const chartOption2 = ref({
  title: { text: '图书借阅趋势', left: 'center' },
  xAxis: {
    type: 'category',
    data: ['March', 'April', 'May', 'June']
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      data: [
        { value: 1, itemStyle: { color: '#f9c116' } },
        { value: 5, itemStyle: { color: '#f9c116' } },
        { value: 7, itemStyle: { color: '#f9c116' } },
        { value: 4, itemStyle: { color: '#f9c116' } }
      ],
      type: 'bar'
    }
  ]
})

interface BorrowRecord {
  isbn: string;
  bookName: string;
  userID: number;
  outDay: string;
  backDay: string | null;
}

const borrowRecords = ref<BorrowRecord[]>([])
const searchQuery = ref('')
const currentPage = ref(1)
const pageSize = ref(10)

// 获取借阅记录
const fetchBorrowRecords = async () => {
  try {
    const response = await axios.get("http://localhost:8080/borrow-records")
    borrowRecords.value = response.data.map(record => ({
      ...record,
      backDay: record.backDay || null
    }))
  } catch (error) {
    ElMessage.error('获取借阅记录失败')
    console.error(error)
  }
}

// 搜索功能
const filteredRecords = computed(() => {
  let records = borrowRecords.value
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    records = records.filter(record => 
      record.isbn.toLowerCase().includes(query) ||
      (record.bookName && record.bookName.toLowerCase().includes(query)) ||
      record.userID.toString().includes(query)
    )
  }
  return records.slice(
    (currentPage.value - 1) * pageSize.value,
    currentPage.value * pageSize.value
  )
})

// 分页和搜索控制
const resetSearch = () => {
  searchQuery.value = ''
  currentPage.value = 1
}
const handleSizeChange = (size: number) => pageSize.value = size
const handlePageChange = (page: number) => currentPage.value = page

// 统计数据
const totalRecords = computed(() => {
  if (!searchQuery.value) return borrowRecords.value.length
  return filteredRecords.value.length
})

onMounted(fetchBorrowRecords)
</script>

<style scoped>
	/* 表格单元格样式 */
	:deep(.el-table td.el-table__cell),
	:deep(.el-table th.el-table__cell) {
	  padding: 12px 0;
	}
	
	/* 表头样式 */
	:deep(.el-table__header th) {
	  background-color: #f5f7fa !important;
	}
	
	/* 单元格文本样式 */
	:deep(.el-table .cell) {
	  white-space: nowrap;
	  text-overflow: ellipsis;
	  overflow: hidden;
	  padding: 0 10px;
	}
	
	/* 状态标签样式 */
	:deep(.el-tag) {
	  width: 70px;
	  justify-content: center;
	}
.borrow-management {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

h1 {
  text-align: center;
  margin-bottom: 20px;
  color: #333;
  font-size: 24px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.el-table {
  margin-top: 20px;
}

.charts-container {
  display: flex;
  justify-content: space-between;
  margin-top: 30px;
  gap: 20px;
}

.chart-item {
  flex: 1;
  min-width: 0;
  height: 400px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 15px;
}
</style>