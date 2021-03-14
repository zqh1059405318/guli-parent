<template>

  <div class="app-container">

    <h2 style="text-align: center;">发布新课程</h2>

    <el-steps :active="2" process-status="wait" align-center style="margin-bottom: 40px;">
      <el-step title="填写课程基本信息"/>
      <el-step title="创建课程大纲"/>
      <el-step title="最终发布"/>
    </el-steps>

    <el-button type="text" @click="openChapterDialog">添加章节</el-button>

    <!-- 添加和修改章节表单  章节输入框-->
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


   <!-- 添加和修改课时表单 课时（video）输入框 -->
    <el-dialog :visible.sync="dialogVideoFormVisible" title="添加课时">
      <el-form :model="video" label-width="120px">
        <el-form-item label="课时标题">
          <el-input v-model="video.title"/>
        </el-form-item>
        <el-form-item label="课时排序">
          <el-input-number v-model="video.sort" :min="0" controls-position="right"/>
        </el-form-item>
        <el-form-item label="是否免费">
          <el-radio-group v-model="video.isFree">
            <el-radio :label=0>免费</el-radio>
            <el-radio :label=1>默认</el-radio>
          </el-radio-group>
        </el-form-item>
       
        <!-- TODO -->
        <el-form-item label="上传视频">
        <el-upload
            :on-success="handleVodUploadSuccess"
            :on-remove="handleVodRemove"
            :before-remove="beforeVodRemove"
            :on-exceed="handleUploadExceed"
            :file-list="fileList"
            :action="BASE_API+'/eduvod/video/uploadAlyVideo'"
            :limit="1"
            class="upload-demo">
        <el-button size="small" type="primary">上传视频</el-button>
        <el-tooltip placement="right-end">
            <div slot="content">最大支持1G，<br>
                支持3GP、ASF、AVI、DAT、DV、FLV、F4V、<br>
                GIF、M2T、M4V、MJ2、MJPEG、MKV、MOV、MP4、<br>
                MPE、MPG、MPEG、MTS、OGG、QT、RM、RMVB、<br>
                SWF、TS、VOB、WMV、WEBM 等视频格式上传</div>
            <i class="el-icon-question"/>
        </el-tooltip>
        </el-upload>
    </el-form-item>
        
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button  @click="dialogVideoFormVisible = false">取 消</el-button>
        <el-button  type="primary" @click="saveOrUpdateVideo">确 定</el-button>
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
                    <el-button type="text" @click="openVideo(chapter.id)">添加小节</el-button>
                    <el-button style="" type="text" @click="openEditChapter(chapter.id)">编辑</el-button>
                    <el-button type="text" @click="removeChapter(chapter.id)">删除</el-button>
                </span>
            </p>

                <!-- 视频 -->
                <ul class="chanpterList videoList">
                    <li
                        v-for="video in chapter.children"
                        :key="video.id">
                        <p>{{ video.title }}
                            <span class="acts">
                                <el-button type="text" @click="openEditVideo(video.id)">编辑</el-button>
                                <el-button type="text" @click="removeVideo(video.id)">删除</el-button>
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
import video from '@/api/edu/video'
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
              isFree: 0,
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

//—————————————与章节有关———————————————//
      // 只要是弹窗里的确定，都会调用saveOrUpdate函数
      saveOrUpdate(){
        // 如果有id 说明是在修改阶段，因为这个时候已经调用了数据的回显，这个时候的chapter是完整的
        if(this.chapter.id){
          this.updateChapter()
        }else{
          this.addChapter()
        }
        
      },
      // 单纯的添加章节
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
      // 删除当前章节，根据里面是否有video来表示
      removeChapter(chapterId){
          console.log(chapterId)
          this.$confirm('此操作将永久删除章节, 是否继续?', '提示', {
                  confirmButtonText: '确定',
                 cancelButtonText: '取消',
                  type: 'warning'
                }).then(()=>{ 
                    chapter.deleteChapter(chapterId)
                      .then(response =>{
                            this.$message({
                            type: 'success',
                            message: '删除成功!'
                            })       
                            this.getChapterVideo()
                      })
                })
      },
      //刷新弹出框,记得要加this，才会指向当前的变量值，添加章节的时候调用
      openChapterDialog(){
            this.chapter.title = ''
            this.chapter.sort = 0
            this.chapter.id = ''
            this.dialogChapterFormVisible = true
      },
      // 修改章节的弹窗，点击编辑的时候调用,这样就可以进行数据的回显
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
//—————————————与章节有关———————————————//

//—————————————与小节有关———————————————//
      openVideo(chapterId){
          this.video.courseId = this.courseId
          this.video.chapterId=chapterId
          this.video.title='',
          this.video.sort=0,
          this.video.isFree=0,
          this.video.videoSourceId='',
          this.video.videoOriginalName=''
          this.video.id = ''
          this.fileList = []
          this.dialogVideoFormVisible = true
      },
      saveOrUpdateVideo(){
          // 如果有值，说明是进行修改
          if(this.video.id){
            this.updateVideo()
          }else{
              this.addVideo()
          }
      },
      // 新增小节
      addVideo(){
          video.addVideo(this.video)
              .then(response=>{
                //关闭弹框
                this.dialogVideoFormVisible = false
                //提示信息
                this.$message({
                  type: 'success',
                  message: '添加小节信息成功!'
                });
                //刷新页面
                this.getChapterVideo()
            })
      },
      // 展示章节信息（数据的回显）
      openEditVideo(videoId){
          //弹框
          this.dialogVideoFormVisible = true
          video.getVideo(videoId)
            .then(response =>{
                this.video = response.data.video
            })
      },
      // 修改章节信息章节
      updateVideo(){
          video.updateVideo(this.video)
              .then(response=>{
                    //关闭弹框
                    this.dialogVideoFormVisible = false
                    //提示信息
                    this.$message({
                      type: 'success',
                      message: '修改小节信息成功!'
                    });
                    //刷新页面
                    this.getChapterVideo()
              })
      },

      // 删除小节
      removeVideo(videoId){
          this.$confirm('此操作将永久删除小节, 是否继续?', '提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
                }).then(()=>{ 
                    video.deleteVideo(videoId)
                      .then(response =>{
                            this.$message({
                            type: 'success',
                            message: '删除小节信息成功!'
                            })       
                            this.getChapterVideo()
                      })
                })
      },


//—————————————与小节有关———————————————//

//—————————————与视频有关———————————————//
       //点击x调用的方法
      //TODO:，添加视频之后，直接刷新后，再编辑的时候，无法看到相关的视频
       beforeVodRemove(file,fileList) {
         return this.$confirm(`确定移除 ${file.name}?`)
       },
       //点击确定调用的方法
       handleVodRemove() {
         video.deleteAliyunvod(this.video.videoSourceId)
              .then(response => {
                 this.$message({
                     type: 'success',
                     message: '删除视频成功!'
                   });
                 //把文件列表清空
                 this.fileList = []  
                 //删除视频之后把视频id和视频名称清空
                 this.video.videoSourceId = ''
                 this.video.videoOriginalName = ''
              })
       },
       //上传成功之后调用的方法
       handleVodUploadSuccess(response, file, fileList) {
         this.video.videoSourceId = response.data.videoId
         this.video.videoOriginalName = file.name
       },
       //上传之前调用的方法
       handleUploadExceed() {
         this.$message.warning('想要重新上传视频，请先删除已上传的视频')
       },

//—————————————与视频有关———————————————//

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