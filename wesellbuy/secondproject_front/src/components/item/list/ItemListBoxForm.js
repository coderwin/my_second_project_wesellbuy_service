import React, { useContext } from 'react'
import { CardGroup, Row } from 'react-bootstrap';
import CardForm from '../../common/card/CardForm';
import { ItemListContext } from '../ItemListForm';

/**
 * Item list box component
 * writer : 이호진
 * init : 2023.03.09
 * updated by writer :
 * update :
 * description : 상품 목록의 상품들을 나열한 component
 */
const ItemListBoxForm = () => {

  /// 변수 모음
  // 외부의 변수 불러오기
  const {cardDatas, likesList, memberInfo, addItemLikesList, countOutInItemLikesList} = useContext(ItemListContext);
  /// 상태 모음

  /// 메서드 모음

  /// view 모음
  let view = null;// view 변수
  // cardDatas에 데이터가 있으면 실행된다
    // 모두 출력말고 한줄에 원하는 개수의 Card만 뿌려줄 수 없을까?
      // css로 조절해야하나?
  if(cardDatas) {
    // 모든 cardData 뿌려주기
    view = cardDatas.map((cardData, i) => {
      
      return (
        <CardForm
          key={i}
          data={cardData} 
          likesList={likesList} 
          memberInfo={memberInfo}
          addItemLikesList={addItemLikesList}
          countOutInItemLikesList={countOutInItemLikesList}
        /> 
        );
    });
  } else {
    view = "상품이 없습니다.";
  }

  return (
    <Row xs={1} md={5} className="g-4">
        {view}
    </Row>
  );
}

export default ItemListBoxForm;