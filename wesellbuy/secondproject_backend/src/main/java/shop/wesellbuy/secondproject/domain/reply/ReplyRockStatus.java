package shop.wesellbuy.secondproject.domain.reply;

public enum ReplyRockStatus {

    T("TRUE"),
    F("FALSE");

    private final String replyRockStatus; // 댓글 공개 여부

    ReplyRockStatus(String replyRockStatus) {
        this.replyRockStatus = replyRockStatus;
    }

    public String getReplyRockStatus() {
        return this.replyRockStatus;
    }
}
