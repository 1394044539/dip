package com.wpy.dips.service;

import com.wpy.dips.bean.Disaster;

import java.util.List;
import java.util.Map;

public interface DisasterService {
    Disaster findDisasterById(int id);

    List<Disaster> findAllDisaster();

    List<Disaster> findDisasterBySearch(Map<String, String> map);

    List<Disaster> findAllUserDisaster(int userid);

    Integer addDisaster(Disaster disaster);

    boolean addHot(int id);
}
