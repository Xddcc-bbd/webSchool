<template>
  <div class="box">
	  <h2>登录</h2>
    账号：<el-input
      v-model="userName"
      style="width: 240px"
      placeholder="Please input"
      clearable
    /> <br /><br />
    密码: <el-input
      v-model="userPwd"
      style="width: 240px"
      type="password"
      placeholder="Please input password"
      show-password
    /><br />
	 <br />
      <el-button type="primary" round @click="login">登录</el-button>
  </div>
</template>

<script setup lang="ts">
import axios from 'axios';
import { ElMessage } from 'element-plus';
import { ref } from 'vue';
import {useRouter} from 'vue-router';
let router=useRouter();
let userName = ref("");
let userPwd = ref("");

function login() {
  let user = {
    userName: userName.value,
    userPwd: userPwd.value
  };
  axios.post("http://localhost:8080/login", user)
    .then(res => {
      if (res.data) {
        ElMessage.success("登录成功");
		router.push("/home")
      } else {
        ElMessage.error("登录失败");
      }
    })
    .catch(err => {
      ElMessage.error("请求失败: " + err.message);
    });
}
</script>

<style scoped>
.box{
		border: 1px solid #b6dbe6;
		width: 300px;
		height: 240px;
		border-radius: 10px;
		margin: 150px auto;
		text-align: center;
		
	}

</style>