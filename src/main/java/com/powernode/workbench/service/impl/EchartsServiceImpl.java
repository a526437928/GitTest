package com.powernode.workbench.service.impl;

import com.powernode.workbench.mapper.TblActivityMapper;
import com.powernode.workbench.pojo.TblActivity;
import com.powernode.workbench.service.EchartsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class EchartsServiceImpl implements EchartsService {
    @Autowired
    private TblActivityMapper tblActivityMapper;

    @Override
    public Map<String[], String[]> getActivityNameCost() {
        Map map = new HashMap();
        String[] activityName = tblActivityMapper.getActivityName();
        String[] activityCost = tblActivityMapper.getActivityCost();
        map.put("Cost",activityCost);
        map.put("Name",activityName);
        return map;
    }
}
