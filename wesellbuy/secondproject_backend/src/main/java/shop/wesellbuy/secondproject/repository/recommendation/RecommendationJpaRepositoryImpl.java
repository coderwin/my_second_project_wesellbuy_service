package shop.wesellbuy.secondproject.repository.recommendation;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import shop.wesellbuy.secondproject.domain.Recommendation;
import shop.wesellbuy.secondproject.domain.board.BoardStatus;
import shop.wesellbuy.secondproject.util.LocalDateParser;

import java.util.List;
import java.util.Optional;

import static shop.wesellbuy.secondproject.domain.QMember.member;
import static shop.wesellbuy.secondproject.domain.QRecommendation.recommendation;

/**
 * RecommendationJpaRepositoryCustom 구현
 * writer : 이호진
 * init : 2023.01.18
 * updated by writer :
 * update :
 * description : RecommendationJpaRepository 구현 모음 + 최적화 사용(fetch)
 */
@RequiredArgsConstructor
@Slf4j
public class RecommendationJpaRepositoryImpl implements RecommendationJpaRepositoryCustom{

    private final JPAQueryFactory query;

    /**
     * writer : 이호진
     * init : 2023.02.01
     * updated by writer :
     * update :
     * description : 모든 추천합니다 게시글 찾기 + fetchjoin
     *               -> admin에서 사용
     */
    @Override
    public Page<Recommendation> findAllInfoForAdmin(RecommendationSearchCond recommendationSearchCond, Pageable pageable) {

        // list
        List<Recommendation> result = query
                .select(recommendation)
                .from(recommendation)
                .join(recommendation.member, member).fetchJoin()
                .where(
                        recommendationIdEq(recommendationSearchCond.getMemberId()),
                        recommendationItemNameEq(recommendationSearchCond.getItemName()),
                        recommendationItemSellerIdEq(recommendationSearchCond.getSellerId()),
                        recommendationCreateDateBetween(recommendationSearchCond.getCreateDate())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(recommendation.num.desc())
                .fetch();

        // totalCount
        Long totalCount = query
                .select(recommendation.count())
                .from(recommendation)
                .where(
                        recommendationIdEq(recommendationSearchCond.getMemberId()),
                        recommendationItemNameEq(recommendationSearchCond.getItemName()),
                        recommendationItemSellerIdEq(recommendationSearchCond.getSellerId()),
                        recommendationCreateDateBetween(recommendationSearchCond.getCreateDate())
                )
                .fetchOne();

        return new PageImpl<>(result, pageable, totalCount);
    }

    /**
     * writer : 이호진
     * init : 2023.01.18
     * updated by writer : 이호진
     * update : 2023.02.01
     * description : 모든 추천합니다 게시글 찾기 + fetchjoin
     *
     * update : add where절에 status = R(Register인 것만)
     */
    @Override
    public Page<Recommendation> findAllInfo(RecommendationSearchCond recommendationSearchCond, Pageable pageable) {

        // list
        List<Recommendation> result = query
                .select(recommendation)
                .from(recommendation)
                .join(recommendation.member, member).fetchJoin()
                .where(
                        recommendationIdEq(recommendationSearchCond.getMemberId()),
                        recommendationItemNameEq(recommendationSearchCond.getItemName()),
                        recommendationItemSellerIdEq(recommendationSearchCond.getSellerId()),
                        recommendationCreateDateBetween(recommendationSearchCond.getCreateDate()),
                        recommendation.status.eq(BoardStatus.R)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(recommendation.num.desc())
                .fetch();

        // totalCount
        Long totalCount = query
                .select(recommendation.count())
                .from(recommendation)
                .where(
                        recommendationIdEq(recommendationSearchCond.getMemberId()),
                        recommendationItemNameEq(recommendationSearchCond.getItemName()),
                        recommendationItemSellerIdEq(recommendationSearchCond.getSellerId()),
                        recommendationCreateDateBetween(recommendationSearchCond.getCreateDate()),
                        recommendation.status.eq(BoardStatus.R)
                )
                .fetchOne();

        return new PageImpl<>(result, pageable, totalCount);
    }

    /**
     * writer : 이호진
     * init : 2023.01.18
     * updated by writer :
     * update :
     * description : 추천합니다 정보 검색 조건 eq by createDate
     */
    private BooleanExpression recommendationCreateDateBetween(String createDate) {
        if(StringUtils.hasText(createDate)) {
            // String을 LocalDateTime으로 바꾸기
            LocalDateParser localDateParser = new LocalDateParser(createDate);
            return recommendation.createdDate.between(localDateParser.startDate(), localDateParser.endDate());
        }
        return null;
    }

    /**
     * writer : 이호진
     * init : 2023.01.18
     * updated by writer :
     * update :
     * description : 추천합니다 정보 검색 조건 eq by 판매자 아이디
     */
    private BooleanExpression recommendationItemSellerIdEq(String sellerId) {
        return StringUtils.hasText(sellerId) ? recommendation.sellerId.eq(sellerId) : null;
    }

    /**
     * writer : 이호진
     * init : 2023.01.18
     * updated by writer :
     * update :
     * description : 추천합니다 정보 검색 조건 eq by 상품이름
     */
    private BooleanExpression recommendationItemNameEq(String itemName) {
        return StringUtils.hasText(itemName) ? recommendation.itemName.eq(itemName) : null;
    }

    /**
     * writer : 이호진
     * init : 2023.01.18
     * updated by writer :
     * update :
     * description : 추천합니다 정보 검색 조건 eq by 작성자 아이디
     */
    private BooleanExpression recommendationIdEq(String memberId) {
        return StringUtils.hasText(memberId) ? recommendation.member.id.eq(memberId) : null;
    }

    /**
     * writer : 이호진
     * init : 2023.01.18
     * updated by writer : 이호진
     * update : 2023.01.18
     * description : 추천합니다 상세보기 + fetchjoin by num(id)
     */
    public Optional<Recommendation> findDetailInfoById(int num) {

        Recommendation findRecommendation = query
                .select(recommendation)
                .from(recommendation)
                .join(recommendation.member, member).fetchJoin()
                .where(recommendation.num.eq(num))
                .fetchOne();

        return Optional.ofNullable(findRecommendation);
    }


}
