package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.common.enums.StatusEnum;
import com.example.entity.Info;
import com.example.mapper.InfoMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class InfoService {

    @Resource
    private InfoMapper infoMapper;

    /**
     * 新增
     */
    public void add(Info info) {
        // 先查询出表里有没有该教员提交的资料信息
        Info dbInfo = infoMapper.selectByTeacherId(info.getTeacherId());
        if (ObjectUtil.isEmpty(dbInfo)) {
            infoMapper.insert(info);
        } else {
            // 清掉管理员审核时候的几个字段的值
            info.setStar("");
            info.setLevel("");
            info.setReason("");
            infoMapper.updateById(info);
        }
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        infoMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            infoMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Info info) {
        if (ObjectUtil.isNotEmpty(info.getStatus()) && StatusEnum.CHECK_OK.status.equals(info.getStatus())) {
            //加锁，防止code相同
            synchronized (this) {
                // 给该教员一个教员编号
                if (ObjectUtil.isEmpty(info.getCode())) {
                    info.setCode(DateUtil.format(new Date(), "yyyyMMddHHmmss"));
                }
            }
        }
        infoMapper.updateById(info);
    }

    /**
     * 根据ID查询
     */
    public Info selectById(Integer id) {
        return infoMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Info> selectAll(Info info) {
        return infoMapper.selectAll(info);
    }

    /**
     * 分页查询
     */
    public PageInfo<Info> selectPage(Info info, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Info> list = infoMapper.selectAll(info);
        return PageInfo.of(list);
    }

    public Info selectByTeacherId(Integer id) {
        return infoMapper.selectByTeacherId(id);
    }

    public Integer selectByType(String name) {
        return infoMapper.selectByType(name);
    }
}