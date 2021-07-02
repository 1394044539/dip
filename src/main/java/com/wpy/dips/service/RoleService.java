package com.wpy.dips.service;

import com.wpy.dips.bean.Role;

import java.util.Set;

public interface RoleService {

    Set<String> findRoleListByUserId(int userid);

    boolean setRoleByUserId(int userid,int roleid);

    Role findRoleByUserId(int userid);

    Role findRoleInfoByRoleId(int roleid);

    boolean updateRole(int userid,int roleid);
}
