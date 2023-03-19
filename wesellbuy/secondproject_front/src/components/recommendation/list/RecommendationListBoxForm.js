import React, { useContext } from 'react'
import { CardGroup, Col, Row } from 'react-bootstrap';
import CardNotLikesForm from '../../common/card/CardNotLikesForm';
import { RecommendationListContext } from '../RecommendationListForm';

/**
 * Recommendation list box component
 * writer : 이호진
 * init : 2023.03.10
 * updated by writer :
 * update :
 * description : 추천합니다글 목록의 게시글들을 나열한 component
 */
const RecommendationListBoxForm = () => {

  /// 변수 모음
  // 외부의 변수 불러오기
  const {cardDatas, memberInfo} = useContext(RecommendationListContext);
  
  /// 상태 모음

  /// 메서드 모음

  /// view 모음
  let view = null;// view 변수
  // cardDatas에 데이터가 있으면 실행된다
    // 모두 출력말고 한줄에 원하는 개수의 Card만 뿌려줄 수 없을까?
      // css로 조절해야하나?
  if(cardDatas.length !== 0) {
    // 모든 cardData 뿌려주기
    view = cardDatas.map((cardData, i) => {
      return (
        <CardNotLikesForm
          key={i}
          data={cardData}
        /> 
      );
    });
  } else {
    view = <Col>추천글이 없습니다.</Col>;
  }

  return (
    <Row xs={1} md={5} className="g-4">
        {view}
    </Row>
  );
}

export default RecommendationListBoxForm;