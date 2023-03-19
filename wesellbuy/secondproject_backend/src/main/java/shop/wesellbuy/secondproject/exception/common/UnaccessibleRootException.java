package shop.wesellbuy.secondproject.exception.common;

/**
 * 사용자 정의 예외
 * writer : 이호진
 * init : 2023.02.14
 * updated by writer :
 * update :
 * description : > 로그인 상태 검사 중(in 인터셉터) 비회원인 경우 발생
 *               > 배달원용 데이터 접근 검사 중(in 인터셉터) 배달원 아이디 아닐 경우 발생
 */
public class UnaccessibleRootException extends RuntimeException {

    public UnaccessibleRootException() {
        super();
    }

    public UnaccessibleRootException(String message) {
        super(message);
    }

    public UnaccessibleRootException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnaccessibleRootException(Throwable cause) {
        super(cause);
    }

    protected UnaccessibleRootException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
