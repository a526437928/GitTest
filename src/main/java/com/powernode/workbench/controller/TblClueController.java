package com.powernode.workbench.controller;

import com.powernode.Exception.ResultException;
import com.powernode.settings.pojo.TblUser;
import com.powernode.util.PageResult;
import com.powernode.util.Result;
import com.powernode.workbench.pojo.TblActivity;
import com.powernode.workbench.pojo.TblClue;
import com.powernode.workbench.service.TblClueService;
import org.omg.PortableInterceptor.ACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("clue")
public class TblClueController {
    @Autowired
    private TblClueService tblClueService;

    @Value("${session}")
    private String SESSION_USER;



    @RequestMapping("/clues")
    public Result getClueList() {
        try {
            List clueList = tblClueService.getItems();
            return Result.OK(clueList);
        } catch (ResultException e) {
            return Result.build(404, e);
        }

    }

    //获取下拉框数据
    @RequestMapping("appellation")
    public Result getSelectData(HttpServletRequest request) {
        try {
            Map map = new HashMap<>();
            ServletContext servletContext = request.getSession().getServletContext();
            map.put("userList", tblClueService.getUserItems());
            map.put("appellation", servletContext.getAttribute("appellation"));
            map.put("clueState", servletContext.getAttribute("clueState"));
            map.put("source", servletContext.getAttribute("source"));
            return Result.OK(map);
        } catch (ResultException e) {
            return Result.build(404, e);
        }

    }

    //创建线索，点击保存
    @RequestMapping(value="/addd",method = RequestMethod.POST)
    public Result addClueSave(@RequestBody TblClue tblClue, HttpServletRequest request) {
        //拿到登录者作为创建人createby
        TblUser tblUser = (TblUser) request.getSession().getAttribute(SESSION_USER);
        String name = tblUser.getName();
        tblClue.setCreateby(name);

        try {
            tblClueService.addClueSave(tblClue);
            return Result.OK();
        } catch (ResultException e) {
            return Result.build(1005,e);
        }
    }


    ////获取详情页面的信息(跳转过后的页面)
    @RequestMapping("/getDetail")
    public Result getDetail(String id){
        try {
            TblClue tblClueDetail = tblClueService.getDetail(id);
           return Result.OK(tblClueDetail);
        } catch (ResultException e) {
           return Result.build(404,e);
        }

    }

    @RequestMapping("/relation")
    public Result getClueRelation(String id){
        try {
            List byActivity = tblClueService.getByActivity(id);
           return Result.OK(byActivity);
        } catch (ResultException e) {
           return Result.build(1005,e);
        }

    }

    //解除关联关系
    @RequestMapping("/dell")
    public Result dell(String id){
        try {
            tblClueService.dell(id);
            return Result.OK();
        } catch (ResultException e) {
           return Result.build(404,e);
        }
    }


    //获取关联市场活动数据
    @RequestMapping("/getActivity")
    public Result getActivitys(@RequestParam(value = "name", required = false) String name) {

            List activitys = tblClueService.getActivitys(name);
           return Result.OK(activitys);


    }

    //添加关联
    @RequestMapping("/addrelation")
    public Result relationActivitySave(@RequestParam("id") String id,@RequestParam("ids[]") String[] ids){

            tblClueService.relationActivitySavee(id,ids);
            return Result.OK();
    }

    //从域中获取下拉框的值并赋值在下拉框内
    @RequestMapping("/stage")
    public Result getStage(HttpServletRequest request){
        Object stage = request.getSession().getServletContext().getAttribute("stage");
        return Result.OK(stage);
    }

    //查看线索和市场活动关联关系
    @RequestMapping("/relationactivity")
    public Result relationactivity(
            @RequestParam("id") String id,
            @RequestParam(value = "search",required = false) String search){
        List<TblActivity> relationactivity = tblClueService.relationactivity(id,search);
        return Result.OK(relationactivity);
    }


    //把线索转换成真正的客户
    @RequestMapping("/convert")
    public Result convert(String clueId,
                          @RequestParam(value = "money",required = false) String money,
                          @RequestParam(value = "tran",required = false) String tran,
                          @RequestParam(value = "exdate",required = false) String exdate,
                          @RequestParam(value = "stage",required = false) String stage,
                          @RequestParam(value = "activityId",required = false) String activityId,
                          String tranStage,
                          HttpServletRequest request){

        //将钱逗号隔开
        StringBuilder str = new StringBuilder();
        str.append(money);
        int last = str.length();
        for(int i = last - 3; i > 0; i-=3) {
            str.insert(i,',');
        }
        String money1 = str.toString();


        TblUser tblUser =  (TblUser)request.getSession().getAttribute(SESSION_USER);
        String createBy = tblUser.getName();

        tblClueService.convert(clueId, money1, tran, exdate, stage, activityId,createBy,tranStage);
        return Result.OK();
    }

    //分页模糊查询
    //分页模糊查询   pageSize:每页显示数，pageNo:页码
    @RequestMapping("/pageSelect")
    public Result pageList(@RequestParam(value = "pageSize", required = true) int pageSize,
                           @RequestParam(value = "pageNo", required = true, defaultValue = "1") int pageNo,
                           @RequestParam(value = "fullname", required = false) String fullname,
                           @RequestParam(value = "company", required = false) String company,
                           @RequestParam(value = "phone", required = false) String phone,
                           @RequestParam(value = "mphone", required = false) String mphone,
                           @RequestParam(value = "source", required = false) String source,
                           @RequestParam(value = "owner", required = false) String owner,
                           @RequestParam(value = "state", required = false) String state) {


        PageResult pageResult = tblClueService.selectPageList(pageSize, pageNo, fullname, company, phone, mphone, source, owner, state);
        return Result.OK(pageResult);

    }
}
