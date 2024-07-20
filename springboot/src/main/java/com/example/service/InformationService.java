package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Information;
import com.example.mapper.InformationMapper;
import com.example.utils.RedisKeyUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class InformationService {

    @Resource
    private InformationMapper informationMapper;


    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增
     */
    public void add(Information information) {
        information.setTime(DateUtil.now());
        informationMapper.insert(information);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        informationMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            informationMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Information information) {
        informationMapper.updateById(information);
    }

    /**
     * 根据ID查询
     */
    public Information selectById(Integer id) {
        Information information = informationMapper.selectById(id);
        int count = 0;
        int find = findInfoReadCount(id);
        if(find != 0){
            count = find + 1;
        }else {
            count = information.getCount()+1;
        }
        setInfoReadCount(id,count);
        return information;
    }
    public int findInfoReadCount(int id){
        String readCountKey = RedisKeyUtil.getReadCountKey(id);

        Integer count = (Integer) redisTemplate.opsForValue().get(readCountKey);

        return count == null ? 0:count.intValue();
    }

    public void setInfoReadCount(int id,int count){
        String readCountKey = RedisKeyUtil.getReadCountKey(id);
        redisTemplate.opsForValue().set(readCountKey, count);
    }


//information.setCount(count);
    /**
     * 查询所有
     */
    public List<Information> selectAll(Information information) {
        return informationMapper.selectAll(information);
    }

    /**
     * 分页查询
     */
    public PageInfo<Information> selectPage(Information information, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Information> list = informationMapper.selectAll(information);
        return PageInfo.of(list);
    }





}