<template>

  <div class="app-container">

    <h2 style="text-align: center;">发布新课程</h2>

    <el-steps :active="2" process-status="wait" align-center style="margin-bottom: 40px;">
      <el-step title="填写课程基本信息"/>
      <el-step title="创建课程大纲"/>
      <el-step title="最终发布"/>
    </el-steps>

<el-button type="text">添加章节</el-button>
<!-- 章节 -->
<ul class="chanpterList">
    <li
        v-for="chapter in chapterVideoList"
        :key="chapter.id">
        <p>
            {{ chapter.title }}

            <span class="acts">
                <el-button type="text">添加课时</el-button>
                <el-button style="" type="text">编辑</el-button>
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
      this.$router.push({ path: '/course/info/1' })
    },

    next() {
      console.log('next')
      this.$router.push({ path: '/course/publish/1' })
    },
    getChapterVideo(){
        chapter.getAllChapterVideo(this.courseId).then(response =>{
            this.chapterVideoList = response.data.allChapterVideo
            
        })
    }
  }
}
</script>