package com.hoa.jewels.dto;

/**
 * Created by maidinh on 5/30/2017.
 */

public class UserDto {
    int UID;
    String Username="";

    public int getUID() {
        return UID;
    }

    public void setUID(int UID) {
        this.UID = UID;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }
}
