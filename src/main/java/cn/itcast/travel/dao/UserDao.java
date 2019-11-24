package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

public interface UserDao {
    User findByUsername(String Username);
    void saveUser(User user);

    User findByCode(String code);

    void updateStatus(User user);

    User findByUsernameAndPassword(String Username,String password);
}
