package com.powernode.workbench.service;

import com.github.pagehelper.PageInfo;
import com.powernode.settings.pojo.TblUser;
import com.powernode.util.PageResult;
import com.powernode.workbench.pojo.TblActivity;

import java.util.List;

public interface TblActivityService {
    List<TblUser> getByList();

    void add(TblActivity tblActivity,String name);

    PageResult selectPageList(int pageSize, int pageNo, String name, String owner, String startdate, String enddate);

    void delItem(String[] ids);

    TblActivity getById(String id);

    TblActivity updateMessage(TblActivity tblActivity);

    TblActivity getItem(String id);

    TblActivity updateMessage2(TblActivity tblActivity);


}
