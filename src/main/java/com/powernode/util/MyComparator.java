package com.powernode.util;

import com.powernode.settings.pojo.TblDicValue;

import java.util.Comparator;


//自定义比较器
public class MyComparator implements Comparator<TblDicValue> {
    @Override
    public int compare(TblDicValue o1, TblDicValue o2) {
        //将orderno字段转成int类型比较
        int a = Integer.parseInt(o1.getOrderno());

        int b = Integer.parseInt(o2.getOrderno());

        return a-b;
    }
}
