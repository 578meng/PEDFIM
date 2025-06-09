package com.ruoyi.task.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 结果集对象 resultfile
 *
 * @author ytl
 * @date 2024-02-23
 */
public class ResultFile extends CommonFile
{
    @Override
    public String toString() {
        return "*************ResultFile****************\n"
                +super.toString()
                +"*************ResultFile****************\n";
    }
}
