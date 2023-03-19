import axios from 'axios';
import React, { useContext } from 'react'
import { Button, Col, ListGroup, ListGroupItem, Row } from 'react-bootstrap';
import { useNavigate, useParams } from 'react-router-dom';
import ImagesBox from '../../common/image/ImagesBox';
import { RecommendationDetailContext } from '../RecommendationDetailForm';
import '../../../css/form.css';

/**
 * Recommendation detail 내용 component
 * writer : 이호진
 * init : 2023.03.10
 * updated by writer :
 * update :
 * description : 추천합니다글 상세보기 내용 box component
 */
const RecommendationDetailBoxForm = () => {

  /// 변수 모음
  const {data, setLoding, memberInfo, srcArr} = useContext(RecommendationDetailContext);// 외부의 변수, 상태, 메서드 불러오기
  const {num: boardNum} = useParams();// 추천합니다글번호 불러오기
  const navigation = useNavigate();// navigation

  /// 상태 모음

  /// 메서드 모음

  // 수정 버튼 클릭했을 때
  function handleUpdateClick() {
    // 수정 form으로 이동한다.
    navigation(`/recommendation/${boardNum}/update`);
  }
  // 서버로 삭제요청 한다
  async function deleteRecommendation() {
    
    return await axios.delete(
      `http://localhost:8080/recommendations/${boardNum}`,
      {
        withCredentials: true
      }
    );
  }
  // 삭제 버튼 클릭했을 때
  async function handleDeleteClick() {
    // 정말 삭제할 건지 물어보기
    const answer = window.confirm("정말로 삭제하시겠습니까?");
    // answer === true
    if(answer === true) {
      try {
        // lodiing === true
        setLoding(true);
        // 서버로 삭제요청 한다.
        const response = await deleteRecommendation();
        // 요청 성공
        setLoding(false);
        console.log("요청 성공");
        alert(response.data.data);
        // 추천합니다 list로 간다
        navigation("/recommendation/list");
      } catch(err) {
        // 요청 실패
        setLoding(false);
        console.log("요청 실패");
        console.log(err);
      }
    }
  }
  // 시간 날짜만 나오게 하기
  function printDate(datetime) {
    // 날짜시간 받아서 
      // T부분에서 cut
    const datetimeArr = datetime.split("T");
    return datetimeArr[0];
  }
  

  /// view 모음
  // 수정/삭제 버튼 만들기
  let updateAndeDeleteButtonesBox = "";// 수정/삭제 버튼 담는 변수

  if(memberInfo) {
    if(data.memberId === memberInfo.id) {
      updateAndeDeleteButtonesBox = (
        <ListGroupItem>
          <Row>
            <Col className="body_text_right">
              <Button onClick={handleUpdateClick}>수정</Button>
              <Button onClick={handleDeleteClick}>삭제</Button>
            </Col>
          </Row>
        </ListGroupItem>
      );
    }
  }

  return (
    <>
      <ListGroup as="ul">
        {/* 수정/삭제 button */}
        {updateAndeDeleteButtonesBox}
        {/* 이미지 모음 */}
        {
          data.recommendationPictureFormList.length !== 0 && (<ListGroupItem>
            <ImagesBox srcArr={srcArr}/>
          </ListGroupItem>)
        }
        {/* 추천 상품 */}
        <ListGroupItem >
          <Row>
            <Col md="2">추천 상품</Col>
            <Col md="2">{data.itemName}</Col>
          </Row>
        </ListGroupItem>
        {/* 판매자 아이디 */}
        <ListGroupItem >
          <Row>
            <Col md="2">판매자</Col>
            <Col md="2">{data.sellerId}</Col>
          </Row>
        </ListGroupItem>
        {/* 조회수 */}
        <ListGroupItem>
          <Row>
            <Col md="2">조회수</Col>
            <Col md="2">{data.hits}</Col>
          </Row>
        </ListGroupItem>
        {/* 작성자 */}
        <ListGroupItem>
          <Row>
            <Col md="2">작성자</Col>
            <Col md="2">{data.memberId}</Col>
          </Row>
        </ListGroupItem>
        {/* 작성 날짜 */}
        <ListGroupItem>
          <Row>
            <Col md="2">작성날짜</Col>
            <Col md="2">{printDate(data.createDate)}</Col>
          </Row>
        </ListGroupItem>
        {/* 추천 이유 */}
        <ListGroupItem className="originContent">
          {data.content}
        </ListGroupItem>
      </ListGroup>
    </>
  )
}

export default RecommendationDetailBoxForm;