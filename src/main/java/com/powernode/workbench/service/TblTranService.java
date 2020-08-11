package com.powernode.workbench.service;

import com.powernode.settings.pojo.TblDicValue;
import com.powernode.settings.pojo.TblUser;
import com.powernode.util.PageResult;
import com.powernode.workbench.pojo.TblActivity;
import com.powernode.workbench.pojo.TblContacts;
import com.powernode.workbench.pojo.TblTran;
import com.powernode.workbench.pojo.TblTranHistory;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TblTranService {
    List<TblUser> getByUsers();

    //查询市场活动
    List<TblActivity> addActivity(String search);

    //查询联系人
    List<TblContacts> addContacts(String search);

    //实时搜索
    Map<String,Object> getCustomerName(String name);

    //创建交易
    void saveTran(TblTran tblTran);

    //分页查询
    PageResult selectPageList(int pageSize, int pageNo, String name,String customerid, String stage,String type, String owner, String source,String contactsid);

    //交易详细信息
    TblTran getDetailMegssage(String id);

    //获取当前阶段名称，下标，以及图标类型
    Map<String,Object> getStage(Map<String,String> prossMap, Set<TblDicValue> divList, String tranId);

    TblTran getAddHistory(TblTranHistory tblTranHistory);

    List<TblTranHistory> getTranHistory(Map map);


    Map getEditMessage(String id,Map<String,String> prossMap);
}
