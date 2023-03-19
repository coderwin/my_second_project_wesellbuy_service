package shop.wesellbuy.secondproject.web.controller;


import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shop.wesellbuy.secondproject.repository.customerservice.CustomerServiceSearchCond;
import shop.wesellbuy.secondproject.service.customerservice.CustomerServiceService;
import shop.wesellbuy.secondproject.util.SessionConst;
import shop.wesellbuy.secondproject.web.customerservice.CustomerServiceDetailForm;
import shop.wesellbuy.secondproject.web.customerservice.CustomerServiceForm;
import shop.wesellbuy.secondproject.web.customerservice.CustomerServiceListForm;
import shop.wesellbuy.secondproject.web.member.login.LoginMemberSessionForm;
import shop.wesellbuy.secondproject.web.resultBox.Result;
import shop.wesellbuy.secondproject.web.resultBox.ResultV2;

/**
 * CustomerService Contoller
 * writer : 이호진
 * init : 2023.02.12
 * updated by writer :
 * update :
 * description : CustomerService RestController 메소드 모음
 */
@RestController
@RequestMapping("/customerservices")
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceController {

    private final CustomerServiceService customerServiceService;

    /**
     * writer : 이호진
     * init : 2023.02.12
     * updated by writer : 이호진
     * update :
     * description : 고객지원글 저장
     */
    @PostMapping
    @ApiOperation(value = "고객지원 게시글 등록")
    public ResponseEntity<?> save(@RequestBody @Validated CustomerServiceForm form,
                                  @SessionAttribute(name = SessionConst.LOGIN_MEMBER) LoginMemberSessionForm sessionForm
                                  ) {
        log.info("customerServiceForm form : {}", form);
        // 회원의 number 가져오기 in Session
        int memberNum = sessionForm.getNum();
        log.info("save memberNum : {}", memberNum);

        // 저장
        int boardNum = customerServiceService.save(memberNum, form);
        // 저장 성공
        String successMsg = "등록 완료";
        // ResultV2 생성
        ResultV2<String> body = new ResultV2<>(successMsg, boardNum);

        return new ResponseEntity<>(body ,HttpStatus.OK);
    }

    /**
     * writer : 이호진
     * init : 2023.02.12
     * updated by writer : 이호진
     * update :
     * description : 고객지원글 상세보기
     */
    @GetMapping("/{num}")
    @ApiOperation(value = "고객지원 게시글 상세보기")
    public Result<CustomerServiceDetailForm> watchDetail(@PathVariable int num) {
        // 게시글 상세보기 불러오기
        CustomerServiceDetailForm form = customerServiceService.watchDetail(num);
        // Result 생성
        Result<CustomerServiceDetailForm> result = new Result<>(form);

        return result;
    }

    /**
     * writer : 이호진
     * init : 2023.02.12
     * updated by writer :
     * update :
     * description : 회원의 고객지원글 가져오기
     *               > 검색 데이터 reportedId, createDate를 받고
     *               > 페이징 데이터 size, page를 받는다.
     */
    @GetMapping
    @ApiOperation("회원의 고객지원글 목록")
    public Result<Page<CustomerServiceListForm>> selectList(CustomerServiceSearchCond customerServiceSearchCond,
                                                            Pageable pageable,
                                                            @SessionAttribute(SessionConst.LOGIN_MEMBER) LoginMemberSessionForm sessionForm) {
        // 회원 아이디 가져오기
        String id = sessionForm.getId();
        // 조건에 id 담아주기
        customerServiceSearchCond.addMemberId(id);
        log.info("customerServiceSearchCond : {}", customerServiceSearchCond);
        // 게시글 가져오기
        Page<CustomerServiceListForm> pageForm = customerServiceService.selectList(customerServiceSearchCond, pageable);
        // Result 생성하기
        Result<Page<CustomerServiceListForm>> result = new Result<>(pageForm);

        return result;
    }


    //   ------------------------------methods using for admin start --------------------------------
    /**
     * writer : 이호진
     * init : 2023.02.12
     * updated by writer :
     * update :
     * description : 모든 고객지원글 가져오기 for admin
     *               > 검색 데이터 memberId, reportedId, createDate를 받고
     *               > 페이징 데이터 size, page를 받는다.
     */
    @GetMapping("/admin")
    @ApiOperation("모든 고객지원글 목록")
    public Result<Page<CustomerServiceListForm>> selectListForAdmin(CustomerServiceSearchCond customerServiceSearchCond, Pageable pageable) {
        // 게시글 가져오기
        Page<CustomerServiceListForm> pageForm = customerServiceService.selectList(customerServiceSearchCond, pageable);
        // Result 생성하기
        Result<Page<CustomerServiceListForm>> result = new Result<>(pageForm);

        return result;
    }

    //   ------------------------------methods using for admin end --------------------------------













}
