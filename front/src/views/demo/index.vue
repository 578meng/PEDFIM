<template>
  <div class="demo-body">
    <h1>Demo测试界面</h1>
    <el-divider content-position="left">文件展示Demo</el-divider>
    <el-collapse accordion>
      <el-collapse-item title="TXT 展示" name="1">
        <FileDisplay :file="fileList[0]"></FileDisplay>
      </el-collapse-item>
      <el-collapse-item title="Excel 展示" name="2">
        <FileDisplay :file="fileList[1]"></FileDisplay>
      </el-collapse-item>
      <el-collapse-item title="CSV 展示" name="3">
        <FileDisplay :file="fileList[2]"></FileDisplay>
      </el-collapse-item>
    </el-collapse>


    <el-divider content-position="left">待开发。。。</el-divider>
  </div>
</template>

<script>
import FileDisplay from "../task/FileDisplay/index"
export default {
  components:{
    FileDisplay
  },
  data(){
    return {
      fileList:[]
    }
  }
  ,created() {
    this.fileList = [
      {name:"mushroom.txt",type:"txt",path:"C:\\Users\\wxhllj\\Desktop\\SPEFIMSYSTEM\\mushroom.txt"}
      ,{name:"task.xlsx",type:"xlsx",path:"C:\\Users\\wxhllj\\Desktop\\SPEFIMSYSTEM\\task.xlsx"}
      ,{name:"mushroom.csv",type:"csv",path:"C:\\Users\\wxhllj\\Desktop\\SPEFIMSYSTEM\\mushroom.csv"}
      ]
    console.log(this.fileList)
  }

}
</script>

<style scoped>
  .demo-body{
    height: 650px;
    width: 1400px;
    /*background-color: #00afff;*/
    margin-left: 70px;
    text-align: center;
  }
</style>
