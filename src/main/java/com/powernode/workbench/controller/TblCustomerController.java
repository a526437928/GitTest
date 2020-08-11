package com.powernode.workbench.controller;

import com.powernode.Exception.ResultException;
import com.powernode.settings.pojo.TblUser;
import com.powernode.util.PageResult;
import com.powernode.util.Result;
import com.powernode.workbench.pojo.TblCustomer;
import com.powernode.workbench.service.TblCustomerService;
import jdk.nashorn.internal.ir.CallNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customer")
public class TblCustomerController {
    @Autowired
    TblCustomerService tblCustomerService;

    @Value("${session}")
    private String SESSION_USER;

    @RequestMapping("/user")
    public Result getUsers(){

        Map users = tblCustomerService.getUsers();

        return Result.OK(users);
    }

    @RequestMapping("/addcustomer")
    public Result addCustomer(TblCustomer tblCustomer, HttpServletRequest request){

        TblUser tblUser = (TblUser)request.getSession().getAttribute(SESSION_USER);
        String createBy = tblUser.getName();

        tblCustomerService.addCustomer(tblCustomer,createBy);
        return  Result.OK();
    }


    //分页查询
    //分页查询   pageSize:每页显示数，pageNo:页码
    @RequestMapping("/pageSelect")
    public Result pageList(@RequestParam(value = "pageSize", required = true) int pageSize,
                           @RequestParam(value = "pageNo", required = true, defaultValue = "1") int pageNo,
                           @RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "owner", required = false) String owner,
                           @RequestParam(value = "phone", required = false) String phone,
                           @RequestParam(value = "website", required = false) String website) {



            PageResult pageResult = tblCustomerService.selectPageList(pageSize, pageNo, name, owner, phone, website);
            return Result.OK(pageResult);

    }



}
