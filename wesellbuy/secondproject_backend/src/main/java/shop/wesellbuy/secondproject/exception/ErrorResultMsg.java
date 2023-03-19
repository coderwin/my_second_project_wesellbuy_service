package shop.wesellbuy.secondproject.exception;

import lombok.*;

/**
 * 클라이언트로 예외 전달 방법1
 * writer : 이호진
 * init : 2023.01.20
 * updated by writer :
 * update :
 * description : API 에러를 객체(json)으로 던져준다
 */
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResultMsg {

    private String code;// 에러 코드
    private String errMsg; // 에러 메시지

    // ** 생성 메서드** //
    public static ErrorResultMsg create(String code, String errMsg) {

        ErrorResultMsg errorResultMsg = new ErrorResultMsg(code, errMsg);
        return errorResultMsg;
    }

}
