package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Message;
import com.example.entity.Teacher;
import com.example.entity.User;
import com.example.mapper.MessageMapper;
import com.example.mapper.TeacherMapper;
import com.example.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 留言建议表业务处理
 **/
@Service
public class MessageService {

    @Resource
    private MessageMapper messageMapper;
    @Resource
    private TeacherMapper teacherMapper;
    @Resource
    private UserMapper userMapper;

    /**
     * 新增
     */
    public void add(Message message) {
        message.setTime(DateUtil.today());
        messageMapper.insert(message);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        messageMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            messageMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Message message) {
        messageMapper.updateById(message);
    }

    /**
     * 根据ID查询
     */
    public Message selectById(Integer id) {
        return messageMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Message> selectAll(Message message) {
        List<Message> messages = messageMapper.selectAll(message);
        for (Message dbMessage : messages) {
            extracted(dbMessage);
        }
        return messages;
    }

    /**
     * 分页查询
     */
    public PageInfo<Message> selectPage(Message message, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Message> list = messageMapper.selectAll(message);
        for (Message dbMessage : list) {
            extracted(dbMessage);
        }
        return PageInfo.of(list);
    }

    private void extracted(Message dbMessage) {
        if (RoleEnum.TEACHER.name().equals(dbMessage.getRole())) {
            Teacher teacher = teacherMapper.selectById(dbMessage.getUserId());
            if (ObjectUtil.isNotEmpty(teacher)) {
                dbMessage.setUserName(teacher.getName());
                dbMessage.setUserAvatar(teacher.getAvatar());
            }
        }
        //如果是家长角色，就从家长表里面查询用户名，头像等信息
        if (RoleEnum.USER.name().equals(dbMessage.getRole())) {
            User user = userMapper.selectById(dbMessage.getUserId());
            if (ObjectUtil.isNotEmpty(user)) {
                dbMessage.setUserName(user.getName());
                dbMessage.setUserAvatar(user.getAvatar());
            }
        }
    }

}