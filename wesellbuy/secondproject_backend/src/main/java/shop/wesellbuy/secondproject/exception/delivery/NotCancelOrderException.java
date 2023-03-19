package shop.wesellbuy.secondproject.exception.delivery;

/**
 * 사용자 정의 예외
 * writer : 이호진
 * init : 2023.02.04
 * updated by writer :
 * update :
 * description : 주문 취소 중, 배달상태가 배송중, 배송완료면 예외 발생
 */
public class NotCancelOrderException extends RuntimeException {

    public NotCancelOrderException() {
        super();
    }

    public NotCancelOrderException(String message) {
        super(message);
    }

    public NotCancelOrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotCancelOrderException(Throwable cause) {
        super(cause);
    }

    protected NotCancelOrderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
