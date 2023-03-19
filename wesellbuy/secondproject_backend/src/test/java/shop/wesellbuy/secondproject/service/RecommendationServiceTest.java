package shop.wesellbuy.secondproject.service;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import shop.wesellbuy.secondproject.domain.Item;
import shop.wesellbuy.secondproject.domain.Member;
import shop.wesellbuy.secondproject.domain.Recommendation;
import shop.wesellbuy.secondproject.domain.board.BoardStatus;
import shop.wesellbuy.secondproject.domain.item.Book;
import shop.wesellbuy.secondproject.domain.item.ItemPicture;
import shop.wesellbuy.secondproject.domain.member.SelfPicture;
import shop.wesellbuy.secondproject.domain.recommendation.RecommendationPicture;
import shop.wesellbuy.secondproject.domain.reply.RecommendationReply;
import shop.wesellbuy.secondproject.exception.recommendation.NotExistingItemException;
import shop.wesellbuy.secondproject.repository.item.ItemJpaRepository;
import shop.wesellbuy.secondproject.repository.member.MemberJpaRepository;
import shop.wesellbuy.secondproject.repository.recommendation.RecommendationJpaRepository;
import shop.wesellbuy.secondproject.repository.recommendation.RecommendationSearchCond;
import shop.wesellbuy.secondproject.repository.reply.recommendation.RecommendationReplyJpaRepository;
import shop.wesellbuy.secondproject.service.recommendation.RecommendationService;
import shop.wesellbuy.secondproject.web.item.BookForm;
import shop.wesellbuy.secondproject.web.member.MemberForm;
import shop.wesellbuy.secondproject.web.member.MemberOriginForm;
import shop.wesellbuy.secondproject.web.recommendation.*;
import shop.wesellbuy.secondproject.web.reply.ReplyForm;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@Slf4j
public class RecommendationServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    RecommendationService recommendationService;
    @Autowired
    RecommendationJpaRepository recommendationJpaRepository;
    @Autowired
    MemberJpaRepository memberJpaRepository;
    @Autowired
    ItemJpaRepository itemJpaRepository;
    @Autowired
    RecommendationReplyJpaRepository replyRepository;

    // 필요한 객체들
    Member member; // test 회원
    Member member2; // test 회원
    Member member3; // test 회원
    Item item; // test 상품
    Item item2; // test 상품
    Item item3; // test 상품
    List<MultipartFile> files; // test 이미지 파일 모음


    @BeforeEach
    public void init() throws IOException {

        // member 생성
        SelfPicture selfPicture = SelfPicture.createSelfPicture("test1", "test2");
        SelfPicture selfPicture3 = SelfPicture.createSelfPicture("test1", "test2");
        MemberForm memberForm1 = new MemberForm("a", "a1", "123", "a", "a@a", "01012341234", "0511231234", "korea", "b", "h", "h", selfPicture);
        Member member = Member.createMember(memberForm1);
        MemberForm memberForm2 = new MemberForm("a", "b1", "123", "a", "a@a", "01012341234", "0511231234", "korea", "b", "h", "h", null);
        Member member2 = Member.createMember(memberForm2);
        MemberForm memberForm3 = new MemberForm("a", "c1", "123", "a", "a@a", "01012341234", "0511231234", "korea", "b", "h", "h", selfPicture3);
        Member member3 = Member.createMember(memberForm3);

        memberJpaRepository.save(member);
        memberJpaRepository.save(member2);
        memberJpaRepository.save(member3);
        this.member = member;
        this.member2 = member2;
        this.member3 = member3;

        // 상품 생성
        List<ItemPicture> itemPictureList = new ArrayList<>();
        itemPictureList.add(ItemPicture.createItemPicture("a", "a"));
        itemPictureList.add(ItemPicture.createItemPicture("a1", "a2"));

        BookForm bookForm = new BookForm(10, 1000, "book1", "x is...", itemPictureList, "ed", "ok");
        BookForm bookForm2 = new BookForm(10, 1000, "book123", "x is...", itemPictureList, "ed", "ok");
        BookForm bookForm3 = new BookForm(10, 1000, "book2", "x is...", itemPictureList, "ed", "ok");
        Item item = Book.createBook(bookForm, member);
        Item item2 = Book.createBook(bookForm2, member);
        Item item3 = Book.createBook(bookForm3, member2);

        itemJpaRepository.save(item);
        itemJpaRepository.save(item2);
        itemJpaRepository.save(item3);
        this.item = item;
        this.item2 = item2;
        this.item3 = item3;

        // 추천합니다글 생성
        RecommendationForm recommendationForm = new RecommendationForm("book1", "a1", "재미있어요", null);

        // 이미지 저장
        // MockMultipartFile 생성
        String fileName = "book"; // 파일명
        String contentType = "jpg"; // 파일 확장자
        String originFileName = fileName + "." + contentType;
        String filePath = "src/test/resources/testImages/" + fileName + "." + contentType;
        FileInputStream fileInputStream = new FileInputStream(filePath);

        MockMultipartFile file = new MockMultipartFile("book_image", originFileName, contentType, fileInputStream);

        String fileName2 = "book2"; // 파일명
        String contentType2 = "jpg"; // 파일 확장자
        String originFileName2 = fileName + "." + contentType;
        String filePath2 = "src/test/resources/testImages/" + fileName + "." + contentType;
        FileInputStream fileInputStream2 = new FileInputStream(filePath);

        MockMultipartFile file2 = new MockMultipartFile("book_image2", originFileName2, contentType2, fileInputStream2);

        // 이미지 모음
        List<MultipartFile> files = new ArrayList<>();
        files.add(file);
        files.add(file2);

        this.files = files;

    }

    /**
     * 추천합니다글 저장 확인
     */
    @Test
//    @Rollback(value = false)
    public void 저장_확인() throws IOException {
        // given
        // member 생성
        SelfPicture selfPicture = SelfPicture.createSelfPicture("test1", "test2");
        MemberForm memberForm1 = new MemberForm("a", "ab1", "123", "a", "a@a", "01012341234", "0511231234", "korea", "b", "h", "h", selfPicture);
        Member member = Member.createMember(memberForm1);

        memberJpaRepository.save(member);

        // 상품 생성
        List<ItemPicture> itemPictureList = new ArrayList<>();
        itemPictureList.add(ItemPicture.createItemPicture("a", "a"));
        itemPictureList.add(ItemPicture.createItemPicture("a1", "a2"));

        BookForm bookForm = new BookForm(10, 1000, "book1", "x is...", itemPictureList, "ed", "ok");
        Item item = Book.createBook(bookForm, member);

        itemJpaRepository.save(item);

        // 추천합니다글 생성
        RecommendationForm recommendationForm = new RecommendationForm("book1", "ab1", "재미있어요", null);
        // 이미지 저장
        // MockMultipartFile 생성
        String fileName = "book"; // 파일명
        String contentType = "jpg"; // 파일 확장자
        String originFileName = fileName + "." + contentType;
        String filePath = "src/test/resources/testImages/" + fileName + "." + contentType;
        FileInputStream fileInputStream = new FileInputStream(filePath);

        MockMultipartFile file = new MockMultipartFile("book_image", originFileName, contentType, fileInputStream);

        String fileName2 = "book2"; // 파일명
        String contentType2 = "jpg"; // 파일 확장자
        String originFileName2 = fileName + "." + contentType;
        String filePath2 = "src/test/resources/testImages/" + fileName2 + "." + contentType2;
        FileInputStream fileInputStream2 = new FileInputStream(filePath2);

        MockMultipartFile file2 = new MockMultipartFile("book_image2", originFileName2, contentType2, fileInputStream2);

        // 이미지 모음
        List<MultipartFile> files = new ArrayList<>();
        files.add(file);
        files.add(file2);

        // 추천합니다글 생성하기
        int savedNum = recommendationService.save(recommendationForm, files, member.getNum());

        // when
        Recommendation findRecommendation = recommendationJpaRepository.findById(savedNum).orElseThrow();

        // then
        assertThat(findRecommendation.getNum()).isEqualTo(savedNum);
        assertThat(findRecommendation.getContent()).isEqualTo(recommendationForm.getContent());
        assertThat(findRecommendation.getItemName()).isEqualTo(recommendationForm.getItemName());
        assertThat(findRecommendation.getSellerId()).isEqualTo(recommendationForm.getSellerId());
        assertThat(findRecommendation.getHits()).isEqualTo(0);
        assertThat(findRecommendation.getMember()).isEqualTo(member);
        assertThat(findRecommendation.getRecommendationPictureList().size()).isEqualTo(2);
    }

    /**
     * 추천합니다글 저장 확인
     * -> 상품 또는 판매자 이름 잘못 되었을 때
     */
    @Test
//    @Rollback(value = false)
    public void 저장_예외_발생_확인() {
        // given
        // 추천합니다글 생성
        RecommendationForm failedItemNameForm = new RecommendationForm("book1234", "a1", "재미있어요", null);
        RecommendationForm failedSellerIdForm = new RecommendationForm("book1", "a123", "재미있어요", null);

        // when // then
        assertThrows(NotExistingItemException.class, () -> recommendationService.save(failedItemNameForm, files, member.getNum()));
        assertThrows(NotExistingItemException.class, () -> recommendationService.save(failedSellerIdForm, files, member.getNum()));
    }

    /**
     * 추천합니다글 상세보기 확인
     *
     * comment : db 저장시 소수 6째자리까지만 저장되어 flush 할 때, 에러 발생
     */
    @Test
//    @Rollback(value = false)
    public void 상세보기_확인() {
        // given
        // 추천합니다글 생성
        List<RecommendationPicture> rpList = new ArrayList<>();
        rpList.add(RecommendationPicture.createRecommendationPicture("x", "x"));
        rpList.add(RecommendationPicture.createRecommendationPicture("y1", "y2"));
        RecommendationForm recommendationForm = new RecommendationForm("book1", "a1", "ok", rpList);

        Recommendation recommendation = Recommendation.createRecommendation(recommendationForm, member);
        // 저장
        recommendationJpaRepository.save(recommendation);

        // 댓글 생성
        ReplyForm replyForm1 = new ReplyForm("멋져요~");
        ReplyForm replyForm2 = new ReplyForm("멋져요2~");
        ReplyForm replyForm3 = new ReplyForm("멋져요3~");
        RecommendationReply recommendationReply1 = RecommendationReply.createRecommendationReply(replyForm1, member2, recommendation);
        RecommendationReply recommendationReply2 = RecommendationReply.createRecommendationReply(replyForm2, member3, recommendation);
        RecommendationReply recommendationReply3 = RecommendationReply.createRecommendationReply(replyForm3, member2, recommendation);
        // 저장
        replyRepository.save(recommendationReply1);
        replyRepository.save(recommendationReply2);
        replyRepository.save(recommendationReply3);

        // betch size 동작 확인
        // 확인 완료
//        em.flush();
//        em.clear();

        // when
        // 상세보기
        RecommendationDetailForm detailForm = recommendationService.watchDetail(recommendation.getNum());

        // then
        assertThat(detailForm.getNum()).isEqualTo(recommendation.getNum());
        assertThat(detailForm.getContent()).isEqualTo(recommendation.getContent());
        assertThat(detailForm.getItemName()).isEqualTo(recommendation.getItemName());
        assertThat(detailForm.getSellerId()).isEqualTo(recommendation.getSellerId());
//        assertThat(detailForm.getHits()).isEqualTo(0);// 에러 발생
        assertThat(detailForm.getHits()).isEqualTo(1);
//        assertThat(detailForm.getCreateDate()).isEqualTo(recommendation.getCreatedDate());// db 저장시 소수 6째자리까지만 저장된다?
        // 저장된 오리지널 이미지 파일 이름
        assertThat(detailForm.getRecommendationPictureFormList().stream()
                .map(p -> p.getOriginalFileName())
                .collect(toList()))
                .containsExactly("x", "y1");
        // 댓글 작성자 확인
        assertThat(detailForm.getReplyFormList().stream()
                .map(r -> r.getMemberId())
                .collect(toList()))
                .containsExactly(member2.getId(), member3.getId(), member2.getId());
    }

    /**
     * 수정 확인
     */
    @Test
//    @Rollback(value = false)
    public void 수정_확인() throws IOException {
        // given
        // 추천합니다글 생성
        List<RecommendationPicture> rpList = new ArrayList<>();
        rpList.add(RecommendationPicture.createRecommendationPicture("x", "x"));
        rpList.add(RecommendationPicture.createRecommendationPicture("y1", "y2"));
        RecommendationForm recommendationForm = new RecommendationForm("book1", "a1", "좋아요~", rpList);

        Recommendation recommendation = Recommendation.createRecommendation(recommendationForm, member);
        // 저장
        recommendationJpaRepository.save(recommendation);

        // 수정 form 만들기
        RecommendationUpdateForm updateForm = new RecommendationUpdateForm(recommendation.getNum(), "book123", "a1", "아주 좋아요~");

        // when
        // 파일 있을 때
//        recommendationService.update(updateForm, files);
        // 파일 없을 때
        recommendationService.update(updateForm, new ArrayList<>());

        // then
        assertThat(recommendation.getItemName()).isEqualTo(updateForm.getItemName());
        assertThat(recommendation.getSellerId()).isEqualTo(updateForm.getSellerId());
        assertThat(recommendation.getContent()).isEqualTo(updateForm.getContent());
        // 파일 있을 때
//        assertThat(recommendation.getRecommendationPictureList().size()).isEqualTo(2 + 2);
        // 파일 없을 때
        assertThat(recommendation.getRecommendationPictureList().size()).isEqualTo(2);
    }

    /**
     * 수정 확인
     * -> 상품 이름 or 판매자 아이디가 존재하지 않을 때 예외 발생
     */
    @Test
    public void 수정_예외_발생_확인() throws IOException {
        // given
        // 추천합니다글 생성
        List<RecommendationPicture> rpList = new ArrayList<>();
        rpList.add(RecommendationPicture.createRecommendationPicture("x", "x"));
        rpList.add(RecommendationPicture.createRecommendationPicture("y1", "y2"));
        RecommendationForm recommendationForm = new RecommendationForm("book1", "a1", "좋아요~", rpList);

        Recommendation recommendation = Recommendation.createRecommendation(recommendationForm, member);
        // 저장
        recommendationJpaRepository.save(recommendation);

        // 수정 form 만들기
        RecommendationUpdateForm falutItemNameUpdateForm = new RecommendationUpdateForm(recommendation.getNum(), "book12", "a1", "아주 좋아요~");
        RecommendationUpdateForm falutSellerIdUpdateForm = new RecommendationUpdateForm(recommendation.getNum(), "book123", "a1342", "아주 좋아요~");

        // when
        assertThrows(NotExistingItemException.class, () -> recommendationService.update(falutItemNameUpdateForm, files));;
        assertThrows(NotExistingItemException.class, () -> recommendationService.update(falutSellerIdUpdateForm, new ArrayList<>()));
//        assertThrows(NotExistingIdException.class, () -> recommendationService.update(falutSellerIdUpdateForm, new ArrayList<>())); // 에러 발생

    }

    /**
     * 삭제 확인
     * comment: status R -> D로 변경된다.
     */
    @Test
    public void 삭제_확인() {
        // given
        // 추천합니다글 생성
        List<RecommendationPicture> rpList = new ArrayList<>();
        rpList.add(RecommendationPicture.createRecommendationPicture("x", "x"));
        rpList.add(RecommendationPicture.createRecommendationPicture("y1", "y2"));
        RecommendationForm recommendationForm = new RecommendationForm("book1", "a1", "좋아요~", new ArrayList<>());

        Recommendation recommendation = Recommendation.createRecommendation(recommendationForm, member);
        // 저장
        recommendationJpaRepository.save(recommendation);
        // when
        // 삭제하기
        recommendationService.delete(recommendation.getNum());

        // then
        assertThat(recommendation.getStatus()).isEqualTo(BoardStatus.D);
    }

    /**
     * 이미지 삭제 확인
     */
    @Test
//    @Rollback(value = false)
    public void 이미지_삭제_확인() {
        // given
        // 추천합니다글 생성
        List<RecommendationPicture> rpList = new ArrayList<>();
        RecommendationPicture picture1 = RecommendationPicture.createRecommendationPicture("x", "x");
        RecommendationPicture picture2 = RecommendationPicture.createRecommendationPicture("y1", "y2");
        RecommendationPicture picture3 = RecommendationPicture.createRecommendationPicture("y1", "y2");
        rpList.add(picture1);
        rpList.add(picture2);
        rpList.add(picture3);
        RecommendationForm recommendationForm = new RecommendationForm("book1", "a1", "좋아요~", rpList);

        Recommendation recommendation = Recommendation.createRecommendation(recommendationForm, member);
        // 저장
        recommendationJpaRepository.save(recommendation);

        // when
        // 이미지 삭제하기
        recommendationService.deletePicture(recommendation.getNum(), picture1.getNum());

        // then
        // 상세보기에서 이미지 나오는지 확인
        RecommendationDetailForm detailForm = recommendationService.watchDetail(recommendation.getNum());

        assertThat(detailForm.getRecommendationPictureFormList().size()).isEqualTo(2);
    }

    /**
     * 관리자가 아닌 경우 추천합니다글 목록 불러오기 확인
     */
    @Test
//    @Rollback(value = false)
    public void 관라지_아닌_경우_목록_불러오기_확인() {
        // given
        // 추천합니다글 생성
        List<RecommendationPicture> rpList = new ArrayList<>();
        List<RecommendationPicture> rpList2 = new ArrayList<>();
        RecommendationPicture picture1 = RecommendationPicture.createRecommendationPicture("x", "x");
        RecommendationPicture picture2 = RecommendationPicture.createRecommendationPicture("y1", "y2");
        RecommendationPicture picture3 = RecommendationPicture.createRecommendationPicture("y1", "y2");
        RecommendationPicture picture4 = RecommendationPicture.createRecommendationPicture("y1", "y2");
        rpList.add(picture1);
        rpList.add(picture2);
        rpList.add(picture3);

        rpList2.add(picture4);

        // 더미 데이터 생성
        Recommendation recommendation = null;
        var rCount = 0;
        var r2Count = 0;
        var r3Count = 0;
        var amount = 100;
        for(int i = 0; i < amount; i++) {
            if(i % 3 == 0) {
                RecommendationForm recommendationForm = new RecommendationForm("book1", "a1", "좋아요~", rpList);
                recommendation = Recommendation.createRecommendation(recommendationForm, member);
                rCount += 1;
            } else if(i % 3 == 1) {
                RecommendationForm recommendationForm = new RecommendationForm("book1", "a1", "좋아요~", new ArrayList<>());
                recommendation = Recommendation.createRecommendation(recommendationForm, member2);
                r2Count += 1;

            } else {
                RecommendationForm recommendationForm = new RecommendationForm("book3", "b2", "좋아요~", rpList2);
                recommendation = Recommendation.createRecommendation(recommendationForm, member3);
                r3Count += 1;
            }
            // 저장
            recommendationJpaRepository.save(recommendation);
        }

        // when
        // 페이지 생성
        Pageable page0size10 = PageRequest.of(0, 10);
        Pageable page2size5 = PageRequest.of(2, 5);

        // 날짜
        String today = "2023-02-01";
        String otherDay = "2023-02-02";

        // 조건 생성
        // 조건 0
        RecommendationSearchCond cond0 = new RecommendationSearchCond("", "", "", "");
        // 조건 1
        RecommendationSearchCond cond11 = new RecommendationSearchCond("book1", "", "", "");
        RecommendationSearchCond cond12 = new RecommendationSearchCond("", "a1", "", "");
        RecommendationSearchCond cond13 = new RecommendationSearchCond("", "", "b1", "");
        RecommendationSearchCond cond14 = new RecommendationSearchCond("", "", "", today);
        // 조건 2
        RecommendationSearchCond cond21 = new RecommendationSearchCond("book1", "a1", "", "");
        RecommendationSearchCond cond22 = new RecommendationSearchCond("book1", "", "b1", "");
        RecommendationSearchCond cond23 = new RecommendationSearchCond("book1", "", "", today);
        RecommendationSearchCond cond24 = new RecommendationSearchCond("", "a1", "a1", "");
        RecommendationSearchCond cond25 = new RecommendationSearchCond("", "a1", "", today);
        RecommendationSearchCond cond26 = new RecommendationSearchCond("", "", "c1", today);
        // 조건 3
        RecommendationSearchCond cond31 = new RecommendationSearchCond("book1", "a1", "b1", "");
        RecommendationSearchCond cond32 = new RecommendationSearchCond("book1", "a1", "", today);
        RecommendationSearchCond cond33 = new RecommendationSearchCond("book3", "", "c1", today);
        RecommendationSearchCond cond34 = new RecommendationSearchCond("", "a1", "b1", today);
        // 조건4
        RecommendationSearchCond cond41 = new RecommendationSearchCond("book1", "a1", "b1", today);

        // 조건에 맞는 값 없을 때
        RecommendationSearchCond cond15 = new RecommendationSearchCond("", "a3", "", "");
        RecommendationSearchCond cond27 = new RecommendationSearchCond("", "123456", "c1", "");
        RecommendationSearchCond cond35 = new RecommendationSearchCond("book1", "a1", "b12", "");
        RecommendationSearchCond cond42 = new RecommendationSearchCond("book1123", "a1", "b1", otherDay);


        // then
        // 조건0
        searchListTestResult(cond0, page0size10, rCount + r2Count + r3Count);
        // 조건1
        searchListTestResult(cond11, page0size10, rCount + r2Count);
        searchListTestResult(cond12, page0size10, rCount + r2Count);
        searchListTestResult(cond13, page0size10, r2Count);
        searchListTestResult(cond14, page0size10, rCount + r2Count + r3Count);
        // 조건2
        searchListTestResult(cond21, page2size5, rCount + r2Count);
        searchListTestResult(cond22, page2size5, r2Count);
        searchListTestResult(cond23, page2size5, rCount + r2Count);
        searchListTestResult(cond24, page2size5, rCount);
        searchListTestResult(cond25, page2size5, rCount + r2Count);
        searchListTestResult(cond26, page2size5, r3Count);
        // 조건3
        searchListTestResult(cond31, page2size5, r2Count);
        searchListTestResult(cond32, page2size5, rCount + r2Count);
        searchListTestResult(cond33, page2size5, r3Count);
        searchListTestResult(cond34, page2size5, r2Count);
        // 조건4
        searchListTestResult(cond41, page0size10, r2Count);

        // 조건에 맞는 값 없을 때
        searchListTestResult(cond15, page0size10, 0);
        searchListTestResult(cond27, page2size5, 0);
        searchListTestResult(cond35, page2size5, 0);
        searchListTestResult(cond42, page0size10, 0);
    }

    // 목록_불러오기_확인 test에 사용
    private void searchListTestResult(RecommendationSearchCond cond, Pageable pageable, int count) {
        // 추천합니다글 찾기
        Page<RecommendationListForm> result = recommendationService.selectList(cond, pageable);
        // 결과 확인
        assertThat(result.getTotalElements()).isEqualTo(count);
    }

    /**
     * 추천합니다글 목록 불러오기 확인
     * -> 삭제 게시글 있을 경우
     */
    @Test
//    @Rollback(value = false)
    public void 목록_불러오기_삭제_게시글_있는경우_확인() {
        // given
        // 추천합니다글 생성
        List<RecommendationPicture> rpList = new ArrayList<>();
        List<RecommendationPicture> rpList2 = new ArrayList<>();
        RecommendationPicture picture1 = RecommendationPicture.createRecommendationPicture("x", "x");
        RecommendationPicture picture2 = RecommendationPicture.createRecommendationPicture("y1", "y2");
        RecommendationPicture picture3 = RecommendationPicture.createRecommendationPicture("y1", "y2");
        RecommendationPicture picture4 = RecommendationPicture.createRecommendationPicture("y1", "y2");
        rpList.add(picture1);
        rpList.add(picture2);
        rpList.add(picture3);

        rpList2.add(picture4);

        RecommendationForm recommendationForm = new RecommendationForm("book1", "a1", "좋아요~", rpList);
        Recommendation recommendation = Recommendation.createRecommendation(recommendationForm, member);

        RecommendationForm recommendationForm2 = new RecommendationForm("book1", "a1", "좋아요~", new ArrayList<>());
        Recommendation recommendation2 = Recommendation.createRecommendation(recommendationForm, member2);

        RecommendationForm recommendationForm3 = new RecommendationForm("book3", "b2", "좋아요~", rpList2);
        Recommendation recommendation3 = Recommendation.createRecommendation(recommendationForm, member3);
        // 저장
        recommendationJpaRepository.save(recommendation);
        recommendationJpaRepository.save(recommendation2);
        recommendationJpaRepository.save(recommendation3);

        //when
        // 추천합니다글 삭제하기
        recommendationService.delete(recommendation2.getNum());

        //then
        // 게시글 목록 게시글 수 확인
        Pageable page0size10 = PageRequest.of(0, 10);
        RecommendationSearchCond cond0 = new RecommendationSearchCond("", "", "", "");

        // 관리자 아닌 경우
        searchListTestResult(cond0, page0size10, 2);
//        searchListTestResult(cond0, page0size10, 3);// 에러 발생
        // 관리자인 경우
        searchListForAdminTestResult(cond0, page0size10, 3);
    }


    /**
     * 관리자인 경우 추천합니다글 목록 불러오기 확인
     */
    @Test
    public void 관라지인_경우_목록_불러오기_확인() {
// given
        // 추천합니다글 생성
        List<RecommendationPicture> rpList = new ArrayList<>();
        List<RecommendationPicture> rpList2 = new ArrayList<>();
        RecommendationPicture picture1 = RecommendationPicture.createRecommendationPicture("x", "x");
        RecommendationPicture picture2 = RecommendationPicture.createRecommendationPicture("y1", "y2");
        RecommendationPicture picture3 = RecommendationPicture.createRecommendationPicture("y1", "y2");
        RecommendationPicture picture4 = RecommendationPicture.createRecommendationPicture("y1", "y2");
        rpList.add(picture1);
        rpList.add(picture2);
        rpList.add(picture3);

        rpList2.add(picture4);

        // 더미 데이터 생성
        Recommendation recommendation = null;
        var rCount = 0;
        var r2Count = 0;
        var r3Count = 0;
        var amount = 100;
        for(int i = 0; i < amount; i++) {
            if(i % 3 == 0) {
                RecommendationForm recommendationForm = new RecommendationForm("book1", "a1", "좋아요~", rpList);
                recommendation = Recommendation.createRecommendation(recommendationForm, member);
                rCount += 1;
            } else if(i % 3 == 1) {
                RecommendationForm recommendationForm = new RecommendationForm("book1", "a1", "좋아요~", new ArrayList<>());
                recommendation = Recommendation.createRecommendation(recommendationForm, member2);
                r2Count += 1;

            } else {
                RecommendationForm recommendationForm = new RecommendationForm("book3", "b2", "좋아요~", rpList2);
                recommendation = Recommendation.createRecommendation(recommendationForm, member3);
                r3Count += 1;
            }
            // 저장
            recommendationJpaRepository.save(recommendation);
        }

        // when
        // 페이지 생성
        Pageable page0size10 = PageRequest.of(0, 10);
        Pageable page2size5 = PageRequest.of(2, 5);

        // 날짜
        String today = "2023-02-01";
        String otherDay = "2023-02-02";

        // 조건 생성
        // 조건 0
//        RecommendationSearchCond cond0 = new RecommendationSearchCond("", "", "", "");
        // 조건 1
        RecommendationSearchCond cond11 = new RecommendationSearchCond("book1", "", "", "");
        RecommendationSearchCond cond12 = new RecommendationSearchCond("", "a1", "", "");
        RecommendationSearchCond cond13 = new RecommendationSearchCond("", "", "b1", "");
        RecommendationSearchCond cond14 = new RecommendationSearchCond("", "", "", today);
        // 조건 2
        RecommendationSearchCond cond21 = new RecommendationSearchCond("book1", "a1", "", "");
        RecommendationSearchCond cond22 = new RecommendationSearchCond("book1", "", "b1", "");
        RecommendationSearchCond cond23 = new RecommendationSearchCond("book1", "", "", today);
        RecommendationSearchCond cond24 = new RecommendationSearchCond("", "a1", "a1", "");
        RecommendationSearchCond cond25 = new RecommendationSearchCond("", "a1", "", today);
        RecommendationSearchCond cond26 = new RecommendationSearchCond("", "", "c1", today);
        // 조건 3
        RecommendationSearchCond cond31 = new RecommendationSearchCond("book1", "a1", "b1", "");
        RecommendationSearchCond cond32 = new RecommendationSearchCond("book1", "a1", "", today);
        RecommendationSearchCond cond33 = new RecommendationSearchCond("book3", "", "c1", today);
        RecommendationSearchCond cond34 = new RecommendationSearchCond("", "a1", "b1", today);
        // 조건4
        RecommendationSearchCond cond41 = new RecommendationSearchCond("book1", "a1", "b1", today);

        // 조건에 맞는 값 없을 때
        RecommendationSearchCond cond15 = new RecommendationSearchCond("", "a3", "", "");
        RecommendationSearchCond cond27 = new RecommendationSearchCond("", "123456", "c1", "");
        RecommendationSearchCond cond35 = new RecommendationSearchCond("book1", "a1", "b12", "");
        RecommendationSearchCond cond42 = new RecommendationSearchCond("book1123", "a1", "b1", otherDay);

        // then
        // 조건0
//        searchListForAdminTestResult(cond0, page0size10, rCount + r2Count + r3Count);
        // 조건1
        searchListForAdminTestResult(cond11, page0size10, rCount + r2Count);
        searchListForAdminTestResult(cond12, page0size10, rCount + r2Count);
        searchListForAdminTestResult(cond13, page0size10, r2Count);
        searchListForAdminTestResult(cond14, page0size10, rCount + r2Count + r3Count);
        // 조건2
        searchListForAdminTestResult(cond21, page2size5, rCount + r2Count);
        searchListForAdminTestResult(cond22, page2size5, r2Count);
        searchListForAdminTestResult(cond23, page2size5, rCount + r2Count);
        searchListForAdminTestResult(cond24, page2size5, rCount);
        searchListForAdminTestResult(cond25, page2size5, rCount + r2Count);
        searchListForAdminTestResult(cond26, page2size5, r3Count);
        // 조건3
        searchListForAdminTestResult(cond31, page2size5, r2Count);
        searchListForAdminTestResult(cond32, page2size5, rCount + r2Count);
        searchListForAdminTestResult(cond33, page2size5, r3Count);
        searchListForAdminTestResult(cond34, page2size5, r2Count);
        // 조건4
        searchListForAdminTestResult(cond41, page0size10, r2Count);

        // 조건에 맞는 값 없을 때
        searchListForAdminTestResult(cond15, page0size10, 0);
        searchListForAdminTestResult(cond27, page2size5, 0);
        searchListForAdminTestResult(cond35, page2size5, 0);
        searchListForAdminTestResult(cond42, page0size10, 0);
    }

    // 목록_불러오기_확인 test에 사용 for admin
    private void searchListForAdminTestResult(RecommendationSearchCond cond, Pageable pageable, int count) {
        // 추천합니다글 찾기
        Page<RecommendationListForAdminForm> result = recommendationService.selectListForAdmin(cond, pageable);
        // 결과 확인
        assertThat(result.getTotalElements()).isEqualTo(count);
    }
























}
