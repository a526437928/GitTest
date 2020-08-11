package com.powernode.settings.mapper;

import com.powernode.settings.pojo.TblDicType;
import com.powernode.settings.pojo.TblDicTypeExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblDicTypeMapper {
    int countByExample(TblDicTypeExample example);

    int deleteByExample(TblDicTypeExample example);

    int deleteByPrimaryKey(String code);

    int insert(TblDicType record);

    int insertSelective(TblDicType record);

    List<TblDicType> selectByExample(TblDicTypeExample example);

    TblDicType selectByPrimaryKey(String code);

    int updateByExampleSelective(@Param("record") TblDicType record, @Param("example") TblDicTypeExample example);

    int updateByExample(@Param("record") TblDicType record, @Param("example") TblDicTypeExample example);

    int updateByPrimaryKeySelective(TblDicType record);

    int updateByPrimaryKey(TblDicType record);
}