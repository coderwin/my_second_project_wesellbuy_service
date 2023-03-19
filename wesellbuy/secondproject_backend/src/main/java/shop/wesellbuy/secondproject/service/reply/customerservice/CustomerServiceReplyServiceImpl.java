package shop.wesellbuy.secondproject.service.reply.customerservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.wesellbuy.secondproject.domain.CustomerService;
import shop.wesellbuy.secondproject.domain.Member;
import shop.wesellbuy.secondproject.domain.reply.CustomerServiceReply;
import shop.wesellbuy.secondproject.repository.customerservice.CustomerServiceJpaRepository;
import shop.wesellbuy.secondproject.repository.member.MemberJpaRepository;
import shop.wesellbuy.secondproject.repository.reply.customerservice.CustomerServiceReplyJpaRepository;
import shop.wesellbuy.secondproject.repository.reply.customerservice.CustomerServiceReplySearchCond;
import shop.wesellbuy.secondproject.web.reply.ReplyDetailForm;
import shop.wesellbuy.secondproject.web.reply.ReplyForm;
import shop.wesellbuy.secondproject.web.reply.ReplyUpdateForm;

/**
 * CustomerServiceReply Service 구현 클래스
 * writer : 이호진
 * init : 2023.02.01
 * updated by writer :
 * update :
 * description : CustomerServiceReply Service 구현 메소드 모음
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceReplyServiceImpl implements CustomerServiceReplyService {

    private final MemberJpaRepository memberJpaRepository;
    private final CustomerServiceJpaRepository customerServiceJpaRepository;
    private final CustomerServiceReplyJpaRepository customerServiceReplyJpaRepository;

    /**
     * writer : 이호진
     * init : 2023.02.01
     * updated by writer :
     * update :
     * description : 댓글 저장
     */
    @Override
    @Transactional
    public int save(ReplyForm replyForm, int memberNum, int customerServiceNum) {
        // member 불러오기
        Member member = memberJpaRepository.findById(memberNum).orElseThrow();
        // customerService 불러오기
        CustomerService customerService = customerServiceJpaRepository.findById(customerServiceNum).orElseThrow();
        // 댓글 생성
        CustomerServiceReply customerServiceReply = CustomerServiceReply.createCustomerServiceReply(replyForm, member, customerService);
        // 댓글 저장하기
        customerServiceReplyJpaRepository.save(customerServiceReply);

        return customerServiceReply.getNum();
    }

    /**
     * writer : 이호진
     * init : 2023.02.01
     * updated by writer :
     * update :
     * description : 댓글 수정
     */
    @Override
    @Transactional
    public void update(ReplyUpdateForm updateReplyForm) {
        // 댓글 불러오기
        CustomerServiceReply findCustomerServiceReply = customerServiceReplyJpaRepository.findById(updateReplyForm.getNum()).orElseThrow();
        // 댓글 수정
        findCustomerServiceReply.updateCustomerServiceReply(updateReplyForm);
    }

    /**
     * writer : 이호진
     * init : 2023.02.01
     * updated by writer :
     * update :
     * description : 댓글 삭제
     *               status를 변경한다.(R -> D)
     */
    @Override
    @Transactional
    public void delete(int num) {
        // 댓글 불러오기
        CustomerServiceReply findCustomerServiceReply = customerServiceReplyJpaRepository.findById(num).orElseThrow();
        // 댓글 삭제하기
        // status를 변경한다.(R -> D)
        findCustomerServiceReply.delete();
    }

//    -------------------------methods using for admin start----------------------------------

    /**
     * writer : 이호진
     * init : 2023.02.01
     * updated by writer :
     * update :
     * description : 모두 불러오기
     *               -> admin이 사용
     */
    @Override
    public Page<ReplyDetailForm> selectListForAdmin(CustomerServiceReplySearchCond cond, Pageable pageable) {
        // 모두 불러오기
        Page<CustomerServiceReply> customerServiceReplyPage = customerServiceReplyJpaRepository.findAllInfo(cond, pageable);
        // Page<ReplyDetailForm> 객체로 만들기
        // 모든 상태 포함
        Page<ReplyDetailForm> result = customerServiceReplyPage.map(r -> ReplyDetailForm.create(r));

        return result;
    }

//    -------------------------methods using for admin end----------------------------------







}
