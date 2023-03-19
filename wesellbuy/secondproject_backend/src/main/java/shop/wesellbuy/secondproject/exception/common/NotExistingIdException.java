package shop.wesellbuy.secondproject.exception.common;

/**
 * 사용자 정의 예외
 * writer : 이호진
 * init : 2023.01.28
 * updated by writer :
 * update :
 * description : 고객지원글, 추천합니다글 작성 중 존재하지 않는 아이디이면 예외 발생
 */
public class NotExistingIdException extends RuntimeException {
    public NotExistingIdException() {
        super();
    }

    public NotExistingIdException(String message) {
        super(message);
    }

    public NotExistingIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotExistingIdException(Throwable cause) {
        super(cause);
    }

    protected NotExistingIdException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
