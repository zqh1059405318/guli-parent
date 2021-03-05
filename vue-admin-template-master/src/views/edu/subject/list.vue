<template>
  <div class="app-container">
    <el-input v-model="filterText" placeholder="Filter keyword" style="margin-bottom:30px;" />

    <el-tree
      ref="tree2"
      :data="data2"
      :props="defaultProps"
      :filter-node-method="filterNode"
      class="filter-tree"
      default-expand-all
    />

  </div>
</template>

<script>

import subjectApi from '@/api/edu/subject'
export default {

  data() {
    return {
      filterText: '',
      data2: [], // 返回空的数组
      defaultProps: {
        children: 'children',
        label: 'title'
      }
    }
  },
  created(){
    this.getAllSubjectList()
  },
  watch: {
    filterText(val) {
      this.$refs.tree2.filter(val)
    }
  },

  methods: {
    getAllSubjectList(){
      subjectApi.getSubjectList().then( response =>{
          this.data2 = response.data.reslist;
      })
    },
    filterNode(value, data) {
      if (!value) return true
      // 使得大小写不敏感
      return data.title.toLowerCase().indexOf(value.toLowerCase()) !== -1
    }
  }
}
</script>

