package cn.imerji.pojo;

public class Msg {
    private String msg;
    private String reason;
    private String reqMethod;
    private String reqParams;
    private String state;

    public Msg() {
    }

    public Msg(String msg, String state) {
        this.msg = msg;
        this.state = state;
    }

    public Msg(String msg, String reason, String state) {
        this.msg = msg;
        this.reason = reason;
        this.state = state;
    }

    /**
     * 信息体
     */
    public String getMsg() { return msg; }
    public void setMsg(String value) { this.msg = value; }

    /**
     * 引起原因
     */
    public String getReason() { return reason; }
    public void setReason(String value) { this.reason = value; }

    /**
     * 请求方式
     */
    public String getReqMethod() { return reqMethod; }
    public void setReqMethod(String value) { this.reqMethod = value; }

    /**
     * 请求的参数
     */
    public String getReqParams() { return reqParams; }
    public void setReqParams(String value) { this.reqParams = value; }

    /**
     * 数据状态
     */
    public String getState() { return state; }
    public void setState(String value) { this.state = value; }
}
