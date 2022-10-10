package com.cpsteam.torontoso.bean;

import java.io.Serializable;

/**
 * Created by Dung on 4/7/2016.
 */
public class User implements Serializable {
    private int userId;

    private String userName;

    private String passWord;

    private boolean disabled;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
