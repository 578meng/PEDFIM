package com.ruoyi.task.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.framework.config.ServerConfig;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * @Author：ytl
 * @Package：com.ruoyi.task.domain
 * @Project：ruoyi
 * @name：CommonFile
 * @Date：2024/2/23 13:09
 * @Filename：CommonFile
 */
public class CommonFile  extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 文件ID */
    private Long fileId;

    /** 文件名 */
    @Excel(name = "文件名")
    private String fileName;

    /** 文件类型 */
    @Excel(name = "文件类型")
    private String fileType;

    /** 文件大小 */
    @Excel(name = "文件大小")
    private BigDecimal fileSize;

    /** 文件路径 */
    @Excel(name = "文件路径")
    private String filePath;

    /** 任务ID */
    @Excel(name = "任务ID")
    private Long taskId;

    private String url;

    public void setFileId(Long fileId)
    {
        this.fileId = fileId;
    }

    public Long getFileId()
    {
        return fileId;
    }
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getFileName()
    {
        return fileName;
    }
    public void setFileType(String fileType)
    {
        this.fileType = fileType;
    }

    public String getFileType()
    {
        return fileType;
    }
    public void setFileSize(BigDecimal fileSize)
    {
        this.fileSize = fileSize;
    }

    public BigDecimal getFileSize()
    {
        return fileSize;
    }
    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
        ServerConfig serverConfig = new ServerConfig();
        this.url = serverConfig.getUrl() + this.filePath;
    }
    public void setFilePath2(String filePath)
    {
        this.filePath = filePath;
    }

    public String getFilePath()
    {
        return filePath;
    }
    public void setTaskId(Long taskId)
    {
        this.taskId = taskId;
    }

    public Long getTaskId()
    {
        return taskId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "CommonFile{" +
                "fileId=" + fileId +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileSize=" + fileSize +
                ", filePath='" + filePath + '\'' +
                ", taskId=" + taskId +
                ", url='" + url + '\'' +
                '}';
    }
}
