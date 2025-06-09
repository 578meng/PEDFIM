package com.ruoyi.task.mapper;

import java.util.List;
import com.ruoyi.task.domain.ResultFile;

/**
 * 结果集Mapper接口
 *
 * @author ytl
 * @date 2024-02-23
 */
public interface ResultFileMapper
{
    /**
     * 查询结果集
     *
     * @param fileId 结果集主键
     * @return 结果集
     */
    public ResultFile selectResultFileByFileId(Long fileId);

    /**
     * 查询结果集
     *
     * @param taskId 结果集主键
     * @return 结果集
     */
    public ResultFile selectResultFileByTaskId(Long taskId);

    /**
     * 查询结果集列表
     *
     * @param resultFile 结果集
     * @return 结果集集合
     */
    public List<ResultFile> selectResultFileList(ResultFile resultFile);

    /**
     * 新增结果集
     *
     * @param resultFile 结果集
     * @return 结果
     */
    public int insertResultFile(ResultFile resultFile);

    /**
     * 修改结果集
     *
     * @param resultFile 结果集
     * @return 结果
     */
    public int updateResultFile(ResultFile resultFile);

    /**
     * 删除结果集
     *
     * @param fileId 结果集主键
     * @return 结果
     */
    public int deleteResultFileByFileId(Long fileId);

    /**
     * 批量删除结果集
     *
     * @param fileIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteResultFileByFileIds(Long[] fileIds);
}
