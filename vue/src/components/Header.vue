<template>
<!--  <div style="line-height: 60px; display: flex ">-->
<!--    <div style="flex: 1; font-size: 20px">
      <span :class="collapseBtnClass" style="cursor: pointer; font-size: 18px" @click="collapse"></span>
    </div>-->
    <el-dropdown style="width: 70px; cursor: pointer;line-height: 60px ;float:right">
      <span style="color:#0a0b0ba1">王小虎</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item>个人信息</el-dropdown-item>
        <el-dropdown-item>退出</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
<!--  </div>-->
</template>

<script>
export default {
  name: "Header",
  props: {
    collapseBtnClass: String,
    collapse: Boolean,
  },

}

</script>

<style scoped>

</style>