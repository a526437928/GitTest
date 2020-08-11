package com.powernode.workbench.mapper;

import com.powernode.workbench.pojo.TblClueRemark;
import com.powernode.workbench.pojo.TblClueRemarkExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblClueRemarkMapper {
    int countByExample(TblClueRemarkExample example);

    int deleteByExample(TblClueRemarkExample example);

    int deleteByPrimaryKey(String id);

    int insert(TblClueRemark record);

    int insertSelective(TblClueRemark record);

    List<TblClueRemark> selectByExample(TblClueRemarkExample example);

    TblClueRemark selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TblClueRemark record, @Param("example") TblClueRemarkExample example);

    int updateByExample(@Param("record") TblClueRemark record, @Param("example") TblClueRemarkExample example);

    int updateByPrimaryKeySelective(TblClueRemark record);

    int updateByPrimaryKey(TblClueRemark record);
}