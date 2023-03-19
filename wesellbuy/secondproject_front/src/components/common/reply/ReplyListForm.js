import React from 'react'
import { ListGroup } from 'react-bootstrap'
import ReplyForm from './ReplyForm'

/**
 * reply list component
 * writer : 이호진
 * init : 2023.03.07
 * updated by writer :
 * update :
 * description : 댓글 리스트 component
 *               > props
 *                 - replyFormList : 댓글 목록 데이터
 *                 - replies: 댓글 목록 담은 상태 -> 왜 못 읽어 올까?
 *                 - setReplies: replies 상태 변경 함수
 */
const ReplyListForm = ({replyFormList, replies, setReplies, updateReply, deleteReply}) => {


  /// 메서드 모음
  // ReplyForm에서 삭제 버튼 눌렀을 때
  // replyFormList에서 reply 삭제하기
  function handleDeleteRepliesChange(replyNum) {
    console.log(`replyNum : ${replyNum}`);
    console.log(replyNum);
    // 번호와 일치하는 replyNum을 지워준다.
    setReplies(
      replies.filter((reply) => reply.num !== replyNum)
    );
  }
  

  // 여기서는 루프로 댓글하나하나 나열하기
    // 댓글 한개씩은 폼을 만들기
  let view = "";// view를 담는 변수
  // replyFormList가 undefined가 아닐 때
  if(replyFormList.length !== 0) {
    view = replyFormList.map((reply) => {
      return <ReplyForm 
        key={reply.num} 
        OnDeleteRepliesChange={handleDeleteRepliesChange} 
        reply={reply} 
        updateReply={updateReply} 
        deleteReply={deleteReply} />;
    });
  }

  return (
    <>
      {/* 댓글 하나하나 component 생성하는 Form 만들기 */}
      <ListGroup>
        {view}
      </ListGroup>
    </>
  )
}

export default ReplyListForm