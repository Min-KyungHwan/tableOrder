package com.mkh.tableOrder.api.controller;

import com.mkh.tableOrder.api.service.ApiService;
import com.mkh.tableOrder.api.vo.LoginResVO;
import com.mkh.tableOrder.api.vo.OrderResVO;
import com.mkh.tableOrder.api.vo.OrderVO;
import com.mkh.tableOrder.api.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiController {
    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private ApiService apiService;

    @PostMapping("/auth/login")
    public LoginResVO login(@RequestBody UserVO userVO) {
        return apiService.login(userVO);
    }

    @PostMapping("/order")
    public OrderResVO order(@RequestBody UserVO userVO) {
        return apiService.order(userVO);
    }
}
