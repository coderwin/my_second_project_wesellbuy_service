import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { Button, Col, Form, Row, Image } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import "../../css/form.css";
import Loding from '../Loding';

const MemberUpdateForm = () => {
  
  /// 변수 모음
  // defaultData
  let defaultData = {
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
    zipcode: ""
  }
  // defaultError
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
  // navigation
  const navigation = useNavigate();
  // country에 들어가는 국가 모음
  const countryValues = ["", "ko", "us", "gb", "cn", "jp"];
  const countryNames= ["국적", "대한민국", "미국", "영국", "중국", "일본"];

  /// 상태 모음
  const [loding, setLoding] = useState(false); // 요청 상태 
  const [errorMsgs, setErrorMsgs] = useState(defaultErrMsgs); // data 에러 상태
  const [error, setError] = useState(null); // 요청 에러 상태
  const [data, setData] = useState(defaultData); // 데이터 상태
  const [file, setFile] = useState({file: null});
  
  /// 메서드 모음

  // 회원정보 불러오기
  async function getMemberDetailInfo() {
    // loding ture로 처리
    setLoding(true);
    // sessionStorage에서 num 불러오기
    const {num} = JSON.parse(sessionStorage.getItem("LOGIN_MEMBER"));
    // 서버에 회원정보 요청
    return await axios.get(
      `http://localhost:8080/members/${num}`,
      {
        withCredentials: true
      }
    );
  }
  // 회원정보 data에 담기
  async function inputData() {
    try {
      // 서버로부터 데이터 불러오기
      const result = await getMemberDetailInfo();
      // 데이터 다 가져왔으니 loding false로 처리
      setLoding(false);
      // 받아온 데이터를 변수에 담기
      const {
        name,
        id,
        email,
        phones,
        address,
      } = result.data.data;
      // data에 데이터 담기
      setData({
        ...data,
        name: name,
        id: id,
        email: email,
        selfPhone: phones.selfPhone,
        homePhone: phones.homePhone,
        country: address.country,
        city: address.city,
        street: address.street,
        detail: address.detail,
        zipcode: address.zipcode,
      });

    } catch(error) {
      // 데이터 가져오기 끝
      setLoding(false);
      // console.log(error);
      // error Msg 담기
      alert(error.response.data.errMsg);
      setError(error.response.data.errMsg);
    }
  }
  // 컨테이너 처음 실행될 때 실행
  useEffect(() => {
    // member 상세정보 불러오기 => async await 안 붙여도 될까?
    inputData();
  }, []);
  // data에 데이터 입력하기
  function handleDataChange(e) {
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
      // .imageBox에 Image 태그 담기
      document.querySelector(".imageBox").appendChild(img);
    }
    // reader가 이미지 읽도록 하기
    reader.readAsDataURL(e.target.files[0]);
  }
  // 파일 데이터 상태에 넣기
  function handleInputFileChange(e) {
    // file이 있으면 실행된다
    if(e.target.files) {
      setFile(e.target.files[0]);
      // 파일 미리보기 실행
      showImage(e);
    }
  }
  // 회원정보 수정하기
  async function handleUpdateSubmit(e) {
    // 이벤트 막기
    e.preventDefault();
    // loding 상태 true로 바꾸기
    setLoding(true);
    // FormData 객체 생성
    const formData = new FormData();
    // 데이터 넣기
    const jsonData = JSON.stringify(data);
    // file 데이터와 함께 보내기 위해 blob 객체 사용하기
    const blob = new Blob([jsonData], {type: "application/json"});
    // 데이터 넣어주기
    formData.append("data", blob);
    formData.append("file", file);
    try {
      // 데이터 서버로 보내기
      const response = await update(formData);
      // 요청 성공
      console.log("요청 성공");
      // loding을 false로
      setLoding(false);
      // 수정 완료 alert창 띄우기
      alert(response.data.data);
      // detailForm으로 이동하기
      navigation("/mydetail");
    } catch(err) {
      // 요청 실패
      console.log("요청 실패");
      // loding false로
      setLoding(false);
      // newErrorMsgs 객체 생성하기
      let newErrorMsgs = {};
      // field 값에 따라서 데이터 넣기
      for(let key in err.response.data.errors) {
        // newErrorMsgs에 에러 메시지 모두 담기
        const newErrMsg = err.response.data.errors[key];
        // errorMsgs내의 데이터 key와 newErrMsg의 filed값을 비교한다.
        for(let errorMsgKey in errorMsgs) {
          // errormsgKey와 newErrMsg의 filed가 같으면 
          // newErrMsgs에 새로운 속성을 추가한다
          if(newErrMsg.field === errorMsgKey) {
            newErrorMsgs = {
              ...newErrorMsgs,
              [newErrMsg.field]: newErrMsg.errMsg
            }
          }
        }
      }
      // errorMsgs value 교체하기
      setErrorMsgs({
        ...defaultErrMsgs,
        ...newErrorMsgs
      });
    }
  }
  // 서버로 수정 데이터 보내기
  async function update(formData) {
    // 회원 번호 불러오기
    const key = "LOGIN_MEMBER";
    const {num} = getSessionStorageData(key);
    return await axios.put(
      `http://localhost:8080/members/${num}`,
      formData,
      {
        headers: {
          "Content-Type": "multipart/form-data"
        },
        withCredentials: true
      }
    );
  }
  // sessionStorage에서 데이터 불러오기
  function getSessionStorageData(key) {
     return JSON.parse(sessionStorage.getItem(key));
  }
  // cancel click -> 상세보기로 이동
  function handleCancelClick() {
    // 상세보기로 이동
    navigation("/mydetail");
  }

  // loding true일 때
  if(loding) return (<Loding />);

  return (
    <Form onSubmit={handleUpdateSubmit}>
      {/* name */}
      <Row className="d-flex justify-content-center">
        <Col sm={8}>
          <Form.Group
            as={Row}
            className="mb-3"
          >
            <Form.Label column sm="3">
              NAME <span className='important'>*</span>
            </Form.Label>
            <Col sm="5">
              <Form.Control
                type="text"
                name="name"
                value={data.name}
                readOnly
              />
            </Col>
            {/* 에러 메시지 */}
            <Col className="error" sm="12">
              {errorMsgs.name}
            </Col>
          </Form.Group>
        </Col>
      </Row>

      {/* id */}
      <Row className="d-flex justify-content-center">
        <Col sm={8}>
          <Form.Group
            as={Row}
            className="mb-3"
          >
            <Form.Label column sm="3">
              ID <span className='important'>*</span>
            </Form.Label>
            <Col sm="5">
              <Form.Control
                type="text"
                name="id"
                value={data.id}
                readOnly
              />
            </Col>
            {/* 에러 메시지 */}
            <Col className="error" sm="12">
              {errorMsgs.id}
            </Col>
          </Form.Group>
        </Col>
       </Row>

      {/* pwd */}
      <Row className="d-flex justify-content-center">
        <Col sm={8}>
          <Form.Group
            as={Row}
            className="mb-3"
          >
            <Form.Label column sm="3">
              Password <span className='important'>*</span>
            </Form.Label>
            <Col sm="5">
              <Form.Control
                type="password"
                name="pwd"
                value={data.pwd}
                onChange={handleDataChange}
              />
            </Col>
            {/* 에러 메시지 */}
            <Col className="error" sm="12">
              {errorMsgs.pwd}
            </Col>
          </Form.Group>
        </Col>
      </Row>

      {/* pwdconfirm */}
      <Row className="d-flex justify-content-center">
        <Col sm={8}>
          <Form.Group
            as={Row}
            className="mb-3"
          >
            <Form.Label column sm="3">
              PasswordConfirm <span className='important'>*</span>
            </Form.Label>
            <Col sm="5">
              <Form.Control
                type="password"
                name="pwdConfirm"
                value={data.pwdConfirm}
                onChange={handleDataChange}
              />
            </Col>
            {/* 에러 메시지 */}
            <Col className="error" sm="12">
              {errorMsgs.pwdConfirm}
            </Col>
          </Form.Group>
        </Col>
      </Row>

      {/* email */}
      <Row className="d-flex justify-content-center">
        <Col sm={8}>
          <Form.Group
            as={Row}
            className="mb-3"
          >
            <Form.Label column sm="3">
              Email <span className='important'>*</span>
            </Form.Label>
            <Col sm="5">
              <Form.Control
                type="text"
                name="email"
                value={data.email}
                onChange={handleDataChange}
              />
            </Col>
            {/* 에러 메시지 */}
            <Col className="error" sm="12">
              {errorMsgs.email}
            </Col>
          </Form.Group>
        </Col>
       </Row>

      {/* selfphone */}
      <Row className="d-flex justify-content-center">
        <Col sm={8}>
          <Form.Group
            as={Row}
            className="mb-3"
          >
            <Form.Label column sm="3">
              SelfPhone <span className='important'>*</span>
            </Form.Label>
            <Col sm="5">
              <Form.Control
                type="text"
                name="selfPhone"
                value={data.selfPhone}
                onChange={handleDataChange}
              />
            </Col>
            {/* 에러 메시지 */}
            <Col className="error" sm="12">
              {errorMsgs.selfPhone}
            </Col>
          </Form.Group>
        </Col>
      </Row>

      {/* homephone */}
      <Row className="d-flex justify-content-center">
        <Col sm={8}>
          <Form.Group
            as={Row}
            className="mb-3"
          >
            <Form.Label column sm="3">
              Homephone <span className='important'>*</span>
            </Form.Label>
            <Col sm="5">
              <Form.Control
                type="text"
                name="homePhone"
                value={data.homePhone}
                onChange={handleDataChange}
              />
            </Col>
            {/* 에러 메시지 */}
            <Col className="error" sm="12">
              {errorMsgs.homePhone}
            </Col>
          </Form.Group>
        </Col>
      </Row>

      {/* address */}
      <Row className="d-flex justify-content-center">
        <Col sm={8}>
          <Form.Group
            as={Row}
            className="mb-3"
          >
            <Form.Label>
              Address <span className='important'>*</span>
            </Form.Label>
            <Row>
              {/* country */}
              <Form.Select name="country" value={data.country} onChange={handleDataChange}>
                {
                  countryValues.map((value, i) => value === data.country ? <option key={i} value={value}>{countryNames[i]}</option> : <option key={i} value={value}>{countryNames[i]}</option>)
                }
              </Form.Select>
              {/* 에러 메시지 */}
              <Col className="error">
                {errorMsgs.country}
              </Col>
            </Row>
            {/* city */}
            <Row>
              <Form.Control 
                type="text"
                placeholder="도시명"
                name="city"
                value={data.city}
                onChange={handleDataChange}
              />
              {/* 에러 메시지 */}
              <Col className="error">
                {errorMsgs.city}
              </Col>
            </Row>
            {/* street */}
            <Row>
              <Form.Control 
                type="text"
                placeholder="동/거리명"
                name="street"
                value={data.street}
                onChange={handleDataChange}
              />
              {/* 에러 메시지 */}
              <Col className="error">
                {errorMsgs.street}
              </Col>
            </Row>
            {/* detail */}
            <Row>
              <Form.Control 
                type="text"
                placeholder="상세정보"
                name="detail"
                value={data.detail}
                onChange={handleDataChange}
              />
              {/* 에러 메시지 */}
              <Col className="error">
                {errorMsgs.detail}
              </Col>
            </Row>
            {/* zipcode */}
            <Row>
            <Form.Control 
                type="text"
                placeholder="우편번호"
                name="zipcode"
                value={data.zipcode}
                onChange={handleDataChange}
              />
              {/* 에러 메시지 */}
              <Col className="error">
                {errorMsgs.zipcode}
              </Col>
            </Row>
          </Form.Group>  
          {/* file */}
          <Form.Group
            as={Row}
            className="mb-3"
          >
            <Form.Label column sm="2">프로필</Form.Label>
            <Col sm="4">
              <Form.Control 
                type="file"
                name="file"
                onChange={handleInputFileChange}
              />
            </Col>
            {/* 이미지 미리보기 box */}
            <Col sm="6" className="imageBox" />
            {/* 에러 메시지 */}
            <Col className="error" sm="12">
              {errorMsgs.file}
            </Col>
          </Form.Group>
        </Col>
      </Row>
      
      {/* 버튼 box */}
      <Row className="d-flex justify-content-center d-grid gap-2">
        <Col sm={2} >
          <Form.Group>
            <Button type="submit" variant="outline-primary" size="lg">수정</Button>
            <Button type="button" variant="outline-primary" size="lg" onClick={handleCancelClick}>취소</Button>
          </Form.Group>
        </Col>
      </Row>     
    </Form>
  )
}

export default MemberUpdateForm