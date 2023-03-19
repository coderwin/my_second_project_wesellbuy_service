package shop.wesellbuy.secondproject.repository.reply.customerservice;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import shop.wesellbuy.secondproject.domain.reply.CustomerServiceReply;
import shop.wesellbuy.secondproject.util.LocalDateParser;

import java.util.List;

import static shop.wesellbuy.secondproject.domain.QCustomerService.customerService;
import static shop.wesellbuy.secondproject.domain.QMember.member;
import static shop.wesellbuy.secondproject.domain.reply.QCustomerServiceReply.customerServiceReply;

/**
 * CustomerServiceReplyJpaRepositoryCustom 구현
 * writer : 이호진
 * init : 2023.01.18
 * updated by writer :
 * update :
 * description : CustomerServiceReplyJpaRepository 구현 모음 + 최적화 사용(fetch)
 */
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceReplyJpaRepositoryImpl implements CustomerServiceReplyJpaRepositoryCustom{


    private final JPAQueryFactory query;

    /**
     * writer : 이호진
     * init : 2023.01.18
     * updated by writer :
     * update :
     * description : 모든 고객지원 댓글 찾기 + fetchjoin
     */
    @Override
    public Page<CustomerServiceReply> findAllInfo(CustomerServiceReplySearchCond customerServiceReplySearchCond, Pageable pageable) {

        // list
        List<CustomerServiceReply> result = query
                .select(customerServiceReply)
                .from(customerServiceReply)
                .join(customerServiceReply.member, member).fetchJoin()
                .join(customerServiceReply.customerService, customerService).fetchJoin()
                .where(
                        customerServiceReplyIdEq(customerServiceReplySearchCond.getMemberId()),
                        customerServiceReplyContentLike(customerServiceReplySearchCond.getContent()),
                        customerServiceReplyCreateDateBetween(customerServiceReplySearchCond.getCreateDate())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(customerServiceReply.num.desc())
                .fetch();

        // totalCount
        Long totalCount = query
                .select(customerServiceReply.count())
                .from(customerServiceReply)
                .where(
                        customerServiceReplyIdEq(customerServiceReplySearchCond.getMemberId()),
                        customerServiceReplyContentLike(customerServiceReplySearchCond.getContent()),
                        customerServiceReplyCreateDateBetween(customerServiceReplySearchCond.getCreateDate())
                )
                .fetchOne();

        return new PageImpl<>(result, pageable, totalCount);
    }

    /**
     * writer : 이호진
     * init : 2023.01.18
     * updated by writer :
     * update :
     * description : 고객지원 댓글 정보 검색 조건 eq by createDate
     */
    private BooleanExpression customerServiceReplyCreateDateBetween(String createDate) {
        if(StringUtils.hasText(createDate)) {
            // String을 LocalDateTime으로 바꾸기
            LocalDateParser localDateParser = new LocalDateParser(createDate);
            return customerServiceReply.createdDate.between(localDateParser.startDate(), localDateParser.endDate());
        }
        return null;
    }

    /**
     * writer : 이호진
     * init : 2023.01.18
     * updated by writer :
     * update :
     * description : 고객지원 댓글 정보 검색 조건 eq by content
     */
    private BooleanExpression customerServiceReplyContentLike(String content) {
        return StringUtils.hasText(content) ?
                customerServiceReply.content.like("%" + content + "%") : null;
    }

    /**
     * writer : 이호진
     * init : 2023.01.18
     * updated by writer :
     * update :
     * description : 고객지원 댓글 정보 검색 조건 eq by 회원아이디(작성자)
     */
    private BooleanExpression customerServiceReplyIdEq(String memberId) {
        return StringUtils.hasText(memberId) ? customerServiceReply.member.id.eq(memberId) : null;
    }
}
