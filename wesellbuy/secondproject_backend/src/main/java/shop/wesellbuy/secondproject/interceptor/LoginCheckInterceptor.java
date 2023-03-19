package shop.wesellbuy.secondproject.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import shop.wesellbuy.secondproject.exception.common.UnaccessibleRootException;
import shop.wesellbuy.secondproject.util.SessionConst;

/**
 * LoginCheck Interceptor
 * writer : 이호진
 * init : 2023.02.14
 * updated by writer :
 * update :
 * description : LoginCheck Interceptor 처리 로직 구현
 */
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    /**
     * writer : 이호진
     * init : 2023.02.14
     * updated by writer : 이호진
     * update : 2023.03.02
     * description : 로그인 된 사용자인지 체크
     *
     * update: cros처리 중 preflight 처리 추가
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 요청 uri 확인
        String requestURI = request.getRequestURI();
        log.info("LoginCheckInterceptor preHandle URI : {}", requestURI);

        // preflight는 OPTIONS임을 이용해 OPTIONS 모두 허용하기
        if(HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        log.info("인증체크 인터셉터 실행중... {}", requestURI);
        // session 불러오기
        HttpSession session = request.getSession(false);
        log.info("session : {}", session);
        // session 존재유무 확인
        // + session에 key가 LOGIN_MEMBER 존재유무 확인
        if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            // 비회원 사용자일 때
            log.info("비회원 사용자 요청");
            // 비회원 사용 불가 예외 발생
            String errMsg = "비회원은 접근 불가";
            throw new UnaccessibleRootException(errMsg);
            // 여기서 요청이 종료된다.(인터셉터 종료)
//            return false;
        }

        // 로그인한 경우
        return true;
    }
}
