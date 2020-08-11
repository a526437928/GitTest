package com.powernode.settings.service;

import com.powernode.settings.pojo.TblUser;

public interface TblUserServie {
    //登录接口
    TblUser login(String loginAct, String loginPwd, String ip);
}
