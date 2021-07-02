package com.wpy.dips.dao;

import com.wpy.dips.bean.DisData;

import java.util.List;

public interface DisDataDao {

    int addData(DisData disData);

    List<DisData> findDisData(int id);

    int updateDisData(DisData disData);

    int deleteDisData(int id);
}
