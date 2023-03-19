package shop.wesellbuy.secondproject.repository.reply.item;

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
import org.springframework.transaction.annotation.Transactional;
import shop.wesellbuy.secondproject.domain.Item;
import shop.wesellbuy.secondproject.domain.Member;
import shop.wesellbuy.secondproject.domain.item.Book;
import shop.wesellbuy.secondproject.domain.reply.ItemReply;
import shop.wesellbuy.secondproject.repository.item.ItemJpaRepository;
import shop.wesellbuy.secondproject.web.item.BookForm;
import shop.wesellbuy.secondproject.web.member.MemberForm;
import shop.wesellbuy.secondproject.web.reply.ReplyForm;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
public class ItemReplyRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    ItemReplyJpaRepository itemReplyJpaRepository;

    @Autowired
    ItemJpaRepository itemJpaRepository;

    Member member; // 등록 회원
    Member member2; // 등록 회원
    Member member3; // 등록 회원

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
        Member member = Member.createMember(memberForm1);
        Member member2 = Member.createMember(memberForm2);
        Member member3 = Member.createMember(memberForm3);

        em.persist(member);
        em.persist(member2);
        em.persist(member3);

        this.member = member;
        this.member2 = member2;
        this.member3 = member3;

        // 상품 저장
        BookForm bookForm = new BookForm(10, 1000, "x", "x is...", null, "ed", "ok");
        item = Book.createBook(bookForm, member);
        BookForm bookForm2 = new BookForm(10, 1000, "x", "x is...", null, "ed", "ok");
        item2 = Book.createBook(bookForm2, member2);
        BookForm bookForm3 = new BookForm(10, 1000, "x", "x is...", null, "ed", "ok");
        item3 = Book.createBook(bookForm3, member3);

        itemJpaRepository.save(item);
        itemJpaRepository.save(item2);
        itemJpaRepository.save(item3);
    }

    /**
     * writer : 이호진
     * init : 2023.01.19
     * updated by writer :
     * update :
     * description : save Test
     */
    @Test
//    @Rollback(false)
    public void 상품_댓글_저장() {
        // given
        ReplyForm replyForm = new ReplyForm("hello2");
        ItemReply itemReply = ItemReply.createItemReply(replyForm, member, item);

        itemReplyJpaRepository.save(itemReply);

        // when
        ItemReply findItemReply = itemReplyJpaRepository.findById(itemReply.getNum()).orElseThrow();

        // then
        assertThat(findItemReply).isEqualTo(itemReply);
    }

    @Test
//    @Rollback(value = false)
    public void 상품_모두_가져오기_By_조건_페이징() {
        // given
        // 100개의 더미 파일 만들기
        Long count = 100L;
        int rCount = 0; // 게시글 개수 member의
        int r2Count = 0; // 게시글 개수 member2의
        int r3Count = 0; // 게시글 개수 member3의

        ItemReply reply = null;
        for (int i = 0; i < count; i++) {

            if (i % 3 == 0) {
                ReplyForm replyForm = new ReplyForm("hello11");
                reply = ItemReply.createItemReply(replyForm, member, item);
                rCount += 1;
            } else if (i % 3 == 1) {
                ReplyForm replyForm = new ReplyForm("hello22");
                reply = ItemReply.createItemReply(replyForm, member2, item2);
                r2Count += 1;
            } else {
                ReplyForm replyForm = new ReplyForm("hello33");
                reply = ItemReply.createItemReply(replyForm, member3, item3);
                r3Count += 1;
            }

            em.persist(reply);
        }

        // 페이지, 사이즈 정하기
//        Pageable pageablePage0Size100 = PageRequest.of(0, 100);
        Pageable pageablePage0Size10 = PageRequest.of(0, 10);
        Pageable pageablePage1Size10 = PageRequest.of(1, 10);
        Pageable pageablePage3Size5 = PageRequest.of(3, 5);
        Pageable pageablePage2Size2 = PageRequest.of(2, 2);

        // 날짜 condition
        String today = "2023-01-27";
        String otherDay = "2023-01-28";

        // when
        // 검색조건 생성
        // 조건 1
        ItemReplySearchCond cond1 = new ItemReplySearchCond("a", null, null);
        ItemReplySearchCond cond2 = new ItemReplySearchCond(null, "hello11", null);
        ItemReplySearchCond cond3 = new ItemReplySearchCond(null, null, today);
        // 조건 2
        ItemReplySearchCond cond21 = new ItemReplySearchCond("b", "hello2", null);
        ItemReplySearchCond cond22 = new ItemReplySearchCond("b", null, today);
        ItemReplySearchCond cond23 = new ItemReplySearchCond(null, "hello2", today);

        // 조건 3
        ItemReplySearchCond cond31 = new ItemReplySearchCond("c", "he", today);


        // 맞지 않는 조건
        ItemReplySearchCond cond4 = new ItemReplySearchCond("ab", null, null);
        ItemReplySearchCond cond24 = new ItemReplySearchCond("ab", "hello1", null);
        ItemReplySearchCond cond32 = new ItemReplySearchCond("c", "hello3", otherDay);

        // then
        // 조건1
        testResult(pageablePage0Size10, cond1, rCount);
        testResult(pageablePage0Size10, cond2, rCount);
        testResult(pageablePage0Size10, cond3, rCount + r2Count + r3Count);
        // 조건2
        testResult(pageablePage0Size10, cond21, r2Count);
        testResult(pageablePage0Size10, cond22, r2Count);
        testResult(pageablePage0Size10, cond23, r2Count);
        // 조건3
        testResult(pageablePage2Size2, cond31, r3Count);



        // 맞지 않는 조검
        testResult(pageablePage0Size10, cond4, 0);
        testResult(pageablePage0Size10, cond24, 0);
        testResult(pageablePage3Size5, cond32, 0);

    }

    private void testResult(Pageable pageable, ItemReplySearchCond cond, int count) {
        Page<ItemReply> result = itemReplyJpaRepository.findAllInfo(cond, pageable);
        assertThat(result.getTotalElements()).isEqualTo(count);
    }
}
