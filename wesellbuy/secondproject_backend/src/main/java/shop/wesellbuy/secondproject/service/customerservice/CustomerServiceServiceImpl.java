package shop.wesellbuy.secondproject.service.customerservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.wesellbuy.secondproject.domain.CustomerService;
import shop.wesellbuy.secondproject.domain.Member;
import shop.wesellbuy.secondproject.exception.common.NotExistingIdException;
import shop.wesellbuy.secondproject.repository.customerservice.CustomerServiceJpaRepository;
import shop.wesellbuy.secondproject.repository.customerservice.CustomerServiceSearchCond;
import shop.wesellbuy.secondproject.repository.member.MemberJpaRepository;
import shop.wesellbuy.secondproject.web.customerservice.CustomerServiceDetailForm;
import shop.wesellbuy.secondproject.web.customerservice.CustomerServiceForm;
import shop.wesellbuy.secondproject.web.customerservice.CustomerServiceListForm;

/**
 * CustomerService Service 구현 클래스
 * writer : 이호진
 * init : 2023.01.28
 * updated by writer :
 * update :
 * description : CustomerService Service 구현 메소드 모음
 *
 * comment : 삭제 기능 없음 - 고객지원은 글을 다시 업로드
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceServiceImpl implements CustomerServiceService{

    private final CustomerServiceJpaRepository customerServiceJpaRepository;// 고객지원 리포지토리
    private final MemberJpaRepository memberJpaRepository; // 회원 리포지토리

    /**
     * writer : 이호진
     * init : 2023.01.28
     * updated by writer :
     * update :
     * description : 고객지원글 저장
     */
    @Override
    @Transactional
    public int save(int memberNum, CustomerServiceForm customerServiceForm) {
        log.info("CustomerService 저장 중...");
        // 회원 불러오기
        Member findMember = memberJpaRepository.findById(memberNum).orElseThrow();

        // 신고된 회원 있나없나 확인
        checkReportedId(customerServiceForm.getReportedId());

        // 고객지원글 생성하기
        CustomerService customerService = CustomerService.createCustomerService(customerServiceForm.getReportedId(), customerServiceForm.getContent(), findMember);

        // 고객지원글 저장하기
        CustomerService savedCustomerService = customerServiceJpaRepository.save(customerService);

        return savedCustomerService.getNum();
    }

    /**
     * writer : 이호진
     * init : 2023.01.28
     * updated by writer :
     * update :
     * description : 신고된 회원이 존재하는지 확인
     */
    private void checkReportedId(String id) {
        String errMsg = "존재하지 않는 아이디입니다.";
        memberJpaRepository.findByMemberId(id)
                .orElseThrow(() -> new NotExistingIdException(errMsg));
    }

    /**
     * writer : 이호진
     * init : 2023.01.28
     * updated by writer :
     * update :
     * description : 고객지원 게시글 상세보기
     *
     * comment - CustomerServiceReply에서 memberId 주입시 n + 1문제 발생할 거 같음
     */
    @Override
    public CustomerServiceDetailForm watchDetail(int num) {
        // 모두 가져오기
        CustomerService findCustomerService = customerServiceJpaRepository.findDetailInfoById(num).orElseThrow();
        // 댓글도 넣어주기(form 만들어서)
        CustomerServiceDetailForm customerServiceDetailForm =
                CustomerServiceDetailForm.createCustomerServiceDetailForm(findCustomerService);

        return customerServiceDetailForm;
    }

    /**
     * writer : 이호진
     * init : 2023.01.28
     * updated by writer :
     * update :
     * description : 모든 고객지원글 가져오기
     */
    @Override
    public Page<CustomerServiceListForm> selectList(CustomerServiceSearchCond customerServiceSearchCond, Pageable pageable) {
        // 게시글 가져오기
        Page<CustomerService> customerServices = customerServiceJpaRepository.findAllInfo(customerServiceSearchCond, pageable);
        // form에 담기
        Page<CustomerServiceListForm> result = customerServices
                .map(cs -> CustomerServiceListForm.create(cs));

        return result;
    }











}
