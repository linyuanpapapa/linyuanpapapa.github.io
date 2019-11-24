package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImp1;
import cn.itcast.travel.dao.impl.RouteImgDaoImp1;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImp1 implements RouteService {
    private RouteDao routeDao=new RouteDaoImp1();
    private RouteImgDao routeImgDao=new RouteImgDaoImp1();
    private SellerDao sellerDao=new SellerDaoImpl();
    private FavoriteDao favoriteDao=new FavoriteDaoImpl();
    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize,String rname) {
        //封装PageBean对象
        PageBean<Route> pb=new PageBean<>();
        //设置当前页码
        pb.setCurrentPage(currentPage);
        //设置每页显示条数
        pb.setPageSize(pageSize);
        //设置总记录数
        int totalCount=routeDao.findTotalCount(cid,rname);
        pb.setTotalCount(totalCount);
        //设置当前页码显示的数据List集合
        int start=(currentPage-1)*pageSize;//开始的记录数
        List<Route> list=routeDao.findByPage(cid,start,pageSize,rname);
        pb.setList(list);
        //设置总页数
        int totalPage=totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        pb.setTotalPage(totalPage);
        return pb;
    }

    @Override
    public Route findOne(String rid) {
        //1、根据id去查询route表中的route对象
        Route route=routeDao.findOne(Integer.parseInt(rid));
        //2、根据route的id查询图片集合信息
        List<RouteImg> list = routeImgDao.findByRid(route.getRid());
        route.setRouteImgList(list);
        //3、根据route的sid（商家id）查询商家对象
        Seller seller=sellerDao.findById(route.getSid());
        route.setSeller(seller);
        //4、获取线路收藏次数
        int count=favoriteDao.findCountByRid(route.getRid());
        route.setCount(count);
        return route;
    }
}
