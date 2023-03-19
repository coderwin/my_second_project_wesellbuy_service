package shop.wesellbuy.secondproject.web.resultBox;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 클라이언트 응답 data V2
 * writer : 이호진
 * init : 2023.02.13
 * updated by writer :
 * update :
 * description : 클라이언트에게 보낼 데이터를 담아둔다.
 *               > data + boardNum
 */
@Getter
@AllArgsConstructor
public class ResultV2<T> {

    private final T data;
    private int boardNum; // 게시글 번호 보내주기
}
