package shop.wesellbuy.secondproject.repository.order;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.wesellbuy.secondproject.domain.delivery.DeliveryStatus;
import shop.wesellbuy.secondproject.domain.order.OrderStatus;

/**
 * Order findAll for condition dto
 * writer : 이호진
 * init : 2023.02.04
 * updated by writer : 이호진
 * update : 2023.02.14
 * description : Order finaAll에 사용되는 where 절의 조건 데이터 모음
 *
 * update : @NoArgsConstructor 추가
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderSearchCond {

    private String memberId; // 주문 회원 아이디
    private String orderStatus; // 주문 상태
    private String deliveryStatus; // 배달 상태
    private String createDate;// 생성 날짜

    // ** 비즈니스 메서드 ** //
    public void addMemberId(String memberId) {
        this.memberId = memberId;
    }
}
