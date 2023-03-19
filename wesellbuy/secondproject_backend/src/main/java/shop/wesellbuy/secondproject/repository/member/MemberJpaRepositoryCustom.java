package shop.wesellbuy.secondproject.repository.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import shop.wesellbuy.secondproject.domain.Member;

import java.util.List;
import java.util.Optional;

/**
 * Member Repository by using queryDsl
 * writer : 이호진
 * init : 2023.01.16
 * updated by writer :
 * update :
 * description : querydsl이용한 memberRepository 모음
 */
public interface MemberJpaRepositoryCustom {

    /**
     * writer : 이호진
     * init : 2023.01.26
     * updated by writer :
     * update :
     * description : 아이디 중복인지 확인한다.
     */
    public Optional<Member> findByMemberId(String id);

    //    -----------------------------아이디 찾기/비밀번호 찾기 시작--------------------------------------------
    /**
     * writer : 이호진
     * init : 2023.01.17
     * updated by writer :
     * update :
     * description : 모든 회원 정보 찾기 by 이름 and email
     */
    public List<Member> findByNameAndSelfPhoneAndEmail(MemberSearchIdCond memberSearchIdCond);

    /**
     * writer : 이호진
     * init : 2023.01.17
     * updated by writer :
     * update :
     * description : 모든 회원 정보 찾기 by id and email
     */
    public List<Member> findByIdAndSelfPhoneAndEmail(MemberSearchPwdCond memberSearchPwdCond);

    //    -----------------------------아이디 찾기/비밀번호 찾기 끝--------------------------------------------

    /**
     * writer : 이호진
     * init : 2023.01.17
     * updated by writer :
     * update :
     * description : 회원 상세정보 찾기
     */
    public Optional<Member> findDetailInfoById(int num);

    /**
     * writer : 이호진
     * init : 2023.01.16
     * updated by writer :
     * update :
     * description : 전체 회원정보 찾기 + paging
     *              - admin에서 사용
     */
    public Page<Member> findAllInfo(MemberSearchCond memberSearchCond, Pageable pageable);

    /**
     * writer : 이호진
     * init : 2023.01.16
     * updated by writer :
     * update :
     * description : 전체 회원정보 찾기 + paging
     *              - admin에서 사용
     *              - Slice 객체 반환
     */
    public Slice<Member> findAllInfoUsingSlice(MemberSearchCond memberSearchCond, Pageable pageable);
}
