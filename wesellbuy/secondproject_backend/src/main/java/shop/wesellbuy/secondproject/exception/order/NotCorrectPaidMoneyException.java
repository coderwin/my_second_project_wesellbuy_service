package shop.wesellbuy.secondproject.exception.order;

/**
 * 사용자 정의 예외
 * writer : 이호진
 * init : 2023.02.04
 * updated by writer :
 * update :
 * description : 주문 진행 중, 지불 가격이 일치하지 않을 때 예외 발생
 */
public class NotCorrectPaidMoneyException extends RuntimeException {
    public NotCorrectPaidMoneyException() {
        super();
    }

    public NotCorrectPaidMoneyException(String message) {
        super(message);
    }

    public NotCorrectPaidMoneyException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotCorrectPaidMoneyException(Throwable cause) {
        super(cause);
    }

    protected NotCorrectPaidMoneyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
