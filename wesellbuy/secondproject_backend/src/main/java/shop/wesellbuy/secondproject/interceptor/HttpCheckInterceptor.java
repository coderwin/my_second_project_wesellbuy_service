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
 * HttpCheck Interceptor
 * writer : 이호진
 * init : 2023.02.14
 * updated by writer :
 * update :
 * description : HttpCheck Interceptor 처리 로직 구현
 *               -> restful api 대한 접근 httpMethod 체크
 */
@Slf4j
public class HttpCheckInterceptor implements HandlerInterceptor {

    /**
     * writer : 이호진
     * init : 2023.02.14
     * updated by writer : 이호진
     * update : 2023.03.07
     * description : restful api에 대한 httpMethod 체크
     *
     * update : GET 외의 방식으로 접근했을 때 처리 방법 수정
     *              > 로그인한 사용자는 pass
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 클라이언트 요청 httpMethod 불러오기
        String httpMethod = request.getMethod();

        // preflight는 OPTIONS임을 이용해 OPTIONS 모두 허용하기
        if(HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        // 로그인한 사용자인 경우
            // 모든 httpMethod 통과 가능
        HttpSession session = request.getSession(false);
        // 로그인한 사용자 LoginSessionForm 있는지 확인
        // 있으면 통과
        if(session != null && session.getAttribute(SessionConst.LOGIN_MEMBER) != null) {
            return true;
        }
        // 로그인한 사용자가 아닌 경우
        // 그외 통과 못함
        if(!HttpMethod.GET.matches(httpMethod)) {
            // 비회원 사용자일 때
            log.info("비회원 사용자 요청");
            // 비회원 사용 불가 예외 발생
            String errMsg = "비회원은 접근 불가";
            throw new UnaccessibleRootException(errMsg);
            // 여기서 요청이 종료된다.(인터셉터 종료)
//            return false;
        }
        // GET이면 통과
        return true;
    }
}
