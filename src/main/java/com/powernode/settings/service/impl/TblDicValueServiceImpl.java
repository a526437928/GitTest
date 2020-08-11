package com.powernode.settings.service.impl;

import com.powernode.Exception.ResultException;
import com.powernode.settings.mapper.TblDicTypeMapper;
import com.powernode.settings.mapper.TblDicValueMapper;
import com.powernode.settings.pojo.TblDicType;
import com.powernode.settings.pojo.TblDicValue;
import com.powernode.settings.pojo.TblDicValueExample;
import com.powernode.settings.service.TblDicValueService;
import com.powernode.util.MyComparator;
import com.powernode.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Service
public class TblDicValueServiceImpl implements TblDicValueService {
    @Autowired
    private TblDicTypeMapper tblDicTypeMapper;
    @Autowired
    private TblDicValueMapper tblDicValueMapper;

    @Override
    public Map<String, Set<TblDicValue>> getItems() {

        //查询所有的字典类型数据
        Map<String, Set<TblDicValue>> divValueMap = new HashMap<>();
        List<TblDicType> tblDicTypes = tblDicTypeMapper.selectByExample(null);
        if (tblDicTypes == null) {
            throw new ResultException("未找到字典数据");
        }
        for (TblDicType tblDicType : tblDicTypes) {
            String code = tblDicType.getCode();
            TblDicValueExample tblDicValueExample = new TblDicValueExample();
            TblDicValueExample.Criteria criteria = tblDicValueExample.createCriteria();
            //根据code查询所有的dic_value中的数据
            criteria.andTypecodeEqualTo(code);
            List<TblDicValue> tblDicValues = tblDicValueMapper.selectByExample(tblDicValueExample);

            //初始化比较器
            MyComparator myComparator = new MyComparator();

            //转set集合
            Set<TblDicValue> dicSet = new TreeSet<>(myComparator);

            //添加进set集合
            dicSet.addAll(tblDicValues);


            if (tblDicValues == null) {
                throw new ResultException("未找到字典数据");
            }
            //把数据存入Map集合  code和code对应的所有div_value表中的数据
            divValueMap.put(code, dicSet);

        }
        return divValueMap;

    }

}
