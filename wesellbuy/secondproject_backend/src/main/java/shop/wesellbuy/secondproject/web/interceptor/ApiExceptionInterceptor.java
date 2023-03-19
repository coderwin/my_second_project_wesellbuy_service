package shop.wesellbuy.secondproject.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shop.wesellbuy.secondproject.exception.ErrorResultMsg;
import shop.wesellbuy.secondproject.exception.common.NotExistingIdException;
import shop.wesellbuy.secondproject.exception.common.UnaccessibleRootException;

/**
 * Interceptor 예외 처리 Controller
 * writer : 이호진
 * init : 2023.02.08
 * updated by writer :
 * update :
 * description : 인터셉터에서 예외 처리 메서드 모음
 */
@RestControllerAdvice
@Slf4j
public class ApiExceptionInterceptor {

    /**
     * writer : 이호진
     * init : 2023.02.14
     * updated by writer :
     * update :
     * description : 로그인 상태 검사 중(in 인터셉터) 비회원인 경우 예외 발생
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UnaccessibleRootException.class)
    public ErrorResultMsg UnaccessibleRootEx(UnaccessibleRootException e) {
        log.error("exception appears : ", e);
        // 에러 메시지 전달하기
        ErrorResultMsg result = ErrorResultMsg.create("bad request", e.getMessage());

        return result;
    }
}
