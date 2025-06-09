const diskinfo = require('diskinfo'); // 需安装diskinfo依赖

function get(){
  diskinfo.getDrives(function (err, aDrives) {
    for (var i = 0; i < aDrives.length; i++) {
      console.log('磁盘：' + aDrives[i].mounted);
      console.log('磁盘总内存：' + aDrives[i].blocks);
      console.log('磁盘已用内存：' + aDrives[i].used);
      console.log('磁盘可用内存：' + aDrives[i].available);
      console.log('磁盘已用内存百分比： ' + aDrives[i].capacity);
      console.log('-----------------------------------------');
    }
  })
}



export default {
  get
}
