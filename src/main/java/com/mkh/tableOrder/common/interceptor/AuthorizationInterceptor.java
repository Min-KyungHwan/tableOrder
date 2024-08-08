package com.mkh.tableOrder.common.interceptor;

import com.mkh.tableOrder.api.mapper.ApiMapper;
import com.mkh.tableOrder.api.service.TokenService;
import com.mkh.tableOrder.api.vo.TokenVO;
import com.mkh.tableOrder.api.vo.UserVO;
import com.mkh.tableOrder.common.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;

    /**
     * login관련 DAO
     */
    @Autowired
    private ApiMapper apiMapper;

    private Logger logger = LoggerFactory.getLogger(AuthorizationInterceptor.class);
    /**
     * preHandle: 토큰 검증
     * 1. header("Authorization")나 urlQueryParameter("token")에 토큰이 없을 경우 401 리턴
     * 2. 토큰이 유효하지 않을 경우(유효시간 경과 등) 401 리턴
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(httpServletRequest.getMethod())) {
            // preflight(OPTIONS) 요청인 경우 패스, CorsFilter에서 처리
            return true;
        }
        try {
            // 토큰 조회
            String rawToken = CommonUtil.getToken(httpServletRequest);
            TokenVO tokenVO = tokenService.decodeToken(rawToken);
            String userId = tokenVO.getAudience();
            UserVO userVO = apiMapper.getUserById(userId);
            String dbToken = userVO.getToken();

            logger.debug(httpServletRequest.toString());
            logger.debug("token userName : " + tokenVO.getUserName());
            logger.debug("token expireDate : " + tokenVO.getExpireDate());

            logger.debug("dbToken : " + (dbToken == null ? "null" : dbToken));
            // 토큰이 없을 경우 : 세션 만료
            if (dbToken == null || dbToken.isEmpty() || "removed".equals(dbToken)) {
                logger.info("Token expired for  : " + tokenVO.getUserName());
                httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                httpServletResponse.getWriter().write("Token expired");
                httpServletResponse.getWriter().flush();
                return false;
            }

            // 토큰 없을 경우 Unauthorized
            if (rawToken == null || "".equals(rawToken)) {
                logger.info("Token not found for  : " + tokenVO.getUserName());
                httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                httpServletResponse.getWriter().write("Token not found.");
                httpServletResponse.getWriter().flush();
                return false;
            }

            // 토큰 유효한지 검증
            if (tokenService.isExpired(rawToken)) {
                logger.info("Unauthorized token or token expired for  : " + tokenVO.getUserName());
                httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                httpServletResponse.getWriter().write("Unauthorized Token or token expired.");
                httpServletResponse.getWriter().flush();
                return false;
            }
        } catch (Exception e) {
            logger.warn("Exception : " + e.getClass().getName() + " : "+  e.getMessage());
            return false;
        }
        
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
