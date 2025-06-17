<template>
	<div class="common-layout">
	    <el-container>
	      <el-header>图书管理系统</el-header>
	      <el-container>
	        <el-aside width="200px">
				<el-menu
				        active-text-color="#ffd04b"
				        background-color="#545c64"
				        class="el-menu-vertical-demo"
						router
				        default-active="2"
				        text-color="#fff"
				        @open="handleOpen"
				        @close="handleClose"
				      >
				     <el-menu-item index="/bookoutline">
				       <el-icon><Management /></el-icon>
				       <span>图书一览</span>
				     </el-menu-item> 
				             <el-menu-item index="/bookmanage">
				               <el-icon><document /></el-icon>
				               <span>图书信息管理</span>
				             </el-menu-item> 
							          <el-menu-item index="/brmanage">
							            <el-icon><List /></el-icon>
							            <span>借阅记录管理</span>
							          </el-menu-item> 
				</el-menu>
			</el-aside>
	        <el-main><router-view> </router-view></el-main>
	      </el-container>
	    </el-container>
	  </div>
</template>

<script setup lang="ts">
	import {
	  Document,
	  Menu as IconMenu,
	  Location,
	  Setting,
	} from '@element-plus/icons-vue'
	const handleOpen = (key: string, keyPath: string[]) => {
	  console.log(key, keyPath)
	}
	const handleClose = (key: string, keyPath: string[]) => {
	  console.log(key, keyPath)
	}
</script>

<style scoped>
      .el-header{
        background-color: #93b5cf;
        color: #333;
        text-align: center;
        line-height: 60px;
        font-size: 30px;
      }
      
      .el-aside {
        background-color: #d0dfe6;
        color: #333;
        text-align: center;
        line-height: 200px;
        height: 1200px;
      }
      
      .el-main {
        background-color: #f8f4ed;
        color: #333;
        text-align: center;
        line-height: 60px;
      }
      
      body > .el-container {
        margin-bottom: 40px;
      }
</style>