package com.ruoyi.task.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.task.mapper.DataFileMapper;
import com.ruoyi.task.domain.DataFile;
import com.ruoyi.task.service.IDataFileService;

/**
 * 数据集Service业务层处理
 *
 * @author ytl
 * @date 2024-02-23
 */
@Service
public class DataFileServiceImpl implements IDataFileService
{
    @Autowired
    private DataFileMapper dataFileMapper;

    /**
     * 查询数据集
     *
     * @param fileId 数据集主键
     * @return 数据集
     */
    @Override
    public DataFile selectDataFileByFileId(Long fileId)
    {
        return dataFileMapper.selectDataFileByFileId(fileId);
    }


    /**
     * 查询数据集列表
     *
     * @param dataFile 数据集
     * @return 数据集
     */
    @Override
    public List<DataFile> selectDataFileList(DataFile dataFile)
    {
        return dataFileMapper.selectDataFileList(dataFile);
    }

    /**
     * 新增数据集
     *
     * @param dataFile 数据集
     * @return 结果
     */
    @Override
    public int insertDataFile(DataFile dataFile)
    {
        return dataFileMapper.insertDataFile(dataFile);
    }

    /**
     * 修改数据集
     *
     * @param dataFile 数据集
     * @return 结果
     */
    @Override
    public int updateDataFile(DataFile dataFile)
    {
        return dataFileMapper.updateDataFile(dataFile);
    }

    /**
     * 批量删除数据集
     *
     * @param fileIds 需要删除的数据集主键
     * @return 结果
     */
    @Override
    public int deleteDataFileByFileIds(Long[] fileIds)
    {
        return dataFileMapper.deleteDataFileByFileIds(fileIds);
    }

    /**
     * 删除数据集信息
     *
     * @param fileId 数据集主键
     * @return 结果
     */
    @Override
    public int deleteDataFileByFileId(Long fileId)
    {
        return dataFileMapper.deleteDataFileByFileId(fileId);
    }
}
