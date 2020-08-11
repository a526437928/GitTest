package com.powernode.workbench.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.powernode.Exception.ResultException;
import com.powernode.settings.mapper.TblUserMapper;
import com.powernode.settings.pojo.TblDicValue;
import com.powernode.settings.pojo.TblUser;
import com.powernode.settings.pojo.TblUserExample;
import com.powernode.util.*;
import com.powernode.workbench.mapper.*;
import com.powernode.workbench.pojo.*;
import com.powernode.workbench.service.TblTranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Transactional
@Service
public class TblTranServiceImpl implements TblTranService {
    @Autowired
    TblUserMapper tblUserMapper;

    @Autowired
    TblActivityMapper tblActivityMapper;

    @Autowired
    TblContactsMapper tblContactsMapper;

    @Autowired
    TblCustomerMapper tblCustomerMapper;

    @Autowired
    TblTranMapper tblTranMapper;

    @Autowired
    TblTranHistoryMapper tblTranHistoryMapper;

    @Override
    //单独获取一个Users类
    public List<TblUser> getByUsers() {
        List<TblUser> tblUsers = tblUserMapper.selectByExample(null);
        if (tblUsers == null || tblUsers.size() == 0) {
            throw new ResultException("未找到数据");
        }
        return tblUsers;
    }


    //获取市场活动源
    @Override
    public List<TblActivity> addActivity(String search) {
        TblActivityExample tblActivityExample = new TblActivityExample();
        TblActivityExample.Criteria criteria = tblActivityExample.createCriteria();

        if (search != null && !"".equals(search)) {
            criteria.andNameLike("%" + search + "%");
        }

        List<TblActivity> activities = tblActivityMapper.selectByExample(tblActivityExample);

        if (activities == null || activities.size() == 0) {
            throw new ResultException(ResultEnum.NOTFOUND_DATA);
        }

        //将MD5转中文
        for (TblActivity activity : activities) {
            TblUser tblUser = tblUserMapper.selectByPrimaryKey(activity.getOwner());
            activity.setOwner(tblUser.getName());
        }

        return activities;
    }

    //获取联系人
    @Override
    public List<TblContacts> addContacts(String search) {
        TblContactsExample tblContactsExample = new TblContactsExample();
        TblContactsExample.Criteria criteria = tblContactsExample.createCriteria();

        if (search != null && !"".equals(search)) {
            criteria.andFullnameLike("%" + search + "%");
        }

        List<TblContacts> tblContacts = tblContactsMapper.selectByExample(tblContactsExample);

        if (tblContacts == null || tblContacts.size() == 0) {
            throw new ResultException(ResultEnum.NOTFOUND_DATA);
        }

        return tblContacts;

    }

    //实时搜索功能
    @Override
    public Map<String, Object> getCustomerName(String name) {
        //根据传入的name查询出所有的客户
        TblCustomerExample tblCustomerExample = new TblCustomerExample();
        TblCustomerExample.Criteria criteria = tblCustomerExample.createCriteria();
        criteria.andNameLike("%" + name + "%");

        List<TblCustomer> tblCustomers = tblCustomerMapper.selectByExample(tblCustomerExample);

        if (tblCustomers == null || tblCustomers.size() == 0) {
            throw new ResultException("未找到客户客户信息");
        }
        Map<String, Object> map = new HashMap<>();
        Map<String, String> map1 = new HashMap<>();
        //获取每个要查询的客户名字并放入集合中
        List<String> lists = new ArrayList<>();
        for (TblCustomer tblCustomer : tblCustomers) {

            String id = tblCustomer.getId();
            String name1 = tblCustomer.getName();
            map1.put(id, name1);
            lists.add(name1);
            map.put("name", lists);
            map.put("id", map1);

        }

        return map;
    }

    //点击保存，将交易保存
    @Override
    public void saveTran(TblTran tblTran) {
        String sysTime = DateTimeUtil.getSysTime();
        tblTran.setCreatetime(sysTime);
        tblTran.setId(UUIDUtil.getUUID());

        TblActivityExample tblActivityExample = new TblActivityExample();
        TblActivityExample.Criteria criteria = tblActivityExample.createCriteria();
        criteria.andNameEqualTo(tblTran.getActivityid());
        List<TblActivity> activities = tblActivityMapper.selectByExample(tblActivityExample);
        for (TblActivity activity : activities) {
            tblTran.setActivityid(activity.getId());
        }

        TblContactsExample tblContactsExample = new TblContactsExample();
        TblContactsExample.Criteria criteria1 = tblContactsExample.createCriteria();
        criteria1.andFullnameEqualTo(tblTran.getContactsid());
        List<TblContacts> tblContacts = tblContactsMapper.selectByExample(tblContactsExample);
        for (TblContacts tblContact : tblContacts) {
            tblTran.setContactsid(tblContact.getId());
        }

        TblCustomerExample tblCustomerExample = new TblCustomerExample();
        TblCustomerExample.Criteria criteria2 = tblCustomerExample.createCriteria();
        criteria2.andNameEqualTo(tblTran.getCustomerid());
        List<TblCustomer> tblCustomers = tblCustomerMapper.selectByExample(tblCustomerExample);
        for (TblCustomer tblCustomer : tblCustomers) {
            tblTran.setCustomerid(tblCustomer.getId());
        }


        if (tblTran.getOwner() == null || "".equals(tblTran.getOwner())) {
            throw new ResultException("交易信息不完整，缺少所有者信息");
        }
        if (tblTran.getName() == null || "".equals(tblTran.getName())) {
            throw new ResultException("交易信息不完整，缺少名称信息");
        }
        if (tblTran.getCustomerid() == null || "".equals(tblTran.getCustomerid())) {
            throw new ResultException("交易信息不完整，缺少客户名称信息");
        }
        if (tblTran.getStage() == null || "".equals(tblTran.getStage())) {
            throw new ResultException("交易信息不完整，缺少阶段信息");
        }
        if (tblTran.getExpecteddate() == null || "".equals(tblTran.getExpecteddate())) {
            throw new ResultException("交易信息不完整,缺少预计成交日期信息");
        }
        try {
            tblTranMapper.insertSelective(tblTran);
        } catch (Exception e) {
            throw new ResultException("插入失败！");
        }

        try {

            //创建交易历史
            TblTranHistory tblTranHistory = new TblTranHistory();
            tblTranHistory.setId(UUIDUtil.getUUID());
            tblTranHistory.setTranid(tblTran.getId());
            tblTranHistory.setMoney(tblTran.getMoney());
            tblTranHistory.setExpecteddate(tblTran.getExpecteddate());
            tblTranHistory.setCreatetime(sysTime);
            tblTranHistory.setCreateby(tblTran.getCreateby());
            tblTranHistory.setStage(tblTran.getStage());

            tblTranHistoryMapper.insertSelective(tblTranHistory);
        } catch (Exception e) {
            throw new ResultException("插入失败！");
        }
    }

    //分页查询
    @Override
    public PageResult selectPageList(int pageSize, int pageNo, String name, String customerid, String stage, String type, String owner, String source, String contactsid) {

        //开启分页
        PageHelper.startPage(pageNo, pageSize);

        //模糊查询
        TblTranExample tblTranExample = new TblTranExample();
        TblTranExample.Criteria criteria = tblTranExample.createCriteria();

        if (name != null && !"".equals(name)) {
            criteria.andNameLike("%" + name + "%");
        }

        List list = new ArrayList();
        if (customerid != null && !"".equals(customerid)) {
            TblCustomerExample tblCustomerExample = new TblCustomerExample();
            TblCustomerExample.Criteria criteria1 = tblCustomerExample.createCriteria();

            criteria1.andNameLike("%" + customerid + "%");

            List<TblCustomer> tblCustomers = tblCustomerMapper.selectByExample(tblCustomerExample);
            for (TblCustomer tblCustomer : tblCustomers) {
                list.add(tblCustomer.getId());
            }

            //根据所有的所有的customer的ID查询
            criteria.andCustomeridIn(list);

        }

        if (stage != null && !"".equals(stage)) {
            criteria.andStageLike("%" + stage + "%");
        }

        if (type != null & !"".equals(type)) {
            criteria.andTypeLike("%" + type + "%");
        }

        List listt = new ArrayList();
        if (owner != null && !"".equals(owner)) {
            //创建user离线检索对象
            TblUserExample tblUserExample = new TblUserExample();
            TblUserExample.Criteria criteria1 = tblUserExample.createCriteria();

            //根据传过来的owner查询user表
            criteria1.andNameLike("%" + owner + "%");

            //将查询出来的所有user的id属性循环放入集合
            List<TblUser> tblUsers = tblUserMapper.selectByExample(tblUserExample);
            for (TblUser tblUser : tblUsers) {
                listt.add(tblUser.getId());
            }
            //activity根据所有user的id查询
            criteria.andOwnerIn(listt);
        }

        if (source != null && !"".equals(source)) {
            criteria.andSourceLike("%" + source + "%");
        }

        List listtt = new ArrayList();
        if (contactsid != null && !"".equals(contactsid)) {
            TblContactsExample tblContactsExample = new TblContactsExample();
            TblContactsExample.Criteria criteria1 = tblContactsExample.createCriteria();

            criteria1.andFullnameLike("%" + contactsid + "%");

            List<TblContacts> tblContacts = tblContactsMapper.selectByExample(tblContactsExample);
            for (TblContacts tblContact : tblContacts) {
                listtt.add(tblContact.getId());
            }

            criteria.andContactsidIn(listtt);
        }

        //查询
        List<TblTran> tblTrans = tblTranMapper.selectByExample(tblTranExample);
        if (tblTrans == null || tblTrans.size() == 0) {
            throw new ResultException("未找到数据!");
        }

        /*根据owner查询user，拿到user对象的name属性给owner赋值*/
        for (TblTran tblTran : tblTrans) {
            TblUser tblUser = tblUserMapper.selectByPrimaryKey(tblTran.getOwner());
            tblTran.setOwner(tblUser.getName());

            TblCustomer tblCustomer = tblCustomerMapper.selectByPrimaryKey(tblTran.getCustomerid());
            tblTran.setCustomerid(tblCustomer.getName());

            TblContacts tblContacts = tblContactsMapper.selectByPrimaryKey(tblTran.getContactsid());
            tblTran.setContactsid(tblContacts.getFullname());

        }

        //封装list到PageInfo对象中自动分页
        PageInfo<TblTran> pageInfo = new PageInfo<>(tblTrans);
        PageResult pageResult = new PageResult(pageInfo.getTotal(), pageInfo.getList());
        return pageResult;
    }

    //获取详细信息（页面跳转过后的）
    @Override
    public TblTran getDetailMegssage(String id) {

        TblTran tblTran = tblTranMapper.selectByPrimaryKey(id);

        if (tblTran == null) {
            throw new ResultException("未找到详细信息");
        }

        TblCustomer tblCustomer = tblCustomerMapper.selectByPrimaryKey(tblTran.getCustomerid());
        tblTran.setCustomerid(tblCustomer.getName());

        TblActivity tblActivity = tblActivityMapper.selectByPrimaryKey(tblTran.getActivityid());
        tblTran.setActivityid(tblActivity.getName());

        TblContacts tblContacts = tblContactsMapper.selectByPrimaryKey(tblTran.getContactsid());
        tblTran.setContactsid(tblContacts.getFullname());

        TblUser tblUser = tblUserMapper.selectByPrimaryKey(tblTran.getOwner());
        tblTran.setOwner(tblUser.getName());

        return tblTran;
    }




    /*
           实现阶段图标业务
            1.绿圈    2.绿锚点    3.黑圈   4.红叉   5.黑叉
           返回结果：并不是最终结果
           [
            {text:  01xxx
             lcoation: 1
             type: 1
            },
           ]
     */



    //获取当前阶段名称，下标，以及图标类型
    @Override                                //从域中获取properties中的值        //从域中的字典数据中取值stage
    public Map<String,Object> getStage(Map<String, String> prossMap, Set<TblDicValue> divList, String tranId) {
        Map<String,Object> returnObj = new HashMap<String,Object>();

        List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

        //把set集合转list
        List<TblDicValue> dvList = new ArrayList<>(divList);

        //---------------------获取0和非0的分界点
        int point = 0;

        //获取阶段中的每一个值
        for (int i = 0; i < dvList.size(); i++) {
            //阶段的每一个对象
            TblDicValue dicValue = dvList.get(i);

            //获取每一个stage的Value值
            String value = dicValue.getValue();

            //获取每一个可能性
            String pross = prossMap.get(value);

            //如果可能性为0，则point为圈 叉分界点
            if ("0".equals(pross)) {
                point = i;
                break;
            }
        }


        ///-------------获取当前交易阶段和对应的可能性的值-----------------------
        //阶段图标
        TblTran tblTran = tblTranMapper.selectByPrimaryKey(tranId);

        //获取当前阶段
        String currentStage = tblTran.getStage();

        //根据key获取当前properties中 可能性(当前阶段)
        String currentPross = prossMap.get(currentStage);

        //判断当前阶段(可能性)是否为0，如果是，则表示前面7个标全是黑圈
        if ("0".equals(currentPross)) {
            for (int i = 0; i < dvList.size(); i++) {
                //在最后两个中判断，因为可能性是0才可进来

                ////阶段的每一个对象
                TblDicValue dicValue = dvList.get(i);
                //获取stage的Value值
                String stage = dicValue.getValue();
                //获取每一个可能性
                String pross = prossMap.get(stage);

                //判断后两个的值,是红叉黑叉还是黑圈
                if ("0".equals(pross)) {
                    //如果stage等于当前阶段，那么就为红叉              1.绿圈    2.绿锚点    3.黑圈   4.红叉   5.黑叉
                    if (stage.equals(currentStage)) {
                        Map<String,String> stageMap = new HashMap<String,String>();
                        stageMap.put("text",stage);              //阶段的文本信息
                        stageMap.put("location",i+"");           //location为坐标
                        stageMap.put("type","4");                //类型4代表红叉
                        resultList.add(stageMap);

                    }else {    //黑叉
                        Map<String,String> stageMap = new HashMap<String,String>();
                        stageMap.put("text",stage);
                        stageMap.put("location",i+"");
                        stageMap.put("type","5");
                        resultList.add(stageMap);
                    }


                    //前面7个  黑圈
                } else {
                    Map<String,String> stageMap = new HashMap<String,String>();
                    stageMap.put("text",stage);
                    stageMap.put("location",i+"");
                    stageMap.put("type","3");
                    resultList.add(stageMap);

                }
            }
        } else {   //非0的,前7个可能中部分可能是绿圈，可能是绿锚点，可能是黑圈，必定后两个是黑叉
            int index = 0;  //索引位置
            for (int i = 0; i < dvList.size(); i++) {

                ////阶段的每一个对象
                TblDicValue dicValue = dvList.get(i);
                //获取stage的Value值
                String stage = dicValue.getValue();
                //获取每一个可能性
                String pross = prossMap.get(stage);
                //如果当前可能性等于你获取的可能性，把循环的值赋给index索引
                if (currentPross.equals(pross)) {
                    index = i;
                    break;
                }
            }

            for (int i = 0; i < dvList.size(); i++) {
                ////阶段的每一个对象
                TblDicValue dicValue = dvList.get(i);
                //获取stage的Value值
                String stage = dicValue.getValue();
                //获取每一个可能性
                String pross = prossMap.get(stage);

                //如果i小于index说明在左边是绿圈
                if (i < index) {
                    //绿圈
                    Map<String,String> stageMap = new HashMap<String,String>();
                    stageMap.put("text",stage);
                    stageMap.put("location",i+"");
                    stageMap.put("type","1");
                    resultList.add(stageMap);


                } else if (i == index) {    //说明当前是被指向的绿圈锚点
                    Map<String,String> stageMap = new HashMap<String,String>();
                    stageMap.put("text",stage);
                    stageMap.put("location",i+"");
                    stageMap.put("type","2");
                    resultList.add(stageMap);


                } else if (i > index && i < point) {  //大于index小于point  说明在锚点右边，临界点左边，是黑圈
                    Map<String,String> stageMap = new HashMap<String,String>();
                    stageMap.put("text",stage);
                    stageMap.put("location",i+"");
                    stageMap.put("type","3");
                    resultList.add(stageMap);

                }else {  //其他情况则是黑叉
                    Map<String,String> stageMap = new HashMap<String,String>();
                    stageMap.put("text",stage);
                    stageMap.put("location",i+"");
                    stageMap.put("type","5");
                    resultList.add(stageMap);
                }
            }
        }
        returnObj.put("dvList",divList);// stage   value
        returnObj.put("point",point);
        returnObj.put("result",resultList);

        return  returnObj;
    }

    @Override
    public TblTran getAddHistory(TblTranHistory tblTranHistory) {
        String sysTime = DateTimeUtil.getSysTime();
        tblTranHistory.setCreatetime(sysTime);
        tblTranHistory.setId(UUIDUtil.getUUID());

        TblTran tblTran = new TblTran();
        tblTran.setEdittime(sysTime);
        tblTran.setEditby(tblTranHistory.getCreateby());
        tblTran.setStage(tblTranHistory.getStage());
        tblTran.setId(tblTranHistory.getTranid());  //由于下面updateByPrimaryKeySelective必须加此条

        try {
            tblTranMapper.updateByPrimaryKeySelective(tblTran);
            tblTranHistoryMapper.insert(tblTranHistory);
        } catch (Exception e) {
            throw new ResultException("新增失败");
        }
        TblTran tblTran2 = tblTranMapper.selectByPrimaryKey(tblTranHistory.getTranid());
        return tblTran2;
    }

    @Override
    public List<TblTranHistory> getTranHistory(Map mMap) {
        List<TblTranHistory> tblTranHistories = tblTranHistoryMapper.selectByExample(null);
        for (TblTranHistory tblTranHistory : tblTranHistories) {
            tblTranHistory.setPoss((String) mMap.get(tblTranHistory.getStage()));
        }

        return tblTranHistories;
    }

    //跳转到编辑页面显示获取详细信息
    @RequestMapping("/getEditMessage")
    public Map getEditMessage(String id,Map<String,String > prossMap){
        Map map = new HashMap();
        TblTran tblTran = tblTranMapper.selectByPrimaryKey(id);

        TblUser tblUser = tblUserMapper.selectByPrimaryKey(tblTran.getOwner());
        if(tblUser==null){
            throw new ResultException("未找到用户");
        }
        List<TblUser> tblUsers = tblUserMapper.selectByExample(null);
        if(tblUsers==null||tblUsers.size()==0){
            throw new ResultException("未找到用户集合");
        }
        List list = new ArrayList<>();
        for (TblUser user : tblUsers) {
            //把所有的owner放入一个集合
            list.add(user);
        }

        //获取stage对应的可能性
        String poss = prossMap.get(tblTran.getStage());

        //获取被选中的各个属性值

        String selectedStage = tblTran.getStage();
        String selectedSource = tblTran.getSource();
        String selectedType = tblTran.getType();

        TblCustomer tblCustomer = tblCustomerMapper.selectByPrimaryKey(tblTran.getCustomerid());
        tblTran.setCustomerid(tblCustomer.getName());


        //被选中的所有者
        map.put("userNameList",list);
        map.put("userName",tblUser.getName());

        //被选中stage值
        map.put("selectedstage",selectedStage);

        //被选中的source值
        map.put("selectedsource",selectedSource);

        //被选中的type值
        map.put("selectedsype",selectedType);

        //被选中的tbl对象
        map.put("selectedTblTran",tblTran);

      /*  //被选中对象stage属性对应的可能性
        map.put("poss",poss);*/

        return map;
    }


}
