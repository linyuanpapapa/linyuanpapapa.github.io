package cn.itcast.travel.service;

import cn.itcast.travel.domain.User;

public interface UserService {
    /*
      注册功能的实现
    */
    boolean regist(User user);

    boolean active(String code);

    User login(User user);
}
