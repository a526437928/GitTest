package com.powernode.workbench.service;

import com.github.pagehelper.PageInfo;
import com.powernode.settings.pojo.TblUser;
import com.powernode.util.PageResult;
import com.powernode.workbench.pojo.TblActivity;
import com.powernode.workbench.pojo.TblClue;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public interface TblClueService {
    //获取所有线索
    List getItems();

    //获取所有者
    List<TblUser> getUserItems();

    //添加线索信息
    void addClueSave(TblClue tblClue);

    //获取详情页面的信息(跳转过后的页面)
    TblClue getDetail(String id);

    List getByActivity(String clueId);

    void dell(String id);

    List getActivitys(String name);

    void relationActivitySavee(String id, String[] ids);

    List<TblActivity> relationactivity(String id, String search);

    void convert(String clueId, String money, String tran, String exdate, String stage, String activityId,String createBy,String tranStage);

    PageResult selectPageList(int pageSize, int pageNo, String fullname, String company, String phone, String mphone, String source, String owner, String state);
}
