import axios from 'axios';
import React, { useState } from 'react'
import { Button } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import Loding from '../../Loding';

/**
 * Order of Order box component
 * writer : 이호진
 * init : 2023.03.11
 * updated by writer :
 * update :
 * description : 주문목록의 주문 component
 *               props
 *                 > data : 주문 데이터 prop
 *                 > searchCond : 주문목록 찾기 위한 조건 데이터 prop
 *                 > numPosition : 현재 페이지에서 주문목록의 자리선정위한 번호 prop
 *                 > totalPages : 전체 페이지수 prop
 * 
 * comment : 주문취소 완료되었을 때 
 *              -> 배송상태 처리 필요
 *                -> 현재 : 취소전 그대로 유지될 것으로 예상
 *                -> 수정한다면 : 배송취소가 생겼으면 좋겠군
 */
const OrderForm = ({data, numPosition, datasLength, totalPages, searchCond}) => {
  
  /// 변수 모음
  const navigation = useNavigate();// navigation
  const orderStatusValues = ["ORDER", "CANCEL"];// 주문상태 values 모음
  const orderStatusNames = ["주문", "취소"];// 주문상태 names 모음
  const deliveryStatusValues = ["READY", "TRANSIT", "COMPLETE", "OVER"];// 배달상태 values 모음
  const deliveryStatusNames = ["준비중", "배송중", "배송완료", "배송취소"];// 배달상태 names 모음

  /// 상태 모음
  const [loding, setLoding] = useState(false);// 요청처리 상태
  const [cancelResult, setCancelResult] = useState(false);// 취소버튼 클릭 후 처리 상태 

  /// 메서드 모음
  // 주문을 클릭했을 때
  function handleItemNameClick(e) {
    // id 불러오기
    const id = e.target.id;// 상품번호 불러오기
    // 주문 상세보기로 이동
    navigation(`/order/${id}`);
  }
  // 취소버튼을 클릭했을 때
  async function handleCancelClick(e) {
    setLoding(true);
    try {
      // 주문번호
      const orderNum = e.target.id
      // 서버로 취소 요청
      const response = await cancel(orderNum);
      // 요청 성공
      // loding = false
      setLoding(false);
      console.log("요청 성공");
      alert(response.data.data);
      // cancelResult 변경
      setCancelResult(true);
    } catch(err) {
      // 요청 실패
      console.log("요청 실패");
      // loding = false
      setLoding(false);
      // 에러 메시지 보여주기
      if(err.response.data.errMsg) {
        alert(err.response.data.errMsg);
      }
      // // CancelResult 그대로 두기
    }
  }
  // 서버로 취소 요청
  async function cancel(num) {
    return await axios.delete(
      `http://localhost:8080/orders/${num}`,
      {
        withCredentials: true
      }
    );
  }
  // 시간 날짜만 나오게 하기
  function printDate(datetime) {
    // 날짜시간 받아서 
      // T부분에서 cut
    const datetimeArr = datetime.split("T");
    return datetimeArr[0];
  }

  /// view 모음
  let orderStatusView = null;// 주문상태 출력 변수
  let deliveryStatusView = null;// 배달상태 출력 변수
  let cancelBtnBoxView = null;// 취소버튼 박스 출력 변수

  // orderStatusView 결정
  // 처음 and 취소요청실패
  if(cancelResult === false) {
    for(let i = 0; i < orderStatusValues.length; i++) {
      if(orderStatusValues[i] === data.orderStatus) {
        orderStatusView = orderStatusNames[i];
      }
    }
  // 취소요청성공
  } else {
    // 서버에서 처리 필요 -> 아직 서버에서 처리 안 함
    orderStatusView = orderStatusNames[1];// 취소
  }

  // deliveryStatusView 결정
  // 처음 and 취소요청실패
  if(cancelResult === false) {
    for(let i = 0; i < deliveryStatusValues.length; i++) {
      if(deliveryStatusValues[i] === data.deliveryStatus) {
        deliveryStatusView = deliveryStatusNames[i];
      }
    }
  } else {
    deliveryStatusView = deliveryStatusNames[3];// 배송취소 
  }

  // cancelBtnBoxView 결정
  // 처음 and 취소요청실패
  if(cancelResult === false) {
    cancelBtnBoxView = (
      data.orderStatus === "CANCEL" ? 
      "취소완료" :
      (
        data.deliveryStatus === "READY" ?
        <Button id={data.num} onClick={handleCancelClick}>취소</Button> :
        "취소불가" 
      )
    );
  } else {
    cancelBtnBoxView = "취소완료";
  }

  if(loding) return (<Loding />);// 클라이언트 요청 처리 view

  return (
    <tr>
      {/* 나의 주문번호 순서 */}
      <th id={data.num} 
        onClick={handleItemNameClick} className="mousePointer"
      >
        {/* 첫페이지가 1번부터 => 순서가 안 맞군*/}
        {searchCond.size * (totalPages - searchCond.page - 1) + datasLength - numPosition}
        {/* 첫페이지가 마지막번호부터 -> 어떻게 하지? */}
      </th>
      {/* 주문날짜 */}
      <th>
        {printDate(data.createDate)}
      </th>
      {/* 주문상태 */}
      <th>  
        {orderStatusView}
      </th>
      {/* 배달상태 */}
      <th>
        {deliveryStatusView}
      </th>
      {/* 주문취소 button */}
      <th>
        {cancelBtnBoxView}
      </th>
    </tr>
  )
}

export default OrderForm