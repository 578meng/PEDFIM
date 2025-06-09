package com.ruoyi.task.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.task.domain.DataFile;
import com.ruoyi.task.domain.SPEFIM;
import com.ruoyi.task.mapper.DataFileMapper;
import com.ruoyi.task.service.ICommonExec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.task.mapper.PEFIMMapper;
import com.ruoyi.task.domain.PEFIM;
import com.ruoyi.task.service.IPEFIMService;

/**
 * PEFIMService业务层处理
 *
 * @author ytl
 * @date 2024-02-22
 */
@Service
public class PEFIMServiceImpl implements IPEFIMService
{
    @Autowired
    private PEFIMMapper pEFIMMapper;

    @Autowired
    private DataFileMapper dataFileMapper;

    @Autowired
    private ICommonExec commonExec;

    /**
     * 查询PEFIM
     *
     * @param taskId PEFIM主键
     * @return PEFIM
     */
    @Override
    public PEFIM selectPEFIMByTaskId(Long taskId)
    {
        return pEFIMMapper.selectPEFIMByTaskId(taskId);
    }

    /**
     * 查询PEFIM列表
     *
     * @param pEFIM PEFIM
     * @return PEFIM
     */
    @Override
    public List<PEFIM> selectPEFIMList(PEFIM pEFIM)
    {
        return pEFIMMapper.selectPEFIMList(pEFIM);
    }

    /**
     * 新增PEFIM
     *
     * @param pEFIM PEFIM
     * @return 结果
     */
    @Override
    public int insertPEFIM(PEFIM pEFIM)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        LoginUser loginUser = SecurityUtils.getLoginUser();
        pEFIM.setCreateBy(loginUser.getUserId().toString());
        pEFIM.setCreateTime(DateUtils.getNowDate());
        pEFIM.setStartTime(sdf.format(DateUtils.getNowDate()));
        pEFIM.setTaskStatus("running");
        int res = pEFIMMapper.insertPEFIM(pEFIM);
        if(res == 1){
            pEFIM.setTaskStatus(null);
            List<PEFIM> list = pEFIMMapper.selectPEFIMList(pEFIM);
            if(list.size() == 1){
                PEFIM info = list.get(0);
                DataFile file = pEFIM.getDataFile();
                file.setTaskId(info.getTaskId());
                dataFileMapper.insertDataFile(file);
                // python fim.py 挖掘任务1 /profile/upload/2024/02/23/workdata.xlsx fim 0.00002 0.1 12
                info = pEFIMMapper.selectPEFIMByTaskId(info.getTaskId());
                commonExec.execSpark(info);
            }
        }
        return res;
    }

    /**
     * 修改PEFIM
     *
     * @param pEFIM PEFIM
     * @return 结果
     */
    @Override
    public int updatePEFIM(PEFIM pEFIM)
    {
        pEFIM.setUpdateTime(DateUtils.getNowDate());
        return pEFIMMapper.updatePEFIM(pEFIM);
    }

    /**
     * 批量删除PEFIM
     *
     * @param taskIds 需要删除的PEFIM主键
     * @return 结果
     */
    @Override
    public int deletePEFIMByTaskIds(Long[] taskIds)
    {
        return pEFIMMapper.deletePEFIMByTaskIds(taskIds);
    }

    /**
     * 删除PEFIM信息
     *
     * @param taskId PEFIM主键
     * @return 结果
     */
    @Override
    public int deletePEFIMByTaskId(Long taskId)
    {
        return pEFIMMapper.deletePEFIMByTaskId(taskId);
    }
}
