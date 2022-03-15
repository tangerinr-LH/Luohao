<template>
  <div style="min-height: 100vh">
    <el-container style="height: 100vh; border: 1px solid #eee">
      <el-aside :width="sideWidth + 'px'"
                style="background-color: rgb(238, 241, 246); box-shadow: 2px 0 6px rgba(0, 21, 41 ,0.35);">
        <Aside :isCollapse="isCollapse" :logoTextShow="logoTextShow"/>
      </el-aside>

      <el-container>

        <el-header style="border-bottom: 1px solid #ccc;">
<!--          <Header :collapseBtnClass="collapseBtnClass" :collapse="isCollapse"/>-->

          <span :class="collapseBtnClass" style="cursor: pointer; font-size: 18px" @click="collapse"></span>

          <Header :collapseBtnClass="collapseBtnClass"  :collapse="isCollapse" />



        </el-header>

        <!--
        页签
        -->

        <div style="padding: 35px; padding-bottom: 0px;">
<!--          separator="/"-->
          <el-breadcrumb class="breadcrumb-container" separator-class="el-icon-arrow-right" style="font-size: 17px">
            <el-breadcrumb-item v-for="item in levelList" :key="item.path" :to="item.path">{{item.meta.title}}</el-breadcrumb-item>

          </el-breadcrumb>

        </div>


        <el-main>
          <!--        表示当前页面的子路由会在 router-view 里展示 -->
          <router-view></router-view>


        </el-main>
      </el-container>
    </el-container>

  </div>
</template>

<script>

import request from "@/utils/request";
import Aside from "@/components/Aside";
import Header from "@/components/Header";

export default {
  name: 'Home',
  data() {
    return {
      collapseBtnClass: 'el-icon-s-fold',
      isCollapse: false,
      sideWidth: 200,
      logoTextShow: true,
      levelList: []
    }
  },


  /*引入侧边栏*/
  components: {
    Aside,
    Header
  },

  watch: {
    $route() {
      this.getBreadcrumb()
      console.log(this.$route.matched)
    }
  },

  created() {
    this.getBreadcrumb()
  },

  methods: {
    collapse() {  // 点击收缩按钮触发
      this.isCollapse = !this.isCollapse
      if (this.isCollapse) {  // 收缩
        this.sideWidth = 64
        this.collapseBtnClass = 'el-icon-s-unfold'
        this.logoTextShow = false
      } else {   // 展开
        this.sideWidth = 200
        this.collapseBtnClass = 'el-icon-s-fold'
        this.logoTextShow = true
      }
    },

    getBreadcrumb() {
      //$route.matched一个数组 包含当前路由的所有嵌套路径片段的路由记录,过滤出所有包含name 的item
      let matched = this.$route.matched.filter(item => item.name)
      const first = matched[0];
      if (first && first.name.trim().toLocaleLowerCase() !== '首页'.toLocaleLowerCase()) {
        matched = [{path: '/', meta: {title: '首页'}}].concat(matched)
      }
      this.levelList = matched
    }
  },


}


</script>

<style>
.headerBg {
  background: #9fccd766 !important;
}
</style>