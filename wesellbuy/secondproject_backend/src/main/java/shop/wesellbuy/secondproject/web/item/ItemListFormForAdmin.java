package shop.wesellbuy.secondproject.web.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import shop.wesellbuy.secondproject.domain.Item;
import shop.wesellbuy.secondproject.domain.common.PictureStatus;
import shop.wesellbuy.secondproject.domain.item.ItemPicture;
import shop.wesellbuy.secondproject.domain.item.ItemStatus;

/**
 * 추천합니다글 상세보기 dto
 * writer : 이호진
 * init : 2023.02.02
 * updated by writer : 이호진
 * update : 2023.02.14
 * description : 서버에서 받아온 상품 상세정보를 담아둔다. for admin
 *
 * update : ItemPicture -> ItemPictureForm 교체
 */
@Getter
@AllArgsConstructor
public class ItemListFormForAdmin {

    private Integer num; // 상품 번호
    private String name; // 상품명
    private Integer price; // 가격
    private Integer hits; // 조회수
    private ItemStatus status; // 등록 상태
    private Integer likes; // 좋아요수
    private ItemPictureForm pictureForm; // 이미지 한장만

    // 있어야 하는지 생각해 볼 것들
    private String memberId; // 상품 등록 회원(판매자) 아이디

    // ** 생성 메서드 ** //
    public static ItemListFormForAdmin create(Item item) {

        ItemListFormForAdmin form = new ItemListFormForAdmin(
                item.getNum(),
                item.getName(),
                item.getPrice(),
                item.getHits(),
                item.getStatus(),
                item.getItemLikesList().size(),
                item.getItemPictureList().stream()
                        .filter(p -> p.getStatus().equals(PictureStatus.R))
                        .findFirst()
                        .map(p -> ItemPictureForm.create(p))
                        .orElse(null),
                item.getMember().getId()
        );

        return form;
    }
}
