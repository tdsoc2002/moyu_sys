package cn.imerji.mapper;


import cn.imerji.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * UserMapper接口
 * 日期：2022年7月29日
 * @author ErJiQwQ
 * @version 1.0
 * @Description  增删改查大法
 */
public interface UserMapper {
    /**
     * 登录
     * @param email 邮箱
     * @param password 密码
     * @return 用户信息
     */
    @Select("select * from user where u_email = #{email} and u_pwd = #{password}")
    @ResultMap("userResultMap")
    User login(@Param("email") String email, @Param("password") String password);

    /**
     * 注册前置查询
     * @param name 用户名
     * @param email 邮箱
     * @return 结果
     */
    List<User> pre_reg(@Param("name") String name, @Param("email") String email);

    /**
     * 注册
     * @param user 用户信息
     */
    @Insert("INSERT into user value (null,#{user.name},#{user.password},#{user.email},#{user.power})")
    void register(@Param("user")User user);

    /**
     * 更新用户信息
     * @param user 修改的用户信息
     * 个人用户/管理用户 均会使用
     */
    void upUserData(@Param("user")User user);

    /**
     * 查询所有用户信息
     * @return 所有用户信息
     */
    @Select("select * from user")
    @ResultMap("userResultMap")
    List<User> selectAll();

    /**
     * 查询某个用户信息 大众权限
     * @param userid 用户id
     * @return 用户信息
     */
    @Select("select * from user where u_id=#{userid}")
    @ResultMap("userResultMap")
    User selectByID(@Param("userid") String userid);

}
