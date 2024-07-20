<template>
  <div class="main-content">
    <div style="width: 60%; margin: 30px auto">
      <div style="font-size: 17px; color: #333333">请对本平台提出建议或者反馈：</div>
      <div style="margin-top: 10px">
        <el-input type="textarea" :rows="5" v-model="content" placeholder="请输入建议或者反馈"></el-input>
      </div>
      <div style="margin-top: 10px; text-align: right">
        <el-button type="primary" @click="submit(content)">提 交</el-button>
      </div>
      <div style="margin: 20px 0; font-size: 17px; color: #9a6d2a; font-weight: 550">
        看看其他小伙伴提出的建议或者反馈（{{count}}）
      </div>

      <div style="margin-top: 30px">
        <el-row :gutter="10" v-for="item in messageData" style="margin-bottom: 20px">
         <el-col :span="5">
           <div style="display: flex; align-items: center">
             <img :src="item.userAvatar" alt="" style="width: 40px; height: 40px; border-radius: 50%">
             <div style="margin-left: 10px">{{ item.userName }}</div>
           </div>
         </el-col>
         <el-col :span="15">
           <div style="height: 40px; line-height: 40px;" class="overflowShow">{{ item.content }}</div>
         </el-col>
         <el-col :span="4">
           <div style="text-align: right; height: 40px; line-height: 40px">{{ item.time }}</div>
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
      content: null,
      messageData: [],
      count: 0
    }
  },
  created() {
    this.loadMessage()
  },
  methods: {
    loadMessage() {
      this.$request.get('/message/selectAll').then(res => {
        if (res.code === '200') {
          this.messageData = res.data
          this.count = res.data.length;
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    submit(content) {
      let data = {
        userId: this.user.id,
        role: this.user.role,
        content: content
      }
      this.$request.post('/message/add', data).then(res => {
        if (res.code === '200') {
          this.$message.success('操作成功，感谢您的反馈！')
          this.content = null
          // 重新加载一下留言反馈数据
          this.loadMessage()
        }
      })
    }
  }
}
</script>
<style>
  .overflowShow {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis
  }
</style>
