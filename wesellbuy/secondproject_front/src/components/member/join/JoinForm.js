import axios from 'axios';
import React, { useState } from 'react';
import { Button, Col, Form, ListGroup, ListGroupItem, Row } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import '../../../css/form.css';
import Loding from '../../Loding';

/**
 * member save component
 * writer : 이호진
 * init : 2023.03.07
 * updated by writer :
 * update :
 * description : 회원가입/회원정보 저장  component
 */
const JoinForm = () => {

  // 상태 생성
  let defaultData = {
    name: "",
    id: "",
    pwd: "",
    pwdConfrim: "",
    email: "",
    selfPhone: "",
    homePhone: "",
    country: "",
    city: "",
    street: "",
    detail: "",
    zipcode: ""
  }
  // data
  const [data, setData] = useState(defaultData);
  // file
  const [file, setFile] = useState({file: null});
  
  let defaultErrMsgs = {
    name: "",
    id: "",
    pwd: "",
    pwdConfirm: "",
    email: "",
    selfPhone: "",
    homePhone: "",
    country: "",
    city: "",
    street: "",
    detail: "",
    zipcode: "",
    file: ""
  }
  // errMsgs
  const [errMsgs, setErrMsgs] = useState(defaultErrMsgs);
  // 작업 처리 상태(로딩 상태)
  const [loding, setLoding] = useState(false);
  // 아이디 중복 확인 상태
  const [checkId, setCheckId] = useState({msg : ""});


  // 상태값 변화시키기
  function handleInputDataChange(e) {
    // 상태값 바꾸기
    setData({
      ...data,
      [e.target.name]: e.target.value
    });
  }

  // 파일 미리보기
  function showImage(e) {
    // .imageBox 아무것도 없게 만들기
    document.querySelector(".imageBox").innerHTML = "";

    let reader = new FileReader();

    reader.onload = function(event) {
      // img 태그 만들기
      let img = document.createElement("img");
      img.setAttribute("src", event.target.result);
      // .imageBox에 img 태그 담기
      document.querySelector(".imageBox").appendChild(img);
    }
    // reader가 이미지 읽도록 하기
    reader.readAsDataURL(e.target.files[0]);
  }

  // 파일 데이터 상태에 넣기
  // + 파일 미리보기
  function handleInputFileChange(e) {
    // file이 있으면 실행된다
    if(e.target.files) {
      setFile(e.target.files[0]);
      // 파일 미리보기 실행
      showImage(e);
    }
  }

  // navigator 생성
  const navigation = useNavigate();
  
  // 취소 클릭 -> /으로 가기
  function handleCancelClick() {
    navigation("/");
  }

  // 가입하기
  async function handleJoinSubmit(e) {
    // 이벤트 막기
    e.preventDefault();
    // loding 상태 true로 바꾸기
    setLoding(true);
    // FormData 객체 생성
    const formData = new FormData();
    // 데이터 넣기
    const jsonData = JSON.stringify(data);
    const blob = new Blob([jsonData], {type: "application/json"});
    formData.append("data", blob);
    formData.append("file", file);
    // 데이터 서버로 보내기
    await axios({
        method: 'post',
        url: 'http://localhost:8080/members',
        data: formData,
        headers: {
          "Content-Type": "multipart/form-data"
        }
    })
    .then((response) => {
      console.log("요청 성공");
      // 가입성공 alert창 띄우기
      alert(response.data.data);
      // 로그인으로 이동
      navigation("/login");   
    })
    .catch((err) => {
      console.log("요청 실패");
      // console.log(err);
      // 로딩중 해제하기
      setLoding(false);    
      // newErrMsgs 객체 생성하기
      let newErrMsgs = {};

      // field 값에 따라서 데이터 넣기
      for(let key in err.response.data.errors) {
        // errMsgs 상태 순회하기
        const newErrMsg = err.response.data.errors[key];
        for(let errMsgKey in errMsgs) {
            // newErrMsgs의 새로운 속성을 추가한다
            if(newErrMsg.field === errMsgKey) {
              newErrMsgs = {
                ...newErrMsgs,
                [newErrMsg.field]: newErrMsg.errMsg
              };
            // 없으면 errmsgKey의 값은 ""이다  
            }
        }
      }

      // 이미 존재하는 회원 일 때
      if(err.response.data.errMsg === "이미 사용중인 아이디") {
        newErrMsgs = {
          ...newErrMsgs,
          id: err.response.data.errMsg
        };
      }
      // errMsgs value 교체하기
      setErrMsgs({
        ...defaultErrMsgs,
        ...newErrMsgs
      });
    });
  }

  // 아이디 중복 확인
  function handleCheckIdClick() {
    // id 받아오기
    const {id} = data;
    // 서버에 아이디 중복확인 요청
    axios.get(
      "http://localhost:8080/members/id/check", 
      {
        params: {
          id
        }
      })
    .then((response) => {
      setCheckId({
        ...checkId,
        msg: response.data.data
      });
      setErrMsgs({
        ...errMsgs,
        id: ""
      });
    })
    .catch((err) => {
      setErrMsgs({
        ...errMsgs,
        id: err.response.data.errMsg
      });
      setCheckId({
        msg: ""
      });
    });
  }

  // 로딩 상태가 true이면 화면에 로딩중 뜬다.
  if(loding) {
    return (
      <Loding />
    );
  }
  
  // view 만들기
  return (

    <Form onSubmit={handleJoinSubmit}>
      
      {/* name  */}
      <Row className="d-flex justify-content-center">
        <Col sm={8}>
          <Form.Group as={Row} className="mb-3">
            <Form.Label column sm="3">
              Name <span className='important'>*</span>
            </Form.Label>
            <Col sm="5">
              <Form.Control type="text" name="name" onChange={handleInputDataChange} value={data.name} />
            </Col>
            {/* 에러 메시지 */}
            <Col className="error" sm="12">
              {errMsgs.name}
            </Col>
          </Form.Group>
        </Col>
      </Row>

      {/* id  */}
      <Row className="d-flex justify-content-center">
        <Col sm={8}>
          <Form.Group as={Row} className="mb-3">
            <Form.Label column sm="3">
              ID <span className='important'>*</span>
            </Form.Label>
            <Col sm="5">
              <Form.Control type="text" name="id" onChange={handleInputDataChange} value={data.id} />
            </Col>
            <Col sm="2">
              <Button onClick={handleCheckIdClick}>중복확인</Button>
            </Col>
            {/* 에러/중복확인 메시지 */}
            <Col sm="12">
              <span className="error">{errMsgs.id}</span>
              <span className="pass">{checkId.msg}</span>
            </Col>
          </Form.Group>
        </Col>
      </Row>

      {/* pwd  */}
      <Row className="d-flex justify-content-center">
        <Col sm={8}>
          <Form.Group as={Row} className="mb-3">
            <Form.Label column sm="3">
              Password <span className='important'>*</span>
            </Form.Label>
            <Col sm="5">
              <Form.Control type="password" name="pwd" onChange={handleInputDataChange} value={data.pwd} />
            </Col>
            {/* 에러 메시지 */}
            <Col className="error" sm="12">
              {errMsgs.pwd}
            </Col>
          </Form.Group>
        </Col>
      </Row>

      {/* pwdConfrim  */}
      <Row className="d-flex justify-content-center">
        <Col sm={8}>
          <Form.Group as={Row} className="mb-3">
            <Form.Label column sm="3">
              PasswordConfirm <span className='important'>*</span>
            </Form.Label>
            <Col sm="5">
              <Form.Control type="password" name="pwdConfirm" onChange={handleInputDataChange} value={data.pwdConfirm} />
            </Col>
            {/* 에러 메시지 */}
            <Col className="error" sm="12">
              {errMsgs.pwdConfirm}
            </Col>
          </Form.Group>
        </Col>
      </Row>

      {/* email  */}
      <Row className="d-flex justify-content-center">
        <Col sm={8}>
          <Form.Group as={Row} className="mb-3">
            <Form.Label column sm="3">
              Email <span className='important'>*</span>
            </Form.Label>
            <Col sm="5">
              <Form.Control type="text" name="email" onChange={handleInputDataChange} value={data.email} />
            </Col>
            {/* 에러 메시지 */}
            <Col className="error" sm="12">
              {errMsgs.email}
            </Col>
          </Form.Group>
        </Col>
      </Row>

      {/* selfphone  */}
      <Row className="d-flex justify-content-center">
        <Col sm={8}>
          <Form.Group as={Row} className="mb-3">
            <Form.Label column sm="3">
              Selfphone <span className='important'>*</span>
            </Form.Label>
            <Col sm="5">
              <Form.Control type="text" name="selfPhone" onChange={handleInputDataChange} value={data.selfPhone} />
            </Col>
            {/* 에러 메시지 */}
            <Col className="error" sm="12">
              {errMsgs.selfPhone}
            </Col>
          </Form.Group>
       </Col>
      </Row>

      {/* homephone  */}
      <Row className="d-flex justify-content-center">
        <Col sm={8}>
          <Form.Group as={Row} className="mb-3">
            <Form.Label column sm="3">
              Homephone
            </Form.Label>
            <Col sm="5">
              <Form.Control type="text" name="homePhone" onChange={handleInputDataChange} value={data.homePhone} />
            </Col>
            {/* 에러 메시지 */}
            <Col className="error" sm="12">
              {errMsgs.homePhone}
            </Col>
          </Form.Group>
        </Col>
      </Row>

      {/* address  */}
      <Row className="d-flex justify-content-center">
        <Col sm={8}>
          <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
            <Form.Label>
              Address <span className='important'>*</span>
            </Form.Label>
            {/* country */}
            <Form.Select aria-label="Default select example" name="country" onChange={handleInputDataChange}>
              <option value="">국적</option>
              <option value="ko">대한민국</option>
              <option value="us">미국</option>
              <option value="gb">영국</option>
              <option value="cn">중국</option>
              <option value="jp">일본</option>
            </Form.Select>
            {/* 에러 메시지 */}
            <Col className="error">
              {errMsgs.country}
            </Col>
            {/* city */}
            <Form.Control type="text" placeholder="도시명" name="city" onChange={handleInputDataChange} value={data.city} />
            {/* 에러 메시지 */}
            <Col className="error">
              {errMsgs.city}
            </Col>
            {/* street */}
            <Form.Control type="text" placeholder="동/거리명" name="street" onChange={handleInputDataChange} value={data.street} />
            {/* 에러 메시지 */}
            <Col className="error">
              {errMsgs.street}
            </Col>
            {/* detail */}
            <Form.Control type="text" placeholder="상세정보" name="detail" onChange={handleInputDataChange} value={data.detail} />
            {/* 에러 메시지 */}
            <Col className="error">
              {errMsgs.detail}
            </Col>
            {/* zipcode */}
            <Form.Control type="text" placeholder="우편번호" name="zipcode" onChange={handleInputDataChange} value={data.zipcode} />
            {/* 에러 메시지 */}
            <Col className="error">
              {errMsgs.zipcode}
            </Col>
          </Form.Group>
          {/* file  */}
          <Form.Group controlId="formFile" className="mb-3">
            <Form.Label>프로필</Form.Label>
            <Form.Control type="file" name="file" onChange={handleInputFileChange} />
            {/* 에러 메시지 */}
            <Col className="error">
              {errMsgs.file}
            </Col>
            <Col className="imageBox" />
          </Form.Group>
        </Col>
      </Row>

      <Row className="d-flex justify-content-center d-grid gap-2">
        <Col sm={2} className="d-grid gap-2" >
          <Button type="submit" variant="outline-primary" size="lg">
            가입
          </Button>
        </Col>
        <Col sm={2} className="d-grid gap-2">
          <Button type="button" variant="outline-primary" onClick={handleCancelClick} size="lg">
            취소
          </Button>
        </Col>
      </Row>
    </Form>
  );
}

export default JoinForm