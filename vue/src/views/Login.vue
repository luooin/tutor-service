<template>
  <div class="container">
    <!-- <div style="width: 50% "></div> -->
    <div style="width: 400px; padding: 30px; background-color: white; border-radius: 5px;">
      <div style="text-align: center; font-size: 20px; margin-bottom: 20px; color: #333">欢迎使用家教服务平台</div>
      <el-form :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="username">
          <el-input prefix-icon="el-icon-user" placeholder="请输入账号" v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input prefix-icon="el-icon-lock" placeholder="请输入密码" show-password  v-model="form.password"></el-input>
        </el-form-item>
        <el-form-item prop="role">
          <el-select v-model="form.role" placeholder="请选择角色" style="width: 100%">
            <el-option label="管理员" value="ADMIN"></el-option>
            <el-option label="教员" value="TEACHER"></el-option>
            <el-option label="家长" value="USER"></el-option>
          </el-select>
        </el-form-item>
<!--        <el-form-item>-->
<!--          <div style="display: flex; justify-content: center">-->
<!--            <el-input v-model="form.verCode" prefix-icon="el-icon-user" style="width: 42%; margin-right: 10px" placeholder="请输入验证码"></el-input>-->
<!--            <img :src="captchaUrl" @click="clickImg()" width="140px" height="33px" />-->
<!--          </div>-->
<!--        </el-form-item>-->
<!--        <el-form-item>-->
<!--          <div style="display: flex; justify-content: center">-->
<!--            <el-input v-model="form.verCode" prefix-icon="el-icon-user" style="width: 42%; margin-right: 10px" placeholder="请输入验证码"></el-input>-->
<!--            &lt;!&ndash; 这里的 :src 绑定了 captchaUrl，确保点击图片时会重新加载 &ndash;&gt;-->
<!--            <img :src="captchaUrl" @click="clickImg()" width="140px" height="33px" />-->
<!--          </div>-->
<!--        </el-form-item>-->
        <el-form-item>
          <el-button style="width: 100%; background-color: #333; border-color: #333; color: white" @click="login">登 录</el-button>
        </el-form-item>
        <div style="display: flex; align-items: center">
          <div style="flex: 1"></div>
          <div style="flex: 1; text-align: right">
            还没有账号？请 <a href="/register">注册</a>
          </div>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      form: { },
      rules: {
        username: [
          { required: true, message: '请输入账号', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ]
      },

    }
  },
  created() {

  },
  methods: {
    login() {
      this.$refs['formRef'].validate((valid) => {
        if (valid) {
          // 验证通过
          this.$request.post('/login', this.form).then(res => {
            if (res.code === '200') {
              localStorage.setItem("xm-user", JSON.stringify(res.data))  // 存储用户数据
              if (res.data.role === 'ADMIN') {
                //this.$router.push('/home')  // 跳转后台主页
                location.href = '/home'
              } else {
                location.href = '/front/home' //其余角色跳转到前台首页
              }
              this.$message.success('登录成功')
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
  }
}

</script>

<style scoped>
.container {
  height: 100vh;
  overflow: hidden;
  background-image: url("@/assets/imgs/bg3.jpg");
  background-size: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #666;
}
a {
  color: #2a60c9;
}
</style>