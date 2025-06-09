package com.ruoyi.task.service;

import java.util.List;
import com.ruoyi.task.domain.DataFile;

/**
 * 数据集Service接口
 * 
 * @author ytl
 * @date 2024-02-23
 */
public interface IDataFileService 
{
    /**
     * 查询数据集
     * 
     * @param fileId 数据集主键
     * @return 数据集
     */
    public DataFile selectDataFileByFileId(Long fileId);

    /**
     * 查询数据集列表
     * 
     * @param dataFile 数据集
     * @return 数据集集合
     */
    public List<DataFile> selectDataFileList(DataFile dataFile);

    /**
     * 新增数据集
     * 
     * @param dataFile 数据集
     * @return 结果
     */
    public int insertDataFile(DataFile dataFile);

    /**
     * 修改数据集
     * 
     * @param dataFile 数据集
     * @return 结果
     */
    public int updateDataFile(DataFile dataFile);

    /**
     * 批量删除数据集
     * 
     * @param fileIds 需要删除的数据集主键集合
     * @return 结果
     */
    public int deleteDataFileByFileIds(Long[] fileIds);

    /**
     * 删除数据集信息
     * 
     * @param fileId 数据集主键
     * @return 结果
     */
    public int deleteDataFileByFileId(Long fileId);
}
