package com.example.mapper;


import com.example.entity.Reserve;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作预约Reserve相关数据接口
*/
public interface ReserveMapper {

    /**
     * 新增
     */
    int insert(Reserve reserve);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Reserve reserve);

    /**
     * 根据ID查询
     */
    Reserve selectById(Integer id);

    /**
     * 查询所有
     */
    List<Reserve> selectAll(Reserve reserve);

    @Select("select * from reserve where user_id = #{userId} and teacher_id = #{teacherId}")
    Reserve selectByUserIdAndTeacherId(@Param("userId") Integer userId, @Param("teacherId") Integer teacherId);
}