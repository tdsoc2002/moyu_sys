package cn.imerji.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BaseServlet extends HttpServlet {

//根据请求的最后一段路径来进行方法分发 url可在方法后携带参数
@Override
protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    //设置编码格式
    req.setCharacterEncoding("utf-8");
    resp.setCharacterEncoding("UTF-8");
    //1. 获取请求路径
    String uri = req.getRequestURI(); // 例如路径为：/brand-case/brand/selectAll
    //2. 获取最后一段路径，方法名
    int index = uri.lastIndexOf('/');
    int lastIndex = uri.lastIndexOf('?');
    String methodName = ""; //  获取到资源的二级路径  selectAll
    if (lastIndex == -1) {
        methodName = uri.substring(index + 1);
    }else {
        methodName = uri.substring(index + 1,lastIndex);
    }
    //2. 执行方法
    //2.1 获取BrandServlet /UserFunctionServlet 字节码对象 Class
    //System.out.println(this);

    Class<? extends BaseServlet> cls = this.getClass();
    //2.2 获取方法 Method对象
    try {
        Method method = cls.getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
        //2.3 执行方法
        method.invoke(this,req,resp);
    } catch (NoSuchMethodException e) {
        e.printStackTrace();
    } catch (IllegalAccessException e) {
        e.printStackTrace();
    } catch (InvocationTargetException e) {
        e.printStackTrace();
    }
}
}
