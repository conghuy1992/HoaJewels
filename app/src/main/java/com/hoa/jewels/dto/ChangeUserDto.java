package com.hoa.jewels.dto;

/**
 * Created by maidinh on 5/29/2017.
 */

public class ChangeUserDto {
    String Code = "";
    String Status = "";
    String Message = "";

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

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
