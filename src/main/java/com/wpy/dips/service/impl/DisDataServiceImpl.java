package com.wpy.dips.service.impl;

import com.wpy.dips.bean.DisData;
import com.wpy.dips.dao.DisDataDao;
import com.wpy.dips.service.DisDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisDataServiceImpl implements DisDataService {

    @Autowired
    DisDataDao disDataDao;

    @Override
    public int addData(DisData disData) {
        return disDataDao.addData(disData);
    }

    @Override
    public List<DisData> findDisData(int id) {
        return disDataDao.findDisData(id);
    }

    @Override
    public int updateDisData(DisData disData) {
        return disDataDao.updateDisData(disData);
    }

    @Override
    public int deleteDisData(int id) {
        return disDataDao.deleteDisData(id);
    }
}
