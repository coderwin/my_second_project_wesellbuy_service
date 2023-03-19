package shop.wesellbuy.secondproject.exception.member.login;

/**
 * 사용자 정의 예외
 * writer : 이호진
 * init : 2023.01.27
 * updated by writer :
 * update :
 * description : 로그인 중 탈퇴 회원인 경우
 */
public class WithdrawalMemberException extends RuntimeException {

    public WithdrawalMemberException() {
        super();
    }

    public WithdrawalMemberException(String message) {
        super(message);
    }

    public WithdrawalMemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public WithdrawalMemberException(Throwable cause) {
        super(cause);
    }

    protected WithdrawalMemberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
