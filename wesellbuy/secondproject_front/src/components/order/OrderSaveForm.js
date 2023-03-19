import axios from 'axios';
import React, { useCallback, useContext, useEffect, useState } from 'react'
import { Button, Col, Container, ListGroup, ListGroupItem, Row } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import Loding from '../Loding';
import OrderItemForm from './save/OrderItemForm';
import '../../css/form.css';
import { CustomContext } from '../../App';

/**
 * Order Save component
 * writer : 이호진
 * init : 2023.03.11
 * updated by writer :
 * update :
 * description : 주문하기(주문 저장) component
 */

const OrderSaveForm = () => {

  /// 변수 모음
  // navigation
  const navigation = useNavigate();
  // 외부 변수, 상태, 메서드 불러오기
  const {setLoding: setAllLoding} = useContext(CustomContext);

  /// 상태 모음
  const [loding, setLoding] = useState(false);// 요청처리 상태
  const [payLoding, setPayLoding] = useState(false);// 요청처리 상태
  const [datas, setDatas] = useState(null);// 장바구니에 있는 상품 목록 상태
  const [readyOrder, setReadyOrder] = useState([]);// 주문하기 전 예비주문 상태
  const [order, setOrder] = useState([]);// 주문하기 클릭했을 때 최종주문상품 상태
  const [allTotalPrice, setAllTotalPrice] = useState(0);// 모든 주문상품 총가격 합 상태
  const [memberInfo, setMemberInfo] = useState(null);// 회원정보 상태


  /// 메서드 모음
  // localStorage에 있는 장바구니 불러오기
  function getShopingBasket() {
    // LocalStorage에 shopingBasket 있는지 확인하기
    let strShopingBasket = localStorage.getItem("shopingBasket");
    // json parse 해준다
    const shopingBasket = JSON.parse(strShopingBasket);
    // datas에 담기
    setDatas(shopingBasket);
    // readyOrder에 담기 => 안 담는다.
    // setReadyOrder(shopingBasket);
  }
  // sessionStorage에서 회원정보 불러오기
  function getMemberInfo() {
    const key = "LOGIN_MEMBER";
    const memberData = JSON.parse(sessionStorage.getItem(key));
    // memberInfo에 담기
    setMemberInfo(memberData);
  }
  // 주문하기 클릭했을 때
  async function handlePayClick() {
    const paidMoney = window.prompt("지불금액을 입력하세요", "숫자만 가능합니다.");
    // 숫자인지 아닌지 조사하기
    // 숫자아닌 게 하나라도 있으면 안 된다
    const pattern = /[^0-9]/;// 숫자가 아닌 것 조사
    const result = pattern.test(paidMoney);
    // true
    if(result) {
      // 경고창 띄우기
      alert("다시 입력하세요");
      return;
    // false
    } else {
      // paidMoney를 Number로 만들기
      const paidMoneyNum = Number(paidMoney);
      // order 생성하기
      const newOrder = createOrder();
      // order가 처음과 바꼈는지 확인하기
      // 서버로 주문등록하기
      if(memberInfo) {
        if(newOrder) {
          // loding = true
          setAllLoding(true);
          try {
            const response = await save(paidMoneyNum);
            // 요청 성공
            // loding = false
            setAllLoding(false);
            console.log("요청 성공");
            alert(response.data.data);
            // 내 주문보기로 이동하기
            navigation("/order/list");
          } catch(err) {
            // 요청 실패
            // loding = false
            setAllLoding(false);
            console.log("요청 실패");
            // field 데이터가 잘못 입력된 에러
            if(err.response.data.errors) {
              // 순회하며 알려주기
              for(let key in err.response.data.errors) {
                // field의 에러메시지 객체 담기
                const newErrMsg = err.response.data.errors[key];
                // errMsg 뿌려주기
                alert(newErrMsg.errMsg);
              }
              return;
            }

            // 계산 중 에러
            if(err.response.data.errMsg) {
              // console.log(err.response.data.errMsg);
              // 클라이언트에게 에러 이유 알려주기
              alert(err.response.data.errMsg);
            }
            return;
          }
        }
      }
    }
  }
  // 주문결제버튼 눌렀을 때 order 생성하기
  // 서버에 필요한 데이터로 만든다.
  const createOrder = useCallback(() => {
    let newOrder = null;
    if(readyOrder) {
      newOrder = readyOrder.map((item) => {
        return {
          quantity: item.quantity,
          itemNum: item.itemNum
        };
      });
    }
    return newOrder;
  }, []);
  // 서버로 주문등록하기
  async function save(paidMoney) {
    return await axios.post(
      "http://localhost:8080/orders",
      {
        data: readyOrder,
        paidMoney: paidMoney
      },
      {
        withCredentials: true
      }
    );
  }
  // 주문상품 삭제 클릭했을 때
  function handleDeleteBtnClick(e) {
    // 버튼의 id 확인하기
    console.log(`delete id: ${e.target.id}`);
    // datas에서 itemData의 id가 같은 걸 삭제한다(filter)
    const newDatas = datas.filter((data) => data.id !== Number(e.target.id));
    console.log(newDatas);
    setDatas(
      datas.filter((data) => data.id !== e.target.id
    ));
    // readyOrder에서 itemData의 id가 같은 걸 삭제한다(filter)
    setReadyOrder(
      readyOrder.filter((item) => item.id !== e.target.id
    ));
    // shopingBaskik에서 빼주자
    changeShopingBaskit(newDatas);
    // loding이 안 되네
    navigation(0); 
  }
  // change shopingBaskit in localStorage
  function changeShopingBaskit(newDatas) {
    // string으로 만들기
    const datas = JSON.stringify(newDatas);
    // localStorage에 담기
    const key = "shopingBasket";
    localStorage.setItem(key, datas);
  }
  // 주문상품 선택을 클릭했을 때
  // readyOrder의 데이터도 바꿔준다.
  //    - params
  //        > selection : true or false
  //        > itemData : 한 개의 상품 데이터
  function handleSelectionChange(selection, itemData) {
    
    // true
    if(selection) {
      // readyOrder에 itemData 담기
      for(let i = 0; i < datas.length; i++) {
        // id가 같으면 readyOrder에 담기
        if(datas[i].id === itemData.id) {
          setReadyOrder((readyOrder) => {
            return [
              ...readyOrder,
              itemData
            ];
          });
        }
      }
    // false
    } else {
      // readyOrder에서 itemData를 뺀다
      setReadyOrder(
        readyOrder.filter((item) => {
          return item.id !== itemData.id;
        })
      );
    }
  }

  /// 처음 시작
  
  useEffect(() => {
    // loding
    setLoding(true);
    getShopingBasket();
    // 작업 끝남
    setLoding(false);
  }, []);
  // 회원정보 불러오기
  useEffect(() => {
    getMemberInfo();
  }, []);
  // allTotalPrice 구하기
  useEffect(() => {
    let newAllTotalPrice = [];
    
    if(readyOrder.length !== 0) {
      newAllTotalPrice = readyOrder.map((data) => {
        return data.quantity * data.price;
      });
      // 모든 배열값 더하기
      const sum = newAllTotalPrice.reduce((a, b) => a + b);
      // allTotalPrice에 담기
      setAllTotalPrice(sum);
    } else {
      setAllTotalPrice(0);
    }
  }, [readyOrder]);

  /// view 모음
  let itemList = null;// 장바구니에 담긴 상품 목록
  // 주문 상품들 하나씩 OrderItemForm으로 만들기
  // i는 주문상품 삭제할 때 사용
  if(datas) {
    itemList = datas.map((data, i) => {
      return (
          <OrderItemForm 
            key={i} 
            data={data} 
            num={i} 
            datasLength={datas.length}
            handleDeleteBtnClick={handleDeleteBtnClick}
            setReadyOrder={setReadyOrder}
            readyOrder={readyOrder}
            handleSelectionChange={handleSelectionChange}
          />
      );
    });
  }

  if(loding) return (<Loding />);// 요청 처리 view

  if(payLoding) return (<Loding />);// 결제 처리 view

  return (
    <Container className="body_text_center">
      <Row className="d-flex justify-content-center">
        <Col sm={10}>
          <ListGroup>
            {/* order header -> 제목 */}
            <ListGroupItem>
              <Row>
                <Col sm="1">선택</Col>
                <Col sm="2">주문번호</Col>
                <Col sm="2">상품명</Col>
                <Col sm="2">주문수량</Col>
                <Col sm="2">상품가격</Col>
                <Col sm="2">총가격</Col>
                <Col sm="2"></Col>{/* 삭제 버튼  */}
              </Row>
            </ListGroupItem>
            {/* order body -> 주문 상품들 뿌려주기 */}
            {
              itemList !== null ? 
              itemList : 
              <ListGroupItem>
                <Col>주문상품이 없습니다.</Col>
              </ListGroupItem>
            }
          </ListGroup>
          {/* 선택한 상품들 결제금액 */}
          <ListGroup>
            <ListGroupItem>
              <Row>
                <Col sm="4">결제금액</Col>
                <Col sm="8">
                  <span>{allTotalPrice}</span>
                  <span>원</span>
                </Col>
              </Row>
            </ListGroupItem>
            <ListGroupItem>
              <Row className="d-flex justify-content-center d-grid gap-2">
                <Col sm={2} className="d-grid gap-2" >
                  <Button stype="button" onClick={handlePayClick}>결제하기</Button>
                </Col>
              </Row>
            </ListGroupItem>
          </ListGroup>
          </Col>
      </Row>
    </Container>
  );
}

export default OrderSaveForm