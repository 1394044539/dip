package com.wpy.dips.dao;

import com.wpy.dips.bean.UserAuthentication;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserAuthenticationDao {

    Integer addAu(UserAuthentication userAuthentication);

    List<UserAuthentication> findAllAu(String isPass);

    UserAuthentication findUserAuInfo(int userid);

    boolean updataIsPass(@Param("userid") int userid,@Param("roleid") int roleid ,@Param("isPass") String isPass);
}
