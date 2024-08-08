package com.mkh.tableOrder.api.mapper;

import com.mkh.tableOrder.api.vo.CartVO;
import com.mkh.tableOrder.api.vo.OrderVO;
import com.mkh.tableOrder.api.vo.UserVO;

import java.util.HashMap;
import java.util.List;

public interface ApiMapper {
    UserVO getUserById(String userId);

    void updateLoginSuccessful(HashMap<String, Object> param);

    List<CartVO> getCartById(String userId);
    
    int addOrderBas(OrderVO param);

    int addOrderDtlBas(OrderVO param);
}
