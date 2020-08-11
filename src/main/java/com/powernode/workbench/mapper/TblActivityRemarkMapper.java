package com.powernode.workbench.mapper;

import com.powernode.workbench.pojo.TblActivityRemark;
import com.powernode.workbench.pojo.TblActivityRemarkExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblActivityRemarkMapper {
    int countByExample(TblActivityRemarkExample example);

    int deleteByExample(TblActivityRemarkExample example);

    int deleteByPrimaryKey(String id);

    int insert(TblActivityRemark record);

    int insertSelective(TblActivityRemark record);

    List<TblActivityRemark> selectByExample(TblActivityRemarkExample example);

    TblActivityRemark selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TblActivityRemark record, @Param("example") TblActivityRemarkExample example);

    int updateByExample(@Param("record") TblActivityRemark record, @Param("example") TblActivityRemarkExample example);

    int updateByPrimaryKeySelective(TblActivityRemark record);

    int updateByPrimaryKey(TblActivityRemark record);
}