package shop.wesellbuy.secondproject.domain.item;

/**
 * 상품 상태 정보
 * writer : 이호진
 * init : 2023.01.14
 * updated by writer :
 * update :
 * description : 상품 등록 상태(등록(REGISTER), 삭제(DELTE))알려준다.
 */
public enum ItemStatus {

    R("REGISTER"),
    D("DELETE");

    private final String itemStatus; // board의 상태

    ItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    // ** getter ** //
    public String getItemStatus() {
        return this.itemStatus;
    }
}
