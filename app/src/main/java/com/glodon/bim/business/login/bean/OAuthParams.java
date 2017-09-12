package com.glodon.bim.business.login.bean;

/**
 * 描述：
 * 作者：zhourf on 2017/9/12
 * 邮箱：zhourf@glodon.com
 */

public class OAuthParams {
    private String grant_type = "password";
    private String username ;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
