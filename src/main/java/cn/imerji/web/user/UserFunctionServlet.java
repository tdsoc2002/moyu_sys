package cn.imerji.web.user;


import cn.imerji.pojo.Msg;
import cn.imerji.pojo.User;
import cn.imerji.service.UserService;
import cn.imerji.service.impl.UserServiceImpl;
import cn.imerji.web.BaseServlet;
import com.alibaba.fastjson.JSON;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "用户功能业务", value = "/user/*")
public class UserFunctionServlet extends BaseServlet {
    private UserService service = new UserServiceImpl();

    public void selectAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("test selectAll...");
    }

    //登录
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getMethod().equals("POST")) {
            //判断是否已经登录
            User attribute = (User) request.getSession().getAttribute("user");
            if (attribute != null) {
                //错误信息转为转为JSON
                String jsonString = JSON.toJSONString(new Msg("error", "已登录", "403"));
                response.setContentType("text/json;charset=utf-8");
                response.getWriter().write(jsonString);
                return;
            }
            //读取数据
            BufferedReader br = request.getReader();
            User user = new User();
            try {
                StringBuilder params = new StringBuilder(br.readLine());
                while (true) {
                    String param = br.readLine();
                    if (param == null) break;
                    params.append(param);
                }
                // 将JSON字符串转为Java对象
                String s = String.valueOf(params);
                user = JSON.parseObject(s, User.class);
                //调用 UserService
                user = service.login(user.getEmail(), user.getPassword());
            } catch (Exception e) {
                //TODO 代码有问题
                Msg msg = new Msg("error", "请求服务参数错误，请核实参数 :)", "400");
                msg.setReqParams(user.toString());
                //错误信息转为转为JSON
                String jsonString = JSON.toJSONString(msg);
                response.setContentType("text/json;charset=utf-8");
                response.getWriter().write(jsonString);
                return;
            }
            //判断是否登录成功
            if (user == null) {
                //响应失败
                String jsonString = JSON.toJSONString(new Msg("error", "200"));
                response.setContentType("text/json;charset=utf-8");
                response.getWriter().write(jsonString);
                return;
            }
            //出于安全性，所以将用户信息存到 Session 里面
            HttpSession session = request.getSession();
            user.setPassword(null);
            session.setAttribute("userid", user.getUserid());//用户id储存
            session.setAttribute("power", user.getPower());//用户权限储存
            session.setAttribute("user", user);//用户信息储存
            //响应成功
            String jsonString = JSON.toJSONString(new Msg("success", "200"));
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(jsonString);
        } else {
            //响应失败
            String jsonString = JSON.toJSONString(new Msg("error", "200"));
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(jsonString);
        }
    }

    //注册前置查询
    public void pre_reg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //创建User对象
        User user = new User();
        //读取数据
        BufferedReader br = request.getReader();
        StringBuilder params;
        String s = "";
        try {
            params = new StringBuilder(br.readLine());
            while (true) {
                String param = br.readLine();
                if (param == null) break;
                params.append(param);
            }
            s = String.valueOf(params);
            user = JSON.parseObject(s, User.class);
        } catch (Exception e) {
            //如果携带数据为空则尝试从Params中获取数据 创建java对象
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            user.setName(name);
            user.setEmail(email);
        }
        //执行方法
        try {
            List<User> users = service.pre_reg(user.getName(), user.getEmail());
            String jsonString;
            if (users.size() == 0) {
                //响应失败
                jsonString = JSON.toJSONString(new Msg("true", "200"));
            } else {
                //响应失败
                jsonString = JSON.toJSONString(new Msg("false", "200"));
            }
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(jsonString);
        } catch (Exception e) {
            Msg msg = new Msg("error", "请求服务参数错误，请核实参数 :)", "400");
            msg.setReqParams(user.toString());
            //错误信息转为转为JSON
            String jsonString = JSON.toJSONString(msg);
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(jsonString);
        }
    }

    //注册
    public void reg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //判断是否已经登录
        User attribute = (User) request.getSession().getAttribute("user");
        if (attribute != null) {
            //错误信息转为转为JSON
            String jsonString = JSON.toJSONString(new Msg("error", "已登录", "403"));
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(jsonString);
            return;
        }
        //读取数据
        BufferedReader br = request.getReader();
        String userInfo = "";
        try {
            StringBuilder params = new StringBuilder(br.readLine());
            while (true) {
                String param = br.readLine();
                if (param == null) break;
                params.append(param);
            }
            userInfo = String.valueOf(params);
        } catch (Exception e) {
            //错误信息转为转为JSON
            String jsonString = JSON.toJSONString(new Msg("error", "请求服务参数错误，请核实参数，有可能是因为没有发送任何数据 :)", "400"));
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(jsonString);
            return;
        }
        //将JSON字符串转为Java对象
        User user = JSON.parseObject(userInfo, User.class);
        //调用 pre_reg 进行参数验证是否已经被注册
        try {
            List<User> users = service.pre_reg(user.getName(), user.getEmail());
            if (users.size() != 0) {
                //错误信息转为转为JSON
                String jsonString = JSON.toJSONString(new Msg("error", "已经被注册了哦 :)", "400"));
                response.setContentType("text/json;charset=utf-8");
                response.getWriter().write(jsonString);
                return;
            }
        } catch (Exception e) {
            Msg msg = new Msg("error", "请求服务参数错误，请核实参数 :)", "400");
            msg.setReqParams(user.toString());
            //错误信息转为转为JSON
            String jsonString = JSON.toJSONString(msg);
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(jsonString);
            return;
        }
        //无异常后进入
        try {
            user.setPower("user");
            service.register(user);
            //响应成功
            String jsonString = JSON.toJSONString(new Msg("success", "200"));
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(jsonString);
        } catch (Exception e) {
            //如果字节长度对不上，那就报错！
            Msg msg = new Msg("error", "请求服务参数错误，请核实参数 :)", "400");
            msg.setReqParams(user.toString());
            //错误信息转为转为JSON
            String jsonString = JSON.toJSONString(msg);
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(jsonString);
        }
    }

    //注销
    public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.getSession().invalidate();
            //响应成功
            String jsonString = JSON.toJSONString(new Msg("success", "200"));
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(jsonString);
        } catch (Exception e) {
            String jsonString = JSON.toJSONString(new Msg("error", "服务器内部错误", "500"));
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(jsonString);
        }
    }

    //多用户数据请求
    public void s(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getMethod().equals("GET")) {
            String power = (String) request.getSession().getAttribute("power");
            if (power == null || !power.equals("root")) {
                //错误信息转为转为JSON
                String jsonString = JSON.toJSONString(new Msg("error", "用户权限不足", "403"));
                response.setContentType("text/json;charset=utf-8");
                response.getWriter().write(jsonString);
            } else {
                //请求方式GET&需要权限root
                try {
                    List<User> userList = service.selectAll();
                    //2. 转为JSON
                    String jsonString = JSON.toJSONString(userList);
                    //3. 写数据
                    response.setContentType("text/json;charset=utf-8");
                    response.getWriter().write(jsonString);
                } catch (Exception e) {
                    String jsonString = JSON.toJSONString(new Msg("error", "服务器内部错误", "500"));
                    response.setContentType("text/json;charset=utf-8");
                    response.getWriter().write(jsonString);
                }
            }
        } else {
            Msg msg = new Msg("error", "请求方式错误", "400");
            msg.setReqMethod(request.getMethod());
            //错误信息转为转为JSON
            String jsonString = JSON.toJSONString(msg);
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(jsonString);
        }
    }

    //单用户信息查询
    public void search(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userid = request.getParameter("userid");
        try {
            User user = service.selectByID(userid);
            //2. 转为JSON
            String jsonString = JSON.toJSONString(user);
            //3. 写数据
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(jsonString);
        } catch (Exception e) {
            //未查询到用户或者其他报错
            Msg msg = new Msg("error", "请求错误, 可能错误发生在用户ID不匹配 :)", "400");
            msg.setReqParams(userid);
            //错误信息转为转为JSON
            String jsonString = JSON.toJSONString(msg);
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(jsonString);
        }
    }

}
