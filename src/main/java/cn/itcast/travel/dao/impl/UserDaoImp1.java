package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDaoImp1 implements UserDao {
    private JdbcTemplate jdbcTemplate=new JdbcTemplate(JDBCUtils.getDataSource());
    @Override
    public User findByUsername(String Username){
        User user=null;
        System.out.println("用户名"+Username);
        try{
            //1、定义sql语句
            String sql="select * from tab_user where username = ?";
            //2、执行sql
            user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), Username);
        }catch(Exception e){

        }
        return user;
    }

    @Override
    public void saveUser(User user) {
        try {
            //1、定义sql
            String sql="insert into tab_user(username,password,name,birthday,sex," +
                    "telephone,email,status,code) values (?,?,?,?,?,?,?,?,?)";
            //2、执行sql
            jdbcTemplate.update(sql,user.getUsername(),user.getPassword(),
                    user.getName(),user.getBirthday(),user.getSex(),user.getTelephone(),user.getEmail(),
            user.getStatus(),user.getCode());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
    /*
        根据激活码查找用户对象
     */
    @Override
    public User findByCode(String code) {
        User user=null;
        try {
            //1、定义sql
            String sql="select * from tab_user where code = ?";
            //2、执行sql
            user = jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),code);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return user;
    }
    /*
        修改指定用户激活状态
     */
    @Override
    public void updateStatus(User user) {
        String sql="update tab_user set status = 'Y' where username = ?";
        jdbcTemplate.update(sql,user.getUsername());
    }

    @Override
    public User findByUsernameAndPassword(String Username,String password) {
        User user=null;
        try{
            //1、定义sql语句
            String sql="select * from tab_user where username = ? and password = ?";
            //2、执行sql
            user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), Username,password);
        }catch(Exception e){

        }
        return user;
    }
}
