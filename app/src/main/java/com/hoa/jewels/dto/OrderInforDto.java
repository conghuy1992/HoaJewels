package com.hoa.jewels.dto;

/**
 * Created by huy on 28/05/2017.
 */

public class OrderInforDto {
    String Code = "";
    String Status = "";
    OrderSimple OrderSimple;

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

    public com.hoa.jewels.dto.OrderSimple getOrderSimple() {
        return OrderSimple;
    }

    public void setOrderSimple(com.hoa.jewels.dto.OrderSimple orderSimple) {
        OrderSimple = orderSimple;
    }
}
