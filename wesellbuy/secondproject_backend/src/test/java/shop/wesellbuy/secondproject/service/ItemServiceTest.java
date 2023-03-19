package shop.wesellbuy.secondproject.service;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
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
import shop.wesellbuy.secondproject.domain.common.PictureStatus;
import shop.wesellbuy.secondproject.domain.item.*;
import shop.wesellbuy.secondproject.domain.likes.ItemLikes;
import shop.wesellbuy.secondproject.domain.member.SelfPicture;
import shop.wesellbuy.secondproject.domain.reply.ItemReply;
import shop.wesellbuy.secondproject.exception.item.NotExistingItemTypeException;
import shop.wesellbuy.secondproject.repository.item.ItemJpaRepository;
import shop.wesellbuy.secondproject.repository.item.ItemSearchCond;
import shop.wesellbuy.secondproject.repository.likes.ItemLikesJpaRepository;
import shop.wesellbuy.secondproject.repository.member.MemberJpaRepository;
import shop.wesellbuy.secondproject.repository.reply.item.ItemReplyJpaRepository;
import shop.wesellbuy.secondproject.service.item.ItemService;
import shop.wesellbuy.secondproject.service.reply.item.ItemReplyService;
import shop.wesellbuy.secondproject.web.controller.ItemController;
import shop.wesellbuy.secondproject.web.item.*;
import shop.wesellbuy.secondproject.web.member.MemberForm;
import shop.wesellbuy.secondproject.web.reply.ReplyForm;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@Slf4j
public class ItemServiceTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;
    @Autowired
    ItemService itemService;
    @Autowired
    ItemJpaRepository itemJpaRepository;
    @Autowired
    EntityManager em;
    @Autowired
    ItemLikesJpaRepository itemLikesJpaRepository;
    @Autowired
    ItemReplyJpaRepository itemReplyJpaRepository;
    @Autowired
    ItemReplyService itemReplyService;

    // 필요한 객체들
    Member member; // test 회원
    Member member2; // test 회원
    Member member3; // test 회원
    Member member4; // test 회원
    Member member5; // test 회원
    Member member6; // test 회원

    List<MultipartFile> bookFiles = new ArrayList<>(); // test 파일 모음
    List<MultipartFile> furnitureFiles = new ArrayList<>(); // test 파일 모음
    List<MultipartFile> HAFiles = new ArrayList<>(); // test 파일 모음
    List<MultipartFile> itemFiles = new ArrayList<>(); // test 파일 모음

    @BeforeEach
    public void init() throws IOException {

        log.info("test init 시작");

        // member 생성
        SelfPicture selfPicture = SelfPicture.createSelfPicture("test1", "test2");
        SelfPicture selfPicture3 = SelfPicture.createSelfPicture("test1", "test2");
        MemberForm memberForm1 = new MemberForm("a", "a1", "123", "a", "a@a", "01012341234", "0511231234", "korea", "b", "h", "h", selfPicture);
        Member member = Member.createMember(memberForm1);
        MemberForm memberForm2 = new MemberForm("a", "b1", "123", "a", "a@a", "01012341234", "0511231234", "korea", "b", "h", "h", null);
        Member member2 = Member.createMember(memberForm2);
        MemberForm memberForm3 = new MemberForm("a", "c1", "123", "a", "a@a", "01012341234", "0511231234", "korea", "b", "h", "h", selfPicture3);
        Member member3 = Member.createMember(memberForm3);
        MemberForm memberForm4 = new MemberForm("a", "d1", "123", "a", "a@a", "01012341234", "0511231234", "korea", "b", "h", "h", selfPicture3);
        Member member4 = Member.createMember(memberForm4);
        MemberForm memberForm5 = new MemberForm("a", "e1", "123", "a", "a@a", "01012341234", "0511231234", "korea", "b", "h", "h", selfPicture3);
        Member member5 = Member.createMember(memberForm5);
        MemberForm memberForm6 = new MemberForm("a", "f1", "123", "a", "a@a", "01012341234", "0511231234", "korea", "b", "h", "h", selfPicture3);
        Member member6 = Member.createMember(memberForm6);

        memberJpaRepository.save(member);
        memberJpaRepository.save(member2);
        memberJpaRepository.save(member3);
        memberJpaRepository.save(member4);
        memberJpaRepository.save(member5);
        memberJpaRepository.save(member6);
        this.member = member;
        this.member2 = member2;
        this.member3 = member3;
        this.member4 = member4;
        this.member5 = member5;
        this.member6 = member6;

        // 파일 생성
        // 파일명 모음
        String[] bookFileNames = {"book", "book2"};
        String[] furnitureFileNames = {"table", "box", "soap"};
        String[] HAFileNames = {"refrigerator", "선풍기"};
        String[] itemFileNames = {"bag", "shoes", "칫솔"};
        String contentType = "jpg"; // 파일 확장자

        // 파일 생성 시작

        // book MultipartFile List 생성하기
        makeMultipartFileList(bookFileNames, contentType, bookFiles);
        // furniture MultipartFile List 생성하기
        makeMultipartFileList(furnitureFileNames, contentType, furnitureFiles);
        // homeappliances MultipartFile List 생성하기
        makeMultipartFileList(HAFileNames, contentType, HAFiles);
        // item(기타) MultipartFile List 생성하기
        makeMultipartFileList(itemFileNames, contentType, itemFiles);

        // 파일 생성 끝

        log.info("test init 끝");

    }

    /**
     * Multipart 파일 생성하기
     */
    private void makeMultipartFileList(String[] bookFileNames, String contentType, List<MultipartFile> multipartFileList) throws IOException {
        for(String fileName : bookFileNames) {
            int i = 0;
            String originalFileName = fileName + "." + contentType; // 원본 파일 이름.확장자
            String filePath = "src/test/resources/testImages/" + fileName + "." + contentType;// 파일 경로
            FileInputStream fileInputStream = new FileInputStream(filePath); // 파일 불러오기
            // MockMultipartFile 생성
            MockMultipartFile file = new MockMultipartFile("image" + i, originalFileName, contentType, fileInputStream);

            // list에 추가하기
            multipartFileList.add(file);
            i++;
        }
    }

    /**
     * 상품 저장 확인
     *
     * comment : entity의 dtype을 보려면 db에 저장이 된 후 볼 수 있다.
     */
    @Test
    @Rollback(value = false)
    public void 상품_저장_확인() throws IOException {
        // given
        // ItemOriginalForm 생성
        ItemOriginalForm form = new ItemOriginalForm("선풍기", 10,2000, "깨끗하고 좋아요", "HA", new ArrayList<>(), "", "", "samsung");
        // when
        // 저장하기
        int itemNum = itemService.save(form, HAFiles, member2.getNum());

        // dtype 읽어오기 위해 영속성컨택스트/캐시 비우기기
       em.flush();
        em.clear();

        // then
        // 저장되었는지 확인하기
        Item findItem2 = itemJpaRepository.findById(itemNum).orElseThrow();
        HomeAppliances findItem = null;

        log.info("findItem2 : {}", findItem2.getName());
        log.info("findItem2 dtype : {}", findItem2.getDtype());

        if(findItem2.getDtype().equals("HA")) {
            findItem = (HomeAppliances) findItem2;
        }

//        Assertions.assertThat(findItem.getMember()).isEqualTo(member2); // flush를 해서 다르다.
        assertThat(findItem.getMember().getId()).isEqualTo(member2.getId());
        assertThat(findItem.getName()).isEqualTo(form.getName());
        assertThat(findItem.getStock()).isEqualTo(form.getStock());
        assertThat(findItem.getPrice()).isEqualTo(form.getPrice());
        assertThat(findItem.getContent()).isEqualTo(form.getContent());
        assertThat(findItem.getDtype()).isEqualTo(form.getType());
        assertThat(findItem.getItemPictureList().size()).isEqualTo(form.getItemPictureList().size());
        assertThat(findItem.getCompany()).isEqualTo(form.getCompany());
        // 강제형변환으로 데이터가 가져와지는지 확인
        log.info("강제형변환으로 데이터가 가져와지는지 확인 : {}", findItem.getCompany()); // ok
        // 이미지 같은지 확인
//        Assertions.assertThat(findItem.getItemPictureList()).isEqualTo(form.getItemPictureList()); // flush로 인해 다르다.
        assertThat(findItem.getItemPictureList().size()).isEqualTo(form.getItemPictureList().size());
        assertThat(findItem.getItemPictureList().size()).isEqualTo(2);
    }

    /**
     * 상품 저장 확인
     * -> 없는 타입일 때 예외 발생 확인
     */
    @Test
//    @Rollback(value = false)
    public void 상품_저장_예외_발생_확인() throws IOException {
        // given
        // ItemOriginalForm 생성
        ItemOriginalForm form = new ItemOriginalForm("선풍기", 10, 2000, "깨끗하고 좋아요", "ABC", new ArrayList<>(), "", "", "samsung");
        // when // then
        // 저장하기
        assertThrows(NotExistingItemTypeException.class, () -> itemService.save(form, HAFiles, member2.getNum()));
    }

    /**
     * 상품 수정 확인
     */
    @Test
    @Rollback(value = false)
    public void 상품_수정_확인() throws IOException {
        // given
        // 상품 저장
        ItemOriginalForm form = new ItemOriginalForm("책1", 10, 2000, "재미있어요~", "B", new ArrayList<>(), "곰돌이푸", "곰돌이 출판사", "");
        // 처음 이미지 없을 때
//        int itemNum = itemService.save(form, new ArrayList<>(), member3.getNum());
        // 처음 이미지 있을 때
        int itemNum = itemService.save(form, bookFiles, member3.getNum());

        // dtype 사용 위해
        em.flush();
        em.clear();

        // when
        // 상품 수정 form 생성
        // type 변경 안 되었을 때
        ItemUpdateForm updateForm = new ItemUpdateForm(itemNum, "book12", 20, 25000, "새로워요~", "B", new ArrayList<>(), "곰돌이푸", "곰돌이2출판사", "");
        // type이 변경되었을 때(type 강제형변환 에러 발생 book을 homeappliances로 바꿀 수 없다.)
//        ItemUpdateForm updateForm = new ItemUpdateForm(itemNum, "book12", 20, 25000, "새로워요~", "HA", new ArrayList<>(), "곰돌이푸", "곰돌이2출판사", "");

//        itemService.update(updateForm, bookFiles);
        // 수정 이미지 없을 때
        itemService.update(updateForm, new ArrayList<>());
        // then
        Item findItem = itemJpaRepository.findById(itemNum).orElseThrow();
        Book findBook = null;

        if(findItem.getDtype().equals("B")) {
            findBook = (Book) findItem;
        }

        assertThat(findBook.getName()).isEqualTo(updateForm.getName());
        assertThat(findBook.getStock()).isEqualTo(updateForm.getStock());
        assertThat(findBook.getPrice()).isEqualTo(updateForm.getPrice());
        assertThat(findBook.getContent()).isEqualTo(updateForm.getContent());
        assertThat(findBook.getAuthor()).isEqualTo(updateForm.getAuthor());
        assertThat(findBook.getPublisher()).isEqualTo(updateForm.getPublisher());
        // 처음 이미지 없을 때
//        assertThat(findBook.getItemPictureList().size()).isEqualTo(2);
        // 처음 이미지 있을 때
//        assertThat(findBook.getItemPictureList().size()).isEqualTo(4);
        // 수정 이미지 없을 때
        assertThat(findBook.getItemPictureList().size()).isEqualTo(2);
        assertThat(findBook.getMember().getId()).isEqualTo(member3.getId());
    }

    /**
     * 상품 삭제 확인
     * -> status (R -> D)
     */
    @Test
    @Rollback(value = false)
    public void 상품_삭제_확인() {
        // given
        // 상품 저장
        List<ItemPicture> itemPictureList = new ArrayList<>();
        itemPictureList.add(ItemPicture.createItemPicture("a", "a"));
        itemPictureList.add(ItemPicture.createItemPicture("a1", "a2"));

        FurnitureForm furnitureForm = new FurnitureForm(10, 2000, "책상", "잘 만들어졌어요~", itemPictureList, "hansem");
        Item item2 = Furniture.createFurniture(furnitureForm, member);

        Item item = itemJpaRepository.save(item2);
        // when
        // 상품 삭제하기
        itemService.delete(item.getNum());
        // then

        // 상태 변경 확인하기
        Item findItem = itemJpaRepository.findById(item.getNum()).orElseThrow();
        assertThat(findItem.getStatus()).isEqualTo(ItemStatus.D);

        // list에 나오는지 확인하기
        Pageable pageable = PageRequest.of(0, 10);
        ItemSearchCond cond = new ItemSearchCond("", "", "", "");
        Page<ItemListForm> form = itemService.selectList(cond, pageable);

        assertThat(form.getTotalElements()).isEqualTo(0L);

        // 관리자에는 나오는지 확인하기
        Page<ItemListFormForAdmin> form2 = itemService.selectListForAdmin(cond, pageable);

        assertThat(form2.getTotalElements()).isEqualTo(1L);

    }

    /**
     * 상품 상세보기 확인
     * -> status (R -> D)
     */
    @Test
    @Rollback(value = false)
    public void 상품_상세보기_확인() throws IOException {
        // given
        ItemOriginalForm itemForm = new ItemOriginalForm("자전거", 15, 2000, "어디든 갈 수 있어요~", "ITEM", new ArrayList<>(), "", "", "");
        ItemOriginalForm bookForm = new ItemOriginalForm("책1", 10, 2000, "재미있어요~", "B", new ArrayList<>(), "곰돌이푸", "곰돌이 출판사", "");

        int itemNum = itemService.save(itemForm, itemFiles, member2.getNum());
        int bookNum = itemService.save(bookForm, new ArrayList<>(), member3.getNum());

        // dtype 사용하기 위해
        em.flush();
        em.clear();

        // 좋아요 추가
        Book findBook = (Book) itemJpaRepository.findById(bookNum).orElseThrow();
        ItemLikes itemLikes = ItemLikes.createItemLikes(member, findBook);
        ItemLikes itemLikes2 = ItemLikes.createItemLikes(member2, findBook);

        itemLikesJpaRepository.save(itemLikes);
        itemLikesJpaRepository.save(itemLikes2);

        // 댓글 작성하기
        ReplyForm replyForm = new ReplyForm("저도 구입해야겠네요");
        ItemReply itemReply = ItemReply.createItemReply(replyForm, member, findBook);
        ItemReply itemReply2 = ItemReply.createItemReply(replyForm, member, findBook);

        itemReplyJpaRepository.save(itemReply);
        itemReplyJpaRepository.save(itemReply2);

        // 댓글 1개 삭제하기
        itemReplyService.delete(itemReply.getNum());

        // when
        // 상세보기
        // 1번 조회
        ItemDetailForm itemDetailForm = itemService.watchDetail(itemNum);
        // 2번 조회
        ItemDetailForm bookDetailForm = itemService.watchDetail(bookNum);
        ItemDetailForm bookDetailForm2 = itemService.watchDetail(bookNum);

        // then
        // item(기타)
        assertThat(itemDetailForm.getName()).isEqualTo(itemForm.getName());
        assertThat(itemDetailForm.getStock()).isEqualTo(itemForm.getStock());
        assertThat(itemDetailForm.getPrice()).isEqualTo(itemForm.getPrice());
        assertThat(itemDetailForm.getContent()).isEqualTo(itemForm.getContent());
        assertThat(itemDetailForm.getType()).isEqualTo(itemForm.getType());
        assertThat(itemDetailForm.getHits()).isEqualTo(1);
        assertThat(itemDetailForm.getMemberId()).isEqualTo(member2.getId());
        assertThat(itemDetailForm.getLikes()).isEqualTo(0);
        assertThat(itemDetailForm.getPictureForms().size()).isEqualTo(itemFiles.size());
        assertThat(itemDetailForm.getReplyFormList().size()).isEqualTo(0);


        // book
        assertThat(bookDetailForm.getName()).isEqualTo(bookForm.getName());
        assertThat(bookDetailForm.getStock()).isEqualTo(bookForm.getStock());
        assertThat(bookDetailForm.getPrice()).isEqualTo(bookForm.getPrice());
        assertThat(bookDetailForm.getContent()).isEqualTo(bookForm.getContent());
        assertThat(bookDetailForm.getType()).isEqualTo(bookForm.getType());
        assertThat(bookDetailForm.getHits()).isEqualTo(1);
        assertThat(bookDetailForm.getMemberId()).isEqualTo(member3.getId());
        assertThat(bookDetailForm.getLikes()).isEqualTo(2);
        assertThat(bookDetailForm.getPictureForms().size()).isEqualTo(0);
        assertThat(bookDetailForm.getReplyFormList().size()).isEqualTo(1);
        assertThat(bookDetailForm.getAuthor()).isEqualTo(bookForm.getAuthor());
        assertThat(bookDetailForm.getPublisher()).isEqualTo(bookForm.getPublisher());

        // book 2번 조회
        assertThat(bookDetailForm2.getHits()).isEqualTo(2);

    }

    /**
     * 상품 이미지 확인
     * -> status : R -> D
     */
    @Test
    @Rollback(value = false)
    public void 상품_이미지_삭제_확인() {
        // given
        // 이미지, 상품 저장
        List<ItemPicture> itemPictureList = new ArrayList<>();
        ItemPicture picture1 = ItemPicture.createItemPicture("a", "a");
        ItemPicture picture2 = ItemPicture.createItemPicture("a1", "a2");
        itemPictureList.add(picture1);
        itemPictureList.add(picture2);

        FurnitureForm furnitureForm = new FurnitureForm(10, 2000, "책상", "잘 만들어졌어요~", itemPictureList, "hansem");
        Item item2 = Furniture.createFurniture(furnitureForm, member);

        itemJpaRepository.save(item2);

        // dtype 확인 위해
        em.flush();
        em.clear();

        // when
        // 이미지 삭제하기
        itemService.deletePicture(item2.getNum(), picture1.getNum());

        // then
        // 이미지 상태 확인
        // em.flush, clear로 인해서 기존의 picture2 말고 새로운 picture2가 생겨있다.
        // 본래의 picture2는 수정된 picture2를 참조하지 못한다.
//        assertThat(picture2.getStatus()).isEqualTo(PictureStatus.D); 에러 발생
        // 다른 방법
        Item findItem = itemJpaRepository.findById(item2.getNum()).orElseThrow();
        // 삭제된 사진 찾아오기
        ItemPicture deletedPicture = findItem.getItemPictureList().stream()
                .filter(p -> p.getStatus().equals(PictureStatus.D))
                .findFirst()
                .orElseThrow();

        assertThat(deletedPicture.getNum()).isEqualTo(picture1.getNum());
        // 상품 상세보기에서 이미지 나오는지 확인
        ItemDetailForm form = itemService.watchDetail(item2.getNum());
        assertThat(form.getPictureForms().stream()
                .map(p -> p.getNum()).
                collect(toList()))
                .containsExactly(picture2.getNum());
    }

    /**
     * 상품 목록 불러오기 확인
     * -> 삭제 상품 있는 경우
     *
     * -> 소문자 대문자 구별한다.(조건에)
     *    -> 정확성 필요
     */
    @Test
    @Rollback(value = false)
    public void 목록_불러오기_삭제_게시글_있는_경우_확인() {
        // given
        // 페이지, 사이즈 정하기
        Pageable pageablePage0Size10 = PageRequest.of(0, 10);
        Pageable pageablePage3Size5 = PageRequest.of(3, 5);
        Pageable pageablePage2Size2 = PageRequest.of(2, 2);

        // 날짜 condition
        String today = "2023-02-03";
        String otherDay = "2023-02-04";

        // 상품 생성하기
        int number = 100;
        Item item = null;
        var rCount = 0;
        var r2Count = 0;
        var r3Count = 0;
        // item 생성
        List<ItemPicture> itemPictureList = new ArrayList<>();
        List<ItemPicture> itemPictureList2 = new ArrayList<>();
        ItemPicture picture1 = ItemPicture.createItemPicture("a", "a");
        ItemPicture picture2 = ItemPicture.createItemPicture("a1", "a2");
        itemPictureList.add(picture1);
        itemPictureList.add(picture2);

        itemPictureList2.add(picture1);

        for(int i = 0; i < number; i++) {
            if(i % 3 == 0) {
                BookForm bookForm = new BookForm(10, 1000, "x", "x is...", itemPictureList, "ed", "ok");
                item = Book.createBook(bookForm, member);
                rCount += 1;
            } else if(i % 3 == 1) {
                FurnitureForm furnitureForm = new FurnitureForm(20, 2000, "y", "y is...", new ArrayList<>(), "ed");
                item = Furniture.createFurniture(furnitureForm, member2);
                r2Count += 1;
            } else {
                HomeAppliancesForm homeAppliancesForm = new HomeAppliancesForm(30, 3000, "z", "z is...", itemPictureList2, "ed2");
                item = HomeAppliances.createHomeAppliances(homeAppliancesForm, member3);
                r3Count += 1;
            }
            itemJpaRepository.save(item);

            // 삭제하기
            if(i % 3 == 2) {
                itemService.delete(item.getNum());
            }
        }

        // when
        // 검색조건 생성
        // 조건 0
        ItemSearchCond cond0 = new ItemSearchCond("", "", "", "");
        // 조건 1
        ItemSearchCond cond1 = new ItemSearchCond("x", "", "", "");
        ItemSearchCond cond2 = new ItemSearchCond("", "a1", "", "");
        ItemSearchCond cond3 = new ItemSearchCond("", "", today, "");
        ItemSearchCond cond4 = new ItemSearchCond("", "", "", "HA");

        // 조건 2
        ItemSearchCond cond21 = new ItemSearchCond("x", "a1", "", "");
        ItemSearchCond cond22 = new ItemSearchCond("x", "", today, "");
        ItemSearchCond cond23 = new ItemSearchCond("x", "", "", "B");
        ItemSearchCond cond24 = new ItemSearchCond("", "a1", today, "");
        ItemSearchCond cond25 = new ItemSearchCond("", "a1", "", "B");
        ItemSearchCond cond26 = new ItemSearchCond("", "", today, "F");

        // 조건 3
        ItemSearchCond cond31 = new ItemSearchCond("x", "a1", today, "");
        ItemSearchCond cond32 = new ItemSearchCond("x", "a1", "", "B");
        ItemSearchCond cond33 = new ItemSearchCond("x", "", today, "B");
        ItemSearchCond cond34 = new ItemSearchCond("", "b1", today, "F");

        // 조건 4
        ItemSearchCond cond41 = new ItemSearchCond("x", "a1", today, "B");


        // 검색에 대한 result가 없는 경우
        ItemSearchCond cond5 = new ItemSearchCond("ri", "", "", "");
        ItemSearchCond cond6 = new ItemSearchCond("", "", "", "B23");
        ItemSearchCond cond27 = new ItemSearchCond("", "", otherDay, "B");
        ItemSearchCond cond35 = new ItemSearchCond("", "abc", today, "B");
        ItemSearchCond cond42 = new ItemSearchCond("x", "a1", today, "Hcd");


        // then
        // 결과 확인(개수로)
        // 조건 0
        testResult(pageablePage2Size2, cond0, rCount + r2Count);
        // 조건 1
        testResult(pageablePage0Size10, cond1, rCount);
        testResult(pageablePage0Size10, cond2, rCount);
        testResult(pageablePage0Size10, cond3, rCount + r2Count);
        testResult(pageablePage0Size10, cond4, 0);
        // 조건 2
        testResult(pageablePage0Size10, cond21, rCount);
        testResult(pageablePage0Size10, cond22, rCount);
        testResult(pageablePage0Size10, cond23, rCount);
        testResult(pageablePage0Size10, cond24, rCount);
        testResult(pageablePage0Size10, cond25, rCount);
        testResult(pageablePage0Size10, cond26, r2Count);
        // 조건 3
        testResult(pageablePage0Size10, cond31, rCount);
        testResult(pageablePage0Size10, cond32, rCount);
        testResult(pageablePage0Size10, cond33, rCount);
        testResult(pageablePage0Size10, cond34, r2Count);

        // 조건 4
        testResult(pageablePage3Size5, cond41, rCount);


        // 검색에 대한 result가 없는 경우
        testResult(pageablePage0Size10, cond5, 0);
        testResult(pageablePage0Size10, cond6, 0);
        testResult(pageablePage0Size10, cond27, 0);
        testResult(pageablePage0Size10, cond35, 0);
        testResult(pageablePage2Size2, cond42, 0);
    }

    // test for 관리자 아닐 때
    private void testResult(Pageable pageable, ItemSearchCond cond, int count) {
        Page<ItemListForm> result = itemService.selectList(cond, pageable);
        assertThat(result.getTotalElements()).isEqualTo(count);
    }

    /**
     * 관리자인 경우 상품 목록 불러오기 확인
     */
    @Test
    @Rollback(value = false)
    public void 관라지인_경우_목록_불러오기_확인() {

        // given
        // 페이지, 사이즈 정하기
        Pageable pageablePage0Size10 = PageRequest.of(0, 10);
        Pageable pageablePage3Size5 = PageRequest.of(3, 5);
        Pageable pageablePage2Size2 = PageRequest.of(2, 2);

        // 날짜 condition
        String today = "2023-02-03";
        String otherDay = "2023-02-04";

        // 상품 생성하기
        int number = 100;
        Item item = null;
        var rCount = 0;
        var r2Count = 0;
        var r3Count = 0;
        // item 생성
        List<ItemPicture> itemPictureList = new ArrayList<>();
        List<ItemPicture> itemPictureList2 = new ArrayList<>();
        ItemPicture picture1 = ItemPicture.createItemPicture("a", "a");
        ItemPicture picture2 = ItemPicture.createItemPicture("a1", "a2");
        itemPictureList.add(picture1);
        itemPictureList.add(picture2);

        itemPictureList2.add(picture1);

        for(int i = 0; i < number; i++) {
            if(i % 3 == 0) {
                BookForm bookForm = new BookForm(10, 1000, "x", "x is...", itemPictureList, "ed", "ok");
                item = Book.createBook(bookForm, member);
                rCount += 1;
            } else if(i % 3 == 1) {
                FurnitureForm furnitureForm = new FurnitureForm(20, 2000, "y", "y is...", new ArrayList<>(), "ed");
                item = Furniture.createFurniture(furnitureForm, member2);
                r2Count += 1;
            } else {
                HomeAppliancesForm homeAppliancesForm = new HomeAppliancesForm(30, 3000, "z", "z is...", itemPictureList2, "ed2");
                item = HomeAppliances.createHomeAppliances(homeAppliancesForm, member3);
                r3Count += 1;
            }
            itemJpaRepository.save(item);

            // 삭제하기
            if(i % 3 == 1) {
                itemService.delete(item.getNum());
            }
        }

        // when
        // 검색조건 생성
        // 조건 0
        ItemSearchCond cond0 = new ItemSearchCond("", "", "", "");
        // 조건 1
        ItemSearchCond cond1 = new ItemSearchCond("x", "", "", "");
        ItemSearchCond cond2 = new ItemSearchCond("", "a1", "", "");
        ItemSearchCond cond3 = new ItemSearchCond("", "", today, "");
        ItemSearchCond cond4 = new ItemSearchCond("", "", "", "HA");

        // 조건 2
        ItemSearchCond cond21 = new ItemSearchCond("x", "a1", "", "");
        ItemSearchCond cond22 = new ItemSearchCond("x", "", today, "");
        ItemSearchCond cond23 = new ItemSearchCond("x", "", "", "B");
        ItemSearchCond cond24 = new ItemSearchCond("", "a1", today, "");
        ItemSearchCond cond25 = new ItemSearchCond("", "a1", "", "B");
        ItemSearchCond cond26 = new ItemSearchCond("", "", today, "F");

        // 조건 3
        ItemSearchCond cond31 = new ItemSearchCond("x", "a1", today, "");
        ItemSearchCond cond32 = new ItemSearchCond("x", "a1", "", "B");
        ItemSearchCond cond33 = new ItemSearchCond("x", "", today, "B");
        ItemSearchCond cond34 = new ItemSearchCond("", "b1", today, "F");

        // 조건 4
        ItemSearchCond cond41 = new ItemSearchCond("x", "a1", today, "B");


        // 검색에 대한 result가 없는 경우
        ItemSearchCond cond5 = new ItemSearchCond("ri", "", "", "");
        ItemSearchCond cond6 = new ItemSearchCond("", "", "", "B23");
        ItemSearchCond cond27 = new ItemSearchCond("", "", otherDay, "B");
        ItemSearchCond cond35 = new ItemSearchCond("", "abc", today, "B");
        ItemSearchCond cond42 = new ItemSearchCond("x", "a1", today, "Hcd");


        // then
        // 결과 확인(개수로)
        // 조건 0
        testResultForAdmin(pageablePage2Size2, cond0, rCount + r2Count + r3Count);
        // 조건 1
        testResultForAdmin(pageablePage0Size10, cond1, rCount);
        testResultForAdmin(pageablePage0Size10, cond2, rCount);
        testResultForAdmin(pageablePage0Size10, cond3, rCount + r2Count + r3Count);
        testResultForAdmin(pageablePage0Size10, cond4, r3Count);
        // 조건 2
//        testResultForAdmin(pageablePage0Size10, cond21, rCount);
//        testResultForAdmin(pageablePage0Size10, cond22, rCount);
//        testResultForAdmin(pageablePage0Size10, cond23, rCount);
//        testResultForAdmin(pageablePage0Size10, cond24, rCount);
//        testResultForAdmin(pageablePage0Size10, cond25, rCount);
//        testResultForAdmin(pageablePage0Size10, cond26, r2Count);
        // 조건 3
//        testResultForAdmin(pageablePage0Size10, cond31, rCount);
//        testResultForAdmin(pageablePage0Size10, cond32, rCount);
//        testResultForAdmin(pageablePage0Size10, cond33, rCount);
//        testResultForAdmin(pageablePage0Size10, cond34, r2Count);

        // 조건 4
//        testResultForAdmin(pageablePage3Size5, cond41, rCount);


        // 검색에 대한 result가 없는 경우
//        testResultForAdmin(pageablePage0Size10, cond5, 0);
//        testResultForAdmin(pageablePage0Size10, cond6, 0);
//        testResultForAdmin(pageablePage0Size10, cond27, 0);
//        testResultForAdmin(pageablePage0Size10, cond35, 0);
//        testResultForAdmin(pageablePage2Size2, cond42, 0);

    }

    // test for 관리자 일 때
    private void testResultForAdmin(Pageable pageable, ItemSearchCond cond, int count) {
        Page<ItemListFormForAdmin> result = itemService.selectListForAdmin(cond, pageable);
        assertThat(result.getTotalElements()).isEqualTo(count);
    }

    /**
     * 상품 순위 정하기_확인
     * -> 좋아요수로 판별한다.
     */
    @Test
    @Rollback(value = false)
    public void 순위_정하기_확인() {
        // given
        // 10개의 상품 생성
        List<ItemPicture> itemPictureList = new ArrayList<>();
        itemPictureList.add(ItemPicture.createItemPicture("a", "a"));
        itemPictureList.add(ItemPicture.createItemPicture("a1", "a2"));

        FurnitureForm furnitureForm1 = new FurnitureForm(10, 2000, "책상", "잘 만들어졌어요~", itemPictureList, "hansem");
        Item item1 = Furniture.createFurniture(furnitureForm1, member);
        FurnitureForm furnitureForm2 = new FurnitureForm(10, 2000, "책상", "잘 만들어졌어요~", itemPictureList, "hansem");
        Item item2 = Furniture.createFurniture(furnitureForm2, member2);
        FurnitureForm furnitureForm3 = new FurnitureForm(10, 2000, "책상", "잘 만들어졌어요~", itemPictureList, "hansem");
        Item item3 = Furniture.createFurniture(furnitureForm3, member);
        FurnitureForm furnitureForm4 = new FurnitureForm(10, 2000, "책상", "잘 만들어졌어요~", itemPictureList, "hansem");
        Item item4 = Furniture.createFurniture(furnitureForm4, member2);
        FurnitureForm furnitureForm5 = new FurnitureForm(10, 2000, "책상", "잘 만들어졌어요~", itemPictureList, "hansem");
        Item item5 = Furniture.createFurniture(furnitureForm5, member);

        HomeAppliancesForm haForm1 = new HomeAppliancesForm(20, 2000, "냉장고", "잘 만들어졌어요~", itemPictureList, "samsug");
        Item item6 = HomeAppliances.createHomeAppliances(haForm1, member);
        HomeAppliancesForm haForm2 = new HomeAppliancesForm(20, 2000, "냉장고", "잘 만들어졌어요~", itemPictureList, "samsug");
        Item item7 = HomeAppliances.createHomeAppliances(haForm2, member3);
        HomeAppliancesForm haForm3 = new HomeAppliancesForm(20, 2000, "냉장고", "잘 만들어졌어요~", itemPictureList, "samsug");
        Item item8 = HomeAppliances.createHomeAppliances(haForm3, member3);

        BookForm bForm = new BookForm(5, 2000, "책1", "잘 만들어졌어요~", itemPictureList, "곰돌이푸", "곰돌이출판사");
        Item item9 = Book.createBook(bForm, member);

        ItemForm iForm = new ItemForm(10, 2000, "책상", "잘 만들어졌어요~", itemPictureList);
        Item item10 = Item.createItem(iForm, member);

        itemJpaRepository.save(item1);
        itemJpaRepository.save(item2);
        itemJpaRepository.save(item3);
        itemJpaRepository.save(item4);
        itemJpaRepository.save(item5);
        itemJpaRepository.save(item6);
        itemJpaRepository.save(item7);
        itemJpaRepository.save(item8);
        itemJpaRepository.save(item9);
        itemJpaRepository.save(item10);


        // when
        // 좋아요 생성(4, 6, 5, 1, 4, 3, 6, 0, 4, 1)
        Member[] members = {member, member2, member3, member4, member5, member6};
        Item[] items = {item1, item2, item3, item4, item5,
                item6, item7, item8, item9 ,item10};
        int[] counts = {4, 6, 5, 2, 4, 3, 6, 1, 4, 2}; // 좋아요수

        for(int i = 0; i < counts.length; i++) {
            chooseLikes(members, counts[i], items[i]);
        }

        // then
        // 순위 불러오기
        List<ItemRankForm> rankList = itemService.selectRankV2();
        // 순위만 List 만들기
        List<Integer> rankResultNumList = rankList.stream().map(rl -> rl.getNum()).collect(toList());

        // 예측 순위
        int[] expectedResult = {item2.getNum(), item7.getNum(), item3.getNum(), item1.getNum(), item5.getNum(),
                                item9.getNum(), item6.getNum(), item4.getNum(), item10.getNum(), item8.getNum()};
        // 순위 결과
        assertThat(rankResultNumList).containsExactly(item2.getNum(), item7.getNum(), item3.getNum(), item1.getNum(), item5.getNum(),
                                                      item9.getNum(), item6.getNum(), item4.getNum(), item10.getNum(), item8.getNum());
        // 같은 순위 비교하기
        // 6
        assertThat(rankList.get(0).getRank()).isEqualTo(rankList.get(1).getRank()).isEqualTo(1);
        // 4
        assertThat(rankList.get(3).getRank()).isEqualTo(rankList.get(4).getRank()).isEqualTo(rankList.get(5).getRank()).isEqualTo(4);
        // 2
        assertThat(rankList.get(7).getRank()).isEqualTo(rankList.get(8).getRank()).isEqualTo(8);
        // 1
        assertThat(rankList.get(9).getRank()).isEqualTo(10);
        // list 개수 알아보기
        assertThat(rankList.size()).isEqualTo(10);

    }

    // 좋아요를 여러번 수행하기
    private void chooseLikes(Member[] members, int count, Item items) {
        for(int i = 0; i < count; i++) {
            makeLikes(members[i], items);
        }
    }

    // 좋아요 만들기
    private void makeLikes(Member member, Item item) {
        ItemLikes itemLikes = ItemLikes.createItemLikes(member, item);
        itemLikesJpaRepository.save(itemLikes);
    }

    /**
     * 상품 순위 정하기_확인
     * -> 좋아요수로 판별한다.
     * -> item이 삭제된 경우
     */
    @Test
    @Rollback(value = false)
    public void 순위_정하기_item삭제_일어났을_때_확인() {
        // given
        // 10개의 상품 생성
        List<ItemPicture> itemPictureList = new ArrayList<>();
        itemPictureList.add(ItemPicture.createItemPicture("a", "a"));
        itemPictureList.add(ItemPicture.createItemPicture("a1", "a2"));

        FurnitureForm furnitureForm1 = new FurnitureForm(10, 2000, "책상", "잘 만들어졌어요~", itemPictureList, "hansem");
        Item item1 = Furniture.createFurniture(furnitureForm1, member);
        FurnitureForm furnitureForm2 = new FurnitureForm(10, 2000, "책상", "잘 만들어졌어요~", itemPictureList, "hansem");
        Item item2 = Furniture.createFurniture(furnitureForm2, member2);
        FurnitureForm furnitureForm3 = new FurnitureForm(10, 2000, "책상", "잘 만들어졌어요~", itemPictureList, "hansem");
        Item item3 = Furniture.createFurniture(furnitureForm3, member);
        FurnitureForm furnitureForm4 = new FurnitureForm(10, 2000, "책상", "잘 만들어졌어요~", itemPictureList, "hansem");
        Item item4 = Furniture.createFurniture(furnitureForm4, member2);
        FurnitureForm furnitureForm5 = new FurnitureForm(10, 2000, "책상", "잘 만들어졌어요~", itemPictureList, "hansem");
        Item item5 = Furniture.createFurniture(furnitureForm5, member);

        HomeAppliancesForm haForm1 = new HomeAppliancesForm(20, 2000, "냉장고", "잘 만들어졌어요~", itemPictureList, "samsug");
        Item item6 = HomeAppliances.createHomeAppliances(haForm1, member);
        HomeAppliancesForm haForm2 = new HomeAppliancesForm(20, 2000, "냉장고", "잘 만들어졌어요~", itemPictureList, "samsug");
        Item item7 = HomeAppliances.createHomeAppliances(haForm2, member3);
        HomeAppliancesForm haForm3 = new HomeAppliancesForm(20, 2000, "냉장고", "잘 만들어졌어요~", itemPictureList, "samsug");
        Item item8 = HomeAppliances.createHomeAppliances(haForm3, member3);

        BookForm bForm = new BookForm(5, 2000, "책1", "잘 만들어졌어요~", itemPictureList, "곰돌이푸", "곰돌이출판사");
        Item item9 = Book.createBook(bForm, member);

        ItemForm iForm = new ItemForm(10, 2000, "책상", "잘 만들어졌어요~", itemPictureList);
        Item item10 = Item.createItem(iForm, member);

        itemJpaRepository.save(item1);
        itemJpaRepository.save(item2);
        itemJpaRepository.save(item3);
        itemJpaRepository.save(item4);
        itemJpaRepository.save(item5);
        itemJpaRepository.save(item6);
        itemJpaRepository.save(item7);
        itemJpaRepository.save(item8);
        itemJpaRepository.save(item9);
        itemJpaRepository.save(item10);


        // when
        // 좋아요 생성(4, 6, 5, 1, 4, 3, 6, 0, 4, 1)
        Member[] members = {member, member2, member3, member4, member5, member6};
        Item[] items = {item1, item2, item3, item4, item5,
                item6, item7, item8, item9 ,item10};
        int[] counts = {4, 6, 5, 2, 4, 3, 6, 1, 4, 2}; // 좋아요수

        for(int i = 0; i < counts.length; i++) {
            chooseLikes(members, counts[i], items[i]);
        }

        // item5 삭제
        itemService.delete(item5.getNum());

        // 새로 불러오기
        // flush 없어도 item5의 status가 바뀌었네
        // 영속성 컨텍스트에 존재하는 것으로 보는 구나
        // betch size 확인
        em.flush();
        em.clear();

        // then
        // 순위 불러오기
        List<ItemRankForm> rankList = itemService.selectRankV2();
        // 순위만 List 만들기
        List<Integer> rankResultNumList = rankList.stream().map(rl -> rl.getNum()).collect(toList());

        // 예측 순위
        int[] expectedResult = {item2.getNum(), item7.getNum(), item3.getNum(), item1.getNum(),
                item9.getNum(), item6.getNum(), item4.getNum(), item10.getNum(), item8.getNum()};
        // 순위 결과
        assertThat(rankResultNumList).containsExactly(item2.getNum(), item7.getNum(), item3.getNum(), item1.getNum(),
                item9.getNum(), item6.getNum(), item4.getNum(), item10.getNum(), item8.getNum());
        // 같은 순위 비교하기
        // 6
        assertThat(rankList.get(0).getRank()).isEqualTo(rankList.get(1).getRank()).isEqualTo(1);
        // 4
        assertThat(rankList.get(3).getRank()).isEqualTo(rankList.get(4).getRank()).isEqualTo(4);
        // 2
        assertThat(rankList.get(6).getRank()).isEqualTo(rankList.get(7).getRank()).isEqualTo(7);
        // 1
        assertThat(rankList.get(8).getRank()).isEqualTo(9);
        // list 개수 알아보기
        assertThat(rankList.size()).isEqualTo(9);

    }

    /**
     * 상품 순위 정하기_확인
     * -> 좋아요수로 판별한다.
     * -> item이 삭제된 경우
     * -> fetchJoin 사용?
     */
    @Test
//    @Rollback(value = false)
    public void 순위_정하기_item삭제_일어났을_때_확인_fetchJoin_사용_확인() {
        // given
        // 10개의 상품 생성
        List<ItemPicture> itemPictureList1 = new ArrayList<>();
        itemPictureList1.add(ItemPicture.createItemPicture("a", "a"));
        itemPictureList1.add(ItemPicture.createItemPicture("a1", "a2"));

        List<ItemPicture> itemPictureList2 = new ArrayList<>();
        itemPictureList2.add(ItemPicture.createItemPicture("a", "a"));
        itemPictureList2.add(ItemPicture.createItemPicture("a1", "a2"));

        List<ItemPicture> itemPictureList3 = new ArrayList<>();
        itemPictureList3.add(ItemPicture.createItemPicture("a", "a"));
        itemPictureList3.add(ItemPicture.createItemPicture("a1", "a2"));

        List<ItemPicture> itemPictureList4 = new ArrayList<>();
        itemPictureList4.add(ItemPicture.createItemPicture("a", "a"));
        itemPictureList4.add(ItemPicture.createItemPicture("a1", "a2"));

        FurnitureForm furnitureForm1 = new FurnitureForm(10, 2000, "책상", "잘 만들어졌어요~", itemPictureList1, "hansem");
        Item item1 = Furniture.createFurniture(furnitureForm1, member);
        FurnitureForm furnitureForm2 = new FurnitureForm(10, 2000, "책상", "잘 만들어졌어요~", itemPictureList2, "hansem");
        Item item2 = Furniture.createFurniture(furnitureForm2, member2);
        FurnitureForm furnitureForm3 = new FurnitureForm(10, 2000, "책상", "잘 만들어졌어요~", new ArrayList<>(), "hansem");
        Item item3 = Furniture.createFurniture(furnitureForm3, member);
        FurnitureForm furnitureForm4 = new FurnitureForm(10, 2000, "책상", "잘 만들어졌어요~", itemPictureList3, "hansem");
        Item item4 = Furniture.createFurniture(furnitureForm4, member2);
        FurnitureForm furnitureForm5 = new FurnitureForm(10, 2000, "책상", "잘 만들어졌어요~", new ArrayList<>(), "hansem");
        Item item5 = Furniture.createFurniture(furnitureForm5, member);

        HomeAppliancesForm haForm1 = new HomeAppliancesForm(20, 2000, "냉장고", "잘 만들어졌어요~", new ArrayList<>(), "samsug");
        Item item6 = HomeAppliances.createHomeAppliances(haForm1, member);
        HomeAppliancesForm haForm2 = new HomeAppliancesForm(20, 2000, "냉장고", "잘 만들어졌어요~", itemPictureList4, "samsug");
        Item item7 = HomeAppliances.createHomeAppliances(haForm2, member3);
        HomeAppliancesForm haForm3 = new HomeAppliancesForm(20, 2000, "냉장고", "잘 만들어졌어요~", new ArrayList<>(), "samsug");
        Item item8 = HomeAppliances.createHomeAppliances(haForm3, member3);

        BookForm bForm = new BookForm(5, 2000, "책1", "잘 만들어졌어요~", new ArrayList<>(), "곰돌이푸", "곰돌이출판사");
        Item item9 = Book.createBook(bForm, member);

        ItemForm iForm = new ItemForm(10, 2000, "책상", "잘 만들어졌어요~", new ArrayList<>());
        Item item10 = Item.createItem(iForm, member);

        itemJpaRepository.save(item1);
        itemJpaRepository.save(item2);
        itemJpaRepository.save(item3);
        itemJpaRepository.save(item4);
        itemJpaRepository.save(item5);
        itemJpaRepository.save(item6);
        itemJpaRepository.save(item7);
        itemJpaRepository.save(item8);
        itemJpaRepository.save(item9);
        itemJpaRepository.save(item10);


        // when
        // 좋아요 생성(4, 6, 5, 1, 4, 3, 6, 0, 4, 1)
        Member[] members = {member, member2, member3, member4, member5, member6};
        Item[] items = {item1, item2, item3, item4, item5,
                item6, item7, item8, item9 ,item10};
        int[] counts = {4, 6, 5, 2, 4, 3, 6, 1, 4, 2}; // 좋아요수

        for(int i = 0; i < counts.length; i++) {
            chooseLikes(members, counts[i], items[i]);
        }

        // item5 삭제
        itemService.delete(item5.getNum());

        // 새로 불러오기
        // flush 없어도 item5의 status가 바뀌었네
        // 영속성 컨텍스트에 존재하는 것으로 보는 구나
        // betch size 확인
        em.flush();
        em.clear();

        // then
        // 순위 불러오기
        List<ItemRankForm> rankList = itemService.selectRank();
        // 순위만 List 만들기
        List<Integer> rankResultNumList = rankList.stream().map(rl -> rl.getNum()).collect(toList());

        // 예측 순위
        int[] expectedResult = {item2.getNum(), item7.getNum(), item3.getNum(), item1.getNum(),
                item9.getNum(), item6.getNum(), item4.getNum(), item10.getNum(), item8.getNum()};
        // 순위 결과
        assertThat(rankResultNumList).containsExactly(item2.getNum(), item7.getNum(), item3.getNum(), item1.getNum(),
                item9.getNum(), item6.getNum(), item4.getNum(), item10.getNum(), item8.getNum());
        // 같은 순위 비교하기
        // 6
        assertThat(rankList.get(0).getRank()).isEqualTo(rankList.get(1).getRank()).isEqualTo(1);
        // 4
        assertThat(rankList.get(3).getRank()).isEqualTo(rankList.get(4).getRank()).isEqualTo(4);
        // 2
        assertThat(rankList.get(6).getRank()).isEqualTo(rankList.get(7).getRank()).isEqualTo(7);
        // 1
        assertThat(rankList.get(8).getRank()).isEqualTo(9);
        // list 개수 알아보기
        assertThat(rankList.size()).isEqualTo(9);
    }




}
