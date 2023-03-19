package shop.wesellbuy.secondproject.service.customerservice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import shop.wesellbuy.secondproject.repository.customerservice.CustomerServiceSearchCond;
import shop.wesellbuy.secondproject.web.customerservice.CustomerServiceDetailForm;
import shop.wesellbuy.secondproject.web.customerservice.CustomerServiceForm;
import shop.wesellbuy.secondproject.web.customerservice.CustomerServiceListForm;

/**
 * CustomerService Service
 * writer : 이호진
 * init : 2023.01.28
 * updated by writer :
 * update :
 * description : CustomerService Service 메소드 모음
 */
public interface CustomerServiceService {

    /**
     * writer : 이호진
     * init : 2023.01.28
     * updated by writer :
     * update :
     * description : 고객지원글 저장
     */
    int save(int memberNum, CustomerServiceForm customerServiceForm);

    /**
     * writer : 이호진
     * init : 2023.01.28
     * updated by writer :
     * update :
     * description : 고객지원 게시글 상세보기
     *
     * comment - CustomerServiceReply에서 memberId 주입시 n + 1문제 발생할 거 같음
     */
    CustomerServiceDetailForm watchDetail(int num);

    /**
     * writer : 이호진
     * init : 2023.01.28
     * updated by writer :
     * update :
     * description : 모든 고객지원글 가져오기
     */
    Page<CustomerServiceListForm> selectList(CustomerServiceSearchCond customerServiceSearchCond, Pageable pageable);
}
