package com.ruoyi.task.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.task.mapper.ResultFileMapper;
import com.ruoyi.task.domain.ResultFile;
import com.ruoyi.task.service.IResultFileService;

/**
 * 结果集Service业务层处理
 * 
 * @author ytl
 * @date 2024-02-23
 */
@Service
public class ResultFileServiceImpl implements IResultFileService 
{
    @Autowired
    private ResultFileMapper resultFileMapper;

    /**
     * 查询结果集
     * 
     * @param fileId 结果集主键
     * @return 结果集
     */
    @Override
    public ResultFile selectResultFileByFileId(Long fileId)
    {
        return resultFileMapper.selectResultFileByFileId(fileId);
    }

    /**
     * 查询结果集列表
     * 
     * @param resultFile 结果集
     * @return 结果集
     */
    @Override
    public List<ResultFile> selectResultFileList(ResultFile resultFile)
    {
        return resultFileMapper.selectResultFileList(resultFile);
    }

    /**
     * 新增结果集
     * 
     * @param resultFile 结果集
     * @return 结果
     */
    @Override
    public int insertResultFile(ResultFile resultFile)
    {
        return resultFileMapper.insertResultFile(resultFile);
    }

    /**
     * 修改结果集
     * 
     * @param resultFile 结果集
     * @return 结果
     */
    @Override
    public int updateResultFile(ResultFile resultFile)
    {
        return resultFileMapper.updateResultFile(resultFile);
    }

    /**
     * 批量删除结果集
     * 
     * @param fileIds 需要删除的结果集主键
     * @return 结果
     */
    @Override
    public int deleteResultFileByFileIds(Long[] fileIds)
    {
        return resultFileMapper.deleteResultFileByFileIds(fileIds);
    }

    /**
     * 删除结果集信息
     * 
     * @param fileId 结果集主键
     * @return 结果
     */
    @Override
    public int deleteResultFileByFileId(Long fileId)
    {
        return resultFileMapper.deleteResultFileByFileId(fileId);
    }
}
