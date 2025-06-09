package com.ruoyi.task.mapper;

import java.util.List;
import com.ruoyi.task.domain.SPEFIM;

/**
 * SPEFIMMapper接口
 * 
 * @author ruoyi
 * @date 2024-02-22
 */
public interface SPEFIMMapper 
{
    /**
     * 查询SPEFIM
     * 
     * @param taskId SPEFIM主键
     * @return SPEFIM
     */
    public SPEFIM selectSPEFIMByTaskId(Long taskId);

    /**
     * 查询SPEFIM列表
     * 
     * @param sPEFIM SPEFIM
     * @return SPEFIM集合
     */
    public List<SPEFIM> selectSPEFIMList(SPEFIM sPEFIM);

    /**
     * 新增SPEFIM
     * 
     * @param sPEFIM SPEFIM
     * @return 结果
     */
    public int insertSPEFIM(SPEFIM sPEFIM);

    /**
     * 修改SPEFIM
     * 
     * @param sPEFIM SPEFIM
     * @return 结果
     */
    public int updateSPEFIM(SPEFIM sPEFIM);

    /**
     * 删除SPEFIM
     * 
     * @param taskId SPEFIM主键
     * @return 结果
     */
    public int deleteSPEFIMByTaskId(Long taskId);

    /**
     * 批量删除SPEFIM
     * 
     * @param taskIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSPEFIMByTaskIds(Long[] taskIds);
}
