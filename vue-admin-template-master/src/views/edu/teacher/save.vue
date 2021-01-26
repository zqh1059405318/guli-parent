<template>
  <div class="app-container">
    <el-form label-width="120px">
      <el-form-item label="讲师名称">
        <el-input v-model="teacher.name" />
      </el-form-item>
      <el-form-item label="讲师排序">
        <el-input-number
          v-model="teacher.sort"
          controls-position="right"
          :min="0"
        />
      </el-form-item>
      <el-form-item label="讲师头衔">
        <el-select v-model="teacher.level" clearable placeholder="请选择">
          <!--
            数据类型一定要和取出的json中的一致，否则没法回填
            因此，这里value使用动态绑定的值，保证其数据类型是number
          -->
          <el-option :value="1" label="高级讲师" />
          <el-option :value="2" label="首席讲师" />
        </el-select>
      </el-form-item>
      <el-form-item label="讲师资历">
        <el-input v-model="teacher.career" />
      </el-form-item>
      <el-form-item label="讲师简介">
        <el-input v-model="teacher.intro" :rows="10" type="textarea" />
      </el-form-item>

     <!-- 讲师头像 -->
      <el-form-item label="讲师头像">
          <!-- 头衔缩略图 -->
          <pan-thumb :image="teacher.avatar"/>
           <!-- 文件上传按钮 -->
          <el-button type="primary" icon="el-icon-upload" @click="imagecropperShow=true">更换头像
          </el-button>
          <!--
      v-show：是否显示上传组件
      :key：类似于id，如果一个页面多个图片上传控件，可以做区分
      :url：后台上传的url地址
      @close：关闭上传组件
      @crop-upload-success：上传成功后的回调 -->
      <image-cropper
                   v-show="imagecropperShow"
                   :width="300"
                   :height="300"
                   :key="imagecropperKey"
                   :url="BASE_API+'/eduoss/fileOss'"
                   field="file"
                   @close="close"
                   @crop-upload-success="cropSuccess"/>
      </el-form-item>


      <el-form-item>
        <el-button
          :disabled="saveBtnDisabled"
          type="primary"
          @click="saveOrUpdateTeacher()"
          >保存</el-button
        >
      </el-form-item>
    </el-form>
  </div>
</template>

<script>

import teacherApi from '@/api/edu/teacher'
import ImageCropper from '@/components/ImageCropper'
import PanThumb from '@/components/PanThumb'

export default {
  // 声明组件之后才可以进行使用
  components: { ImageCropper, PanThumb },
  data() {
    return {
      teacher: {
        name: "",
        sort: 0,
        level: 1,
        career: "",
        intro: "",
        avatar: "",
      },

      //上传弹框组件是否显示
      saveBtnDisabled: false, // 保存按钮是否禁用
      imagecropperShow: false, // 上传弹框组件是否显示
      imagecropperKey: 0, //上传组件key值
      BASE_API: process.env.BASE_API, //地址
    };
  },

  created() {
    // 多次跳转到同一个页面，created方法只执行一次
    this.init()
  },

  watch: {
    // 监听
    $route(to, from) {
      // 路由发生变化，就会执行
      this.init()
    },
  },

  methods: {
    close() { //关闭上传弹框的方法
        this.imagecropperShow=false
        //上传组件初始化
        this.imagecropperKey = this.imagecropperKey+1
    },
    //上传成功方法
    cropSuccess(data) {
        this.imagecropperShow=false
        //上传之后接口返回图片地址
        this.teacher.avatar = data.url
        this.imagecropperKey = this.imagecropperKey+1
    },
    // 初始化界面（根据是新增还是添加）
    init() {
      if (this.$route.params && this.$route.params.id) {
        const id = this.$route.params.id;
        this.getInfo(id);
      } else {
        //如果是增加的话，必须把表单里的东西全部清空
        this.teacher = {};
      }
    },

    //讲师列表方法
    saveOrUpdateTeacher() {
      //根据id值判断是修改还是添加
      if (!this.teacher.id) {
        // 没有id，做添加操作，
        this.saveTeacher();
      } else {
        this.updateTeacher();
      }
    },
    saveTeacher() {
      //调用添加函数
      teacherApi.addTeacher(this.teacher).then((response) => {
        // 调用了添加函数，已经添加成功了
        this.$message({
          type: "success",
          message: "新增教师成功!",
        });
        this.$router.push({ path: "/teacher/table" });
        // 回到列表页面，路由跳转
      });
    },

    // 根据讲师ID进行查询修改
    getInfo(id) {
      teacherApi.getTeacherInfo(id).then((response) => {
        this.teacher = response.data.teacher;
      });
    },

    // 更新讲师信息

    updateTeacher() {
      teacherApi.updateTeacher(this.teacher).then((response) => {
        // 调用了添加函数，已经添加成功了
        this.$message({
          type: "success",
          message: "修改教师成功!",
        });
        this.$router.push({ path: "/teacher/table" });
        // 回到列表页面，路由跳转
      });
    },
  },
};
</script>
