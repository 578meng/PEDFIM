<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="任务类型" prop="taskType">
        <el-select v-model="queryParams.taskType" placeholder="请选择任务类型" clearable>
          <el-option
            v-for="dict in dict.type.task_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="任务状态" prop="taskStatus">
        <el-select v-model="queryParams.taskStatus" placeholder="请选择任务状态" clearable>
          <el-option
            v-for="dict in dict.type.task_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['task:pefim:add']"
        >新建任务</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['task:pefim:remove']"
        >删除任务</el-button>
      </el-col>

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="pefimList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="任务名称" align="center" prop="taskName" />
      <el-table-column label="算法类型" align="center" prop="algorithmType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.algorithm_type" :value="scope.row.algorithmType"/>
        </template>
      </el-table-column>
      <el-table-column label="任务类型" align="center" prop="taskType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.task_type" :value="scope.row.taskType"/>
        </template>
      </el-table-column>

      <el-table-column label="开始时间" align="center" prop="startTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d} {h}:{m}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d} {h}:{m}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="任务状态" align="center" prop="taskStatus">
        <template slot-scope="scope">
          <el-tag
            :type="scope.row.taskStatus === 'running' ? 'primary' : scope.row.taskStatus === 'success'?'success' : 'danger'"
            disable-transitions>
            <dict-tag :options="dict.type.task_status" :value="scope.row.taskStatus"/>
          </el-tag>

        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding" width="250px">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleDetail(scope.row)"
            v-hasPermi="['task:pefim:edit']"
          >查看任务配置</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleResult(scope.row)"
            v-hasPermi="['task:pefim:edit']"
          >查看任务结果</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleDataExport(scope.row)"
            v-hasPermi="['task:pefim:edit']"
          >导出数据集</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleResultExport(scope.row)"
            v-hasPermi="['task:pefim:edit']"
          >导出结果集</el-button>
          <el-button
            size="mini"
            type="text danger"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['task:pefim:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或查看PEFIM对话框 -->
    <el-dialog :title="title" :visible.sync="open" :width="width" append-to-body :before-close="cancel">
      <!-- 添加PEFIM对话框 -->
      <el-form ref="form" :model="form" :rules="rules" label-width="80px" v-if="state === 'add'">
        <!--        <ExcelDisplay v-if="this.form.dataFile !== null" :file="this.form.dataFile"></ExcelDisplay>-->
        <el-form-item label="任务名称" prop="taskName">
          <el-input v-model="form.taskName" placeholder="请输入任务名称" />
        </el-form-item>
        <el-form-item label="任务类型" prop="taskType">
          <el-select v-model="form.taskType" placeholder="请选择任务类型">
            <el-option
              v-for="dict in dict.type.task_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数据集" prop="dataFile">
          <el-upload
            class="upload-demo"
            ref="upload"
            drag
            :headers="upload.headers"
            :action="upload.url"
            :limit="1"
            :on-exceed="handleExceed"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :before-remove="beforeRemove"
            :on-success="handleSuccess"
            multiple>
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <div class="el-upload__tip" slot="tip">只能上传txt\xlx\xlsx\csv文件，且不超过1GB</div>
          </el-upload>
        </el-form-item>
        <el-form-item label="支持度" prop="support">
          <el-input v-model="form.support" placeholder="请输入支持度" />
        </el-form-item>
        <el-form-item label="置信度" prop="confidence" v-show="form.taskType === 'rules'">
          <el-input v-model="form.confidence" placeholder="请输入置信度" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" v-if="state === 'add'">
        <el-button type="primary" @click="submitForm" >提 交</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>

      <!-- 添加PEFIM对话框 -->
      <div v-else-if="state === 'detail'">
        <el-descriptions title="基本配置" border>
          <el-descriptions-item label="任务名称">{{form.taskName}}</el-descriptions-item>
          <el-descriptions-item label="任务类型" v-if="form.taskType === 'fim'">频繁项集挖掘</el-descriptions-item>
          <el-descriptions-item label="任务类型" v-else>关联规则挖掘</el-descriptions-item>
          <el-descriptions-item label="数据集名称">{{form.dataFile.fileName}}</el-descriptions-item>
          <el-descriptions-item label="支持度">{{form.support}}</el-descriptions-item>
          <el-descriptions-item label="置信度" v-if="form.taskType === 'rules'">{{form.confidence}}</el-descriptions-item>
          <el-descriptions-item label="备注">{{form.remark}}</el-descriptions-item>
        </el-descriptions>
        <el-divider content-position="center">数据集{{form.dataFile.fileName}}</el-divider>
        <div style="height: 400px">
<!--          <ExcelDisplay ref="exceldisplay" v-if="form.dataFile !== null" :file="form.dataFile"></ExcelDisplay>-->
          <FileDisplay ref="exceldisplay" v-if="form.resultFile !== null" :file="form.dataFile"></FileDisplay>
          <el-empty v-else description="无结果"></el-empty>
        </div>
      </div>

      <!-- PEFIM结果对话框 -->
      <div v-else-if="state === 'result'">
        <el-descriptions title="基本配置" border>
          <el-descriptions-item label="任务名称">{{form.taskName}}</el-descriptions-item>
          <el-descriptions-item label="任务类型" v-if="form.taskType === 'fim'">频繁项集挖掘</el-descriptions-item>
          <el-descriptions-item label="任务类型" v-else>关联规则挖掘</el-descriptions-item>
          <el-descriptions-item label="数据集名称">{{form.dataFile.fileName}}</el-descriptions-item>
          <el-descriptions-item label="支持度">{{form.support}}</el-descriptions-item>
          <el-descriptions-item label="置信度" v-if="form.taskType === 'rules'">{{form.confidence}}</el-descriptions-item>
          <el-descriptions-item label="备注">{{form.remark}}</el-descriptions-item>
        </el-descriptions>
        <el-divider content-position="center">结果集</el-divider>
        <div style="height: 400px">
<!--          <ExcelDisplay ref="exceldisplay" v-if="form.resultFile !== null" :file="form.resultFile"></ExcelDisplay>-->
          <FileDisplay ref="exceldisplay" v-if="form.resultFile !== null" :file="form.resultFile"></FileDisplay>
          <el-empty v-else description="无结果"></el-empty>
        </div>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { listPefim, getPefim, delPefim, addPefim, updatePefim } from "@/api/task/pefim";
import { getToken } from "@/utils/auth";
import ExcelDisplay from "../ExcelDisplay/index"
import FileDisplay from "../FileDisplay/index"

export default {
  name: "Pefim",
  dicts: ['task_status', 'algorithm_type', 'task_type'],
  components:{
    FileDisplay
  },
  data() {
    var supportRule = (rule, value, callback) => {
      console.log(value,"supportRule")
      if (value === '' || value === null || value === undefined) {
        callback(new Error('支持度不能为空！'));
      } else if (value < 0 || value > 1) {
        callback(new Error('支持度只能在0-1之间!'));
      } else {
        callback();
      }
    };
    var confidenceRule = (rule, value, callback) => {
      if(this.form.taskType === 'rules'){
        if (value === '' || value === null || value === undefined) {
          callback(new Error('置信度不能为空！'));
        } else if (value < 0 || value > 1) {
          callback(new Error('置信度只能在0-1之间!'));
        } else {
          callback();
        }
      } else {
        callback();
      }
    };

    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // PEFIM表格数据
      pefimList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      state:"",
      width:"500px",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        algorithmType: "pefim",
        taskType: null,
        support: null,
        confidence: null,
        startTime: null,
        endTime: null,
        taskStatus: null,
        dataFile:null,
      },
      // 表单参数
      form: {
        dataFile:null,
      },
      // 表单校验
      rules: {
        taskName: [
          { required: true, message: "任务类型不能为空", trigger: "blur" }
        ],
        taskType: [
          { required: true, message: "任务类型不能为空", trigger: "blur" }
        ],
        dataFile: [
          { required: true, message: "数据集不能为空", trigger: "blur" }
        ],
        support: [
          { validator: supportRule, trigger: 'blur' }
        ],
        confidence: [
          { validator: confidenceRule, trigger: 'blur' }
        ],
      },
      upload:{
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/task/commonfile/upload"
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    handleSuccess(res){
      if(res.code === 200){
        console.log("handleSuccess");
        console.log(res,"res");
        this.form.dataFile = res.file;
        console.log(this.form.dataFile)
      }
      else{
        this.$refs.upload.clearFiles();
        this.$message.warning(`只能上传.txt .xls .xlsx .csv后缀的文件！`);
      }
    },
    handleRemove() {
      this.$refs.upload.clearFiles();
    },
    handlePreview(file) {
      console.log("handlePreview");
      console.log(file);
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${ file.name }？`);
    },
    /** 查询PEFIM列表 */
    getList() {
      this.loading = true;
      listPefim(this.queryParams).then(response => {
        this.pefimList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      if(this.state === 'add'){
        this.$refs.upload.clearFiles();
      }
      else {
        const component = this.$refs.exceldisplay;
        if (component) {
          component.$destroy();
          console.log('组件已成功销毁');
        } else {
          console.error('未能找到指定的组件');
        }
      }
      this.state = "";
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        taskId: null,
        algorithmType: "pefim",
        taskType: null,
        support: null,
        confidence: null,
        startTime: null,
        endTime: null,
        taskStatus: null,
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        dataFile:null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.taskId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.state = "add";
      this.width = "500px";
      this.title = "创建PEFIM挖掘任务";
    },
    handleDetail(row) {
      this.reset();
      const taskId = row.taskId || this.ids
      getPefim(taskId).then(response => {
        this.form = response.data;
        this.open = true;
        this.state = "detail";
        this.width = "1000px";
        this.title = "查看PEFIM挖掘任务配置";
      });
    },
    handleResult(row) {
      this.reset();
      const taskId = row.taskId || this.ids
      getPefim(taskId).then(response => {
        this.form = response.data;
        this.open = true;
        this.state = "result";
        this.width = "1000px";
        this.title = "查看PEFIM挖掘任务结果";
      });
    },
    handleResultExport(row) {
      this.reset();
      const taskId = row.taskId || this.ids
      getPefim(taskId).then(response => {
        this.form = response.data;
        if(this.form.resultFile !== null){
          window.location.href = this.form.resultFile.url;
        }
        else{
          this.$modal.msgError("无可下载文件！！！");
        }
      });
    },
    handleDataExport(row) {
      this.reset();
      const taskId = row.taskId || this.ids
      getPefim(taskId).then(response => {
        this.form = response.data;
        if(this.form.dataFile !== null){
          window.location.href = this.form.dataFile.url;
        }
        else{
          this.$modal.msgError("无可下载文件！！！");
        }
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const taskId = row.taskId || this.ids
      getPefim(taskId).then(response => {
        this.form = response.data;
        this.open = true;
        this.state = "update";
        this.title = "修改PEFIM挖掘任务配置";
      });
    },
    /** 提交按钮 */
    submitForm() {
      console.log(this.form)
      console.log(this.file)
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.taskId != null) {
            updatePefim(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.$refs.upload.clearFiles();
              this.getList();
            });
          } else {
            addPefim(this.form).then(response => {
              this.$modal.msgSuccess("创建成功");
              this.open = false;
              this.$refs.upload.clearFiles();
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const taskIds = row.taskId || this.ids;
      this.$modal.confirm('是否确认删除PEFIM编号为"' + taskIds + '"的数据项？').then(function() {
        return delPefim(taskIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('task/pefim/export', {
        ...this.queryParams
      }, `pefim_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
