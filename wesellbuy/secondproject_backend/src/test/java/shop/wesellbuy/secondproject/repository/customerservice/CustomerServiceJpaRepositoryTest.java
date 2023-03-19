package shop.wesellbuy.secondproject.repository.customerservice;

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
import shop.wesellbuy.secondproject.web.member.MemberForm;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
public class CustomerServiceJpaRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    CustomerServiceJpaRepository customerServiceJpaRepository;

    private Member member; // test 사용 member
    private Member member2; // test 사용 member
    private Member member3; // test 사용 member

    // test에 필요한 객체 생성
    @BeforeEach
    public void init() {

        // 회원 정보 저장
        MemberForm memberForm1 = new MemberForm("a", "a", "123","a@a", "01012341234", "0511231234", "korea1", "b", "h", "h", "123", null);
        Member member = Member.createMember(memberForm1);
        MemberForm memberForm2 = new MemberForm("a", "ac","123", "a@a", "01012341234", "0511231234", "korea1", "b", "h", "h", "123", null);
        Member member2 = Member.createMember(memberForm2);
        MemberForm memberForm3 = new MemberForm("b", "bd", "123","a@a", "01012341234", "0511231234", "korea1", "b", "h", "h", "123", null);
        Member member3 = Member.createMember(memberForm3);

        em.persist(member);
        em.persist(member2);
        em.persist(member3);

        this.member = member;
        this.member2 = member2;
        this.member3 = member3;
    }

    /**
     * writer : 이호진
     * init : 2023.01.17
     * updated by writer :
     * update :
     * description : save Test
     */
    @Test
//    @Rollback(false)
    public void 고객지원글저장() {
        // given
        CustomerService customerService = CustomerService.createCustomerService("a", "ab", member);

        // when
        customerServiceJpaRepository.save(customerService);

        // then
        var findCustomerService = customerServiceJpaRepository.findById(customerService.getNum()).orElseThrow();

        assertThat(findCustomerService).isEqualTo(customerService);
    }

    /**
     * writer : 이호진
     * init : 2023.01.17
     * updated by writer : 이호진
     * update : 2023.01.18
     * description : findAll by condition and pageable Test
     *
     * comment : update 날짜 조건 test
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

        CustomerService customerService = null;
        for(int i = 0; i < count; i++) {

            if(i % 3 == 0) {
                customerService = CustomerService.createCustomerService("ac", "ab", member);
                mCount += 1;
            } else if(i % 3 == 1) {
                customerService = CustomerService.createCustomerService("bd", "ab", member2);
                m2Count += 1;
            } else {
                customerService = CustomerService.createCustomerService("a", "ab", member3);
                m3Count += 1;
            }
            em.persist(customerService);
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

        // 검색 조건
        CustomerServiceSearchCond customerServiceSearchCond1 = new CustomerServiceSearchCond("a", null, today);
        CustomerServiceSearchCond customerServiceSearchCond2 = new CustomerServiceSearchCond(null, "bd", today);
        CustomerServiceSearchCond customerServiceSearchCond3 = new CustomerServiceSearchCond("bd", null, today);
        // List가 있나 없나 구분
        CustomerServiceSearchCond customerServiceSearchCond4 = new CustomerServiceSearchCond("a", "ac", today);
        CustomerServiceSearchCond customerServiceSearchCond5 = new CustomerServiceSearchCond("a", "bd", today);
        // 날짜 검색 되는지 확인
        CustomerServiceSearchCond customerServiceSearchCond6 = new CustomerServiceSearchCond("a", "ac", today);
        CustomerServiceSearchCond customerServiceSearchCond7 = new CustomerServiceSearchCond("a", "ac", otherDay);


        // when
        // total count 비교
        Page<CustomerService> result1 = customerServiceJpaRepository.findAllInfo(customerServiceSearchCond1, pageablePage0Size10);
        Page<CustomerService> result2 = customerServiceJpaRepository.findAllInfo(customerServiceSearchCond1, pageablePage1Size10);
        Page<CustomerService> result3 = customerServiceJpaRepository.findAllInfo(customerServiceSearchCond1, pageablePage3Size5);
        Page<CustomerService> result4 = customerServiceJpaRepository.findAllInfo(customerServiceSearchCond1, pageablePage2Size2);

        // condition에 따라 content 찾아지는지 확인
        Page<CustomerService> result5 = customerServiceJpaRepository.findAllInfo(customerServiceSearchCond1, pageablePage0Size10);
        Page<CustomerService> result6 = customerServiceJpaRepository.findAllInfo(customerServiceSearchCond2, pageablePage0Size10);
        Page<CustomerService> result7 = customerServiceJpaRepository.findAllInfo(customerServiceSearchCond3, pageablePage0Size10);

        // List 있나 없나 비교
        Page<CustomerService> result8 = customerServiceJpaRepository.findAllInfo(customerServiceSearchCond4, pageablePage0Size10);
        Page<CustomerService> result9 = customerServiceJpaRepository.findAllInfo(customerServiceSearchCond5, pageablePage0Size10);

        // 날짜 검색 되는지 확인
        Page<CustomerService> result10 = customerServiceJpaRepository.findAllInfo(customerServiceSearchCond6, pageablePage0Size10);
        Page<CustomerService> result11 = customerServiceJpaRepository.findAllInfo(customerServiceSearchCond7, pageablePage0Size10);



        // then
        // total count 비교
//        assertThat(result1.getTotalElements()).isEqualTo(10L); // false
        assertThat(result1.getTotalElements()).isEqualTo(mCount);
        assertThat(result2.getTotalElements()).isEqualTo(mCount);
        assertThat(result3.getTotalElements()).isEqualTo(mCount);
        assertThat(result4.getTotalElements()).isEqualTo(mCount);

        // condition에 따라 content 찾아지는지 확인
        assertThat(result5.getTotalElements()).isEqualTo(mCount);
        assertThat(result6.getTotalElements()).isEqualTo(m2Count);
        assertThat(result7.getTotalElements()).isEqualTo(m3Count);

        // List 있나 없나 비교
        assertThat(result8.getTotalElements()).isEqualTo(mCount);
        assertThat(result9.getTotalElements()).isEqualTo(0);

        // 날짜 검색 되는지 확인
        assertThat(result10.getTotalElements()).isEqualTo(mCount);
        assertThat(result11.getTotalElements()).isEqualTo(0);
    }

    /**
     * 고객지원글 상세보기 출력 확인
     */
    @Test
    public void 상세보기_출력_확인() {
        // given
        //고객 지원글 저장하기
        CustomerService customerService = CustomerService.createCustomerService("a", "ab", member);
        em.persist(customerService);

        // when
        // 상세보기
        CustomerService findCustomerService = customerServiceJpaRepository.findDetailInfoById(customerService.getNum()).orElseThrow();

        // then
        assertThat(customerService).isEqualTo(findCustomerService);
    }










}
