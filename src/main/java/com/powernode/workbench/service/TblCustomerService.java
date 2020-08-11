package com.powernode.workbench.service;

import com.powernode.settings.pojo.TblUser;
import com.powernode.util.PageResult;
import com.powernode.workbench.pojo.TblCustomer;

import java.util.List;
import java.util.Map;

public interface TblCustomerService {
    Map getUsers();

    void addCustomer(TblCustomer tblCustomer,String createBy);

    PageResult selectPageList(int pageSize, int pageNo, String name, String owner, String phone, String website);
}
