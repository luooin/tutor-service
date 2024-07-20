<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入标题查询" style="width: 200px" v-model="name"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe>
        <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="name" label="标题" show-overflow-tooltip width="300"></el-table-column>
        <el-table-column prop="content" label="内容" show-overflow-tooltip>
          <template v-slot="scope">
            <el-button type="primary" @click="viewEditor(scope.row.content)">点击查看</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="userName" label="发布人"></el-table-column>
        <el-table-column prop="userPhone" label="发布人电话"></el-table-column>
        <el-table-column prop="teacherName" label="接单人"></el-table-column>
        <el-table-column prop="teacherPhone" label="接单人电话"></el-table-column>
        <el-table-column prop="time" label="发布时间"></el-table-column>
        <el-table-column prop="status" label="状态"></el-table-column>
      </el-table>

      <div class="pagination">
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
    <el-dialog title="详细信息" :visible.sync="viewVisible" width="55%" :close-on-click-modal="false" destroy-on-close>
      <div v-html="viewContent" class="w-e-text w-e-text-container"></div>
    </el-dialog>

  </div>
</template>

<script>
import E from 'wangeditor'
export default {
  name: "Publish",
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      name: null,
      fromVisible: false,
      viewVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {},
      ids: [],
      viewContent: null
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
    handleAdd() {   // 新增数据
      this.form = {}  // 新增数据的时候清空数据
      this.fromVisible = true   // 打开弹窗
    },
    handleEdit(row) {   // 编辑数据
      this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
      this.fromVisible = true   // 打开弹窗
    },
    save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.$refs.formRef.validate((valid) => {
        if (valid) {
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
        }
      })
    },
    del(id) {   // 单个删除
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/publish/delete/' + id).then(res => {
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
    handleSelectionChange(rows) {   // 当前选中的所有的行数据
      this.ids = rows.map(v => v.id)   //  [1,2]
    },
    delBatch() {   // 批量删除
      if (!this.ids.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('您确定批量删除这些数据吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/publish/delete/batch', {data: this.ids}).then(res => {
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
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/publish/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.name = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
  }
}
</script>

<style scoped>

</style>
