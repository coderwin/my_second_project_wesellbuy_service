package shop.wesellbuy.secondproject.util;

import org.springframework.validation.BindingResult;

import java.util.regex.Pattern;

/**
 * 패턴 검사 도구
 * writer : 이호진
 * init : 2023.02.08
 * updated by writer :
 * update :
 * description : 클라이언트로부터 받은 데이터를 패턴을 통해 검증한다.
 *               -> 패턴과 맞지 않을 경우 BindingResult 객체에 패턴 에러 저장
 */
public class ValidationOfPattern {

    /**
     * writer : 이호진
     * init : 2023.02.08
     * updated by writer :
     * update :
     * description : 패턴을 검사한 후 맞지 않으면 bindingResult에 에러 저장
     */
    public static void validateValues(String pattern, String value, BindingResult bindingResult, String field, String errorCode, String defaultMessage) {
        // 패턴 검증 결과
        boolean result = Pattern.matches(pattern, value);
        //  pattern과 일치하지 않을 때
        if(!result) {
            bindingResult.rejectValue(field, errorCode, defaultMessage);
        }
    }



}
