package com.mkh.tableOrder.api.vo;

import lombok.Data;

import java.util.List;

@Data
public class OrderVO {
    private int orderSeq;

    private String userId;

    private String restaurantId;

    private String tableNo;

    private String status;

    private String modId;

    private String modDt;

    private String regId;

    private String regDt;
    
    private List<CartVO> cartList;

    private boolean valid;

    private String message;
}
