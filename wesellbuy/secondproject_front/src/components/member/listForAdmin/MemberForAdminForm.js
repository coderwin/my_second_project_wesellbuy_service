import axios from "axios";
import { useContext, useState } from "react";
import { Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { CustomContext } from "../../../App";
import Loding from "../../Loding";

/**
 * Member of Member box component
 * writer : 이호진
 * init : 2023.03.28
 * updated by writer :
 * update :
 * description : 회원목록의 회원정보 관리자용 component
 *               props
 *                 > data : 회원 데이터 prop
 *                 > searchCond : 회원 목록 찾기 위한 조건 데이터 prop
 *                 > numPosition : 현재 페이지에서 회원목록의 자리선정위한 번호 prop
 *                 > totalPages : 전체 페이지수 prop
 *                 > datasLength : 데이터 총 개수
 */
const MemberForAdminForm = ({data, numPosition, datasLength, totalPages, searchCond}) => {
  
  /// 변수 모음
  const joinStatusValues = ["J", "W"];// 회원가입 상태 values 모음
  const joinStatusNames = ["가입", "탈퇴"];// 회원 상태 names 모음
  const {serverHost} = useContext(CustomContext);

  /// 상태 모음
  const [loding, setLoding] = useState(false);// 요청처리 상태
  const [withdrawalResult, setWithdrawalResult] = useState(false);// 탈퇴버튼 클릭 후 처리 상태 

  /// 메서드 모음
  // 탈퇴버튼을 클릭했을 때
  async function handleCancelClick(e) {
    // 탈퇴할 건지 물어보기
    const result = window.confirm("정말로 탈퇴하시겠습니까?");
    // result === true
    if(result) {
      setLoding(true);
      try {
        // 게시글 번호
        const memberNum = e.target.id
        // 서버로 취소 요청
        const response = await cancel(memberNum);
        // 요청 성공
        // loding = false
        setLoding(false);
        // console.log("요청 성공");
        alert(response.data.data);
        // cancelResult 변경
        setWithdrawalResult(true);
      } catch(err) {
        // 요청 실패
        // console.log("요청 실패");
        // loding = false
        setLoding(false);
        // 에러 메시지 보여주기
        if(err.response.data.errMsg) {
          alert(err.response.data.errMsg);
        }
        // // WithdrawalResult 그대로 두기
      }
    }
  }
  // 서버로 탈퇴 요청
  async function cancel(num) {
    return await axios.delete(
      `${serverHost}:8080/members/${num}/admin`,
      {
        withCredentials: true
      }
    );
  }
  // 시간 -> 날짜만 나오게 하기
  function printDate(datetime) {
    // 날짜시간 받아서 
      // T부분에서 cut
    const datetimeArr = datetime.split("T");
    return datetimeArr[0];
  }

  /// view 모음
  let addressBoxView = "";// 배송지 박스 출력 변수
  let joinStatusView = null;// 회원가입 상태 출력 변수
  let withdrqwalBtnBoxView = null;// 탈퇴버튼 박스 출력 변수

  // addressBoxView 결정
  // 주소 있을 때
  if(data.address) {
    for(let key in data.address) {
      addressBoxView += data.address[key] + " ";
    }
  // 주소 없을 때  
  } else {
    addressBoxView = "주소 없음";
  }

  // joinStatusView 결정
  // 처음 and 탈퇴요청실패
  if(withdrawalResult === false) {
    for(let i = 0; i < joinStatusValues.length; i++) {
      if(joinStatusValues[i] === data.status) {
        joinStatusView = joinStatusNames[i];
      }
    }
  // 삭제요청성공
  } else {
    // 서버에서 처리 필요 -> 아직 서버에서 처리 안 함
    joinStatusView = joinStatusNames[1];// 탈퇴
  }

  // cancelBtnBoxView 결정
  // 처음 and 탈퇴요청실패
  if(withdrawalResult === false) {
    withdrqwalBtnBoxView = (
      data.status === "W" ? 
      "" :
      <Button id={data.num} onClick={handleCancelClick}>탈퇴</Button>
    );
  } else {
    withdrqwalBtnBoxView = "탈퇴완료";
  }

  if(loding) return (<Loding />);// 클라이언트 요청 처리 view

  return (
    <tr>
      {/* 회원 순서 */}
      <th>
        {/* 첫페이지가 1번부터 => 순서가 안 맞군*/}
        {searchCond.size * (totalPages - searchCond.page - 1) + datasLength - numPosition}
        {/* 첫페이지가 마지막번호부터 -> 어떻게 하지? */}
      </th>
      {/* 아이디 */}
      <th>
          {data.id}
      </th>
      {/* 이름 */}
      <th>  
        {data.name}
      </th>
      {/* 이메일 */}
      <th>
        {data.email}
      </th>
      {/* 휴대전화 */}
      <th>
        {data.phones.selfPhone}
      </th>
      {/* 집전화 */}
      <th>
        {data.phones.homePhone}
      </th>
      {/* 주소 */}
      <th>
        {addressBoxView}
      </th>
      {/* 가입날짜 */}
      <th>
        {printDate(data.createDate)}
      </th>
      {/* 가입상태 */}
      <th>
        {joinStatusView}
      </th>
      {/* 탈퇴 button */}
      <th>
        {withdrqwalBtnBoxView}
      </th>
    </tr>
  )
}

export default MemberForAdminForm;