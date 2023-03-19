package shop.wesellbuy.secondproject.domain.board;

/**
 * 게시판(board)의 상태 정보
 * writer : 이호진
 * init : 2023.01.15
 * updated by writer :
 * update :
 * description : 게시판의 상태(등록/삭제 상태) 정의한다.
 */
public enum BoardStatus {
    R("REGISTER"),
    D("DELETE");

    private final String boardStatus; // board의 상태

    BoardStatus(String boardStatus) {
        this.boardStatus = boardStatus;
    }

    // ** getter ** //
    public String getBoardStatus() {
        return this.boardStatus;
    }
}
