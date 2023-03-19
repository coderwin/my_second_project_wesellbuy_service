package shop.wesellbuy.secondproject.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * 클라이언트로 예외 전달 방법 3
 * writer : 이호진
 * init : 2023.02.10
 * updated by writer :
 * update :
 * description : API 에러를 객체(json)으로 던져준다
 *               > @Validated filed error를 넘겨준다.
 */
@Getter
@AllArgsConstructor
@Slf4j
public class ValidatedErrorsMsg<T> {

    private T errors; // validated의 필드 에러 모음

    // ** 비즈니스 메서드 ** //
    /**
     * writer : 이호진
     * init : 2023.02.12
     * updated by writer :
     * update :
     * description : BindingResult에 의해 만들어진 error들을
     *               클라이언트에게 알려주기
     *
     * comment : bindingResult는 RestControllerAdvice에서 처리 못할까?
     */
    public static ResponseEntity<ValidatedErrorsMsg<List<ValidatedErrorMsg>>> makeValidatedErrorsContents(BindingResult bindingResult) {

        // bindingResult에 들어있는 에러 정보 담기
        List<ValidatedErrorMsg> errors = bindingResult.getFieldErrors().stream()
                .map(fe -> ValidatedErrorMsg.create(fe.getField(), fe.getDefaultMessage()))
                .collect(toList());
        // ValidatedErrorsMsg 만들기
        ValidatedErrorsMsg<List<ValidatedErrorMsg>> result = new ValidatedErrorsMsg<>(errors);
        // 클라이언트에게 보내주기
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

}
