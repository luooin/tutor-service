<template>
  <div class="main-content">
    <el-card style="width: 50%; margin: 30px auto">
      <div style="text-align: right; margin-bottom: 20px">
        <el-button type="success" @click="infoInit">提交详细资料</el-button>
        <el-button type="primary" @click="updatePassword">修改密码</el-button>
      </div>
      <el-form :model="user" label-width="80px" style="padding-right: 20px">
        <div style="margin: 15px; text-align: center">
          <el-upload
              class="avatar-uploader"
              :action="$baseUrl + '/files/upload'"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
          >
            <img v-if="user.avatar" :src="user.avatar" class="avatar" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </div>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="user.username" placeholder="用户名" disabled></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="user.name" placeholder="姓名"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="user.phone" placeholder="电话"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="user.email" placeholder="邮箱"></el-input>
        </el-form-item>
        <div style="text-align: center; margin-bottom: 20px">
          <el-button type="primary" @click="update">保 存</el-button>
        </div>
      </el-form>
    </el-card>
    <el-dialog title="修改密码" :visible.sync="dialogVisible" width="30%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="user" label-width="80px" style="padding-right: 20px" :rules="rules" ref="formRef">
        <el-form-item label="原始密码" prop="password">
          <el-input show-password v-model="user.password" placeholder="原始密码"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input show-password v-model="user.newPassword" placeholder="新密码"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input show-password v-model="user.confirmPassword" placeholder="确认密码"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="详细资料" :visible.sync="dialogVisible2" width="50%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="form" label-width="80px" style="padding-right: 20px">
        <el-form-item label="出生年月" prop="birth">
          <el-date-picker style="width: 100%"
                          v-model="form.birth"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="所在学校" prop="school">
          <el-input v-model="form.school" placeholder="所在学校"></el-input>
        </el-form-item>
        <el-form-item label="所学专业" prop="speciality">
          <el-input v-model="form.speciality" placeholder="所学专业"></el-input>
        </el-form-item>
        <el-form-item label="最高学历" prop="education">
          <el-select v-model="form.education" placeholder="请选择" style="width: 100%">
            <el-option label="博士后" value="博士后"></el-option>
            <el-option label="博士" value="博士"></el-option>
            <el-option label="硕士" value="硕士"></el-option>
            <el-option label="本科" value="本科"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="所在省份" prop="province">
          <el-input v-model="form.province" placeholder="所在省份"></el-input>
        </el-form-item>
        <el-form-item label="授课方式" prop="type">
          <el-select v-model="form.type" placeholder="请选择" style="width: 100%">
            <el-option label="线上授课" value="线上授课"></el-option>
            <el-option label="线下授课" value="线下授课"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="可授科目" prop="typeName">
          <el-select v-model="form.typeName" placeholder="请选择" style="width: 100%">
            <el-option v-for="item in typeData" :label="item.name" :value="item.name" :key="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="个人介绍" prop="description">
          <el-input type="textarea" :rows="3" v-model="form.description" placeholder="个人介绍"></el-input>
        </el-form-item>
        <el-form-item label="授课安排" prop="plan">
          <el-input type="textarea" :rows="4" v-model="form.plan" placeholder="授课安排"></el-input>
        </el-form-item>
        <el-form-item label="教员编号" prop="code">{{form.code}}</el-form-item>
        <el-form-item label="教员星级" prop="code">{{form.star}}</el-form-item>
        <el-form-item label="教员身份" prop="code">{{form.level}}</el-form-item>
        <el-form-item label="审核状态" prop="code">{{form.status}}</el-form-item>
        <el-form-item label="审核理由" prop="code">{{form.reason}}</el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible2 = false">取 消</el-button>
        <el-button type="primary" @click="submitInfo">提 交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    const validatePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请确认密码'))
      } else if (value !== this.user.newPassword) {
        callback(new Error('确认密码错误'))
      } else {
        callback()
      }
    }
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      dialogVisible: false,
      rules: {
        password: [
          { required: true, message: '请输入原始密码', trigger: 'blur' },
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
        ],
        confirmPassword: [
          { validator: validatePassword, required: true, trigger: 'blur' },
        ],
      },
      dialogVisible2: false,
      form: {},
      typeData: []
    }
  },
  created() {
    this.loadType()
    this.loadInfo()
  },
  methods: {
    loadInfo() {
      this.$request.get('/info/selectByTeacherId/' + this.user.id).then(res => {
        if (res.code === '200') {
          this.form = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    loadType() {
      this.$request.get('/type/selectAll').then(res => {
        if (res.code === '200') {
          this.typeData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    update() {
      // 保存当前的用户信息到数据库
      this.$request.put('/teacher/update', this.user).then(res => {
        if (res.code === '200') {
          // 成功更新
          this.$message.success('保存成功')
          // 更新浏览器缓存里的用户信息
          localStorage.setItem('xm-user', JSON.stringify(this.user))

          // 触发父级的数据更新
          this.$emit('update:user')
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleAvatarSuccess(response, file, fileList) {
      // 把user的头像属性换成上传的图片的链接
      this.$set(this.user, 'avatar', response.data)
    },
    // 修改密码
    updatePassword() {
      this.dialogVisible = true
    },
    //如果selectByTeacherId查询结果为null，则默认给一个空对象，并将dialogVisible2显示
    infoInit() {
      if (!this.form) {
        this.form = {}
      }
      this.dialogVisible2 = true
    },
    submitInfo() {
      this.$confirm('您确定要提交吗？提交后会覆盖原有的信息，需要管理员审核通过后才能生效', '确认提交', {type: "warning"}).then(response => {
        this.form.teacherId = this.user.id
        this.form.status = '待审核'
        this.$request.post('/info/add', this.form).then(res => {
          if (res.code === '200') {
            this.$message.success('提交成功，等待管理员审核')
            this.dialogVisible2 = false
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {
      })
    },
    save() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request.put('/updatePassword', this.user).then(res => {
            if (res.code === '200') {
              // 成功更新
              this.$message.success('修改密码成功')
              this.$router.push('/login')
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    }
  }
}
</script>

<style scoped>
/deep/.el-form-item__label {
  font-weight: bold;
}
/deep/.el-upload {
  border-radius: 50%;
}
/deep/.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  border-radius: 50%;
}
/deep/.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
  border-radius: 50%;
}
.avatar {
  width: 120px;
  height: 120px;
  display: block;
  border-radius: 50%;
}
</style>