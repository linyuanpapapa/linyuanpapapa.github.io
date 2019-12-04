package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImp1;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImp1 implements UserService {
    UserDaoImp1 userDaoImp1=new UserDaoImp1();
    /*
       注册功能的实现
     */
    @Override
    public boolean regist(User user) {
        //1、查看用户名是否可用
        User u = userDaoImp1.findByUsername(user.getUsername());
        if(u==null){
            //2、保存用户名
            //3、保存用户信息
            //3.1 设置激活码，唯一字符串
            user.setCode(UuidUtil.getUuid());
            //3.2 设置激活状态
            user.setStatus("N");
            userDaoImp1.saveUser(user);
            //3、激活邮件发送，邮件正文
            String content="<a href='http://localhost/travel/active?code="+user.getCode()+"'>点击激活【旅游网】</a>";
            MailUtils.sendMail(user.getEmail(),content,"激活邮件");
            return true;
        }else{
            return false;
        }
    }
    /*
        激活用户
     */
    @Override
    public boolean active(String code) {
        //1、通过code查找对应的用户
        User u=userDaoImp1.findByCode(code);
        //2、修改用户的Status
        if(u!=null){
            userDaoImp1.updateStatus(u);
            return true;
        }
        return false;
    }

    @Override
    public User login(User user) {
        User u=userDaoImp1.findByUsernameAndPassword(user.getUsername(),user.getPassword());
        return u;
    }
}
