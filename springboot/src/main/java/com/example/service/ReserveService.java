package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.common.enums.StatusEnum;
import com.example.entity.Account;
import com.example.entity.Reserve;
import com.example.exception.CustomException;
import com.example.mapper.ReserveMapper;
import com.example.mapper.TeacherMapper;
import com.example.utils.MailClient;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import java.util.List;

/**
 * 预约业务处理
 **/
@Service
public class ReserveService {
    @Resource
    private ReserveMapper reserveMapper;

    @Autowired
    private MailClient mailClient;

    @Resource
    private TeacherMapper teacherMapper;

    /**
     * 新增
     */
    public void add(Reserve reserve) {
        // 判断该家长有没有预约过该教员
        Reserve dbReserve = reserveMapper.selectByUserIdAndTeacherId(reserve.getUserId(), reserve.getTeacherId());
        if (ObjectUtil.isEmpty(dbReserve)) {
            reserveMapper.insert(reserve);
            String to = teacherMapper.selectEmailById(reserve.getTeacherId());
            System.out.println(to);
            mailClient.sendMail(to, "新的预约", "你好！教员，您有新的家教预约，请注意查收");
            return;
        }
        // 已拒绝，可以重复提交，更新一下原来的记录
        if (StatusEnum.RESERVE_NO.status.equals(dbReserve.getStatus())) {
            reserve.setId(dbReserve.getId());
            reserveMapper.updateById(reserve);
            return;
        }
        // 待确认或者已接受状态，不允许再重复提交了
        if (StatusEnum.RESERVING.status.equals(dbReserve.getStatus()) || StatusEnum.RESERVE_OK.status.equals(dbReserve.getStatus())) {
            throw new CustomException(ResultCodeEnum.RESERVE_AGIN_ERROR);
        }

    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        reserveMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            reserveMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Reserve reserve) {
        reserveMapper.updateById(reserve);
    }

    /**
     * 根据ID查询
     */
    public Reserve selectById(Integer id) {
        return reserveMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Reserve> selectAll(Reserve reserve) {
        return reserveMapper.selectAll(reserve);
    }

    /**
     * 分页查询,根据不同的角色查询预约记录，家长只能看自己的，教师可以看所有自己的预约记录
     */
    public PageInfo<Reserve> selectPage(Reserve reserve, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        //判断是家长还是教员，根据家长id或者教员id查看记录
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            reserve.setUserId(currentUser.getId());
        }
        if (RoleEnum.TEACHER.name().equals(currentUser.getRole())) {
            reserve.setTeacherId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Reserve> list = reserveMapper.selectAll(reserve);
        return PageInfo.of(list);
    }

}