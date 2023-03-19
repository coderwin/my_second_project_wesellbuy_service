import React, { useContext, useEffect, useState} from 'react'
import axios from 'axios';
import { Button, Col, Container, Form, Image, Row } from 'react-bootstrap';
import noImage from '../../images/common/no_image.png';
import { useNavigate } from 'react-router-dom';
import { CustomContext } from '../../App';
import MemberDetailLeftNavForm from './detail/MemberDetailLeftNavForm';
import Loding from '../Loding';
import '../../css/form.css';

/**
 * Member detail component
 * writer : 이호진
 * init : 2023.03.06
 * updated by writer :
 * update :
 * description : 회원 정보 상세보기 component
 */
const MemberDetailForm = () => {
  /// 변수 모음
  // defaultData
  const defaultData = {
    num: "", // 회원 번호
    id: "", // 아이디
    name: "", // 이름
    email: "", // 이메일
    phones: "", // 휴대전화 + 집전화
    address: "", // 주소
    status: "", // 등록 상태
    createDate: "", // 회원 가입 날짜
    selfPictureForm: "" // 이미지
  }
  // navigation
  const navigation = useNavigate();
  // CustomContext 사용하기
  const {handleSessionFormChangeData} = useContext(CustomContext);
  // country에 들어가는 국가 모음
  const countryValues = ["ko", "us", "gb", "cn", "jp"];
  const countryNames= ["대한민국", "미국", "영국", "중국", "일본"];

  /// 상태 모음
  const [loding, setLoding] = useState(false); // 작업 상태
  const [error, setError] = useState(null); // 에러 상태
  const [data, setData] = useState(defaultData);// 회원 정보 상태
  const [memberImage, setMemberImage] = useState(null);// 회원 이미지 상태

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
    setLoding(true);
    try {
      // 서버로부터 데이터 불러오기
      const result = await getMemberDetailInfo();
      // 데이터 다 가져왔으니 loding false로 처리
      setLoding(false);
      // data에 데이터 담기
      setData(result.data.data);

    } catch(error) {
      // 데이터 가져오기 끝
      setLoding(false);
      console.log(error);
      // error Msg 담기
      alert(error.response.data.errMsg);
      setError(error.response.data.errMsg);
    }
  }
  // // 회원 이미지 불러오기 -> 이미지 태그에서 보내보기 => 사용 안 함
  // function getMemberImage() {
  //   // data의 selfPictureForm에서 이미지 이름 가져오기
  //   const {
  //     selfPictureForm : {
  //       storedFileName
  //     }
  //   } = data;
  //   console.log(`storedFileName : ${storedFileName}`);
  //   // 서버에 이미지 요청
  //   axios.get(
  //     `http://localhost:8080/members/images/${storedFileName}`
  //   )
  //   .then((response) => {
  //     // 요청 성공
  //     console.log("요청 성공");
  //     // 이미지 상태에 넣기

  //   })
  //   .catch();
  // }
  // 수정 버튼 클릭
  function handleUpdateClick() {
    // 수정 form으로 이동
    navigation("/mydetail/update");
  }
  // 서버에 탈퇴 요청 보내기
  async function withdraw() {
    // sessionStorage에서 num 불러오기
    const {num} = JSON.parse(sessionStorage.getItem("LOGIN_MEMBER"));
    // 서버에 탈퇴요청 보낸다.
    return await axios.delete(
      `http://localhost:8080/members/${num}`,
      {
        withCredentials: true
      }
    ); 
  }
  // 탈퇴 버튼 클릭
  async function handleWithdrawalClick() {
    // 탈퇴 할 건지 물어보기
    const answer = window.confirm("정말로 탈퇴하시겠습니까?");
    // answer false면 메서드 종료한다
    if(!answer) return;
    // answer true일 때
    try {
      // loding ture로 처리
      setLoding(true);
      // 서버에 탈퇴요청 보낸다.
      const result = await withdraw();
      // data는 null로 만든다.
      setData(null);
      // 탈퇴 성공 메시지 띄우기
      console.log(result.data.data);
      alert(`${result.data.data}`);
      // sessionStorage 비우기
      sessionStorage.clear();
      // App.js에 있는 sessionForm 비우기
      handleSessionFormChangeData(null);
      // 홈으로 이동
      navigation("/");
    } catch(error) {
      // loding 상태 false
      setLoding(false);
      console.log("탈퇴 요청 중 에러 발생");
      console.log(error);
      // 에러 상태 알리기
      const errMsg = "요청 처리 중 에러 발생";
      setError(errMsg);
    }
  }
  // country 이름 출력하기
  function changeCountryName() {
    
    // countryName 변수 생성
    let countryName = "";
    // countryValue와 data.address.country 같은 것 찾기
    for(let i = 0; i < countryValues.length; i++) {
      if(countryValues[i] === data.address.country) {
        countryName = countryNames[i];
      }
    }
    return countryName;
  }

  /// 처음 시작
  // 내정보 들어올 때 처음으로 시작
  useEffect(() => {
    // 회원정보 서버에서 불러오기 => async await 안 붙여도 될까?
    inputData();
  }, []);

  // loding true일 때
  if(loding) return (<Loding />);
  // 서버 요청 처리 중 에러 발생 때
  if(error) return (<div>요청 작업 중 에러 발생</div>);
    
  return (
    <Container className="member_container">
      <Row>
        {/* left side Nav */}
        <Col sm="3">
          <MemberDetailLeftNavForm />
        </Col>
        {/* memberInfo box */}
        <Col sm="9">
          <Form>
            <Row className="mb-3">
              <Form.Group
                as={Col}
                md="4"
              >
                <Form.Label>ID</Form.Label>
                <Form.Control
                  type="text"
                  value={data.id}
                  readOnly
                />
              </Form.Group>
              <Form.Group
                as={Col}
                md="4"
                controlId="validationFormik102"
                className="position-relative"
              >
                <Form.Label>NAME</Form.Label>
                <Form.Control
                  type="text"
                  value={data.name}
                  readOnly
                />
              </Form.Group>
              <Form.Group as={Col} md="4" >
                <Form.Label>EMAIL</Form.Label>
                  <Form.Control
                    type="text"
                    value={data.email}
                    readOnly
                  />
              </Form.Group>
              <Form.Group as={Col} md="4" >
                <Form.Label>EMAIL</Form.Label>
                  <Form.Control
                    type="text"
                    value={data.email}
                    readOnly
                  />
              </Form.Group>
            </Row>
            {/* 전화번호 box */}
            <Row>
              <Form.Group as={Col} md="6" >
                <Form.Label>selfphone</Form.Label>
                <Form.Control
                  type="text"
                  value={data.phones.selfPhone}
                  readOnly
                />
              </Form.Group>
              <Form.Group as={Col} md="6" >
                <Form.Label>homePhone</Form.Label>
                <Form.Control
                  type="text"
                  value={data.phones.homePhone}
                  readOnly
                />
              </Form.Group>
            </Row>
            {/* 주소 box */}
            <Row className="mb-3">
              <Form.Group as={Col} md="2">
                <Form.Label>Country</Form.Label>
                <Form.Control
                  type="text"
                  value={changeCountryName()}
                  readOnly
                />
              </Form.Group>
              <Form.Group as={Col} md="2">
                <Form.Label>City</Form.Label>
                <Form.Control
                  type="text"
                  value={data.address.city}
                  readOnly
                />
              </Form.Group>
              <Form.Group as={Col} md="2">
                <Form.Label>Street</Form.Label>
                <Form.Control
                  type="text"
                  value={data.address.street}
                  readOnly
                />
              </Form.Group>
              <Form.Group as={Col} md="2">
                <Form.Label>Detail</Form.Label>
                <Form.Control
                  type="text"
                  value={data.address.detail}
                  readOnly
                />
              </Form.Group>
              <Form.Group as={Col} md="2">
                <Form.Label>Zipcode</Form.Label>
                <Form.Control
                  type="text"
                  value={data.address.zipcode}
                  readOnly
                />
              </Form.Group>
            </Row>
            
            {/* 이미지 box */}
            <Row className="d-flex justify-content-center">
              <Form.Label>프로필</Form.Label>
              <Col sm={12}>
              {data.selfPictureForm ? <Image className="img-responsive profil" roundedCircle src={`http://localhost:8080/members/images/${data.selfPictureForm.storedFileName}`} /> 
              : <Image className="profil" roundedCircle src={noImage} />}
              </Col>
            </Row>
            
            {/* 버튼 box */}
            <Form.Group>
              <Row className="d-flex justify-content-center d-grid gap-2">
                <Col sm={2} className="d-grid gap-2" >
                  <Button type="button" onClick={handleUpdateClick}>수정</Button>
                </Col>
                <Col sm={2} className="d-grid gap-2" >
                  <Button type="button" onClick={handleWithdrawalClick}>탈퇴</Button>
                </Col>
              </Row>  
            </Form.Group>       
          </Form>
        </Col>
      </Row>
    </Container>
  )
}

export default MemberDetailForm;