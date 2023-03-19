package shop.wesellbuy.secondproject.repository.reply.customerservice;

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
import shop.wesellbuy.secondproject.domain.CustomerService;
import shop.wesellbuy.secondproject.domain.Member;
import shop.wesellbuy.secondproject.domain.reply.CustomerServiceReply;
import shop.wesellbuy.secondproject.web.member.MemberForm;
import shop.wesellbuy.secondproject.web.reply.ReplyForm;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
public class CustomerServiceReplyRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    CustomerServiceReplyJpaRepository csrJpaRepository;

    Member member; // 등록 회원
    Member member2; // 등록 회원
    Member member3; // 등록 회원
    CustomerService customerService; // 고객지원 글
    CustomerService customerService2; // 고객지원 글
    CustomerService customerService3; // 고객지원 글

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

        // 고객지원 글 저장
        CustomerService customerService = CustomerService.createCustomerService("c", "ab", member);
        CustomerService customerService2 = CustomerService.createCustomerService("a", "ab", member2);
        CustomerService customerService3 = CustomerService.createCustomerService("b", "ab", member3);

        em.persist(customerService);
        em.persist(customerService2);
        em.persist(customerService3);

        this.customerService = customerService;
        this.customerService2 = customerService2;
        this.customerService3 = customerService3;
    }


    /**
     * writer : 이호진
     * init : 2023.01.18
     * updated by writer :
     * update :
     * description : save Test
     */
    @Test
//    @Rollback(false)
    public void 고객지원글저장() {
        // given
        ReplyForm replyForm = new ReplyForm("hello");
        CustomerServiceReply customerServiceReply = CustomerServiceReply.createCustomerServiceReply(replyForm, member, customerService);

        // when
        csrJpaRepository.save(customerServiceReply);

        // then
        CustomerServiceReply findCustomerServiceReply = csrJpaRepository.findById(customerServiceReply.getNum()).orElseThrow();

        assertThat(customerServiceReply).isEqualTo(findCustomerServiceReply);
    }

    /**
     * writer : 이호진
     * init : 2023.01.18
     * updated by writer : 이호진
     * update : 2023.01.18
     * description : findAll by condition and pageable Test
     *
     */
    @Test
//    @Rollback(false)
    public void 고객지원글_모두_가져오기_By_조건_페이징() {

        // given
        // 100개의 더미 파일 만들기
        Long count = 100L;
        int mCount = 0; // 게시글 개수 member의
        int m2Count = 0; // 게시글 개수 member2의
        int m3Count = 0; // 게시글 개수 member3의

        CustomerServiceReply customerServiceReply = null;
        for(int i = 0; i < count; i++) {

            if(i % 3 == 0) {
                ReplyForm replyForm = new ReplyForm("hello1");
                customerServiceReply = CustomerServiceReply.createCustomerServiceReply(replyForm, member, customerService);
                mCount += 1;
            } else if(i % 3 == 1) {
                ReplyForm replyForm = new ReplyForm("hello2");

                customerServiceReply = CustomerServiceReply.createCustomerServiceReply(replyForm, member2, customerService2);
                m2Count += 1;
            } else {
                ReplyForm replyForm = new ReplyForm("hello3");
                customerServiceReply = CustomerServiceReply.createCustomerServiceReply(replyForm, member3, customerService3);
                m3Count += 1;
            }

            em.persist(customerServiceReply);
        }

        // 페이지, 사이즈 정하기
        Pageable pageablePage0Size100 = PageRequest.of(0, 100);
        Pageable pageablePage0Size10 = PageRequest.of(0, 10);
        Pageable pageablePage1Size10 = PageRequest.of(1, 10);
        Pageable pageablePage3Size5 = PageRequest.of(3, 5);
        Pageable pageablePage2Size2 = PageRequest.of(2, 2);

        // 날짜 condition
        String today = "2023-01-27";
        String otherDay = "2023-01-28";

        // 검색조건
        String content = "el";
        CustomerServiceReplySearchCond cond1 = new CustomerServiceReplySearchCond(member.getId(), null, null);
        CustomerServiceReplySearchCond cond2 = new CustomerServiceReplySearchCond(member.getId(), content, null);
        CustomerServiceReplySearchCond cond3 = new CustomerServiceReplySearchCond(member.getId(), null, today);
        CustomerServiceReplySearchCond cond4 = new CustomerServiceReplySearchCond(member.getId(), content, today);
        // 아이디 다를 때
        String content3 = "hello1";
        CustomerServiceReplySearchCond condOtherId = new CustomerServiceReplySearchCond(member2.getId(), content3, today);
        // 내용 다를 때
        String content2 = "ads";
        CustomerServiceReplySearchCond cond5 = new CustomerServiceReplySearchCond(member.getId(), content2, today);

        // when
        // 검색조건 count 비교
        Page<CustomerServiceReply> result1 = csrJpaRepository.findAllInfo(cond1, pageablePage0Size10);
        Page<CustomerServiceReply> result2 = csrJpaRepository.findAllInfo(cond2, pageablePage0Size10);
        Page<CustomerServiceReply> result3 = csrJpaRepository.findAllInfo(cond3, pageablePage0Size10);
        Page<CustomerServiceReply> result4 = csrJpaRepository.findAllInfo(cond4, pageablePage0Size10);
        Page<CustomerServiceReply> result5 = csrJpaRepository.findAllInfo(cond5, pageablePage0Size10);
        // 아이디 다를 때
        Page<CustomerServiceReply> resultOtherId = csrJpaRepository.findAllInfo(condOtherId, pageablePage0Size10);

        // 내용 다를 때
        // page에 따른 레코드 수 검사
//        Page<CustomerServiceReply> result11 = csrJpaRepository.findAllInfo(cond1, pageablePage0Size10);
//        Page<CustomerServiceReply> result12 = csrJpaRepository.findAllInfo(cond1, pageablePage1Size10);
//        Page<CustomerServiceReply> result13 = csrJpaRepository.findAllInfo(cond1, pageablePage2Size2);
//        Page<CustomerServiceReply> result14 = csrJpaRepository.findAllInfo(cond1, pageablePage3Size5);
//        // getSize()와 레코드수가 같은 것인지 확인
//        Page<CustomerServiceReply> result15 = csrJpaRepository.findAllInfo(cond1, pageablePage0Size100);


        // then
        // 검색조건 count 비교
        assertThat(result1.getTotalElements()).isEqualTo(mCount);
        assertThat(result2.getTotalElements()).isEqualTo(mCount);
        assertThat(result3.getTotalElements()).isEqualTo(mCount);
        assertThat(result4.getTotalElements()).isEqualTo(mCount);
        assertThat(result5.getTotalElements()).isEqualTo(0);

        // 아이디 다를 때
        assertThat(resultOtherId.getTotalElements()).isEqualTo(0);


        // page에 따른 레코드 수 확인
//        assertThat(result11.getContent().size()).isEqualTo(result11.getSize());
//        assertThat(result12.getContent().size()).isEqualTo(10);
//        assertThat(result13.getContent().size()).isEqualTo(2);
//        assertThat(result14.getContent().size()).isEqualTo(5);
//        // getSize()와 레코드수가 같은 것인지 확인
////        assertThat(result15.getSize()).isEqualTo(mCount); // size크기로 나온다. false
//        assertThat(result15.getSize()).isEqualTo(100);
//        assertThat(result15.getContent().size()).isEqualTo(mCount);



    }




}
