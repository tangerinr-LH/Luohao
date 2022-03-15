<template>
<div>
  <div style="padding: 10px 0">
    <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search"
              v-model="username"></el-input>
    <el-input style="width: 200px" placeholder="请输入邮箱" suffix-icon="el-icon-message" class="ml-5"
              v-model="email"></el-input>
    <el-input style="width: 200px" placeholder="请输入地址" suffix-icon="el-icon-map-location" class="ml-5" v-model="address
"></el-input>
    <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
    <el-button class="ml-5" type="warning" @click="reset">重置</el-button>
  </div>

  <!--
  功能按钮
  -->
  <div style="padding: 10px 0">
    <el-button type="primary" @click="handleadd">新增<i class="el-icon-circle-plus-outline"></i></el-button>
    <el-button type="danger" @click="open">批量删除<i class="el-icon-remove-outline"></i></el-button>
    <el-upload action="http://localhost:9090/user/import" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
      <el-button type="primary" class="ml-5">导入 <i class="el-icon-bottom"></i></el-button>
    </el-upload>
    <el-button type="primary" @click="exp" class="ml-5">导出 <i class="el-icon-top"></i></el-button>
  </div>
  <el-table :data="tableData" border stripe header-cell-class-name="headerBg"
            @selection-change="handleSelectionChange" v-loading="loading">

    <el-table-column type="selection" width="55"></el-table-column>

    <el-table-column prop="username" label="用户名" width="140">
    </el-table-column>
    <el-table-column prop="nickname" label="昵称" width="120">
    </el-table-column>
    <el-table-column prop="address" label="地址">
    </el-table-column>
    <el-table-column prop="email" label="邮箱">
    </el-table-column>
    <el-table-column prop="phone" label="电话">
    </el-table-column>
    <el-table-column label="操作">
      <template slot-scope="scope">
        <el-button
            size="mini"

            @click="handleEdit( scope.row)" icon="el-icon-edit">编辑
        </el-button>

        <!--
        删除确认框
        -->
        <el-popconfirm
            placement="bottom"
            confirm-button-text='好的'
            cancel-button-text='不用了'
            icon="el-icon-info"
            icon-color="red"
            title="确定删除这一段记录吗？"
            @confirm="handleDelete(scope.row)"
        >
          <el-button
              class="ml-5"
              size="mini"
              type="danger"
              slot="reference"
              icon="el-icon-delete">删除
          </el-button>
        </el-popconfirm>
      </template>
    </el-table-column>
  </el-table>
  <div style="padding: 10px 0">
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[5, 10, 15, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>
  </div>

  <!--   弹窗   -->
  <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%" center>
    <el-form label-width="80px" size="small">
      <el-form-item label="用户名">
        <el-input v-model="form.username" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="昵称">
        <el-input v-model="form.nickname" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="form.email" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="电话">
        <el-input v-model="form.phone" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="地址">
        <el-input v-model="form.address" autocomplete="off"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogFormVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">保存</el-button>
    </div>
  </el-dialog>
</div>

</template>

<script>
import request from "@/utils/request";

export default {
  name: "User",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 5,
      username: "",
      email: "",
      address: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      loading: true

    }
  },

  created() {
    this.load()
  },


  methods: {

    load() {
      /* fetch("http://localhost:9090/user/page?pageNum="+this.pageNum+"&pageSize="+this.pageSize).then(res=>res.json()).then(res=>{console.log(res)
         this.tableData=res.data
         this.total=res.total
       })*/
      request.get("http://localhost:9090/user/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          username: this.username,
          email: this.email,
          address: this.address,
        }

      }).then(res => {
        console.log(res)
        this.tableData = res.records
        this.total = res.total
        this.loading = false
      })

    },
    /*重置*/
    reset() {
      this.username = ""
      this.email = ""
      this.address = ""
      this.load()
    },

    /*新增弹窗*/
    handleadd() {
      this.dialogFormVisible = true
      this.form = {}
    },

    /*新增数据*/
    save() {
      request.post("http://localhost:9090/user/save", this.form).then(res => {
        if (res) {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
    },

    /*批量删除*/
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val;
    },

    /* removeList() {
       let ids = this.multipleSelection.map(v => v.id) //let用于声明变量，只在该代码块生效  将对象数组处理为id数组
       request.post("http://localhost:9090/user/removeList",ids).then(res=>{
         if (res) {
           this.$message.success("批量删除成功")
           this.load()
         } else {
           this.$message.error("批量删除失败")
         }
       })
     },*/

    /*批量删除*/
    open() {

      this.$confirm('是否批量删除选中的数据?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        /* this.$message({
           type: 'success',
           message: '删除成功!'
         });*/
        let ids = this.multipleSelection.map(v => v.id) //let用于声明变量，只在该代码块生效  将对象数组处理为id数组
        request.post("http://localhost:9090/user/removeList", ids).then(res => {
          if (res) {
            this.$message.success("批量删除成功")
            const totalPage = Math.ceil((this.total - this.multipleSelection.length) / this.pageSize) // 剩余数据总页数
            this.pageNum = this.pageNum > totalPage ? totalPage : this.pageNum
            this.pageNum = this.pageNum < 1 ? 1 : this.pageNum
// this.multipleSelection 是批量删除的数组
            this.load()
          } else {
            this.$message.error("批量删除失败")
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },

    /*修改数据*/
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    /*删除数据*/
    handleDelete(row) {
      request.put("http://localhost:9090/user/remove" + "?id=" + row.id).then(res => {
        if (res) {
          this.$message.success("删除成功")
          const totalPage = Math.ceil((this.total - 1) / this.pageSize) // 总页数
          this.pageNum = this.pageNum > totalPage ? totalPage : this.pageNum
          this.pageNum = this.pageNum < 1 ? 1 : this.pageNum
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
      this.load()
    },


    /*导出*/
    exp() {
      window.open("http://localhost:9090/user/export")
    },

    /*导入数据*/
    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load()
    },

    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize;
      this.load()
    },


    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    }
  }



}
</script>


<style >
.headerBg {
  background: #9fccd766 !important;
}
</style>