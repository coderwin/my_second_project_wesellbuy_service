package shop.wesellbuy.secondproject.domain.item;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import shop.wesellbuy.secondproject.domain.Item;
import shop.wesellbuy.secondproject.domain.Member;
import shop.wesellbuy.secondproject.web.item.BookForm;
import shop.wesellbuy.secondproject.web.item.FurnitureForm;
import shop.wesellbuy.secondproject.web.item.ItemUpdateForm;

@Entity
@DiscriminatorValue("B")
@Getter
@Slf4j
public class Book extends Item {

    @Column(length = 200)
    private String author; // 저자
    @Column(length = 200)
    private String publisher; // 출판사

    // ** setter ** //

    public void addAuthor(String author) {
        this.author = author;
    }

    public void addPublisher(String publisher) {
        this.publisher = publisher;
    }

    // ** 생성 메서드 ** //
    // item controller 만들 때, 나중에 다시 생각
    public static Book createBook(BookForm bookForm, Member member) {
        Book book = new Book();

        book.addStock(bookForm.getStock());
        book.addPrice(bookForm.getPrice());
        book.addName(bookForm.getName());
        book.addContent(bookForm.getContent());
        book.addStatus(ItemStatus.R);
        book.addMember(member);
        book.addAuthor(bookForm.getAuthor());
        book.addPublisher(bookForm.getPublisher());
        // 각각의 itemPicture에 item 등록
        if(bookForm.getItemPictureList() != null) {
            log.info("ItemPicureList iterater 실행 중...");
//            bookForm.getItemPictureList().forEach((ip) -> ip.addItem(book));
            bookForm.getItemPictureList().forEach((ip) -> book.addItemPictures(ip));
        }
        return book;
    }

    // ** 비즈니스 로직(메서드) ** //

    /**
     * writer : 이호진
     * init : 2023.02.02
     * updated by writer :
     * update :
     * description : Book type 상품 정보 수정
     */
    public void update(ItemUpdateForm updateForm) {
        this.addName(updateForm.getName());
        this.addStock(updateForm.getStock());
        this.addPrice(updateForm.getPrice());
        this.addContent(updateForm.getContent());
        // 사진 추가하기
        // 연관관계 생각
        if(updateForm.getItemPictureList() != null) {
            updateForm.getItemPictureList()
                    .forEach(p -> this.addItemPictures(p));
        }
        // book
        this.addAuthor(updateForm.getAuthor());
        this.addPublisher(updateForm.getPublisher());
    }
}
