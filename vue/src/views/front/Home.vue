<template>
  <div class="main-content">
    <div style="width: 75%; margin: 10px auto; min-height: 1000px">
      <div style="display: flex; height: 100px">
        <div style="width: 200px; height: 250px; border: 1px solid #cccccc; border-radius: 5px; padding: 10px">
          <el-row :gutter="10">
            <el-col :span="8" style="padding: 0 5px; cursor: pointer; margin-bottom: 5px" v-for="item in baseTypeData">
              <img :src="item.img" alt="" style="width: 50px" @click="navTo('/front/teacherType?typeName=' + item.name)">
              <div style="font-size: 12px; text-align: center">{{ item.name }}</div>
            </el-col>
          </el-row>
        </div>
        <div style="flex: 1; height: 250px; margin: 0 10px; border-radius: 5px">
          <el-carousel height="250px" style="border-radius: 5px">
            <el-carousel-item v-for="item in carouselData">
              <img :src="item" alt="" style="width: 100%; height: 250px; border-radius: 5px">
            </el-carousel-item>
          </el-carousel>
        </div>
        <div style="width: 230px; height: 250px; border: 1px solid #cccccc; border-radius: 5px; text-align: center; padding: 30px 0">
          <img src="@/assets/imgs/logo.png" alt="" style="width: 80px; height: 80px">
          <div style="margin-top: 15px">
            <el-button style="padding: 10px 50px" type="success" @click="$router.push('/login?role=USER')">家长登录</el-button>
          </div>
          <div style="margin-top: 10px">
            <el-button style="padding: 10px 50px" type="danger" @click="$router.push('/login?role=TEACHER')">教员登录</el-button>
          </div>
        </div>
      </div>
      <div style="display: flex; height: 100px; margin-top: 160px">
        <div style="width: 200px; height: 170px; border: 1px solid #cccccc; border-radius: 5px; padding: 10px">
          <el-row :gutter="10">
            <el-col :span="8" style="padding: 0 5px; cursor: pointer; margin-bottom: 5px" v-for="item in excellentTypeData">
              <img :src="item.img" alt="" style="width: 50px"  @click="navTo('/front/teacherType?typeName=' + item.name)">
              <div style="font-size: 12px; text-align: center">{{ item.name }}</div>
            </el-col>
          </el-row>
        </div>
        <div style="flex: 1; height: 170px; margin: 0 10px; border-radius: 5px">
          <el-row :gutter="5">
            <el-col :span="6">
              <img src="@/assets/imgs/qingjiajiao.png" alt="" style="width: 100%; height: 170px; border-radius: 5px; cursor: pointer" @click="qingjiajiao()">
            </el-col>
            <el-col :span="6">
              <img src="@/assets/imgs/zuojiajiao.png" alt="" style="width: 100%; height: 170px; border-radius: 5px; cursor: pointer" @click="navTo('/front/publishSquare')">
            </el-col>
          </el-row>
        </div>
        <!-- <div style="width: 230px; height: 170px; border: 1px solid #cccccc; border-radius: 5px; text-align: center; padding: 5px 0">
          <img src="@/assets/imgs/二维码.png" alt="" style="height: 130px; width: 130px; padding: 5px">
          <div style="color: #666666">关注公众号，快速请家教</div>
        </div> -->
      </div>
      <div style="margin-top: 100px; border-left: 5px solid #71d5a1; font-size: 20px; padding: 5px 10px; font-weight: bold">
        平台教员
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
      baseTypeData: [],
      excellentTypeData: [],
      carouselData: [
        require('@/assets/imgs/轮播1.jpg'),
        require('@/assets/imgs/轮播2.jpg'),
        require('@/assets/imgs/lun-3.jpg'),
        require('@/assets/imgs/lun-4.jpg'),
      ],
      infoData: []
    }
  },
  mounted() {
    this.loadBaseType()
    this.loadExcellentTypeData()
    this.loadInfo()
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    loadInfo() {
      this.$request.get('/info/selectAll', {
        params: {
          status: '审核通过'
        }
      }).then(res => {
        if (res.code === '200') {
          this.infoData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    loadBaseType() {
      this.$request.get('/type/selectAll', {
        params: {
          type: '普通文化课'
        }
      }).then(res => {
        if (res.code === '200') {
          this.baseTypeData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    loadExcellentTypeData() {
      this.$request.get('/type/selectAll', {
        params: {
          type: '兴趣拔高课'
        }
      }).then(res => {
        if (res.code === '200') {
          this.excellentTypeData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    navTo(url) {
      location.href = url
    },
    qingjiajiao() {
      if (this.user.role !== 'USER') {
        this.$message.warning('您的角色暂不支持该操作')
        return
      }
      this.navTo('/front/publish')
    }
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