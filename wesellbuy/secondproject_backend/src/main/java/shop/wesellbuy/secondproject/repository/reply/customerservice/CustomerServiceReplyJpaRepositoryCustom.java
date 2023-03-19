package shop.wesellbuy.secondproject.repository.reply.customerservice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.wesellbuy.secondproject.domain.reply.CustomerServiceReply;

/**
 * CustomerServiceReply Repository by using queryDsl
 * writer : 이호진
 * init : 2023.01.16
 * updated by writer :
 * update :
 * description : querydsl이용한 CustomerServiceReplyJpaRepository 모음
 */
public interface CustomerServiceReplyJpaRepositoryCustom {

    /**
     * writer : 이호진
     * init : 2023.01.17
     * updated by writer :
     * update :
     * description : 모든 고객지원 댓글 찾기
     */
    public Page<CustomerServiceReply> findAllInfo(CustomerServiceReplySearchCond customerServiceReplySearchCond, Pageable pageable);
}
