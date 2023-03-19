import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { Button, Card, Col, Row } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';

/**
 * Card Form component
 * writer : 이호진
 * init : 2023.03.10
 * updated by writer :
 * update :
 * description : Card component
 *               > 속성(props)
 *                - data: card에 입력하고 싶은 데이터 prop
 *                - memberInfo: 회원정보 prop
 * 
 */
const CardForm = ({data})=> {

  /// 변수 모음
  // memberId(판매자도 칸을 만들지 생각해보기)
  const {num: boardNum, itemName, sellerId, hits} = data;
  const navigation = useNavigate();// navigation

  /// 상태 모음
  const [content, setContent] = useState("");// 내용 상태
  // const [src, setSrc] = useState("");// 이미지 src -> 현재 사용 안 함

  /// 메서드 모음
  /// 처음 시작
  useEffect(() => {
    // 상품 설명은 30 글자로만
    inputContent();
    // 이미지 src 담기 -> 현재 사용 안 함
      // 이미지 있으면 사용하기 
      // if(pictureForm) {
      //   setSrc(createSrc(pictureForm.storedFileName));
      // // 이미지 없을 때
      // } else {
      //   setSrc(NoImage);
      // }
  }, []);
  // 상품 설명은 30 글자로만
  function inputContent() {
    // 상품의 설명은 30 글자만 한다.
    const newContent = data.content.slice(0, 30) + "...";
    // content에 담기
    setContent(newContent);
  }
  // 이미지 src 만들기 -> 현재 사용 안 함
  function createSrc(storedFileName) {
    return `http://localhost:8080/recommendations/images/${storedFileName}`;
  }
  // 상세보기 클릭했을 때
  // 추천합니다 상세보기로 간다
  function handleDetailClick(e) {
    const boardNum = e.target.id;
    navigation(`/recommendation/${boardNum}`);
    return;
  }


  /// view 모음
  
  return (
    <Col>
      <Card className="body_card h-100">
        {/* 이미지 있으면 사용하자 -> 현재 사용 안 함 */}
        {/* <Card.Img variant="top" src={src} />  */}
        <Card.Header>{itemName}</Card.Header>
        <Card.Body>
          <Card.Text>
            {content}
          </Card.Text>
          <Card.Text>
            <span>판매자 : </span>{sellerId}
          </Card.Text>
        </Card.Body>
        <Card.Footer>
          <Row>
            <Col sm="12">
              <span>조회수 </span>{hits}
            </Col>
            <Col sm="12">
              <Button id={boardNum} variant="primary" onClick={handleDetailClick}>상세보기</Button>
            </Col>
          </Row>
        </Card.Footer>
      </Card>
    </Col>
  )
}

export default CardForm