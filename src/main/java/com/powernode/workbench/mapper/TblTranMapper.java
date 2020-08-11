package com.powernode.workbench.mapper;

import com.powernode.workbench.pojo.TblTran;
import com.powernode.workbench.pojo.TblTranExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblTranMapper {
    int countByExample(TblTranExample example);

    int deleteByExample(TblTranExample example);

    int deleteByPrimaryKey(String id);

    int insert(TblTran record);

    int insertSelective(TblTran record);

    List<TblTran> selectByExample(TblTranExample example);

    TblTran selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TblTran record, @Param("example") TblTranExample example);

    int updateByExample(@Param("record") TblTran record, @Param("example") TblTranExample example);

    int updateByPrimaryKeySelective(TblTran record);

    int updateByPrimaryKey(TblTran record);
}