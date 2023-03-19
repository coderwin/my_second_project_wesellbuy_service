package shop.wesellbuy.secondproject.web.item;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.wesellbuy.secondproject.domain.item.ItemPicture;

import java.util.List;

/**
 * Item(Book) dto
 * writer : 이호진
 * init : 2023.01.14
 * updated by writer :
 * update :
 * description : 클라이언트에게서 받은 상품(Book) 정보를 담아둔다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookForm extends ItemForm{

    private String author; // 저자
    private String publisher; // 출판사

    public BookForm(Integer stock, Integer price, String name, String content, List<ItemPicture> itemPictureList, String author, String publisher) {
        super(stock, price, name, content, itemPictureList);
        this.author = author;
        this.publisher = publisher;
    }

    // ** 생성 메서드 ** //
    public static BookForm create(ItemOriginalForm itemOriginalForm) {
        BookForm form = new BookForm(
                itemOriginalForm.getStock(),
                itemOriginalForm.getPrice(),
                itemOriginalForm.getName(),
                itemOriginalForm.getContent(),
                itemOriginalForm.getItemPictureList(),
                itemOriginalForm.getAuthor(),
                itemOriginalForm.getPublisher()
        );

        return form;
    }

}
