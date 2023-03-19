package shop.wesellbuy.secondproject.web.reply;

import lombok.Getter;
import shop.wesellbuy.secondproject.domain.reply.ReplyRockStatus;
import shop.wesellbuy.secondproject.domain.reply.ReplyStatus;

/**
 * board 댓글 with rock dto
 * writer : 이호진
 * init : 2023.01.15
 * updated by writer :
 * update :
 * description : 클라이언트에게서 받은 댓글 내용/공개여부 정보를 담아둔다.
 */
@Getter
public class replyFormWithRock extends ReplyForm {

    private ReplyRockStatus rock; // 공개여부(TRUE/FALSE)
}
