package com.powernode.settings.service;

import com.powernode.settings.pojo.TblDicValue;

import java.util.Map;
import java.util.Set;

public interface TblDicValueService {
    Map<String, Set<TblDicValue>> getItems();

}
