<template>
  <div class="main-content">
    <div style="width: 60%; margin: 80px auto">
      <div style="display: flex">
        <div style="width: 200px; text-align: center">
          <img :src="infoData.teacherAvatar" alt="" style="width: 200px; height: 200px; border-radius: 50%">
          <div style="margin: 20px 0; font-size: 16px">
            <strong>姓名：</strong> {{infoData.teacherName}}
          </div>
          <div>
            <el-rate v-model="infoData.star" disabled></el-rate>
          </div>
        </div>
        <div style="width: 250px; margin-left: 50px">
          <div style="height: 40px; line-height: 40px; font-size: 16px">
            <strong>学校：</strong><span style="color: #666666">{{infoData.school}}</span>
          </div>
          <div style="height: 40px; line-height: 40px; font-size: 16px">
            <strong>学历：</strong><span style="color: #666666">{{infoData.education}}</span>
          </div>
          <div style="height: 40px; line-height: 40px; font-size: 16px">
            <strong>专业：</strong><span style="color: #666666">{{infoData.speciality}}</span>
          </div>
          <div style="height: 40px; line-height: 40px; font-size: 16px">
            <strong>等级：</strong><span style="color: #666666">{{infoData.level}}</span>
          </div>
          <div style="height: 40px; line-height: 40px; font-size: 16px">
            <strong>生日：</strong><span style="color: #666666">{{infoData.birth}}</span>
          </div>
          <div style="height: 40px; line-height: 40px; font-size: 16px">
            <strong>邮箱：</strong><span style="color: #666666">{{infoData.teacherEmail}}</span>
          </div>
          <div style="height: 40px; line-height: 40px; font-size: 16px">
            <strong>电话：</strong><span style="color: #666666">{{infoData.teacherPhone}}</span>
          </div>
        </div>
        <div style="flex: 1; margin-left: 20px">
          <div style="height: 40px; line-height: 40px; font-size: 16px">
            <strong>介绍：</strong><span style="color: #666666">{{infoData.description}}</span>
          </div>
          <div style="height: 40px; line-height: 40px; font-size: 16px">
            <strong>说明：</strong><span style="color: #666666">{{infoData.plan}}</span>
          </div>
          <div style="height: 40px; line-height: 40px; font-size: 16px">
            <strong>省份：</strong><span style="color: #666666">{{infoData.province}}</span>
          </div>
          <div style="height: 40px; line-height: 40px; font-size: 16px">
            <strong>家教：</strong><span style="color: #666666">{{infoData.typeName}}</span>
          </div>
          <div style="height: 40px; line-height: 40px; font-size: 16px">
            <strong>形式：</strong><span style="color: #666666">{{infoData.type}}</span>
          </div>
        </div>
      </div>
      <div style="margin-top: 40px; text-align: center">
        <el-button type="primary" style="padding: 15px 40px" @click="reserveInit">预约家教</el-button>
      </div>
    </div>
    <el-dialog title="预约信息" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="100px" style="padding-right: 50px" :model="form">
        <el-form-item prop="start" label="开始时间">
          <el-date-picker style="width: 100%"
                          v-model="form.start"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="end" label="结束时间">
          <el-date-picker style="width: 100%"
                          v-model="form.end"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="content" label="预约说明">
          <el-input type="textarea" :rows="5" v-model="form.content" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">提 交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      infoId: this.$route.query.id,
      infoData: {},
      form: {},
      fromVisible: false
    }
  },
  created() {
    this.loadInfo()
  },
  methods: {
    loadInfo() {
      this.$request.get('/info/selectById/' + this.infoId).then(res => {
        if (res.code === '200') {
          this.infoData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    reserveInit() {
      this.form = {}
      this.fromVisible = true
    },
    save() {
      this.form.userId = this.user.id
      this.form.teacherId = this.infoData.teacherId
      this.form.status = '待确认'
      this.$request.post('/reserve/add', this.form).then(res => {
        if (res.code === '200') {
          this.$message.success('预约成功，等待教员确认，您可以在我的预约里面查看确认结果')
          this.fromVisible = false
        } else {
          this.$message.error(res.msg)
        }
      })
    },
  }
}
</script>