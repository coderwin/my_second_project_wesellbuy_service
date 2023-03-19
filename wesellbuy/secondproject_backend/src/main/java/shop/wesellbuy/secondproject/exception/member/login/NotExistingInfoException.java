package shop.wesellbuy.secondproject.exception.member.login;

/**
 * 사용자 정의 예외
 * writer : 이호진
 * init : 2023.01.27
 * updated by writer :
 * update :
 * description : 아이디 or 비밀번호 찾는 중 존재하지 않는 경우
 */
public class NotExistingInfoException extends RuntimeException{
    public NotExistingInfoException() {
        super();
    }

    public NotExistingInfoException(String message) {
        super(message);
    }

    public NotExistingInfoException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotExistingInfoException(Throwable cause) {
        super(cause);
    }

    protected NotExistingInfoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
