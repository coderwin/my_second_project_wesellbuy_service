package shop.wesellbuy.secondproject.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * writer : 이호진
 * init : 2023.03.02
 * updated by writer : 이호진
 * update :
 * description : Cros의 preflight 확인
 *
 * comment : 현재 사용 안 함 - LoginCheckInterceptor에서 처리 중
 */
public class CrosCheckInterceptor implements HandlerInterceptor {

    /**
     * writer : 이호진
     * init : 2023.03.02
     * updated by writer : 이호진
     * update :
     * description : Cros의 preflight 확인
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // preflight는 OPTIONS임을 이용해 OPTIONS 모두 허용하기
        if(HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

//        // Spring에서 제공해주는 Util 활용하기
//        if(CorsUtils.isPreFlightRequest(request)) {
//            return true;
//        }

        return false;
    }
}
