package com.wpy.dips.dao;

import java.util.Set;

public interface PermissionDao {

    Set<String> findPermissionListByUserId(int userid);
}
