package com.mkh.tableOrder.api.service.impl;

import com.mkh.tableOrder.api.mapper.ApiMapper;
import com.mkh.tableOrder.api.service.ApiService;
import com.mkh.tableOrder.api.service.TokenService;
import com.mkh.tableOrder.api.vo.*;
import com.mkh.tableOrder.common.exception.BadRequestException;
import com.mkh.tableOrder.common.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@Service
public class ApiServiceImpl implements ApiService {
    
    @Autowired
    private ApiMapper apiMapper;
    
    @Autowired
    private TokenService tokenService;
    
    @Autowired
    private MessageSource messageSource;

    @Override
    public LoginResVO login(UserVO user) {
        LoginResVO response = new LoginResVO();
        String userId = user.getUserId();
        UserVO loginResultVO;
        // 입력값체크
        if(user.getUserId() == null || user.getPassword() == null) {
            throw new BadRequestException(messageSource.getMessage("login.required.fields", null, Locale.getDefault()));
        }
        // ID 존재 체크
        loginResultVO = apiMapper.getUserById(userId);
        if(loginResultVO == null) {
            response.setMessage(messageSource.getMessage("login.userid.not.registered", null, Locale.getDefault()));
            return response;
        }
        // pwd 체크
        if(!isCorrectPassword(loginResultVO, user.getPassword())) {
            response.setMessage(messageSource.getMessage("login.password.not.match", null, Locale.getDefault()));
            return response;
        }
        // token 저장
        String createdToken = tokenService.createToken(loginResultVO);
        response.setUserId(userId);
        response.setUserName(loginResultVO.getUserName());
        response.setToken(createdToken);
        response.setMessage("정상 처리되었습니다.");
        response.setValid(true);
        
        HashMap<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("token", createdToken);
        apiMapper.updateLoginSuccessful(param);
        
        return response;
    }
    
    @Override
    public OrderResVO order(UserVO user) {
        OrderResVO response = new OrderResVO();
        List<CartVO> cartList = new ArrayList<CartVO>();
        OrderVO orderParam = new OrderVO();
        String userId = user.getUserId();


        // 입력값체크
        if(user.getUserId() == null || user.getRestaurantId() == null || user.getTableNo() == null) {
            throw new BadRequestException(messageSource.getMessage("login.required.fields", null, Locale.getDefault()));
        }
        //Cart 조회
        cartList = apiMapper.getCartById(userId);
        if(cartList.size() > 0) {
            //Order 등록
            orderParam.setUserId(user.getUserId());
            orderParam.setRestaurantId(user.getRestaurantId());
            orderParam.setTableNo(user.getTableNo());
            int cnt = apiMapper.addOrderBas(orderParam);
            if(cnt > 0){
                orderParam.setCartList(cartList);
                apiMapper.addOrderDtlBas(orderParam);
            }
        }
        response.setOrderSeq(orderParam.getOrderSeq());
        response.setMessage("정상 처리되었습니다.");
        response.setValid(true);
        return response;
    }
    /**
     * 패스워드 검증
     * @param dbUser
     * @param targetPassword
     * @return
     */
    private boolean isCorrectPassword(UserVO dbUser, String targetPassword) {
        String encryptedPassword = SecurityUtil.encryptSHA256(targetPassword);
        return dbUser.getPassword().equals(encryptedPassword);
    }
}
