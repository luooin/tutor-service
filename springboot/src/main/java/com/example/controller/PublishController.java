package com.example.controller;

import com.example.common.Result;
import com.example.entity.Publish;
import com.example.service.PublishService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 家教发布表前端操作接口
 **/
@RestController
@RequestMapping("/publish")
public class PublishController {

    @Resource
    private PublishService publishService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Publish publish) {
        publishService.add(publish);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        publishService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        publishService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Publish publish) {
        publishService.updateById(publish);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Publish publish = publishService.selectById(id);
        return Result.success(publish);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Publish publish ) {
        List<Publish> list = publishService.selectAll(publish);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Publish publish,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Publish> page = publishService.selectPage(publish, pageNum, pageSize);
        return Result.success(page);
    }

}