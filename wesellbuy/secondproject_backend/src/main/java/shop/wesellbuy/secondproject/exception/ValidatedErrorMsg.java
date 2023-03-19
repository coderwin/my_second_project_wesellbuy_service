package shop.wesellbuy.secondproject.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 클라이언트로 예외 전달 방법 2
 * writer : 이호진
 * init : 2023.02.10
 * updated by writer :
 * update :
 * description : @Validated field error 정보 담기
 *                                (field, message)
 */
@Getter
@AllArgsConstructor
public class ValidatedErrorMsg {

    private String field; // error가 난 field
    private String errMsg; // 검증 오류 메시지

    // ** 생성 메서드** //
    public static ValidatedErrorMsg create(String field, String errMsg) {

        ValidatedErrorMsg result = new ValidatedErrorMsg(field, errMsg);
        return result;
    }
}
