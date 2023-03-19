package shop.wesellbuy.secondproject.domain.member;

/**
 * 회원 가입 현황 정보
 * writer : 이호진
 * init : 2023.01.14
 * updated by writer :
 * update :
 * description : 회원이 가입된 상태(JOIN)인지, 탈퇴한 상태(WITHDRAWAL)인지 알려준다.
 */
public enum MemberStatus {
    J("JOIN"),
    W("WITHDRAWAL");

    private final String memberStatus; // 회원 가입 현황

    MemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }

    public String getMemberStatus() {
        return this.memberStatus;
    }
}
