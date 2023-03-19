package shop.wesellbuy.secondproject.exception.order;

/**
 * 사용자 정의 예외
 * writer : 이호진
 * init : 2023.02.04
 * updated by writer :
 * update :
 * description : 주문의 배달상태 변경 중, 주문취소상태일 때 배송상태 변경 불가 예외 발생
 */
public class NotChangeDeliveryStatusException extends RuntimeException {
    public NotChangeDeliveryStatusException() {
        super();
    }

    public NotChangeDeliveryStatusException(String message) {
        super(message);
    }

    public NotChangeDeliveryStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotChangeDeliveryStatusException(Throwable cause) {
        super(cause);
    }

    protected NotChangeDeliveryStatusException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
