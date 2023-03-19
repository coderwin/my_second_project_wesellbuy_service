package shop.wesellbuy.secondproject.exception.member;

/**
 * 사용자 정의 예외
 * writer : 이호진
 * init : 2023.01.20
 * updated by writer :
 * update :
 * description : 회원 가입 중 사용중인 아이디이면 예외 발생
 */
public class ExistingIdException extends RuntimeException {

    public ExistingIdException() {
        super();
    }

    public ExistingIdException(String message) {
        super(message);
    }

    public ExistingIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExistingIdException(Throwable cause) {
        super(cause);
    }

    protected ExistingIdException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
