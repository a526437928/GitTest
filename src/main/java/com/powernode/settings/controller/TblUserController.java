package com.powernode.settings.controller;

import com.powernode.Exception.ResultException;
import com.powernode.settings.pojo.TblUser;
import com.powernode.settings.service.TblUserServie;
import com.powernode.util.MD5Util;
import com.powernode.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("user")
public class TblUserController {
    @Autowired
    TblUserServie tblUserServie;

    @Value("${session}")
    private String SESSION_USER;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestParam("name") String loginAct,@RequestParam("pwd") String loginPwd, HttpServletRequest request) {

        //请求的ip地址
        String remoteAddr = request.getRemoteAddr();
        try {

            //把用户的数据传入，并且密码进行MD5加密
            TblUser login = tblUserServie.login(loginAct, MD5Util.getMD5(loginPwd), remoteAddr);

            //将登录用户存储在session域中
            request.getSession().setAttribute(SESSION_USER, login);
            return Result.OK();
        } catch (ResultException e) {
            return Result.build(1005, e);
        }
    }


    @RequestMapping("username")
    public Result getByUserName(HttpServletRequest request){
        TblUser tblUser = (TblUser) request.getSession().getAttribute(SESSION_USER);
        /*System.out.println(tblUser);*/
        return Result.OK(tblUser);
    }
}
