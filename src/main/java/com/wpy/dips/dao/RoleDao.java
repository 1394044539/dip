package com.wpy.dips.dao;

import com.wpy.dips.bean.Role;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

public interface RoleDao {

    //根据用户查询用户的角色列表
    Set<String> findRoleListByUserId(int userid);

    boolean setRoleByUserId(@Param("userid") int userid, @Param("roleid") int roleid);

    Role findRoleByUserId(int userid);

    Role findRoleInfoByRoleId(int roleid);

    boolean updateRole(@Param("userid")int userid,@Param("roleid")int roleid);
}
