package shop.wesellbuy.secondproject.domain.reply;

/**
 * 게시판 댓글(reply)의 상태 정보
 * writer : 이호진
 * init : 2023.01.15
 * updated by writer :
 * update :
 * description : 게시판의 상태(등록/삭제 상태) 정의한다.
 */
public enum ReplyStatus {
    R("REGISTER"),
    D("DELETE");

    private final String replyStatus; // board의 상태

    ReplyStatus(String replyStatus) {
        this.replyStatus = replyStatus;
    }

    // ** getter ** //
    public String getReplyStatus() {
        return this.replyStatus;
    }
}
