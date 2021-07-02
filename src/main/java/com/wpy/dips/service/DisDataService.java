package com.wpy.dips.service;

import com.wpy.dips.bean.DisData;
import org.springframework.stereotype.Service;

import java.util.List;


public interface DisDataService {

    int addData(DisData disData);

    List<DisData> findDisData(int id);

    int updateDisData(DisData disData);

    int deleteDisData(int id);
}
