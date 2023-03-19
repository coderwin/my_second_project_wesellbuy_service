package shop.wesellbuy.secondproject.service.item;

import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import shop.wesellbuy.secondproject.domain.Item;
import shop.wesellbuy.secondproject.domain.Member;
import shop.wesellbuy.secondproject.domain.item.Book;
import shop.wesellbuy.secondproject.domain.item.Furniture;
import shop.wesellbuy.secondproject.domain.item.HomeAppliances;
import shop.wesellbuy.secondproject.domain.item.ItemPicture;
import shop.wesellbuy.secondproject.exception.item.NotExistingItemTypeException;
import shop.wesellbuy.secondproject.repository.item.ItemJpaRepository;
import shop.wesellbuy.secondproject.repository.item.ItemSearchCond;
import shop.wesellbuy.secondproject.repository.likes.ItemLikesJpaRepository;
import shop.wesellbuy.secondproject.repository.member.MemberJpaRepository;
import shop.wesellbuy.secondproject.web.item.*;

import java.io.IOException;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static shop.wesellbuy.secondproject.domain.QItem.item;
import static shop.wesellbuy.secondproject.domain.QMember.member;
import static shop.wesellbuy.secondproject.domain.likes.QItemLikes.itemLikes;

/**
 * Item Service 구현 클래스
 * writer : 이호진
 * init : 2023.02.02
 * updated by writer :
 * update :
 * description : Item Service 구현 메소드 모음
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemJpaRepository itemJpaRepository;
    private final FileStoreOfItemPicture fileStore;
    private final MemberJpaRepository memberJpaRepository;
    private final ItemLikesJpaRepository itemLikesJpaRepository;

    /**
     * writer : 이호진
     * init : 2023.02.02
     * updated by writer :
     * update :
     * description : 상품 저장
     *               -> 상품이름 중복 상관 없음
     */
    @Override
    @Transactional
    public int save(ItemOriginalForm itemOriginalForm, List<MultipartFile> files, int memberNum) throws IOException {
        // 상품 이미지 서버 컴퓨터에 저장하기
        List<ItemPicture> itemPictureList = fileStore.storeFiles(files);
        // 생성된 이미지 list를 itemOriginalForm에 담기
        itemOriginalForm.addItemPictureList(itemPictureList);
        // 회원 불러오기
        Member member = memberJpaRepository.findById(memberNum).orElseThrow();
        // 상품 종류(type)에 따라 저장 객체 정하기
        Item item = makeItemByType(itemOriginalForm, member);
        // 상품 저장
        itemJpaRepository.save(item);

        return item.getNum();
    }

    /**
     * writer : 이호진
     * init : 2023.02.02
     * updated by writer :
     * update :
     * description : 상품 종류(type)에 따라 상품 객체 생성하기
     */
    private Item makeItemByType(ItemOriginalForm itemOriginalForm, Member member) {

        Item item = null;

        switch(itemOriginalForm.getType()) {
            case "B": BookForm bookForm = BookForm.create(itemOriginalForm);
                      item = Book.createBook(bookForm, member);
                      break;
            case "HA": HomeAppliancesForm homeAppliancesForm = HomeAppliancesForm.create(itemOriginalForm);
                       item = HomeAppliances.createHomeAppliances(homeAppliancesForm, member);
                       break;
            case "F": FurnitureForm furnitureForm = FurnitureForm.create(itemOriginalForm);
                      item = Furniture.createFurniture(furnitureForm, member);
                      break;
            case "ITEM": ItemForm itemForm = ItemForm.create(itemOriginalForm);
                         item = Item.createItem(itemForm, member);
                         break;
            // 상품 종류가 없을 때
            default: String errMsg = "상품 종류를 다시 선택해주세요";
                     throw new NotExistingItemTypeException(errMsg);
        }
        return item;
    }

    /**
     * writer : 이호진
     * init : 2023.02.02
     * updated by writer :
     * update :
     * description : 상품 수정
     *
     * comment : 강제 type 변환 말고 다른 방법이 있을까?
     */
    @Override
    @Transactional
    public void update(ItemUpdateForm itemUpdateForm, List<MultipartFile> files) throws IOException {
        // 상품 불러오기
        Item item = itemJpaRepository.findById(itemUpdateForm.getNum()).orElseThrow();
        // 이미지 서버 컴퓨터에 저장하기
        List<ItemPicture> newPictures = fileStore.storeFiles(files);
        // 생성된 이미지 list를 itemUpdateForm에 담기
        itemUpdateForm.addItemPictureList(newPictures);
        // type에 따라 다른 수정 메서드로 이동한다.(수정하기)
        changeTypeAndUpdateByType(itemUpdateForm, item);
    }

    /**
     * writer : 이호진
     * init : 2023.02.02
     * updated by writer :
     * update :
     * description : type에 따라 다른 수정 메서드로 이동한다.(수정하기)
     */
    private void changeTypeAndUpdateByType(ItemUpdateForm itemUpdateForm, Item item) {
        String type = itemUpdateForm.getType();// 상품 type

        if(type.equals("B")) {
            Book book = (Book) item;
            book.update(itemUpdateForm);
        } else if(type.equals("HA")) {
            HomeAppliances homeAppliances = (HomeAppliances) item;
            homeAppliances.update(itemUpdateForm);
        } else if(type.equals("F")) {
            Furniture furniture = (Furniture) item;
            furniture.update(itemUpdateForm);
        } else if(type.equals("ITEM")) {
            item.update(itemUpdateForm);

        // 상품 종류가 없을 때 예외 발생
        } else {
            String errMsg = "상품 종류를 다시 선택해주세요";
            throw new NotExistingItemTypeException(errMsg);
        }
    }

    /**
     * writer : 이호진
     * init : 2023.02.02
     * updated by writer :
     * update :
     * description : 상품 삭제
     *               -> status 상태를 변경한다(R -> D)
     */
    @Override
    @Transactional
    public void delete(int num) {
        // 상품 불러오기
        Item item = itemJpaRepository.findById(num).orElseThrow();
        // 상품 상태 변경(R -> D)
        item.changeStatus();
    }

    /**
     * writer : 이호진
     * init : 2023.02.02
     * updated by writer : 이호진
     * update : 2023.03.14
     * description : 상품 상세보기
     */
    @Override
    @Transactional
    public ItemDetailForm watchDetail(int num) {
        // 조회수 1 증가 시킨다.
        updateHits(num);
        // 상품 상세보기 불러오기
        Item item = itemJpaRepository.findDetailInfoById(num).orElseThrow();
        // dtype에 따라 상품 타입 변경 and ItemDetailForm으로 변경하기
        ItemDetailForm itemDetailForm = changeTypeAndMakeItemDetailFormByDtype(item);

        return itemDetailForm;
    }

    /**
     * writer : 이호진
     * init : 2023.02.02
     * updated by writer :
     * update :
     * description : dtype에 따라 상품 타입 변경
     *               + ItemDetailForm으로 변경하기
     */
    private ItemDetailForm changeTypeAndMakeItemDetailFormByDtype(Item item) {

        ItemDetailForm itemDetailForm = null;
        String type = item.getDtype();

        if(type.equals("B")) {
            Book book = (Book) item;
            itemDetailForm = ItemDetailForm.create(book);
        } else if(type.equals("F")) {
            Furniture furniture = (Furniture) item;
            itemDetailForm = ItemDetailForm.create(furniture);
        } else if(type.equals("HA")) {
            HomeAppliances homeAppliances = (HomeAppliances) item;
            itemDetailForm = ItemDetailForm.create(homeAppliances);
        } else if(type.equals("ITEM")) {
            itemDetailForm = ItemDetailForm.create(item);
        }
        return itemDetailForm;
    }

    /**
     * writer : 이호진
     * init : 2023.02.02
     * updated by writer :
     * update :
     * description : 상품 조회수 1 증가
     *               -> 상세보기 클릭시 실행된다.
     */
    private void updateHits(int num) {
        // 상품 불러오기
        Item item = itemJpaRepository.findById(num).orElseThrow();
        // 조회수 1 증가 시키기
        item.changeHits();
    }

    /**
     * writer : 이호진
     * init : 2023.02.02
     * updated by writer :
     * update :
     * description : 상품 이미지 삭제
     *               -> status 상태를 변경한다(R -> D)
     *
     * comment : 확인해보기
     *           -> betch size 작동하는가?
     */
    @Override
    @Transactional
    public void deletePicture(int itemNum, int pictureNum) {
        // 상품 불러오기
        Item item = itemJpaRepository.findById(itemNum).orElseThrow();
        // picture 삭제한다.
        item.deletePicture(pictureNum);
    }

    /**
     * writer : 이호진
     * init : 2023.02.02
     * updated by writer : 이호진
     * update : 2023.03.16
     * description : 상품 순위 불러오기
     *               -> 좋아요수가 높은 순으로
     *
     * comment : repository에서 select(item)에 문제가 있을 것으로 예상()
     *           -> Dto로 해결해볼 것을 생각
     *           -> Dto 말고는 없을까?
     *
     *           -> itemLikes에서 fetch join 사용 못하는 것 말고는 문제 없음
     *              -> itemLikes만 fetchjoin을 왜 사용 못하지?
     *                  -> foreign key만 있어서 그런 것 같다.
     *
     * update : 순위 입력시 rankList에 값이 있을 때만 입력한다.
     */
    @Override
    public List<ItemRankForm> selectRank() {
        // 상품 순위 불러오기
        List<Item> itemRankList = itemLikesJpaRepository.findRankV4();
        // ItemRankForm으로 만들기
        List<ItemRankForm> rankList = itemRankList.stream()
                .map(i -> ItemRankForm.create(i))
                .collect(toList());
        // 순위 입력하기(결정하기)
        if(rankList.size() != 0) {
            inputRank(rankList);
        }
        return rankList;
    }

    /**
     * writer : 이호진
     * init : 2023.02.02
     * updated by writer :
     * update :
     * description : 상품 순위(rank)를 결정한다.
     *               -> sql의 rank함수로 만든다.
     *               -> 1,1,3,4,5,5,7...... 순위가 이렇게 나오게
     */
    private void inputRank(List<ItemRankForm> rankList) {
        Long max = rankList.get(0).getLikes();
        int rank = 1; // 1번부터 rankList 수까지
        int i = 1; // 차례(1, 2, 3, ...)를 정한다.
        for(ItemRankForm form : rankList) {

            if(form.getLikes() == max) {
                form.addRank(rank);
                // rank는 같다.
                i++;
                continue;
            } else if(form.getLikes() < max) {
                rank = i;
                form.addRank(rank);
                max = form.getLikes();
                i++;
            }
        }
    }

    /**
     * writer : 이호진
     * init : 2023.02.03
     * updated by writer :
     * update :
     * description : 상품 순위 불러오기 V2
     *               -> 좋아요수가 높은 순으로
     *               -> Tuple 이용
     *
     * comment : Qclass를 써도 될까?(서비스에서?)
     *           -> 안 쓰고 순위대로 불러오는 방법이 있을까?
     *           -> DTO로 조회하는 것 밖에 없나?
     *              -> DTO로 조회시, 사진 list는 어떻게 할 것인가?
     *                  -> N + 1문제 발생하지 않나?
     *                  -> 불러와지지도 않을까?
     *           -> test 결과
     *              -> 영속성에 객체들이 있어서
     *              -> N + 1 문제 발생 안 함
     *              -> 불러와짐
     *              -> 쿼리는 한번만 불러와진다.
     *                  -> 다른 List<>객체를 불러오면 어떻게 될지는 모르겠다.
     */
    @Override
    public List<ItemRankForm> selectRankV2() {
        // 상품 순위 불러오기
        List<Tuple> tupleList = itemLikesJpaRepository.findRank();
        // ItemRankForm으로 만들기
        List<ItemRankForm> rankList = tupleList.stream()
                .map(t -> ItemRankForm.create(
                        t.get(itemLikes.count()),
                        t.get(item),
                        t.get(member)
                ))
                .collect(toList());
        // 순위 입력하기(결정하기)
        inputRank(rankList);

        return rankList;
    }

    /**
     * writer : 이호진
     * init : 2023.02.02
     * updated by writer : 이호진
     * update : 2023.02.01
     * description : 상품 모두 불러오기
     *               -> status 상태(D)는 출력 안 한다.
     */
    @Override
    public Page<ItemListForm> selectList(ItemSearchCond cond, Pageable pageable) {
        // 조건에 맞는 상품 불러오기
        Page<Item> itemPage = itemJpaRepository.findAllInfo(cond, pageable);
        // ItemListForm에 정보담기
        // Page 만들기
        Page<ItemListForm> result = itemPage.map(i -> ItemListForm.create(i));

        return result;
    }

//    -------------------------methods using for admin start----------------------------------

    /**
    * writer : 이호진
    * init : 2023.02.02
    * updated by writer :
    * update :
    * description : 상품 모두 불러오기
    *               -> status 사용
    *               -> admin이 사용한다.
    */
    @Override
    public Page<ItemListFormForAdmin> selectListForAdmin(ItemSearchCond cond, Pageable pageable) {
        // 조건에 맞는 상품 불러오기
        Page<Item> itemPage = itemJpaRepository.findAllInfoForAdmin(cond, pageable);
        // ItemListFormFormAdmin에 정보담기
        // Page 만들기
        Page<ItemListFormForAdmin> result = itemPage.map(i -> ItemListFormForAdmin.create(i));

        return result;
    }

//    -------------------------methods using for admin end----------------------------------

}
