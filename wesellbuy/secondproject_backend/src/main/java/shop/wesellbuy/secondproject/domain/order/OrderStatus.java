package shop.wesellbuy.secondproject.domain.order;

import jakarta.persistence.Enumerated;

/**
 * 주문 상태 정보
 * writer : 이호진
 * init : 2023.01.14
 * updated by writer :
 * update :
 * description : 주문이 주문 상태(ORDER)인지, 주문 취소 상태(CANCEL)인지 알려준다.
 */
public enum OrderStatus {
    O("ORDER"),
    C("CANCEL");

    private final String orderStatus;

    OrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return this.orderStatus;
    }
}
