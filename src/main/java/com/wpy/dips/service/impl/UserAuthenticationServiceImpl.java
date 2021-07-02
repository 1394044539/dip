package com.wpy.dips.service.impl;

import com.wpy.dips.bean.UserAuthentication;
import com.wpy.dips.dao.UserAuthenticationDao;
import com.wpy.dips.service.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    @Autowired
    UserAuthenticationDao userAuthenticationDao;

    @Override
    public Integer addAu(UserAuthentication userAuthentication) {
        return userAuthenticationDao.addAu(userAuthentication);
    }

    @Override
    public UserAuthentication findUserAuInfo(int userid) {
        return userAuthenticationDao.findUserAuInfo(userid);
    }

    @Override
    public List<UserAuthentication> findAllAu(String isPass) {
        return userAuthenticationDao.findAllAu(isPass);
    }

    @Override
    public boolean updataIsPass(int userid, int roleid ,String isPass) {
        return userAuthenticationDao.updataIsPass(userid,roleid,isPass);
    }
}
