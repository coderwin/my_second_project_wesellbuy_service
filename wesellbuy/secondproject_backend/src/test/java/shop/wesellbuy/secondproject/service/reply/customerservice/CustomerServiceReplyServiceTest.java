package shop.wesellbuy.secondproject.service.reply.customerservice;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import shop.wesellbuy.secondproject.domain.CustomerService;
import shop.wesellbuy.secondproject.domain.Member;
import shop.wesellbuy.secondproject.domain.member.SelfPicture;
import shop.wesellbuy.secondproject.domain.reply.CustomerServiceReply;
import shop.wesellbuy.secondproject.repository.customerservice.CustomerServiceJpaRepository;
import shop.wesellbuy.secondproject.repository.member.MemberJpaRepository;
import shop.wesellbuy.secondproject.repository.reply.customerservice.CustomerServiceReplyJpaRepository;
import shop.wesellbuy.secondproject.repository.reply.customerservice.CustomerServiceReplySearchCond;
import shop.wesellbuy.secondproject.service.customerservice.CustomerServiceService;
import shop.wesellbuy.secondproject.web.customerservice.CustomerServiceDetailForm;
import shop.wesellbuy.secondproject.web.member.MemberForm;
import shop.wesellbuy.secondproject.web.reply.ReplyDetailForm;
import shop.wesellbuy.secondproject.web.reply.ReplyForm;
import shop.wesellbuy.secondproject.web.reply.ReplyUpdateForm;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
public class CustomerServiceReplyServiceTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;
    @Autowired
    CustomerServiceJpaRepository customerServiceJpaRepository;
    @Autowired
    CustomerServiceReplyService customerServiceReplyService;
    @Autowired
    CustomerServiceReplyJpaRepository customerServiceReplyJpaRepository;

    Member member; // test 회원
    Member member2; // test 회원
    Member member3; // test 회원

    CustomerService customerService;// test 고객지원글
    CustomerService customerService2;// test 고객지원글
    CustomerService customerService3;// test 고객지원글

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

        // 고객지원글 생성
        CustomerService customerService1 = CustomerService.createCustomerService("b1", "불량을 팔았어요!", member);
        CustomerService customerService2 = CustomerService.createCustomerService("c1", "아니에요!", member2);
        CustomerService customerService3 = CustomerService.createCustomerService("a1", "배달이 잘못 왔어요", member3);

        customerServiceJpaRepository.save(customerService1);
        customerServiceJpaRepository.save(customerService2);
        customerServiceJpaRepository.save(customerService3);

        this.customerService = customerService1;
        this.customerService2 = customerService2;
        this.customerService3 = customerService3;

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
        int replyNum = customerServiceReplyService.save(replyForm, member.getNum(), customerService2.getNum());
        // then
        CustomerServiceReply result = customerServiceReplyJpaRepository.findById(replyNum).orElseThrow();

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
        int replyNum = customerServiceReplyService.save(replyForm, member.getNum(), customerService2.getNum());

        // when
        // 댓글 수정
        ReplyUpdateForm replyUpdateForm = new ReplyUpdateForm(replyNum, "그랬군요!");

        customerServiceReplyService.update(replyUpdateForm);
        // then
        CustomerServiceReply findReply = customerServiceReplyJpaRepository.findById(replyNum).orElseThrow();

        // 수정 후 content
        assertThat(findReply.getContent()).isEqualTo(replyUpdateForm.getContent());
        // 수정 전 content
        assertThat(findReply.getContent()).isNotEqualTo(replyForm.getContent());
    }

    /**
     * 댓글 삭제 확인
     * -> status : R -> D로 바뀜
     * -> 고객지원글 자세히 보기에서 확인 가능(안 나타나야함) : CustomerServiceService
     */
    @Test
//    @Rollback(value = false)
    public void 댓글_삭제_확인(@Autowired CustomerServiceService customerServiceService,
                         @Autowired EntityManager em) {
        // given
        // 댓글 생성
        ReplyForm replyForm = new ReplyForm("멋지네요~");
        int replyNum = customerServiceReplyService.save(replyForm, member.getNum(), customerService3.getNum());
        ReplyForm replyForm2 = new ReplyForm("멋지네요~");
        int replyNum2 = customerServiceReplyService.save(replyForm2, member2.getNum(), customerService3.getNum());
        ReplyForm replyForm3 = new ReplyForm("멋지네요~");
        int replyNum3 = customerServiceReplyService.save(replyForm3, member.getNum(), customerService3.getNum());
        // when
        customerServiceReplyService.delete(replyNum);

        // betch size 작동하는지 확인하기
        em.flush();
        em.clear();

        // then
        // CustomerServiceService에서 확인
        CustomerServiceDetailForm detailForm = customerServiceService.watchDetail(customerService3.getNum());

        // 댓글을 R -> D로 바꾸었기에
        // 댓글은 3개에서 2개로 나타난다.
        assertThat(detailForm.getReplyList().size()).isEqualTo(2);
        assertThat(detailForm.getReplyList().size()).isNotEqualTo(3);
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
        CustomerServiceReply customerServiceReply = null;

        for(int i = 0; i < amount; i++) {
            if(i % 3 == 1) {
                replyForm = new ReplyForm("편리해요");
                customerServiceReply = CustomerServiceReply.createCustomerServiceReply(replyForm, member, customerService);
                rCount += 1;
            } else if(i * 3 == 2) {
                replyForm = new ReplyForm("좋아요~~~~~");
                customerServiceReply = CustomerServiceReply.createCustomerServiceReply(replyForm, member2, customerService);
                r2Count += 1;
            } else {
                replyForm = new ReplyForm("멋지네요~~~~~");
                customerServiceReply = CustomerServiceReply.createCustomerServiceReply(replyForm, member3, customerService2);
                r3Count += 1;
            }
            // 저장하기
            customerServiceReplyJpaRepository.save(customerServiceReply);

            // 삭제하기
            if(i % 4 == 0) {
                customerServiceReply.delete();
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
        CustomerServiceReplySearchCond cond0 = new CustomerServiceReplySearchCond("", "", "");
        // 조건 1
        CustomerServiceReplySearchCond cond11 = new CustomerServiceReplySearchCond("a1", "", "");
        CustomerServiceReplySearchCond cond12 = new CustomerServiceReplySearchCond("", "요~~", "");
        CustomerServiceReplySearchCond cond13 = new CustomerServiceReplySearchCond("", "", today);
        // 조건 2
        CustomerServiceReplySearchCond cond21 = new CustomerServiceReplySearchCond("b1", "아요", "");
        CustomerServiceReplySearchCond cond22 = new CustomerServiceReplySearchCond("b1", "", today);
        CustomerServiceReplySearchCond cond23 = new CustomerServiceReplySearchCond("", "요~", today);
        // 조건 3
        CustomerServiceReplySearchCond cond31 = new CustomerServiceReplySearchCond("c1", "멋지네요", today);


        // 조건이 잘못 전달될 때
        CustomerServiceReplySearchCond cond14 = new CustomerServiceReplySearchCond("a123", "", "");
        CustomerServiceReplySearchCond cond24 = new CustomerServiceReplySearchCond("b123", "", today);
        CustomerServiceReplySearchCond cond32 = new CustomerServiceReplySearchCond("c1", "멋지네요!", today);

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
    private void testResultForAdmin(CustomerServiceReplySearchCond cond, Pageable pageable, int count) {
        Page<ReplyDetailForm> result = customerServiceReplyService.selectListForAdmin(cond, pageable);
        assertThat(result.getTotalElements()).isEqualTo(count);
    }

//    -------------------------methods using for admin admin----------------------------------

}
