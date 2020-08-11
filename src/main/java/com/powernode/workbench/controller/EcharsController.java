package com.powernode.workbench.controller;

import com.powernode.util.Result;
import com.powernode.workbench.service.EchartsService;
import com.powernode.workbench.service.TblActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/eharts")
public class EcharsController {

    @Autowired
    EchartsService echartsService;

    @RequestMapping("/getActivityData")
    public Result getActivityData(){
        Map<String[], String[]> activityNameCost = echartsService.getActivityNameCost();
        return Result.OK(activityNameCost);
    }

}
