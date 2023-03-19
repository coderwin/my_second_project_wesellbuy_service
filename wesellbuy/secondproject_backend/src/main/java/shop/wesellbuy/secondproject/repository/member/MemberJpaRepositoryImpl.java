package shop.wesellbuy.secondproject.repository.member;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.util.StringUtils;
import shop.wesellbuy.secondproject.domain.Member;
import shop.wesellbuy.secondproject.util.LocalDateParser;

import java.util.List;
import java.util.Optional;

import static shop.wesellbuy.secondproject.domain.QMember.member;
import static shop.wesellbuy.secondproject.domain.member.QSelfPicture.selfPicture;

/**
 * MemberJpaRepositoryCustom 구현
 * writer : 이호진
 * init : 2023.01.16
 * updated by writer :
 * update :
 * description : Admin에서 사용하는 MemberJpaRepository 구현 모음 + 최적화 사용(fetch)
 */
@RequiredArgsConstructor
@Slf4j
public class MemberJpaRepositoryImpl implements MemberJpaRepositoryCustom{

    private final JPAQueryFactory query; // qureyDsl 사용 위한 필드

    /**
     * writer : 이호진
     * init : 2023.01.26
     * updated by writer :
     * update :
     * description : 회원 찾기 by id
     *
     * comment : text 진행하기
     */
    public Optional<Member> findByMemberId(String id) {

        Member findMember = query
                .select(member)
                .from(member)
                .where(member.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(findMember);
    }

    /**
     * writer : 이호진
     * init : 2023.01.17
     * updated by writer :
     * update :
     * description : 모든 회원 정보 찾기 by 이름 and email and selfPhone
     */
    @Override
    public List<Member> findByNameAndSelfPhoneAndEmail(MemberSearchIdCond memberSearchIdCond) {

        return query
                .selectFrom(member)
                .where(
                        memberNameEq(memberSearchIdCond.getName()),
                        memberEmailEq(memberSearchIdCond.getEmail()),
                        memberSelfPhoneEq(memberSearchIdCond.getSelfPhone())
                )
                .fetch();
    }

    /**
     * writer : 이호진
     * init : 2023.01.17
     * updated by writer :
     * update :
     * description : 회원 정보 검색 조건 eq by name
     */
    private BooleanExpression memberNameEq(String name) {
        return member.name.eq(name);
    }

    /**
     * writer : 이호진
     * init : 2023.01.17
     * updated by writer :
     * update :
     * description : 회원 정보 검색 조건 eq by id
     */
    private BooleanExpression memberIdEq(String id) {
        return member.id.eq(id);
    }

    /**
     * writer : 이호진
     * init : 2023.01.17
     * updated by writer :
     * update :
     * description : 회원 정보 검색 조건 eq by email
     */
    private BooleanExpression memberEmailEq(String email) {
        if(StringUtils.hasText(email)) {
            return member.email.eq(email);
        }
        return null;
    }

    /**
     * writer : 이호진
     * init : 2023.01.17
     * updated by writer :
     * update :
     * description : 회원 정보 검색 조건 eq by slefPhone
     */
    private BooleanExpression memberSelfPhoneEq(String selfPhone) {
        if(StringUtils.hasText(selfPhone)) {
            return member.phones.selfPhone.eq(selfPhone);
        }
        return null;
    }

    /**
     * writer : 이호진
     * init : 2023.01.17
     * updated by writer :
     * update :
     * description : 모든 회원 정보 찾기 by id and email and selfPhone
     */
    @Override
    public List<Member> findByIdAndSelfPhoneAndEmail(MemberSearchPwdCond memberSearchPwdCond) {
        return query
                .selectFrom(member)
                .where(
                        memberIdEq(memberSearchPwdCond.getId()),
                        memberEmailEq(memberSearchPwdCond.getEmail()),
                        memberSelfPhoneEq(memberSearchPwdCond.getSelfPhone())
                )
                .fetch();
    }


    /**
     * writer : 이호진
     * init : 2023.01.17
     * updated by writer :
     * update :
     * description : 회원 상세정보 찾기
     */
    public Optional<Member> findDetailInfoById(int num) {
        Member findMember = query
                .select(member)
                .from(member)
                .leftJoin(member.selfPicture, selfPicture).fetchJoin()
                .where(member.num.eq(num))
                .fetchOne();

        return Optional.ofNullable(findMember);
    }


    /**
     * writer : 이호진
     * init : 2023.01.16
     * updated by writer : 이호진
     * update : 2023.02.12
     * description : 전체 회원정보 찾기 + paging 구현 admin에서 사용
     *
     * update : selfPicture를 join 추가
     */
    @Override
    public Page<Member> findAllInfo(MemberSearchCond memberSearchCond, Pageable pageable) {

        // 모든 회원 정보 query 보내기
        List<Member> result = query
                .select(member)
                .from(member)
                .join(member.selfPicture, selfPicture).fetchJoin()
                .where(memberIdLike(memberSearchCond.getId()),
                        memberCountryLike(memberSearchCond.getCountry()),
                        memberCityLike(memberSearchCond.getCity()),
                        memberCreateDateBetween(memberSearchCond.getCreateDate()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(member.num.desc())
                .fetch();

        // 모든 회원 정보 count 가져오기 v1 using PageableExecutionUtils
//        JPAQuery<Long> countQuery = query
//                .select(member.count())
//                .from(member)
//                .where(
//                        memberIdLike(memberSearchCond.getId()),
//                        memberCountryLike(memberSearchCond.getCountry()),
//                        memberCityLike(memberSearchCond.getCity()),
//                        memberCreateDateBetween(memberSearchCond.getCreateDate())
//                );

        // 모든 회원 정보 count 가져오기 v2 using PageImpl
        Long totalCount = query
                .select(member.count())
                .from(member)
                .where(
                        memberIdLike(memberSearchCond.getId()),
                        memberCountryLike(memberSearchCond.getCountry()),
                        memberCityLike(memberSearchCond.getCity()),
                        memberCreateDateBetween(memberSearchCond.getCreateDate())
                )
                .fetchOne();

//        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne());
//        return PageableExecutionUtils.getPage(result, pageable, () -> countQuery.fetchOne());
        return new PageImpl(result, pageable, totalCount);
    }

    @Override
    public Slice<Member> findAllInfoUsingSlice(MemberSearchCond memberSearchCond, Pageable pageable) {

        // 모든 회원 정보 query 보내기
        List<Member> result = query
                .selectFrom(member)
                .where(memberIdLike(memberSearchCond.getId()),
                        memberCountryLike(memberSearchCond.getCountry()),
                        memberCityLike(memberSearchCond.getCity()),
                        memberCreateDateBetween(memberSearchCond.getCreateDate()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(member.num.desc())
                .fetch();

//        // 모든 회원 정보 count 가져오기 v1 using PageableExecutionUtils
//        JPAQuery<Long> countQuery = query
//                .select(member.count())
//                .from(member)
//                .where();

        // 모든 회원 정보 count 가져오기 v2 using PageImpl
        Long totalCount = query
                .select(member.count())
                .from(member)
                .where(
                        memberIdLike(memberSearchCond.getId()),
                        memberCountryLike(memberSearchCond.getCountry()),
                        memberCityLike(memberSearchCond.getCity()),
                        memberCreateDateBetween(memberSearchCond.getCreateDate())
                )
                .fetchOne();

//        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne());
//        return PageableExecutionUtils.getPage(result, pageable, () -> countQuery.fetchOne());
        return new PageImpl(result, pageable, totalCount);
    }

    /**
     * writer : 이호진
     * init : 2023.01.16
     * updated by writer :
     * update :
     * description : 회원 정보 검색 조건 between by createDate
     */
    private BooleanExpression memberCreateDateBetween(String createDate) {
        if(StringUtils.hasText(createDate)) {
            // String date를 LocalDate로 change
            LocalDateParser localDateParser = new LocalDateParser(createDate);

            return member.createdDate.between(localDateParser.startDate(), localDateParser.endDate());
        }
        return null;

    }

    /**
     * writer : 이호진
     * init : 2023.01.16
     * updated by writer :
     * update :
     * description : 회원 정보 검색 조건 like by city
     */
    private BooleanExpression memberCityLike(String city) {
        if(StringUtils.hasText(city)) {
            return member.address.city.like("%" + city + "%");
        }
        return null;
    }

    /**
     * writer : 이호진
     * init : 2023.01.16
     * updated by writer :
     * update :
     * description : 회원 정보 검색 조건 like by country
     */
    private BooleanExpression memberCountryLike(String country) {
        if(StringUtils.hasText(country)) {
            return member.address.country.like("%" + country + "%");
        }
        return null;
    }

    /**
     * writer : 이호진
     * init : 2023.01.16
     * updated by writer :
     * update :
     * description : 회원 정보 검색 조건 like by id
     */
    private BooleanExpression memberIdLike(String id) {
        if(StringUtils.hasText(id)) {
            return member.id.like("%" + id + "%");
        }
        return null;
    }
}
