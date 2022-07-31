package cn.imerji.web.user;

import cn.imerji.pojo.Msg;
import cn.imerji.pojo.User;
import cn.imerji.service.UserService;
import cn.imerji.service.impl.UserServiceImpl;
import com.alibaba.fastjson.JSON;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "用户信息业务", value = "/user")
public class UserServlet extends HttpServlet {
    private final UserService service = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取 session
        HttpSession session = request.getSession();
        Object user = session.getAttribute("user");
        //2. 转为JSON
        String jsonString = JSON.toJSONString(user);
        //3. 写数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(jsonString);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //判断是否已经具有权限
        String attribute = (String) request.getSession().getAttribute("power");
        if (attribute == null || !attribute.equals("root")) {
            //错误信息转为转为JSON
            String jsonString = JSON.toJSONString(new Msg("error", "用户权限不足", "403"));
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
            String jsonString = JSON.toJSONString(new Msg("error", "服务请求参数错误(Server Request Error) :(", "400"));
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
                String jsonString = JSON.toJSONString(new Msg("error", "该账号已被注册 :(", "200"));
                response.setContentType("text/json;charset=utf-8");
                response.getWriter().write(jsonString);
                return;
            }
        } catch (Exception e) {
            //错误信息转为转为JSON
            String jsonString = JSON.toJSONString(new Msg("error", "服务器错误 , 发生在注册功能的前置查询模块", "500"));
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(jsonString);
            return;
        }
        //无异常后进入
        try {
            service.register(user);
            //信息转为转为JSON
            String jsonString = JSON.toJSONString(new Msg("success", "200"));
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(jsonString);
        } catch (Exception e) {
            //如果字节长度对不上，那就报错
            Msg msg = new Msg("error", "服务器错误 ,发生在注册功能\n可能原因：超出字符数量限制", "400");
            msg.setReqParams(String.valueOf(user));
            String jsonString = JSON.toJSONString(msg);
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(jsonString);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //判断是否已经登录
        User attribute = (User) request.getSession().getAttribute("user");
        if (attribute == null) {
            //错误信息转为转为JSON
            String jsonString = JSON.toJSONString(new Msg("error", "未登录", "403"));
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(jsonString);
            return;
        }
        //设置编码格式
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("UTF-8");
        //读取数据
        BufferedReader br = request.getReader();
        String params = "";
        try {
            StringBuilder string = new StringBuilder(br.readLine());
            while (true) {
                String param = br.readLine();
                if (param == null) break;
                string.append(param);
            }
            params = String.valueOf(string);
        } catch (Exception e) {
            //错误信息转为转为JSON
            String jsonString = JSON.toJSONString(new Msg("error", "请求服务参数错误，请核实参数，有可能是因为没有发送任何数据 :)", "400"));
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(jsonString);
            return;
        }
        //将JSON字符串转为Java对象
        User user = JSON.parseObject(params, User.class);
        if (user == null || user.getUserid() == null) {
            //检验是否存在userid或者为空
            Msg msg = new Msg("error", "请求服务参数错误，请核实参数 :)", "400");
            msg.setReqParams(user.toString());
            //错误信息转为转为JSON
            String jsonString = JSON.toJSONString(msg);
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(jsonString);
            return;
        }
        String power = (String) request.getSession().getAttribute("power");
        if (!power.equals("root")) {//判断是否具有root权限 如果不具有则执行
            String userid = (String) request.getSession().getAttribute("userid");
            if (!userid.equals(user.getUserid())) {//判断id是否相等，如果不相等则执行
                Msg msg = new Msg("error", "请求错误, 可能错误发生在用户ID不匹配 :)", "400");
                msg.setReqParams(user.toString());
                //错误信息转为转为JSON
                String jsonString = JSON.toJSONString(msg);
                response.setContentType("text/json;charset=utf-8");
                response.getWriter().write(jsonString);
                return;
            }
        }
        try {
            User selectByID;//前置查询用User对象
            try {
                selectByID = service.selectByID(user.getUserid());
            } catch (Exception e) {
                //错误信息
                Msg msg = new Msg("error", "该用户ID不存在 :)", "400");
                msg.setReqParams(user.getUserid());
                //错误信息转为转为JSON
                String jsonString = JSON.toJSONString(msg);
                response.setContentType("text/json;charset=utf-8");
                response.getWriter().write(jsonString);
                return;
            }
            if (!power.equals("root")) {//判断是否具有root权限
                user.setPower(selectByID.getPower());
            }
            service.upUserData(user);
            //请求成功
            String jsonString = JSON.toJSONString(new Msg("success", "200"));
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(jsonString);
        } catch (Exception e) {
            //如果字节长度对不上，那就报错！
            Msg msg = new Msg("error", "服务器错误 ,发生在注册功能\n可能原因：超出字符数量限制", "400");
            msg.setReqParams(String.valueOf(user));
            String jsonString = JSON.toJSONString(msg);
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(jsonString);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置编码格式
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("UTF-8");
        //判断是否已经登录
        HttpSession session = request.getSession();
        User attribute = (User) session.getAttribute("user");
        if (attribute == null) {
            //错误信息转为转为JSON
            String jsonString = JSON.toJSONString(new Msg("error", "未登录", "403"));
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(jsonString);
            return;
        }
        BufferedReader br = request.getReader();
        String params = "";
        try {
            StringBuilder string = new StringBuilder(br.readLine());
            while (true) {
                String param = br.readLine();
                if (param == null) break;
                string.append(param);
            }
            params = String.valueOf(string);
        } catch (Exception e) {
            //错误信息转为转为JSON
            String jsonString = JSON.toJSONString(new Msg("error", "请求服务参数错误，请核实参数，有可能是因为没有发送任何数据 :)", "400"));
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(jsonString);
            return;
        }
        //将JSON字符串转为Java对象
        User user = JSON.parseObject(params, User.class);
        String power = (String) session.getAttribute("power");
        if (!power.equals("root")) {
            if (!user.getUserid().equals(session.getAttribute("userid"))) {
                //错误信息转为转为JSON
                String jsonString = JSON.toJSONString(new Msg("error", "用户权限不足 :)", "403"));
                response.setContentType("text/json;charset=utf-8");
                response.getWriter().write(jsonString);
                return;
            }
        }
        try {
            User selectByID = service.selectByID(user.getUserid());
        } catch (Exception e) {
            //错误信息
            Msg msg = new Msg("error", "该用户ID不存在 :)", "400");
            msg.setReqParams(user.getUserid());
            //错误信息转为转为JSON
            String jsonString = JSON.toJSONString(msg);
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(jsonString);
            return;
        }
        user.setPassword("Death");
        user.setPower("Death");
        user.setEmail("Death");
        user.setName("已注销");
        try {
            service.upUserData(user);
            request.getSession().invalidate();//注销登录态
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
}
