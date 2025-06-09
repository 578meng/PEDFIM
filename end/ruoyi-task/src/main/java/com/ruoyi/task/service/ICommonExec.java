package com.ruoyi.task.service;

import com.ruoyi.task.domain.CommonTask;
import org.springframework.scheduling.annotation.Async;

/**
 * @Author：ytl
 * @Package：com.ruoyi.task.service
 * @Project：ruoyi
 * @name：CommonExec
 * @Date：2024/2/23 20:47
 * @Filename：CommonExec
 */
public interface ICommonExec {

    public void execPython(CommonTask commonTask);

    public void execSpark(CommonTask commonTask);
}
