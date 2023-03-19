package shop.wesellbuy.secondproject.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.wesellbuy.secondproject.domain.Member;

import java.util.List;

/**
 * Member Repository
 * writer : 이호진
 * init : 2023.01.16
 * updated by writer :
 * update :
 * description : Member Repository by Spring Data Jpa
 */
public interface MemberJpaRepository extends JpaRepository<Member, Integer>, MemberJpaRepositoryCustom {

}
