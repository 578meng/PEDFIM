<template>
  <div id="pc">
  </div>

</template>

<script>

const nodeCMD = require('node-cmd');
const os = require('os-utils');
// import PCInformation from './PCInformation'
import PCI from './PCI'
export default {
  name: "pc",
  data(){
    return{

    }
  },
  created() {
    console.log(PCI.get())
    // console.log(PCInformation.getIPAdress())
  },
  methods:{

  }
}
</script>

<style scoped>

</style>
