import axios from 'axios';
import React, { useState } from 'react'
import ReplyListForm from '../../common/reply/ReplyListForm';
import ReplyRecommendationWritingForm from './ReplyRecommendationWritingForm';

/**
 * recommendation reply component
 * writer : 이호진
 * init : 2023.03.10
 * updated by writer :
 * update :
 * description : 댓글 component
 * 
 * props
 *  -> replyFormList : recommendation의 댓글 list 
 */
const ReplyRecommendationBoxForm = ({replyFormList}) => {

  /// 상태 모음
  /// 상태 모음
  const [replies, setReplies] = useState(replyFormList);// 댓글 모음 상태

  //// 메서드 모음
  /// ReplyForm 에서 사용
  // 서버로 댓글 수정 데이터 보내기
  async function updateReply(boardNum, replyNum, content) {
    return await axios.put(
      `http://52.79.48.234:8080/recommendations/${boardNum}/replies/${replyNum}`,
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
      `http://52.79.48.234:8080/recommendations/${boardNum}/replies/${replyNum}`,
      {
        withCredentials: true
      }
    );
  }
  /// ReplyWritingForm 에서 사용
  // 서버에 댓글 등록 요청하기
  async function saveReply(boardNum, data) {
    return await axios.post(
      `http://52.79.48.234:8080/recommendations/${boardNum}/replies`,
      data,
      {
        withCredentials: true
      }
    );
  }

  return (
    <>
      <ReplyListForm 
        replyFormList={replyFormList} 
        replies={replies} 
        setReplies={setReplies} 
        updateReply={updateReply} 
        deleteReply={deleteReply} />
      <ReplyRecommendationWritingForm 
        saveReply={saveReply} 
      />   
    </>
  )
}

export default ReplyRecommendationBoxForm;