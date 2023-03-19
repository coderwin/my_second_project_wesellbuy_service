package shop.wesellbuy.secondproject.exception.item;

/**
 * 사용자 정의 예외
 * writer : 이호진
 * init : 2023.01.20
 * updated by writer :
 * update :
 * description : 주문 중, 상품의 주문수량 초과/재고 부족인 경우 예외 발생
 */
public class OverflowQuantityException extends RuntimeException {

    public OverflowQuantityException() {
        super();
    }

    public OverflowQuantityException(String message) {
        super(message);
    }

    public OverflowQuantityException(String message, Throwable cause) {
        super(message, cause);
    }

    public OverflowQuantityException(Throwable cause) {
        super(cause);
    }

    protected OverflowQuantityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
