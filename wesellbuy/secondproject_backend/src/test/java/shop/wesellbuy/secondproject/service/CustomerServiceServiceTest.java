package shop.wesellbuy.secondproject.service;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import shop.wesellbuy.secondproject.domain.CustomerService;
import shop.wesellbuy.secondproject.domain.Member;
import shop.wesellbuy.secondproject.domain.reply.CustomerServiceReply;
import shop.wesellbuy.secondproject.exception.common.NotExistingIdException;
import shop.wesellbuy.secondproject.repository.customerservice.CustomerServiceJpaRepository;
import shop.wesellbuy.secondproject.repository.customerservice.CustomerServiceSearchCond;
import shop.wesellbuy.secondproject.repository.member.MemberJpaRepository;
import shop.wesellbuy.secondproject.repository.reply.customerservice.CustomerServiceReplyJpaRepository;
import shop.wesellbuy.secondproject.service.customerservice.CustomerServiceService;
import shop.wesellbuy.secondproject.service.member.MemberService;
import shop.wesellbuy.secondproject.web.customerservice.CustomerServiceDetailForm;
import shop.wesellbuy.secondproject.web.customerservice.CustomerServiceForm;
import shop.wesellbuy.secondproject.web.customerservice.CustomerServiceListForm;
import shop.wesellbuy.secondproject.web.member.MemberOriginForm;
import shop.wesellbuy.secondproject.web.reply.ReplyForm;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@Slf4j
public class CustomerServiceServiceTest {

    @Autowired
    CustomerServiceService customerServiceService;
    @Autowired
    CustomerServiceJpaRepository csjr;
    @Autowired
    MemberJpaRepository mjr;
    @Autowired
    MemberService ms;
    @Autowired
    CustomerServiceReplyJpaRepository csrjr;
    @Autowired
    EntityManager em;

    int memberNum; // 저장된 회원번호
    Member member; // 저장된 회원
    Member member2; // 저장된 회원
    Member member3; // 저장된 회원
    Member member4; // 저장된 회원

    @BeforeEach
    public void init() throws IOException {

        log.info("test init 시작");

        // 회원 생성
        // MockMultipartFile 생성
        String fileName = "testFile1"; // 파일명
        String contentType = "jpg"; // 파일 확장자
        String originFileName = fileName + "." + contentType;
        String filePath = "src/test/resources/testImages/" + fileName + "." + contentType;
        FileInputStream fileInputStream = new FileInputStream(filePath);

        MockMultipartFile file = new MockMultipartFile("image1", originFileName, contentType, fileInputStream);

        // 회원 정보 생성
        MemberOriginForm memberOriginForm = new MemberOriginForm("a", "a1", "123","123", "a1@a1.a1", "01012341234", "021231234", "korea", "s", "h", "123", "12345", file);
        MemberOriginForm memberOriginForm2 = new MemberOriginForm("a", "b1", "123","123", "a1@a1.a1", "01012341234", "021231234", "korea", "s", "h", "123", "12345", file);
        MemberOriginForm memberOriginForm3 = new MemberOriginForm("a", "c1", "123","123", "a1@a1.a1", "01012341234", "021231234", "korea", "s", "h", "123", "12345", file);
        MemberOriginForm memberOriginForm4 = new MemberOriginForm("a", "d1", "123","123", "a1@a1.a1", "01012341234", "021231234", "korea", "s", "h", "123", "12345", file);
        int memberNum = ms.join(memberOriginForm);
        int memberNum2 = ms.join(memberOriginForm2);
        int memberNum3 = ms.join(memberOriginForm3);
        int memberNum4 = ms.join(memberOriginForm4);
        this.memberNum = memberNum;
        // 회원 부르기
        Member member = mjr.findById(memberNum).orElse(null);
        Member member2 = mjr.findById(memberNum2).orElse(null);
        Member member3 = mjr.findById(memberNum3).orElse(null);
        Member member4 = mjr.findById(memberNum4).orElse(null);
        this.member = member;
        this.member2 = member2;
        this.member3 = member3;
        this.member4 = member4;

        log.info("test init 끝");
    }

    /**
     * 고객지원글 저장하기 확인
     * -> 신고된 회원 존재할 때
     */
    @Test
//    @Rollback(value = false)
    public void 고객지원글_저장하기_확인() {
        // given
        // customerServiceForm 생성
        CustomerServiceForm customerServiceForm = new CustomerServiceForm("b1", "누가 이렇게 했어");
        // when
        // 고객지원글 저장
        // 신고된 회원 존재할 때
        int num = customerServiceService.save(memberNum, customerServiceForm);

        // then
        CustomerService findCustomerService = csjr.findById(num).orElseThrow();

        assertThat(findCustomerService.getNum()).isEqualTo(num);
        assertThat(findCustomerService.getReportedId()).isEqualTo(customerServiceForm.getReportedId());
        assertThat(findCustomerService.getContent()).isEqualTo(customerServiceForm.getContent());
        assertThat(findCustomerService.getMember().getNum()).isEqualTo(memberNum);
    }

    /**
     * 고객지원글 저장하기 확인
     * -> 신고된 회원 존재하지 않을 때
     */
    @Test
    public void 고객지원글_저장하기_신고된_회원_존재하지_않을때_확인() {
        // given
        // customerServiceForm 생성
        CustomerServiceForm customerServiceForm = new CustomerServiceForm("c123", "누가 이렇게 했어");
        // when // then
        // 고객지원글 저장
        // 신고된 회원 존재하지 않을 때
        assertThrows(NotExistingIdException.class, () -> customerServiceService.save(memberNum, customerServiceForm));
//        assertThrows(IllegalStateException.class, () -> customerServiceService.save(memberNum, customerServiceForm)); // 에러 발생
    }

    /**
     * 상세보기 확인
     *
     * comment : betch size 사용으로 where절에 in() 쿼리 생성으로 N + 1문제 해결
     */
    @Test
//    @Rollback(false)
    public void 상세보기_확인() {
        // given
        // 고객 지원글 저장하기
        CustomerService customerService = CustomerService.createCustomerService("b1", "내가 누구?", member);
        CustomerService savedCustomerService = csjr.save(customerService);

        // 댓글 저장하기
        ReplyForm replyForm = new ReplyForm("조치하도록 하겠습니다.");
        ReplyForm replyForm2 = new ReplyForm("맞아요.");
        ReplyForm replyForm3 = new ReplyForm("그렇죠");
        CustomerServiceReply customerServiceReply = CustomerServiceReply.createCustomerServiceReply(replyForm, member, customerService);
        CustomerServiceReply customerServiceReply2 = CustomerServiceReply.createCustomerServiceReply(replyForm2, member2, customerService);
        CustomerServiceReply customerServiceReply3 = CustomerServiceReply.createCustomerServiceReply(replyForm3, member, customerService);
        CustomerServiceReply customerServiceReply4 = CustomerServiceReply.createCustomerServiceReply(replyForm3, member3, customerService);
        CustomerServiceReply customerServiceReply5 = CustomerServiceReply.createCustomerServiceReply(replyForm3, member4, customerService);
        CustomerServiceReply customerServiceReply6 = CustomerServiceReply.createCustomerServiceReply(replyForm3, member3, customerService);
        CustomerServiceReply customerServiceReply7 = CustomerServiceReply.createCustomerServiceReply(replyForm3, member4, customerService);
        CustomerServiceReply reply1 = csrjr.save(customerServiceReply);
        CustomerServiceReply reply2 = csrjr.save(customerServiceReply2);
        CustomerServiceReply reply3 = csrjr.save(customerServiceReply3);
        CustomerServiceReply reply4 = csrjr.save(customerServiceReply4);
        CustomerServiceReply reply5 = csrjr.save(customerServiceReply5);
        CustomerServiceReply reply6 = csrjr.save(customerServiceReply6);
        CustomerServiceReply reply7 = csrjr.save(customerServiceReply7);

        // 영속성 캐시를 지워주기(N + 1 안 되는지 확인 필요)
        em.flush();
        em.clear();

        log.info("영속성 컨텍스트 clean");

        // when
        CustomerServiceDetailForm detailForm = customerServiceService.watchDetail(savedCustomerService.getNum());

        // then
        assertThat(detailForm.getNum()).isEqualTo(savedCustomerService.getNum());
        assertThat(detailForm.getReportedId()).isEqualTo(customerService.getReportedId());
        assertThat(detailForm.getContent()).isEqualTo(customerService.getContent());
        assertThat(detailForm.getMemberId()).isEqualTo(customerService.getMember().getId());
        // 댓글 확인
        // 댓글 저장 번호로 확인
        List<Integer> replyNums = detailForm.getReplyList().stream()
                .map(r -> r.getNum())
                .collect(toList());
        // 댓글 content로 확인
        List<String> replyContents = detailForm.getReplyList().stream()
                .map(r -> r.getContent())
                .collect(toList());
//        assertThat(replyNums).containsExactly(reply1.getNum(), reply2.getNum(), reply3.getNum());
        assertThat(replyNums).containsExactly(
                reply1.getNum(),
                reply2.getNum(),
                reply3.getNum(),
                reply4.getNum(),
                reply5.getNum(),
                reply6.getNum(),
                reply7.getNum()
        );
//        assertThat(replyNums).containsExactly(reply1.getNum(), reply2.getNum());// 에러 발생
//        assertThat(replyContents).containsExactly(reply1.getContent(), reply2.getContent(), reply3.getContent());
//        assertThat(replyContents).containsExactly(reply1.getContent(), reply2.getContent()); // 에러 발생
    }

    /**
     * 모든 고객지원글(page) 불러오기 확인
     */
    @Test
    public void 고객지원글_page_불러오기_확인() {
        // given
        // 페이지 사이즈 정하기
        Pageable pageablePage0Size10 = PageRequest.of(0,10);
        Pageable pageablePage1Size5 = PageRequest.of(1,5);
        Pageable pageablePage2Size2 = PageRequest.of(2,2);

        // 날짜 condition
        String today = "2023-01-28";
        String otherDay = "2023-01-29";

        // 게시글 생성하기
        int amounts = 100; // 총 게시글 수
        int r2Count = 0; // member2의 게시글 수
        int r3Count = 0;// member3의 게시글 수
        int r4Count = 0;// member4의 게시글 수
        CustomerService customerService = null;
        for(int i = 0; i < amounts; i++) {
            if (i % 3 == 0) {
                customerService = CustomerService.createCustomerService("a1", "내가 누구?", member2);
                r2Count += 1;
            } else if (i % 3 == 1) {
                customerService = CustomerService.createCustomerService("a1", "이렇게 했던데", member3);
                r3Count += 1;
            } else {
                customerService = CustomerService.createCustomerService("b1", "불량이네요", member4);
                r4Count += 1;
            }
            csjr.save(customerService);
        }

        // when
        // 검색 조건 생성하기
        // 조건 0
//        CustomerServiceSearchCond cond0 = new CustomerServiceSearchCond("", "", "");
        // 조건 1
//        CustomerServiceSearchCond cond11 = new CustomerServiceSearchCond("b1", "", "");
//        CustomerServiceSearchCond cond12 = new CustomerServiceSearchCond("", "b1", "");
//        CustomerServiceSearchCond cond13 = new CustomerServiceSearchCond("", "", today);
        // 조건 2
//        CustomerServiceSearchCond cond21 = new CustomerServiceSearchCond("b1", "a1", "");
//        CustomerServiceSearchCond cond22 = new CustomerServiceSearchCond("b1", "", today);
//        CustomerServiceSearchCond cond23 = new CustomerServiceSearchCond("", "a1", today);

        // 조건 3
        CustomerServiceSearchCond cond31 = new CustomerServiceSearchCond("b1", "a1", today);

        // 조건에 맞는 값이 없을 때
//        CustomerServiceSearchCond cond14 = new CustomerServiceSearchCond("", "", otherDay);
//        CustomerServiceSearchCond cond15 = new CustomerServiceSearchCond("c1234", "", "");
//        CustomerServiceSearchCond cond24 = new CustomerServiceSearchCond("", "abc1", today);
        CustomerServiceSearchCond cond32 = new CustomerServiceSearchCond("b1", "d1", today);


        log.info("고객지원글_page_불러오기_확인 결과 보기 start");
        // then
        // 전체 게시글 수로 확인
        // 조건 0
//        testAboutTotalAmount(pageablePage0Size10, cond0, r2Count + r3Count + r4Count);
        // 조건 1
//        testAboutTotalAmount(pageablePage1Size5, cond11, r2Count);
//        testAboutTotalAmount(pageablePage1Size5, cond12, r4Count);
//        testAboutTotalAmount(pageablePage1Size5, cond13, r2Count + r3Count + r4Count);
        // 조건 2
//        testAboutTotalAmount(pageablePage2Size2, cond21, r2Count);
//        testAboutTotalAmount(pageablePage2Size2, cond22, r2Count);
//        testAboutTotalAmount(pageablePage2Size2, cond23, r2Count + r3Count);
        // 조건 3
        testAboutTotalAmount(pageablePage0Size10, cond31, r2Count);


        // page와 size로 확인
        // 조건 0
//        testAboutPageSize(pageablePage0Size10, cond0, 10);
        // 조건 1
//        testAboutPageSize(pageablePage1Size5, cond12, 5);
//        testAboutPageSize(pageablePage1Size5, cond13, 5);
//        testAboutPageSize(pageablePage1Size5, cond11, 5);
        // 조건 2
//        testAboutPageSize(pageablePage0Size10, cond21, 10);
//        testAboutPageSize(pageablePage0Size10, cond22, 10);
//        testAboutPageSize(pageablePage0Size10, cond23, 10);
        // 조건 3
        testAboutPageSize(pageablePage2Size2, cond31, 2);

        // 조건에 맞는 값이 없을 때
        // 전체 게시글 수로 확인
//        testAboutTotalAmount(pageablePage1Size5, cond14, 0);
//        testAboutTotalAmount(pageablePage1Size5, cond15, 0);
//        testAboutTotalAmount(pageablePage1Size5, cond24, 0);
        testAboutTotalAmount(pageablePage1Size5, cond32, 0);

        // page와 size로 확인
//        testAboutPageSize(pageablePage1Size5, cond14, 0);
//        testAboutPageSize(pageablePage1Size5, cond15, 0);
//        testAboutPageSize(pageablePage1Size5, cond24, 0);
        testAboutPageSize(pageablePage2Size2, cond32, 0);

    }

    // page와 size로 확인 (실제 보이는 게시글 수)
    private void testAboutPageSize(Pageable pageable, CustomerServiceSearchCond cond, int count) {
        Page<CustomerServiceListForm> result = customerServiceService.selectList(cond, pageable);
        assertThat(result.getContent().size()).isEqualTo(count);
    }

    // 전체 게시글 수로 확인 (전체 게시글 수)
    private void testAboutTotalAmount(Pageable pageable, CustomerServiceSearchCond cond, int count) {
        Page<CustomerServiceListForm> result = customerServiceService.selectList(cond, pageable);
        assertThat(result.getTotalElements()).isEqualTo(count);
    }


}
