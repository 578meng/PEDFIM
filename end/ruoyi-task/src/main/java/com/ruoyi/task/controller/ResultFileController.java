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
import com.ruoyi.task.domain.ResultFile;
import com.ruoyi.task.service.IResultFileService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 结果集Controller
 * 
 * @author ytl
 * @date 2024-02-23
 */
@RestController
@RequestMapping("/task/resultfile")
public class ResultFileController extends BaseController
{
    @Autowired
    private IResultFileService resultFileService;

    /**
     * 查询结果集列表
     */
    @PreAuthorize("@ss.hasPermi('task:resultfile:list')")
    @GetMapping("/list")
    public TableDataInfo list(ResultFile resultFile)
    {
        startPage();
        List<ResultFile> list = resultFileService.selectResultFileList(resultFile);
        return getDataTable(list);
    }

    /**
     * 导出结果集列表
     */
    @PreAuthorize("@ss.hasPermi('task:resultfile:export')")
    @Log(title = "结果集", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ResultFile resultFile)
    {
        List<ResultFile> list = resultFileService.selectResultFileList(resultFile);
        ExcelUtil<ResultFile> util = new ExcelUtil<ResultFile>(ResultFile.class);
        util.exportExcel(response, list, "结果集数据");
    }

    /**
     * 获取结果集详细信息
     */
    @PreAuthorize("@ss.hasPermi('task:resultfile:query')")
    @GetMapping(value = "/{fileId}")
    public AjaxResult getInfo(@PathVariable("fileId") Long fileId)
    {
        return success(resultFileService.selectResultFileByFileId(fileId));
    }

    /**
     * 新增结果集
     */
    @PreAuthorize("@ss.hasPermi('task:resultfile:add')")
    @Log(title = "结果集", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ResultFile resultFile)
    {
        return toAjax(resultFileService.insertResultFile(resultFile));
    }

    /**
     * 修改结果集
     */
    @PreAuthorize("@ss.hasPermi('task:resultfile:edit')")
    @Log(title = "结果集", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ResultFile resultFile)
    {
        return toAjax(resultFileService.updateResultFile(resultFile));
    }

    /**
     * 删除结果集
     */
    @PreAuthorize("@ss.hasPermi('task:resultfile:remove')")
    @Log(title = "结果集", businessType = BusinessType.DELETE)
	@DeleteMapping("/{fileIds}")
    public AjaxResult remove(@PathVariable Long[] fileIds)
    {
        return toAjax(resultFileService.deleteResultFileByFileIds(fileIds));
    }
}
