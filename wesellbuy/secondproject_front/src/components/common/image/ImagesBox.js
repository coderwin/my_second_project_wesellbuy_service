import React, { useContext } from 'react'
import { Carousel } from 'react-bootstrap'
import { ItemDetailContext } from '../../item/ItemDetailForm';
import ImageForm from './ImageForm';

/**
 * Images component
 * writer : 이호진
 * init : 2023.03.07
 * updated by writer :
 * update :
 * description : 이미지를 여러장 보여주는 component
 *               > 속성
 *                 > srcArr : img 태그의 src 속성의 값들을 갖는 배열 prop
 */
const ImagesBox = ({srcArr}) => {

  /// 변수 모음
  // 외부의 변수, 상태, 메서드 불러오기 --> 현재 사용 안 함
  // const {srcArr} = useContext(ItemDetailContext);

  /// view 만들기
  let imageForms = null; // 모든 imageForm 
  // ImageForm들을 생성
  if(srcArr) {
    imageForms = srcArr.map((src, num) => {
      return (<Carousel.Item key={num}>
        <img
          className="d-block w-100"
          src={src}
          alt={src}
        />
        {/* 이미지 위에 글을 입력가능*/}
        <Carousel.Caption>
        </Carousel.Caption>
    </Carousel.Item>);
    });
  }
  // image error 해결하기
  // const CustomLink = styled.a;
  // ImageForm 루프 돌기
  return (
    <Carousel interval={1500}>
      {/* 클라이언트 저장 이미지가 나온다 */}
      {imageForms !== null && imageForms}
    </Carousel>
  )
}

export default ImagesBox