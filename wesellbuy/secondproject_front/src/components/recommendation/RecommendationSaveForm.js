import axios from 'axios';
import React, { useState } from 'react'
import { Button, Col, Row, Form } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import Loding from '../Loding';

/**
 * Recommendation save component
 * writer : 이호진
 * init : 2023.03.10
 * updated by writer :
 * update :
 * description : Recommendation 등록 component
 */
const RecommendationSaveForm = () => {
  /// 변수 모음
  // defaultData
  const defaultData = {
    itemName: "",// 추천받은 상품 이름
    sellerId: "",// 추천받은 판매자 아이디
    content: "",// 추천 이유
  }
  // defaultErrMsgs
  const defaultErrMsgs = {
    itemName: "",// 추천받은 상품 이름
    sellerId: "",// 추천받은 판매자 아이디
    content: "",// 추천 이유
    files: ""// 파일 에러메시지
  }
  // navigation
  const navigation = useNavigate();

  /// 상태 모음
  const [loding, setLoding] = useState(false);// 요청 처리 상태
  const [data, setData] = useState(defaultData);// 서버에 보내는 데이터 상태
  const [error, setError] = useState(null);// 에러 상태
  const [errMsgs, setErrMsgs] = useState(defaultErrMsgs); // 에러 메시지 상태
  const [files, setFiles] = useState(null);// 파일들 상태
  const [memberInfo, setMemberInfo] = useState(() => {
    const key = "LOGIM_MEMBER";
    return JSON.parse(sessionStorage.getItem(key));
  });// 회원정보 불러오기

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
    // loding 상태 true로 바꾸기
    setLoding(true);
    // FormData 객체 생성
    const formData = new FormData();
    // 데이터 넣기
    const jsonData = JSON.stringify(data);
    const blob = new Blob([jsonData], {type: "application/json"}) // file과 content-type 다르게 하기 위해 사용?
    formData.append("data", blob);
    // files에 담겨있는 객체들을 files에 각각 담기
    for(let key in files) {
      formData.append("files", files[key]);
    }
    // 데이터 서버로 보내기
    try {
      // 데이터 저장하기
      const response = await save(formData);
      // 저장 성공
      console.log("저장 성공");
      // loding false로
      setLoding(false);
      // 상품 등록 완료 alert창 띄우기
      alert(response.data.data);
      // boardNum 가져오기
      const {boardNum} = response.data;
      // ItemDetailForm으로 이동하기 - 나중에 작동시키기
      navigation(`/recommendation/${boardNum}`);
    } catch(err) {
      // 요청 실패
      console.log("저장 실패");
      // loding false로 
      setLoding(false);
      // console.log(err);
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
  }
  // 상품 데이터 서버로 보내기
  async function save(formData) {

    return await axios.post(
      "http://localhost:8080/recommendations",
      formData,
      {
        headers: {
          "Content-Type": "multipart/form-data"
        },
        withCredentials: true
      }
    );
  }
  // 파일 데이터들 상태에 넣기
  function handleFilesChange(e) {
    // file이 있으면 실행된다.
    if(e.target.files) {
      // 파일을 담기
      setFiles(e.target.files);
      // 파일 미리보기 실행
      showImage(e);
    }
  }
  // 파일(들)을 미리보기
  function showImage(e) {
    // .imageBox 아무것도 없게 만들기
    document.querySelector(".imageBox").innerHTML = "";
    // event 변수로 파일들을 가져오자
    // 파일 있으면 실행된다
    if(e.target.files) {
      for(let key = 0; key < e.target.files.length; key++) {
        let file = e.target.files[key];
        // 이미지 태그 생성하기
        createImage(file);
      }
    }
  }
  // 파일 하나에 대한 이미지 태그 생성하기
  function createImage(file) {
    // FileReader 객체 생성하기
    let reader = new FileReader();
    // reader의 onload 함수 실행
    reader.onload = function(event) {
      // img 태그 만들기
      let img = document.createElement("img");
      img.setAttribute("src", event.target.result);
      // .imageBox에 Image 태그 담기
      document.querySelector(".imageBox").appendChild(img);
    }
    // reader가 이미지 읽도록 하기
    reader.readAsDataURL(file); 
  }

  // 취소 클릭
  function handleCancelClick() {
    // "/recommendation/list"으로(추천합니다글 목록) 이동한다
    navigation("/recommendation/list");
  }

  /// view

  // 서버로 데이터 요청 할 때 view
  if(loding) return (<Loding />);

  return (
    <>
      <Form onSubmit={handleSaveSubmit}>
        {/* 상품명 */}
        <Row className="d-flex justify-content-center">
          <Col sm={8}>
            <Form.Group
            as={Row}
            className="mb-3"
            >
            <Form.Label column sm="3">
                ITEMNAME <span className='important'>*</span>
            </Form.Label>
            <Col sm="5">
                <Form.Control
                type="text"
                name="itemName"
                value={data.itemName}
                onChange={handleDataChange}
                />
            </Col>
            {/* 에러 메시지 */}
            <Col className="error" sm="12">
                {errMsgs.itemName}
            </Col>
            </Form.Group>
          </Col>
        </Row>

        {/* 판매자 아이디 */}
        <Row className="d-flex justify-content-center">
          <Col sm={8}>
            <Form.Group
            as={Row}
            className="mb-3"
            >
              <Form.Label column sm="3">
                  sellerId <span className='important'>*</span>
              </Form.Label>
              <Col sm="5">
                  <Form.Control
                  type="text"
                  name="sellerId"
                  min="1"
                  value={data.sellerId}
                  onChange={handleDataChange}
                  />
              </Col>
              {/* 에러 메시지 */}
              <Col className="error" sm="12">
                  {errMsgs.sellerId}
              </Col>
            </Form.Group>
          </Col>
        </Row>

        {/* 추천 이유 */}
        <Row className="d-flex justify-content-center">
          <Col sm={8}>
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
                placeholder="추천 이유를 설명해주세요"
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

        {/* 상품 이미지 */}
        <Row className="d-flex justify-content-center">
          <Col sm={8}>
            <Form.Group
            as={Row}
            className="mb-3"
            >
              <Form.Label column sm="2">이미지</Form.Label>
              <Col sm="6">
                  <Form.Control 
                  type="file"
                  name="files"
                  multiple
                  onChange={handleFilesChange}
                  />
              </Col>
              {/* 에러 메시지 */}
              <Col sm="10" className="error">
                  {errMsgs.files}
              </Col>
              {/* 이미지 미리보기 box */}
              <Col sm="10" className="imageBox" />
            </Form.Group>
          </Col>
        </Row>

        {/* 버튼 box */}
        <Row className="d-flex justify-content-center">
          <Col sm={8}>
            <Form.Group
            as={Row}
            className="mb-3"
            >
              <Row className="d-flex justify-content-center">
                <Col sm={2} className="d-grid gap-2">
                  <Button type="submit" size="lg">등록</Button>
                </Col>
                <Col sm={2} className="d-grid gap-2">
                  <Button type="button" onClick={handleCancelClick} size="lg">취소</Button>
                </Col>
              </Row>
            </Form.Group>
          </Col>
        </Row>
      </Form>
    </>
  )
}

export default RecommendationSaveForm