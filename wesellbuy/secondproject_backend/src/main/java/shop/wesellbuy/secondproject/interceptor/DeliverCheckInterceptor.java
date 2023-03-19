package shop.wesellbuy.secondproject.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import shop.wesellbuy.secondproject.exception.common.UnaccessibleRootException;
import shop.wesellbuy.secondproject.util.SessionConst;
import shop.wesellbuy.secondproject.web.member.login.LoginMemberSessionForm;

/**
 * LoginCheck Interceptor
 * writer : 이호진
 * init : 2023.03.12
 * updated by writer :
 * update :
 * description : DeliverCheck Interceptor 처리 로직 구현
 *               > 주문 목록 배달원용 페이지에 접속하기 위한 Interceptor
 */
@Slf4j
public class DeliverCheckInterceptor implements HandlerInterceptor {

    /**
     * writer : 이호진
     * init : 2023.03.12
     * updated by writer : 이호진
     * update :
     * description : > 배달원 인증 체크
     *               > 배달원용 아이디를 사용하는 사용자인지 체크
     *
     * update: cros처리 중 preflight 처리 추가 => 지금은 보류(구현 안 함)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 요청 uri 확인
        String requestURI = request.getRequestURI();
        log.info("DeliverCheckInterceptor preHandle URI : {}", requestURI);

        // preflight는 OPTIONS임을 이용해 options 모두 허용하기 -> 사용해야 함
        // 처음 요청이 오는 프록시 요청
        if(HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }
        // preflight 요청 pass 후 진짜 요청 check

        // session 불러오기
        HttpSession session = request.getSession(false);
        // 회원아이디 불러오기
        LoginMemberSessionForm sessionForm = (LoginMemberSessionForm)session.getAttribute(SessionConst.LOGIN_MEMBER);
        String memberId = sessionForm.getId();
        // deliver로 시작하는 아이디를 가졌는지 확인한다.
        String pattern = "^deliver[\\w]*$";
        boolean result = memberId.matches(pattern);
        // deliver이라는 아이디로 시작하면 통과
        if(result == true) {
            return true;
        }
        // 배달원이 아니라 사용 불가 예외 발생
        String errMsg = "접근 불가";
        throw new UnaccessibleRootException(errMsg);
//        return false;
    }
}
