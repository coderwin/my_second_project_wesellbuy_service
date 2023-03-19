package shop.wesellbuy.secondproject.web.resultBox;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 클라이언트 응답 data
 * writer : 이호진
 * init : 2023.01.15
 * updated by writer : 이호진
 * update : 2023.02.13
 * description : 클라이언트에게 보낼 데이터를 담아둔다.
 *               > data 만
 *               > filed int boardNum 삭제
 */
@ToString
@Getter
@AllArgsConstructor
public class Result <T> {

    private final T data;
}
