package com.mkh.tableOrder.api.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mkh.tableOrder.api.service.TokenService;
import com.mkh.tableOrder.api.vo.UserVO;
import com.mkh.tableOrder.api.vo.TokenVO;
import com.mkh.tableOrder.common.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

@Service
public class TokenServiceImpl implements TokenService {

    /**
     * 로그인 토큰 유효시간(분)
     */
    @Value("60")
    private int sessionMin;

    private final static Logger logger = LoggerFactory.getLogger(TokenServiceImpl.class);

    /**
     * 토큰 생성
     * @param userVO
     * @return
     */
    @Override
    public String createToken(UserVO userVO) {
        String token = ""; // 토큰 값이 저장될 변수

        // 토큰에 들어갈 정보 (로그인 이후 접근시 토큰에서 이 값을 가져다 사용)
        String issuer = "tableOrder"; // 토큰 발급자
        String subject = "subject"; // 토큰에 담길 내용
        //String jwtId = "jwtID"; // JWT ID (토큰에 대한 식별자)
        //String keyId = "keyID"; // KEY ID (헤더에 들어가는 키 ID)
        Date expireDate = new Date(System.currentTimeMillis() + (60 * 1000 * sessionMin)); // 토큰 만료 시간

        try {
            Algorithm algorithm = Algorithm.HMAC256("secretKey");
            token = JWT.create()
                    .withIssuer(issuer)
                    .withSubject(subject)
                    .withAudience(userVO.getUserId())
                    .withClaim("userName", userVO.getUserName())
                    //.withJWTId(jwtId)
                    //.withKeyId(keyId)
                    .withExpiresAt(expireDate)
                    .sign(algorithm);

        } 
        catch (JWTCreationException | IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
        }

        return token;
    }

    /**
     * 토큰 검증 후 정상이면 payload 데이터 반환 (Issuer, Audience, GroupCode)
     * @param token
     * @return
     */
    @Override
    public boolean isExpired(String token) {
    	
    	token = removeBearer(token);
    	
    	try {
    		Algorithm algorithm = Algorithm.HMAC256("secretKey");
    		JWTVerifier verifier = JWT.require(algorithm)
                  .withIssuer("tableOrder")
                  .build();
    		DecodedJWT jwt = verifier.verify(token); // 검증 처리
    		
    		if(jwt != null) {
    			String issuer = jwt.getIssuer();
    			String audience = jwt.getAudience().get(0);
    			String userName = jwt.getClaim("userName").asString();
    			
    			if(logger.isDebugEnabled()) {
    				logger.debug("validToken: issuer:{}, audience:{}, userName:{}", 
    					new Object[] {issuer, audience, userName});
    			}
    			return false;
    		}
    		else {
    			return true;
    		}
    	}
    	catch(JWTDecodeException e) {
    		logger.debug("### 디코딩 에러 : " + token);
    		return true;
    	}
    	catch(TokenExpiredException e) {
    		logger.debug("### 토큰 만료됨 : " + token);
    		return true;
    	}
    }

    /**
     * 토큰 디코딩
     * @param token
     * @return
     */
    @Override
    public TokenVO decodeToken(String token) {
        token = removeBearer(token);
    	
        if(logger.isDebugEnabled())
            logger.debug("decodeToken.token: [{}]", token);

        TokenVO tokenVO = new TokenVO();
        tokenVO.setToken(token);

        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            String issuer = decodedJWT.getIssuer();
            String audience = decodedJWT.getAudience().get(0);
            String userName = decodedJWT.getClaim("userName").asString();

            tokenVO.setIssuer(issuer);
            tokenVO.setAudience(audience); // audience(==userId) 담아서 리턴
            tokenVO.setUserName(userName);
            tokenVO.setExpireDate(decodedJWT.getExpiresAt());

        }
        catch (JWTDecodeException e) {
            // 토큰 디코딩 실패시
            logger.error(e.getMessage(), e);
            throw new UnauthorizedException("JWT Decode Exception.");
        }
        return tokenVO;
    }

    /**
     * 헤더에 붙은 bearer 문자열 삭제
     * @param rawToken
     * @return
     */
    private String removeBearer(String rawToken) {
    	
    	if(rawToken == null) {
    		return null;
    	}
    	
    	// trim
        if(rawToken != null && !"".equals(rawToken)) {
        	rawToken = rawToken.trim();
        }

        // Bearer 문자열 제거
        if (StringUtils.hasText(rawToken) && rawToken.toLowerCase().startsWith("bearer ")) {
        	rawToken = rawToken.substring(7, rawToken.length());
        }
        
        return rawToken;
    }
}
