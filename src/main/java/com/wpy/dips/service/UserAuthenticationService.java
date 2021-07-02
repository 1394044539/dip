package com.wpy.dips.service;

import com.wpy.dips.bean.UserAuthentication;

import java.util.List;

public interface UserAuthenticationService {

    public Integer addAu(UserAuthentication userAuthentication);

    UserAuthentication findUserAuInfo(int userid);

    List<UserAuthentication> findAllAu(String isPass);

    boolean updataIsPass(int userid, int roleid, String isPass);
}
