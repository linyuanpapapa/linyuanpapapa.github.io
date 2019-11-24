package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImp1;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImp1 implements CategoryService {
    private CategoryDao categoryDao=new CategoryDaoImp1();
    @Override
    public List<Category> findAll() {
        //1、从redis中查询
        //1.1 获取jedis客户端
        Jedis jedis= JedisUtil.getJedis();
        //1.2 可使用sortedset排序查询
        //Set<String> categorys = jedis.zrange("category", 0, -1);
        //1.3 查询sortedset中的分数（cid）和值（cname）
        Set<Tuple> categorys = jedis.zrangeWithScores("category", 0, -1);
        List<Category> cs=null;
        //2、判断查询的集合是否为空
        if(categorys==null || categorys.size()==0){
            //2.1 如果为空，需要从数据库查询并存入redis
            cs=categoryDao.findAll();
            for (int i = 0; i < cs.size(); i++) {
                jedis.zadd("category",cs.get(i).getCid(),cs.get(i).getCname());
            }
            //注意set的存取方式
        }else{
            //2.2 如果不为空，将set的数据存入list，因为方法返回的是List，所以需要转换
            cs=new ArrayList<Category>();
            for (Tuple tuple: categorys) {
                Category category=new Category();
                category.setCname(tuple.getElement());
                category.setCid((int)tuple.getScore());
                cs.add(category);
            }
        }
        return cs;
    }
}
