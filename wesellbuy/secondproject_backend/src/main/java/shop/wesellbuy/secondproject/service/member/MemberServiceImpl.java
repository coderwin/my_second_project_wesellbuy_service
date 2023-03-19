package shop.wesellbuy.secondproject.service.member;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import shop.wesellbuy.secondproject.domain.Member;
import shop.wesellbuy.secondproject.domain.member.MemberStatus;
import shop.wesellbuy.secondproject.domain.member.SelfPicture;
import shop.wesellbuy.secondproject.exception.member.ExistingIdException;
import shop.wesellbuy.secondproject.exception.member.login.NotExistingInfoException;
import shop.wesellbuy.secondproject.exception.member.login.WithdrawalMemberException;
import shop.wesellbuy.secondproject.repository.member.MemberJpaRepository;
import shop.wesellbuy.secondproject.repository.member.MemberSearchCond;
import shop.wesellbuy.secondproject.repository.member.MemberSearchIdCond;
import shop.wesellbuy.secondproject.repository.member.MemberSearchPwdCond;
import shop.wesellbuy.secondproject.web.member.MemberDetailForm;
import shop.wesellbuy.secondproject.web.member.MemberForm;
import shop.wesellbuy.secondproject.web.member.MemberOriginForm;
import shop.wesellbuy.secondproject.web.member.MemberUpdateForm;
import shop.wesellbuy.secondproject.web.member.login.LoginMemberForm;
import shop.wesellbuy.secondproject.web.member.login.LoginMemberSessionForm;
import shop.wesellbuy.secondproject.web.member.login.LoginSearchIdResultForm;
import shop.wesellbuy.secondproject.web.member.login.LoginSearchPwdResultForm;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * Member Service 구현 클래스
 * writer : 이호진
 * init : 2023.01.26
 * updated by writer :
 * update :
 * description : Member Service 구현 메소드 모음
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Setter
public class MemberServiceImpl implements MemberService {

    private final MemberJpaRepository memberJpaRepository;
    private final FileStoreOfSelfPicture fileStoreOfSelfPicture;

    /**
     * writer : 이호진
     * init : 2023.01.26
     * updated by writer :
     * update :
     * description : 회원 가입
     */
    @Override
    @Transactional
    public int join(MemberOriginForm memberOriginForm) throws IOException {

        // 아이디 중복 확인
        checkId(memberOriginForm.getId());

        // selfPicture 생성
        SelfPicture selfPicture = fileStoreOfSelfPicture.storeFile(memberOriginForm.getFile());

        // MemberForm 생성
        MemberForm memberForm = memberOriginForm.changeAsMemberForm(selfPicture);

        // member 생성
        Member member = Member.createMember(memberForm);

        // 회원 정보 저장
        return memberJpaRepository.save(member).getNum();
    }

    /**
     * writer : 이호진
     * init : 2023.01.26
     * updated by writer :
     * update :
     * description : 아이디가 존재하는지 확인한다.
     */
    private void checkId(String id) {
        // 아이디로 member 조회하기
        if(!memberJpaRepository.findByMemberId(id).isEmpty()) {
            String errMsg = "이미 사용중인 아이디";
            throw new ExistingIdException(errMsg);
        };
    }

    /**
     * writer : 이호진
     * init : 2023.01.26
     * updated by writer :
     * update :
     * description : 회원 정보 수정
     */
    @Override
    @Transactional
    public void update(MemberUpdateForm memberUpdateForm, Integer num) throws IOException {
        // 파일 저장하기
        SelfPicture selfPicture = fileStoreOfSelfPicture.storeFile(memberUpdateForm.getFile());

        // 회원 찾기
        Member findMember = memberJpaRepository.findById(num).orElseThrow();

        // 회원 정보 수정하기
        findMember.updateMember(memberUpdateForm, selfPicture);
    }

    /**
     * writer : 이호진
     * init : 2023.01.26
     * updated by writer :
     * update :
     * description : 회원 탈퇴
     *               -> status 변경
     */
    @Override
    @Transactional
    public void withdrawal(int num) {
        // 회원 찾기
        Member findMember = memberJpaRepository.findById(num).orElseThrow();

        // 회원 상태 변경
        findMember.withdrawMember(findMember);
    }

    /**
     * writer : 이호진
     * init : 2023.01.26
     * updated by writer :
     * update :
     * description : 회원 정보 상세보기
     */
    @Override
    public MemberDetailForm watchDetail(int num) {
        // 회원 정보 가져오기
        Member findMember = memberJpaRepository.findDetailInfoById(num).orElseThrow();
        // MemberDetailForm으로 변경하기
        return MemberDetailForm.createMemberDetailForm(findMember);
    }

//   ------------------------------methods using for admin start --------------------------------

    /**
     * writer : 이호진
     * init : 2023.01.26
     * updated by writer :
     * update :
     * description : 모든 회원 정보 가져오기 + 검색
     */
    @Override
    public Page<MemberDetailForm> selectList(MemberSearchCond memberSearchCond, Pageable pageable) {
        // member List 가져오기
        Page<Member> members = memberJpaRepository.findAllInfo(memberSearchCond, pageable);
        // memberDetailForm으로 변경하기
        Page<MemberDetailForm> memberDetailFormList = members.map(m -> MemberDetailForm.createMemberDetailForm(m));

        return memberDetailFormList;
    }

    //   ------------------------------methods using for admin end --------------------------------

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
    @Override
    public LoginMemberSessionForm login(LoginMemberForm loginMemberForm) {
        // 아이디 일치여부 확인
        Optional<Member> findMember = checkLoginId(loginMemberForm.getId());
        // 비밀번호 일치여부 확인
        Member member = checkLoginPwd(loginMemberForm, findMember);

        // 일치하지 않으면 NotExistionInfoException 발생
        if(member == null) {
            // error 메시지 생성
            String errMsg = "아이디 또는 비밀번호를 잘못 입력하셨습니다.";
            throw new NotExistingInfoException(errMsg);
        }

        // 탈퇴 회원인지 확인하기
        if(member.getStatus().equals(MemberStatus.W)) {
            // 탈퇴회원 에러 말해주기
            String errMsg = "탈퇴한 회원입니다.";
            throw new WithdrawalMemberException(errMsg);
        }
        // 일치하면 세션에 회원 정보를 담을 세션Form 생성
        LoginMemberSessionForm loginMemberSessionForm = LoginMemberSessionForm.createLoginMemberSessionForm(member.getNum(), member.getId(), member.getName());
        return loginMemberSessionForm;
    }

    /**
     * writer : 이호진
     * init : 2023.01.27
     * updated by writer :
     * update :
     * description : 로그인 중 비밀번호 일치여부 확인
     */
    private Member checkLoginPwd(LoginMemberForm loginMemberForm, Optional<Member> findMember) {
        Member member = findMember.filter(m -> m.getPwd().equals(loginMemberForm.getPwd()))
                .orElse(null);
        return member;
    }

    /**
     * writer : 이호진
     * init : 2023.01.27
     * updated by writer :
     * update :
     * description : 로그인 중 아이디 일치여부 확인
     */
    private Optional<Member> checkLoginId(String id) {
        Optional<Member> findMember = memberJpaRepository.findAll()
                .stream()
                .filter(m -> m.getId().equals(id))
                .findFirst();

        return findMember;
    }


    /**
     * writer : 이호진
     * init : 2023.01.27
     * updated by writer :
     * update :
     * description : 회원 아이디 찾기
     */
    @Override
    public LoginSearchIdResultForm searchForIds(MemberSearchIdCond memberSearchIdCond) {
        // 휴대폰과 이메일 값이 없는 경우 확인
        checkEmailAndSelfPhone(memberSearchIdCond.getEmail(), memberSearchIdCond.getSelfPhone());

        // 아이디 찾기
        List<Member> result = memberJpaRepository.findByNameAndSelfPhoneAndEmail(memberSearchIdCond);

        // 아이디가 없는 경우
        if(result.size() == 0) {
            String errMsg = "입력하신 정보가 정확하지 않습니다.";
            throw new NotExistingInfoException(errMsg);
        }
        // 아이디 있는 경우
        // 아이디 모음 만들기
        List<String> ids = result.stream()
                .map(m -> m.getId())
                .collect(toList());
        // 아이디의 일정 정보를 숨기기
        List<String> resultIds = hideRealId(ids);

        // 결과 form에 담기
        LoginSearchIdResultForm loginSearchIdResultForm = new LoginSearchIdResultForm(memberSearchIdCond.getName(), resultIds);

        return loginSearchIdResultForm;
    }

    /**
     * writer : 이호진
     * init : 2023.01.27
     * updated by writer :
     * update :
     * description : 휴대폰과 이메일 값이 없는 경우 확인
     *               -> 아이디 찾기 & 비밀번호 찾기에서 사용
     */
    private void checkEmailAndSelfPhone(String email, String selfPhone) {
        // 휴대폰과 이메일 값이 없는 경우
        if(!StringUtils.hasText(email) & !StringUtils.hasText(selfPhone)) {
            String errMsg = "입력하신 정보가 정확하지 않습니다.";
            throw new NotExistingInfoException(errMsg);
        }
    }

    /**
     * writer : 이호진
     * init : 2023.01.27
     * updated by writer :
     * update :
     * description : id의 일정 정보를 숨긴다.
     *               -> 아이디의 글자수에 따라 뒤에 *표시
     *                  -> 1~3: *, 4~6: **, 7~9: ***, 그외: ****
     */
    private List<String> hideRealId(List<String> ids) {

         List<String> result = ids.stream().map(id -> {
            StringBuffer sb = new StringBuffer();
            sb.append(id);

            // 변수 정하기
            int start = 0;// 치환 시작 site
            int end = 0;// 치환 끝 앞 site
            String printId = "";// 치환된 id

            // 아이디가 1~3 length 일 때 - 1나만 *
            if (id.length() <= 3) {
                start = id.length() - 1;
                end = id.length() + 1;

                printId = sb.replace(start, end, "*").toString();
                // 아이디가 4~6 length 일 때 - 2나만 **
            } else if (id.length() <= 6) {
                start = id.length() - 2;
                end = id.length() + 1;

                printId = sb.replace(start, end, "**").toString();
                // 아이디가 7~9 length 일 때 - 3나만 **
            } else if (id.length() <= 9) {
                start = id.length() - 3;
                end = id.length() + 1;

                printId = sb.replace(start, end, "***").toString();
                // 그외 - 4나만 ***
            } else {
                start = id.length() - 4;
                end = id.length() + 1;

                printId = sb.replace(start, end, "****").toString();
            }
            // 치환된 id 반환
            return printId;
        })
        .collect(toList());

        // 결과 보내기
        return result;
    }

    /**
     * writer : 이호진
     * init : 2023.01.27
     * updated by writer :
     * update :
     * description : 회원 비밀번호 찾기
     */
    @Override
    public LoginSearchPwdResultForm searchForPwd(MemberSearchPwdCond memberSearchPwdCond) {
        // 휴대폰과 이메일 값이 없는 경우 확인
        checkEmailAndSelfPhone(memberSearchPwdCond.getEmail(), memberSearchPwdCond.getSelfPhone());
        // 비밀번호 찾기
        List<Member> result = memberJpaRepository.findByIdAndSelfPhoneAndEmail(memberSearchPwdCond);

        // 비밀번호 없는 경우
        if(result.size() == 0) {
            String errMsg = "입력하신 정보가 정확하지 않습니다.";
            throw new NotExistingInfoException(errMsg);
        }
        // 비밀번호 있는 경우
        // 비밀번호 찾기
        Member findMember = result.get(0);
        // 비밀번호 결과 숨기기
        String resultPwd = hideRealPwd(findMember.getPwd());
        // 결과 form에 담기
        LoginSearchPwdResultForm loginSearchPwdResultForm = new LoginSearchPwdResultForm(memberSearchPwdCond.getId(), resultPwd);

        return loginSearchPwdResultForm;
    }

    /**
     * writer : 이호진
     * init : 2023.01.27
     * updated by writer :
     * update :
     * description : 비밀번호의 일정 정보를 숨긴다.
     *               -> 뒤에 4자리를 ****로 표시
     */
    private String hideRealPwd(String pwd) {
        // 아이디 뒤에 4자리는 ****로 표시
        StringBuffer sb = new StringBuffer();

        sb.append(pwd);
        int start = pwd.length() - 4;// 치환 시작점
        int end = pwd.length() + 1;// 치환 끝나는 점
        // ****로 치환하기
        String resultPwd = sb.replace(start, end, "****").toString();

        return resultPwd;
    }


    //   ------------------------------ methods using at login end  --------------------------------

    //   ------------------------------methods using at join start --------------------------------

    /**
     * writer : 이호진
     * init : 2023.01.27
     * updated by writer :
     * update :
     * description : 회원 가입 중, 아이디 중복 확인
     */
    @Override
    public String confirmDuplicatedId(String id) {
        // 중복 확인
        checkId(id);
        // 아니면 사용 가능 msg 보내기
        String successMsg = "사용가능 아이디";
        return successMsg;
    }

    //   ------------------------------methods using at join end --------------------------------



}
