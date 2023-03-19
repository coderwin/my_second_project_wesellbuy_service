package shop.wesellbuy.secondproject.repository.order;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
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
import shop.wesellbuy.secondproject.exception.item.OverflowQuantityException;
import shop.wesellbuy.secondproject.web.item.BookForm;
import shop.wesellbuy.secondproject.web.item.FurnitureForm;
import shop.wesellbuy.secondproject.web.item.HomeAppliancesForm;
import shop.wesellbuy.secondproject.web.member.MemberForm;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@Slf4j
public class OrderJpaRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    OrderJpaRepository orderJpaRepository;

    private Member member; // test 사용 member
    private Member member2; // test 사용 member
    private Member member3; // test 사용 member

    private Delivery delivery; // test 사용 delivery
    private Delivery delivery2; // test 사용 delivery
    private Delivery delivery3; // test 사용 delivery

    int rCount; // Book 개수
    int r2Count; // Furniture 개수
    int r3Count; // HomeAppliance 개수

    Item itemTest1; // test 사용 상품
    Item itemTest2; // test 사용 상품
    Item itemTest3; // test 사용 상품

    // create initial data
    @BeforeEach
    public void init() {
        /// 회원 생성
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

        // delivery 생성
        Delivery delivery = Delivery.createDelivery(member);
        Delivery delivery2 = Delivery.createDelivery(member2);
        Delivery delivery3 = Delivery.createDelivery(member3);
        this.delivery = delivery;
        this.delivery2 = delivery2;
        this.delivery3 = delivery3;

        // item 더미 생성
        // picture 생성
        List<ItemPicture> itemPictureList = new ArrayList<>();
        itemPictureList.add(ItemPicture.createItemPicture("a", "a"));
        itemPictureList.add(ItemPicture.createItemPicture("a1", "a2"));

        List<ItemPicture> itemPictureList2 = new ArrayList<>();
        itemPictureList2.add(ItemPicture.createItemPicture("b", "b"));
        itemPictureList2.add(ItemPicture.createItemPicture("b1", "b2"));

        // item 한 개 생성
        BookForm bookFormTest1 = new BookForm(100, 1000, "x", "x is...", itemPictureList, "ed", "ok");
        this.itemTest1 = Book.createBook(bookFormTest1, member);
        FurnitureForm furnitureFormTest1 = new FurnitureForm(200, 2000, "y", "y is...", itemPictureList, "ed");
        this.itemTest2 = Furniture.createFurniture(furnitureFormTest1, member2);
        HomeAppliancesForm homeAppliancesFormTest1 = new HomeAppliancesForm(300, 3000, "z", "z is...", itemPictureList, "ed2");
        this.itemTest3 = HomeAppliances.createHomeAppliances(homeAppliancesFormTest1, member3);

        em.persist(itemTest1);
        em.persist(itemTest2);
        em.persist(itemTest3);


//        int number = 100;
//        Item item = null;
//        // 더미 item 생성
//        for(int i = 0; i < number; i++) {
//            if(i % 3 == 0) {
//                BookForm bookForm = new BookForm(100, 1000, "x", "x is...", itemPictureList, "ed", "ok");
//                item = Book.createBook(bookForm, member);
//
//                this.rCount += 1;
//            } else if(i % 3 == 1) {
//                FurnitureForm furnitureForm = new FurnitureForm(200, 2000, "y", "y is...", itemPictureList, "ed");
//                item = Furniture.createFurniture(furnitureForm, member2);
//                this.r2Count += 1;
//            } else {
//                HomeAppliancesForm homeAppliancesForm = new HomeAppliancesForm(300, 3000, "z", "z is...", itemPictureList, "ed2");
//                item = HomeAppliances.createHomeAppliances(homeAppliancesForm, member3);
//                this.r3Count += 1;
//            }
//            em.persist(item);
//        }

        em.flush();
        em.clear();
    }

    @Test
//    @Rollback(value = false)
    public void 주문_저장_add_상세보기() {
        // given
        OrderItem orderItem1 = OrderItem.createOrderItem(5, itemTest1.getPrice(), itemTest1);
        OrderItem orderItem2 = OrderItem.createOrderItem(3, itemTest1.getPrice(), itemTest1);
        OrderItem orderItem3 = OrderItem.createOrderItem(10, itemTest2.getPrice(), itemTest2);

        Order order = Order.createOrder(member, delivery, orderItem1, orderItem2, orderItem3);
        // when
        // 주문 저장
        orderJpaRepository.save(order);

        // then
        // 주문 상세보기
        Order findOrder = orderJpaRepository.findDetailInfoById(order.getNum()).orElseThrow();

        assertThat(findOrder).isEqualTo(order);
    }

    /**
     * comment - test시 예외를 어디에서 처리하면 좋을까?(exception 발생에서? exception 자체에?)
     *         - service에서 확인해야겠다.
     */
    @Test
//    @Rollback(value = false)
    public void 주문량_초과_예외_발생_확인() {
        // given
        // 주문량이 재고량을 초과(재고량 < 0)
        try {
            OrderItem orderItem1 = OrderItem.createOrderItem(5, itemTest1.getPrice(), itemTest1);
            OrderItem orderItem2 = OrderItem.createOrderItem(3, itemTest1.getPrice(), itemTest1);
            OrderItem orderItem3 = OrderItem.createOrderItem(3, itemTest2.getPrice(), itemTest2);
            OrderItem orderItem4 = OrderItem.createOrderItem(10, itemTest1.getPrice(), itemTest1);

            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2, orderItem3, orderItem4);
            // when
            // 주문 저장
            // then
            // 예외 발생
            fail("재고수량 부족 예외가 발생한다.");
            assertThrows(OverflowQuantityException.class, () -> orderJpaRepository.save(order));
            assertThatThrownBy(() -> orderJpaRepository.save(order)).isInstanceOf(OverflowQuantityException.class);
        } catch(Exception e) {
            em.clear();// 주문 상품을 다시 상품 진열대로 돌려놓기
            fail("재고수량 부족 예외가 발생한다.");

        }

    }

    /**
     * 관리자용 주문 모두 가져오기 확인
     */
    @Test
//    @Rollback(value = false)
    public void 관리자용_주문_모두_가져오기_By_조건_페이징() {
        // given
        // 페이지, 사이즈 정하기
//        Pageable pageablePage0Size100 = PageRequest.of(0, 100);
        Pageable pageablePage0Size10 = PageRequest.of(0, 10);
        Pageable pageablePage1Size10 = PageRequest.of(1, 10);
        Pageable pageablePage3Size5 = PageRequest.of(3, 5);
        Pageable pageablePage2Size2 = PageRequest.of(2, 2);

        // 날짜 condition
        String today = "2023-02-04";
        String otherDay = "2023-02-05";

        // 주문서 생성하기
        OrderItem orderItem1 = OrderItem.createOrderItem(2, itemTest1.getPrice(), itemTest1);
        OrderItem orderItem2 = OrderItem.createOrderItem(2, itemTest2.getPrice(), itemTest2);
        OrderItem orderItem3 = OrderItem.createOrderItem(2, itemTest3.getPrice(), itemTest3);
        OrderItem orderItem4 = OrderItem.createOrderItem(2, itemTest3.getPrice(), itemTest2);

        // 주문하기
        int number = 100; //
        int oCount = 0; // member 주문수
        int oCount2 = 0; // member2 주문수
        int oCount3 = 0; // member3 주문수
        for(int i = 0; i < number; i++) {
            if(i % 3 == 0) {
                Order order1 = Order.createOrder(member, delivery, orderItem1, orderItem2, orderItem3);
                Order order5 = Order.createOrder(member, delivery, orderItem1, orderItem2, orderItem3);
                Order order7 = Order.createOrder(member, delivery, orderItem1, orderItem2, orderItem3);

                em.persist(order1);
                em.persist(order5);
                em.persist(order7);

                oCount += 3;
            } else if(i % 3 == 1) {
                Order order2 = Order.createOrder(member2, delivery2, orderItem1, orderItem2, orderItem3);
                Order order4 = Order.createOrder(member2, delivery2, orderItem1, orderItem2, orderItem3);

                em.persist(order2);
                em.persist(order4);

                oCount2 += 2;
            } else {
                Order order3 = Order.createOrder(member3, delivery3, orderItem1, orderItem2, orderItem3);

                em.persist(order3);

                oCount3 += 1;
            }
        }

        // 검색조건 생성
        // when
        // 조건 1
        OrderSearchCond cond1 = new OrderSearchCond("a", "", "", "");
        OrderSearchCond cond2 = new OrderSearchCond("", "o", "", "");
        OrderSearchCond cond3 = new OrderSearchCond("", "", "R", "");
        OrderSearchCond cond4 = new OrderSearchCond("", "", "", today);
        // 조건 2
        OrderSearchCond cond21 = new OrderSearchCond("b", "o", "", "");
        OrderSearchCond cond22 = new OrderSearchCond("a", "", "R", "");
        OrderSearchCond cond23 = new OrderSearchCond("a", "", "", today);
        OrderSearchCond cond24 = new OrderSearchCond("", "o", "r", "");
        OrderSearchCond cond25 = new OrderSearchCond("", "O", "", today);
        OrderSearchCond cond26 = new OrderSearchCond("", "", "R", today);
        // 조건 3
        OrderSearchCond cond31 = new OrderSearchCond("b", "o", "R", "");
        OrderSearchCond cond32 = new OrderSearchCond("b", "", "R", today);
        OrderSearchCond cond33 = new OrderSearchCond("b", "o", "", today);
        OrderSearchCond cond34 = new OrderSearchCond("", "o", "R", today);
        // 조건 4
        OrderSearchCond cond41 = new OrderSearchCond("cd", "o", "R", today);

        // 검색에 대한 result가 없는 경우
        OrderSearchCond cond5 = new OrderSearchCond("", "ab", "", "");
        OrderSearchCond cond27 = new OrderSearchCond("", "", "Rsdaf", otherDay);
        OrderSearchCond cond35 = new OrderSearchCond("sdf", "", "R", today);
        OrderSearchCond cond42 = new OrderSearchCond("a", "o", "R", otherDay);



        // then
        // 결과 확인(개수로)
        // 조건 1
        testResultForAdmin(pageablePage0Size10, cond1, oCount);
        testResultForAdmin(pageablePage0Size10, cond2, oCount + oCount2 + oCount3);
        testResultForAdmin(pageablePage0Size10, cond3, oCount + oCount2 + oCount3);
        testResultForAdmin(pageablePage0Size10, cond4, oCount + oCount2 + oCount3);
        // 조건 2
        testResultForAdmin(pageablePage0Size10, cond21, oCount2);
        testResultForAdmin(pageablePage0Size10, cond22, oCount);
        testResultForAdmin(pageablePage0Size10, cond23, oCount);
        testResultForAdmin(pageablePage0Size10, cond24, oCount + oCount2 + oCount3);
        testResultForAdmin(pageablePage0Size10, cond25, oCount + oCount2 + oCount3);
        testResultForAdmin(pageablePage0Size10, cond26, oCount + oCount2 + oCount3);
        // 조건 3
        testResultForAdmin(pageablePage3Size5, cond31, oCount2);
        testResultForAdmin(pageablePage3Size5, cond32, oCount2);
        testResultForAdmin(pageablePage3Size5, cond33, oCount2);
        testResultForAdmin(pageablePage3Size5, cond34, oCount + oCount2 + oCount3);
        // 조건 4
        testResultForAdmin(pageablePage3Size5, cond41, oCount3);


        // 검색에 대한 result가 없는 경우
        testResultForAdmin(pageablePage0Size10, cond5, oCount + oCount2 + oCount3);
        testResultForAdmin(pageablePage0Size10, cond27, 0);
        testResultForAdmin(pageablePage0Size10, cond35, 0);
        testResultForAdmin(pageablePage0Size10, cond42, 0);
    }

    // findAllInfo test result
    private void testResultForAdmin(Pageable pageable, OrderSearchCond cond, int count) {
        Page<Order> result = orderJpaRepository.findAllInfoForAdmin(cond, pageable);
        assertThat(result.getTotalElements()).isEqualTo(count);
    }

    /**
     * 관리자가 아닌, 회원일 때 주문 모두 가져오기 확인
     *
     * commnet : OrderSearchCond에 memberId가 무조건 들어있어야 한다.
     */
    @Test
    @Rollback(value = false)
    public void 관리자_아닌경우_주문_모두_가져오기_By_조건_페이징_확인_v2() {
        // given
        // 페이지, 사이즈 정하기
//        Pageable pageablePage0Size100 = PageRequest.of(0, 100);
        Pageable pageablePage0Size10 = PageRequest.of(0, 10);
        Pageable pageablePage1Size10 = PageRequest.of(1, 10);
        Pageable pageablePage3Size5 = PageRequest.of(3, 5);
        Pageable pageablePage2Size2 = PageRequest.of(2, 2);

        // 날짜 condition
        String today = "2023-02-04";
        String otherDay = "2023-02-05";

        // 주문서 생성하기
        OrderItem orderItem1 = OrderItem.createOrderItem(2, itemTest1.getPrice(), itemTest1);
        OrderItem orderItem2 = OrderItem.createOrderItem(2, itemTest2.getPrice(), itemTest2);
        OrderItem orderItem3 = OrderItem.createOrderItem(2, itemTest3.getPrice(), itemTest3);
        OrderItem orderItem4 = OrderItem.createOrderItem(2, itemTest3.getPrice(), itemTest2);

        // 주문하기
        int number = 100; //
        int oCount = 0; // member 주문수
        int oCount2 = 0; // member2 주문수
        int oCount3 = 0; // member3 주문수
        for(int i = 0; i < number; i++) {
            if(i % 3 == 0) {
                Order order1 = Order.createOrder(member, delivery, orderItem1, orderItem2, orderItem3);
                Order order5 = Order.createOrder(member, delivery, orderItem1, orderItem2, orderItem3);
                Order order7 = Order.createOrder(member, delivery, orderItem1, orderItem2, orderItem3);

                em.persist(order1);
                em.persist(order5);
                em.persist(order7);
                oCount += 3;
            } else if(i % 3 == 1) {
                Order order2 = Order.createOrder(member2, delivery2, orderItem1, orderItem2, orderItem3);
                Order order4 = Order.createOrder(member2, delivery2, orderItem1, orderItem2, orderItem3);

                em.persist(order2);
                em.persist(order4);
                oCount2 += 2;
            } else {
                Order order3 = Order.createOrder(member3, delivery3, orderItem1, orderItem2, orderItem3);

                em.persist(order3);
                oCount3 += 1;
            }
        }

        // 검색조건 생성
        // when
        // 조건 1
        OrderSearchCond cond1 = new OrderSearchCond("a", "", "", "");
        OrderSearchCond cond2 = new OrderSearchCond("a", "o", "", "");
        OrderSearchCond cond3 = new OrderSearchCond("a", "", "R", "");
        OrderSearchCond cond4 = new OrderSearchCond("a", "", "", today);
        // 조건 2
        OrderSearchCond cond21 = new OrderSearchCond("b", "o", "", "");
        OrderSearchCond cond22 = new OrderSearchCond("b", "", "R", "");
        OrderSearchCond cond23 = new OrderSearchCond("a", "", "", today);
        OrderSearchCond cond24 = new OrderSearchCond("a", "o", "r", "");
        OrderSearchCond cond25 = new OrderSearchCond("a", "O", "", today);
        OrderSearchCond cond26 = new OrderSearchCond("a", "", "R", today);
        // 조건 3
        OrderSearchCond cond31 = new OrderSearchCond("b", "o", "R", "");
        OrderSearchCond cond32 = new OrderSearchCond("b", "", "R", today);
        OrderSearchCond cond33 = new OrderSearchCond("b", "o", "", today);
        OrderSearchCond cond34 = new OrderSearchCond("b", "o", "R", today);
        // 조건 4
        OrderSearchCond cond41 = new OrderSearchCond("cd", "o", "R", today);

        // 검색에 대한 result가 없는 경우
        OrderSearchCond cond5 = new OrderSearchCond("", "ab", "", "");
        OrderSearchCond cond27 = new OrderSearchCond("a", "", "Rsdaf", otherDay);
        OrderSearchCond cond35 = new OrderSearchCond("sdf", "", "R", today);
        OrderSearchCond cond42 = new OrderSearchCond("a", "o", "R", otherDay);

        // then
        // 결과 확인(개수로)
        // 조건 1
        testResult(pageablePage0Size10, cond1, oCount);
        testResult(pageablePage0Size10, cond2, oCount);
        testResult(pageablePage0Size10, cond3, oCount);
        testResult(pageablePage0Size10, cond4, oCount);
        // 조건 2
        testResult(pageablePage0Size10, cond21, oCount2);
        testResult(pageablePage0Size10, cond22, oCount2);
        testResult(pageablePage0Size10, cond23, oCount);
        testResult(pageablePage0Size10, cond24, oCount);
        testResult(pageablePage0Size10, cond25, oCount);
        testResult(pageablePage0Size10, cond26, oCount);
        // 조건 3
        testResult(pageablePage3Size5, cond31, oCount2);
        testResult(pageablePage3Size5, cond32, oCount2);
        testResult(pageablePage3Size5, cond33, oCount2);
        testResult(pageablePage3Size5, cond34, oCount2);
        // 조건 4
        testResult(pageablePage3Size5, cond41, oCount3);


        // 검색에 대한 result가 없는 경우
        testResult(pageablePage0Size10, cond5, 0);
        testResult(pageablePage0Size10, cond27, 0);
        testResult(pageablePage0Size10, cond35, 0);
        testResult(pageablePage0Size10, cond42, 0);
    }

    // findAllInfo test result
    private void testResult(Pageable pageable, OrderSearchCond cond, int count) {
        Page<Order> result = orderJpaRepository.findAllInfo(cond, pageable);
        assertThat(result.getTotalElements()).isEqualTo(count);
    }




}
