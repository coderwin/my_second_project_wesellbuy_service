import axios from 'axios';
import React, { createContext, useEffect, useState } from 'react'
import { Col, Container, Row } from 'react-bootstrap';
import { useNavigate, useParams } from 'react-router-dom';
import Loding from '../Loding';
import CustomerServiceDetailBoxForm from './detail/CustomerServiceDetailBoxForm';
import ReplyCustomerServiceBoxForm from './reply/ReplyCustomerServiceBoxForm';
import '../../css/form.css';

/**
 * CustomerService detail component
 * writer : 이호진
 * init : 2023.03.10
 * updated by writer : 이호진
 * update : 2023.03.18
 * description : 고객지원글 상세보기 component
 * 
 * update : > 접근 권한 없는 사용자 입장 막기 추가
 */
export const CustomerServiceDetailContext = createContext(null);// CustomerServiceDetailForm Context

const CustomerServiceDetailForm = () => {
  /// 변수 모음
  const defaultData = {
    num: "", // 게시글 번호
    reportedId: "", // 신고된 회원 아이디
    content: "", // 신고 내용
    memberId: "", // 회원 아이디
    createDate: "", // 작성 날짜
    replyList: "" // 댓글 모음
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
  // 고객지원글 상세정보 데이터에 담기
  async function inputData() {
    setLoding(true);
    if(memberInfo) {
      try {
        // 고객지원글 detail 불러오기
        const response = await getCustomerServiceDetailInfo();
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
        // 상품이 없는 곳으로 입장했을 때
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
        console.log(err);
        // errMsg 보여주기
        alert(err.response.data.errMsg);
      }
    } else {
      // 회원이 아니면 접근 불가
      // error page로 이동
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

  // 고객지원글 상세보기 데이터 불러오기
  async function getCustomerServiceDetailInfo() {
    // 서버에 item detail 요청하기
    // 누구든 볼수 없음 - 인증 필요
    // CORS 정책을 따라야 할 듯
    return await axios.get(
      `http://localhost:8080/customerservices/${boardNum}`,
      {
        withCredentials: true
      }
    );
  }

  // 페이지 처음 시작
  useEffect(() => {
    // sessionStorage에서 사용자 정보 불러오기
    getMemberInfo();
    // 고객지원글 상세보기 데이터 불러오기
    inputData();
  }, []);

  // loding true -> 작업 준비중 view
  if(loding) return (<Loding />);

  return (
    <CustomerServiceDetailContext.Provider value={{data, setLoding}}>
      <Container>
        <Row className="d-flex justify-content-center">
          {/* CustomerService detail box */}
          <Col className="CustomerServiceDetailBox" sm="8">
            {/* CustomerService detail */}
            <Row>
              <CustomerServiceDetailBoxForm />
            </Row>
            {/* reply(댓글) box */}
            <Row>
              <ReplyCustomerServiceBoxForm replyFormList={data.replyList} />
            </Row>
          </Col>
        </Row>
      </Container>
    </CustomerServiceDetailContext.Provider>
  )
}

export default CustomerServiceDetailForm;