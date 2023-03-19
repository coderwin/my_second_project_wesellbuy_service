import axios from 'axios';
import React, { useContext, useEffect, useState } from 'react'
import { Button, Col, ListGroup, ListGroupItem, Row } from 'react-bootstrap';
import { useNavigate, useParams } from 'react-router-dom';
import ImagesBox from '../../common/image/ImagesBox';
import { ItemDetailContext } from '../ItemDetailForm';
import '../../../css/form.css';

/**
 * Item detail 내용 component
 * writer : 이호진
 * init : 2023.03.07
 * updated by writer :
 * update :
 * description : 상품 상세보기 내용 box component
 */
const ItemDetailBoxForm = () => {

  /// 변수 모음
  const {data, setLoding, memberInfo, srcArr, likesList, addItemLikesList, countOutInItemLikesList} = useContext(ItemDetailContext);// 외부의 변수, 상태, 메서드 불러오기
  const {num: boardNum} = useParams();// 상품번호 불러오기
  const navigation = useNavigate();// navigation
  const favoriteHeart = likesList.includes(Number(boardNum));

  /// 상태 모음
  const [likesState, setLikesState] = useState(false);// 좋아요 선택 상태
  const [likesCount, setLikesCount] = useState(data.likes);// 좋아요수 상태


  /// 메서드 모음
 
  // 서버에서 좋아요 리스트 불러오기
  async function getLikesList() {
    return await axios.get(
      `http://localhost:8080/items/likes`,
      {
        withCredentials: true
      }
    );
  }

  // 좋아요 하트를 클릭했을 때 삭제하기
  async function deleteLikes(boardNum, likesNum) {
    // 서버에 좋아요 삭제 요청하기
    return await axios.delete(
      `http://localhost:8080/items/${boardNum}/likes`,
      {
        withCredentials: true
      }
    );
  }
  // 좋아요 하트를 클릭했을 때 등록하기
  async function saveLikes(boardNum) {
    // 서버에 좋아요 등록 요청하기
    return await axios.post(
      `http://localhost:8080/items/${boardNum}/likes`,
      {},
      {
        withCredentials: true
      }
    );
  }

  // 좋아요 하트를 클릭했을 때
  async function handleLikesClick() {
    // memberInfo 있는지 확인 == 로그인 사용자인지 확인
    if(memberInfo) {
      // likesState === true
      if(likesState === true) {
        // 서버에 좋아요 delete를 요청한다.
        try {
          const response = await deleteLikes(boardNum);
          // 요청 성공
          console.log("요청 성공");
          const itemNum = Number(boardNum);
          // sessionStorage에서 빼기
          countOutInItemLikesList(likesList, itemNum);
          // likesState = false로 바꾸기
          setLikesState(() => false);
          // 좋아요수 1 감소시키기
          setLikesCount((likesCount) => {
            return likesCount - 1;
          });
        } catch(err) {
          // 요청 실패
          console.log("요청 실패");
          // console.log(err);
        }
      // likesState === false
      } else {
        // 서버에 좋아요 delete를 요청한다.
        try {
          const response = await saveLikes(boardNum);
          // 요청 성공
          console.log("요청 성공");
          const itemNum = Number(boardNum);
          // sessionStorage에 저장
          addItemLikesList(likesList, itemNum);
          // likesState = true로 바꾸기
          setLikesState(() => true);
          // 좋아요수 1 증가시키기
          setLikesCount((likesCount) => {
            return likesCount + 1; 
          });
        } catch(err) {
          // 요청 실패
          console.log("요청 실패");
          // console.log(err);
        }
      }
    } else {
      // 로그인 후 사용하라고 말하기
      alert("로그인 후 이용해주세요");
    }
  }
  

  // 수정 버튼 클릭했을 때
  function handleUpdateClick() {
    // 수정 form으로 이동한다.
    navigation(`/item/${boardNum}/update`);
  }
  // 서버로 삭제요청 한다
  async function deleteItem() {
    
    return await axios.delete(
      `http://localhost:8080/items/${boardNum}`,
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
        const response = await deleteItem();
        // 요청 성공
        setLoding(false);
        console.log("요청 성공");
        alert(response.data.data);
        // "/"로 돌아간다
        navigation("/");
      } catch(err) {
        // 요청 실패
        setLoding(false);
        console.log("요청 실패");
        // console.log(err);
      }
    }
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

  /// 처음 시작
  // likesState value 정하기
  useEffect(() => {
    if(likesList.includes(Number(boardNum))) {
      setLikesState(true);
    }
  }, []);

  return (
    <>
      <ListGroup as="ul">
        {/* 수정/삭제 button */}
        {updateAndeDeleteButtonesBox}
        {/* 상품명 */}
        <ListGroupItem className="body_text_center display-4 bg-light">
          {data.name}
        </ListGroupItem>
        {/* 이미지 모음 */}
        {
          data.pictureForms.length !== 0 && (<ListGroupItem>
            <ImagesBox srcArr={srcArr}/>
          </ListGroupItem>)
        }
        {/* 판매자 아이디 */}
        <ListGroupItem >
          <Row>
            <Col md="2">판매자</Col>
            <Col md="2">{data.memberId}</Col>
          </Row>
        </ListGroupItem>
        {/* 좋아요수 */}
        <ListGroupItem>
          <Row>
            {/* 클릭하면 증가 */}
            <Col md="2">
              <span>좋아요</span> 
              <span className="likes mousePointer" onClick={handleLikesClick}>
                {/* {likesState ? "💓" : "♥️"} */}
                {favoriteHeart ? "💓" : "♥️"}
              </span>
            </Col>
            <Col md="2">{likesCount}</Col>
          </Row>
        </ListGroupItem>
        {/* 조회수 */}
        <ListGroupItem>
          <Row>
            <Col md="2">조회수</Col>
            <Col md="2">{data.hits}</Col>
          </Row>
        </ListGroupItem>
        {/* 가격 */}
        <ListGroupItem>
          <Row>
            <Col md="2">가격(원)</Col>
            <Col md="2">{data.price}</Col>
          </Row>
        </ListGroupItem>
        {/* 제고수량 */}
        <ListGroupItem>
          <Row>
            <Col md="2">수량(개)</Col>
            <Col md="2">{data.stock}</Col>
          </Row>
        </ListGroupItem>
        {/* 저자 - type:B에서만 */}
        {data.type === "B" && 
          <ListGroupItem>
            <Row>
              <Col md="2">저자</Col>
              <Col md="2">{data.author}</Col>
            </Row>
          </ListGroupItem>
        }
        {/* 출판사 - type:B에서만 */}
        {data.type === "B" && 
          <ListGroupItem>
            <Row>
              <Col md="2">출판사</Col>
              <Col md="2">{data.publisher}</Col>
            </Row>
          </ListGroupItem>
        }
        {/* 제조회사 - type:HA에서만 */}
        {data.type === "HA" && 
          <ListGroupItem>
            <Row>
              <Col md="2">제조회사</Col>
              <Col md="2">{data.company}</Col>
            </Row>
          </ListGroupItem>
        }
        {/* 설명 */}
        <ListGroupItem className="originContent">
          {data.content}
        </ListGroupItem>
      </ListGroup>
    </>
  )
}

export default ItemDetailBoxForm;