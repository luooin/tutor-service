<template>
  <div class="main-content">
    <div style="width: 50%; margin: 40px auto">
      <div style="display: flex; margin-bottom: 10px" v-for="item in informationData">
        <div style="width: 100px">
          <img :src="item.img" alt="" style="width: 100%; height: 60px; border-radius: 5px">
        </div>
        <div style="flex: 1; margin-left: 20px">
          <div style="height: 30px; font-size: 16px; font-weight: 550; cursor: pointer" @click="$router.push('/front/informationDetail?id=' + item.id)">{{ item.name }}</div>
          <div style="color: #666666; text-align: right">{{ item.time }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      informationData: []
    }
  },
  created() {
    this.loadInformation()
  },
  methods: {
    loadInformation() {
      this.$request.get('/information/selectAll').then(res => {
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
