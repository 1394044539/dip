package com.wpy.dips.dao;

import com.wpy.dips.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {
    User findUserByName(String username);

    boolean addUser(User user);

    List<User> findAllUser();

    User findUserByPhone(String phone);

    boolean updatePassword(User user);
}
