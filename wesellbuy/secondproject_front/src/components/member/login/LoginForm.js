import React, { useState, useEffect, useContext } from 'react';
import { Button, Col, Container, Form, Row } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { findAllByTestId } from '@testing-library/react';
import { CustomContext } from '../../../App';
import Loding from '../../Loding';

export const LoginForm = () => {

  // 기본 데이터 모음
  const defaultData = {
    id: "",
    pwd: "",
    rememberId: false
  }
  const defaultError = {
    errMsg: ""
  }
  // 상태 생성
  const [data, setData] = useState(defaultData); // 로그인 정보
  const [loding, setLoding] = useState(false); // 데이터 처리 중
  const [error, setError] = useState(defaultError); // 에러 정보
  // 외부에서 변수, 함수, 상태 불러오기
  const {handleSessionFormDataInput} = useContext(CustomContext);
  // 필요 도구 모음
  const navigation = useNavigate(); // 페이지 이동

  /// 메서드 모음
  // 클라이언트가 입력한 데이터를 data 상태에 입력하기
  function handleInputDataChange(event) {
    setData({
      ...data,
      [event.target.name]: event.target.value
    });
  }
  // 아이디 기억 클릭 처리 토글 처리
  function handleRememberIdChange(e) {
    // rememberId == true로 변경
    if(data.rememberId === false) {
      setData({
        ...data,
        rememberId: true
      });
    } else {
      setData({
        ...data,
        rememberId: false
      });
    }
  }

  // /login 처음 입장했을 때 실행되는 메서드
  useEffect(() => {
    // cookie에 key가 REMEMBER_ID인 cookie가 있는지 확이하기
    let cookieKey = "REMEMBER_ID";
    const cookieValue = getCookieValue(cookieKey);
    // 있으면 data.id에 아이디 입력
    if(cookieValue) {
      setData({
        ...data,
        id: cookieValue,
        rememberId: true
      });
    }
  }, []);
  
  // 쿠키 value 불러오기
  function getCookieValue(key) {
    let cookieKey = key + "="; // 쿠키내의 모습 만들기 
    let result = ""; // 결과값
    const cookieArr = document.cookie.split(";"); // 쿠키들을 배열로 만들기
    
    for(let i = 0; i < cookieArr.length; i++) {
      // 쿠키에서 처음이 " "로 시작할 때 처리
      if(cookieArr[i][0] === " ") {
        cookieArr[i] = cookieArr[i].substring(1);
      }
      // 쿠키의 value 찾기
      if(cookieArr[i].indexOf(cookieKey) === 0) {
        result = cookieArr[i].slice(cookieKey.length, cookieArr[i].length);
        return result;
      }
    }
    // key에 대한 value 없을 때
    return result;
  }

  // 쿠키 삭제
  function deleteCookie(key) {
    document.cookie = key + '=; expires=Thu, 01 Jan 1999 00:00:10 GMT;'
  }

  // 쿠키 생성
  function createCookie(key, value) {
    const cookieData = `${key}=${value};`;
    // cookie에 저장
    document.cookie = cookieData;
  }

  // 로그인 시도하기
  async function handleLoginSubmit(e) {
    // onSubmit 막기
    e.preventDefault();
    // loding 상태 true로 변경
    setLoding(true);
    // 로그인 서버로 요청
    try {
      const result = await axios.post(
        "http://localhost:8080/members/login",
        data,
        {
          withCredentials: true
        }
      );
      // 요청 성공
      console.log("요청 성공");
      // sessionStorage에 로그인 정보 저장
      saveInSessionStorage("LOGIN_MEMBER", result.data.data);
      // data의 rememberId 값을 통해 쿠키에 아이디 기억 정보 저장
      const key = "REMEMBER_ID";// 아이디 기억 cookie key
      if(data.rememberId) {
        // cookie에 저장할 데이터 생성
        // 쿠키 생성
        createCookie(key, data.id);
      } else {
        // 쿠키 삭제
        deleteCookie(key);
      }
      /// sessionForm 데이터 입력하기
      handleSessionFormDataInput();
      // "/"으로 이동
      navigation("/");

    } catch(error) {
      // 요청 실패
      console.log("요청 실패");
      // loding false로 바꿈
      setLoding(false);
      // error 상태에 데이터 넣기
      setError({
        ...error,
        errMsg: error.response.data.errMsg
      });

    }
  }

  // sessionSotrage에 데이터 저장
  function saveInSessionStorage(key, value) {
    // value를 string으로 변환
    const sessionData = JSON.stringify(value);
    // sessionStorage에 데이터 저장
    sessionStorage.setItem(key, sessionData);
  }

  // loding == ture이면 로딩중
  if(loding) return (<Loding />); 

  return (
    <Container>
      <Row>
        <Form onSubmit={handleLoginSubmit}>
          {/* ID */}
          <Row className="d-flex justify-content-center">
            <Col sm={3}>
              <Form.Group className="mb-3">          
                <Form.Label>ID</Form.Label>
                <Form.Control type="text" name="id" onChange={handleInputDataChange} value={data.id} />  
              </Form.Group>
            </Col>
          </Row>
          {/* passoword  */}
          <Row className="d-flex justify-content-center">
            <Col sm={3}>
              <Form.Group className="mb-3" >
                  <Form.Label>Password</Form.Label>
                  <Form.Control type="password" name="pwd" onChange={handleInputDataChange} value={data.pwd} />
              </Form.Group>
            </Col>
          </Row>
          {/* check */}
          <Row className="d-flex justify-content-center">
            <Col sm={3}>
              <Form.Group className="mb-3" >
                  <Form.Check 
                    type="checkbox" 
                    label="Check me out" 
                    name="rememberId" 
                    onChange={handleRememberIdChange}
                    checked={data.rememberId}
                    />
              </Form.Group>
            </Col>
          </Row>
          {/* Error Message */}
          <Row className="d-flex justify-content-center">
            <Col sm={3}>
              <Form.Group className="mb-3 error">
                  {error.errMsg}
              </Form.Group>
            </Col>
          </Row>
          <Row className="d-flex justify-content-center">
            <Col sm={3} className="d-grid gap-2">
              <Button variant="primary" type="submit" size="lg">
                login
              </Button>
            </Col>
          </Row>
        </Form>
      </Row>
    </Container>
  )
}

export default LoginForm;
