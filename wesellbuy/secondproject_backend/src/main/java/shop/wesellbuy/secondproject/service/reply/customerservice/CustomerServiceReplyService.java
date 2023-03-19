package shop.wesellbuy.secondproject.service.reply.customerservice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.wesellbuy.secondproject.repository.reply.customerservice.CustomerServiceReplySearchCond;
import shop.wesellbuy.secondproject.web.reply.ReplyDetailForm;
import shop.wesellbuy.secondproject.web.reply.ReplyForm;
import shop.wesellbuy.secondproject.web.reply.ReplyUpdateForm;

/**
 * CustomerServiceReply Service
 * writer : 이호진
 * init : 2023.02.01
 * updated by writer :
 * update :
 * description : CustomerServiceReply Service 메소드 모음
 */
public interface CustomerServiceReplyService {

    /**
     * writer : 이호진
     * init : 2023.02.01
     * updated by writer :
     * update :
     * description : 댓글 저장
     */
    int save(ReplyForm replyForm, int memberNum, int customerServiceNum);

    /**
     * writer : 이호진
     * init : 2023.02.01
     * updated by writer :
     * update :
     * description : 댓글 수정
     */
    void update(ReplyUpdateForm updateReplyForm);

    /**
     * writer : 이호진
     * init : 2023.02.01
     * updated by writer :
     * update :
     * description : 댓글 삭제
     *               status를 변경한다.(R -> D)
     */
    void delete(int num);

//    -------------------------methods using for admin start----------------------------------

    /**
     * writer : 이호진
     * init : 2023.02.01
     * updated by writer :
     * update :
     * description : 모두 불러오기
     *               -> admin이 사용
     */
    Page<ReplyDetailForm> selectListForAdmin(CustomerServiceReplySearchCond cond, Pageable pageable);

//    -------------------------methods using for admin admin----------------------------------

}
