<template>

  <div class="app-container">

    <h2 style="text-align: center;">发布新课程</h2>

    <el-steps :active="1" process-status="wait" align-center style="margin-bottom: 40px;">
      <el-step title="填写课程基本信息"/>
      <el-step title="创建课程大纲"/>
      <el-step title="提交审核"/>
    </el-steps>

    <el-form label-width="120px">

        <el-form-item label="课程标题">
            <el-input v-model="courseInfo.title" placeholder=" 示例：机器学习项目课：从基础到搭建项目视频课程。专业名称注意大小写"/>
        </el-form-item>

        <!-- 所属分类 TODO -->
        <el-form-item label="课程分类">
        <!-- 一级分类 -->
        <el-select
            v-model="courseInfo.subjectParentId"
            placeholder="请选择一级分类" @change="subjectLevelOneChanged">
            <el-option
                v-for="subject in subjectOneList"
                :key="subject.id"
                :label="subject.title"
                :value="subject.id"/>
        </el-select>

        <!-- 二级分类 -->
        <el-select v-model="courseInfo.subjectId" placeholder="请选择">
            <el-option
                v-for="subject in subjectTwoList"
                :key="subject.id"
                :label="subject.title"
                :value="subject.id"/>
        </el-select>
        </el-form-item>


        <!-- 课程讲师 -->
        <el-form-item label="课程讲师">
        <el-select
            v-model="courseInfo.teacherId"
            placeholder="请选择">
            <el-option
            v-for="teacher in teacherList"
            :key="teacher.id"
            :label="teacher.name"
            :value="teacher.id"/>
        </el-select>
        </el-form-item>

        <el-form-item label="总课时">
            <el-input-number :min="0" v-model="courseInfo.lessonNum" controls-position="right" placeholder="请填写课程的总课时数"/>
        </el-form-item>

        <!-- 课程简介 TODO -->
        <el-form-item label="课程简介">
            <tinymce :height="300" v-model="courseInfo.description"/>
        </el-form-item>
        <!-- 课程封面 TODO -->


        <el-form-item label="课程封面">
        <el-upload
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
            :action="BASE_API+'/eduoss/fileOss'"
            class="avatar-uploader">
            <img :src="courseInfo.cover">
        </el-upload>
        </el-form-item>

        <el-form-item label="课程价格">
            <el-input-number :min="0" v-model="courseInfo.price" controls-position="right" placeholder="免费课程请设置为0元"/> 元
        </el-form-item>

        <el-form-item>
            <el-button :disabled="saveBtnDisabled" type="primary" @click="saveOrUpdate">保存并下一步</el-button>
        </el-form-item>
    </el-form>

  </div>
</template>


<script>
import subject from '@/api/edu/subject'
import course from '@/api/edu/course'
import Tinymce from '@/components/Tinymce'
export default {
    // 声明组件
  components: { Tinymce },
  data() {
        return {
            saveBtnDisabled: false,
            courseInfo: {
                 title: '',
                 subjectId: '', //二级分类Id
                 subjectParentId: '', //一级分类Id
                 teacherId: '',
                 lessonNum: 0,
                 description: '',
                 cover: '/static/01.jpg',
                 price: 0
            },
            BASE_API: process.env.BASE_API, // 接口API地址
            teacherList: [], //存放所有讲师的数据
            subjectOneList: [], //一级分类
            subjectTwoList: [], //二级分类
            courseId: ''
        }
  },

  created() {
      
      this.init()
  },
  // 如果不加watch监听会出现以下问题：第一次点击修改的时候，界面里有数据，第二次再去点击
  // 添加讲师，预期没有数据，结果表单页面上还是显示数据，这是因为多次路由跳转到同一个界面的时候，
  //created方法只会执行一次 ，因此一直是第一次的数据。
  watch: {
    // 监听
    $route(to, from) {
      // 路由发生变化，就会执行
      this.init()
    },
  },

  methods: {
        init(){
            //首先判断是否有初始值，如果有，说明是第二/三步返回的，因此要数据回显
            if(this.$route.params && this.$route.params.id) {
                this.courseId = this.$route.params.id
                this.getInfo()
            }else{
                this.getListTeacher()
                this.getOneSubject()
            }
        },
        saveOrUpdate() {
            if(this.courseInfo.id){
                this.upDateCourse()
            }
            else{
                this.addCourse()
            }

            
        },
        // 更新课程
        upDateCourse(){
            course.updateCourseInfo(this.courseInfo)
                .then(response =>{
                    this.$message({
                        type: 'success',
                        message: '修改课程信息成功!'
                        }),     
                    this.$router.push({ path: '/course/chapter/'+this.courseId})
                })
        },

        //添加课程
        addCourse(){
            course.addCourseInfo(this.courseInfo)
                .then(response =>{
                    this.$message({
                        type: 'success',
                        message: '添加课程信息成功!'
                        }),     
                    this.$router.push({ path: '/course/chapter/'+response.data.courseId})
                })
        },
        //查询所有的讲师
        getListTeacher() {
            course.getListTeacher().then(response => {this.teacherList = response.data.items})
        },

        //查询所有的一级分类
        getOneSubject() {
            subject.getSubjectList().then(response => {this.subjectOneList = response.data.reslist})
        },

        //当一级分类发生改变时，执行该方法
        subjectLevelOneChanged(value){
            for(var i = 0; i < this.subjectOneList.length; i++){
                var oneSubject = this.subjectOneList[i]
                if(value === oneSubject.id){
                    this.subjectTwoList = oneSubject.children
                    // 点第二次的时候，还是会保留当前的二级选项，所以每次change之后，都要将subjectId清零
                    this.courseInfo.subjectId = ''
                }
            }
        },
             //上传封面之前
        beforeAvatarUpload(file) {
            const isJPG = file.type === 'image/jpeg'
            const isLt2M = file.size / 1024 / 1024 < 2
            if (!isJPG) {
                this.$message.error('上传头像图片只能是 JPG 格式!')
            }
            if (!isLt2M) {
                this.$message.error('上传头像图片大小不能超过 2MB!')
            }
            return isJPG && isLt2M
        },
        //上传封面成功
        handleAvatarSuccess(res,file) {
            this.courseInfo.cover = res.data.url
        },

        // 数据回显函数
        getInfo() {
            course.getCourseInfoId(this.courseId)
                    .then(response => {      
                        this.courseInfo = response.data.courseInfo         
                        //查询有的分类
                        subject.getSubjectList()
                               .then(res => {
                                   //获取所有一级分类
                                   this.subjectOneList= res.data.reslist
                                   //把所有一级分类数组进行遍历,比较当前curseInfo里面一级分类id和所有一级分类id
                                   for (var i = 0; i < this.subjectOneList.length; i++) {
                                       var oneSubject = this.subjectOneList[i]
                                       if(this.courseInfo.subjectParentId == oneSubject.id) {
                                           this.subjectTwoList = oneSubject.children
                                       }
                                   }
                               })
                        this.getListTeacher()
                    })
        },

  }
}
</script>

<style scoped>
.tinymce-container {
  line-height: 29px;
}
</style>