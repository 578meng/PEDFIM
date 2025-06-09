package com.ruoyi.task.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author：ytl
 * @Package：com.ruoyi.task.domain
 * @Project：ruoyi
 * @name：CommonTask
 * @Date：2024/2/23 15:36
 * @Filename：CommonTask
 */
public class CommonTask  extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 任务ID */
    private Long taskId;

    /** 任务名称 */
    @Excel(name = "任务名称")
    private String taskName;

    /** 算法类型 */
    @Excel(name = "算法类型")
    private String algorithmType;

    /** 任务类型 */
    @Excel(name = "任务类型")
    private String taskType;

    /** 支持度 */
    @Excel(name = "支持度")
    private BigDecimal support;

    /** 置信度 */
    @Excel(name = "置信度")
    private BigDecimal confidence;

    /** 提升度 */
    @Excel(name = "提升度")
    private BigDecimal lift;

    /** 开始时间 */
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    @Excel(name = "开始时间", width = 30)
    private String startTime;

    /** 结束时间 */
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    @Excel(name = "结束时间", width = 30)
    private String endTime;

    /** 任务状态 */
    @Excel(name = "任务状态")
    private String taskStatus;

    /** 数据集 */
    private DataFile dataFile;
    /** 结果集 */
    private ResultFile resultFile;

    public ResultFile getResultFile() {
        return resultFile;
    }

    public void setResultFile(ResultFile resultFile) {
        this.resultFile = resultFile;
    }

    public DataFile getDataFile() {
        return dataFile;
    }

    public void setDataFile(DataFile dataFile) {
        this.dataFile = dataFile;
    }

    public void setTaskId(Long taskId)
    {
        this.taskId = taskId;
    }

    public Long getTaskId()
    {
        return taskId;
    }
    public void setAlgorithmType(String algorithmType)
    {
        this.algorithmType = algorithmType;
    }

    public String getAlgorithmType()
    {
        return algorithmType;
    }
    public void setTaskType(String taskType)
    {
        this.taskType = taskType;
    }

    public String getTaskType()
    {
        return taskType;
    }
    public void setSupport(BigDecimal support)
    {
        this.support = support;
    }

    public BigDecimal getSupport()
    {
        return support;
    }
    public void setConfidence(BigDecimal confidence)
    {
        this.confidence = confidence;
    }

    public BigDecimal getConfidence()
    {
        return confidence;
    }
    public void setLift(BigDecimal lift)
    {
        this.lift = lift;
    }

    public BigDecimal getLift()
    {
        return lift;
    }
    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    public String getStartTime()
    {
        return startTime;
    }
    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    public String getEndTime()
    {
        return endTime;
    }
    public void setTaskStatus(String taskStatus)
    {
        this.taskStatus = taskStatus;
    }

    public String getTaskStatus()
    {
        return taskStatus;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public String toString() {
        return "CommonTask{" +
                "taskId=" + taskId +
                ", taskName='" + taskName + '\'' +
                ", algorithmType='" + algorithmType + '\'' +
                ", taskType='" + taskType + '\'' +
                ", support=" + support +
                ", confidence=" + confidence +
                ", lift=" + lift +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", taskStatus='" + taskStatus + '\'' +
                ", dataFile=" + dataFile +
                ", resultFile=" + resultFile +
                '}';
    }
}
