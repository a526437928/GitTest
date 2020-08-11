package com.powernode.workbench.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.powernode.Exception.ResultException;
import com.powernode.settings.mapper.TblUserMapper;
import com.powernode.settings.pojo.TblUser;
import com.powernode.settings.pojo.TblUserExample;
import com.powernode.util.*;
import com.powernode.workbench.mapper.*;
import com.powernode.workbench.pojo.*;
import com.powernode.workbench.service.TblClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Service
public class TblClueServiceImpl implements TblClueService {
    @Autowired
    private TblClueMapper tblClueMapper;

    @Autowired
    private TblUserMapper tblUserMapper;


    @Autowired
    private TblClueActivityRelationMapper tblClueActivityRelationMapper;


    @Autowired
    private TblActivityMapper tblActivityMapper;

    @Autowired
    private TblContactsMapper tblContactsMapper;

    @Autowired
    private TblCustomerMapper tblCustomerMapper;

    @Autowired
    private TblTranMapper tblTranMapper;

    @Autowired
    private TblContactsRemarkMapper tblContactsRemarkMapper;

    @Autowired
    private TblClueRemarkMapper tblClueRemarkMapper;

    @Autowired
    private TblCustomerRemarkMapper tblCustomerRemarkMapper;

    @Autowired
    private TblTranHistoryMapper tblTranHistoryMapper;

    @Autowired
    private TblTranRemarkMapper tblTranRemarkMapper;

    @Autowired
    private TblContactsActivityRelationMapper tblContactsActivityRelationMapper;


    //获取所有clue的数据
    @Override
    public List getItems() {
        List<TblClue> tblClues = tblClueMapper.selectByExample(null);
        if (tblClues == null || tblClues.size() == 0) {
            throw new ResultException("未找到数据");
        }
        for (TblClue tblClue : tblClues) {
            TblUser tblUser = tblUserMapper.selectByPrimaryKey(tblClue.getOwner());
            if (tblUser != null) {
                tblClue.setOwner(tblUser.getName());
            }
        }
        return tblClues;
    }

    @Override
    public List<TblUser> getUserItems() {
        List<TblUser> tblUsers = tblUserMapper.selectByExample(null);
        if (tblUsers == null) {
            throw new ResultException("未找到数据");
        }
        return tblUsers;
    }

    @Override
    public void addClueSave(TblClue tblClue) {

        //线索对象补全属性值
        String sysTime = DateTimeUtil.getSysTime();
        tblClue.setCreatetime(sysTime);
        String uuid = UUIDUtil.getUUID();
        tblClue.setId(uuid);

        try {
            tblClueMapper.insertSelective(tblClue);
        } catch (Exception e) {

            throw new ResultException("添加失败");
        }
    }


    ////获取详情页面的信息(跳转过后的页面)
    @Override
    public TblClue getDetail(String id) {
        TblClue tblClue = tblClueMapper.selectByPrimaryKey(id);

        TblUser tblUser = tblUserMapper.selectByPrimaryKey(tblClue.getOwner());
        tblClue.setOwner(tblUser.getName());
        if (tblClue == null) {
            throw new ResultException("未找到数据");
        }
        return tblClue;
    }


    //获取市场活动
    @Override
    public List getByActivity(String clueId) {
        //根据市场ID 查询中间表

        //创建离线检索对象,根据线索ID找到所有的中间表数据
        TblClueActivityRelationExample tblClueActivityRelationExample = new TblClueActivityRelationExample();
        TblClueActivityRelationExample.Criteria criteria = tblClueActivityRelationExample.createCriteria();
        criteria.andClueidEqualTo(clueId);
        List<TblClueActivityRelation> tblClueActivityRelations = tblClueActivityRelationMapper.selectByExample(tblClueActivityRelationExample);
        if (tblClueActivityRelations == null || tblClueActivityRelations.size() == 0) {
            throw new ResultException("未找到关联关系信息");
        }

        //作用就是把map集合放入
        List activitys = new ArrayList();
        for (TblClueActivityRelation tblClueActivityRelation : tblClueActivityRelations) {

            //作用就是把一张表的ID和另一张表的对象放入此集合中
            Map hashmapp = new HashMap();


            //根据中间表的市场活动外键(activityId)拿到市场活动信息
            TblActivity tblActivity = tblActivityMapper.selectByPrimaryKey(tblClueActivityRelation.getActivityid());

            //把MD5转成文字
            TblUser tblUser = tblUserMapper.selectByPrimaryKey(tblActivity.getOwner());
            tblActivity.setOwner(tblUser.getName());

            //将tblClueActivityRelation的ID放入map集合
            hashmapp.put("acid", tblClueActivityRelation.getId());

            // 将TblActivity的对象放入map集合
            hashmapp.put("activity", tblActivity);

            //将Map放入List集合
            activitys.add(hashmapp);
        }


        return activitys;

    }


    //解除关联关系
    @Override
    public void dell(String id) {
        try {
            tblClueActivityRelationMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            throw new ResultException("解除关联关系失败");
        }
    }

    @Override
    public List getActivitys(String name) {

        //获取activity的离线检索对象
        TblActivityExample tblActivityExample = new TblActivityExample();
        TblActivityExample.Criteria criteria = tblActivityExample.createCriteria();
        if (name != null) {
            //不为空时根据名称模糊查询
            criteria.andNameLike("%" + name + "%");
        }
        List<TblActivity> tblActivities = tblActivityMapper.selectByExample(tblActivityExample);

        for (TblActivity tblActivity : tblActivities) {
            TblUser tblUser = tblUserMapper.selectByPrimaryKey(tblActivity.getOwner());
            tblActivity.setOwner(tblUser.getName());
        }
        if (tblActivities == null || tblActivities.size() == 0) {
            throw new ResultException("未找到数据");
        }


        return tblActivities;
    }


    @Override
    public void relationActivitySavee(String clueId, String[] ids) {
        if (ids != null && ids.length != 0) {
            try {
                for (String id : ids) {
                    TblClueActivityRelation tblClueActivityRelation = new TblClueActivityRelation();
                    tblClueActivityRelation.setId(UUIDUtil.getUUID());
                    //线索id
                    tblClueActivityRelation.setClueid(clueId);
                    //每次循环的ID
                    tblClueActivityRelation.setActivityid(id);
                    tblClueActivityRelationMapper.insert(tblClueActivityRelation);
                }
            } catch (Exception e) {
                throw new ResultException("插入数据失败");
            }
        }
    }

    @Override
    public List<TblActivity> relationactivity(String id, String search) {
        //根据线索和市场的关联关系查询出所有的市场活动

        //根据ClueId查询出所有的关联关系
        TblClueActivityRelationExample tblClueActivityRelationExample = new TblClueActivityRelationExample();
        TblClueActivityRelationExample.Criteria criteria = tblClueActivityRelationExample.createCriteria();
        criteria.andClueidEqualTo(id);
        List<TblClueActivityRelation> tblClueActivityRelations = tblClueActivityRelationMapper.selectByExample(tblClueActivityRelationExample);
        if (tblClueActivityRelations == null || tblClueActivityRelations.size() == 0) {
            throw new ResultException(ResultEnum.NOTFOUND_DATA);  //未找到数据
        }


        List ids = new ArrayList();
        //根据查询出来的所有关联关系的Id查找市场活动
        for (TblClueActivityRelation tblClueActivityRelation : tblClueActivityRelations) {
            //把所有的关联关系的表的activityId放入集合内
            ids.add(tblClueActivityRelation.getActivityid());

        }

        TblActivityExample tblActivityExample = new TblActivityExample();
        TblActivityExample.Criteria criteria1 = tblActivityExample.createCriteria();

        //这里只能用andIdIn因为equalTo不能是集合
        criteria1.andIdIn(ids);

        if (search != null && !"".equals(search)) {
            criteria1.andNameLike("%" + search + "%");
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

    @Override
    public void convert(String clueId, String money, String tran, String exdate, String stage, String activityId, String createBy, String tranStage) {
        String sysTime = DateTimeUtil.getSysTime();
        //获取线索信息
        TblClue tblClue = tblClueMapper.selectByPrimaryKey(clueId);
        if (tblClue == null) {
            throw new ResultException("线索已丢失!");
        }

        //根据公司名称去检索客户是否存在，存在则获取，不存在则新建
        TblCustomerExample tblCustomerExample = new TblCustomerExample();
        TblCustomerExample.Criteria criteria = tblCustomerExample.createCriteria();
        //根据公司名称查询，因为公司名称具有唯一性
        criteria.andNameEqualTo(tblClue.getCompany());
        List<TblCustomer> tblCustomers = tblCustomerMapper.selectByExample(tblCustomerExample);

        //判断客户是否存在存在，如果不存在则新建客户
        TblCustomer tblCustomer = null;
        if (tblCustomers.size() == 0 || tblCustomers == null) {
            tblCustomer = new TblCustomer();
            tblCustomer.setId(UUIDUtil.getUUID());
            tblCustomer.setOwner(tblClue.getOwner());
            tblCustomer.setName(tblClue.getCompany());
            tblCustomer.setWebsite(tblClue.getWebsite());
            tblCustomer.setPhone(tblClue.getPhone());
            tblCustomer.setCreateby(createBy);
            tblCustomer.setCreatetime(sysTime);
            tblCustomer.setContactsummary(tblClue.getContactsummary());
            tblCustomer.setNextcontacttime(tblClue.getNextcontacttime());
            tblCustomer.setDescription(tblClue.getDescription());
            tblCustomer.setAddress(tblClue.getAddress());
            tblCustomerMapper.insert(tblCustomer);
        } else {
            //如果存在则从List中提取出来
            tblCustomer = tblCustomers.get(0);
        }

        //创建联系人
        TblContacts tblContacts = new TblContacts();
        tblContacts.setId(UUIDUtil.getUUID());
        tblContacts.setOwner(tblClue.getOwner());
        tblContacts.setSource(tblClue.getSource());
        tblContacts.setCustomerid(tblCustomer.getId());
        tblContacts.setFullname(tblClue.getFullname());
        tblContacts.setAppellation(tblClue.getAppellation());
        tblContacts.setEmail(tblClue.getEmail());
        tblContacts.setMphone(tblClue.getMphone());
        tblContacts.setJob(tblClue.getJob());
        tblContacts.setCreateby(createBy);
        tblContacts.setCreatetime(sysTime);
        tblContacts.setDescription(tblClue.getDescription());
        tblContacts.setContactsummary(tblClue.getContactsummary());
        tblContacts.setNextcontacttime(tblClue.getNextcontacttime());
        tblContacts.setAddress(tblClue.getAddress());
        tblContactsMapper.insertSelective(tblContacts);


        //把线索备注提取到客户备注和联系人备注中取去
        TblClueRemarkExample tblClueRemarkExample = new TblClueRemarkExample();
        TblClueRemarkExample.Criteria criteria1 = tblClueRemarkExample.createCriteria();
        criteria1.andClueidEqualTo(clueId);
        //根据线索ID，查询线索备注信息
        List<TblClueRemark> tblClueRemarks = tblClueRemarkMapper.selectByExample(tblClueRemarkExample);

        if (tblClueRemarks != null) {
            //再把所有的线索备注信息提取到客户备注和联系人备注中

            for (TblClueRemark tblClueRemark : tblClueRemarks) {
                //客户备注
                TblCustomerRemark tblCustomerRemark = new TblCustomerRemark();
                tblCustomerRemark.setId(UUIDUtil.getUUID());
                tblCustomerRemark.setNotecontent(tblClueRemark.getNotecontent());
                tblCustomerRemark.setCreateby(createBy);
                tblCustomerRemark.setCreatetime(sysTime);
                //0代表未编辑，1代表已编辑
                tblCustomerRemark.setEditflag("0");
                tblCustomerRemark.setCustomerid(tblCustomer.getId());
                tblCustomerRemarkMapper.insert(tblCustomerRemark);

                //联系人备注
                TblContactsRemark tblContactsRemark = new TblContactsRemark();
                tblContactsRemark.setId(UUIDUtil.getUUID());
                tblContactsRemark.setNotecontent(tblClueRemark.getNotecontent());
                tblContactsRemark.setCreateby(createBy);
                tblContactsRemark.setCreatetime(sysTime);
                //0代表未修改，1代表已修改
                tblContactsRemark.setEditflag("0");
                tblContactsRemark.setContactsid(tblContacts.getId());
                tblContactsRemarkMapper.insert(tblContactsRemark);
            }
        }

        //把线索和市场关联关系转到联系人和市场的关联关系
        TblClueActivityRelationExample tblClueActivityRelationExample = new TblClueActivityRelationExample();
        TblClueActivityRelationExample.Criteria criteria2 = tblClueActivityRelationExample.createCriteria();
        //根据clueId找到所有的关联关系
        criteria2.andClueidEqualTo(clueId);
        List<TblClueActivityRelation> tblClueActivityRelations = tblClueActivityRelationMapper.selectByExample(tblClueActivityRelationExample);
        if (tblClueActivityRelations != null) {


            for (TblClueActivityRelation tblClueActivityRelation : tblClueActivityRelations) {
                TblContactsActivityRelation tblContactsActivityRelation = new TblContactsActivityRelation();
                tblContactsActivityRelation.setActivityid(tblClueActivityRelation.getActivityid());
                tblContactsActivityRelation.setContactsid(tblContacts.getId());
                tblContactsActivityRelation.setId(UUIDUtil.getUUID());
                tblContactsActivityRelationMapper.insert(tblContactsActivityRelation);
            }
        }

        //交易信息,如果点击了“为用户创建交易”，则会走此条。
        if ("1".equals(tranStage)) {
            if ((money == null || "".equals(money)) || (tran == null || "".equals(tran)) || (exdate == null || "".equals(exdate)) || (stage == null || "".equals(stage)) || (activityId == null || "".equals(activityId))) {
                throw new ResultException("交易信息不完善！");
            }

            //创建交易
            TblTran tblTran = new TblTran();
            tblTran.setId(UUIDUtil.getUUID());
            tblTran.setActivityid(activityId);
            tblTran.setMoney(money);
            tblTran.setContactsid(tblContacts.getId());
            tblTran.setCustomerid(tblCustomer.getId());
            tblTran.setCreateby(createBy);
            tblTran.setCreatetime(sysTime);
            tblTran.setContactsummary(tblClue.getContactsummary());
            tblTran.setNextcontacttime(tblClue.getNextcontacttime());
            tblTran.setDescription(tblClue.getDescription());
            tblTran.setSource(tblClue.getSource());
            tblTran.setExpecteddate(exdate);
            tblTran.setName(tran);
            tblTran.setStage(stage);
            tblTran.setOwner(tblClue.getOwner());

            /*tblTran.setType();*/  //交易类型
            tblTranMapper.insert(tblTran);


            //交易历史
            TblTranHistory tblTranHistory = new TblTranHistory();
            tblTranHistory.setId(UUIDUtil.getUUID());
            tblTranHistory.setStage(tblTran.getStage());
            tblTranHistory.setMoney(tblTran.getMoney());
            tblTranHistory.setCreateby(tblTran.getCreateby());
            tblTranHistory.setCreatetime(tblTran.getCreatetime());
            tblTranHistory.setTranid(tblTran.getId());
            tblTranHistory.setExpecteddate(tblTran.getExpecteddate());
            tblTranHistoryMapper.insert(tblTranHistory);

        }
        //删除线索备注信息
        TblClueRemarkExample tblClueRemarkExample1 = new TblClueRemarkExample();
        TblClueRemarkExample.Criteria criteria3 = tblClueRemarkExample1.createCriteria();
        criteria3.andClueidEqualTo(clueId);
        tblClueRemarkMapper.deleteByExample(tblClueRemarkExample1);

        //删除线索和市场活动信息
        TblClueActivityRelationExample tblClueActivityRelationExample1 = new TblClueActivityRelationExample();
        TblClueActivityRelationExample.Criteria criteria4 = tblClueActivityRelationExample1.createCriteria();
        criteria4.andClueidEqualTo(clueId);
        tblClueActivityRelationMapper.deleteByExample(tblClueActivityRelationExample1);

        //删除线索记录
        tblClueMapper.deleteByPrimaryKey(clueId);
    }


    //分页查询
    @Override
    public PageResult selectPageList(int pageSize, int pageNo, String fullname, String company, String phone, String mphone, String source, String owner, String state) {

        //开启分页
        PageHelper.startPage(pageNo, pageSize);

        //模糊查询
        TblClueExample tblClueExample = new TblClueExample();
        TblClueExample.Criteria criteria = tblClueExample.createCriteria();

        if (fullname != null && !"".equals(fullname)) {
            criteria.andFullnameLike("%" + fullname + "%");
        }

        List list = new ArrayList();
        if (owner != null && !"".equals(owner)) {
            //创建user离线检索对象
            TblUserExample tblUserExample = new TblUserExample();
            TblUserExample.Criteria criteria1 = tblUserExample.createCriteria();

            //根据传过来的owner查询user表
            criteria1.andNameLike("%" + owner + "%");

            //将查询出来的所有user的id属性循环放入集合
            List<TblUser> tblUsers = tblUserMapper.selectByExample(tblUserExample);
            for (TblUser tblUser : tblUsers) {
                list.add(tblUser.getId());
            }
            //clue根据所有user的id查询
            criteria.andOwnerIn(list);

        }

        if (state != null && !"".equals(state)) {
            criteria.andStateLike("%" + state + "%");
        }

        if (company != null & !"".equals(company)) {
            criteria.andCompanyLike("%" + company + "%");
        }


        if (source != null && !"".equals(source)) {
            criteria.andSourceLike("%" + source + "%");
        }

        if (mphone != null && !"".equals(mphone)) {
            criteria.andMphoneLike("%" + mphone + "%");

        }

        if (phone != null && !"".equals(phone)) {
            criteria.andPhoneLike("%" + phone + "%");
        }

        //查询
        List<TblClue> tblClues = tblClueMapper.selectByExample(tblClueExample);
        if (tblClues == null || tblClues.size() == 0) {
            throw new ResultException("未找到数据!");
        }

        /*根据owner查询user，拿到user对象的name属性给owner赋值*/
        for (TblClue tblClue : tblClues) {
            TblUser tblUser = tblUserMapper.selectByPrimaryKey(tblClue.getOwner());
            tblClue.setOwner(tblUser.getName());

        }

        //封装list到PageInfo对象中自动分页
        PageInfo<TblClue> pageInfo = new PageInfo<>(tblClues);
        PageResult pageResult = new PageResult(pageInfo.getTotal(), pageInfo.getList());
        return pageResult;
    }

}
