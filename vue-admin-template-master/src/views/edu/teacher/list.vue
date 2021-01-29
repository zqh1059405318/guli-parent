<template>
  <div class="app-container">
    
     <!--查询表单-->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item style="width:16%">
        <el-input v-model="teacherQuery.name" placeholder="讲师名"/>
      </el-form-item>

      <el-form-item style="width:16%">
        <el-select v-model="teacherQuery.level" clearable placeholder="讲师头衔">
          <el-option :value="1" label="高级讲师"/>
          <el-option :value="2" label="首席讲师"/>
        </el-select>
      </el-form-item>

      <el-form-item >
        <el-date-picker
          v-model="teacherQuery.begin"
          type="datetime"
          placeholder="选择开始时间"
          value-format="yyyy-MM-dd HH:mm:ss"
          default-time="00:00:00"
        />
      </el-form-item>
    
      <el-form-item>
        <el-date-picker
          v-model="teacherQuery.end"
          type="datetime"
          placeholder="选择截止时间"
          value-format="yyyy-MM-dd HH:mm:ss"
          default-time="00:00:00"
        />
      </el-form-item>
      <el-button type="primary" icon="el-icon-search" @click="getList()">查询</el-button>
      <el-button type="default" @click="resetData()">清空</el-button>
    </el-form>

    <!-- 表格，表格中的数据由list中取得 :data表示单向绑定 -->
    <el-table
      :data="list"
      border
      fit
      highlight-current-row>

      <el-table-column
        label="序号"
        width="70"
        align="center">
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column prop="name"  align="center" label="名称" width="80" />

      <el-table-column label="头衔"  align="center" width="80">
        <template slot-scope="scope">
          {{ scope.row.level===1?'高级讲师':'首席讲师' }}
        </template>
      </el-table-column>

      <el-table-column prop="intro"  align="center" label="资历" />

      <el-table-column prop="gmtCreate"  align="center" label="添加时间" width="160"/>

      <el-table-column prop="sort" label="排序" align="center" width="60" />

      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <router-link :to="'/teacher/save/'+scope.row.id">
          <!-- 如果点击修改的话，会跳转到对应的save界面，同时会传给save一个当前教师的id值 -->
            <el-button type="primary" size="mini" icon="el-icon-edit">修改</el-button>
          </router-link>
          <el-button type="danger" size="mini" icon="el-icon-delete" @click="removeDataById(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>

    </el-table>

    <!-- 分页 getList不需要传,默认封装好了-->
    <el-pagination
      :current-page="page"
      :page-size="limit"
      :total="total"
      style="padding: 30px 0; text-align: center;"
      layout="total, prev, pager, next, jumper"
      @current-change="getList"  
    />
  </div>
</template>

<script>

//引入teacher.js文件
import teacher from '@/api/edu/teacher'

export default {

    data() {  // 从前端返回给后端的data，在里面可以先填写默认值
        return {
            list: null, //查询之后接口返回的数据集合，也就是teacher的列表
            page: 1, //当前页，默认当前页为1
            limit: 10, //每页记录数，默认为10
            total: 0, //总记录数，默认为0
            teacherQuery: {} //条件封装对象 值可以不定义会自动加进去，默认没有，就说明如果一开始返回data，就是无条件查询
        }
    },
    created() { //当界面被创建出来的时候，就会调用created里面的函数
        this.getList()
    },
    methods: {
        //讲师列表方法，参数也可以有默认值
        getList(page = 1) {
            this.page = page
            teacher.getTeacherListPage(this.page,this.limit,this.teacherQuery) //调用api中的方法发送给后端请求
                   .then(response => {  //得到恢复之后，也就是说response是后端返回给前端的数据
                       this.list = response.data.rows
                       this.total = response.data.total
                   })
        },
        //清空方法，清空表单中的输入条件数据，然后重新查询一遍所有的数据。
        resetData() {
            this.teacherQuery = {}
            this.getList()
        },
        //删除讲师
        removeDataById(id) {
            // 出现一个提示框
            this.$confirm('此操作将永久删除讲师记录, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
              // 调用删除的方法，删除之后.then给出删除成功的信息
              teacher.removeTeacher(id)
                   .then(response => {
                    this.$message({
                    type: 'success',
                    message: '删除成功!'
                    })       
                    this.getList()
                })
          }) 
        }
    }
}
</script>