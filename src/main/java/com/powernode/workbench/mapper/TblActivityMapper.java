package com.powernode.workbench.mapper;

import com.powernode.workbench.pojo.TblActivity;
import com.powernode.workbench.pojo.TblActivityExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblActivityMapper {
    int countByExample(TblActivityExample example);

    int deleteByExample(TblActivityExample example);

    int deleteByPrimaryKey(String id);

    int insert(TblActivity record);

    int insertSelective(TblActivity record);

    List<TblActivity> selectByExample(TblActivityExample example);

    TblActivity selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TblActivity record, @Param("example") TblActivityExample example);

    int updateByExample(@Param("record") TblActivity record, @Param("example") TblActivityExample example);

    int updateByPrimaryKeySelective(TblActivity record);

    int updateByPrimaryKey(TblActivity record);

    String[] getActivityName();

    String[] getActivityCost();
}