package cn.imerji.pojo;

/**
 * 用户类
 */
public class User {
    private String email;
    private String name;
    private String password;
    private String power;
    private String userid;

    /**
     * 用户邮箱
     */
    public String getEmail() { return email; }
    public void setEmail(String value) { this.email = value; }

    /**
     * 用户名
     */
    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    /**
     * 用户密码
     */
    public String getPassword() { return password; }
    public void setPassword(String value) { this.password = value; }

    /**
     * 用户权限，root:高级管理员 user:普通用户
     */
    public String getPower() { return power; }
    public void setPower(String value) { this.power = value; }

    /**
     * 用户ID
     */
    public String getUserid() { return userid; }
    public void setUserid(String value) { this.userid = value; }


    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", power='" + power + '\'' +
                ", userid='" + userid + '\'' +
                '}';
    }
}
