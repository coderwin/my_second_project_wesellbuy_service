package shop.wesellbuy.secondproject.repository.likes;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.wesellbuy.secondproject.domain.Item;
import shop.wesellbuy.secondproject.domain.item.ItemStatus;
import shop.wesellbuy.secondproject.domain.likes.ItemLikes;

import java.util.List;
import java.util.Optional;

import static shop.wesellbuy.secondproject.domain.QItem.item;
import static shop.wesellbuy.secondproject.domain.QMember.member;
import static shop.wesellbuy.secondproject.domain.likes.QItemLikes.itemLikes;

/**
 * ItemLikesJpaRepositoryCustom 구현
 * writer : 이호진
 * init : 2023.01.19
 * updated by writer : 2023.03.14
 * update : 이호진
 * description : ItemLikesJpaRepository 구현 모음 + 최적화 사용(fetch)
 *
 * comment : item 좋아요 많은 1,2,3 어떻게 출력할지 고민
 *
 * update : update : findByItemNumAndMemberNum 추가
 */
@RequiredArgsConstructor
@Slf4j
public class ItemLikesJpaRepositoryImpl implements ItemLikesJpaRepositoryCustom, ItemLikesJpaRepositoryQueryCustom{

    private final EntityManager em;
    private final JPAQueryFactory query;

    /**
     * writer : 이호진
     * init : 2023.01.19
     * updated by writer : 이호진
     * update : 2023.01.18
     * description : 모든 상품_좋아요 찾기 + fetchjoin
     *               ...ById == ...ByNum
     *
     * comment : 무엇을 select할지 결정 필요(itemLikes? or item? => service에서 결정하자)
     *           -> 필요 없을수도 있다(item을 select한 후, service에서 itemLikes를 dto에 담는다)
     */
    @Override
    public List<ItemLikes> findAllInfoById(int memberNum) {

        return query
                .selectFrom(itemLikes)
                .join(itemLikes.member, member).fetchJoin()
                .join(itemLikes.item, item).fetchJoin()
                .where(itemLikes.member.num.eq(memberNum))
                .fetch();
    }

    /**
     * writer : 이호진
     * init : 2023.03.14
     * updated by writer :
     * update :
     * description : 상품 좋아요 찾기 by itemNum and memberNum
     */
    @Override
    public Optional<ItemLikes> findByItemNumAndMemberNum(int itemNum, int memberNum) {

        ItemLikes findItemLikes = query
                .selectFrom(itemLikes)
                .where(
                        itemLikes.item.num.eq(itemNum),
                        itemLikes.member.num.eq(memberNum)
                )
                .fetchOne();

        return Optional.ofNullable(findItemLikes);
    }

    /**
     * writer : 이호진
     * init : 2023.01.20
     * updated by writer : 이호진
     * update : 2023.02.02
     * description : 모든 상품 좋아요 많은 순위 찾기
     *
     * update : add where절에 item status == R(Register)
     *
     * comment : test 해보기
     */
    @Override
    public List<Tuple> findRank() {
        String countStr = "count";
        NumberExpression<Long> count = itemLikes.count();// 상품의 좋아요 개수

        List<Tuple> result = query
                .select(itemLikes.count(), item, member)
                .from(itemLikes)
                .join(itemLikes.item, item)
                .join(item.member, member)
                .where(item.status.eq(ItemStatus.R))
                .groupBy(item.num)
                .orderBy(count.desc(), item.num.asc())
                .fetch();

        return result;
    }

    /**
     * writer : 이호진
     * init : 2023.01.20
     * updated by writer : 이호진
     * update : 2023.02.03
     * description : 모든 상품 좋아요 많은 순위 찾기 V4
     *               -> List<Item> 반환
     *               -> fetchJoin 사용
     *
     * update : itemLikes, item의 fetch join 지움
     */
    @Override
    public List<Item> findRankV4() {

        List<Item> result = query
                .select(item)
                .from(itemLikes)
                .join(itemLikes.item, item)
                .join(item.member, member).fetchJoin()
                .where(item.status.eq(ItemStatus.R))
                .groupBy(item.num)
                .orderBy(itemLikes.count().desc(), item.num.asc())
                .fetch();

        return result;
    }

//    -------------------------------------ItemLikesJpaRepositoryQueryCustom method start---------------------

    /**
     * writer : 이호진
     * init : 2023.01.20
     * updated by writer : 이호진
     * update : 2023.02.02
     * description : 모든 상품 좋아요 많은 순위 찾기 + select(dto)
     *
     * update : add where절에 item status == R(Register)
     *
     * comment : test 해보기
     */
    @Override
    public List<ItemRankDto> findRankV2() {

        List<ItemRankDto> result = query.select(
                    Projections.constructor(ItemRankDto.class,
                            itemLikes.count(),
                            item.num,
                            item.price,
                            item.status,
                            item.stock,
                            member.id
                            )
                )
                .from(itemLikes)
                .join(itemLikes.item, item)
                .join(item.member, member)
                .where(item.status.eq(ItemStatus.R))
                .groupBy(item.num)
                .orderBy(itemLikes.count().desc(), item.num.asc())
                .fetch();

        return result;
    }

    /**
     * writer : 이호진
     * init : 2023.01.20
     * updated by writer :
     * update :
     * description : 모든 상품 좋아요 많은 순위 찾기 + query
     *
     * comment : rank() 함수를 사용할 수 없을까?
     */
//    @Override
    public List<Tuple> findRankV3() {


        String query = "select" +
//                "  rank() over (order by count(i1_0.num) desc)," +
                " a.num.count(), " +
                " i.item_num, " +
                " i.content, " +
                " i.price," +
                " i.status," +
                " i.stock," +
                " m.id," +
                " from" +
                " ItemLikes a" +
                " join" +
                " il.item i"+
                " join" +
                " i.member m"+
                " group by" +
                " a.num" +
                " order by" +
                " rank() over (order by a.num.count() desc), i.num";

        List<Tuple> result = em.createQuery(query).getResultList();

        return result;
    }

//    -------------------------------------ItemLikesJpaRepositoryQueryCustom method end---------------------



}
