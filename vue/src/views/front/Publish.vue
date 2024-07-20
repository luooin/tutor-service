<template>
  <div class="main-content">
    <div style="width: 70%; margin: 30px auto">
      <div class="operation" v-if="user.role === 'USER'">
        <el-button type="primary" plain @click="handleAdd">发布家教</el-button>
      </div>
      <div class="table" style="margin-top: 20px">
        <el-table :data="tableData" stripe>
          <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
          <el-table-column prop="name" label="标题" show-overflow-tooltip width="300"></el-table-column>
          <el-table-column prop="content" label="内容" show-overflow-tooltip>
            <template v-slot="scope">
              <el-button type="primary" @click="viewEditor(scope.row.content)">点击查看</el-button>
            </template>
          </el-table-column>
          <el-table-column prop="time" label="发布时间"></el-table-column>
          <el-table-column prop="status" label="状态"></el-table-column>
          <el-table-column prop="teacherName" label="接单人"></el-table-column>
          <el-table-column prop="teacherPhone" label="接单人电话"></el-table-column>

          <el-table-column label="操作" width="180" align="center" v-if="user.role === 'USER'">
            <template v-slot="scope">
              <el-button plain type="primary" @click="handleEdit(scope.row)" size="mini" :disabled="scope.row.status === '已接单'">编辑</el-button>
              <el-button plain type="danger" size="mini" @click=del(scope.row.id) :disabled="scope.row.status === '已接单'">删除</el-button>
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
    <el-dialog title="预约信息" :visible.sync="fromVisible" width="60%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="100px" style="padding-right: 50px" :model="form">
        <el-form-item prop="name" label="标题">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="content" label="内容">
          <div id="editor"></div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">提 交</el-button>
      </div>
    </el-dialog>
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
    initWangEditor(content) {
      this.$nextTick(() => {
        this.editor = new E('#editor')
        this.editor.config.placeholder = '请输入内容'
        this.editor.config.uploadFileName = 'file'
        this.editor.config.uploadImgServer = 'http://localhost:9090/files/wang/upload'
        this.editor.create()
        setTimeout(() => {
          this.editor.txt.html(content)
        })
      })
    },
    load(pageNum) {  // 分页查询
      let params = {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      }
      if (this.user.role === 'USER') {
        params.userId = this.user.id
      }
      if (this.user.role === 'TEACHER') {
        params.teacherId = this.user.id
      }
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/publish/selectPage', {
        params: params
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    handleAdd() {
      this.form = {
        userId: this.user.id,
        status: '待接单',
      }
      this.initWangEditor('')
      this.fromVisible = true
    },
    handleEdit(row) {   // 编辑数据
      this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
      this.initWangEditor(this.form.content)
      this.fromVisible = true   // 打开弹窗
    },
    del(id) {   // 单个删除
      this.$confirm('您确定取消吗？', '确认取消', {type: "warning"}).then(response => {
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
    save() {
      this.form.content = this.editor.txt.html()
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
    }
  }
}
</script>
