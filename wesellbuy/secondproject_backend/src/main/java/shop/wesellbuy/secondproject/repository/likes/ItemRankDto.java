package shop.wesellbuy.secondproject.repository.likes;

import lombok.*;
import shop.wesellbuy.secondproject.domain.item.ItemStatus;

/**
 * ItemLikes findRank for dto
 * writer : 이호진
 * init : 2023.01.19
 * updated by writer :
 * update :
 * description : ItemLikes의 finaRankV2에 사용되는 dto
 */
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ItemRankDto {

    private Long count; // 좋아요 수
    private int itemNum; // 상품번호
    private int price; // 상품 가격
    private ItemStatus status; // 상품 상태
    private int stock; // 재고량
    private String memberId; // 회원 아이디
}
