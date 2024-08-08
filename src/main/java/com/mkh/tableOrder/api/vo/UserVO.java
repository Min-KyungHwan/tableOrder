package com.mkh.tableOrder.api.vo;

import lombok.Data;

@Data
public class UserVO {
    private String userId;
    
    private String userName;
    
    private String password;

    private String token;
    
    private String modId;
    
    private String modDt;
    
    private String regId;
    
    private String regDt;

    private String restaurantId;

    private String tableNo;

    private boolean valid;

    private String message;
}
