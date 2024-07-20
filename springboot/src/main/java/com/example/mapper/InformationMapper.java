package com.example.mapper;

import com.example.entity.Information;

import java.util.List;

public interface InformationMapper {

    /**
     * 新增
     */
    int insert(Information information);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Information information);

    /**
     * 根据ID查询
     */
    Information selectById(Integer id);

    /**
     * 查询所有
     */
    List<Information> selectAll(Information information);

}