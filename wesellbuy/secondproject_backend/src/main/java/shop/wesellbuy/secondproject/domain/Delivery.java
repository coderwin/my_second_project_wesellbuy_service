package shop.wesellbuy.secondproject.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.wesellbuy.secondproject.domain.delivery.DeliveryStatus;
import shop.wesellbuy.secondproject.domain.member.Address;
import shop.wesellbuy.secondproject.exception.delivery.NotCancelOrderException;

/**
 * 회원 가입 현황 정보
 * writer : 이호진
 * init : 2023.01.14
 * updated by writer :
 * update :
 * description : 회원이 가입된 상태인지, 탈퇴한 상태인지 알려준다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_num")
    private Integer num; // 배달
    @Column(length = 5)
    @Enumerated(value = EnumType.STRING)
    private DeliveryStatus status; // 배달 상태(배송준비중(R), 배송중(T), 배송완료(C))
    private Address address; // 배달 주소 === 회원 주소

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "delivery")
    private Order order;

    // ** setter ** //
    public void addStatus(DeliveryStatus status) {
        this.status = status;
    }

    public void addAddress(Address address) {
        this.address = address;
    }

    public void addOrder(Order order) {
        this.order = order;
    }

    // ** 생성 메서드 ** /
    public static Delivery createDelivery(Member member) {
        Delivery delivery = new Delivery();

        delivery.addStatus(DeliveryStatus.R); // 주문 처음 => READY 상태
        delivery.addAddress(member.getAddress());

        return delivery;
    }

    // ** 비즈니스 메서드 ** //

    /**
     * writer : 이호진
     * init : 2023.02.04
     * updated by writer : 이호진
     * update : 2023.03.11
     * description : 주문취소 일어났을 때,
     *               -> 배달 상태에 따라 주문 취소 가능 판별
     *
     * comment : order에 있어야할까? delivery에 있어야할까?
     *
     * update : 주문이 취소가 되면 배달상태를 O(OVER) 배달취소로 바꾸기
     */
    public void cancel() {
        if(status.equals(DeliveryStatus.T)) {
            String errMsg = "배송중으로 취소 불가";
            throw new NotCancelOrderException(errMsg);
        } else if(status.equals(DeliveryStatus.C)) {
            String errMsg = "배송완료로 취소 불가";
            throw new NotCancelOrderException(errMsg);
        }
        // 취소 완료
        this.status = DeliveryStatus.O;
    }

    /**
     * writer : 이호진
     * init : 2023.02.04
     * updated by writer :
     * update :
     * description : 배달 상태 변경
     *               -> delivery status : T(TRANSIT) -> C(COMPLETE)
     */
    public void changeStatusRT() {
        this.status = DeliveryStatus.T;
    }

    /**
     * writer : 이호진
     * init : 2023.02.04
     * updated by writer :
     * update :
     * description : 배달 상태 변경
     *               -> delivery status : T(TRANSIT) -> C(COMPLETE)
     */
    public void changeStatusTC() {
        this.status = DeliveryStatus.C;
    }
}
