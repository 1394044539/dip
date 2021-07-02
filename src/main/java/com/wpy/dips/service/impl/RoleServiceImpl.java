package com.wpy.dips.service.impl;

import com.wpy.dips.bean.Role;
import com.wpy.dips.dao.RoleDao;
import com.wpy.dips.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class RoleServiceImpl implements RoleService
{
    @Autowired
    RoleDao roleDao;

    @Override
    public Set<String> findRoleListByUserId(int userid) {
        return roleDao.findRoleListByUserId(userid);
    }

    @Override
    public boolean setRoleByUserId(int userid,int roleid) {
        return roleDao.setRoleByUserId(userid, roleid);
    }

    @Override
    public Role findRoleByUserId(int userid) {
        return roleDao.findRoleByUserId(userid);
    }

    @Override
    public Role findRoleInfoByRoleId(int roleid) {
        return roleDao.findRoleInfoByRoleId(roleid);
    }

    @Override
    public boolean updateRole(int userid, int roleid) {
        return roleDao.updateRole(userid, roleid);
    }
}
