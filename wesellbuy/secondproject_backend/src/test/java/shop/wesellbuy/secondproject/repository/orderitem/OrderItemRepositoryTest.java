package shop.wesellbuy.secondproject.repository.orderitem;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import shop.wesellbuy.secondproject.domain.*;
import shop.wesellbuy.secondproject.domain.item.Book;
import shop.wesellbuy.secondproject.domain.item.Furniture;
import shop.wesellbuy.secondproject.domain.item.HomeAppliances;
import shop.wesellbuy.secondproject.domain.item.ItemPicture;
import shop.wesellbuy.secondproject.web.item.BookForm;
import shop.wesellbuy.secondproject.web.item.FurnitureForm;
import shop.wesellbuy.secondproject.web.item.HomeAppliancesForm;
import shop.wesellbuy.secondproject.web.member.MemberForm;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
@Slf4j
public class OrderItemRepositoryTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderItemJpaRepository orderItemJpaRepository;

    private Member member; // test 사용 member
    private Member member2; // test 사용 member
    private Member member3; // test 사용 member

    private Delivery delivery; // test 사용 delivery
    private Delivery delivery2; // test 사용 delivery
    private Delivery delivery3; // test 사용 delivery

    Item item1; // test 사용 상품
    Item item2; // test 사용 상품
    Item item3; // test 사용 상품

    int number = 100; //
    int oCount; // member 주문수
    int oCount2; // member2 주문수
    int oCount3; // member3 주문수

    int iaCount = 0; // member(판매자) 상품주문수
    int iaCount2 = 0; // member(판매자) 상품주문수

    @BeforeEach
    public void init() {

        // 회원 생성
        MemberForm memberForm1 = new MemberForm("a", "a","123", "a@a", "01012341234", "0511231234", "korea1", "b", "h", "h", "123", null);
        Member member = Member.createMember(memberForm1);
        MemberForm memberForm2 = new MemberForm("a", "b","123", "a@a", "01012341234", "0511231234", "korea12", "b", "h2", "h2", "123", null);
        Member member2 = Member.createMember(memberForm2);
        MemberForm memberForm3 = new MemberForm("b", "cd","123", "a@a", "01012341234", "0511231234", "korea13", "b", "h3", "h3", "123", null);
        Member member3 = Member.createMember(memberForm3);

        em.persist(member);
        em.persist(member2);
        em.persist(member3);

        this.member = member; // x 물품 판다
        this.member2 = member2; // y 물품 판다
        this.member3 = member3; // z 물품 판다
        // 상품 생성
        // picture 생성
        List<ItemPicture> itemPictureList = new ArrayList<>();
        itemPictureList.add(ItemPicture.createItemPicture("a", "a"));
        itemPictureList.add(ItemPicture.createItemPicture("a1", "a2"));

        BookForm bookFormTest1 = new BookForm(10000, 1000, "x", "x is...", itemPictureList, "ed", "ok");
        this.item1 = Book.createBook(bookFormTest1, member);
        FurnitureForm furnitureFormTest1 = new FurnitureForm(20000, 2000, "y", "y is...", itemPictureList, "ed");
        this.item2 = Furniture.createFurniture(furnitureFormTest1, member2);
        HomeAppliancesForm homeAppliancesFormTest1 = new HomeAppliancesForm(30000, 3000, "z", "z is...", itemPictureList, "ed2");
        this.item3 = HomeAppliances.createHomeAppliances(homeAppliancesFormTest1, member3);

        em.persist(item1);
        em.persist(item2);
        em.persist(item3);


        // 배달 생성
        // delivery 생성
//        Delivery delivery = Delivery.createDelivery(member);
//        Delivery delivery2 = Delivery.createDelivery(member2);
//        Delivery delivery3 = Delivery.createDelivery(member3);
//        this.delivery = delivery;
//        this.delivery2 = delivery2;
//        this.delivery3 = delivery3;

        // 주문상품 생성
//        OrderItem orderItem1 = OrderItem.createOrderItem(3, item1.getPrice(), item1); // 1000 * 3 3000
//        OrderItem orderItem2 = OrderItem.createOrderItem(2, item2.getPrice(), item2); // 2000 * 2 4000
//        OrderItem orderItem3 = OrderItem.createOrderItem(4, item3.getPrice(), item3); // 3000 * 4 12000
//        OrderItem orderItem4 = OrderItem.createOrderItem(2, item3.getPrice(), item2);

        // 주문하기(주문 생성)
        int number = 100; //
        oCount = 0; // member 주문수
        oCount2 = 0; // member2 주문수
        oCount3 = 0; // member3 주문수

        iaCount = 0; // member(판매자) 상품주문수
        iaCount2 = 0; // member(판매자) 상품주문수

        for(int i = 0; i < number; i++) {
            if(i % 3 == 0) {

                OrderItem orderItem1 = OrderItem.createOrderItem(3, item1.getPrice(), item1); // 1000 * 3 3000
                OrderItem orderItem2 = OrderItem.createOrderItem(2, item2.getPrice(), item2); // 2000 * 2 4000
                OrderItem orderItem3 = OrderItem.createOrderItem(4, item3.getPrice(), item3); // 3000 * 4 12000
                OrderItem orderItem4 = OrderItem.createOrderItem(3, item1.getPrice(), item1); // 1000 * 3 3000
                OrderItem orderItem5 = OrderItem.createOrderItem(2, item2.getPrice(), item2); // 2000 * 2 4000
                OrderItem orderItem6 = OrderItem.createOrderItem(4, item3.getPrice(), item3); // 3000 * 4 12000
                OrderItem orderItem7 = OrderItem.createOrderItem(2, item2.getPrice(), item2); // 2000 * 2 4000

                Delivery delivery11 = Delivery.createDelivery(member);
                Delivery delivery12 = Delivery.createDelivery(member);
                Delivery delivery13 = Delivery.createDelivery(member);

                Order order1 = Order.createOrder(member, delivery11, 7000, orderItem1, orderItem2);  //          // 7000
                Order order5 = Order.createOrder(member, delivery12, 19000, orderItem4, orderItem5, orderItem3);  // 19000
                Order order7 = Order.createOrder(member, delivery13, 16000, orderItem7, orderItem6);              // 16000

                em.persist(order1);
                em.persist(order5);
                em.persist(order7);

                oCount += 3;
                iaCount += 2;

                // 배달 상태 -> 배달중
                order1.getDelivery().changeStatusRT();

            } else if(i % 3 == 1) {
                OrderItem orderItem1 = OrderItem.createOrderItem(3, item1.getPrice(), item1); // 1000 * 3 3000
                OrderItem orderItem4 = OrderItem.createOrderItem(3, item1.getPrice(), item1); // 1000 * 3 3000
                OrderItem orderItem2 = OrderItem.createOrderItem(2, item2.getPrice(), item2); // 2000 * 2 4000
                OrderItem orderItem3 = OrderItem.createOrderItem(4, item3.getPrice(), item3); // 3000 * 4 12000
                OrderItem orderItem5 = OrderItem.createOrderItem(4, item3.getPrice(), item3); // 3000 * 4 12000

                Delivery delivery21 = Delivery.createDelivery(member2);
                Delivery delivery22 = Delivery.createDelivery(member2);

                Order order2 = Order.createOrder(member2, delivery21, 15000, orderItem1, orderItem3);            // 15000
                Order order4 = Order.createOrder(member2, delivery22, 19000, orderItem4, orderItem2, orderItem5);// 19000

                em.persist(order2);
                em.persist(order4);

                oCount2 += 2;
                iaCount2 += 2;

                // 주문 취소
                order2.changeStatus();

            } else {

                OrderItem orderItem1 = OrderItem.createOrderItem(3, item1.getPrice(), item1); // 1000 * 3 3000
                OrderItem orderItem2 = OrderItem.createOrderItem(2, item2.getPrice(), item2); // 2000 * 2 4000
                OrderItem orderItem3 = OrderItem.createOrderItem(4, item3.getPrice(), item3); // 3000 * 4 12000

                Delivery delivery3 = Delivery.createDelivery(member3);

                Order order3 = Order.createOrder(member3, delivery3, 16000, orderItem2, orderItem3); // 16000

                em.persist(order3);

                oCount3 += 1;
            }
        }

    }

    /**
     * 판매자의 판매된 상품 목록 확인
     */
    @Test
    @Rollback(value = false)
    public void 판매자의_판매된_상품_목록_확인() {
        // given
        // 페이지, 사이즈 정하기
        Pageable pageablePage0Size100 = PageRequest.of(0, 100);
        Pageable pageablePage0Size10 = PageRequest.of(0, 10);
        Pageable pageablePage1Size10 = PageRequest.of(1, 10);
        Pageable pageablePage3Size5 = PageRequest.of(3, 5);
        Pageable pageablePage2Size2 = PageRequest.of(2, 2);

        // 날짜 condition
        String today = "2023-02-05";
        String otherDay = "2023-02-06";

        // when
        // 조건 생성
        // 판매자가 member 일 때
        // 조건 0
        OrderItemSearchCond cond0 = new OrderItemSearchCond(member.getNum(), "", "", "", "");
        // 조건 1
        OrderItemSearchCond cond11 = new OrderItemSearchCond(member.getNum(), "b", "", "", "");
        OrderItemSearchCond cond12 = new OrderItemSearchCond(member.getNum(), "", "o", "", "");
        OrderItemSearchCond cond13 = new OrderItemSearchCond(member.getNum(), "", "", "R", "");
        OrderItemSearchCond cond14 = new OrderItemSearchCond(member.getNum(), "", "", "", today);
        // 조건 2
        OrderItemSearchCond cond21 = new OrderItemSearchCond(member.getNum(), "a", "O", "", "");
        OrderItemSearchCond cond22 = new OrderItemSearchCond(member.getNum(), "a", "", "R", "");
        OrderItemSearchCond cond23 = new OrderItemSearchCond(member.getNum(), "a", "", "", otherDay);
        OrderItemSearchCond cond24 = new OrderItemSearchCond(member.getNum(), "", "C", "R", "");
        OrderItemSearchCond cond25 = new OrderItemSearchCond(member.getNum(), "", "c", "", today);
        OrderItemSearchCond cond26 = new OrderItemSearchCond(member.getNum(), "", "", "t", today);
        // 배달 완료
        OrderItemSearchCond cond27 = new OrderItemSearchCond(member.getNum(), "", "", "c", today);

        // 조건 3
        OrderItemSearchCond cond31 = new OrderItemSearchCond(member.getNum(), "b", "o", "T", "");
        OrderItemSearchCond cond32 = new OrderItemSearchCond(member.getNum(), "b", "O", "", today);
        OrderItemSearchCond cond33 = new OrderItemSearchCond(member.getNum(), "a", "", "t", today);
        OrderItemSearchCond cond34 = new OrderItemSearchCond(member.getNum(), "", "O", "R", today);
        // 주문 안 한 아이디
        OrderItemSearchCond cond36 = new OrderItemSearchCond(member.getNum(), "cd", "O", "", today);

        // 조건 4
        OrderItemSearchCond cond41 = new OrderItemSearchCond(member.getNum(), "a", "o", "T", today);

        // 조건 잘못 되었을 때
        OrderItemSearchCond cond1 = new OrderItemSearchCond(member.getNum(), "ok12", "", "", "");
        OrderItemSearchCond cond15 = new OrderItemSearchCond(member.getNum(), "", "asdf", "", today);
        OrderItemSearchCond cond28 = new OrderItemSearchCond(member.getNum(), "", "asdf", "sdfas", "");
        OrderItemSearchCond cond35 = new OrderItemSearchCond(member.getNum(), "", "O", "Tasd", today);
        OrderItemSearchCond cond42 = new OrderItemSearchCond(member.getNum(), "aefe", "o", "T", today);


        // then
        // 판매자가 member 일 때
        // 조건 0
        testResult(cond0, pageablePage0Size10, iaCount + iaCount2 );
        // 조건 1
        testResult(cond11, pageablePage1Size10, iaCount2 );
        testResult(cond12, pageablePage1Size10, iaCount + iaCount2 / 2);
        testResult(cond13, pageablePage1Size10, iaCount / 2 + iaCount2 );
        testResult(cond14, pageablePage1Size10, iaCount + iaCount2 );
        // 조건 2
        testResult(cond21, pageablePage0Size100, iaCount);
        testResult(cond22, pageablePage0Size100, iaCount / 2);
        testResult(cond23, pageablePage0Size100, 0);
        testResult(cond24, pageablePage0Size100, iaCount2 / 2);
        testResult(cond25, pageablePage0Size100, iaCount2 / 2);
        testResult(cond26, pageablePage0Size100, iaCount / 2);
        // 배달 완료
        testResult(cond27, pageablePage0Size100, 0);

        // 조건 3
        testResult(cond31, pageablePage0Size100, 0);
        testResult(cond32, pageablePage0Size100, iaCount2 / 2);
        testResult(cond33, pageablePage0Size100, iaCount / 2);
        testResult(cond34, pageablePage0Size100, iaCount / 2 + iaCount2 / 2);
        testResult(cond36, pageablePage0Size100, 0);

        // 조건 4
        testResult(cond41, pageablePage2Size2, iaCount / 2);

        // 조건 잘못 되었을 때
        testResult(cond1, pageablePage0Size10, 0);
        testResult(cond15, pageablePage1Size10, iaCount + iaCount2); // status의 값이 달라질 때 null 처리 말고 있을까?
        testResult(cond28, pageablePage1Size10, iaCount + iaCount2); // status의 값이 달라질 때 null 처리 말고 있을까?
        testResult(cond35, pageablePage1Size10, iaCount + iaCount2 / 2); // status의 값이 달라질 때 null 처리 말고 있을까?
        testResult(cond42, pageablePage2Size2, 0);

    }

    /**
     * 판매자의_판매된_상품_목록_확인의
     *  -> test 결과 확인
     */
    private void testResult(OrderItemSearchCond cond, Pageable pageable, int count) {
        // page 가져오기
        Page<OrderItem> orderItemPage = orderItemJpaRepository.findAllInfo(cond, pageable);
        // 전체 개수로 확인
        Assertions.assertThat(orderItemPage.getTotalElements()).isEqualTo(count);
    }






}
