import axios from 'axios';
import React, { createContext, useEffect, useState } from 'react'
import { Col, Container, Row } from 'react-bootstrap';
import { useNavigate, useParams } from 'react-router-dom';
import Loding from '../Loding';
import OrderDetailBoxForm from './detail/OrderDetailBoxForm';
import '../../css/form.css';

/**
 * Order detail component
 * writer : 이호진
 * init : 2023.03.12
 * updated by writer :
 * update :
 * description : 주문 상세보기 component
 */
export const OrderDetailContext = createContext(null);// OrderDetailForm Context

const OrderDetailForm = () => {
  /// 변수 모음
  const defaultData = {
    num: "", // 게시글 번호
    orderStatus: "", // 주문 상태
    id: "", // 주문한 회원 id
    memberPhone: "", // 주문한 회원 연락처
    address: "", // 주문한 회원 주소
    deliveryStatus: "", // 배달 상태
    orderItemDetailList: "", // 주문상품 정보 모음
    totalPrice: "" // 전체 주문 가격
  }
  // URI의 파라미터 얻어오기
    // num을 itemNum으로 교체
  const {num: boardNum} = useParams();
  const navigation = useNavigate();// navigation

  /// 상태 모음
  const [loding, setLoding] = useState(false);// 요청 상태
  const [error, setError] = useState(null);// 에러 상태
  const [data, setData] = useState(defaultData);// 데이터 상태
  const [memberInfo, setMemberInfo] = useState(() => {
    const key = "LOGIN_MEMBER";
    return JSON.parse(sessionStorage.getItem(key)) || null
  });// 로그인 사용자 정보 상태

  /// 메서드 모음
  // 페이지 처음 시작
  useEffect(() => {
    // 주문 상세보기 데이터 불러오기
    inputData();
    // sessionStorage에서 사용자 정보 불러오기
    getMemberInfo();
  }, []);

  // 주문 상세정보 데이터에 담기
  async function inputData() {
    setLoding(true);
    if(memberInfo) {
      try {
        // 주문 detail 불러오기
        const response = await getOrderDetailInfo();
        // 요청 성공
        console.log("요청 성공");
        setLoding(false);
        // data 데이터 담기
        setData({
          ...data,
          ...response.data.data
        });
      } catch(err) {
        // 요청 실패
        console.log("요청 실패");
        // 데이터가이 없는 곳으로 입장했을 때
          // NotFound page로 이동(4xx error)
        const errMsg = "No value present";
        if(err.response.data.status === 500 && err.response.data.message === errMsg) {
          navigation("/errors/notfound");
          return;
        }
        // 클라이언트가 잘못된 URI데이터 요청을 보냈을 때
        const pattern = /^Failed to convert value of type.*/;
        if(err.response.data.status === 400 && pattern.test(err.response.data.message)) {
          navigation("/errors/notfound");
          return;
        }
        setLoding(false);
        // console.log(err);
        // errMsg 보여주기
        alert(err.response.data.errMsg);
      }
    } else {
      // 접근 권한 없는 회원 접근 막기
      navigation("/errors/notfound");
      setLoding(false);
    }
  }
  // sessionStorage에서 사용자 정보 불러오기
  function getMemberInfo() {
    // sessionStorage에 key="LOGIN_MEMBER" 있는지 확인
    const key = "LOGIN_MEMBER";
    const memberData = JSON.parse(sessionStorage.getItem(key));
    // 데이터가 있으면
    if(memberData) {
      // memberInfo에 memberData 대입
      setMemberInfo(memberData);
    }
  }

  // 주문 상세보기 데이터 불러오기
  async function getOrderDetailInfo() {
    // 서버에 item detail 요청하기
    // 누구든 볼수 없음 - 인증 필요
    // CORS 정책을 따라야 할 듯
    return await axios.get(
      `http://3.35.147.170:8080/orders/${boardNum}`,
      {
        withCredentials: true
      }
    );
  }

  // loding true -> 작업 준비중 view
  if(loding) return (<Loding />);

  return (
    <OrderDetailContext.Provider value={{data}}>
      <Container className="body_text_center">
        <Row className="d-flex justify-content-center">
          {/* Order detail box */}
          <Col className="OrderDetailBox" sm="8">
            {/* order detail box */}
            <Row>
              <OrderDetailBoxForm />
            </Row>
          </Col>
        </Row>
      </Container>
    </OrderDetailContext.Provider>
  )
}

export default OrderDetailForm;