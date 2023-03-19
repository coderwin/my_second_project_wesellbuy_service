package shop.wesellbuy.secondproject.repository.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import shop.wesellbuy.secondproject.domain.Item;
import shop.wesellbuy.secondproject.domain.Member;
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
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
public class ItemJpaRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    ItemJpaRepository itemJpaRepository;

    private Member member; // test 사용 member
    private Member member2; // test 사용 member
    private Member member3; // test 사용 member

    int rCount; // Book 개수
    int r2Count; // Furniture 개수
    int r3Count; // HomeAppliance 개수

    @BeforeEach
    public void init() {

        /// 회원 생성
        MemberForm memberForm1 = new MemberForm("a", "a", "123","a@a", "01012341234", "0511231234", "korea1", "b", "h", "h", "123", null);
        Member member = Member.createMember(memberForm1);
        MemberForm memberForm2 = new MemberForm("a", "b", "123","a@a", "01012341234", "0511231234", "korea1", "b", "h", "h", "123", null);
        Member member2 = Member.createMember(memberForm2);
        MemberForm memberForm3 = new MemberForm("b", "cd","123", "a@a", "01012341234", "0511231234", "korea1", "b", "h", "h", "123", null);
        Member member3 = Member.createMember(memberForm3);

        em.persist(member);
        em.persist(member2);
        em.persist(member3);

        this.member = member; // x 물품 판다
        this.member2 = member2; // y 물품 판다
        this.member3 = member3; // z 물품 판다

        // item 생성
        // picture 생성
        List<ItemPicture> itemPictureList = new ArrayList<>();
        itemPictureList.add(ItemPicture.createItemPicture("a", "a"));
        itemPictureList.add(ItemPicture.createItemPicture("a1", "a2"));

        List<ItemPicture> itemPictureList2 = new ArrayList<>();
        itemPictureList2.add(ItemPicture.createItemPicture("b", "b"));
        itemPictureList2.add(ItemPicture.createItemPicture("b1", "b2"));

        int number = 100;
        Item item = null;
        // item 생성
        for(int i = 0; i < number; i++) {
            if(i % 3 == 0) {
                BookForm bookForm = new BookForm(10, 1000, "x", "x is...", itemPictureList, "ed", "ok");
                item = Book.createBook(bookForm, member);
                this.rCount += 1;
            } else if(i % 3 == 1) {
                FurnitureForm furnitureForm = new FurnitureForm(20, 2000, "y", "y is...", itemPictureList, "ed");
                item = Furniture.createFurniture(furnitureForm, member2);
                this.r2Count += 1;
            } else {
                HomeAppliancesForm homeAppliancesForm = new HomeAppliancesForm(30, 3000, "z", "z is...", itemPictureList, "ed2");
                item = HomeAppliances.createHomeAppliances(homeAppliancesForm, member3);
                this.r3Count += 1;
            }
            itemJpaRepository.save(item);

            // 삭제하기
            if(i % 3 == 2) {
                item.changeStatus();
            }
        }


    }

    @Test
//    @Rollback(value = false)
    public void 상품_저장_add_상세보기() {
        // given
        // picture 생성
        List<ItemPicture> itemPictureList = new ArrayList<>();
        itemPictureList.add(ItemPicture.createItemPicture("a", "a"));
        itemPictureList.add(ItemPicture.createItemPicture("a1", "a2"));

        log.info("itemPictureList : [{}]", itemPictureList);
        // book 생성
        BookForm bookForm = new BookForm(10, 1000, "x", "x is...", itemPictureList, "ed", "ok");
        Item item = Book.createBook(bookForm, member);

        // when
        // 저장 확인
        itemJpaRepository.save(item);

        // then
        // 상세보기 확인
        Item findItem = itemJpaRepository.findDetailInfoById(item.getNum()).orElseThrow();

        assertThat(findItem).isEqualTo(item);
    }

    /**
     * 관리자 일 때, 상품 모두 가져오기 확인
     */
    @Test
    @Rollback(value = false)
    public void 상품_모두_가져오기_By_조건_페이징() {
        // given
        // 페이지, 사이즈 정하기
//        Pageable pageablePage0Size100 = PageRequest.of(0, 100);
        Pageable pageablePage0Size10 = PageRequest.of(0, 10);
        Pageable pageablePage1Size10 = PageRequest.of(1, 10);
        Pageable pageablePage3Size5 = PageRequest.of(3, 5);
        Pageable pageablePage2Size2 = PageRequest.of(2, 2);

        // 날짜 condition
        String today = "2023-01-27";
        String otherDay = "2023-01-28";

        // 검색조건 생성
        // when
        // 조건 1
//        ItemSearchCond cond1 = new ItemSearchCond("x", null, null, null);
//        ItemSearchCond cond2 = new ItemSearchCond(null, "a", null, null);
//        ItemSearchCond cond3 = new ItemSearchCond(null, null, today, null);
//        ItemSearchCond cond4 = new ItemSearchCond(null, null, null, "HA");

        // 조건 2
//        ItemSearchCond cond21 = new ItemSearchCond("x", "a", null, null);
//        ItemSearchCond cond22 = new ItemSearchCond("x", null, today, null);
//        ItemSearchCond cond23 = new ItemSearchCond("x", null, null, "B");
//        ItemSearchCond cond24 = new ItemSearchCond(null, "a", today, null);
//        ItemSearchCond cond25 = new ItemSearchCond(null, "a", null, "B");
//        ItemSearchCond cond26 = new ItemSearchCond(null, null, today, "F");

        // 조건 3
//        ItemSearchCond cond31 = new ItemSearchCond("x", "a", today, null);
//        ItemSearchCond cond32 = new ItemSearchCond("x", "a", null, "B");
//        ItemSearchCond cond33 = new ItemSearchCond("x", null, today, "B");
//        ItemSearchCond cond34 = new ItemSearchCond(null, "b", today, "F");

        // 조건 4
        ItemSearchCond cond41 = new ItemSearchCond("x", "a", today, "B");


        // 검색에 대한 result가 없는 경우

//        ItemSearchCond cond5 = new ItemSearchCond("hello", null, null, null);
//        ItemSearchCond cond6 = new ItemSearchCond(null, null, null, "B23");
//        ItemSearchCond cond27 = new ItemSearchCond(null, null, otherDay, "B");
//        ItemSearchCond cond35 = new ItemSearchCond(null, "abc", today, "B");
        ItemSearchCond cond42 = new ItemSearchCond("x", "a", today, "Hcd");


        // then
        // 결과 확인(개수로)
        // 조건 1
//        testResult(pageablePage0Size10, cond1, rCount);
//        testResult(pageablePage0Size10, cond2, rCount);
//        testResult(pageablePage0Size10, cond3, rCount + r2Count + r3Count);
//        testResult(pageablePage0Size10, cond4, r3Count);
        // 조건 2
//        testResult(pageablePage0Size10, cond21, rCount);
//        testResult(pageablePage0Size10, cond22, rCount);
//        testResult(pageablePage0Size10, cond23, rCount);
//        testResult(pageablePage0Size10, cond24, rCount);
//        testResult(pageablePage0Size10, cond25, rCount);
//        testResult(pageablePage0Size10, cond26, r2Count);
        // 조건 3
//        testResult(pageablePage0Size10, cond31, rCount);
//        testResult(pageablePage0Size10, cond32, rCount);
//        testResult(pageablePage0Size10, cond33, rCount);
//        testResult(pageablePage0Size10, cond34, r2Count);

        // 조건 4
        testResult(pageablePage3Size5, cond41, rCount);


        // 검색에 대한 result가 없는 경우
//        testResult(pageablePage0Size10, cond5, 0);
//        testResult(pageablePage0Size10, cond6, 0);
//        testResult(pageablePage0Size10, cond27, 0);
//        testResult(pageablePage0Size10, cond35, 0);
        testResult(pageablePage2Size2, cond42, 0);
    }

    // findAllInfo test result
    private void testResult(Pageable pageable, ItemSearchCond cond, int count) {
        Page<Item> result = itemJpaRepository.findAllInfoForAdmin(cond, pageable);
        assertThat(result.getTotalElements()).isEqualTo(count);
    }

    /**
     * 관리자 아닐 때, 상품 모두 가져오기 확인
     */
    @Test
    @Rollback(value = false)
    public void 상품_모두_가져오기_By_조건_페이징_확인V2() {
        // given
        // 페이지, 사이즈 정하기
//        Pageable pageablePage0Size100 = PageRequest.of(0, 100);
        Pageable pageablePage0Size10 = PageRequest.of(0, 10);
        Pageable pageablePage1Size10 = PageRequest.of(1, 10);
        Pageable pageablePage3Size5 = PageRequest.of(3, 5);
        Pageable pageablePage2Size2 = PageRequest.of(2, 2);

        // 날짜 condition
        String today = "2023-02-03";
        String otherDay = "2023-02-04";

        // 검색조건 생성
        // when
        // 조건 1
        ItemSearchCond cond1 = new ItemSearchCond("x", "", "", "");
        ItemSearchCond cond2 = new ItemSearchCond("", "a", "", "");
        ItemSearchCond cond3 = new ItemSearchCond("", "", today, "");
        ItemSearchCond cond4 = new ItemSearchCond("", "", "", "HA");

        // 조건 2
        ItemSearchCond cond21 = new ItemSearchCond("x", "a", "", "");
        ItemSearchCond cond22 = new ItemSearchCond("x", "", today, "");
        ItemSearchCond cond23 = new ItemSearchCond("x", "", "", "B");
        ItemSearchCond cond24 = new ItemSearchCond("", "a", today, "");
        ItemSearchCond cond25 = new ItemSearchCond("", "a", "", "B");
        ItemSearchCond cond26 = new ItemSearchCond("", "", today, "F");

        // 조건 3
        ItemSearchCond cond31 = new ItemSearchCond("x", "a", today, "");
        ItemSearchCond cond32 = new ItemSearchCond("x", "a", "", "B");
        ItemSearchCond cond33 = new ItemSearchCond("x", "", today, "B");
        ItemSearchCond cond34 = new ItemSearchCond("", "b", today, "F");

        // 조건 4
        ItemSearchCond cond41 = new ItemSearchCond("x", "a", today, "B");


        // 검색에 대한 result가 없는 경우

        ItemSearchCond cond5 = new ItemSearchCond("hello", "", "", "");
        ItemSearchCond cond6 = new ItemSearchCond("", "", "", "B23");
        ItemSearchCond cond27 = new ItemSearchCond("", "", otherDay, "B");
        ItemSearchCond cond35 = new ItemSearchCond("", "abc", today, "B");
        ItemSearchCond cond42 = new ItemSearchCond("x", "a", today, "Hcd");


        // then
        // 결과 확인(개수로)
        // 조건 1
        testResultV2(pageablePage0Size10, cond1, rCount);
        testResultV2(pageablePage0Size10, cond2, rCount);
        testResultV2(pageablePage0Size10, cond3, rCount + r2Count);
        testResultV2(pageablePage0Size10, cond4, 0);
        // 조건 2
        testResultV2(pageablePage0Size10, cond21, rCount);
        testResultV2(pageablePage0Size10, cond22, rCount);
        testResultV2(pageablePage0Size10, cond23, rCount);
        testResultV2(pageablePage0Size10, cond24, rCount);
        testResultV2(pageablePage0Size10, cond25, rCount);
        testResultV2(pageablePage0Size10, cond26, r2Count);
        // 조건 3
        testResultV2(pageablePage0Size10, cond31, rCount);
        testResultV2(pageablePage0Size10, cond32, rCount);
        testResultV2(pageablePage0Size10, cond33, rCount);
        testResultV2(pageablePage0Size10, cond34, r2Count);

        // 조건 4
        testResultV2(pageablePage3Size5, cond41, rCount);
//        testResultV2(pageablePage3Size5, cond41, 0);// 에러 발생


        // 검색에 대한 result가 없는 경우
        testResultV2(pageablePage0Size10, cond5, 0);
        testResultV2(pageablePage0Size10, cond6, 0);
        testResultV2(pageablePage0Size10, cond27, 0);
        testResultV2(pageablePage0Size10, cond35, 0);
        testResultV2(pageablePage2Size2, cond42, 0);
    }

    // findAllInfo test result
    private void testResultV2(Pageable pageable, ItemSearchCond cond, int count) {
        Page<Item> result = itemJpaRepository.findAllInfo(cond, pageable);
        assertThat(result.getTotalElements()).isEqualTo(count);
    }



    /**
     * 상품 이름, 판매자 검색 확인
     */
    @Test
    @Rollback(value = false)
    public void 상품이름_판매자로_검색_확인() {
        // given
        String itemName = "x";// 상품 이름
        String sellerId = "a";// 판매자아이디 맞음

        String notSellerId = "b";// 판매자아이디 아님
        // when
        Item findItem = itemJpaRepository.findByNameAndSellerId(itemName, sellerId).orElseThrow();

        // then
        assertThat(findItem.getName()).isEqualTo(itemName);
        assertThat(findItem.getMember()).isEqualTo(member);

        // 판매자 아이디 없음
        Assertions.assertThrows(NoSuchElementException.class, () -> itemJpaRepository.findByNameAndSellerId(itemName, notSellerId).orElseThrow());
//        Assertions.assertThrows(IllegalStateException.class, () -> itemJpaRepository.findByNameAndSellerId(itemName, notSellerId).orElseThrow()); // 에러 발생
    }


}
