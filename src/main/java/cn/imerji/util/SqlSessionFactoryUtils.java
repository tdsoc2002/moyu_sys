package cn.imerji.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * SqlSessionFactory
 * 创建这个对象会自动写好SqlSessionFactory
 * 目的减少代码复写，只需要调用SqlSessionFactory对象就好
 */
public class SqlSessionFactoryUtils {

    private static SqlSessionFactory sqlSessionFactory;

    static {
        //静态代码块会随着类的加载而自动执行，且只执行一次
        try {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回方法
     * @return 返回SqlSessionFactory对象
     */
    public static SqlSessionFactory getSqlSessionFactory(){
        return sqlSessionFactory;
    }
}
