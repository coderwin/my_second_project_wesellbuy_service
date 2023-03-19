package shop.wesellbuy.secondproject.service.likes;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import shop.wesellbuy.secondproject.domain.Item;
import shop.wesellbuy.secondproject.domain.Member;
import shop.wesellbuy.secondproject.domain.item.Book;
import shop.wesellbuy.secondproject.domain.item.Furniture;
import shop.wesellbuy.secondproject.domain.item.HomeAppliances;
import shop.wesellbuy.secondproject.domain.item.ItemPicture;
import shop.wesellbuy.secondproject.domain.member.SelfPicture;
import shop.wesellbuy.secondproject.repository.item.ItemJpaRepository;
import shop.wesellbuy.secondproject.repository.member.MemberJpaRepository;
import shop.wesellbuy.secondproject.web.item.BookForm;
import shop.wesellbuy.secondproject.web.item.FurnitureForm;
import shop.wesellbuy.secondproject.web.item.HomeAppliancesForm;
import shop.wesellbuy.secondproject.web.likes.ItemLikesListForm;
import shop.wesellbuy.secondproject.web.member.MemberForm;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * comment - item의 dtype이 왜 'item'으로 나오지?
 *           B, F, HA로 안 나오고
 *
 *           -> 해결
 *              -> createItem()이 아니라 createBook()을 사용해서
 *                 Item 객체가 아닌 Book 객체를 생성해야 한다.
 */
@SpringBootTest
@Transactional
@Slf4j
public class ItemLikesServiceTest {

    @Autowired
    ItemLikesService itemLikesService;
    @Autowired
    MemberJpaRepository memberJpaRepository;
    @Autowired
    ItemJpaRepository itemJpaRepository;

    Member member; // test 회원
    Member member2; // test 회원

    Item item; // test 상품
    Item item2; // test 상품

    @BeforeEach
    public void init() {

        log.info("test init 시작");

        /// 회원 생성
        SelfPicture selfPicture = SelfPicture.createSelfPicture("test1", "test2");
        SelfPicture selfPicture3 = SelfPicture.createSelfPicture("test1", "test2");
        // 이미지 있음
        MemberForm memberForm1 = new MemberForm("a", "a1", "123", "a", "a@a", "01012341234", "0511231234", "korea", "b", "h", "h", selfPicture);
        // 이미지 없음
        MemberForm memberForm2 = new MemberForm("a", "a1", "123", "a", "a@a", "01012341234", "0511231234", "korea", "b", "h", "h", null);
        Member member = Member.createMember(memberForm1);
        Member member2 = Member.createMember(memberForm2);
        memberJpaRepository.save(member);
        memberJpaRepository.save(member2);

        this.member = member;
        this.member2 = member2;

        /// 상품 생성
        List<ItemPicture> itemPictureList = new ArrayList<>();
        itemPictureList.add(ItemPicture.createItemPicture("a", "a"));
        itemPictureList.add(ItemPicture.createItemPicture("a1", "a2"));

        HomeAppliancesForm homeAppliancesForm = new HomeAppliancesForm(20, 5000, "청소기", "쓱쓱 청소 돼요", new ArrayList<>(), "samsung");
        Item item = HomeAppliances.createHomeAppliances(homeAppliancesForm, member2);
        FurnitureForm furnitureForm = new FurnitureForm(10, 2000, "책상", "잘 만들어졌어요~", itemPictureList, "hansem");
        Item item2 = Furniture.createFurniture(furnitureForm, member);
        BookForm bookForm = new BookForm(10, 1000, "book1", "x is...", itemPictureList, "ed", "ok");
        Item item3 = Book.createBook(bookForm, member);

        itemJpaRepository.save(item);
        itemJpaRepository.save(item2);
        itemJpaRepository.save(item3);

        this.item = item;
        this.item2 = item2;

        log.info("test init 끝");

    }

    /**
     * 상품좋아요 저장 & 모든 좋아요 불러오기 확인
     */
    @Test
    @Rollback(value = false)
    public void 저장_모든_좋아요_불러오기_확인() {
        // given
        // 상품좋아요 저장
        int itemNum1 = itemLikesService.save(item.getNum(), member.getNum());
        int itemNum2 = itemLikesService.save(item.getNum(), member2.getNum());
        int itemNum3 = itemLikesService.save(item2.getNum(), member.getNum());

        // when
        // item 좋아요 불러오기
        List<ItemLikesListForm> itemLikesListByMembmer = itemLikesService.selectList(member.getNum());
        // item2 좋아요 불러오기
        List<ItemLikesListForm> itemLikesListByMembmer2 = itemLikesService.selectList(member2.getNum());

        // then
        assertThat(itemLikesListByMembmer.size()).isEqualTo(2);
        assertThat(itemLikesListByMembmer2.size()).isEqualTo(1);
    }

    /**
     * 회원이 좋아요 클릭한 모든 상품 좋아요 불러오기 확인
     *
     * comment : 2/13 ~
     *           > 아직 test 안 해봄
     */
    @Test
//    @Rollback(value = false)
    public void 회원의_모든_상품_좋아요_불러오기_확인() {
        // given
        // 상품좋아요 저장
        int itemNum1 = itemLikesService.save(item.getNum(), member.getNum());
        int itemNum2 = itemLikesService.save(item.getNum(), member2.getNum());
        int itemNum3 = itemLikesService.save(item2.getNum(), member.getNum());

        // when
        // item 좋아요 불러오기
        List<Integer> itemLikesListByMember = itemLikesService.selectListForItemList(member.getNum());
        // item2 좋아요 불러오기
        List<Integer> itemLikesListByMember2 = itemLikesService.selectListForItemList(member2.getNum());

        // then
        assertThat(itemLikesListByMember.size()).isEqualTo(2);
        assertThat(itemLikesListByMember2.size()).isEqualTo(1);
    }

    /**
     * 상품좋아요 삭제 확인
     */
    @Test
//    @Rollback(value = false)
    public void 삭제_확인() {
        // given
        // 상품좋아요 저장
        int itemNum1 = itemLikesService.save(item.getNum(), member.getNum());
        int itemNum2 = itemLikesService.save(item2.getNum(), member.getNum());
        int itemNum3 = itemLikesService.save(item2.getNum(), member2.getNum());

        // when
        // 삭제하기
        itemLikesService.delete(itemNum2);

        // then
        List<ItemLikesListForm> result = itemLikesService.selectList(member.getNum());

        assertThat(result.size()).isEqualTo(1);
    }













}
