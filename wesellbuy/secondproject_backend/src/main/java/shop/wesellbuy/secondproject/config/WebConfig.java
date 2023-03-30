package shop.wesellbuy.secondproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import shop.wesellbuy.secondproject.interceptor.*;

import java.util.List;

/**
 * 사용자 정의 Interceptor 등록
 * writer : 이호진
 * init : 2023.02.14
 * updated by writer :
 * update :
 * description : 사용자 정의 Interceptor 등록
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    public static final String ALLOWED_METHOD_NAMES = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH";

    /**
     * writer : 이호진
     * init : 2023.02.14
     * updated by writer :
     * update :
     * description : 인터셉터들을 등록
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 로그인 인증 인터셉터 등록
        // 이것이 필요 있는지 생각해보기
            // 필요하다 -> member의 정보는 get으로 얻으면 안 된다.
        // 순서 현재 1번
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/*/images/{savedFileName}", "/error",
                        "/members","/members/login", "/members/logout", "/members/find/**", "/members/id/**",
                        "/items", "/items/{num}", "/items/rank/v1",
                        "/orders",
                        "/recommendations", "/recommendations/{num}"
                );
        // Http Method Check 인터셉터 등록
        registry.addInterceptor(new HttpCheckInterceptor())
                .order(2)
                .addPathPatterns(
                        "/items/{num}", "/recommendations/{num}"
                );
        // 배달원 인증 인터셉터 등록
        registry.addInterceptor(new DeliverCheckInterceptor())
                .order(3)
                .addPathPatterns(
                        "/orders/deliver", "/orders/{num}/delivery/deliver"
                );
        // 관리자 인증 인터셉터 등록
        registry.addInterceptor(new AdminCheckInterceptor())
                .order(4)
                .addPathPatterns(
                        "/**/admin"
                );

//        // Cors 처리 인터셉터 등록
//        registry.addInterceptor(new CrosCheckInterceptor())
//                .order(2)
//                .addPathPatterns("/**");

    }

    /**
     * writer : 이호진
     * init : 2023.03.02
     * updated by writer : 이호진
     * update : 2023.03.06
     * description : Access-Control-Allow-Origin 문제 해결위한 설정
     *
     * update : CORS 정책과 session 확인을 위해
     *          > allowedOrigins 추가
     *          > allowCredentials 추가
     *          > allowedOriginPatterns 추가
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods(ALLOWED_METHOD_NAMES.split(","))
//                .allowedOrigins("*")
                .allowedOriginPatterns("*")
                .allowCredentials(true);
//                .maxAge(3600); // 3600초 동안 preflight 결과를 캐시에 저장
    }
}
