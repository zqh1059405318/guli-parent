<template>

  <div class="app-container">

    <h2 style="text-align: center;">发布新课程</h2>

    <el-steps :active="2" process-status="wait" align-center style="margin-bottom: 40px;">
      <el-step title="填写课程基本信息"/>
      <el-step title="创建课程大纲"/>
      <el-step title="最终发布"/>
    </el-steps>

    <el-button type="text" @click="openChapterDialog">添加章节</el-button>

    <!-- 添加和修改章节表单 -->
    <el-dialog :visible.sync="dialogChapterFormVisible" title="添加章节">
        <el-form :model="chapter" label-width="120px">
            <el-form-item label="章节标题">
                <el-input v-model="chapter.title"/>
            </el-form-item>
            <el-form-item label="章节排序">
                <el-input-number v-model="chapter.sort" :min="0" controls-position="right"/>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="dialogChapterFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="saveOrUpdate">确 定</el-button>
        </div>
    </el-dialog>

    <!-- 章节 -->
    <ul class="chanpterList">
        <li
            v-for="chapter in chapterVideoList"
            :key="chapter.id">
            <p>
                {{ chapter.title }}

                <span class="acts">
                    <el-button type="text" >添加课时</el-button>
                    <el-button style="" type="text" @click="openEditChapter(chapter.id)">编辑</el-button>
                    <el-button type="text">删除</el-button>
                </span>
            </p>

                <!-- 视频 -->
                <ul class="chanpterList videoList">
                    <li
                        v-for="video in chapter.children"
                        :key="video.id">
                        <p>{{ video.title }}
                            <span class="acts">
                                <el-button type="text">编辑</el-button>
                                <el-button type="text">删除</el-button>
                            </span>
                        </p>
                    </li>
                </ul>
            </li>
        </ul>
        <div>
            <el-button @click="previous">上一步</el-button>
            <el-button :disabled="saveBtnDisabled" type="primary" @click="next">下一步</el-button>
        </div>
  </div>
</template>

<script>

import chapter from '@/api/edu/chapter'
export default {
  data() {
        return {
            saveBtnDisabled: false,
            chapterVideoList: [],
            courseId: '', //课程id
            chapter: {
              title: '',
              sort: 0
            }, 
            video: {
              title: '',
              sort: 0,
              isFree: false,
              videoSourceId: '',
              videoOriginalName: ''
            },
            dialogChapterFormVisible: false, //章节弹框是否显示
            dialogVideoFormVisible: false, //小节弹框是否显示
            fileList: [],//上传文件列表
            BASE_API: process.env.BASE_API, // 接口API地址
        }
  },

  created() {
      //首先需要获取端口的路由值，因为值中有地址
      if(this.$route.params && this.$route.params.id){
          this.courseId = this.$route.params.id
          this.getChapterVideo();
      }
      
  },

  methods: {
      previous() {
        console.log('previous')
        this.$router.push({ path: '/course/info/' + this.courseId })
      },

      next() {
        console.log('next')
        this.$router.push({ path: '/course/publish/' + this.courseId })
      },
      getChapterVideo(){
          chapter.getAllChapterVideo(this.courseId).then(response =>{
              this.chapterVideoList = response.data.allChapterVideo
              
          })
      },
      // 只要是弹窗里的确定，都会调用saveOrUpdate函数
      saveOrUpdate(){
        // 如果有id 说明是在修改阶段，因为这个时候已经调用了数据的回显，这个时候的chapter是完整的
        if(this.chapter.id){
          this.updateChapter()
        }else{
          this.addChapter()
        }
        
      },
      
      addChapter() {
        //给chapter添加一个courseId
        this.chapter.courseId = this.courseId
        chapter.addChapter(this.chapter)
                .then(response => {
                  //关闭弹框
                  this.dialogChapterFormVisible = false
                  //提示信息
                  this.$message({
                    type: 'success',
                    message: '添加章节信息成功!'
                  });
                  //刷新页面
                  this.getChapterVideo()
                })
        console.log(this.chapter)
      },

      updateChapter(){
        chapter.updateChapter(this.chapter)
            .then(response =>{
                  //关闭弹框
                  this.dialogChapterFormVisible = false
                  //提示信息
                  this.$message({
                    type: 'success',
                    message: '修改章节信息成功!'
                  });
                  //刷新页面
                  this.getChapterVideo()
            })
      },
      //刷新弹出框,记得要加this，才会指向当前的变量值，添加章节的时候调用
      openChapterDialog(){
          console.log(this.chapter)
          this.chapter.title = ''
          this.chapter.sort = 0
          this.chapter.id = ''
          this.dialogChapterFormVisible = true

      },
      // 修改章节的弹窗，点击编辑的时候调用
      openEditChapter(chapterId){
          //弹框
          this.dialogChapterFormVisible = true
          // 如果
          chapter.getChapter(chapterId)
            .then(response =>{
                this.chapter = response.data.chapter
            })
          // }
      },

      //根据课程id查询章节和小节，展示函数
      getChapterVideo() {
        chapter.getAllChapterVideo(this.courseId).then(response => {this.chapterVideoList = response.data.allChapterVideo})
      },
  }
}
</script>

<style scoped>
.chanpterList{
    position: relative;
    list-style: none;
    margin: 0;
    padding: 0;
}
.chanpterList li{
  position: relative;
}
.chanpterList p{
  float: left;
  font-size: 20px;
  margin: 10px 0;
  padding: 10px;
  height: 70px;
  line-height: 50px;
  width: 100%;
  border: 1px solid #DDD;
}
.chanpterList .acts {
    float: right;
    font-size: 14px;
}
.videoList{
  padding-left: 50px;
}
.videoList p{
  float: left;
  font-size: 14px;
  margin: 10px 0;
  padding: 10px;
  height: 50px;
  line-height: 30px;
  width: 100%;
  border: 1px dotted #DDD;
}
</style>