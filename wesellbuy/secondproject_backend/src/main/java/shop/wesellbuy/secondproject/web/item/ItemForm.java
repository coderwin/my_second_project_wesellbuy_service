package shop.wesellbuy.secondproject.web.item;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.*;
import shop.wesellbuy.secondproject.domain.item.ItemPicture;

import java.util.ArrayList;
import java.util.List;

/**
 * 상품 등록 dto
 * writer : 이호진
 * init : 2023.01.14
 * updated by writer :
 * update :
 * description : 클라이언트에게서 받은 상품 정보를 담아둔다.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemForm {

//    private Integer num; // 상품 번호
    private Integer stock; // 제고 수량
    private Integer price; // 가격
    private String name; // 상품명
    private String content; // 설명

    // item controller 만들 때, 나중에 다시 생각
    private List<ItemPicture> itemPictureList; // 상품 이미지 모음

    // ** 생성 메서드 ** //
    public static ItemForm create(ItemOriginalForm itemOriginalForm) {
        ItemForm form = new ItemForm(
                itemOriginalForm.getStock(),
                itemOriginalForm.getPrice(),
                itemOriginalForm.getName(),
                itemOriginalForm.getContent(),
                itemOriginalForm.getItemPictureList()
        );

        return form;
    }
}
