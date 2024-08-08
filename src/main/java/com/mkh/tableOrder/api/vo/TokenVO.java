package com.mkh.tableOrder.api.vo;

import lombok.Data;

import java.util.Date;

@Data
public class TokenVO {

    private String token; // 토큰
    private String issuer; // 토큰 발급자
    private String audience; // 토큰 사용자 (userId와 동일)
    private String subject; // 토큰의 내용
    private Date expireDate; // 토큰 만료 시간
    private String jwtId; // 토큰에 대한 식별자
    private String message; // 결과 메시지
    private String userName; // 사용자명
    private String using; // 이미 로그인 여부

    /**
     * 값이 없을 경우 true
     * @return
     */
    public boolean isEmpty() {
        boolean result = false;

        if (token == null && issuer == null && audience == null && subject == null && expireDate == null && jwtId == null && userName == null) {
            result = true;
        }

        return result;
    }
}
