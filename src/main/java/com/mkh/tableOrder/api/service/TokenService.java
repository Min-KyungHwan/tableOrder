package com.mkh.tableOrder.api.service;

import com.mkh.tableOrder.api.vo.TokenVO;
import com.mkh.tableOrder.api.vo.UserVO;

public interface TokenService {

    String createToken(UserVO userVO);

    boolean isExpired(String token);

    TokenVO decodeToken(String token);

}
