<template>
  <div class="main-content">
    <div style="width: 75%; margin: 10px auto; min-height: 1000px">
      <div style="margin-top: 30px; border-left: 5px solid #71d5a1; font-size: 20px; padding: 5px 10px; font-weight: bold">
        {{typeName}}
      </div>
      <div class="search">
        <el-input placeholder="请输教师姓名查询" style="width: 200px" v-model="teacherName"></el-input>
        <el-button type="info" plain style="margin-left: 10px" @click="loadInfoByName">查询</el-button>
        <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
      </div>
      <div style="margin-top: 20px">
        <el-row :gutter="30">
          <el-col :span="4" style="text-align: center; margin-bottom: 20px" v-for="item in infoData">
            <div class="card" @click="navTo('/front/teacherDetail?id=' + item.id)">
              <img :src="item.teacherAvatar" alt="" style="width: 100px; height: 100px; border-radius: 50%">
              <div style="font-weight: bold">{{ item.teacherName }}</div>
              <div style="margin-top: 5px; color: #666666">{{ item.speciality }}</div>
              <div style="margin-top: 5px; color: #666666">{{ item.school }}</div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script>

export default {

  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      typeName: this.$route.query.typeName,
      infoData: [],
      teacherName:'',
    }
  },
  mounted() {
    this.loadInfo()
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    loadInfo() {
      this.$request.get('/info/selectAll', {
        params: {
          status: '审核通过',
          typeName: this.typeName
        }
      }).then(res => {
        if (res.code === '200') {
          this.infoData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    loadInfoByName() {
      this.$request.get('/info/selectAll', {
        params: {
          status: '审核通过',
          typeName: this.typeName,
          teacherName: this.teacherName
        }
      }).then(res => {
        if (res.code === '200') {
          this.infoData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    navTo(url) {
      location.href = url
    },
    // load(pageNum) {  // 分页查询
    //   if (pageNum) this.pageNum = pageNum
    //   this.$request.get('/info/selectPage', {
    //     params: {
    //       pageNum: this.pageNum,
    //       pageSize: this.pageSize,
    //       username: this.username,
    //     }
    //   }).then(res => {
    //     this.tableData = res.data?.list
    //     this.total = res.data?.total
    //   })
    // },
    reset() {
      this.teacherName = null
    },
  }
}
</script>
<style>
.card {
  padding: 5px;
  cursor: pointer;
  transition: all 0.3s ease; /* 添加平滑过渡效果 */
}

.card:hover {
  background-color: #dbf1e1;
  transform: scale(1.1); /* 鼠标悬停时放大图片 */
}

</style>