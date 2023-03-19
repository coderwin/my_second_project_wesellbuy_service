package shop.wesellbuy.secondproject.service.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.wesellbuy.secondproject.repository.member.MemberSearchCond;
import shop.wesellbuy.secondproject.repository.member.MemberSearchIdCond;
import shop.wesellbuy.secondproject.repository.member.MemberSearchPwdCond;
import shop.wesellbuy.secondproject.web.member.MemberDetailForm;
import shop.wesellbuy.secondproject.web.member.MemberOriginForm;
import shop.wesellbuy.secondproject.web.member.MemberUpdateForm;
import shop.wesellbuy.secondproject.web.member.login.LoginMemberForm;
import shop.wesellbuy.secondproject.web.member.login.LoginMemberSessionForm;
import shop.wesellbuy.secondproject.web.member.login.LoginSearchIdResultForm;
import shop.wesellbuy.secondproject.web.member.login.LoginSearchPwdResultForm;

import java.io.IOException;

/**
 * Member Service
 * writer : 이호진
 * init : 2023.01.26
 * updated by writer :
 * update :
 * description : Member Service 메소드 모음
 */
public interface MemberService {

    /**
     * writer : 이호진
     * init : 2023.01.26
     * updated by writer :
     * update :
     * description : 회원 가입
     *
     * @return
     */
    int join(MemberOriginForm memberOriginFormForm) throws IOException;

    /**
     * writer : 이호진
     * init : 2023.01.26
     * updated by writer :
     * update :
     * description : 회원 정보 수정
     */
    void update(MemberUpdateForm memberUpdateForm, Integer num) throws IOException;

    /**
     * writer : 이호진
     * init : 2023.01.26
     * updated by writer :
     * update :
     * description : 회원 탈퇴
     *               -> status 변경
     */
    void withdrawal(int num);

    /**
     * writer : 이호진
     * init : 2023.01.26
     * updated by writer :
     * update :
     * description : 회원 정보 상세보기
     */
    MemberDetailForm watchDetail(int num);

//    ------------------------------methods using for admin start --------------------------------

    /**
     * writer : 이호진
     * init : 2023.01.26
     * updated by writer :
     * update :
     * description : 모든 회원 정보 가져오기 + 검색
     */
    Page<MemberDetailForm> selectList(MemberSearchCond memberSearchCond, Pageable pageable);

    //    ------------------------------methods using for admin end --------------------------------


    //   ------------------------------ methods using at login start  --------------------------------

    /**
     * writer : 이호진
     * init : 2023.01.27
     * updated by writer :
     * update :
     * description : 로그인 처리를 한다.
     *               -> 아이디, 비밀번호 맞으면 session 생성
     *               -> 아이디 기억 클릭했으면 cookie 생성
     *
     * comment : Login controller에서
     *           -> session 생성 하자(24시간)
     *           -> Cookie도 생성 하자(24시간)
     *              -> '아이디기억' 클릭했으면 cookie에 따로 id 정보 저장
     */
    LoginMemberSessionForm login(LoginMemberForm loginMemberForm);

    /**
     * writer : 이호진
     * init : 2023.01.27
     * updated by writer :
     * update :
     * description : 회원 아이디 찾기
     */
    LoginSearchIdResultForm searchForIds(MemberSearchIdCond memberSearchIdCond);

    /**
     * writer : 이호진
     * init : 2023.01.27
     * updated by writer :
     * update :
     * description : 회원 비밀번호 찾기
     */
    public LoginSearchPwdResultForm searchForPwd(MemberSearchPwdCond memberSearchPwdCond);


    //   ------------------------------ methods using at login end  --------------------------------


    //   ------------------------------methods using at join start --------------------------------


    /**
     * writer : 이호진
     * init : 2023.01.27
     * updated by writer :
     * update :
     * description : 회원 가입 중, 아이디 중복 확인
     */
    String confirmDuplicatedId(String id);

    //   ------------------------------methods using at join end --------------------------------

}
