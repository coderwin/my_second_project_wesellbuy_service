package shop.wesellbuy.secondproject.repository.customerservice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.wesellbuy.secondproject.domain.CustomerService;

import java.util.Optional;


/**
 * CustomerService Repository by using queryDsl
 * writer : 이호진
 * init : 2023.01.16
 * updated by writer :
 * update :
 * description : querydsl이용한 CustomerServiceJpaRepository 모음
 */
public interface CustomerServiceJpaRepositoryCustom {

    /**
     * writer : 이호진
     * init : 2023.01.17
     * updated by writer :
     * update :
     * description : 모든 고객지원 게시글 찾기
     */
    public Page<CustomerService> findAllInfo(CustomerServiceSearchCond customerServiceSearchCond, Pageable pageable);

    /**
     * writer : 이호진
     * init : 2023.01.27
     * updated by writer :
     * update :
     * description : 고객지원 상세보기 + fetchjoin by num(id)
     */
    Optional<CustomerService> findDetailInfoById(int num);
}
