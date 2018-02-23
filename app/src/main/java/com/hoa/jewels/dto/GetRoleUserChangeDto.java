package com.hoa.jewels.dto;

import java.util.List;

/**
 * Created by maidinh on 5/30/2017.
 */

public class GetRoleUserChangeDto {
    String Code="";
    String Status="";
    List<UserDto>ListUser;

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

    public List<UserDto> getListUser() {
        return ListUser;
    }

    public void setListUser(List<UserDto> listUser) {
        ListUser = listUser;
    }
}
