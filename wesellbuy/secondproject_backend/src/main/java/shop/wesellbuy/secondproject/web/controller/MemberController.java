package shop.wesellbuy.secondproject.web.controller;

import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import shop.wesellbuy.secondproject.exception.ValidatedErrorsMsg;
import shop.wesellbuy.secondproject.repository.member.MemberSearchCond;
import shop.wesellbuy.secondproject.repository.member.MemberSearchIdCond;
import shop.wesellbuy.secondproject.repository.member.MemberSearchPwdCond;
import shop.wesellbuy.secondproject.service.member.FileStoreOfSelfPicture;
import shop.wesellbuy.secondproject.service.member.MemberService;
import shop.wesellbuy.secondproject.util.CookieManager;
import shop.wesellbuy.secondproject.util.SessionConst;
import shop.wesellbuy.secondproject.web.member.MemberDetailForm;
import shop.wesellbuy.secondproject.web.member.MemberOriginForm;
import shop.wesellbuy.secondproject.web.member.MemberUpdateForm;
import shop.wesellbuy.secondproject.web.member.login.LoginMemberForm;
import shop.wesellbuy.secondproject.web.member.login.LoginMemberSessionForm;
import shop.wesellbuy.secondproject.web.member.login.LoginSearchIdResultForm;
import shop.wesellbuy.secondproject.web.member.login.LoginSearchPwdResultForm;
import shop.wesellbuy.secondproject.web.resultBox.Result;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Member Contoller
 * writer : 이호진
 * init : 2023.02.08
 * updated by writer :
 * update :
 * description : Member RestController 메소드 모음
 */
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    
    private final MemberService memberService;
    private final FileStoreOfSelfPicture fileStoreOfSelfPicture;

    /**
     * writer : 이호진
     * init : 2023.02.08
     * updated by writer : 이호진
     * update : 2023.02.10
     * description : 회원 가입
     *
     * update : 파일과 json 데이터 함께 받아오기
     *          > 검증 처리 추가
     */
    @PostMapping
    @ApiOperation(value = "회원 정보 등록")
    public ResponseEntity<?> join(@RequestPart("data") @Validated MemberOriginForm memberOriginForm,
                                               @RequestPart(name = "file", required = false) MultipartFile file,
                                               BindingResult bindingResult) throws IOException {

        log.info("file : {}", file);
        log.info("memberOriginForm : {}", memberOriginForm);

        // 파일 dto에 넣기
        memberOriginForm.addFile(file);
        // 데이터 검증하기(bindingResult)
        memberOriginForm.validateJoinValues(bindingResult);
        // bindResult에 에러 있는지 확인
        if(bindingResult.hasErrors()) {
            log.info("member join error : {}", bindingResult);
            // BindingResult에 의해 만들어진 error들을 클라이언트에게 알려주기
            return ValidatedErrorsMsg.makeValidatedErrorsContents(bindingResult);
        }
        /// 검증 통과
        // 회원 등록
        memberService.join(memberOriginForm);
        // code 201 보내기
        String successMsg = "회원가입 성공";
        Result<String> body = new Result(successMsg);

        return new ResponseEntity(body, HttpStatus.CREATED);
    }

//    /**
//     * writer : 이호진
//     * init : 2023.02.10
//     * updated by writer :
//     * update :
//     * description : BindingResult에 의해 만들어진 error들을
//     *               클라이언트에게 알려주기
//     *
//     * comment : bindingResult는 RestControllerAdvice에서 처리 못할까?
//     *           >  ValidatedErrorsMsg class에 새로 생성
//     */
//    private ResponseEntity<ValidatedErrorsMsg<List<ValidatedErrorMsg>>> makeValidatedErrorsContents(BindingResult bindingResult) {
//        // bindingResult에 들어있는 에러 정보 담기
//        List<ValidatedErrorMsg> errors = bindingResult.getFieldErrors().stream()
//                .map(fe -> ValidatedErrorMsg.create(fe.getField(), fe.getDefaultMessage()))
//                .collect(toList());
//        // ValidatatedErrorsMsg 만들기
//        ValidatedErrorsMsg<List<ValidatedErrorMsg>> result = new ValidatedErrorsMsg<>(errors);
//        // 클라이언트에게 보내주기
//        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
//    }

    /**
     * writer : 이호진
     * init : 2023.02.08
     * updated by writer : 이호진
     * update : 2023.03.06
     * description : 회원 정보 수정
     *
     * update : > @RequestBody를 @RequestPart로 수정
     *          > file 파라미터를 추가
     *          > ValidatedErrorsMsg.makeValidatedErrorsContents 추가
     *          > requestPart의 value를 name으로 수정
     */
    @PutMapping("/{num}")
    @ApiOperation(value = "회원 정보 수정")
    public ResponseEntity<?> update(
            @PathVariable Integer num,
            @RequestPart("data") @Validated MemberUpdateForm memberUpdateForm,
            @RequestPart(name = "file", required = false) MultipartFile file,
            BindingResult bindingResult) throws IOException {
        // file을 dto에 넣기
        memberUpdateForm.addFile(file);
        // 데이터 검증
        memberUpdateForm.validateUpdateValues(bindingResult);
        // bindingResult에 에러있는지 확인하기
        if(bindingResult.hasErrors()) {
            log.info("member update error : {}", bindingResult);

            return ValidatedErrorsMsg.makeValidatedErrorsContents(bindingResult);
        }
        /// 검증 통과
        // 수정하기
        memberService.update(memberUpdateForm, num);
        // 수정 완료
        String successMsg = "수정 완료";
        // responseEntity body 생성
        Result<String> body = new Result(successMsg);
        return new ResponseEntity(body, HttpStatus.OK);
    }

    /**
     * writer : 이호진
     * init : 2023.02.08
     * updated by writer : 이호진
     * update : 2023.03.06
     * description : 회원 탈퇴
     *
     * update : @PathVariable 추가
     */
    @DeleteMapping("/{num}")
    @ApiOperation("회원 탈퇴")
    public ResponseEntity<Result<String>> withdrawal(@PathVariable int num, HttpServletRequest request) {
        // 회원 탈퇴
        memberService.withdrawal(num);
        // 로그아웃 하기(session의 데이터 지우기)
        turnOffLogin(request);
        // responseEntity body 생성
        log.info("회원탈퇴 성공 -> member num : {}", num);
        String successMsg = "회원 탈퇴 완료";
        Result<String> body = new Result<>(successMsg);

        return new ResponseEntity(body, HttpStatus.OK);
    }

    /**
     * writer : 이호진
     * init : 2023.02.08
     * updated by writer :
     * update :
     * description : session의 데이터 지우기
     *               > 로그아웃 하기
     */
    private void turnOffLogin(HttpServletRequest request) {
        // session 삭제하기
        HttpSession session = request.getSession(false);
        if(session != null) {
            // session에 있는 값들 clear
            session.invalidate();
        }
    }

    /**
     * writer : 이호진
     * init : 2023.02.08
     * updated by writer :
     * update :
     * description : 회원 정보 상세보기
     *               > num으로 회원정보를 불러오면 쉽게 정보가 유출되지 않을까
     */
    @GetMapping("/{num}")
    @ApiOperation("회원 정보 상세보기")
    public Result<MemberDetailForm> watchDetail(@PathVariable int num) {
        // 회원 상세보기 불러오기
        MemberDetailForm form = memberService.watchDetail(num);
        // Result 생성
        Result<MemberDetailForm> result = new Result<>(form);

        return result;
    }

    /**
     * writer : 이호진
     * init : 2023.02.09
     * updated by writer :
     * update :
     * description : 회원 이미지 파일 불러오기
     */
    @GetMapping("/images/{savedFileName}")
    @ApiOperation("회원 이미지 파일 불러오기")
    public Resource showImage(@PathVariable String savedFileName) throws MalformedURLException {
        log.info("showImage -> savedFileName : {}", savedFileName);
        // 파일이 저장되어있는 root 불러오기
        return new UrlResource("file:" + fileStoreOfSelfPicture.getFullPath(savedFileName));
    }

    //   ------------------------------methods using for admin start --------------------------------

    /**
     * writer : 이호진
     * init : 2023.02.08
     * updated by writer :
     * update :
     * description : 모든 회원 정보 가져오기 + 검색
     *
     * comment : 쿼리스트링으로 넘어오는
     *           > 검색 데이터 id, country, city, createDate를 받고
     *           > 페이징 데이터 size, page를 받는다.
     */
    @GetMapping
    @ApiOperation("회원 목록")
    public Result<Page<MemberDetailForm>> selectList(MemberSearchCond cond, Pageable pageable) {
        // 검색하기
        Page<MemberDetailForm> pageForm = memberService.selectList(cond, pageable);
        // Result 객체 생성하기
        Result<Page<MemberDetailForm>> result = new Result(pageForm);

        return result;
    }

    //   ------------------------------methods using for admin end --------------------------------


    //   ------------------------------ methods using at login start  --------------------------------

    /**
     * writer : 이호진
     * init : 2023.02.08
     * updated by writer : 이호진
     * update : 2023.03.04
     * description : 로그인 처리하기
     *               > id, pwd, rememberId(Boolean) 데이터 받기
     *
     * update : > form.getRememberId() != false 삭제
     *          > GetMapping에서 PostMapping으로 변경
     *          > sessionForm 데이터 view로 전달
     */
    @PostMapping("/login")
    @ApiOperation("로그인 처리")
    public ResponseEntity login(@RequestBody LoginMemberForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        log.info("LoginMemberForm : {}", form);
        // 로그인 처리하기
        LoginMemberSessionForm sessionForm = memberService.login(form);
        /// 로그인 성공
        // session 불러오기
        HttpSession session = request.getSession(true);
        // session에 loginForm 정보 저장
        session.setAttribute(SessionConst.LOGIN_MEMBER, sessionForm);

        /// 아이디 기억 체크
        if(form.getRememberId() == true) {
            // 쿠키 생성
            int maxAge = 86400; // 쿠키 24시간 유지
            CookieManager.makeCookie(CookieManager.REMEMBER_ID, form.getId(), response, maxAge);

        // 아이디 기억 없으면
        } else {
            // 쿠키 만료
            CookieManager.expireCookie(CookieManager.REMEMBER_ID, response);
        }
        // sessionForm 데이터를 보내주기
        // responseEntity body 생성
        Result<LoginMemberSessionForm> body = new Result(sessionForm);

        return new ResponseEntity(body, HttpStatus.OK);
    }

    /**
     * writer : 이호진
     * init : 2023.02.08
     * updated by writer : 이호진
     * update : 2023.03.03
     * description : 로그아웃 처리
     *
     * commnent : logout은 서버에서 처리해야할까?
     *           > 프론트쪽에서 처리하면 안 되나?
     *
     * update : GetMapping에서 PostMapping으로 변경
     */
    @PostMapping("/logout")
    @ApiOperation("로그아웃 처리")
    public ResponseEntity<Result<String>> logout(HttpServletRequest request) {
        // sesssion 삭제하기
        // 로그아웃 하기(session의 데이터 지우기)
        turnOffLogin(request);
        // responseEntity body 생성
        String successMsg = "로그아웃 완료";
        Result<String> body = new Result(successMsg);

        return new ResponseEntity(body, HttpStatus.OK);
    }

    /**
     * writer : 이호진
     * init : 2023.02.08
     * updated by writer : 이호진
     * update : 2023.03.05
     * description : 회원 아이디 찾기
     *
     * update : @RequestBody 삭제
     */
    @GetMapping("/find/id")
    @ApiOperation("회원 아이디 찾기")
    public Result<LoginSearchIdResultForm> searchForIds(MemberSearchIdCond memberSearchIdCond) {
        log.info("memberSearchIdCond : {}", memberSearchIdCond);
        // 아이디(들) 찾기
        LoginSearchIdResultForm form = memberService.searchForIds(memberSearchIdCond);
        // 아이디 찾기 성공
        return new Result<>(form);
    }

    /**
     * writer : 이호진
     * init : 2023.02.08
     * updated by writer : 이호진
     * update : 2023.03.05
     * description : 회원 비밀번호 찾기
     *
     * update : @RequestBody 삭제
     */
    @GetMapping("/find/pwd")
    @ApiOperation("회원 비밀번호 찾기")
    public Result<LoginSearchPwdResultForm> searchForIds(MemberSearchPwdCond memberSearchPwdCond) {
        // 아이디(들) 찾기
        LoginSearchPwdResultForm form = memberService.searchForPwd(memberSearchPwdCond);
        // 아이디 찾기 성공
        return new Result<>(form);
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
    @GetMapping("/id/check")
    @ApiOperation("아이디 중복 확인")
    public ResponseEntity<Result<String>> confirmDuplicatedId(@RequestParam String id) {
        // 아이디 중복 확인
        String successMsg = memberService.confirmDuplicatedId(id);
        /// 확인 성공
        // ResponseEntity body 생성
        Result<String> body = new Result<>(successMsg);

        return ResponseEntity.status(HttpStatus.OK)
                .body(body);
    }

    //   ------------------------------methods using at join end --------------------------------



}
