package com.ruoyi.task.service.impl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.task.domain.DataFile;
import com.ruoyi.task.mapper.DataFileMapper;
import com.ruoyi.task.service.ICommonExec;
import com.ruoyi.task.utils.PythonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.task.mapper.SPEFIMMapper;
import com.ruoyi.task.domain.SPEFIM;
import com.ruoyi.task.service.ISPEFIMService;

/**
 * SPEFIMService业务层处理
 *
 * @author ruoyi
 * @date 2024-02-22
 */
@Service
public class SPEFIMServiceImpl implements ISPEFIMService
{
    @Autowired
    private SPEFIMMapper sPEFIMMapper;

    @Autowired
    private DataFileMapper dataFileMapper;

    @Autowired
    private ICommonExec commonExec;

    /**
     * 查询SPEFIM
     *
     * @param taskId SPEFIM主键
     * @return SPEFIM
     */
    @Override
    public SPEFIM selectSPEFIMByTaskId(Long taskId)
    {
        return sPEFIMMapper.selectSPEFIMByTaskId(taskId);
    }

    /**
     * 查询SPEFIM列表
     *
     * @param sPEFIM SPEFIM
     * @return SPEFIM
     */
    @Override
    public List<SPEFIM> selectSPEFIMList(SPEFIM sPEFIM)
    {
        return sPEFIMMapper.selectSPEFIMList(sPEFIM);
    }

    /**
     * 新增SPEFIM
     *
     * @param sPEFIM SPEFIM
     * @return 结果
     */
    @Override
    public int insertSPEFIM(SPEFIM sPEFIM)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        LoginUser loginUser = SecurityUtils.getLoginUser();
        sPEFIM.setCreateBy(loginUser.getUserId().toString());
        sPEFIM.setCreateTime(DateUtils.getNowDate());
        sPEFIM.setStartTime(sdf.format(DateUtils.getNowDate()));
        sPEFIM.setTaskStatus("running");
        int res = sPEFIMMapper.insertSPEFIM(sPEFIM);
        if(res == 1){
            sPEFIM.setTaskStatus(null);
            List<SPEFIM> list = sPEFIMMapper.selectSPEFIMList(sPEFIM);
            if(list.size() == 1){
                SPEFIM info = list.get(0);
                DataFile file = sPEFIM.getDataFile();
                file.setTaskId(info.getTaskId());
                dataFileMapper.insertDataFile(file);
                // python fim.py 挖掘任务1 /profile/upload/2024/02/23/workdata.xlsx fim 0.00002 0.1 12
                info = sPEFIMMapper.selectSPEFIMByTaskId(info.getTaskId());
                commonExec.execSpark(info);
            }
        }
        return res;
    }

    /**
     * 修改SPEFIM
     *
     * @param sPEFIM SPEFIM
     * @return 结果
     */
    @Override
    public int updateSPEFIM(SPEFIM sPEFIM)
    {
        sPEFIM.setUpdateTime(DateUtils.getNowDate());
        return sPEFIMMapper.updateSPEFIM(sPEFIM);
    }

    /**
     * 批量删除SPEFIM
     *
     * @param taskIds 需要删除的SPEFIM主键
     * @return 结果
     */
    @Override
    public int deleteSPEFIMByTaskIds(Long[] taskIds)
    {
        return sPEFIMMapper.deleteSPEFIMByTaskIds(taskIds);
    }

    /**
     * 删除SPEFIM信息
     *
     * @param taskId SPEFIM主键
     * @return 结果
     */
    @Override
    public int deleteSPEFIMByTaskId(Long taskId)
    {
        return sPEFIMMapper.deleteSPEFIMByTaskId(taskId);
    }
}
