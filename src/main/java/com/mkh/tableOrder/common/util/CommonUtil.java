package com.mkh.tableOrder.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class CommonUtil {
	private final static Logger logger = LoggerFactory.getLogger(SecurityUtil.class);
    
    /**
     * HttpServletRequest 객체를 리턴한다.
     * @return
     */
    public static HttpServletRequest getHttpServletRequest() {
    	try {
    		return ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();	
    	}
    	catch(IllegalStateException e) {
    		return null;
    	}
    }
    
    /**
     * Token 조회
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request) {
    	if(request == null) {
    		request = getHttpServletRequest();
    	}
    	String token = "";
    	
    	// attribute에 있는지 확인
    	String tokenAttr = (String)request.getAttribute("servlet_token");
    	if(tokenAttr != null && !"".equals(tokenAttr)) {
    		token = tokenAttr;
    	}
    	// 없으면
    	else {
    		// header에서 추출
    		token = request.getHeader("Authorization");
    		
    		// 없을 경우 requestParam에서 추출
    		if(token == null || "".equals(token)) {
    			token = request.getParameter("token");
    		}
    		
    		// 헤더 또는 파라미터에서 추출 성공한 경우 attribute에 입력
			if(token != null && !"".equals(token)) {
				request.setAttribute("servlet_token", token);
			}
    	}
    	
    	return token;
    }
}