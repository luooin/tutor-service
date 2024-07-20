package com.example.controller;

import com.example.common.Result;
import com.example.entity.Info;
import com.example.service.InfoService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/info")
public class InfoController {

    @Resource
    private InfoService infoService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Info info) {
        infoService.add(info);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        infoService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        infoService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Info info) {
        infoService.updateById(info);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Info info = infoService.selectById(id);
        return Result.success(info);
    }

    @GetMapping("/selectByTeacherId/{id}")
    public Result selectByTeacherId(@PathVariable Integer id) {
        Info info = infoService.selectByTeacherId(id);
        return Result.success(info);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Info info ) {
        System.out.println(info.getTypeName());

        List<Info> list = infoService.selectAll(info);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Info info,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Info> page = infoService.selectPage(info, pageNum, pageSize);
        return Result.success(page);
    }

}