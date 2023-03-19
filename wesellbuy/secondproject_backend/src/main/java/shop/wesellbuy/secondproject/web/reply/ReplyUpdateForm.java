package shop.wesellbuy.secondproject.web.reply;

import lombok.*;

/**
 * board 댓글 수정 dto
 * writer : 이호진
 * init : 2023.02.01
 * updated by writer : 이호진
 * update : 2023.02.12
 * description : 클라이언트에게서 받은 댓글 수정 내용 정보를 담아둔다.
 *
 * update : @NoArgsConstructor 추가
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ReplyUpdateForm {

    private int num; // 댓글 번호
    private String content; // 댓글 수정 내용

    // ** 비즈니스 메서드 ** //
    public void addNum(int num) {
        this.num = num;
    }
}
