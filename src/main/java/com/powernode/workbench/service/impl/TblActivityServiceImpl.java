package com.powernode.workbench.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.powernode.Exception.ResultException;
import com.powernode.settings.mapper.TblUserMapper;
import com.powernode.settings.pojo.TblUser;
import com.powernode.settings.pojo.TblUserExample;
import com.powernode.util.DateTimeUtil;
import com.powernode.util.MD5Util;
import com.powernode.util.PageResult;
import com.powernode.util.UUIDUtil;
import com.powernode.workbench.mapper.TblActivityMapper;
import com.powernode.workbench.mapper.TblActivityRemarkMapper;
import com.powernode.workbench.pojo.TblActivity;
import com.powernode.workbench.pojo.TblActivityExample;
import com.powernode.workbench.pojo.TblActivityRemarkExample;
import com.powernode.workbench.service.TblActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class TblActivityServiceImpl implements TblActivityService {
    @Autowired
    TblUserMapper tblUserMapper;

    @Autowired
    TblActivityMapper tblActivityMapper;

    @Autowired
    TblActivityRemarkMapper tblActivityRemarkMapper;


    @Override
    public List<TblUser> getByList() {
        List<TblUser> tblUsers = tblUserMapper.selectByExample(null);
        if (tblUsers == null || tblUsers.size() == 0) {
            throw new ResultException("未找到用户");
        }

        return tblUsers;
    }

    @Override
    public void add(TblActivity tblActivity, String name) {
        //补全属性
        String uuid = UUIDUtil.getUUID();
        tblActivity.setId(uuid);
        tblActivity.setCreateby(name);
        tblActivity.setCreatetime(DateTimeUtil.getSysTime());
        try {
            tblActivityMapper.insert(tblActivity);

        } catch (Exception e) {
            throw new ResultException("保存数据失败！");
        }
    }

    //分页查询
    @Override
    public PageResult selectPageList(int pageSize, int pageNo, String name, String owner, String startdate, String enddate) {
        /*Pagehelper.startPage(pageNo,pageSize);*/



        TblActivityExample tblActivityExample = new TblActivityExample();
        TblActivityExample.Criteria criteria = tblActivityExample.createCriteria();

        if (name != null && !"".equals(name)) {

            criteria.andNameLike("%" + name + "%");
        }

        List list = new ArrayList();
        if (owner != null && !"".equals(owner)) {
            //创建user离线检索对象
            TblUserExample tblUserExample = new TblUserExample();
            TblUserExample.Criteria criteria1 = tblUserExample.createCriteria();

            //根据传过来的owner查询user表
            criteria1.andNameLike("%" + owner + "%");

            //将查询出来的所有user的id属性循环放入集合

            List<TblUser> tblUsers = tblUserMapper.selectByExample(tblUserExample);
            for (TblUser tblUser : tblUsers) {
                list.add(tblUser.getId());
            }
            //activity根据所有user的id查询
            criteria.andOwnerIn(list);

        }

        //开启分页
        PageHelper.startPage(pageNo, pageSize);

        if (startdate != null && !"".equals(startdate)) {
            criteria.andStartdateLike("%" + startdate + "%");
        }
        if (enddate != null && !"".equals(enddate)) {
            criteria.andEnddateLike("%" + enddate + "%");
        }

        /*查询所有*/
        List<TblActivity> tblActivities = tblActivityMapper.selectByExample(tblActivityExample);
        if (tblActivities == null || tblActivities.size() == 0) {
            throw new ResultException("未找到数据");
        }

        /*根据owner查询user，拿到user对象的name属性给owner赋值*/
        for (TblActivity tblActivity : tblActivities) {
            TblUser tblUser = tblUserMapper.selectByPrimaryKey(tblActivity.getOwner());
            tblActivity.setOwner(tblUser.getName());
        }

        //封装list到PageInfo对象中自动分页
        PageInfo<TblActivity> pageInfo = new PageInfo<>(tblActivities);
        PageResult pageResult = new PageResult(pageInfo.getTotal(), pageInfo.getList());
        return pageResult;
    }

    //删除
    @Override
    public void delItem(String[] ids) {
        if (ids != null && ids.length != 0) {
            try {
                for (String id : ids) {
                    //创建tblActivityRemarkExample的离线检索对象
                    TblActivityRemarkExample tblActivityRemarkExample = new TblActivityRemarkExample();
                    TblActivityRemarkExample.Criteria criteria = tblActivityRemarkExample.createCriteria();
                    criteria.andActivityidEqualTo(id);

                    //根据activity表的ID，先删除Remark备注表数据
                    tblActivityRemarkMapper.deleteByExample(tblActivityRemarkExample);

                    //再删除activity表中的数据
                    tblActivityMapper.deleteByPrimaryKey(id);
                }
            } catch (Exception e) {
                throw new ResultException("删除数据失败");
            }
        }
    }

    //根据市场活动ID查询
    @Override
    public TblActivity getById(String id) {
        TblActivity tblActivity = tblActivityMapper.selectByPrimaryKey(id);

        if (tblActivity == null) {
            throw new ResultException("未找到数据");
        }
        return tblActivity;
    }


    //修改市场活动信息
    @Override
    public TblActivity updateMessage(TblActivity tblActivity) {
        //获取系统当前时间
        String sysTime = DateTimeUtil.getSysTime();

        //把时间赋值给修改时间
        tblActivity.setEdittime(sysTime);

        try {
            /*//根据activity表的owner找到所有user表的对应的数据
            TblUser tblUser = tblUserMapper.selectByPrimaryKey(tblActivity.getOwner());
            if (tblUser == null) {
                throw new ResultException("未找到信息");
            }

            //把user的name值赋值给activity的owner
            tblActivity.setOwner(tblUser.getName());*/


            tblActivityMapper.updateByPrimaryKeySelective(tblActivity);

        } catch (Exception e) {
            throw new ResultException("更新失败");
        }
        return tblActivity;
    }

    @Override
    public TblActivity getItem(String id) {

        //根据id查询所有activity数据
        TblActivity tblActivity = tblActivityMapper.selectByPrimaryKey(id);
        if (tblActivity == null) {
            throw new ResultException("未找到信息");
        }

        //根据activity表的owner找到所有user表的对应的数据
        TblUser tblUser = tblUserMapper.selectByPrimaryKey(tblActivity.getOwner());
        if (tblUser == null) {
            throw new ResultException("未找到信息");
        }

        //把user的name值赋值给activity的owner
        tblActivity.setOwner(tblUser.getName());


        return tblActivity;
    }

    //详情页修改1
    @Override
    public TblActivity updateMessage2(TblActivity tblActivity) {
        //获取系统当前时间
        String sysTime = DateTimeUtil.getSysTime();
        String id = tblActivity.getOwner();
        TblUser tblUser = tblUserMapper.selectByPrimaryKey(id);
        String name = tblUser.getName();

        //把时间赋值给修改时间
        tblActivity.setEdittime(sysTime);

        try {
          /*  //根据activity表的owner找到所有user表的对应的数据
            TblUser tblUser = tblUserMapper.selectByPrimaryKey(tblActivity.getOwner());
            if (tblUser == null) {
                throw new ResultException("未找到信息");
            }

            //把user的name值赋值给activity的owner
            tblActivity.setOwner(tblUser.getName());*/

           /* TblUser tblUser = tblUserMapper.selectByPrimaryKey(tblActivity.getOwner());
            String name = tblUser.getName();*/


            tblActivityMapper.updateByPrimaryKeySelective(tblActivity);

        } catch (Exception e) {
            throw new ResultException("更新失败");
        }

        //在更新数据库之后再设置owner的值，就不会出现连带数据库一起设置的BUG了
        tblActivity.setOwner(name);
        return tblActivity;
    }
}
