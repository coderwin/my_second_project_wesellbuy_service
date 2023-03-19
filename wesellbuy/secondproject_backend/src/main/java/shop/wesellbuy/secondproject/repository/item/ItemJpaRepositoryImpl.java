package shop.wesellbuy.secondproject.repository.item;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import shop.wesellbuy.secondproject.domain.Item;
import shop.wesellbuy.secondproject.domain.QItem;
import shop.wesellbuy.secondproject.domain.item.ItemStatus;
import shop.wesellbuy.secondproject.util.LocalDateParser;

import java.util.List;
import java.util.Optional;

import static shop.wesellbuy.secondproject.domain.QItem.item;
import static shop.wesellbuy.secondproject.domain.QMember.member;

/**
 * ItemJpaRepositoryCustom 구현
 * writer : 이호진
 * init : 2023.01.19
 * updated by writer : 이호진
 * update : 2023.02.14
 * description : ItemJpaRepository 구현 모음 + 최적화 사용(fetch)
 *
 * update : itemNameEq() -> itemNameLike() 변경
 */
@RequiredArgsConstructor
@Slf4j
public class ItemJpaRepositoryImpl implements ItemJpaRepositoryCustom{
    private final JPAQueryFactory query;

    /**
     * writer : 이호진
     * init : 2023.01.19
     * updated by writer : 이호진
     * update : 2023.02.02
     * description : 모든 상품 게시글 찾기 + fetchjoin
     *               -> admin 외에 사용
     *
     * update : add where절에 status = R(Register)
     *
     * comment : test 해보기
     */
    @Override
    public Page<Item> findAllInfo(ItemSearchCond itemSearchCond, Pageable pageable) {

        // list
        List<Item> result = query
                .select(item)
                .from(item)
                .join(item.member, member).fetchJoin()
                .where(
                        itemIdEq(itemSearchCond.getMemberId()),
                        itemNameLike(itemSearchCond.getName()),
                        itemCreateDateBetween(itemSearchCond.getCreateDate()),
                        itemDtypeEq(itemSearchCond.getDtype()),
                        item.status.eq(ItemStatus.R)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(item.num.desc())
                .fetch();

        // totalCount
        Long totalCount = query
                .select(item.count())
                .from(item)
                .where(
                        itemIdEq(itemSearchCond.getMemberId()),
                        itemNameLike(itemSearchCond.getName()),
                        itemCreateDateBetween(itemSearchCond.getCreateDate()),
                        itemDtypeEq(itemSearchCond.getDtype()),
                        item.status.eq(ItemStatus.R)
                )
                .fetchOne();

        return new PageImpl<>(result, pageable, totalCount);
    }

    /**
     * writer : 이호진
     * init : 2023.01.19
     * updated by writer :
     * update :
     * description : 상품 정보 검색 조건 eq by createDate
     */
    private BooleanExpression itemCreateDateBetween(String createDate) {
        if(StringUtils.hasText(createDate)) {
            // String을 LocalDateTime으로 바꾸기
            LocalDateParser localDateParser = new LocalDateParser(createDate);
            return item.createdDate.between(localDateParser.startDate(), localDateParser.endDate());
        }
        return null;
    }

//    /**
//     * writer : 이호진
//     * init : 2023.01.18
//     * updated by writer :
//     * update :
//     * description : 상품 정보 검색 조건 eq by 가격(이상/이하)
//     *
//     * comment - 조건을 사용할 지 생각해보기
//     */
//    private BooleanExpression itemPriceEq(Integer price, String upAndDown) {
//        if(price != null) {
//            if("up".equalsIgnoreCase(upAndDown)) {
//                // price 이상 검색
//                return item.price.goe(price);
//            } else if("down".equalsIgnoreCase(upAndDown)) {
//                // price 이하 검색
//                return item.price.loe(price);
//            }
//        }
//        return null;
//    }

    /**
     * writer : 이호진
     * init : 2023.01.19
     * updated by writer :
     * update :
     * description : 상품 정보 검색 조건 eq by 상품이름
     */
    private BooleanExpression itemNameEq(String name) {
        return StringUtils.hasText(name) ? item.name.eq(name) : null;
    }

    /**
     * writer : 이호진
     * init : 2023.02.14
     * updated by writer :
     * update :
     * description : 상품 정보 검색 조건 Like by 상품이름
     */
    private BooleanExpression itemNameLike(String name) {
        return StringUtils.hasText(name) ? item.name.like( "%" + name + "%") : null;
    }

    /**
     * writer : 이호진
     * init : 2023.01.19
     * updated by writer :
     * update :
     * description : 상품 정보 검색 조건 eq by 상품 등록 아이디
     */
    private BooleanExpression itemIdEq(String memberId) {
        return StringUtils.hasText(memberId) ? item.member.id.eq(memberId) : null;
    }

    /**
     * writer : 이호진
     * init : 2023.01.19
     * updated by writer :
     * update :
     * description : 상품 정보 검색 조건 eq by 상품종류
     */
    private BooleanExpression itemDtypeEq(String dtype) {
        return StringUtils.hasText(dtype) ? item.dtype.eq(dtype) : null;
    }

    /**
     * writer : 이호진
     * init : 2023.01.19
     * updated by writer :
     * update :
     * description : 상품 상세보기 + fetchjoin by num(id)
     */
    @Override
    public Optional<Item> findDetailInfoById(int num) {

        Item findItem = query
                .select(item)
                .from(item)
                .join(item.member, member).fetchJoin()
                .where(item.num.eq(num))
                .fetchOne();

        return Optional.ofNullable(findItem);
    }


    /**
     * writer : 이호진
     * init : 2023.01.28
     * updated by writer :
     * update :
     * description : 상품 name & 판매자 id로 Item 검색
     */
    @Override
    public Optional<Item> findByNameAndSellerId(String itemName, String memberId) {
        Item item = query
                .select(QItem.item)
                .from(QItem.item)
                .join(QItem.item.member, member).fetchJoin()
                .where(
                        QItem.item.name.eq(itemName),
                        member.id.eq(memberId)
                )
                .fetchFirst();

        return Optional.ofNullable(item);
    }

//    -------------------------methods using for admin start----------------------------------

    /**
     * writer : 이호진
     * init : 2023.02.02
     * updated by writer :
     * update :
     * description : 모든 추천합니다 게시글 찾기 + fetchjoin
     *               -> admin에서 사용
     *
     * comment : test 해보기
     */
    @Override
    public Page<Item> findAllInfoForAdmin(ItemSearchCond itemSearchCond, Pageable pageable) {

        // list
        List<Item> result = query
                .select(item)
                .from(item)
                .join(item.member, member).fetchJoin()
                .where(
                        itemIdEq(itemSearchCond.getMemberId()),
                        itemNameLike(itemSearchCond.getName()),
                        itemCreateDateBetween(itemSearchCond.getCreateDate()),
                        itemDtypeEq(itemSearchCond.getDtype())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(item.num.desc())
                .fetch();

        // totalCount
        Long totalCount = query
                .select(item.count())
                .from(item)
                .where(
                        itemIdEq(itemSearchCond.getMemberId()),
                        itemNameLike(itemSearchCond.getName()),
                        itemCreateDateBetween(itemSearchCond.getCreateDate()),
                        itemDtypeEq(itemSearchCond.getDtype())
                )
                .fetchOne();

        return new PageImpl<>(result, pageable, totalCount);
    }

//    -------------------------methods using for admin end----------------------------------

}
