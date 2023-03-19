package shop.wesellbuy.secondproject.service.reply.item;

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
import shop.wesellbuy.secondproject.domain.Item;
import shop.wesellbuy.secondproject.domain.Member;
import shop.wesellbuy.secondproject.domain.item.Book;
import shop.wesellbuy.secondproject.domain.item.Furniture;
import shop.wesellbuy.secondproject.domain.item.HomeAppliances;
import shop.wesellbuy.secondproject.domain.item.ItemPicture;
import shop.wesellbuy.secondproject.domain.member.SelfPicture;
import shop.wesellbuy.secondproject.domain.reply.ItemReply;
import shop.wesellbuy.secondproject.domain.reply.ReplyStatus;
import shop.wesellbuy.secondproject.repository.item.ItemJpaRepository;
import shop.wesellbuy.secondproject.repository.member.MemberJpaRepository;
import shop.wesellbuy.secondproject.repository.reply.item.ItemReplyJpaRepository;
import shop.wesellbuy.secondproject.repository.reply.item.ItemReplySearchCond;
import shop.wesellbuy.secondproject.web.item.BookForm;
import shop.wesellbuy.secondproject.web.item.FurnitureForm;
import shop.wesellbuy.secondproject.web.item.HomeAppliancesForm;
import shop.wesellbuy.secondproject.web.member.MemberForm;
import shop.wesellbuy.secondproject.web.reply.ReplyDetailForm;
import shop.wesellbuy.secondproject.web.reply.ReplyForm;
import shop.wesellbuy.secondproject.web.reply.ReplyUpdateForm;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
public class ItemReplyServiceTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;
    @Autowired
    ItemJpaRepository itemJpaRepository;
    @Autowired
    ItemReplyService itemReplyService;
    @Autowired
    ItemReplyJpaRepository itemReplyJpaRepository;

    Member member; // test 회원
    Member member2; // test 회원
    Member member3; // test 회원

    Item item; // test 상품
    Item item2; // test 상품
    Item item3; // test 상품
    @BeforeEach
    public void init() {

        log.info("test init 시작");

        // 회원 생성
        SelfPicture selfPicture = SelfPicture.createSelfPicture("test1", "test2");
        SelfPicture selfPicture3 = SelfPicture.createSelfPicture("test1", "test2");
        MemberForm memberForm1 = new MemberForm("a", "a1", "123", "a", "a@a", "01012341234", "0511231234", "korea", "b", "h", "h", selfPicture);
        Member member = Member.createMember(memberForm1);
        MemberForm memberForm2 = new MemberForm("a", "b1", "123", "a", "a@a", "01012341234", "0511231234", "korea", "b", "h", "h", null);
        Member member2 = Member.createMember(memberForm2);
        MemberForm memberForm3 = new MemberForm("a", "c1", "123", "a", "a@a", "01012341234", "0511231234", "korea", "b", "h", "h", selfPicture3);
        Member member3 = Member.createMember(memberForm3);

        memberJpaRepository.save(member);
        memberJpaRepository.save(member2);
        memberJpaRepository.save(member3);
        this.member = member;
        this.member2 = member2;
        this.member3 = member3;

        // 상품 생성
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
        this.item3 = item3;

        log.info("test init 끝");

    }

    /**
     * 댓글 저장 확인
     */
    @Test
//    @Rollback(value = false)
    public void 댓글_저장_확인() {
        // given
        ReplyForm replyForm = new ReplyForm("멋지네요~");
        // when
        // 저장하기
        int replyNum = itemReplyService.save(replyForm, member.getNum(), item.getNum());

        // then
        ItemReply result = itemReplyJpaRepository.findById(replyNum).orElseThrow();

        assertThat(result.getNum()).isEqualTo(replyNum);
        assertThat(result.getContent()).isEqualTo(replyForm.getContent());
    }

    /**
     * 댓글 수정 확인
     */
    @Test
//    @Rollback(value = false)
    public void 댓글_수정_확인() {
        // given
        // 댓글 생성
        ReplyForm replyForm = new ReplyForm("멋지네요~");
        int replyNum = itemReplyService.save(replyForm, member.getNum(), item.getNum());

        // when
        // 댓글 수정
        ReplyUpdateForm replyUpdateForm = new ReplyUpdateForm(replyNum, "아주 멋지네요~");

        itemReplyService.update(replyUpdateForm);
        // then
        ItemReply findReply = itemReplyJpaRepository.findById(replyNum).orElseThrow();

        // 수정 상태 content
        assertThat(findReply.getContent()).isEqualTo(replyUpdateForm.getContent());
        // 처음 상태 content
        assertThat(findReply.getContent()).isNotEqualTo(replyForm.getContent());
    }

    /**
     * 댓글 삭제 확인
     * -> status : R -> D로 바뀜
     * -> 상품 자세히 보기에서 확인 가능(안 나타나야함) : ItemService
     */
    @Test
//    @Rollback(value = false)
    public void 댓글_삭제_확인() {
        // given
        ReplyForm replyForm = new ReplyForm("멋지네요~");
        int replyNum = itemReplyService.save(replyForm, member3.getNum(), item2.getNum());

        // 현재 상태 확인(R)
        ItemReply findItemReply1 = itemReplyJpaRepository.findById(replyNum).orElseThrow();

        assertThat(findItemReply1.getStatus()).isEqualTo(ReplyStatus.R);

        // when
        itemReplyService.delete(replyNum);

        // then
        ItemReply findItemReply2 = itemReplyJpaRepository.findById(replyNum).orElseThrow();

        assertThat(findItemReply2.getStatus()).isEqualTo(ReplyStatus.D);
    }

//    -------------------------methods using for admin start----------------------------------

    /**
     * 조건에 맞게 댓글 모두 불러오기 확인
     * -> 관리자(admin)가 사용
     */
    @Test
//    @Rollback(value = false)
    public void 조건에_맞게_모두_불러오기() {
        // given
        // 더미 데이터 생성
        int amount = 100;
        var rCount = 0; // 댓글 개수
        var r2Count = 0; // 댓글 개수
        var r3Count = 0; // 댓글 개수
        ReplyForm replyForm = null;
        ItemReply itemReply = null;
        for(int i = 0; i < amount; i++) {
            if(i % 3 == 1) {
                replyForm = new ReplyForm("편리해요");
                itemReply = ItemReply.createItemReply(replyForm, member, item);
                rCount += 1;
            } else if(i * 3 == 2) {
                replyForm = new ReplyForm("좋아요~~~~~");
                itemReply = ItemReply.createItemReply(replyForm, member2, item);
                r2Count += 1;
            } else {
                replyForm = new ReplyForm("멋지네요~~~~~");
                itemReply = ItemReply.createItemReply(replyForm, member3, item2);
                r3Count += 1;
            }

            itemReplyJpaRepository.save(itemReply);

            // 삭제하기
            if(i % 4 == 0) {
                itemReply.delete();
            }
        }

        // 페이지 생성
        Pageable page0size10 = PageRequest.of(0, 10);
        Pageable page2size10 = PageRequest.of(0, 10);

        // 날짜 조건
        String today = "2023-02-02";
        String otherDay = "2023-02-03";

        // when
        // 조건 0
        ItemReplySearchCond cond0 = new ItemReplySearchCond("", "", "");
        // 조건 1
        ItemReplySearchCond cond11 = new ItemReplySearchCond("a1", "", "");
        ItemReplySearchCond cond12 = new ItemReplySearchCond("", "요~~", "");
        ItemReplySearchCond cond13 = new ItemReplySearchCond("", "", today);
        // 조건 2
        ItemReplySearchCond cond21 = new ItemReplySearchCond("b1", "아요", "");
        ItemReplySearchCond cond22 = new ItemReplySearchCond("b1", "", today);
        ItemReplySearchCond cond23 = new ItemReplySearchCond("", "요~", today);
        // 조건 3
        ItemReplySearchCond cond31 = new ItemReplySearchCond("c1", "멋지네요", today);


        // 조건이 잘못 전달될 때
        ItemReplySearchCond cond14 = new ItemReplySearchCond("a123", "", "");
        ItemReplySearchCond cond24 = new ItemReplySearchCond("b123", "", today);
        ItemReplySearchCond cond32 = new ItemReplySearchCond("c1", "멋지네요!", today);

        // then
        // 조건 0
        testResultForAdmin(cond0, page0size10, rCount + r2Count + r3Count);
        // 조건 1
        testResultForAdmin(cond11, page2size10, rCount);
        testResultForAdmin(cond12, page2size10, r2Count + r3Count);
        testResultForAdmin(cond13, page2size10, rCount + r2Count + r3Count);
        // 조건 2
        testResultForAdmin(cond21, page0size10,  r2Count);
        testResultForAdmin(cond22, page0size10, r2Count);
        testResultForAdmin(cond23, page0size10, r2Count + r3Count);
        // 조건 3
        testResultForAdmin(cond31, page2size10,  r3Count);

        // 조건이 잘못 전달될 때
        testResultForAdmin(cond14, page2size10, 0);
        testResultForAdmin(cond24, page0size10, 0);
        testResultForAdmin(cond32, page2size10,  0);

    }

    /**
     * test 결과에 사용
     */
    private void testResultForAdmin(ItemReplySearchCond cond, Pageable pageable, int count) {
        Page<ReplyDetailForm> result = itemReplyService.selectListForAdmin(cond, pageable);
        assertThat(result.getTotalElements()).isEqualTo(count);
    }


//    -------------------------methods using for admin admin----------------------------------

}
