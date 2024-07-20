<template>
  <div class="main-content">
    <div style="width: 70%; margin: 40px auto">
      <div class="table">
        <el-table :data="tableData" stripe>
          <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
          <el-table-column prop="userName" label="家长姓名" show-overflow-tooltip></el-table-column>
          <el-table-column prop="userPhone" label="家长电话" show-overflow-tooltip></el-table-column>
          <el-table-column prop="teacherName" label="教师姓名"></el-table-column>
          <el-table-column prop="start" label="开始时间"></el-table-column>
          <el-table-column prop="end" label="结束时间"></el-table-column>
          <el-table-column prop="content" label="预约说明"></el-table-column>
          <el-table-column prop="status" label="预约状态"></el-table-column>

          <el-table-column label="操作" width="180" align="center">
            <template v-slot="scope">
              <el-button plain type="primary" @click="handleEdit(scope.row)" size="mini" v-if="user.role === 'USER'" :disabled="scope.row.status !== '待确认'">编辑</el-button>
              <el-button plain type="danger" size="mini" @click=del(scope.row.id) v-if="user.role === 'USER'" :disabled="scope.row.status !== '待确认'">取消预约</el-button>
              <el-button plain type="primary" @click="checkStatus(scope.row, '已接受')" size="mini" v-if="user.role === 'TEACHER'" :disabled="scope.row.status !== '待确认'">接受</el-button>
              <el-button plain type="primary" @click="checkStatus(scope.row, '已拒绝')" size="mini" v-if="user.role === 'TEACHER'" :disabled="scope.row.status !== '待确认'">拒绝</el-button>
            </template>
          </el-table-column>
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
      }
      // if (this.user.role === 'USER') {
      //   params.userId = this.user.id
      // }
      // if (this.user.role === 'TEACHER') {
      //   params.teacherId = this.user.id
      // }
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/reserve/selectPage', {
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
        this.$request.delete('/reserve/delete/' + id).then(res => {
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
      this.$request.put('/reserve/update', this.form).then(res => {
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