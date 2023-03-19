package shop.wesellbuy.secondproject.web.item;

import lombok.Getter;
import shop.wesellbuy.secondproject.domain.item.ItemPicture;

import java.util.List;

/**
 * Item(Furniture) dto
 * writer : 이호진
 * init : 2023.01.14
 * updated by writer :
 * update :
 * description : 클라이언트에게서 받은 상품(Furniture) 정보를 담아둔다.
 */
@Getter
public class FurnitureForm extends ItemForm{
    private String company;

    public FurnitureForm(Integer stock, Integer price, String name, String content, List<ItemPicture> itemPictureList, String company) {
        super(stock, price, name, content, itemPictureList);
        this.company = company;
    }

    // ** 생성 메서드 ** //
    public static FurnitureForm create(ItemOriginalForm itemOriginalForm) {
        FurnitureForm form = new FurnitureForm(
                itemOriginalForm.getStock(),
                itemOriginalForm.getPrice(),
                itemOriginalForm.getName(),
                itemOriginalForm.getContent(),
                itemOriginalForm.getItemPictureList(),
                itemOriginalForm.getCompany()
        );

        return form;
    }
}
