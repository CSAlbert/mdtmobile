package com.mdt.crhf.mobile.entity;

/**
 * @author chulang
 * @version 1.0
 * @date 2019/11/2 11:03
 * @Description BO系统登录用户实体类
 */
public class User {
    private String userName;
    private String password;
    private String clientType;
    private String auth;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }
}
