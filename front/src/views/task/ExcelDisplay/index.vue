<template>
  <div class="hello">
    <div id="mysheet" style="margin:0px;padding:0px;width:100%;height:400px;"></div>
  </div>
</template>

<script>
import LuckyExcel from "luckyexcel";

export default {
  name: "ExcelDisplay",
  props:{
    file:{}
  },
  mounted() {
    this.excelDisplay();
  },
  beforeDestroy() {
    console.log("要被销毁了！！！！")
    window.luckysheet.destroy();
  },
  methods:{
    excelDisplay(){
      console.log("excelDisplay")
      console.log(this.file)
      // 加载 excel 文件
      LuckyExcel.transformExcelToLuckyByUrl(this.file.url,
        this.file.fileName, (exportJson, luckysheetfile) => {
          console.log(exportJson,"exportJson");
          console.log(luckysheetfile,"luckysheetfile");
          if(exportJson.sheets==null || exportJson.sheets.length==0){
            alert("文件读取失败!");
            return;
          }
          // 销毁原来的表格
          // window.luckysheet.destroy();
          // 重新创建新表格
          window.luckysheet.create({
            container: 'mysheet', // 设定DOM容器的id
            showtoolbar: true, // 是否显示工具栏
            showinfobar: true, // 是否显示顶部信息栏
            showstatisticBar: true, // 是否显示底部计数栏
            sheetBottomConfig: true, // sheet页下方的添加行按钮和回到顶部按钮配置
            allowEdit: true, // 是否允许前台编辑
            enableAddRow: true, // 是否允许增加行
            enableAddCol: true, // 是否允许增加列
            sheetFormulaBar: true, // 是否显示公式栏
            enableAddBackTop: true,//返回头部按钮
            data:exportJson.sheets, //表格内容
            title:exportJson.info.name  //表格标题
          });
        });
    },
  }

}
</script>

<style scoped>

</style>
