package com.powernode.workbench.controller;

import com.powernode.Exception.ResultException;
import com.powernode.settings.pojo.TblUser;
import com.powernode.util.DateTimeUtil;
import com.powernode.util.Result;
import com.powernode.workbench.pojo.TblActivityRemark;
import com.powernode.workbench.service.TblActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("remark")
public class TblActivityRemarkController {

    @Autowired
    private TblActivityRemarkService tblActivityRemarkService;

    @Value("${session}")
    public String SESSION_USER;

    @RequestMapping("/add")
    public Result add(String id, String content, HttpServletRequest request) {
        //获取登录人
        TblUser tblUser = (TblUser) request.getSession().getAttribute(SESSION_USER);
        String LoginName = tblUser.getName();

        try {
            TblActivityRemark tblActivityRemark = tblActivityRemarkService.add(id, content, LoginName);
            return Result.OK(tblActivityRemark);
        } catch (ResultException e) {
            return Result.build(1005, e);
        }

    }

    //展示备注信息
    @RequestMapping("/getRemarks")
    public Result getRemarks(String id) {
        try {
            List remarksList = tblActivityRemarkService.getRemarks(id);
            return Result.OK(remarksList);
        } catch (ResultException e) {
            return Result.build(404, e);
        }
    }

    //删除备注信息
    @RequestMapping("/delRemark")
    public Result delRemarks(String id) {
        try {
            tblActivityRemarkService.delRemarks(id);
            return Result.OK();
        } catch (ResultException e) {
            return Result.build(1005, e);
        }
    }
    //获取输入框内之前的值
    @RequestMapping("/editRemark")
    public Result editRemark(String id) {
        try {
            TblActivityRemark tblActivityRemark = tblActivityRemarkService.editRemark(id);
            return Result.OK(tblActivityRemark);
        } catch (ResultException e) {
            return Result.build(1005, e);
        }

    }
    @RequestMapping("/updateRemarkMessage")
    public Result updateRemarkMessage(TblActivityRemark tblActivityRemark,HttpServletRequest request){
        //获取到当前登录用户
        TblUser tblUser = (TblUser) request.getSession().getAttribute(SESSION_USER);

        //将登录用户赋值给编辑人
        tblActivityRemark.setEditby(tblUser.getName());

        //
        try {
            TblActivityRemark tblActivityRemark1 = tblActivityRemarkService.updateRemarkMessage(tblActivityRemark);
            return Result.OK(tblActivityRemark1);
        } catch (ResultException e) {
            return Result.build(1005,e);
        }
    }

}
