package cn.imerji.service;


import cn.imerji.pojo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserService {

    /**
     * 用户登录
     * @param email 邮箱
     * @param password 密码
     * @return 用户
     */
    User login(String email, String password);

    /**
     * 用户注册前置查询
     * @param name 用户名
     * @param email 邮箱
     * @return 查询结果
     */
    List<User> pre_reg(String name, String email);

    /**
     * 注册
     * @param user 新用户信息
     */
    void register(User user);

    /**
     * 更新用户信息
     * @param user 修改的用户信息
     * 个人用户/管理用户 均会使用
     */
    void upUserData(User user);

    /**
     * 查询所有用户信息
     * @return 所有用户信息
     */
    List<User> selectAll();

    /**
     * 查询某个用户信息 大众权限 不包含关键信息:password
     * @param userid 用户id
     * @return 用户信息
     */
    User selectByID(String userid);
}
