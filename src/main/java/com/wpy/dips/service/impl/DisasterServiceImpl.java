package com.wpy.dips.service.impl;

import com.wpy.dips.bean.Disaster;
import com.wpy.dips.dao.DisasterDao;
import com.wpy.dips.service.DisasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DisasterServiceImpl implements DisasterService {

    @Autowired
    DisasterDao disasterDao;

    @Override
    public Disaster findDisasterById(int id) {
        return disasterDao.findDisasterById(id);
    }

    @Override
    public List<Disaster> findAllDisaster() {
        return disasterDao.findAllDisaster();
    }

    @Override
    public List<Disaster> findDisasterBySearch(Map<String,String> map) {
        return disasterDao.findDisasterBySearch(map);
    }

    @Override
    public List<Disaster> findAllUserDisaster(int userid) {
        return disasterDao.findAllUserDisaster(userid);
    }

    @Override
    public Integer addDisaster(Disaster disaster) {
        return disasterDao.addDisaster(disaster);
    }

    @Override
    public boolean addHot(int id){ return disasterDao.addHot(id); }
}
