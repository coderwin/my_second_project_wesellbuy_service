package shop.wesellbuy.secondproject.web.controlleradvice;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shop.wesellbuy.secondproject.exception.ErrorResultMsg;
import shop.wesellbuy.secondproject.exception.ValidatedErrorMsg;
import shop.wesellbuy.secondproject.exception.ValidatedErrorsMsg;
import shop.wesellbuy.secondproject.exception.common.NotExistingIdException;
import shop.wesellbuy.secondproject.exception.delivery.NotCancelOrderException;
import shop.wesellbuy.secondproject.exception.item.NotExistingItemTypeException;
import shop.wesellbuy.secondproject.exception.item.OverflowQuantityException;
import shop.wesellbuy.secondproject.exception.member.ExistingIdException;
import shop.wesellbuy.secondproject.exception.member.login.NotExistingInfoException;
import shop.wesellbuy.secondproject.exception.member.login.WithdrawalMemberException;
import shop.wesellbuy.secondproject.exception.order.NotChangeDeliveryStatusException;
import shop.wesellbuy.secondproject.exception.order.NotCorrectPaidMoneyException;
import shop.wesellbuy.secondproject.exception.recommendation.NotExistingItemException;

import java.net.BindException;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * API 예외 처리 Controller
 * writer : 이호진
 * init : 2023.02.08
 * updated by writer :
 * update :
 * description : API 예외 처리 메서드 모음
 */
@RestControllerAdvice
@Slf4j
public class ApiExceptionController {

    ///////// common 예외 처리 시작
    /**
     * writer : 이호진
     * init : 2023.02.12
     * updated by writer :
     * update :
     * description : 고객지원글, 추천합니다글 작성 중 존재하지 않는 아이디이면 예외 발생
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotExistingIdException.class)
    public ErrorResultMsg NotExistingIdEx(NotExistingIdException e) {
        log.error("exception appears : ", e);
        // 에러 메시지 전달하기
        ErrorResultMsg result = ErrorResultMsg.create("bad request", e.getMessage());

        return result;
    }

    ///////// common 예외 처리 끝

    ///////// members 예외 처리 시작
    /**
     * writer : 이호진
     * init : 2023.02.08
     * updated by writer :
     * update :
     * description : 아이디 중복 예외 처리
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExistingIdException.class)
    public ErrorResultMsg ExistingIdExHandle(ExistingIdException e) {
        log.error("exception appears : ", e);
        // 에러 메시지 전달하기
        ErrorResultMsg result = ErrorResultMsg.create("bad request", e.getMessage());

        return result;
    }

    /**
     * writer : 이호진
     * init : 2023.02.10
     * updated by writer :
     * update :
     * description : @Validated 예외 처리 V1
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResultMsg ConstraintViolationEx(ConstraintViolationException e) {

        log.info("여기 왔다!!!!!");
        log.error("exception appears : ", e);

        ErrorResultMsg result = ErrorResultMsg.create("bad request", e.getMessage());

        return result;
    }

    /**
     * writer : 이호진
     * init : 2023.02.10
     * updated by writer :
     * update :
     * description : @Validated 예외 처리 V2
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidatedErrorsMsg<List<ValidatedErrorMsg>> MethodArgumentNotValidEx(MethodArgumentNotValidException e) {

        log.error("exception appears : ", e);

        /// validated 에러 모음 result 만들기
        // ValidatedErrorMsg List 만들기
        List<ValidatedErrorMsg> errors = e.getFieldErrors().stream()
                .map(fe -> ValidatedErrorMsg.create(fe.getField(), fe.getDefaultMessage()))
                .collect(toList());
        // ValidatedErrorsMsg<List<ValidatedErrorMsg>> 만들기
        ValidatedErrorsMsg<List<ValidatedErrorMsg>> result = new ValidatedErrorsMsg(errors);

        return result;
    }


    // login 예외 처리 시작
    /**
     * writer : 이호진
     * init : 2023.02.08
     * updated by writer :
     * update :
     * description : > 존재하지 않는 회원 예외 처리,
     *               > 회원 아이디, 비밀번호 찾기(입력 정보 일치 안 할 때, 휴대폰과 이메일 값이 없을 때) 예외 처리
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotExistingInfoException.class)
    public ErrorResultMsg NotExistingInfoEx(NotExistingInfoException e) {
        log.error("exception appears : ", e);

        // 에러 메시지 전달하기
        ErrorResultMsg result = ErrorResultMsg.create("bad Request", e.getMessage());

        return result;

    }

    /**
     * writer : 이호진
     * init : 2023.02.08
     * updated by writer :
     * update :
     * description : 탈퇴 회원 예외 처리
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WithdrawalMemberException.class)
    public ErrorResultMsg WithdrawalMemberEx(WithdrawalMemberException e) {
        log.error("exception appears : ", e);

        // 에러 메시지 전달하기
        ErrorResultMsg result = ErrorResultMsg.create("bad Request", e.getMessage());

        return result;

    }

    // login 예외 처리 끝


    /////////// members 예외 처리 끝

    ///////// recommendations 예외 처리 시작
    /**
     * writer : 이호진
     * init : 2023.02.12
     * updated by writer :
     * update :
     * description : 추천합니다 게시글 저장 예외 처리
     *               > 상품 & 판매자가 존재하지 않을 때
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotExistingItemException.class)
    public ErrorResultMsg NotExistingItemEx(NotExistingItemException e) {
        log.error("exception appers : ", e);

        // 에러 메시지 전달하기
        ErrorResultMsg result = new ErrorResultMsg("bad request", e.getMessage());

        return result;
    }

    ///////// recommendations 예외 처리 끝


    ///////// Item 예외 처리 시작

    /**
     * writer : 이호진
     * init : 2023.02.13
     * updated by writer :
     * update :
     * description : 상품 저장 예외 처리
     *               > 상품 종류가 없을 때
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotExistingItemTypeException.class)
    public ErrorResultMsg NotExistingItemTypeEx(NotExistingItemTypeException e) {
        log.error("exception appears : ", e);

        // 에러 메시지 전달하기
        ErrorResultMsg result = new ErrorResultMsg("bad request", e.getMessage());

        return result;
    }

    ///////// Item 예외 처리 끝

    ///////// Order 예외 처리 끝

    /**
     * writer : 이호진
     * init : 2023.02.14
     * updated by writer :
     * update :
     * description : 주문 저장 예외 처리
     *               > 상품 제고가 주문 수량보다 적을 때
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OverflowQuantityException.class)
    public ErrorResultMsg OverflowQuantityEx(OverflowQuantityException e) {
        log.error("exception appears : ", e);

        // 에러 메시지 전달하기
        ErrorResultMsg result = new ErrorResultMsg("bad request", e.getMessage());

        return result;
    }

    /**
     * writer : 이호진
     * init : 2023.02.14
     * updated by writer :
     * update :
     * description : 주문 저장 예외 처리
     *               > 지불금액이 결제금액과 일치하지 않을 때
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotCorrectPaidMoneyException.class)
    public ErrorResultMsg NotCorrectPaidMoneyEx(NotCorrectPaidMoneyException e) {
        log.error("exception appears : ", e);

        // 에러 메시지 전달하기
        ErrorResultMsg result = new ErrorResultMsg("bad request", e.getMessage());

        return result;
    }

    /**
     * writer : 이호진
     * init : 2023.02.14
     * updated by writer :
     * update :
     * description : 주문 취소 예외 처리
     *               > 주문 취소가 안 될 때
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotCancelOrderException.class)
    public ErrorResultMsg NotCancelOrderEx(NotCancelOrderException e) {
        log.error("exception appears : ", e);

        // 에러 메시지 전달하기
        ErrorResultMsg result = new ErrorResultMsg("bad request", e.getMessage());

        return result;
    }

    // seller(판매자) 예외 처리 시작

    /**
     * writer : 이호진
     * init : 2023.02.14
     * updated by writer :
     * update :
     * description : 주문 배송상태 변경 예외 처리
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotChangeDeliveryStatusException.class)
    public ErrorResultMsg NotChangeDeliveryStatusEx(NotChangeDeliveryStatusException e) {
        log.error("exception appears : ", e);

        // 에러 메시지 전달하기
        ErrorResultMsg result = new ErrorResultMsg("bad request", e.getMessage());

        return result;
    }

    // seller(판매자) 예외 처리 끝

    ///////// Order 예외 처리 끝


}
