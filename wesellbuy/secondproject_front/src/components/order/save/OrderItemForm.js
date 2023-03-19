import React, { useCallback, useEffect, useState } from 'react'
import { Button, Col, ListGroupItem, Form, Row } from 'react-bootstrap'

/**
 * Order Item for save component
 * writer : 이호진
 * init : 2023.03.11
 * updated by writer :
 * update :
 * description : 주문상품 component
 *               > props
 *                 - data: 주문상품 정보
 *                 - num: 주문번호 
 *                 - handleDeleteBtnClick: 삭제버튼 클릭했을 때 메서드
 *                 - handleSelectionChange(selection, itemData): 선택버튼 클랙햇을 때 메서드
 *                    - params
 *                       - selection : true or false
 *                       - itemData : 하나의 상품 데이터(정보)
 */
const OrderItemForm = ({data, num, datasLength, handleDeleteBtnClick, handleSelectionChange}) => {

  /// 변수 모음
  const defaultItemData = {
    id: "",// 장바구니 담긴 순서
    name: "",// 상품이름
    quantity: "",// 주문수량
    price: "",// 상품가격
    itemNum: "",// 상품번호
  }
  // const {id, name, quantity, price, itemNum} = data;
  /// 상태 모음
  const [itemData, setItemData] = useState({
    ...defaultItemData,
    ...data
  });// 주문상품 상태
  const [totalPrice, setTotalPrice] = useState(0);// 상품 총가격
  const [selection, setSelection] = useState(false);// 주문상품에 넣을지 말지 선택
  /// 메서드 모음
  // 총가격 모음
  const getTotalPrice = useCallback((data) => {
    if(data) {
      let newTotalPrice = 0;// 총가격
      newTotalPrice = data.price * data.quantity;
      // totalPrice에 담기
      setTotalPrice(newTotalPrice);
    }
  }, []);
  // 선택 box 클릭했을 때 selection 데이터 변경 
  // + readyOrder 데이터 변경하기
  function handleInitSelectionChangeV2(e) {
    // 체크 되었을 때 itemData 주문에 넣기
    // 체크 안 되면 빼기
    handleSelectionChange(e.target.checked, itemData);
  }

  /// 처음 시작
  useEffect(() => {
    // 상품 가격 구하기
    getTotalPrice(data);
  }, []);

  return (
    <>
      {data && (
        <ListGroupItem>
          <Row> 
            <Col sm="1">
              <input
                type="checkbox"
                name="selection"
                onClick={handleInitSelectionChangeV2}
              />
            </Col>
            <Col sm="1">{datasLength - num}</Col>
            <Col sm="2">{itemData.name}</Col>
            <Col sm="2">
              {itemData.quantity}
            </Col>
            <Col sm="2">
              {itemData.price}
            </Col>
            <Col sm="2">
              {totalPrice}
            </Col>
            
            <Col sm="2">
              <Button id={itemData.id} onClick={handleDeleteBtnClick}>삭제</Button>
            </Col>
          </Row>
        </ListGroupItem>)
      }
    </>
  )
}

export default OrderItemForm