package com.mkh.tableOrder.common.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthorizationInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**") // 모든 경로에 대해 인터셉터를 적용
                .excludePathPatterns("/**/api/auth/login"); // 로그인 경로는 제외
    }
}



//import com.mkh.tableOrder.common.interceptor.AuthorizationInterceptor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.servlet.mvc.WebContentInterceptor;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Bean
//    public WebContentInterceptor webContentInterceptor() {
//        WebContentInterceptor interceptor = new WebContentInterceptor();
//        interceptor.setCacheSeconds(0);
//        return interceptor;
//    }
//
//    @Bean
//    public AuthorizationInterceptor authorizationInterceptor() {
//        return new AuthorizationInterceptor();
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        // WebContentInterceptor 설정
//        registry.addInterceptor(webContentInterceptor())
//                .addPathPatterns("/**");
//
//        // AuthorizationInterceptor 설정
//        registry.addInterceptor(authorizationInterceptor())
//                .addPathPatterns("/**")
//                .excludePathPatterns(
////                        "/**/dashboard/**",
////                        "/**/common/topinformation/**",
////                        "/**/login/duplication/**",
////                        "/**/login/user/**",
////                        "/**/login/login",
////                        "/**/login/loginedUsers"
//
//                        "/**/api/auth/login"
//                );
//    }
//}

