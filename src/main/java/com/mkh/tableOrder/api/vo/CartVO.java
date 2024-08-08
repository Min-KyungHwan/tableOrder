package com.mkh.tableOrder.api.vo;

import lombok.Data;

@Data
public class CartVO {
    private String cartItemSeq;
    
    private String userId;
    
    private String menuItemId;
    
    private String quantity;

    private String modId;

    private String modDt;

    private String regId;

    private String regDt;
    
    private boolean valid;

    private String message;
}
