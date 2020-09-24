package com.yonyougov.http.entity;

/**
 * @ClassName Response
 * @Description 返回值
 * @Author playboy
 * @Date 2020/9/23 2:51 下午
 * @Version 1.0
 **/
public class Response {
    public final static String SUCCESS = "success";

    public final static String FAIL = "fail";

    private String flag;

    private String msg;

    private Object data;

    private Response() {

    }

    public static Response newInstance() {
        return new Response();
    }

    public static Response successReponse(Object data) {
        Response response = new Response();
        response.setFlag(SUCCESS);
        response.setData(data);
        response.setMsg("操作成功");
        return response;
    }

    public static Response successReponse(Object data, String msg) {
        Response response = new Response();
        response.setFlag(SUCCESS);
        response.setData(data);
        response.setMsg(msg);
        return response;
    }

    public static Response failReponse(String msg) {
        Response response = new Response();
        response.setFlag(FAIL);
        response.setMsg(msg);
        return response;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
