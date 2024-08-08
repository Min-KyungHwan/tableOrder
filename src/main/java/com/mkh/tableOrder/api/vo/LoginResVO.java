package com.mkh.tableOrder.api.vo;

import lombok.Data;

@Data
public class LoginResVO {
    private String userId;
    
    private String userName;
    
    private String token;
    
    private boolean valid;

    private String message;
}
