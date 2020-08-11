package com.powernode.workbench.mapper;

import com.powernode.workbench.pojo.TblContactsActivityRelation;
import com.powernode.workbench.pojo.TblContactsActivityRelationExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblContactsActivityRelationMapper {
    int countByExample(TblContactsActivityRelationExample example);

    int deleteByExample(TblContactsActivityRelationExample example);

    int deleteByPrimaryKey(String id);

    int insert(TblContactsActivityRelation record);

    int insertSelective(TblContactsActivityRelation record);

    List<TblContactsActivityRelation> selectByExample(TblContactsActivityRelationExample example);

    TblContactsActivityRelation selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TblContactsActivityRelation record, @Param("example") TblContactsActivityRelationExample example);

    int updateByExample(@Param("record") TblContactsActivityRelation record, @Param("example") TblContactsActivityRelationExample example);

    int updateByPrimaryKeySelective(TblContactsActivityRelation record);

    int updateByPrimaryKey(TblContactsActivityRelation record);
}