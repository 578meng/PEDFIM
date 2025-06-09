package com.ruoyi.task.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.task.domain.PEFIM;
import com.ruoyi.task.service.IPEFIMService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * PEFIMController
 * 
 * @author ytl
 * @date 2024-02-22
 */
@RestController
@RequestMapping("/task/pefim")
public class PEFIMController extends BaseController
{
    @Autowired
    private IPEFIMService pEFIMService;

    /**
     * 查询PEFIM列表
     */
    @PreAuthorize("@ss.hasPermi('task:pefim:list')")
    @GetMapping("/list")
    public TableDataInfo list(PEFIM pEFIM)
    {
        startPage();
        List<PEFIM> list = pEFIMService.selectPEFIMList(pEFIM);
        return getDataTable(list);
    }

    /**
     * 导出PEFIM列表
     */
    @PreAuthorize("@ss.hasPermi('task:pefim:export')")
    @Log(title = "PEFIM", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PEFIM pEFIM)
    {
        List<PEFIM> list = pEFIMService.selectPEFIMList(pEFIM);
        ExcelUtil<PEFIM> util = new ExcelUtil<PEFIM>(PEFIM.class);
        util.exportExcel(response, list, "PEFIM数据");
    }

    /**
     * 获取PEFIM详细信息
     */
    @PreAuthorize("@ss.hasPermi('task:pefim:query')")
    @GetMapping(value = "/{taskId}")
    public AjaxResult getInfo(@PathVariable("taskId") Long taskId)
    {
        return success(pEFIMService.selectPEFIMByTaskId(taskId));
    }

    /**
     * 新增PEFIM
     */
    @PreAuthorize("@ss.hasPermi('task:pefim:add')")
    @Log(title = "PEFIM", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PEFIM pEFIM)
    {
        return toAjax(pEFIMService.insertPEFIM(pEFIM));
    }

    /**
     * 修改PEFIM
     */
    @PreAuthorize("@ss.hasPermi('task:pefim:edit')")
    @Log(title = "PEFIM", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PEFIM pEFIM)
    {
        return toAjax(pEFIMService.updatePEFIM(pEFIM));
    }

    /**
     * 删除PEFIM
     */
    @PreAuthorize("@ss.hasPermi('task:pefim:remove')")
    @Log(title = "PEFIM", businessType = BusinessType.DELETE)
	@DeleteMapping("/{taskIds}")
    public AjaxResult remove(@PathVariable Long[] taskIds)
    {
        return toAjax(pEFIMService.deletePEFIMByTaskIds(taskIds));
    }
}
