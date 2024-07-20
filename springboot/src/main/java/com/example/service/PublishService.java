package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Publish;
import com.example.mapper.PublishMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 家教发布表业务处理
 **/
@Service
public class PublishService {

    @Resource
    private PublishMapper publishMapper;

    /**
     * 新增
     */
    public void add(Publish publish) {
        publish.setTime(DateUtil.now());
        publishMapper.insert(publish);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        publishMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            publishMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Publish publish) {
        publishMapper.updateById(publish);
    }

    /**
     * 根据ID查询
     */
    public Publish selectById(Integer id) {
        return publishMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Publish> selectAll(Publish publish) {
        return publishMapper.selectAll(publish);
    }

    /**
     * 分页查询
     */
    public PageInfo<Publish> selectPage(Publish publish, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Publish> list = publishMapper.selectAll(publish);
        return PageInfo.of(list);
    }

}