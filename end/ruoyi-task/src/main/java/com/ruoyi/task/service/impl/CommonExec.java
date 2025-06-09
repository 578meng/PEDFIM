package com.ruoyi.task.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.task.domain.CommonTask;
import com.ruoyi.task.domain.ResultFile;
import com.ruoyi.task.domain.SPEFIM;
import com.ruoyi.task.mapper.ResultFileMapper;
import com.ruoyi.task.mapper.SPEFIMMapper;
import com.ruoyi.task.service.ICommonExec;
import com.ruoyi.task.utils.PythonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import run.SparkRun;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author：ytl
 * @Package：com.ruoyi.task.service.impl
 * @Project：ruoyi
 * @name：CommonExec
 * @Date：2024/2/23 20:48
 * @Filename：CommonExec
 */
@Service
public class CommonExec implements ICommonExec {

    @Autowired
    private SPEFIMMapper sPEFIMMapper;

    @Autowired
    private ResultFileMapper resultFileMapper;

    @Override
    @Async
    public void execPython(CommonTask info) {
        PythonUtils pythonUtils = new PythonUtils();
        pythonUtils.setPyPath("C:\\Users\\wxhllj\\Desktop\\SPEFIMSYSTEM\\end\\agorithm\\fim.py");
        pythonUtils.setParam(info.getTaskName()
                ,info.getDataFile().getFilePath()
                ,info.getTaskType()
                ,info.getSupport().toString()
                ,info.getConfidence() == null?"0":info.getConfidence().toString()
                ,info.getTaskId().toString()
        );
        try {
            pythonUtils.exec();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("**********************************************");
        System.out.println(pythonUtils.toString());
        System.out.println("**********************************************");

        List<String> response = pythonUtils.getResponse();
        List<String> error = pythonUtils.getError();
        if(error.size() == 0 && response.size() == 5){
            SPEFIM spefim = new SPEFIM();
            ResultFile resultFile = new ResultFile();
            spefim.setTaskId(Long.valueOf(response.get(0)));
            resultFile.setTaskId(Long.valueOf(response.get(0)));
            spefim.setTaskStatus(response.get(1));
            resultFile.setFileName(response.get(2));
            resultFile.setFilePath2(response.get(3));
            spefim.setEndTime(response.get(4));
            sPEFIMMapper.updateSPEFIM(spefim);
            resultFileMapper.insertResultFile(resultFile);
        }
        else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SPEFIM spefim = new SPEFIM();
            spefim.setTaskStatus("fail");
            spefim.setEndTime(sdf.format(DateUtils.getNowDate()));
            spefim.setTaskId(info.getTaskId());
            sPEFIMMapper.updateSPEFIM(spefim);
        }

    }


    @Override
    @Async
    public void execSpark(CommonTask info) {
        SparkRun sparkRun = new SparkRun();
        String[] args = {info.getTaskName()
                ,info.getDataFile().getFilePath()
                ,info.getDataFile().getFileType()
                ,info.getTaskType()
                ,info.getSupport().toString()
                ,info.getConfidence() == null?"0":info.getConfidence().toString()
                ,info.getTaskId().toString()
                ,info.getAlgorithmType()
        };

        SPEFIM spefim = new SPEFIM();
        String respone = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            respone = sparkRun.run(args);
        }catch (RuntimeException e){
            System.out.println(e.toString());
            spefim.setTaskStatus("fail");
            spefim.setEndTime(sdf.format(DateUtils.getNowDate()));
            spefim.setTaskId(info.getTaskId());
            sPEFIMMapper.updateSPEFIM(spefim);
            respone = null;
        }
        if(respone != null){
            ResultFile resultFile = new ResultFile();
            spefim.setTaskId(info.getTaskId());
            resultFile.setTaskId(info.getTaskId());
            spefim.setTaskStatus("success");
            System.out.println(respone);
            String[] res = respone.split(",-,");
            for(int i=0;i<res.length;++i){
                System.out.println(i+"  :  "+res[i]);
            }

            resultFile.setFileName(res[0]);
            resultFile.setFilePath2(res[1]);
            resultFile.setFileType("xlsx");
            spefim.setEndTime(sdf.format(new Date(Long.valueOf(res[2]))));
            sPEFIMMapper.updateSPEFIM(spefim);
            resultFileMapper.insertResultFile(resultFile);
        }


    }
}
