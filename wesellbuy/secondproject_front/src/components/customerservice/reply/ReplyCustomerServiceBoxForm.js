import axios from 'axios';
import React, { useEffect, useState } from 'react'
import ReplyCustomerServiceWritingForm from './ReplyCustomerServiceWritingForm';
import ReplyListForm from '../../common/reply/ReplyListForm';

/**
 * recommendation reply component
 * writer : 이호진
 * init : 2023.03.1
 * updated by writer :
 * update :
 * description : 댓글 component
 * 
 * props
 *  -> replyFormList : customerService의 댓글 list 
 */
const ReplyCustomerServiceBoxForm = ({replyFormList}) => {

  /// 상태 모음
  /// 상태 모음
  const [replies, setReplies] = useState(replyFormList);// 댓글 모음 상태

  //// 메서드 모음
  /// ReplyForm 에서 사용
  // 서버로 댓글 수정 데이터 보내기
  async function updateReply(boardNum, replyNum, content) {
    return await axios.put(
      `http://localhost:8080/customerservices/${boardNum}/replies/${replyNum}`,
      {
        content: content
      },
      {
        withCredentials: true
      }
    );
  }
  // 서버로 댓글 삭제 요청 보내기
  async function deleteReply(boardNum, replyNum) {
    return await axios.delete(
      `http://localhost:8080/customerservices/${boardNum}/replies/${replyNum}`,
      {
        withCredentials: true
      }
    );
  }
  /// ReplyWritingForm 에서 사용
  // 서버에 댓글 등록 요청하기
  async function saveReply(boardNum, data) {
    return await axios.post(
      `http://localhost:8080/customerservices/${boardNum}/replies`,
      data,
      {
        withCredentials: true
      }
    );
  }
  // replies에 데이터 입력 => 보류
  // set으로 받아도 default데이터가 replyFormList라서 그런 것 같다
  function addReplies(reply) {
    
    // const newReplies = replies.unshipt(reply);
    // 새로운 reply 만들기
    setReplies((replies) => {
      return [
        reply,
        ...replies
      ];
    });
  }

  

  return (
    <>
      <ReplyListForm 
        replyFormList={replyFormList} 
        replies={replies} 
        setReplies={setReplies} 
        updateReply={updateReply} 
        deleteReply={deleteReply} />
      <ReplyCustomerServiceWritingForm 
        saveReply={saveReply}
        addReplies={addReplies}
      />   
    </>
  )
}

export default ReplyCustomerServiceBoxForm;