<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入姓名查询" style="width: 200px" v-model="teacherName"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe>
        <el-table-column prop="teacherName" label="教员姓名" show-overflow-tooltip fixed="left"></el-table-column>
        <el-table-column prop="code" label="教员编号" show-overflow-tooltip></el-table-column>
        <el-table-column prop="birth" label="出生年月" show-overflow-tooltip></el-table-column>
        <el-table-column prop="school" label="所在学校"></el-table-column>
        <el-table-column prop="speciality" label="所学专业"></el-table-column>
        <el-table-column prop="education" label="最高学历"></el-table-column>
        <el-table-column prop="province" label="所在省份"></el-table-column>
        <el-table-column prop="type" label="授课方式"></el-table-column>
        <el-table-column prop="typeName" label="可授科目"></el-table-column>
        <el-table-column prop="description" label="个人介绍" show-overflow-tooltip></el-table-column>
        <el-table-column prop="plan" label="授课安排" show-overflow-tooltip></el-table-column>
        <el-table-column prop="star" label="教员星级"></el-table-column>
        <el-table-column prop="level" label="教员身份"></el-table-column>
        <el-table-column prop="status" label="审核状态"></el-table-column>
        <el-table-column prop="reason" label="审核理由"></el-table-column>

        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template v-slot="scope">
            <el-button plain type="primary" @click="handleEdit(scope.row)" size="mini">审核</el-button>
          </template>
        </el-table-column>
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


    <el-dialog title="审核信息" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="100px" style="padding-right: 50px" :model="form">
        <el-form-item prop="star" label="教员星级">
          <el-rate v-model="form.star"></el-rate>
        </el-form-item>
        <el-form-item prop="level" label="教员身份">
          <el-select v-model="form.level" placeholder="请选择" style="width: 100%">
            <el-option label="认证教员" value="认证教员"></el-option>
            <el-option label="优秀教员" value="优秀教员"></el-option>
            <el-option label="金牌教员" value="金牌教员"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="status" label="审核状态">
          <el-select v-model="form.status" placeholder="请选择" style="width: 100%">
            <el-option label="待审核" value="待审核"></el-option>
            <el-option label="审核通过" value="审核通过"></el-option>
            <el-option label="审核不通过" value="审核不通过"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="reason" label="审核理由">
          <el-input v-model="form.reason" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>


  </div>
</template>

<script>
export default {
  name: "Info",
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      teacherName: null,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {},
      ids: []
    }
  },
  created() {
    this.load(1)
  },

  methods: {
    handleAdd() {   // 新增数据
      this.form = {}  // 新增数据的时候清空数据
      this.fromVisible = true   // 打开弹窗
    },
    handleEdit(row) {   // 编辑数据
      this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
      this.fromVisible = true   // 打开弹窗
    },
    save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.$request.put('/info/update', this.form).then(res => {
        if (res.code === '200') {
          this.$message.success('操作成功')
          this.fromVisible = false
          this.load(1)
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    del(id) {   // 单个删除
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/info/delete/' + id).then(res => {
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
        this.$request.delete('/info/delete/batch', {data: this.ids}).then(res => {
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
      this.$request.get('/info/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          teacherName: this.teacherName
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.teacherName = null
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