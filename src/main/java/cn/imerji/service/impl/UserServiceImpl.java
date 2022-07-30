package cn.imerji.service.impl;

import cn.imerji.mapper.UserMapper;
import cn.imerji.pojo.User;
import cn.imerji.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class UserServiceImpl implements cn.imerji.service.UserService {
    // 创建 SqlSessionFactory 工厂对象
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();
    /**
     * 用户登录 -邮箱的方式
     * @param email    邮箱
     * @param password 密码
     * @return 用户
     */
    @Override
    public User login(String email, String password) {
        //获取SqlSession
        SqlSession sqlSession = factory.openSession();
        //获取Mapper
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //调用方法
        User user = mapper.login(email,password);
        //释放资源
        sqlSession.close();
        return user;
    }

    /**
     * 用户注册前置查询
     * @param name  用户名
     * @param email 邮箱
     * @return 查询结果
     */
    @Override
    public List<User> pre_reg(String name, String email) {
        //获取SqlSession
        SqlSession sqlSession = factory.openSession();
        //获取Mapper
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //调用方法
        List<User> users = mapper.pre_reg(name, email);
        //释放资源
        sqlSession.close();
        return users;
    }

    /**
     * 注册
     * @param user 新用户信息
     */
    @Override
    public void register(User user) {
        //获取SqlSession
        SqlSession sqlSession = factory.openSession();
        //获取Mapper
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //调用方法
        mapper.register(user);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    /**
     * 更新信息
     * @param user 用户信息
     */
    @Override
    public void upUserData(User user) {
        //获取SqlSession
        SqlSession sqlSession = factory.openSession();
        //获取Mapper
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //调用方法
        mapper.upUserData(user);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    /**
     * 查询所有用户信息
     * @return 所有用户信息
     */
    @Override
    public List<User> selectAll() {
        //获取SqlSession
        SqlSession sqlSession = factory.openSession();
        //获取Mapper
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //调用方法
        List<User> userList = mapper.selectAll();
        //释放资源
        sqlSession.close();
        //返回信息
        return userList;
    }

    /**
     * 查询某个用户信息 大众权限
     *
     * @param userid 用户id
     * @return 用户信息
     */
    @Override
    public User selectByID(String userid) {
        //获取SqlSession
        SqlSession sqlSession = factory.openSession();
        //获取Mapper
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //调用方法
        User user = mapper.selectByID(userid);
        //释放资源
        user.setPassword(null);
        sqlSession.close();
        //返回信息
        return user;
    }
}
