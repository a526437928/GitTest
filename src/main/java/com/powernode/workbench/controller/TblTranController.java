package com.powernode.workbench.controller;

import com.powernode.Exception.ResultException;
import com.powernode.settings.pojo.TblDicValue;
import com.powernode.settings.pojo.TblUser;
import com.powernode.settings.service.TblUserServie;
import com.powernode.util.PageResult;
import com.powernode.util.Result;
import com.powernode.workbench.pojo.*;
import com.powernode.workbench.service.TblTranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


@RestController
@RequestMapping("/tran")
public class TblTranController {

    @Autowired
    private TblTranService tblTranService;


    @Value("${session}")
    private String SESSION_USER;

    @RequestMapping("/context")
    public Result getApplication(HttpServletRequest request) {
        ServletContext servletContext = request.getSession().getServletContext();
        Map map = new HashMap<>();

        //获取  所有者  下拉框的值
        List<TblUser> users = tblTranService.getByUsers();

        //①先从域中获取三个下拉框的值
        //②再通过Map集合把四个拉下框组合起来并返回
        map.put("stage", servletContext.getAttribute("stage"));
        map.put("source", servletContext.getAttribute("source"));
        map.put("transactionType", servletContext.getAttribute("transactionType"));
        map.put("users", users);

        return Result.OK(map);
    }

    //获取 可能性 值
    @RequestMapping("/getPoss")
    public Result getPoss(String stage, HttpServletRequest request) {
        ServletContext servletContext = request.getSession().getServletContext();
        Map map = (Map) servletContext.getAttribute("mMap");
        //把具体选中的值传入
        return Result.OK(map.get(stage));
    }

    //获取市场活动源
    @RequestMapping("/addActivity")
    public Result addActivity(@RequestParam(value = "search", required = false) String search) {
        List<TblActivity> relationactivity = tblTranService.addActivity(search);
        return Result.OK(relationactivity);
    }

    //获取联系人
    @RequestMapping("/addContacts")
    public Result addContacts(@RequestParam(value = "search", required = false) String search) {
        List<TblContacts> tblContacts = tblTranService.addContacts(search);
        return Result.OK(tblContacts);
    }

    //实时搜索(搜索自动补全)
    @RequestMapping("/customer")
    public Result getCustomer(String name) {
        Map<String, Object> customerName = tblTranService.getCustomerName(name);

        return Result.OK(customerName);
    }

    @RequestMapping(value = "/savetran", method = RequestMethod.POST)
    public Result saveTran(@RequestBody TblTran tblTran, HttpServletRequest request) {
        TblUser tblUser = (TblUser) request.getSession().getAttribute(SESSION_USER);
        String name = tblUser.getName();
        tblTran.setCreateby(name);
        tblTranService.saveTran(tblTran);
        return Result.OK();

    }

    //分页模糊查询
    //分页模糊查询   pageSize:每页显示数，pageNo:页码
    @RequestMapping("/pageSelect")
    public Result pageList(@RequestParam(value = "pageSize", required = true) int pageSize,
                           @RequestParam(value = "pageNo", required = true, defaultValue = "1") int pageNo,
                           @RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "customerid", required = false) String customerid,
                           @RequestParam(value = "stage", required = false) String stage,
                           @RequestParam(value = "type", required = false) String type,
                           @RequestParam(value = "owner", required = false) String owner,
                           @RequestParam(value = "source", required = false) String source,
                           @RequestParam(value = "contactsid", required = false) String contactsid) {


        PageResult pageResult = tblTranService.selectPageList(pageSize, pageNo, name, customerid, stage, type, owner, source, contactsid);
        return Result.OK(pageResult);

    }

    //交易详细信息
    @RequestMapping("getDetailMegssage")
    public Result getDetailMegssage(String id, HttpServletRequest request) {
        TblTran detailMegssageId = tblTranService.getDetailMegssage(id);

        ServletContext servletContext = request.getSession().getServletContext();

        Map mMap = (Map) servletContext.getAttribute("mMap");

        detailMegssageId.setPoss((String) mMap.get(detailMegssageId.getStage()));

        return Result.OK(detailMegssageId);
    }


    //获取阶段图标
    @RequestMapping("/stagelogo")
    public Result getStageLogo(String tranId, HttpServletRequest request) {
        ServletContext servletContext = request.getSession().getServletContext();
        //从域中获取properties中的值
        Map<String, String> prossMap = (Map<String, String>) servletContext.getAttribute("mMap");

        //从域中的字典数据中取值
        Set<TblDicValue> divList = (Set<TblDicValue>) servletContext.getAttribute("stage");

        Map<String,Object> stage = tblTranService.getStage(prossMap, divList, tranId);
        return Result.OK(stage);
    }

    ////根据点击阶段的变化，阶段值和可能性值，修改者和时间都要改变
    @RequestMapping("/addhistory")
    public Result getAddHistory(TblTranHistory tblTranHistory, HttpServletRequest request) {
        TblUser tblUser = (TblUser) request.getSession().getAttribute(SESSION_USER);
        String name = tblUser.getName();
        tblTranHistory.setCreateby(name);
        TblTran addHistory = tblTranService.getAddHistory(tblTranHistory);
        Map mMap = (Map) request.getSession().getServletContext().getAttribute("mMap");
        addHistory.setPoss((String) mMap.get(addHistory.getStage()));

        return Result.OK(addHistory);
    }

    @RequestMapping("/getTranHistory")
    public Result getPoss(HttpServletRequest request){
        ServletContext servletContext = request.getSession().getServletContext();
        Map mMap = (Map) servletContext.getAttribute("mMap");

        List<TblTranHistory> tranHistory = tblTranService.getTranHistory(mMap);

        return Result.OK(tranHistory);
    }


    //跳转到编辑页面显示获取详细信息
    @RequestMapping("/getEditMessage")
    public Result getEditMessage(HttpServletRequest request,String id){
        ServletContext servletContext = request.getSession().getServletContext();
        //从域中获取properties中的值
        Map<String, String> prossMap = (Map<String, String>) servletContext.getAttribute("mMap");

        //获取  owner  下拉框的值以及 owner集合
        Map map = tblTranService.getEditMessage(id,prossMap);

        //①先从域中获取三个下拉框的值
        //②再通过Map集合把四个拉下框组合起来并返回
        map.put("stage", servletContext.getAttribute("stage"));

        map.put("source", servletContext.getAttribute("source"));

        map.put("transactionType", servletContext.getAttribute("transactionType"));

       /* Map mMap = (Map) servletContext.getAttribute("mMap");
        map.put("possList",mMap);*/

        return Result.OK(map);
    }
}
