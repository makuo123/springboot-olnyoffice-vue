<template>
  <div id="app">
    <el-card>
      <!-- 搜索 添加 -->
      <el-row :gutter="20">
        <el-col :span="6">
        </el-col>
        <el-col :span="4">
          <input type="file" ref="myfile">
          <el-button @click="upload" type="success" size="mini" icon="el-icon-upload2">上传文件</el-button>
        </el-col>
      </el-row>
      <!-- 用户列表区域 -->
      <el-table :data="fileList" border stripe>
        <!-- stripe: 斑马条纹
        border：边框-->
        <el-table-column type="index" label="#"></el-table-column>
        <el-table-column prop="file_name" label="文件名称"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button
                    type="primary"
                    icon="el-icon-edit"
                    size="mini"
                    circle
                    @click="edit(scope.row)"
            >编辑</el-button>
            <el-button
                    type="danger"
                    icon="el-icon-delete"
                    size="mini"
                    circle
                    @click="review(scope.row)"
            >预览</el-button>
            <el-button
                    type="danger"
                    icon="el-icon-delete"
                    size="mini"
                    circle
                    @click="download(scope.row)"
            >下载</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>

export default {
  name: 'App',
  data() {
    return {
      fileList: [],
    }
  },
  created() {
    this.getFileList()
  },
  methods: {
    upload () {
      let myfile = this.$refs.myfile
      // console.log(myfile.files)
      // let files = myfile.files
      let file = myfile.files[0]
      console.log(file)
      var forms = new FormData()
      var configs = {
        headers: { 'Content-Type': 'multipart/form-data' }
      }
      forms.append('file', file)
      this.$http.post('http://192.168.1.6:20053/upload', forms, configs).then(res => {
        console.log(res)
      })
    },
    edit(row) {
      let user = JSON.parse(window.sessionStorage.getItem('user'))
      window.open('http://192.168.1.6:20053/edit?name='+row.file_name+'&userName='+'admin'+'&userId='+'1')

    },
    review(row) {},
    download(row) {
      this.$http.get('http://192.168.1.6:20053/editStatus?name='+row.file_name).then(res => {
        console.log(res)
        if (res.data.error == 0) {
          alert('文档正在编辑，5秒后开始下载最新版！');
          setTimeout(function() {
            window.location.href = "http://192.168.1.6:20053/download?name=" +'v1'+ row.file_name;
            // var $eleForm = $("<form method='get'></form>");
            // $eleForm.attr("action",  "http://192.168.0.58:20053/download?name=" + row.file_name);
            // $(document.body).append($eleForm);
            // //提交表单，实现下载
            // $eleForm.submit();
          }, 5000);
          return;
        }else {
          alert('文档未编辑，开始下载！');
        }
        window.location.href = "http://192.168.1.6:20053/download?name=" + row.file_name;
        // var $eleForm = $("<form method='get'></form>");
        // $eleForm.attr("action",  "http://192.168.0.58:20053/download?name=" + row.file_name);
        // $(document.body).append($eleForm);
        // //提交表单，实现下载
        // $eleForm.submit();
      })
    },
    // 获取文件列表
    getFileList () {
      this.$http.get('http://192.168.1.6:20053/filelist').then(res => {
        console.log(res)
        this.fileList = res.data
      })
    },
  }
}
</script>

<style>

</style>
