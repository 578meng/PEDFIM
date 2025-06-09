<template>
<div>
  <el-container>
    <el-header>摸鱼修仙
      <div style="width: 100%; display: inline-block;">
        摸鱼总时长：{{time.day}}天{{time.hour}}时{{time.min}}分{{time.ss}}秒
      </div>
    </el-header>
    <el-main>
      <el-row :gutter="10">
        <el-col :xs="8" :sm="6" :md="4" :lg="3" :xl="1"><div>|</div></el-col>
        <el-col :xs="4" :sm="6" :md="8" :lg="9" :xl="11">
          <el-card class="box-card" shadow="hover" header="人物信息">
            <div slot="header" class="card-header">
              <el-button style="float: right;" type="text">详细属性</el-button>
            </div>
            <div class="card-body">
              <el-progress type="dashboard" :percentage="percentage" :color="colors"></el-progress>
            </div>
          </el-card>
        </el-col>
        <el-col :xs="4" :sm="6" :md="8" :lg="9" :xl="11">
          <el-card class="box-card" shadow="hover">
            <div slot="header" class="clearfix">
              <span>人物信息</span>
              <el-button style="float: right; padding: 3px 0" type="text">详细属性</el-button>
            </div>
          </el-card>
        </el-col>
        <el-col :xs="8" :sm="6" :md="4" :lg="3" :xl="1"><div>|</div></el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :xs="8" :sm="6" :md="4" :lg="3" :xl="1"><div>|</div></el-col>
        <el-col :xs="4" :sm="6" :md="8" :lg="9" :xl="11">
          <el-card class="box-card" shadow="hover">
            <div slot="header" class="clearfix">
              <span>人物信息</span>
              <el-button style="float: right; padding: 3px 0" type="text">详细属性</el-button>
            </div>
          </el-card>
        </el-col>
        <el-col :xs="4" :sm="6" :md="8" :lg="9" :xl="11">
          <el-card class="box-card" shadow="hover">
            <div slot="header" class="clearfix">
              <span>人物信息</span>
              <el-button style="float: right; padding: 3px 0" type="text">详细属性</el-button>
            </div>
          </el-card>
        </el-col>
        <el-col :xs="8" :sm="6" :md="4" :lg="3" :xl="1"><div>|</div></el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :xs="8" :sm="6" :md="4" :lg="3" :xl="1"><div>|</div></el-col>
        <el-col :xs="4" :sm="6" :md="8" :lg="9" :xl="11">
          <el-card class="box-card" shadow="hover">
            <div slot="header" class="clearfix">
              <span>人物信息</span>
              <el-button style="float: right; padding: 3px 0" type="text">详细属性</el-button>
            </div>
          </el-card>
        </el-col>
        <el-col :xs="4" :sm="6" :md="8" :lg="9" :xl="11">
          <el-card class="box-card" shadow="hover">
            <div slot="header" class="clearfix">
              <span>人物信息</span>
              <el-button style="float: right; padding: 3px 0" type="text">详细属性</el-button>
            </div>
          </el-card>
        </el-col>
        <el-col :xs="8" :sm="6" :md="4" :lg="3" :xl="1"><div>|</div></el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :xs="8" :sm="6" :md="4" :lg="3" :xl="1"><div>|</div></el-col>
        <el-col :xs="4" :sm="6" :md="8" :lg="9" :xl="11">
          <el-card class="box-card" shadow="hover">
            <div slot="header" class="clearfix" title="人物信息">
              <el-button style="float: right; padding: 3px 0" type="text">详细属性</el-button>
            </div>
          </el-card>
        </el-col>
        <el-col :xs="4" :sm="6" :md="8" :lg="9" :xl="11">
          <el-card class="box-card" shadow="hover">
            <div slot="header" class="clearfix">
              <span>人物信息</span>
              <el-button style="float: right; padding: 3px 0" type="text">详细属性</el-button>
            </div>
          </el-card>
        </el-col>
        <el-col :xs="8" :sm="6" :md="4" :lg="3" :xl="1"><div>|</div></el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :xs="8" :sm="6" :md="4" :lg="3" :xl="1"><div>|</div></el-col>
        <el-col :xs="4" :sm="6" :md="8" :lg="9" :xl="11">
          <el-card class="box-card" shadow="hover">
            <div slot="header" class="clearfix">
              <span>人物信息</span>
              <el-button style="float: right; padding: 3px 0" type="text">详细属性</el-button>
            </div>
          </el-card>
        </el-col>
        <el-col :xs="4" :sm="6" :md="8" :lg="9" :xl="11">
          <el-card class="box-card" shadow="hover">
            <div slot="header" class="clearfix">
              <span>人物信息</span>
              <el-button style="float: right; padding: 3px 0" type="text">详细属性</el-button>
            </div>
          </el-card>
        </el-col>
        <el-col :xs="8" :sm="6" :md="4" :lg="3" :xl="1"><div>|</div></el-col>
      </el-row>
    </el-main>

    <el-footer>开发者：YTL 版本：1.0.0</el-footer>
  </el-container>

</div>
</template>

<script>
export default {
  name: "game",
  data(){
    return{
      startTime: Date.now(),
      time:{
        day:0,
        hour:0,
        min:0,
        ss:0,
      },
      colors: [
        {color: '#f56c6c', percentage: 20},
        {color: '#e6a23c', percentage: 40},
        {color: '#5cb87a', percentage: 60},
        {color: '#1989fa', percentage: 80},
        {color: '#6f7ad3', percentage: 100}
      ],
      percentage: 0,

    }
  },
  methods:{
    timeCal(){
      let time = Date.now() - this.startTime;
      this.time.day = Math.floor(time / (24 * 3600 * 1000));
      time = time%(24 * 3600 * 1000);
      this.time.hours = Math.floor(time / (3600 * 1000));
      time = time%(3600 * 1000);
      this.time.min = Math.floor(time / (60 * 1000));
      time = time%(60 * 1000);
      this.time.ss = Math.floor(time / (1000));
      this.percentage = (this.percentage + 1) % 101;
      console.log("456456")
    }
  },
  created() {
    setInterval(()=>{this.timeCal()},1000);
  }
}
</script>

<style scoped>
.el-header, .el-footer {
  background-color: #B3C0D1;
  color: #333;
  text-align: center;
  line-height: 60px;
  margin-top: 50px;
  margin-bottom: 50px;
}
.el-main {
  background-color: #E9EEF3;
  color: #333;
  text-align: center;
  line-height: 210px;
  height: 620px;
}
.card-header{
  height: 40px;
}

.card-body{
  height: 160px;
}

.box-card {
  width: 100%;
  height: 200px;
}
.clearfix {
  height: 40px;
  font-size: 25px;
}
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both
}
</style>
