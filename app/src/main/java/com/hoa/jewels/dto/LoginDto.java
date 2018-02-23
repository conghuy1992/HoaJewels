package com.hoa.jewels.dto;

/**
 * Created by huy on 28/05/2017.
 */

public class LoginDto {
    String Code="";
    String Status="";
    User User;

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public com.hoa.jewels.dto.User getUser() {
        return User;
    }

    public void setUser(com.hoa.jewels.dto.User user) {
        User = user;
    }
}
