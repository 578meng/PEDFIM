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
import com.ruoyi.task.domain.SPEFIM;
import com.ruoyi.task.service.ISPEFIMService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * SPEFIMController
 *
 * @author ruoyi
 * @date 2024-02-22
 */
@RestController
@RequestMapping("/task/spefim")
public class SPEFIMController extends BaseController
{
    @Autowired
    private ISPEFIMService sPEFIMService;

    /**
     * 查询SPEFIM列表
     */
    @PreAuthorize("@ss.hasPermi('task:spefim:list')")
    @GetMapping("/list")
    public TableDataInfo list(SPEFIM sPEFIM)
    {
        startPage();
        List<SPEFIM> list = sPEFIMService.selectSPEFIMList(sPEFIM);
        return getDataTable(list);
    }

    /**
     * 导出SPEFIM列表
     */
    @PreAuthorize("@ss.hasPermi('task:spefim:export')")
    @Log(title = "SPEFIM", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SPEFIM sPEFIM)
    {
        List<SPEFIM> list = sPEFIMService.selectSPEFIMList(sPEFIM);
        ExcelUtil<SPEFIM> util = new ExcelUtil<SPEFIM>(SPEFIM.class);
        util.exportExcel(response, list, "SPEFIM数据");
    }

    /**
     * 获取SPEFIM详细信息
     */
    @PreAuthorize("@ss.hasPermi('task:spefim:query')")
    @GetMapping(value = "/{taskId}")
    public AjaxResult getInfo(@PathVariable("taskId") Long taskId)
    {
        return success(sPEFIMService.selectSPEFIMByTaskId(taskId));
    }

    /**
     * 新增SPEFIM
     */
    @PreAuthorize("@ss.hasPermi('task:spefim:add')")
    @Log(title = "SPEFIM", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SPEFIM sPEFIM)
    {

        return toAjax(sPEFIMService.insertSPEFIM(sPEFIM));
    }

    /**
     * 修改SPEFIM
     */
    @PreAuthorize("@ss.hasPermi('task:spefim:edit')")
    @Log(title = "SPEFIM", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SPEFIM sPEFIM)
    {
        return toAjax(sPEFIMService.updateSPEFIM(sPEFIM));
    }

    /**
     * 删除SPEFIM
     */
    @PreAuthorize("@ss.hasPermi('task:spefim:remove')")
    @Log(title = "SPEFIM", businessType = BusinessType.DELETE)
	@DeleteMapping("/{taskIds}")
    public AjaxResult remove(@PathVariable Long[] taskIds)
    {
        return toAjax(sPEFIMService.deleteSPEFIMByTaskIds(taskIds));
    }
}
