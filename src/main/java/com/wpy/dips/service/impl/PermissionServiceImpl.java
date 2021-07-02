package com.wpy.dips.service.impl;

import com.wpy.dips.dao.PermissionDao;
import com.wpy.dips.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    PermissionDao permissionDao;

    @Override
    public Set<String> findPermissionListByUserId(int userid) {
        return permissionDao.findPermissionListByUserId(userid);
    }
}
