package shop.wesellbuy.secondproject.domain.common;

/**
 * 이미지 파일의 상태 정보
 * writer : 이호진
 * init : 2023.02.01
 * updated by writer :
 * update :
 * description : 이미지 파일의 상태(등록/삭제 상태) 정의한다.
 */
public enum PictureStatus {

    R("REGISTER"),
    D("DELETE");

    private final String pictureStatus; // picture의 상태

    PictureStatus(String pictureStatus) {
        this.pictureStatus = pictureStatus;
    }

    // ** getter ** //
    public String getPictureStatus() {
        return this.pictureStatus;
    }
}
