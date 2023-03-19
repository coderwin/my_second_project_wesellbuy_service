package shop.wesellbuy.secondproject.exception.item;

/**
 * 사용자 정의 예외
 * writer : 이호진
 * init : 2023.02.02
 * updated by writer :
 * update :
 * description : 상품 저장 중 존재하지 않는 상품 종류이면 예외 발생
 */
public class NotExistingItemTypeException extends RuntimeException {
    public NotExistingItemTypeException() {
        super();
    }

    public NotExistingItemTypeException(String message) {
        super(message);
    }

    public NotExistingItemTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotExistingItemTypeException(Throwable cause) {
        super(cause);
    }

    protected NotExistingItemTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
