package shop.wesellbuy.secondproject.service.likes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.wesellbuy.secondproject.domain.Item;
import shop.wesellbuy.secondproject.domain.Member;
import shop.wesellbuy.secondproject.domain.likes.ItemLikes;
import shop.wesellbuy.secondproject.repository.item.ItemJpaRepository;
import shop.wesellbuy.secondproject.repository.likes.ItemLikesJpaRepository;
import shop.wesellbuy.secondproject.repository.member.MemberJpaRepository;
import shop.wesellbuy.secondproject.web.likes.ItemLikesListForm;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * ItemLikes Service 구현 클래스
 * writer : 이호진
 * init : 2023.02.01
 * updated by writer : 이호진
 * update : 2023.03.14
 * description : ItemLikes Service 구현 메소드 모음
 *
 * update : 상품 좋아요 삭제 by itemNum and memberNum 추가
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ItemLikesServiceImpl implements ItemLikesService{

    private final ItemLikesJpaRepository itemLikesJpaRepository;
    private final ItemJpaRepository itemJpaRepository;
    private final MemberJpaRepository memberJpaRepository;

    /**
     * writer : 이호진
     * init : 2023.02.01
     * updated by writer :
     * update :
     * description : 상품 좋아요 저장
     */
    @Override
    @Transactional
    public int save(int itemNum, int memberNum) {
        // item 불러오기
        Item item = itemJpaRepository.findById(itemNum).orElseThrow();
        // member 불러오기
        Member member = memberJpaRepository.findById(memberNum).orElseThrow();
        // 좋아요 만들기
        ItemLikes itemLikes = ItemLikes.createItemLikes(member, item);
        // 저장하기
        itemLikesJpaRepository.save(itemLikes);

        return itemLikes.getNum();
    }

//    /**
//     * writer : 이호진
//     * init : 2023.02.01
//     * updated by writer :
//     * update :
//     * description : 상품 좋아요 삭제
//     */
//    @Override
//    @Transactional
//    public void delete(int num) {
//        // 좋아요 불러오기
//        ItemLikes findItemLikes = itemLikesJpaRepository.findById(num).orElseThrow();
//        // 좋아요 삭제하기
//        itemLikesJpaRepository.delete(findItemLikes);
//    }

    /**
     * writer : 이호진
     * init : 2023.03.14
     * updated by writer :
     * update :
     * description : 상품 좋아요 삭제 by itemNum and memberNum
     */
    @Override
    @Transactional
    public void delete(int itemNum, int memberNum) {
        // 좋아요 불러오기
        ItemLikes findItemLikes = itemLikesJpaRepository.findByItemNumAndMemberNum(itemNum, memberNum).orElseThrow();
        // 좋아요 삭제하기
        itemLikesJpaRepository.delete(findItemLikes);
    }

    /**
     * writer : 이호진
     * init : 2023.02.01
     * updated by writer :
     * update :
     * description : 모든 상품 좋아요 불러오기 by memberNum
     *               > 좋아요 개수 알 수 있다.
     *               > 의도하지 않은 로직
     *                    >(2/13 지금 봤을 때 필요없는 메서드 같아서 사용 안함)
     */
    @Override
    public List<ItemLikesListForm> selectList(int memberNum) {
        // 모든 좋아요 불러오기
        List<ItemLikes> itemLikesList = itemLikesJpaRepository.findAllInfoById(memberNum);
        List<ItemLikesListForm> result = itemLikesList.stream()
                .map(l -> ItemLikesListForm.create(l.getNum()))
                .collect(toList());

        return result;
    }

    /**
     * writer : 이호진
     * init : 2023.02.13
     * updated by writer :
     * update :
     * description : 로그인한 회원의 모든 상품 좋아요 불러오기 by memberNum
     *               > 좋아요 색깔 표시할지 말지 결정
     */
    @Override
    public List<Integer> selectListForItemList(int memberNum) {
        // 모든 좋아요 불러오기
        List<ItemLikes> itemLikesList = itemLikesJpaRepository.findAllInfoById(memberNum);
        List<Integer> result = itemLikesList.stream()
                .map(l -> l.getItem().getNum())
                .collect(toList());

        return result;
    }














}
