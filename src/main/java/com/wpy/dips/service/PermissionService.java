package com.wpy.dips.service;

import java.util.Set;

public interface PermissionService {

    Set<String> findPermissionListByUserId(int userid);
}
