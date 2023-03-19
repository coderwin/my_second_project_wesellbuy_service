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
import shop.wesellbuy.secondproject.repository.order.OrderSearchCond;
import shop.wesellbuy.secondproject.repository.orderitem.OrderItemSearchCond;
import shop.wesellbuy.secondproject.service.order.OrderService;
import shop.wesellbuy.secondproject.util.SessionConst;
import shop.wesellbuy.secondproject.web.member.login.LoginMemberSessionForm;
import shop.wesellbuy.secondproject.web.order.*;
import shop.wesellbuy.secondproject.web.orderitem.OrderItemListForm;
import shop.wesellbuy.secondproject.web.resultBox.Result;
import shop.wesellbuy.secondproject.web.resultBox.ResultForOrder;


/**
 * Order Contoller
 * writer : 이호진
 * init : 2023.02.14
 * updated by writer :
 * update :
 * description : Order RestController 메소드 모음
 */
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    /**
     * writer : 이호진
     * init : 2023.02.14
     * updated by writer : 이호진
     * update :
     * description : 주문 저장 V1
     *
     * comment : controller에서 list를 받을 때
     */
    @PostMapping
    @ApiOperation(value = "주문 등록")
    public ResponseEntity<ResultForOrder<String>> save(@RequestBody @Validated OrderDataForm form,
                                                       @SessionAttribute(SessionConst.LOGIN_MEMBER)LoginMemberSessionForm sessionForm) {
        log.info("form : {}", form.getData());
        log.info("paidMoney : {}", form.getPaidMoney());
        /// 검증 통과
        // 주문 등록
        int orderNum = orderService.save(form.getData(), sessionForm.getNum(), form.getPaidMoney());
        // code 201 보내기
        String successMsg = "주문 완료";
        ResultForOrder<String> body = new ResultForOrder<>(successMsg, orderNum);

        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    /**
     * writer : 이호진
     * init : 2023.02.14
     * updated by writer :
     * update :
     * description : 주문 취소
     *
     * comment : 주문 취소면 -> 배송 상태도 '배송종료'[O(OVER)]로 바꾸는 것 생각해보기
     */
    @DeleteMapping("/{num}")
    @ApiOperation(value = "주문 취소")
    public ResponseEntity<Result<String>> cancel(@PathVariable int num) {
        // 주문 취소
        orderService.cancel(num);
        // responseEntity body 생성
        log.info("주문 취소 성공 -> order num : {}", num);
        String successMsg = "취소 완료";
        Result<String> body = new Result<>(successMsg);

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    /**
     * writer : 이호진
     * init : 2023.02.14
     * updated by writer :
     * update :
     * description : 주문 상세보기
     */
    @GetMapping("/{num}")
    @ApiOperation(value = "주문 상세보기")
    public Result<OrderDetailForm> watchDetail(@PathVariable int num) {
        // 주문 상세보기 불러오기
        OrderDetailForm form = orderService.watchDetail(num);
        // Result 생성
        Result<OrderDetailForm> result = new Result<>(form);

        return result;
    }

    /**
     * writer : 이호진
     * init : 2023.02.14
     * updated by writer :
     * update :
     * description : 회원이 주문한 주문 모두 불러오기 + by 회원(주문자)아이디
     *               > 검색 데이터 createDate(주문 날짜), orderStatus(주문 상태 -> O, C), deliveryStatus(배달 상태 -> R, T, C)를 받고
     *               > 페이징 데이터 size, page를 받는다.
     */
    @GetMapping
    @ApiOperation("주문 목록 회원용")
    public Result<Page<OrderListForm>> selectList(OrderSearchCond cond,
                                                  Pageable pageable,
                                                  @SessionAttribute(SessionConst.LOGIN_MEMBER) LoginMemberSessionForm sessionForm) {
        log.info("OrderSearchCond cond : {}", cond);
        // 주문 목록 불러오기
        Page<OrderListForm> pageForm = orderService.selectList(cond, pageable, sessionForm.getId());
        // Result 생성하기
        Result<Page<OrderListForm>> result = new Result<>(pageForm);

        return result;
    }

    //    -------------------------methods using for user(seller) start----------------------------------

    /**
     * writer : 이호진
     * init : 2023.02.12
     * updated by writer :
     * update :
     * description : 판매자가 주문의 배달상태를 배송중으로 변경
     *               배달 상태 변경
     *               -> delivery status : R(READY) -> T(TRANSIT)
     */
    @PatchMapping("/{num}/delivery/seller")
    @ApiOperation("주문 배송상태 변경 판매자용")
    public ResponseEntity<Result<String>> changeDeliveryStatusForSeller(@PathVariable int num) {
        // 주문 배송상태 변경하기
        orderService.changeDeliveryStatusForSeller(num);
        // 변경 완료
        String successMsg = "배송상태 변경 완료";
        // responseEntity body 생성
        Result<String> body = new Result(successMsg);

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    /**
     * writer : 이호진
     * init : 2023.02.04
     * updated by writer :
     * update :
     * description : 판매자가 판매된 상품(목록) 확인하기
     *               > 검색 데이터 orderId(주문자 아이디), createDate(만든 날짜), orderStatus(주문 상태 -> O, C), deliveryStatus(배달 상태 -> R, T, C)를 받고
     *               > 페이징 데이터 size, page를 받는다.
     */
    @GetMapping("/seller")
    @ApiOperation(value = "주문 목록 판매자용")
    public Result<Page<OrderItemListForm>> selectOrderItemList(OrderItemSearchCond cond,
                                                               Pageable pageable,
                                                               @SessionAttribute(SessionConst.LOGIN_MEMBER) LoginMemberSessionForm sessionForm) {
        log.info("cond : {}", cond);
        // 판매된 상품 불러오기
        Page<OrderItemListForm> pageForm = orderService.selectOrderItemList(cond, pageable, sessionForm.getNum());
        // Result 생성하기
        Result<Page<OrderItemListForm>> result = new Result<>(pageForm);

        return result;

    }

    //    -------------------------methods using for user(seller) end ----------------------------------

    //    -------------------------methods using for deliver start----------------------------------

    /**
     * writer : 이호진
     * init : 2023.02.14
     * updated by writer :
     * update :
     * description : 배달원이 주문의 배달상태를 배송중으로 변경
     *               배달 상태 변경
     *               -> delivery status : T(TRANSIT) -> C(COMPLETE)
     *
     *               changeDeliveryStatusForSeller 코드는 같은데
     *               log가 다르다.(controller에서 처리)
     *               하나로 합쳐도 되지 않을까?
     */
    @PatchMapping("/{num}/delivery/deliver")
    @ApiOperation("주문 배송상태 변경 배달원용")
    public ResponseEntity<Result<String>> changeDeliveryStatusForDeliver(@PathVariable int num) {
        // 주문 배송상태 변경하기
        orderService.changeDeliveryStatusForDeliver(num);
        // 변경 완료
        String successMsg = "배송상태 변경 완료";
        // responseEntity body 생성
        Result<String> body = new Result(successMsg);

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    /**
     * writer : 이호진
     * init : 2023.03.14
     * updated by writer :
     * update :
     * description : 회원이 주문한 주문 모두 불러오기 + by 회원(주문자)아이디
     *               > 검색 데이터 memberId(주문 회원 아이디), createDate(주문 날짜), orderStatus(주문 상태 -> O, C), deliveryStatus(배달 상태 -> R, T, C)를 받고
     *               > 페이징 데이터 size, page를 받는다.
     *               -> for deliver
     */
    @GetMapping("/deliver")
    @ApiOperation(value = "주문 목록 배달원용")
    public Result<Page<OrderListFormForAdmin>> selectListForDeliver(OrderSearchCond cond, Pageable pageable) {
        // 주문 목록 불러오기
        Page<OrderListFormForAdmin> pageForm = orderService.selectListForAdmin(cond, pageable);
        // Result 생성하기
        Result<Page<OrderListFormForAdmin>> result = new Result(pageForm);

        return result;
    }

    //    -------------------------methods using for deliver end ----------------------------------

    //    -------------------------methods using for admin, deliver start----------------------------------

    /**
     * writer : 이호진
     * init : 2023.02.04
     * updated by writer :
     * update :
     * description : 회원이 주문한 주문 모두 불러오기 + by 회원(주문자)아이디
     *               > 검색 데이터 memberId(주문 회원 아이디), createDate(주문 날짜), orderStatus(주문 상태 -> O, C), deliveryStatus(배달 상태 -> R, T, C)를 받고
     *               > 페이징 데이터 size, page를 받는다.
     *               -> for admin
     *
     * update : -> ApiOperation value에 배달원용 삭제
     */
    @GetMapping("/admin")
    @ApiOperation(value = "주문 목록 관리자용")
    public Result<Page<OrderListFormForAdmin>> selectListForAdmin(OrderSearchCond cond, Pageable pageable) {
        // 주문 목록 불러오기
        Page<OrderListFormForAdmin> pageForm = orderService.selectListForAdmin(cond, pageable);
        // Result 생성하기
        Result<Page<OrderListFormForAdmin>> result = new Result(pageForm);

        return result;
    }

    //    -------------------------methods using for admin, deliver end ----------------------------------








}
