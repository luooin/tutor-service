<template>
  <div class="main-content">
    <div style="width: 70%; margin: 30px auto">
      <div class="search">
        <el-input placeholder="请输入标题查询" style="width: 200px" v-model="name"></el-input>
        <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
        <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
      </div>
      <div class="table" style="margin-top: 20px">
        <el-table :data="tableData" stripe>
          <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
          <el-table-column prop="name" label="标题" show-overflow-tooltip width="200"></el-table-column>
          <el-table-column prop="content" label="内容" show-overflow-tooltip>
            <template v-slot="scope">
              <el-button type="primary" @click="viewEditor(scope.row.content)">点击查看</el-button>
            </template>
          </el-table-column>
          <el-table-column prop="userName" label="发布人"></el-table-column>
          <el-table-column prop="userPhone" label="发布人电话"></el-table-column>
          <el-table-column prop="time" label="发布时间"></el-table-column>
          <el-table-column prop="status" label="状态"></el-table-column>

          <el-table-column label="操作" width="180" align="center" v-if="user.role === 'TEACHER'">
            <template v-slot="scope">
              <el-button plain type="primary" @click="accept(scope.row)" size="mini" v-if="user.role === 'TEACHER'" :disabled="scope.row.status === '已接单'">我要接单</el-button>
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
    <el-dialog title="详细信息" :visible.sync="viewVisible" width="55%" :close-on-click-modal="false" destroy-on-close>
      <div v-html="viewContent" class="w-e-text w-e-text-container"></div>
    </el-dialog>
  </div>
</template>

<script>
import E from 'wangeditor'
export default {
  data() {
    return {
      editor: null,
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      name: null,
      fromVisible: false,
      viewVisible: false,
      viewContent: null,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    viewEditor(content) {
      this.viewContent = content
      this.viewVisible = true
    },
    load(pageNum) {  // 分页查询
      let params = {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        name: this.name
      }
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/publish/selectPage', {
        params: params
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    accept(row) {
      this.$confirm('您确定已经和家长沟通过吗？接单之后需要认真负责完成工作，否则会受到平台的惩罚', '确认接单', {type: "warning"}).then(response => {
        this.form = JSON.parse(JSON.stringify(row))
        this.form.teacherId = this.user.id
        this.form.status = '已接单'
        console.log('hhhhhh', this.form)
        this.save()
      }).catch(() => {
      })
    },
    save() {
      this.$request({
        url: this.form.id ? '/publish/update' : '/publish/add',
        method: this.form.id ? 'PUT' : 'POST',
        data: this.form
      }).then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.$message.success('保存成功')
          this.load(1)
          this.fromVisible = false
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
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
    },
    reset() {
      this.name = null
      this.load(1)
    }
  }
}
</script>
