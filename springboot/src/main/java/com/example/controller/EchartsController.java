package com.example.controller;

import com.example.common.Result;
import com.example.entity.Type;
import com.example.service.InfoService;
import com.example.service.TypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Resource
    private TypeService typeService;
    @Resource
    private InfoService infoService;


    @GetMapping("/getPie")
    public Result getPie() {

        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        // 查询出所有家教分类信息
        List<Type> types = typeService.selectAll(new Type());
        //再依次根据typename查询每个类型的老师个数
        for (Type type : types) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", type.getName());
            Integer count = infoService.selectByType(type.getName());
            map.put("value", count);
            list.add(map);
        }


        resultMap.put("text", "平台所有分类家教的教员数量统计");
        resultMap.put("subtext", "统计维度：家教类别");
        resultMap.put("name", "数量");
        resultMap.put("data", list);
        return Result.success(resultMap);
    }
}
