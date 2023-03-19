package shop.wesellbuy.secondproject.web.item;

import lombok.Getter;
import shop.wesellbuy.secondproject.domain.item.ItemPicture;

import java.util.List;

/**
 * Item(HomeAppliances) dto
 * writer : 이호진
 * init : 2023.01.14
 * updated by writer :
 * update :
 * description : 클라이언트에게서 받은 상품(HomeAppliances) 정보를 담아둔다.
 */
@Getter
public class HomeAppliancesForm extends ItemForm{
    private String company;

    public HomeAppliancesForm(Integer stock, Integer price, String name, String content, List<ItemPicture> itemPictureList, String company) {
        super(stock, price, name, content, itemPictureList);
        this.company = company;
    }

    // ** 생성 메서드 ** //
    public static HomeAppliancesForm create(ItemOriginalForm itemOriginalForm) {
        HomeAppliancesForm form = new HomeAppliancesForm(
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
