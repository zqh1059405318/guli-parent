import request from '@/utils/request'

export default {
    // 展示课程列表
    getSubjectList() {
        return request({
            url: `/eduservice/subject/getAllSubject`,
            method: 'get',
        })
    },

}

