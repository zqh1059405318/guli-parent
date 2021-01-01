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

      <!-- 讲师头像：TODO -->

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
//引入teacher.js文件
import teacherApi from "@/api/edu/teacher";

export default {
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
      saveBtnDisabled: false,
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
