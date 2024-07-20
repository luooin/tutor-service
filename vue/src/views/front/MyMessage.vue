<template>
  <div class="main-content">
    <div style="width: 70%; margin: 40px auto">
      <div class="table">
        <el-table :data="tableData" stripe>
          <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
          <el-table-column prop="userName" label="用户姓名" show-overflow-tooltip></el-table-column>
          <el-table-column prop="content" label="内容" show-overflow-tooltip></el-table-column>
          <el-table-column prop="time" label="留言时间"></el-table-column>
          <el-table-column prop="reply" label="留言回复"></el-table-column>
        </el-table>

        <div class="pagination" style="margin-top: 20px">
          <el-pagination
              background
              @current-change="handleCurrentChange"
              :current-page="pageNum"
              :page-sizes="[5, 10, 20]"
              :page-size="pageSize"
              layout="total, prev, pager, next"
              :total="total">
          </el-pagination>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    load(pageNum) {  // 分页查询
      let params = {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        userId: this.user.id,
        role: this.user.role
      }
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/message/selectPage', {
        params: params
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    handleEdit(row) {   // 编辑数据
      this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
      this.fromVisible = true   // 打开弹窗
    },
    del(id) {   // 单个删除
      this.$confirm('您确定取消吗？', '确认取消', {type: "warning"}).then(response => {
        this.$request.delete('/message/delete/' + id).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    save() {
      this.$request.put('/message/update', this.form).then(res => {
        if (res.code === '200') {
          this.$message.success('保存成功')
          this.fromVisible = false
          this.load(1)
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    checkStatus(row, status) {
      this.form = JSON.parse(JSON.stringify(row))
      this.form.status = status
      this.save()
    }
  }
}
</script>
