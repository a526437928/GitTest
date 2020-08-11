package com.powernode.workbench.mapper;

import com.powernode.workbench.pojo.TblCustomer;
import com.powernode.workbench.pojo.TblCustomerExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblCustomerMapper {
    int countByExample(TblCustomerExample example);

    int deleteByExample(TblCustomerExample example);

    int deleteByPrimaryKey(String id);

    int insert(TblCustomer record);

    int insertSelective(TblCustomer record);

    List<TblCustomer> selectByExample(TblCustomerExample example);

    TblCustomer selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TblCustomer record, @Param("example") TblCustomerExample example);

    int updateByExample(@Param("record") TblCustomer record, @Param("example") TblCustomerExample example);

    int updateByPrimaryKeySelective(TblCustomer record);

    int updateByPrimaryKey(TblCustomer record);
}