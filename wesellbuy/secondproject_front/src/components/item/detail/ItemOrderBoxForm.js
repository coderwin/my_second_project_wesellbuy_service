import React, { useContext, useEffect, useMemo, useRef, useState } from 'react'
import { Button, Col, Form, ListGroup, ListGroupItem, Row } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { ItemDetailContext } from '../ItemDetailForm';

/**
 * Item detail 주문 component
 * writer : 이호진
 * init : 2023.03.07
 * updated by writer :
 * update :
 * description : 상품 상세보기 주문 box component
 */
// totalPrice를 구한다.
function getTotalPrice(orderData) {
  // console.log("총 가격 구하는 중...");
  // 총 가격 구하기
  const totalPrice = orderData.quantity * orderData.price;
  return totalPrice;
}

const ItemOrderBoxForm = () => {

  /// 변수 모음
  // 외부 데이터 불러오기
  const {data} = useContext(ItemDetailContext);
 
  // defaultOrderData
  const defaultOrderData = {
    id: "",// 주문번호
    name: "",// 상품명
    quantity: 1,// 주문수량
    price: 0,// 상품 1개 가격
    itemNum: ""// 상품번호
  }
  // navigation
  const navigation = useNavigate();

  /// 상태 모음
  const [orderData, setOrderData] = useState(defaultOrderData);// 주문상품 데이터
  // const [totalPrice, setTotalPrice] = useState(0); // 총 주문가격
  // const [shopingBasket, setShopingBasket] = useState(() => callShopingBasket());// 장바구니 상태
  
  /// 메서드 모음
  // 페이지 처음 시작
  useEffect(() => {
    // 필요한 데이터 orderData에 대입
    setOrderData({
      ...orderData,
      name: data.name,
      price: data.price,
      itemNum: data.num
    });
  }, []);

  // input 태그 데이터 바뀔 때 orderData 속성값 바꾸기
  function handleOrderDataChange(e) {
    setOrderData({
      ...orderData,
      [e.target.name]: e.target.value
    });
  }

  // LocalStorage에 shopingBasket 불러오기
  function callShopingBasket() {
    // LocalStorage에 shopingBasket 있는지 확인하기
    let strShopingBasket = localStorage.getItem("shopingBasket");
    // shopingBasket 없으면 localStorage에 생성하기
    if(!strShopingBasket) {
      const newShopingBasketArr = [];
      // string으로 만들어준다.
      const jsonNewShopingBasketArr = JSON.stringify(newShopingBasketArr);
      // strShopingBasket에 대입
      strShopingBasket = jsonNewShopingBasketArr;
    }
    // JSON parse 해준다.
    const shopingBasket = JSON.parse(strShopingBasket);

    return shopingBasket;
  }

  // 주문정보를 LocalStorage에 담기
  function saveLocalStorage() {
    // JSON parse 해준다.
    const shopingBasket = callShopingBasket();
    // data에 id(주문번호) 입력  
    const newOrderData = {...orderData, id: shopingBasket.length};
    // 배열에 orderData 넣기 - 최신순
    shopingBasket.unshift(newOrderData);
    // JSON string으로 변환하기
    const jsonShopingBasket = JSON.stringify(shopingBasket);
    // LocalStorage에 저장하기
    localStorage.setItem("shopingBasket", jsonShopingBasket);
  }

  // localStorage에 주문정보 저장하기
  function handleInputShopingBasketSubmit(e) {
    // onSubmig 이벤트 멈추기
    e.preventDefault();
    // 주문정보를 LocalStorage에 담기
    saveLocalStorage();
    // 장바구니 담기 완료 alert
    alert("장바구니 담기 완료");
  }

  // 주문 클릭했을 때
  function handleOrderClick() {
    // 주문하기로 이동하기
    navigation("/order/new");
  }

  // 총 주문가격 계산하기
  const totalPrice = useMemo(() => getTotalPrice(orderData), [orderData]);

  return (
    <>
      <ListGroup as="ul">
        <Form onSubmit={handleInputShopingBasketSubmit}>
          {/* 상품이름 */}
          
          <ListGroupItem>
            <Row className="d-flex justify-content-center">
              <Col sm="12">
                <Form.Group
                as={Row}
                className="mb-3"
                >
                  <Form.Label column sm="4">
                    상품명
                  </Form.Label>
                  <Col sm="8">
                    <Form.Control
                      type="text"
                      name="name"
                      value={orderData.name}
                      readOnly
                    />
                  </Col>
                </Form.Group>
              </Col>
            </Row>
            </ListGroupItem>

          {/* 주문수량 */}
          <ListGroupItem>
            <Row className="d-flex justify-content-center">
              <Col sm="12">
                <Form.Group
                as={Row}
                className="mb-3"
                >
                  <Form.Label column sm="4">
                    주문수량(개)
                  </Form.Label>
                  <Col sm="8">
                    <Form.Control
                      type="number"
                      name="quantity"
                      value={orderData.quantity}
                      min="1"
                      max={data.stock}
                      placeholder='1'
                      onChange={handleOrderDataChange}
                    />      
                  </Col>
                </Form.Group>
              </Col>
            </Row>
          </ListGroupItem>
          {/* 총 주문가격 */}
          <ListGroupItem>
            <Row className="d-flex justify-content-center">
              <Col sm="12" className="align-self-center">
                <Form.Group
                  as={Row}
                  className="mb-3"
                >
                  <Form.Label column sm="5">
                    총주문가격(원)
                  </Form.Label>
                  <Col sm="7" className="align-self-center">
                    {totalPrice}
                  </Col>
                </Form.Group>
              </Col>
            </Row>
          </ListGroupItem>
          {/* button box */}
          <ListGroupItem>
            <Row>
              <Form.Group>
              <Row className="d-flex justify-content-center">
                <Col sm={6} className="d-grid gap-2" >
                  <Button type="submit">장바구니담기</Button>
                </Col>
                <Col sm={6} className="d-grid gap-2" >
                  <Button type="button" onClick={handleOrderClick}>주문</ Button>
                </Col>
                </Row>
              </Form.Group>
            </Row>
          </ListGroupItem>
        </Form>
      </ListGroup>
    </>
  )
}

export default ItemOrderBoxForm