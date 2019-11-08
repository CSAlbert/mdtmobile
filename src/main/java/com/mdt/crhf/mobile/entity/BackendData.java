package com.mdt.crhf.mobile.entity;

/**
 * @author chulang
 * @version 1.0
 * @date 2019/11/2 13:33
 * @Description 用来存放rest请求的返回数据，包括请求状态码、提示信息、数据
 */
public class BackendData {
    private int code = 0;

    // 在出现Exception的时候，用于存放Exception的信息
    private String message = "";

    // 使用Object类型来存放主体数据
    private Object data = "";

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
