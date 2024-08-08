package com.mkh.tableOrder.api.service;

import com.mkh.tableOrder.api.vo.*;

public interface ApiService {
    LoginResVO login(UserVO param);

    OrderResVO order(UserVO param);
}
