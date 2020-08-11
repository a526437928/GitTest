package com.powernode.workbench.controller;


import com.powernode.Exception.ResultException;
import com.powernode.settings.pojo.TblUser;
import com.powernode.util.DateTimeUtil;
import com.powernode.util.PageResult;
import com.powernode.util.Result;
import com.powernode.workbench.pojo.TblActivity;
import com.powernode.workbench.service.TblActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("activity")
public class TblActivityController {

    @Value("${session}")
    private String SESSION_USER;


    @Autowired
    TblActivityService tblActivityService;

    //获取所有用户
    @RequestMapping("/user")
    public Result findByUsers() {
        try {
            List<TblUser> byList = tblActivityService.getByList();
            return Result.OK(byList);
        } catch (ResultException e) {
            return Result.build(404, e);
        }
    }

    //添加市场活动信息
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result addd(TblActivity tblActivity, HttpServletRequest request) {
        TblUser user = (TblUser) request.getSession().getAttribute(SESSION_USER);
        String name = user.getName();
        if (tblActivity.getEdittime() == null || tblActivity.getEditby() == null) {
            tblActivity.setEdittime("暂无修改");
            tblActivity.setEditby("暂无修改");
        }
        try {
            tblActivityService.add(tblActivity, name);

            return Result.OK();
        } catch (ResultException e) {
            return Result.build(e);
        }
    }

    //分页查询   pageSize:每页显示数，pageNo:页码
    @RequestMapping("/pageSelect")
    public Result pageList(@RequestParam(value = "pageSize", required = true) int pageSize,
                           @RequestParam(value = "pageNo", required = true, defaultValue = "1") int pageNo,
                           @RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "owner", required = false) String owner,
                           @RequestParam(value = "startdate", required = false) String startdate,
                           @RequestParam(value = "enddate", required = false) String enddate) {


        try {
            PageResult pageResult = tblActivityService.selectPageList(pageSize, pageNo, name, owner, startdate, enddate);
            return Result.OK(pageResult);

        } catch (ResultException e) {
            return Result.build(404, e);
        }

    }

    //删除复选框记录
    @RequestMapping("/del")
    public Result delItems(@RequestParam("ids") String[] ids) {
        try {
            tblActivityService.delItem(ids);
            return Result.OK();
        } catch (ResultException e) {
            return Result.build(1005, e);
        }
    }


    //根据市场活动ID查询
    @RequestMapping("/id")
    public Result update(String id) {

        try {
            TblActivity tblActivity = tblActivityService.getById(id);

            //获取所有用户放到集合中
            List<TblUser> byList = tblActivityService.getByList();
            Map map = new HashMap<>();
            map.put("userList", byList);
            map.put("activity", tblActivity);

            return Result.OK(map);
        } catch (ResultException e) {
            return Result.build(404, e);
        }
    }


    @RequestMapping("/updateMessage")
    public Result updateMessage(@RequestBody TblActivity tblActivity, HttpServletRequest request) {
        //从域中拿到登录人
        TblUser tblUser = (TblUser) request.getSession().getAttribute(SESSION_USER);
        String LoginUserName = tblUser.getName();

      /*  //把登录人赋值给创建人
        tblActivity.setCreateby(LoginUserName);*/

        //把登录人赋值给修改人
        tblActivity.setEditby(LoginUserName);

        try {
            TblActivity tblActivity1 = tblActivityService.updateMessage(tblActivity);
            return Result.OK(tblActivity1);
        } catch (ResultException e) {
            return Result.build(1005, e);
        }

    }

    //跳转页面之后的市场活动展示界面
    @RequestMapping("/detaill")
    public Result itemDetail(String id) {
        try {
            TblActivity item = tblActivityService.getItem(id);

            return Result.OK(item);
        } catch (ResultException e) {
            return Result.build(404, e);
        }

    }

    //详情页修改1
    @RequestMapping("/updateMessage2")
    public Result updateMessage2(@RequestBody TblActivity tblActivity, HttpServletRequest request){
        //从域中拿到登录人
        TblUser tblUser = (TblUser) request.getSession().getAttribute(SESSION_USER);
        String LoginUserName = tblUser.getName();

        //把登录人赋值给修改人
        tblActivity.setEditby(LoginUserName);

        try {
            TblActivity tblActivity1 = tblActivityService.updateMessage2(tblActivity);
            return Result.OK(tblActivity1);
        } catch (ResultException e) {
            return Result.build(1005, e);
        }
    }
}
