package com.powernode.workbench.mapper;

import com.powernode.workbench.pojo.TblTranHistory;
import com.powernode.workbench.pojo.TblTranHistoryExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblTranHistoryMapper {
    int countByExample(TblTranHistoryExample example);

    int deleteByExample(TblTranHistoryExample example);

    int deleteByPrimaryKey(String id);

    int insert(TblTranHistory record);

    int insertSelective(TblTranHistory record);

    List<TblTranHistory> selectByExample(TblTranHistoryExample example);

    TblTranHistory selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TblTranHistory record, @Param("example") TblTranHistoryExample example);

    int updateByExample(@Param("record") TblTranHistory record, @Param("example") TblTranHistoryExample example);

    int updateByPrimaryKeySelective(TblTranHistory record);

    int updateByPrimaryKey(TblTranHistory record);
}