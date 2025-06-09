package com.ruoyi.task.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 数据集对象 datafile
 *
 * @author ytl
 * @date 2024-02-23
 */
public class DataFile extends CommonFile
{
    @Override
    public String toString() {
        return "*************DataFile****************\n"
                +super.toString()
                +"*************DataFile****************\n";
    }
}
