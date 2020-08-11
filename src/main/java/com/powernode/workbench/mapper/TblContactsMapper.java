package com.powernode.workbench.mapper;

import com.powernode.workbench.pojo.TblContacts;
import com.powernode.workbench.pojo.TblContactsExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblContactsMapper {
    int countByExample(TblContactsExample example);

    int deleteByExample(TblContactsExample example);

    int deleteByPrimaryKey(String id);

    int insert(TblContacts record);

    int insertSelective(TblContacts record);

    List<TblContacts> selectByExample(TblContactsExample example);

    TblContacts selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TblContacts record, @Param("example") TblContactsExample example);

    int updateByExample(@Param("record") TblContacts record, @Param("example") TblContactsExample example);

    int updateByPrimaryKeySelective(TblContacts record);

    int updateByPrimaryKey(TblContacts record);
}