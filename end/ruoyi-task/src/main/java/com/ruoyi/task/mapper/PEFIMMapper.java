package com.ruoyi.task.mapper;

import java.util.List;
import com.ruoyi.task.domain.PEFIM;

/**
 * PEFIMMapper接口
 * 
 * @author ytl
 * @date 2024-02-22
 */
public interface PEFIMMapper 
{
    /**
     * 查询PEFIM
     * 
     * @param taskId PEFIM主键
     * @return PEFIM
     */
    public PEFIM selectPEFIMByTaskId(Long taskId);

    /**
     * 查询PEFIM列表
     * 
     * @param pEFIM PEFIM
     * @return PEFIM集合
     */
    public List<PEFIM> selectPEFIMList(PEFIM pEFIM);

    /**
     * 新增PEFIM
     * 
     * @param pEFIM PEFIM
     * @return 结果
     */
    public int insertPEFIM(PEFIM pEFIM);

    /**
     * 修改PEFIM
     * 
     * @param pEFIM PEFIM
     * @return 结果
     */
    public int updatePEFIM(PEFIM pEFIM);

    /**
     * 删除PEFIM
     * 
     * @param taskId PEFIM主键
     * @return 结果
     */
    public int deletePEFIMByTaskId(Long taskId);

    /**
     * 批量删除PEFIM
     * 
     * @param taskIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePEFIMByTaskIds(Long[] taskIds);
}
