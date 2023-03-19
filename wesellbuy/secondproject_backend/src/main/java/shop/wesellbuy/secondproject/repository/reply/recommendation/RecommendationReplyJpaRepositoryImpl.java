package shop.wesellbuy.secondproject.repository.reply.recommendation;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import shop.wesellbuy.secondproject.domain.reply.RecommendationReply;
import shop.wesellbuy.secondproject.util.LocalDateParser;

import java.util.List;

import static shop.wesellbuy.secondproject.domain.QCustomerService.customerService;
import static shop.wesellbuy.secondproject.domain.QMember.member;
import static shop.wesellbuy.secondproject.domain.QRecommendation.recommendation;
import static shop.wesellbuy.secondproject.domain.reply.QRecommendationReply.recommendationReply;

/**
 * RecommendationReplyJpaRepositoryCustom 구현
 * writer : 이호진
 * init : 2023.01.19
 * updated by writer :
 * update :
 * description : RecommendationReplyJpaRepository 구현 모음 + 최적화 사용(fetch)
 */
@RequiredArgsConstructor
@Slf4j
public class RecommendationReplyJpaRepositoryImpl implements RecommendationReplyJpaRepositoryCustom {

    private final JPAQueryFactory query;

    /**
     * writer : 이호진
     * init : 2023.01.19
     * updated by writer :
     * update :
     * description : 모든 추천합니다 댓글 찾기 + fetchjoin
     */
    @Override
    public Page<RecommendationReply> findAllInfo(RecommendationReplySearchCond recommendationReplySearchCond, Pageable pageable) {

        // list
        List<RecommendationReply> result = query
                .select(recommendationReply)
                .from(recommendationReply)
                .join(recommendationReply.member, member).fetchJoin()
                .join(recommendationReply.recommendation, recommendation).fetchJoin()
                .where(
                        recommendationReplyIdEq(recommendationReplySearchCond.getMemberId()),
                        recommendationReplyContentLike(recommendationReplySearchCond.getContent()),
                        recommendationReplyCreateDateBetween(recommendationReplySearchCond.getCreateDate())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(recommendationReply.num.desc())
                .fetch();

        // totalCount
        Long totalCount = query
                .select(recommendationReply.count())
                .from(recommendationReply)
                .where(
                        recommendationReplyIdEq(recommendationReplySearchCond.getMemberId()),
                        recommendationReplyContentLike(recommendationReplySearchCond.getContent()),
                        recommendationReplyCreateDateBetween(recommendationReplySearchCond.getCreateDate())
                )
                .fetchOne();

        return new PageImpl<>(result, pageable, totalCount);
    }

    /**
     * writer : 이호진
     * init : 2023.01.19
     * updated by writer :
     * update :
     * description : 추천합니다 댓글 정보 검색 조건 eq by createDate
     */
    private BooleanExpression recommendationReplyCreateDateBetween(String createDate) {
        if(StringUtils.hasText(createDate)) {
            // String을 LocalDateTime으로 바꾸기
            LocalDateParser localDateParser = new LocalDateParser(createDate);
            return recommendationReply.createdDate.between(localDateParser.startDate(), localDateParser.endDate());
        }
        return null;
    }

    /**
     * writer : 이호진
     * init : 2023.01.19
     * updated by writer :
     * update :
     * description : 추천합니다 댓글 정보 검색 조건 eq by content
     */
    private BooleanExpression recommendationReplyContentLike(String content) {
        return StringUtils.hasText(content) ?
                recommendationReply.content.like("%" + content + "%") : null;
    }

    /**
     * writer : 이호진
     * init : 2023.01.19
     * updated by writer :
     * update :
     * description : 추천합니다 댓글 정보 검색 조건 eq by 회원아이디(작성자)
     */
    private BooleanExpression recommendationReplyIdEq(String memberId) {
        return StringUtils.hasText(memberId) ? recommendationReply.member.id.eq(memberId) : null;
    }
}
