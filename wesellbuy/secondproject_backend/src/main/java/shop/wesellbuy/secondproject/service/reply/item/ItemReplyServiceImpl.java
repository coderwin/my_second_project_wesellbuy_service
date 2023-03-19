package shop.wesellbuy.secondproject.service.reply.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.wesellbuy.secondproject.domain.Item;
import shop.wesellbuy.secondproject.domain.Member;
import shop.wesellbuy.secondproject.domain.reply.ItemReply;
import shop.wesellbuy.secondproject.repository.item.ItemJpaRepository;
import shop.wesellbuy.secondproject.repository.member.MemberJpaRepository;
import shop.wesellbuy.secondproject.repository.reply.item.ItemReplyJpaRepository;
import shop.wesellbuy.secondproject.repository.reply.item.ItemReplySearchCond;
import shop.wesellbuy.secondproject.web.reply.ReplyDetailForm;
import shop.wesellbuy.secondproject.web.reply.ReplyForm;
import shop.wesellbuy.secondproject.web.reply.ReplyUpdateForm;

/**
 * ItemReply Service 구현 클래스
 * writer : 이호진
 * init : 2023.02.01
 * updated by writer :
 * update :
 * description : ItemReply Service 구현 메소드 모음
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ItemReplyServiceImpl implements ItemReplyService {

    private final MemberJpaRepository memberJpaRepository;
    private final ItemJpaRepository itemJpaRepository;
    private final ItemReplyJpaRepository itemReplyJpaRepository;

    /**
     * writer : 이호진
     * init : 2023.02.01
     * updated by writer :
     * update :
     * description : 댓글 저장
     */
    @Override
    @Transactional
    public int save(ReplyForm replyForm, int memberNum, int itemNum) {
        // member 불러오기
        Member member = memberJpaRepository.findById(memberNum).orElseThrow();
        // item 불러오기
        Item item = itemJpaRepository.findById(itemNum).orElseThrow();
        // 댓글 생성
        ItemReply itemReply = ItemReply.createItemReply(replyForm, member, item);
        // 댓글 저장하기
        itemReplyJpaRepository.save(itemReply);

        return itemReply.getNum();
    }

    /**
     * writer : 이호진
     * init : 2023.02.01
     * updated by writer :
     * update :
     * description : 댓글 수정
     */
    @Override
    @Transactional
    public void update(ReplyUpdateForm updateReplyForm) {
        // 댓글 불러오기
        ItemReply findItemReply = itemReplyJpaRepository.findById(updateReplyForm.getNum()).orElseThrow();
        // 댓글 수정
        findItemReply.updateItemReply(updateReplyForm);
    }

    /**
     * writer : 이호진
     * init : 2023.02.01
     * updated by writer :
     * update :
     * description : 댓글 삭제
     *               status를 변경한다.(R -> D)
     */
    @Override
    @Transactional
    public void delete(int num) {
        // 댓글 불러오기
        ItemReply itemReply = itemReplyJpaRepository.findById(num).orElseThrow();
        // 댓글 삭제하기
        // status를 변경한다.(R -> D)
        itemReply.delete();
    }

//    -------------------------methods using for admin start----------------------------------

    /**
     * writer : 이호진
     * init : 2023.02.01
     * updated by writer :
     * update :
     * description : 모두 불러오기
     *               -> admin이 사용
     */
    @Override
    public Page<ReplyDetailForm> selectListForAdmin(ItemReplySearchCond cond, Pageable pageable) {
        // 모두 불러오기
        Page<ItemReply> itemReplyPage = itemReplyJpaRepository.findAllInfo(cond, pageable);
        // Page<ReplyDetailForm> 객체로 만들기
        // 모든 상태 포함
        Page<ReplyDetailForm> result = itemReplyPage.map(r -> ReplyDetailForm.create(r));

        return result;
    }

//    -------------------------methods using for admin admin----------------------------------







}
