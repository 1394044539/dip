package com.wpy.dips.service;

import com.wpy.dips.bean.User;

import java.util.List;

public interface UserService {

    User findUserByName(String username);

    User findUserByPhone(String phone);

    List<User> findAllUser();

    boolean addUser(User user);

    boolean updatePassword(User user);
}
