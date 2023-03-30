import axios from "axios";
import { useContext, useState } from "react";
import { Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { CustomContext } from "../../../App";
import Loding from "../../Loding";

/**
 * Recommendation of Recommendation box component
 * writer : 이호진
 * init : 2023.03.28
 * updated by writer :
 * update :
 * description : 추천합니다 목록의 게시글 관리자용 component
 *               props
 *                 > data : 게시글 데이터 prop
 *                 > searchCond : 추천합니다 목록 찾기 위한 조건 데이터 prop
 *                 > numPosition : 현재 페이지에서 추천합니다목록의 자리선정위한 번호 prop
 *                 > totalPages : 전체 페이지수 prop
 *                 > datasLength : 데이터 총 개수
 */
const RecommendationForAdminForm = ({data, numPosition, datasLength, totalPages, searchCond}) => {
  
  /// 변수 모음
  const navigation = useNavigate();// navigation
  const boardStatusValues = ["R", "D"];// 게시글 상태 values 모음
  const boardStatusNames = ["등록", "삭제"];// 게시글 상태 names 모음
  const {serverHost} = useContext(CustomContext);

  /// 상태 모음
  const [loding, setLoding] = useState(false);// 요청처리 상태
  const [deleteResult, setDeleteResult] = useState(false);// 삭제버튼 클릭 후 처리 상태 

  /// 메서드 모음
  // 게시글을 클릭했을 때
  function handleItemNameClick(e) {
    // id 불러오기
    const id = e.target.id;// 게시글번호 불러오기
    // 추천합니다 상세보기로 이동
    navigation(`/recommendation/${id}`);
  }
  // 삭제버튼을 클릭했을 때
  async function handleCancelClick(e) {
    // 삭제할건지 물어보기
    const result = window.confirm("정말로 삭제하시겠습니까?");
    // result === true
    if(result) {
      setLoding(true);
      try {
        // 게시글 번호
        const boardNum = e.target.id
        // 서버로 취소 요청
        const response = await cancel(boardNum);
        // 요청 성공
        // loding = false
        setLoding(false);
        // console.log("요청 성공");
        alert(response.data.data);
        // cancelResult 변경
        setDeleteResult(true);
      } catch(err) {
        // 요청 실패
        // console.log("요청 실패");
        // loding = false
        setLoding(false);
        // 에러 메시지 보여주기
        if(err.response.data.errMsg) {
          alert(err.response.data.errMsg);
        }
        // // DeleteResult 그대로 두기
      }
    }
  }
  // 서버로 삭제 요청
  async function cancel(num) {
    return await axios.delete(
      `${serverHost}:8080/recommendations/${num}`,
      {
        withCredentials: true
      }
    );
  }
  // // 시간 -> 날짜만 나오게 하기
  // function printDate(datetime) {
  //   // 날짜시간 받아서 
  //     // T부분에서 cut
  //   const datetimeArr = datetime.split("T");
  //   return datetimeArr[0];
  // }

  /// view 모음
  let boardStatusView = null;// 게시글 상태 출력 변수
  let deleteBtnBoxView = null;// 삭제버튼 박스 출력 변수

  // boardStatusView 결정
  // 처음 and 삭제요청실패
  if(deleteResult === false) {
    for(let i = 0; i < boardStatusValues.length; i++) {
      if(boardStatusValues[i] === data.status) {
        boardStatusView = boardStatusNames[i];
      }
    }
  // 삭제요청성공
  } else {
    // 서버에서 처리 필요 -> 아직 서버에서 처리 안 함
    boardStatusView = boardStatusNames[1];// 삭제
  }

  // cancelBtnBoxView 결정
  // 처음 and 취소요청실패
  if(deleteResult === false) {
    deleteBtnBoxView = (
      data.status === "D" ? 
      "" :
      <Button id={data.num} onClick={handleCancelClick}>삭제</Button>
    );
  } else {
    deleteBtnBoxView = "삭제완료";
  }

  if(loding) return (<Loding />);// 클라이언트 요청 처리 view

  return (
    <tr>
      {/* 게시글 순서 */}
      <th id={data.num} 
        onClick={handleItemNameClick} className="mousePointer"
      >
        {/* 첫페이지가 1번부터 => 순서가 안 맞군*/}
        {searchCond.size * (totalPages - searchCond.page - 1) + datasLength - numPosition}
        {/* 첫페이지가 마지막번호부터 -> 어떻게 하지? */}
      </th>
      {/* 추천상품명 */}
      <th>
          {data.itemName}
      </th>
      {/* 판매자 */}
      <th>  
        {data.sellerId}
      </th>
      {/* 작성자 */}
      <th>
        {data.memberId}
      </th>
      {/* 게시글 상태 */}
      <th>
        {boardStatusView}
      </th>
      {/* 게시글 삭제 button */}
      <th>
        {deleteBtnBoxView}
      </th>
    </tr>
  )
}

export default RecommendationForAdminForm;