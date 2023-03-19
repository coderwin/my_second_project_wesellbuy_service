import { useEffect, useState } from 'react';
import {Col, ListGroup, ListGroupItem, Row } from 'react-bootstrap'

/**
 * Images component
 * writer : 이호진
 * init : 2023.03.09
 * updated by writer :
 * update :
 * description : 이미지를 펼쳐서 보여주는 component
 *               > 속성(props)
 *                 - pictureForms : 이미지들 info 모음 prop
 *                 - createSrc : img 태그의 src 속성의 값을 만들어주는 메서드 prop
 *                 - OnDeleteImageClick: image 삭제 메서드 prop
 */
const ImagesBoxSpread = ({pictureForms, createSrc, OnDeleteImageClick, pictureNums, srcArr}) => {

  /// 메서드 모음

  /// view 만들기
  let imageForms = null; // 모든 imageForm 
  // ImageForm들을 생성
  if(srcArr) {
    imageForms = srcArr.map((src, num) => {
      return (<Col key={num}>
        <img
          className="d-block w-100"
          src={src}
          alt={src}
        />
        <span id={pictureNums[num]} onClick={OnDeleteImageClick}>X</span>
    </Col>);
    });
  }
  // image error 해결하기
  // ImageForm 루프 돌기
  return (
    <ListGroup>
      <ListGroupItem className="border border-white">
        <Row>
          {/* 클라이언트 저장 이미지가 나온다 */}
          {imageForms !== null && imageForms}
        </Row>
      </ListGroupItem>
    </ListGroup>
  );
}

export default ImagesBoxSpread;