package com.powernode.workbench.mapper;

import com.powernode.workbench.pojo.TblCustomerRemark;
import com.powernode.workbench.pojo.TblCustomerRemarkExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblCustomerRemarkMapper {
    int countByExample(TblCustomerRemarkExample example);

    int deleteByExample(TblCustomerRemarkExample example);

    int deleteByPrimaryKey(String id);

    int insert(TblCustomerRemark record);

    int insertSelective(TblCustomerRemark record);

    List<TblCustomerRemark> selectByExample(TblCustomerRemarkExample example);

    TblCustomerRemark selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TblCustomerRemark record, @Param("example") TblCustomerRemarkExample example);

    int updateByExample(@Param("record") TblCustomerRemark record, @Param("example") TblCustomerRemarkExample example);

    int updateByPrimaryKeySelective(TblCustomerRemark record);

    int updateByPrimaryKey(TblCustomerRemark record);
}