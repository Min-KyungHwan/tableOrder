package com.mkh.tableOrder.api.vo;

import lombok.Data;

import java.util.List;

@Data
public class OrderResVO {
    private int orderSeq;

    private String userId;

    private boolean valid;

    private String message;
}
