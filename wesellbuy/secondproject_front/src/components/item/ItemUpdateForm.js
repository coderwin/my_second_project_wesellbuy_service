import axios from 'axios';
import React, { createContext, useEffect, useRef, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom';
import BookUpdateForm from './update/BookUpdateForm';
import FurnitureUpdateForm from './update/FurnitureUpdateForm';
import HomeApplianceUpdateForm from './update/HomeApplianceUpdateForm';
import { Button, Col, Form, Row} from 'react-bootstrap';
import ImagesBoxSpread from '../common/image/ImagesBoxSpread';
import Loding from '../Loding';

/**
 * Item update component
 * writer : 이호진
 * init : 2023.03.07
 * updated by writer : 이호진
 * update : 2023.03.18
 * description : Item 수정 component
 * 
 * update : > input of price -> readOnly 속성 추가
 *            > 가격을 바꾸면 장바구니에 있는 가격값도 바꿀 수 없을까?
 */
export const ItemUpdateContext = createContext(null); // itemUpdate Contexnt

const ItemUpdateForm = () => {

  /// 변수 모음
  // defaultData
  const defaultData = {
    name: "", // 상품명
    stock: 1, // 제고 수량
    price: "", // 가격
    content: "", // 상품 설명
    type: "", // 상품종류 설정
    author: "", // 저자(type=B에 필요)
    publisher: "", // 출판사(type=B에 필요)
    company: "", // 제조회사(type=HA,F에 필요)
    pictureForms: []// 이미지 info 모음
  }
  // defaultErrMsgs
  const defaultErrMsgs = {
    name: "", // 상품명
    stock: "", // 제고 수량
    price: "", // 가격
    content: "", // 상품 설명
    type: "", // 상품종류 설정
    author: "", // 저자(type=B에 필요)
    publisher: "", // 출판사(type=B에 필요)
    company: "", // 제조회사(type=HA,F에 필요)
    files: ""// 파일 에러메시지
  }
  // navigation
  const navigation = useNavigate();
  const {num: boardNum} = useParams();// 상품번호 불러오기
  // type에 들어가는 상품종류 모음
  const typeValues = ["", "B", "F", "HA", "ITEM"];
  const typeNames = ["선택", "책", "가구", "가전제품", "기타"];

  /// 상태 모음
  const [loding, setLoding] = useState(false);// 요청 처리 상태
  const [data, setData] = useState(defaultData);// 서버에 보내는 데이터 상태
  const [error, setError] = useState(null);// 에러 상태
  const [errMsgs, setErrMsgs] = useState(defaultErrMsgs); // 에러 메시지 상태
  const [files, setFiles] = useState(null);// 파일들 상태
  const [srcArr, setSrcArr] = useState(null);// 이미지 src 배열
  const [pictureNums, setPictureNums] = useState([]);// 이미지번호 모아두는 배열 상태
  const [memberInfo, setMemberInfo] = useState(() => {
    const key = "LOGIN_MEMBER";
    return JSON.parse(sessionStorage.getItem(key)) || null
  });// 로그인 사용자 정보 상태


  /// 메서드 모음

  // 상품 상세정보 데이터에 담기
    // 이미지 srcArr도 담기 - inputSrcArr
  async function inputData() {
    setLoding(true);
    if(memberInfo) {
      try {
        // 상품 detail 불러오기
        const response = await getItemDetailInfo(boardNum);
        // 요청 성공
        console.log("요청 성공");
        setLoding(false);
        // data 데이터 담기
        setData({
          ...data,
          name: response.data.data.name,
          stock: response.data.data.stock,
          price: response.data.data.price,
          content: response.data.data.content,
          type: response.data.data.type,
          author: response.data.data.author,
          publisher: response.data.data.publisher,
          company: response.data.data.company,
          pictureForms: response.data.data.pictureForms
        });
        // srcArr 만들기
        createSrcArr(response.data.data.pictureForms);
        // pictureNums 만들기
        createPictureNums(response.data.data.pictureForms);
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
  // 이미지 src 만들기
  function createSrc(storedFileName) {
    return `http://localhost:8080/items/images/${storedFileName}`;
  }
  // 상품 상세보기 데이터 불러오기
  async function getItemDetailInfo(boardNum) {
    // 서버에 item detail 요청하기
    // 누구든 볼수 있음 - 인증 불필요
    return await axios.get(
      `http://localhost:8080/items/${boardNum}`
    );
  }
  
  // input에 데이터 바뀌면 data 데이터 변경한다
  function handleDataChange(e) {
    setData({
      ...data,
      [e.target.name]: e.target.value
    });
  }
  // submit click했을 때 서버로 전송한다
  async function handleUpdateSubmit(e) {
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
      const response = await update(formData, boardNum);
      // 저장 성공
      console.log("상품 수정 성공");
      // loding false로
      setLoding(false);
      // 상품 수정 완료 alert창 띄우기
      alert(response.data.data);
      // itemNum 가져오기
      const {itemNum} = response.data;
      // ItemDetailForm으로 이동하기 - 나중에 작동시키기
      navigation(`/item/${itemNum}`);
    } catch(err) {
      // 요청 실패
      console.log("상품 수정 실패");
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
  }
  // 상품 수정 데이터 서버로 보내기
  async function update(formData, boardNum) {

    return await axios.put(
      `http://localhost:8080/items/${boardNum}`,
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
    // "/"으로 이동한다
    // 뒤로 이동한다
    navigation(-1);
    // 상세보기로 간다 => 위에 작동 보고 판단
    // navigation(`/item/${boardNum}`);
  }
  // 상품 typeValue에 따른 TypeName으로 바꾸기
  function changeTypeValueToName() {
    let typeName = ""; // 상품종류 이름
    // typeValues를 순회하여 typeName을 찾는다.
    typeValues.forEach((value, i) => {
      // value와 data.type이 같으면
        // typeNames의 typeName을 고른다
      if(value === data.type) {
        typeName = typeNames[i];
      }
    });
    // typeName 반환한다.
    return typeName;
  }
  // 이미지 삭제 클릭했을 때
  async function handleDeleteImageClick(e) {
    // 정말로 삭제할 건지 물어보기
    const answer = window.confirm("정말로 삭제하시겠습니까?");

    if(answer === true) {
      try {
        // 서버로 이미지 삭제요청 보내기
        const response = await deleteImage(boardNum, e.target.id);
        // 요청 성공
        console.log("요청 성공");
        alert(response.data.data);
        // 새로운 pictureForms
        const newPictureForms = data.pictureForms.filter((picture) => {
          return picture.num !== Number(e.target.id);
        });
        // pictureForms의 이미지를 지운다
        setData((data) => {
            return {
            ...data,
            pictureForms: newPictureForms
            }
        });
        // srcArr 만들기
        createSrcArr(newPictureForms);
        // pictureNums 만들기
        createPictureNums(newPictureForms);
      } catch(err) {
        console.log("요청 실패");
        alert(err.response.data.errMsg);
      }
      return;
    }
  }
  // 서버로 이미지 삭제요청 보내기
  async function deleteImage(boardNum, pictureNum) {

    return await axios.delete(
      `http://localhost:8080/items/${boardNum}/pictures/${pictureNum}`,
      {
        withCredentials: true
      }
    );
  }
  // 이미지 srcArr 만들기
  function createSrcArr(pictureForms) {
    // newSrcArr 생성
    let newSrcArr = "";
    // pictureForms를 순회하면서 src 만들기
    if(pictureForms) {
      newSrcArr = pictureForms.map((pictureForm) => {
        return createSrc(pictureForm.storedFileName);
      });
    }
    // srcArr상태에 담기
    setSrcArr([
      ...newSrcArr
    ]);
  }
  // 이미지 pictureNums 만들기
  function createPictureNums(pictureForms) {
    // newPictureNums 생성
    let newPictureNums = "";
    // pictureForms 순회화면서 pictureNum 만들기
    if(pictureForms) {
      newPictureNums = pictureForms.map((pictureForm) => {
        return pictureForm.num;
      });
    }
    // pictureNums에 담기
    setPictureNums([
      ...pictureNums,
      ...newPictureNums
    ]);
  }

  /// 처음 시작
  useEffect(() => {
    // 상품 상세보기 데이터 불러오기
    inputData();
  }, []);

  /// view

  // 서버로 데이터 요청 할 때 view
  if(loding) return (<Loding />);

  return (
    <>
      <ItemUpdateContext.Provider value={{data, errMsgs, handleDataChange}}>
        <Form onSubmit={handleUpdateSubmit}>
          {/* 상품명 */}
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
                    onChange={handleDataChange}
                  />
                </Col>
                {/* 에러 메시지 */}
                <Col className="error" sm="12">
                  {errMsgs.name}
                </Col>
              </Form.Group>
            </Col>
          </Row>

          {/* 상품종류 */}
          <Row className="d-flex justify-content-center">
            <Col sm={8}>
              <Form.Group
                as={Row}
                className="mb-3"
              >
                <Form.Label 
                  column 
                  sm="3"
                >
                  종류
                </Form.Label>
                <Col sm="5">
                  <Form.Control
                    type="text"
                    name="type"
                    value={changeTypeValueToName()}
                    readOnly
                  />
                </Col>
              </Form.Group>
            </Col>
          </Row>

          {/* 제고수량 */}
          <Row className="d-flex justify-content-center">
            <Col sm={8}>
              <Form.Group
                as={Row}
                className="mb-3"
              >
                <Form.Label column sm="3">
                  STOCK(개) <span className='important'>*</span>
                </Form.Label>
                <Col sm="5">
                  <Form.Control
                    type="number"
                    name="stock"
                    min="1"
                    value={data.stock}
                    onChange={handleDataChange}
                  />
                </Col>
                {/* 에러 메시지 */}
                <Col className="error" sm="12">
                  {errMsgs.stock}
                </Col>
              </Form.Group>
            </Col>
          </Row>

          {/* 가격 */}
          <Row className="d-flex justify-content-center">
            <Col sm={8}>
              <Form.Group
                as={Row}
                className="mb-3"
              >
                <Form.Label column sm="3">
                  PRICE(원) <span className='important'>*</span>
                </Form.Label>
                <Col sm="5">
                  <Form.Control
                    type="number"
                    name="price"
                    min="0"
                    value={data.price}
                    readOnly
                    placeholder="0"
                    onChange={handleDataChange}
                  />
                </Col>
                {/* 에러 메시지 */}
                <Col className="error" sm="12">
                  {errMsgs.price}
                </Col>
              </Form.Group>
            </Col>
          </Row>

          {/* 설명 */}
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
                    placeholder="상품을 설명해주세요"
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

          {/* type에 따른 input 태그들 */}
          <Row className="d-flex justify-content-center">
            <Col sm={8}>
              {data.type === "B" && <BookUpdateForm />}
              {data.type === "F" && <FurnitureUpdateForm />}
              {data.type === "HA" && <HomeApplianceUpdateForm />}
            </Col>
          </Row>

          {/* 이미지 모음 */}
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

          {/* 기존 이미지 모음 */}
          <Row className="d-flex justify-content-center">
            <Col sm={8}>
              <ImagesBoxSpread 
                data={data} 
                pictureForms={data.pictureForms} 
                createSrc={createSrc} 
                OnDeleteImageClick={handleDeleteImageClick}
                srcArr={srcArr}
                pictureNums={pictureNums}
              />
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
                    <Button type="submit">수정</Button>
                  </Col>
                  <Col sm={2} className="d-grid gap-2">
                    <Button type="button" onClick={handleCancelClick}>취소</Button>
                  </Col>
                </Row>
              </Form.Group>
            </Col>
          </Row>
        </Form>
        
      </ItemUpdateContext.Provider>
    </>
  )
}

export default ItemUpdateForm