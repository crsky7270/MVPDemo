package com.booway.mvpdemo.retrofit;

/**
 * 创建人：wandun
 * 创建时间：2018/12/19
 * 描述：${DESC}
 */

public class User {

    public User(String name, String code) {
        this.username = name;
        this.userCode = code;
    }

    private String username;

    private String userCode;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
}
