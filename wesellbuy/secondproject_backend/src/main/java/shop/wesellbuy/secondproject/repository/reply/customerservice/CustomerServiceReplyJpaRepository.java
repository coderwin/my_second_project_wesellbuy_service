package shop.wesellbuy.secondproject.repository.reply.customerservice;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.wesellbuy.secondproject.domain.reply.CustomerServiceReply;

/**
 * CustomerServiceReply Repository
 * writer : 이호진
 * init : 2023.01.16
 * updated by writer :
 * update :
 * description : CustomerServiceReply Repository by Spring Data Jpa
 *
 * comment : 왜 안되는지 생각해보기
 */
public interface CustomerServiceReplyJpaRepository extends
        JpaRepository<CustomerServiceReply, Integer>, CustomerServiceReplyJpaRepositoryCustom {

    // 왜 안 되는지 생각해보기
    // fetchjoin 없이 page 검색
//    public Page<CustomerServiceReply> findByIdAndContentAndCreateDate(String id, String content, String createDate, Pageable pageable);
}
