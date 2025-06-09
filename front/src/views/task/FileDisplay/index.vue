<template>
  <div style="margin:0px;padding:0px;width:100%;height:400px;">
    <vue-office-excel v-if="file.fileType == 'xlsx'" :src="file.url" style="margin:0px;padding:0px;width:100%;height:100%;"/>
    <textarea v-if="file.fileType == 'txt'" v-model="fileContent" readonly style="margin:0px;padding:0px;width:100%;height:400px;"></textarea>
    <textarea v-if="file.fileType == 'csv'" v-model="fileContent" readonly style="margin:0px;padding:0px;width:100%;height:400px;"></textarea>
  </div>
</template>

<script>
import VueOfficeExcel from "@vue-office/excel";
import '@vue-office/excel/lib/index.css'

export default {
  name: "FileDisplay",
  components:{
    VueOfficeExcel
  },
  props:{
    file:{}
  },
  data() {
    return{
      fileContent:""
    }
  },
  created() {
    console.log(this.file)
    if(this.file.fileType === 'txt'){
      fetch(this.file.url)
        .then(response => response.text())
        .then(text => {
          this.fileContent = text;
        })
        .catch(error => {
          console.error('Error loading txt file:', error);
          this.fileContent = 'Error loading file';
        });
    }
    else if(this.file.fileType === 'csv'){
      fetch(this.file.url)
        .then(response => response.text())
        .then(text => {
          this.fileContent = text;
        })
        .catch(error => {
          console.error('Error loading txt file:', error);
          this.fileContent = 'Error loading file';
        });
    }

  }
}
</script>

<style scoped>

</style>
