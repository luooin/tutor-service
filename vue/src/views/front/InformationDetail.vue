<template>
  <div class="main-content">
    <div style="width: 60%; margin: 30px auto">
      <div style="text-align: center; font-size: 18px">{{ informationData.name }}</div>
<!--      <div style="text-align: center; margin-top: 15px; color: #666666">点击数量：{{ informationData.count }}</div>-->
      <div style="text-align: center; margin-top: 15px; color: #666666">点击数量：{{ informationData.count }}&nbsp; &nbsp; &nbsp; &nbsp; 发布时间：{{ informationData.time }}</div>
      <div style="margin-top: 50px" v-html="informationData.content" class="w-e-text w-e-text-container"></div>
    </div>
  </div>
</template>

<script>
import E from 'wangeditor'
export default {
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      informationId: this.$route.query.id,
      informationData: {}
    }
  },
  created() {
    this.loadInformation()
  },
  methods: {
    loadInformation() {
      this.$request.get('/information/selectById/' + this.informationId).then(res => {
        if (res.code === '200') {
          this.informationData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    }
  }
}
</script>
<style>
  p {
    color: #5a5757;
  }
</style>