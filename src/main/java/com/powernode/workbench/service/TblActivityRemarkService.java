package com.powernode.workbench.service;

import com.powernode.workbench.pojo.TblActivityRemark;

import java.util.List;

public interface TblActivityRemarkService {

    //添加备注

    TblActivityRemark add(String acId, String noteContent, String createBy);

    List getRemarks(String id);

    void delRemarks(String id);

    TblActivityRemark editRemark(String id);

    TblActivityRemark updateRemarkMessage(TblActivityRemark tblActivityRemark);
}
