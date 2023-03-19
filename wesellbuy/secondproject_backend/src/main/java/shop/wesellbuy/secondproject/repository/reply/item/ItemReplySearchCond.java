package shop.wesellbuy.secondproject.repository.reply.item;

import lombok.*;

/**
 * ItemReply findAll for condition dto
 * writer : 이호진
 * init : 2023.01.19
 * updated by writer : 이호진
 * update : 2023.02.13
 * description : ItemReply finaAll에 사용되는 where 절의 조건 데이터 모음
 *
 * update : @NoArgsConstructor 추가
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ItemReplySearchCond {

    private String memberId; // 작성자 id
    private String content; // 작성 내용
    private String createDate; // 작성날짜
}
