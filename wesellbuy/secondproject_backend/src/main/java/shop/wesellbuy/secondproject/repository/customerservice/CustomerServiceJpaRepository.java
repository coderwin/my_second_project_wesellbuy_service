package shop.wesellbuy.secondproject.repository.customerservice;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.wesellbuy.secondproject.domain.CustomerService;

/**
 * CustomerService Repository
 * writer : 이호진
 * init : 2023.01.16
 * updated by writer :
 * update :
 * description : CustomerService Repository by Spring Data Jpa
 */
public interface CustomerServiceJpaRepository extends
        JpaRepository<CustomerService, Integer>,
        CustomerServiceJpaRepositoryCustom {

}
