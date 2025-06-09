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
import com.ruoyi.task.domain.DataFile;
import com.ruoyi.task.service.IDataFileService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 数据集Controller
 * 
 * @author ytl
 * @date 2024-02-23
 */
@RestController
@RequestMapping("/task/datafile")
public class DataFileController extends BaseController
{
    @Autowired
    private IDataFileService dataFileService;

    /**
     * 查询数据集列表
     */
    @PreAuthorize("@ss.hasPermi('task:datafile:list')")
    @GetMapping("/list")
    public TableDataInfo list(DataFile dataFile)
    {
        startPage();
        List<DataFile> list = dataFileService.selectDataFileList(dataFile);
        return getDataTable(list);
    }

    /**
     * 导出数据集列表
     */
    @PreAuthorize("@ss.hasPermi('task:datafile:export')")
    @Log(title = "数据集", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DataFile dataFile)
    {
        List<DataFile> list = dataFileService.selectDataFileList(dataFile);
        ExcelUtil<DataFile> util = new ExcelUtil<DataFile>(DataFile.class);
        util.exportExcel(response, list, "数据集数据");
    }

    /**
     * 获取数据集详细信息
     */
    @PreAuthorize("@ss.hasPermi('task:datafile:query')")
    @GetMapping(value = "/{fileId}")
    public AjaxResult getInfo(@PathVariable("fileId") Long fileId)
    {
        return success(dataFileService.selectDataFileByFileId(fileId));
    }

    /**
     * 新增数据集
     */
    @PreAuthorize("@ss.hasPermi('task:datafile:add')")
    @Log(title = "数据集", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DataFile dataFile)
    {
        return toAjax(dataFileService.insertDataFile(dataFile));
    }

    /**
     * 修改数据集
     */
    @PreAuthorize("@ss.hasPermi('task:datafile:edit')")
    @Log(title = "数据集", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DataFile dataFile)
    {
        return toAjax(dataFileService.updateDataFile(dataFile));
    }

    /**
     * 删除数据集
     */
    @PreAuthorize("@ss.hasPermi('task:datafile:remove')")
    @Log(title = "数据集", businessType = BusinessType.DELETE)
	@DeleteMapping("/{fileIds}")
    public AjaxResult remove(@PathVariable Long[] fileIds)
    {
        return toAjax(dataFileService.deleteDataFileByFileIds(fileIds));
    }
}
