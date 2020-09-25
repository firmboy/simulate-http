package com.yonyougov.http.entity;

/**
 * @ClassName WebSocketMessage
 * @Description TODO
 * @Author playboy
 * @Date 2020/9/24 8:10 下午
 * @Version 1.0
 **/
public class WebSocketMessage {

    private String url;

    private String body;

    private String result;

    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
