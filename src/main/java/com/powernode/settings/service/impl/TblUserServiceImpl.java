package com.powernode.settings.service.impl;

import com.powernode.Exception.ResultException;
import com.powernode.settings.mapper.TblUserMapper;
import com.powernode.settings.pojo.TblUser;
import com.powernode.settings.pojo.TblUserExample;
import com.powernode.settings.service.TblUserServie;
import com.powernode.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TblUserServiceImpl implements TblUserServie {

    @Autowired
    private TblUserMapper tblUserMapper;

    @Override
    public TblUser login(String loginAct, String loginPwd, String ip) {
        //判断用户输入的 用户名和密码是否为空
        if (loginAct == null || "".equals(loginAct) || (loginPwd == null || "".equals(loginPwd))) {
            throw new ResultException("用户名或密码不能为空");
        }
        //创建离线检索对象,查询
        TblUserExample tblUserExample = new TblUserExample();
        TblUserExample.Criteria criteria = tblUserExample.createCriteria();
        criteria.andLoginactEqualTo(loginAct);
        criteria.andLoginpwdEqualTo(loginPwd);
        List<TblUser> tblUsers = tblUserMapper.selectByExample(tblUserExample);

        if (tblUsers == null || tblUsers.size() == 0) {
            throw new ResultException("登陆失败");
        }

        TblUser tblUser = tblUsers.get(0);  //

        //判断ip地址是否受限
        if(!tblUser.getAllowips().contains(ip)){
            throw new ResultException("当前IP地址受限，请更换地址!");
        }
        //判断该用户状态是否可以正常登录 0：正常  1：受限
        if("1".equals(tblUser.getLockstate())){
            throw new ResultException("当前账号已锁定");
        }

        //失效时间，如果登录时间晚于数据库记录的已失效时间则无法登录
        String sysTime = DateTimeUtil.getSysTime();
        if(sysTime.compareTo(tblUser.getExpiretime())>0){
            throw new ResultException("该账户已失效");
        }

        return tblUser;
    }
}
