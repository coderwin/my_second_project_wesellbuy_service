package shop.wesellbuy.secondproject.repository.customerservice;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import shop.wesellbuy.secondproject.domain.CustomerService;
import shop.wesellbuy.secondproject.domain.QCustomerService;
import shop.wesellbuy.secondproject.util.LocalDateParser;

import java.util.List;
import java.util.Optional;

import static shop.wesellbuy.secondproject.domain.QCustomerService.customerService;
import static shop.wesellbuy.secondproject.domain.QMember.member;

/**
 * CustomerServiceJpaRepositoryCustom 구현
 * writer : 이호진
 * init : 2023.01.17
 * updated by writer :
 * update :
 * description : CustomerServiceJpaRepository 구현 모음 + 최적화 사용(fetch)
 */
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceJpaRepositoryImpl implements CustomerServiceJpaRepositoryCustom{

    private final JPAQueryFactory query;

    /**
     * writer : 이호진
     * init : 2023.01.17
     * updated by writer : 이호진
     * update : 2023.01.18
     * description : 모든 고객지원 게시글 찾기 + fetchjoin
     *
     * comment - update : 날짜 검색 조건 추가
     */
    @Override
    public Page<CustomerService> findAllInfo(CustomerServiceSearchCond customerServiceSearchCond, Pageable pageable) {

        // list
        List<CustomerService> result = query
                .select(customerService)
                .from(customerService)
                .join(customerService.member, member).fetchJoin()
                .where(
                        customerServiceIdEq(customerServiceSearchCond.getMemberId()),
                        customerServiceReportedIdEq(customerServiceSearchCond.getReportedId()),
                        customerServiceCreateDateBetween(customerServiceSearchCond.getCreateDate())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(customerService.num.desc())
                .fetch();

        // totalCount
        Long totalCount = query
                .select(customerService.count())
                .from(customerService)
                .where(
                        customerServiceIdEq(customerServiceSearchCond.getMemberId()),
                        customerServiceReportedIdEq(customerServiceSearchCond.getReportedId()),
                        customerServiceCreateDateBetween(customerServiceSearchCond.getCreateDate())
                )
                .fetchOne();

        return new PageImpl<>(result, pageable, totalCount);
    }

    /**
     * writer : 이호진
     * init : 2023.01.18
     * updated by writer :
     * update :
     * description : 고객지원 정보 검색 조건 eq by createDate
     */
    private BooleanExpression customerServiceCreateDateBetween(String createDate) {
        if(StringUtils.hasText(createDate)) {
            // String을 LocalDateTime으로 바꾸기
            LocalDateParser localDateParser = new LocalDateParser(createDate);
            return customerService.createdDate.between(localDateParser.startDate(), localDateParser.endDate());
        }
        return null;
    }

    /**
     * writer : 이호진
     * init : 2023.01.17
     * updated by writer :
     * update :
     * description : 고객지원 정보 검색 조건 eq by 회원아이디(신고당한 아이디)
     */
    private BooleanExpression customerServiceReportedIdEq(String reportedId) {
        return StringUtils.hasText(reportedId) ? customerService.reportedId.eq(reportedId) : null;
    }

    /**
     * writer : 이호진
     * init : 2023.01.17
     * updated by writer :
     * update :
     * description : 고객지원 정보 검색 조건 eq by 회원아이디(신고한 아이디)
     */
    private BooleanExpression customerServiceIdEq(String memberId) {
        return StringUtils.hasText(memberId) ? customerService.member.id.eq(memberId) : null;
    }

    /**
     * writer : 이호진
     * init : 2023.01.27
     * updated by writer :
     * update :
     * description : 고객지원 상세보기 + fetchjoin by num(id)
     */
    @Override
    public Optional<CustomerService> findDetailInfoById(int num) {
        CustomerService customerService = query
                .select(QCustomerService.customerService)
                .from(QCustomerService.customerService)
                .join(QCustomerService.customerService.member, member).fetchJoin()
                .where(QCustomerService.customerService.num.eq(num))
                .fetchOne();

        return Optional.ofNullable(customerService);
    }

}
