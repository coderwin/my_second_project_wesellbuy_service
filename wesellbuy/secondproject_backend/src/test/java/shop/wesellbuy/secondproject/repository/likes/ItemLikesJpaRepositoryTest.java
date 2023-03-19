package shop.wesellbuy.secondproject.repository.likes;

import com.querydsl.core.Tuple;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import shop.wesellbuy.secondproject.domain.Item;
import shop.wesellbuy.secondproject.domain.Member;
import shop.wesellbuy.secondproject.domain.QItem;
import shop.wesellbuy.secondproject.domain.QMember;
import shop.wesellbuy.secondproject.domain.item.Book;
import shop.wesellbuy.secondproject.domain.item.Furniture;
import shop.wesellbuy.secondproject.domain.item.HomeAppliances;
import shop.wesellbuy.secondproject.domain.item.ItemPicture;
import shop.wesellbuy.secondproject.domain.likes.ItemLikes;
import shop.wesellbuy.secondproject.repository.item.ItemJpaRepository;
import shop.wesellbuy.secondproject.web.item.BookForm;
import shop.wesellbuy.secondproject.web.item.FurnitureForm;
import shop.wesellbuy.secondproject.web.item.HomeAppliancesForm;
import shop.wesellbuy.secondproject.web.item.ItemForm;
import shop.wesellbuy.secondproject.web.member.MemberForm;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static shop.wesellbuy.secondproject.domain.likes.QItemLikes.itemLikes;

@SpringBootTest
@Transactional
@Slf4j
public class ItemLikesJpaRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    ItemLikesJpaRepository itemLikesJpaRepository;
    @Autowired
    ItemJpaRepository itemJpaRepository;

    Member member; // 등록 회원
    Member member2; // 등록 회원
    Member member3; // 등록 회원
    Member member4; // 등록 회원
    Member member5; // 등록 회원
    Member member6; // 등록 회원
    Member member7; // 등록 회원
    Member member8; // 등록 회원
    Member member9; // 등록 회원
    Member member10; // 등록 회원

    Item item; // 등록 상품
    Item item2; // 등록 상품
    Item item3; // 등록 상품

    // create member/customerService object
    @BeforeEach
    public void init() {

        // 회원 정보 저장
        MemberForm memberForm1 = new MemberForm("a", "a","123", "a@a", "01012341234", "0511231234", "korea1", "b", "h", "h", "123", null);
        MemberForm memberForm2 = new MemberForm("b", "b","123", "a@a", "01012341234", "0511231234", "korea1", "b", "h", "h", "123", null);
        MemberForm memberForm3 = new MemberForm("c", "c","123", "a@a", "01012341234", "0511231234", "korea1", "b", "h", "h", "123", null);
        MemberForm memberForm4 = new MemberForm("c", "c","123", "a@a", "01012341234", "0511231234", "korea1", "b", "h", "h", "123", null);
        MemberForm memberForm5 = new MemberForm("c", "c","123", "a@a", "01012341234", "0511231234", "korea1", "b", "h", "h", "123", null);
        MemberForm memberForm6 = new MemberForm("c", "c","123", "a@a", "01012341234", "0511231234", "korea1", "b", "h", "h", "123", null);
        MemberForm memberForm7 = new MemberForm("c", "c","123", "a@a", "01012341234", "0511231234", "korea1", "b", "h", "h", "123", null);
        MemberForm memberForm8 = new MemberForm("c", "c","123", "a@a", "01012341234", "0511231234", "korea1", "b", "h", "h", "123", null);
        MemberForm memberForm9 = new MemberForm("c", "c","123", "a@a", "01012341234", "0511231234", "korea1", "b", "h", "h", "123", null);
        MemberForm memberForm10 = new MemberForm("c", "c","123", "a@a", "01012341234", "0511231234", "korea1", "b", "h", "h", "123", null);

        Member member = Member.createMember(memberForm1);
        Member member2 = Member.createMember(memberForm2);
        Member member3 = Member.createMember(memberForm3);
        Member member4 = Member.createMember(memberForm4);
        Member member5 = Member.createMember(memberForm5);
        Member member6 = Member.createMember(memberForm6);
        Member member7 = Member.createMember(memberForm7);
        Member member8 = Member.createMember(memberForm8);
        Member member9 = Member.createMember(memberForm9);
        Member member10 = Member.createMember(memberForm10);

        em.persist(member);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
        em.persist(member5);
        em.persist(member6);
        em.persist(member7);
        em.persist(member8);
        em.persist(member9);
        em.persist(member10);


        this.member = member;
        this.member2 = member2;
        this.member3 = member3;
        this.member4 = member4;
        this.member5 = member5;
        this.member6 = member6;
        this.member7 = member7;
        this.member8 = member8;
        this.member9 = member9;
        this.member10 = member10;

        // 상품 저장
        BookForm bookForm = new BookForm(10, 1000, "x", "x is...", null, "ed", "ok");
        item = Book.createBook(bookForm, member);
        BookForm bookForm2 = new BookForm(10, 1000, "x", "x is...", null, "ed", "ok");
        item2 = Book.createBook(bookForm2, member2);
        BookForm bookForm3 = new BookForm(10, 1000, "x", "x is...", null, "ed", "ok");
        item3 = Book.createBook(bookForm3, member3);

        em.persist(item);
        em.persist(item2);
        em.persist(item3);
    }

    @Test
//    @Rollback(false)
    public void 상품_좋아요_저장() {
        // given
        ItemLikes itemLikes = ItemLikes.createItemLikes(member, item);

        // when
        itemLikesJpaRepository.save(itemLikes);

        // then
        ItemLikes findItemLikes = itemLikesJpaRepository.findById(itemLikes.getNum()).orElseThrow();
        assertThat(findItemLikes).isEqualTo(itemLikes);
    }

    @Test
//    @Rollback(false)
    public void 상품_좋아요_삭제() {
        // given
        ItemLikes itemLikes = ItemLikes.createItemLikes(member, item);

        em.persist(itemLikes);
        // when1
        // then
        ItemLikes findItemLikes = itemLikesJpaRepository.findById(itemLikes.getNum()).orElseThrow();
        assertThat(findItemLikes).isEqualTo(itemLikes);

        // when2
        // delete
        itemLikesJpaRepository.delete(findItemLikes);

        em.flush();
        em.clear();
        // then
        assertThrows(NoSuchElementException.class, () -> itemLikesJpaRepository.findById(itemLikes.getNum()).orElseThrow());
        assertThatThrownBy(() -> itemLikesJpaRepository.findById(itemLikes.getNum()).orElseThrow())
                .isInstanceOf(NoSuchElementException.class);

    }

    @Test
//    @Rollback(false)
    public void 모두_가져오기_상품_좋아요_by_memberNum() {

        // given
        ItemLikes itemLikes = ItemLikes.createItemLikes(member, item);
        ItemLikes itemLikes2 = ItemLikes.createItemLikes(member, item2);
        ItemLikes itemLikes3 = ItemLikes.createItemLikes(member, item3);
        ItemLikes itemLikes4 = ItemLikes.createItemLikes(member2, item3);

        em.persist(itemLikes);
        em.persist(itemLikes2);
        em.persist(itemLikes3);
        em.persist(itemLikes4);
        // when
        List<ItemLikes> itemLikesList = itemLikesJpaRepository.findAllInfoById(member.getNum());
        List<ItemLikes> itemLikesList2 = itemLikesJpaRepository.findAllInfoById(member2.getNum());

        // then
        assertThat(itemLikesList).containsExactly(itemLikes, itemLikes2, itemLikes3);
        assertThat(itemLikesList2).containsExactly(itemLikes4);
    }

    @Test
//    @Rollback(false)
    public void 상품_좋아요_개수에따른_순위_확인() {
        // given
        // 좋아요 만들기
        // item 좋아요
        em.persist(ItemLikes.createItemLikes(member, item));
        em.persist(ItemLikes.createItemLikes(member2, item));
        em.persist(ItemLikes.createItemLikes(member3, item));
        em.persist(ItemLikes.createItemLikes(member4, item));
        em.persist(ItemLikes.createItemLikes(member5, item));
        // item2 좋아요
        em.persist(ItemLikes.createItemLikes(member, item2));
        em.persist(ItemLikes.createItemLikes(member2, item2));
        em.persist(ItemLikes.createItemLikes(member3, item2));
        em.persist(ItemLikes.createItemLikes(member4, item2));
        em.persist(ItemLikes.createItemLikes(member5, item2));
//        em.persist(ItemLikes.createItemLikes(member6, item2));
//        em.persist(ItemLikes.createItemLikes(member7, item2));
//        em.persist(ItemLikes.createItemLikes(member8, item2));
//        em.persist(ItemLikes.createItemLikes(member9, item2));
//        em.persist(ItemLikes.createItemLikes(member10, item2));
        // item3 좋아요
        em.persist(ItemLikes.createItemLikes(member, item3));
        em.persist(ItemLikes.createItemLikes(member2, item3));
        em.persist(ItemLikes.createItemLikes(member3, item3));

        // when
        // item 좋아요 개수 가져오기
        List<Tuple> result = itemLikesJpaRepository.findRank();

//        QMember m2 = new QMember("m");
//        QItem i2 = new QItem("i");

        // then
        assertThat(result.get(0).get(itemLikes.count())).isEqualTo(5L);
        assertThat(result.get(0).get(QMember.member).getId()).isEqualTo(member.getId());
        assertThat(result.get(0).get(QItem.item).getName()).isEqualTo(item.getName());
        assertThat(result.get(0).get(QItem.item)).isEqualTo(item);

        assertThat(result.get(1).get(itemLikes.count())).isEqualTo(5L);
        assertThat(result.get(1).get(QMember.member).getId()).isEqualTo(member2.getId());
        assertThat(result.get(1).get(QItem.item).getName()).isEqualTo(item2.getName());
        assertThat(result.get(1).get(QItem.item)).isEqualTo(item2);

        assertThat(result.get(2).get(itemLikes.count())).isEqualTo(3L);
        assertThat(result.get(2).get(QMember.member).getId()).isEqualTo(member3.getId());
        assertThat(result.get(2).get(QItem.item).getName()).isEqualTo(item3.getName());
        assertThat(result.get(2).get(QItem.item)).isEqualTo(item3);

        int i = 1;
        for(Tuple subResult : result) {

            log.info("Tuple " + i + ": {}", subResult);
            i++;
        }

        var isTrue = false;
        while(!isTrue) {
            log.info("member: " + member);
            log.info("member2: " + member2);
            log.info("member3: " + member3);

            isTrue = true;
        }
    }

    @Test
//    @Rollback(false)
    public void 상품_좋아요_개수에따른_순위_확인_V2() {
        // given
        // 좋아요 만들기
        // item 좋아요
        em.persist(ItemLikes.createItemLikes(member, item));
        em.persist(ItemLikes.createItemLikes(member2, item));
        em.persist(ItemLikes.createItemLikes(member3, item));
        em.persist(ItemLikes.createItemLikes(member4, item));
        em.persist(ItemLikes.createItemLikes(member5, item));
        // item2 좋아요
        em.persist(ItemLikes.createItemLikes(member, item2));
        em.persist(ItemLikes.createItemLikes(member2, item2));
        em.persist(ItemLikes.createItemLikes(member3, item2));
        em.persist(ItemLikes.createItemLikes(member4, item2));
        em.persist(ItemLikes.createItemLikes(member5, item2));
//        em.persist(ItemLikes.createItemLikes(member6, item2));
//        em.persist(ItemLikes.createItemLikes(member7, item2));
//        em.persist(ItemLikes.createItemLikes(member8, item2));
//        em.persist(ItemLikes.createItemLikes(member9, item2));
//        em.persist(ItemLikes.createItemLikes(member10, item2));
        // item3 좋아요
        em.persist(ItemLikes.createItemLikes(member, item3));
        em.persist(ItemLikes.createItemLikes(member2, item3));
        em.persist(ItemLikes.createItemLikes(member3, item3));

        // when
        // item 좋아요 개수 가져오기
        // rank 버전 by using dto
        List<ItemRankDto> result = itemLikesJpaRepository.findRankV2();

        // then
        int i = 0;
        Long[] values = {5L, 5L, 3L};
        // 좋아요 개수 비교
        for(ItemRankDto subResult : result) {
            log.info("ItemRankDto " + i + ": {}", subResult);
            assertThat(result.get(i).getCount()).isEqualTo(values[i]);
            i++;
        }
    }

    /**
     * findRankV4 확인
     * -> select에 itemLikes.count()가 없을 때
     * -> 에러가 나나?
     *    test 후 -> 에러가 안 난다!
     */
    @Test
    public void findRankV4_test() {

        // given
        // 10개의 상품 생성
        List<ItemPicture> itemPictureList = new ArrayList<>();
        itemPictureList.add(ItemPicture.createItemPicture("a", "a"));
        itemPictureList.add(ItemPicture.createItemPicture("a1", "a2"));

        FurnitureForm furnitureForm1 = new FurnitureForm(10, 2000, "책상", "잘 만들어졌어요~", itemPictureList, "hansem");
        Item item1 = Furniture.createFurniture(furnitureForm1, member);
        FurnitureForm furnitureForm2 = new FurnitureForm(10, 2000, "책상", "잘 만들어졌어요~", itemPictureList, "hansem");
        Item item2 = Furniture.createFurniture(furnitureForm2, member2);
        FurnitureForm furnitureForm3 = new FurnitureForm(10, 2000, "책상", "잘 만들어졌어요~", itemPictureList, "hansem");
        Item item3 = Furniture.createFurniture(furnitureForm3, member);
        FurnitureForm furnitureForm4 = new FurnitureForm(10, 2000, "책상", "잘 만들어졌어요~", itemPictureList, "hansem");
        Item item4 = Furniture.createFurniture(furnitureForm4, member2);
        FurnitureForm furnitureForm5 = new FurnitureForm(10, 2000, "책상", "잘 만들어졌어요~", itemPictureList, "hansem");
        Item item5 = Furniture.createFurniture(furnitureForm5, member);

        HomeAppliancesForm haForm1 = new HomeAppliancesForm(20, 2000, "냉장고", "잘 만들어졌어요~", itemPictureList, "samsug");
        Item item6 = HomeAppliances.createHomeAppliances(haForm1, member);
        HomeAppliancesForm haForm2 = new HomeAppliancesForm(20, 2000, "냉장고", "잘 만들어졌어요~", itemPictureList, "samsug");
        Item item7 = HomeAppliances.createHomeAppliances(haForm2, member3);
        HomeAppliancesForm haForm3 = new HomeAppliancesForm(20, 2000, "냉장고", "잘 만들어졌어요~", itemPictureList, "samsug");
        Item item8 = HomeAppliances.createHomeAppliances(haForm3, member3);

        BookForm bForm = new BookForm(5, 2000, "책1", "잘 만들어졌어요~", itemPictureList, "곰돌이푸", "곰돌이출판사");
        Item item9 = Book.createBook(bForm, member);

        ItemForm iForm = new ItemForm(10, 2000, "책상", "잘 만들어졌어요~", itemPictureList);
        Item item10 = Item.createItem(iForm, member);

        itemJpaRepository.save(item1);
        itemJpaRepository.save(item2);
        itemJpaRepository.save(item3);
        itemJpaRepository.save(item4);
        itemJpaRepository.save(item5);
        itemJpaRepository.save(item6);
        itemJpaRepository.save(item7);
        itemJpaRepository.save(item8);
        itemJpaRepository.save(item9);
        itemJpaRepository.save(item10);


        // when
        // 좋아요 생성(4, 6, 5, 1, 4, 3, 6, 0, 4, 1)
        Member[] members = {member, member2, member3, member4, member5, member6};
        Item[] items = {item1, item2, item3, item4, item5,
                item6, item7, item8, item9 ,item10};
        int[] counts = {4, 6, 5, 2, 4, 3, 6, 1, 4, 2}; // 좋아요수

        for(int i = 0; i < counts.length; i++) {
            chooseLikes(members, counts[i], items[i]);
        }

        // item5 삭제
        item5.changeStatus();

        // then
        // findRankV4 불러오기
        List<Item> rankList = itemLikesJpaRepository.findRankV4();

        int i = 0;
        for(Item item : rankList) {

            log.info("item + " + i + " : {}", item);
            i++;
        }

        // item 출력 개수
        assertThat(rankList.size()).isEqualTo(9);


    }

    // 좋아요를 여러번 수행하기
    private void chooseLikes(Member[] members, int count, Item items) {
        for(int i = 0; i < count; i++) {
            makeLikes(members[i], items);
        }
    }

    // 좋아요 만들기
    private void makeLikes(Member member, Item item) {
        ItemLikes itemLikes = ItemLikes.createItemLikes(member, item);
        itemLikesJpaRepository.save(itemLikes);
    }




}
