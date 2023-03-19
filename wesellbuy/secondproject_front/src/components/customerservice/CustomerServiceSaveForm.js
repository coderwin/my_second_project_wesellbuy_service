import axios from 'axios';
import React, { useContext, useState } from 'react'
import { Button, Col, Row, Form } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { CustomContext } from '../../App';
import Loding from '../Loding';

/**
 * CustomerService save component
 * writer : 이호진
 * init : 2023.03.10
 * updated by writer :
 * update :
 * description : CustomerService 등록 component
 */
const CustomerServiceSaveForm = () => {
  /// 변수 모음
  // defaultData
  const defaultData = {
    reportedId: "",// 신고된 회원 아이디
    content: ""// 신고 내용
  }
  // defaultErrMsgs
  const defaultErrMsgs = {
    reportedId: "",// 신고된 회원 아이디
    content: ""// 신고 내용
  }
  // navigation
  const navigation = useNavigate();

  /// 상태 모음
  const [loding, setLoding] = useState(false);// 요청 처리 상태
  const [data, setData] = useState(defaultData);// 서버에 보내는 데이터 상태
  const [error, setError] = useState(null);// 에러 상태
  const [errMsgs, setErrMsgs] = useState(defaultErrMsgs); // 에러 메시지 상태
  const [memberInfo, setMemberInfo] = useState(() => {
    const key = "LOGIN_MEMBER";
    return JSON.parse(sessionStorage.getItem(key)) || null;
  }); // 회원정보 상태

  /// 메서드 모음
  // input에 데이터 바뀌면 data 데이터 변경한다
  function handleDataChange(e) {
    setData({
      ...data,
      [e.target.name]: e.target.value
    });
  }
  // submit click했을 때 서버로 전송한다
  async function handleSaveSubmit(e) {
    // 이벤트 막기
    e.preventDefault();
    // 로그인 중인지 확인
    if(memberInfo) {
      // loding 상태 true로 바꾸기
      setLoding(true);
      // 데이터 서버로 보내기
      try {
        // 데이터 저장하기
        const response = await save();
        // 저장 성공
        console.log("저장 성공");
        // loding false로
        setLoding(false);
        // 고객지원글 등록 완료 alert창 띄우기
        alert(response.data.data);
        // boardNum 가져오기
        const {boardNum} = response.data;
        // ItemDetailForm으로 이동하기 - 나중에 작동시키기
        navigation(`/cs/list`);
      } catch(err) {
        // 요청 실패
        console.log("저장 실패");
        // loding false로 
        setLoding(false);
        // 다른 에러일 경우
        if(err.response.data.errMsg) {
          alert(err.response.data.errMsg);
        }
        // field error 일 때
        if(err.response.data.errors) {
          // newErrMsgs 객체 생성하기
          let newErrMsgs = {};
          // field 값에 따라서 데이터 넣기
          for(let key in err.response.data.errors) {
            // newErrMsg에 각가의 field 에러 메시지 객체 담기
            const newErrMsg = err.response.data.errors[key];
            // newErrMsg의 field와 errMsgs의 key를 비교한다.
            for(let errMsgKey in errMsgs) {
              // errMsgKey와 newErrMsg의 field가 같으면 
              // newErrMsgs에 새로운 데이터를 추가한다.
              if(newErrMsg.field === errMsgKey) {
                newErrMsgs = {
                  ...newErrMsgs,
                  [newErrMsg.field]: newErrMsg.errMsg
                }
              } 
            }
          }
          // errMsgs에 newErrMsgs 담기
          // defaultMsgs는 항상 이벤트가 일어나면 초기화 시켜준다 
          setErrMsgs({
            ...defaultErrMsgs,
            ...newErrMsgs
          });
        }
      }
    } else {
      // 로그인 하고 이용하라고 알려주기
      alert("로그인 후 이용하세요");
    }
  }
  // 고객지원글 데이터 서버로 보내기
  async function save() {

    return await axios.post(
      "http://localhost:8080/customerservices",
      data,
      {
        withCredentials: true
      }
    );
  }
  // 취소 클릭
  function handleCancelClick() {
    // "/"으로(홈으로) 이동한다
    navigation("/");
  }

  /// view

  // 서버로 데이터 요청 할 때 view
  if(loding) return (<Loding />);

  return (
    <>
      <Form onSubmit={handleSaveSubmit}>
        {/* 신고된 회원 아이디 */}
        <Row className="d-flex justify-content-center">
          <Col sm={7}>
            <Form.Group
            as={Row}
            className="mb-3"
            >
              <Form.Label column sm="3">
                신고회원아이디 <span className='important'>*</span>
              </Form.Label>
              <Col sm="9">
                  <Form.Control
                  type="text"
                  name="reportedId"
                  value={data.reportedId}
                  onChange={handleDataChange}
                  />
              </Col>
              {/* 에러 메시지 */}
              <Col className="error" sm="12">
                  {errMsgs.reportedId}
              </Col>
            </Form.Group>
          </Col>
        </Row>

        {/* 신고 이유 */}
        <Row className="d-flex justify-content-center">
          <Col sm={7}>
            <Form.Group
              as={Row}
              className="mb-3"
            >
              <Form.Label>
                  CONTENT <span className='important'>*</span>
              </Form.Label>
              <Col sm="12">
                  <Form.Control
                    as="textarea"
                    name="content"
                    rows={10}
                    value={data.content}
                    placeholder="신고 이유를 설명해주세요"
                    onChange={handleDataChange}
                  />
              </Col>
              {/* 에러 메시지 */}
              <Col className="error">
                  {errMsgs.content}
              </Col>
            </Form.Group>
          </Col>
        </Row>

        {/* 버튼 box */}
        <Row className="d-flex justify-content-center">
          <Col sm={7}>
            <Form.Group
              as={Row}
              className="mb-3"
            >
              <Row className="d-flex justify-content-center">
                <Col sm={2}>
                  <Button type="submit">등록</Button>
                </Col>
                <Col sm={2}>
                  <Button type="button" onClick={handleCancelClick}>취소</Button>
                </Col>
              </Row>
            </Form.Group>
          </Col>
        </Row>
      </Form>
    </>
  )
}

export default CustomerServiceSaveForm;