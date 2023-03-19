package shop.wesellbuy.secondproject.repository.reply.item;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import shop.wesellbuy.secondproject.domain.reply.ItemReply;
import shop.wesellbuy.secondproject.domain.reply.RecommendationReply;
import shop.wesellbuy.secondproject.repository.reply.recommendation.RecommendationReplySearchCond;
import shop.wesellbuy.secondproject.util.LocalDateParser;

import java.util.List;

import static shop.wesellbuy.secondproject.domain.QItem.item;
import static shop.wesellbuy.secondproject.domain.QMember.member;
import static shop.wesellbuy.secondproject.domain.QRecommendation.recommendation;
import static shop.wesellbuy.secondproject.domain.reply.QItemReply.itemReply;
import static shop.wesellbuy.secondproject.domain.reply.QRecommendationReply.recommendationReply;

/**
 * ItemReplyJpaRepositoryCustom 구현
 * writer : 이호진
 * init : 2023.01.19
 * updated by writer :
 * update :
 * description : ItemReplyJpaRepository 구현 모음 + 최적화 사용(fetch)
 */
@RequiredArgsConstructor
@Slf4j
public class ItemReplyJpaRepositoryImpl implements ItemReplyJpaRepositoryCustom{

    private final JPAQueryFactory query;
    /**
     * writer : 이호진
     * init : 2023.01.19
     * updated by writer :
     * update :
     * description : 모든 상품 댓글 찾기
     */
    @Override
    public Page<ItemReply> findAllInfo(ItemReplySearchCond itemReplySearchCond, Pageable pageable) {

        // list
        List<ItemReply> result = query
                .select(itemReply)
                .from(itemReply)
                .join(itemReply.member, member).fetchJoin()
                .join(itemReply.item, item).fetchJoin()
                .where(
                        itemReplyIdEq(itemReplySearchCond.getMemberId()),
                        itemReplyContentLike(itemReplySearchCond.getContent()),
                        itemReplyCreateDateBetween(itemReplySearchCond.getCreateDate())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(itemReply.num.desc())
                .fetch();

        // totalCount
        Long totalCount = query
                .select(itemReply.count())
                .from(itemReply)
                .where(
                        itemReplyIdEq(itemReplySearchCond.getMemberId()),
                        itemReplyContentLike(itemReplySearchCond.getContent()),
                        itemReplyCreateDateBetween(itemReplySearchCond.getCreateDate())
                )
                .fetchOne();

        return new PageImpl<>(result, pageable, totalCount);
    }

    /**
     * writer : 이호진
     * init : 2023.01.19
     * updated by writer :
     * update :
     * description : 상품 댓글 정보 검색 조건 eq by createDate
     */
    private BooleanExpression itemReplyCreateDateBetween(String createDate) {
        if(StringUtils.hasText(createDate)) {
            // String을 LocalDateTime으로 바꾸기
            LocalDateParser localDateParser = new LocalDateParser(createDate);
            return itemReply.createdDate.between(localDateParser.startDate(), localDateParser.endDate());
        }
        return null;
    }

    /**
     * writer : 이호진
     * init : 2023.01.19
     * updated by writer :
     * update :
     * description : 상품 댓글 정보 검색 조건 eq by content
     */
    private BooleanExpression itemReplyContentLike(String content) {
        return StringUtils.hasText(content) ?
                itemReply.content.like("%" + content + "%") : null;
    }

    /**
     * writer : 이호진
     * init : 2023.01.19
     * updated by writer :
     * update :
     * description : 상품 댓글 정보 검색 조건 eq by 회원아이디(작성자)
     */
    private BooleanExpression itemReplyIdEq(String memberId) {
        return StringUtils.hasText(memberId) ? itemReply.member.id.eq(memberId) : null;
    }


}
