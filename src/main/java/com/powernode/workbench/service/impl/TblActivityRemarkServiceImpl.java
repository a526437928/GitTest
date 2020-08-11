package com.powernode.workbench.service.impl;

import com.powernode.Exception.ResultException;
import com.powernode.util.DateTimeUtil;
import com.powernode.util.UUIDUtil;
import com.powernode.workbench.mapper.TblActivityRemarkMapper;
import com.powernode.workbench.pojo.TblActivityRemark;
import com.powernode.workbench.pojo.TblActivityRemarkExample;
import com.powernode.workbench.service.TblActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class TblActivityRemarkServiceImpl implements TblActivityRemarkService {

    @Autowired
    TblActivityRemarkMapper tblActivityRemarkMapper;

    //创建备注对象
    @Override
    public TblActivityRemark add(String acId, String noteContent, String createBy) {
        String uuid = UUIDUtil.getUUID();
        TblActivityRemark tblActivityRemark = new TblActivityRemark();
        tblActivityRemark.setId(uuid);
        tblActivityRemark.setActivityid(acId);
        tblActivityRemark.setCreateby(createBy);
        tblActivityRemark.setNotecontent(noteContent);
        tblActivityRemark.setCreatetime(DateTimeUtil.getSysTime());

        //0表示未修改，1表示已修改
        tblActivityRemark.setEditflag("0");
        tblActivityRemark.setNotecontent(noteContent);

        try {
            tblActivityRemarkMapper.insert(tblActivityRemark);
        } catch (Exception e) {
            throw new RuntimeException("添加数据失败");
        }

        //根据uuid查询
        TblActivityRemark tblActivityRemark1 = tblActivityRemarkMapper.selectByPrimaryKey(uuid);
        if (tblActivityRemark1 == null) {
            throw new RuntimeException("未查询到数据");
        }

        return tblActivityRemark1;
    }

    @Override
    public List getRemarks(String id) {
        TblActivityRemarkExample tblActivityRemarkExample = new TblActivityRemarkExample();
        TblActivityRemarkExample.Criteria criteria = tblActivityRemarkExample.createCriteria();
        criteria.andActivityidEqualTo(id);
        List<TblActivityRemark> tblActivityRemarks = tblActivityRemarkMapper.selectByExample(tblActivityRemarkExample);
        if (tblActivityRemarks == null || tblActivityRemarks.size() == 0) {
            throw new ResultException("未找到数据");
        }
        return tblActivityRemarks;
    }

    @Override
    public void delRemarks(String id) {
        try {
            tblActivityRemarkMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            throw new ResultException("删除失败");
        }
    }

    @Override
    public TblActivityRemark editRemark(String id) {
        TblActivityRemark tblActivityRemark = tblActivityRemarkMapper.selectByPrimaryKey(id);
        if (tblActivityRemark == null) {
            throw new ResultException("未找到数据");
        }
        return tblActivityRemark;
    }

    @Override
    public TblActivityRemark updateRemarkMessage(TblActivityRemark tblActivityRemark) {

        //获取系统当前时间作为修改时间
        String sysTime = DateTimeUtil.getSysTime();
        tblActivityRemark.setEdittime(sysTime);

        //把修改状态改为1
        tblActivityRemark.setEditflag("1");

        try {
            tblActivityRemarkMapper.updateByPrimaryKeySelective(tblActivityRemark);

        } catch (Exception e) {
            throw new ResultException("更新失败");
        }

        //根据传进来的ID查询对象
        TblActivityRemark tblActivityRemark1 = tblActivityRemarkMapper.selectByPrimaryKey(tblActivityRemark.getId());
        if (tblActivityRemark1 == null) {
            throw new ResultException("未找到数据");
        }

        return tblActivityRemark1;

    }
}
