package com.powernode.workbench.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.powernode.Exception.ResultException;
import com.powernode.settings.mapper.TblUserMapper;
import com.powernode.settings.pojo.TblUser;
import com.powernode.settings.pojo.TblUserExample;
import com.powernode.util.DateTimeUtil;
import com.powernode.util.PageResult;
import com.powernode.util.UUIDUtil;
import com.powernode.workbench.mapper.TblContactsMapper;
import com.powernode.workbench.mapper.TblContactsRemarkMapper;
import com.powernode.workbench.mapper.TblCustomerMapper;
import com.powernode.workbench.mapper.TblCustomerRemarkMapper;
import com.powernode.workbench.pojo.*;
import com.powernode.workbench.service.TblCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TblCustomerServiceImpl implements TblCustomerService {
    @Autowired
    TblUserMapper tblUserMapper;

    @Autowired
    TblCustomerMapper tblCustomerMapper;

    @Autowired
    TblCustomerRemarkMapper tblCustomerRemarkMapper;

    @Autowired
    TblContactsMapper tblContactsMapper;

    @Autowired
    TblContactsRemarkMapper tblContactsRemarkMapper;

    @Override
    public Map getUsers() {
        Map map = new HashMap();
        List<TblCustomer> tblCustomers = tblCustomerMapper.selectByExample(null);
        if (tblCustomers.size() == 0 || tblCustomers == null) {
            throw new ResultException("未找到客户数据");
        }
        List<TblUser> tblUsers = tblUserMapper.selectByExample(null);
        if (tblUsers == null || tblUsers.size() == 0) {
            throw new ResultException("未找到用户");
        }

        map.put("tblcustomers",tblCustomers);
        map.put("tblusers",tblUsers);

        return map;
    }


    //添加客户
    @Override
    public void addCustomer(TblCustomer tblCustomer,String createBy) {
        String sysTime = DateTimeUtil.getSysTime();
        //补全属性
        tblCustomer.setId(UUIDUtil.getUUID());
        tblCustomer.setCreatetime(sysTime);
        tblCustomer.setCreateby(createBy);
        try {
            tblCustomerMapper.insert(tblCustomer);
        } catch (Exception e) {
            throw new ResultException("插入失败");
        }

        //创建customer备注
        TblCustomerRemark tblCustomerRemark = new TblCustomerRemark();
        tblCustomerRemark.setId(UUIDUtil.getUUID());
        tblCustomerRemark.setCreateby(createBy);
        tblCustomerRemark.setCreatetime(sysTime);

        //0代表未编辑，1代表已编辑
        tblCustomerRemark.setEditflag("0");
        tblCustomerRemark.setCustomerid(tblCustomer.getId());
        tblCustomerRemarkMapper.insertSelective(tblCustomerRemark);

        //创建联系人
        TblContacts tblContacts = new TblContacts();
        tblContacts.setId(UUIDUtil.getUUID());
        tblContacts.setOwner(tblCustomer.getOwner());
        tblContacts.setCustomerid(tblCustomer.getId());
        tblContacts.setCreateby(createBy);
        tblContacts.setCreatetime(sysTime);
        tblContacts.setDescription(tblCustomer.getDescription());
        tblContacts.setContactsummary(tblCustomer.getContactsummary());
        tblContacts.setNextcontacttime(tblCustomer.getNextcontacttime());
        tblContacts.setAddress(tblCustomer.getAddress());
        tblContactsMapper.insertSelective(tblContacts);

        //创建联系人备注
        TblContactsRemark tblContactsRemark = new TblContactsRemark();
        tblContactsRemark.setCreatetime(sysTime);
        tblContactsRemark.setCreateby(createBy);
        tblContactsRemark.setId(UUIDUtil.getUUID());
        tblContactsRemark.setEditflag("0");
        tblContactsRemark.setContactsid(tblContacts.getId());
        tblContactsRemarkMapper.insertSelective(tblContactsRemark);
    }


    //分页查询
    @Override
    public PageResult selectPageList(int pageSize, int pageNo, String name, String owner, String phone, String website) {
        /*Pagehelper.startPage(pageNo,pageSize);*/



        TblCustomerExample tblCustomerExample = new TblCustomerExample();
        TblCustomerExample.Criteria criteria = tblCustomerExample.createCriteria();


        if (name != null && !"".equals(name)) {

            criteria.andNameLike("%" + name + "%");
        }

        List list = new ArrayList();
        if (owner != null && !"".equals(owner)) {
            //创建user离线检索对象
            TblUserExample tblUserExample = new TblUserExample();
            TblUserExample.Criteria criteria1 = tblUserExample.createCriteria();

            //根据传过来的owner（中文）查询user表
            criteria1.andNameLike("%" + owner + "%");

            //将查询出来的所有user的id属性循环放入集合

            List<TblUser> tblUsers = tblUserMapper.selectByExample(tblUserExample);
            for (TblUser tblUser : tblUsers) {
                list.add(tblUser.getId());
            }
            //activity根据所有user的id查询
            criteria.andOwnerIn(list);
        }

        //开启分页,在此分页，owner不会出现查询分页的问题
        PageHelper.startPage(pageNo, pageSize);

        if (phone != null && !"".equals(phone)) {
            criteria.andPhoneLike("%" + phone + "%");
        }
        if (website != null && !"".equals(website)) {
            criteria.andWebsiteLike("%" + website + "%");
        }

        /*查询所有*/
        List<TblCustomer> tblCustomers = tblCustomerMapper.selectByExample(tblCustomerExample);
        if (tblCustomers == null || tblCustomers.size() == 0) {
            throw new ResultException("未找到数据");
        }

        /*根据owner查询user，拿到user对象的name属性给owner赋值*/
        for (TblCustomer tblCustomer : tblCustomers) {
            TblUser tblUser = tblUserMapper.selectByPrimaryKey(tblCustomer.getOwner());
            tblCustomer.setOwner(tblUser.getName());
        }

        //封装list到PageInfo对象中自动分页
        PageInfo<TblCustomer> pageInfo = new PageInfo<>(tblCustomers);
        PageResult pageResult = new PageResult(pageInfo.getTotal(), pageInfo.getList());
        return pageResult;
    }




}
