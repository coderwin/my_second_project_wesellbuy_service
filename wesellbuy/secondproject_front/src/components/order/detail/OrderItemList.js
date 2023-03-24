import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../../../css/form.css';

/**
 * Order item list of Order detail box component
 * writer : 이호진
 * init : 2023.03.12
 * updated by writer :
 * update :
 * description : 주문 상세보기 주문상품 목록 component
 *               props
 *                 > orderItem: 주문 상품 prop
 *                 > num : list 순서 
 */
const OrderItemList = ({orderItem, num}) => {

  /// 변수 모음
  const navigation = useNavigate();// navigation

  /// 메서드 모음
  // 상품명 클릭했을 때
    // 상품 상세보기로 이동한다.
  function handleItemNameClick(e) {
    const itemNum = e.target.id;// 상품번호
    // 상품 상세보기로 이동하기
    navigation(`/item/${itemNum}`);
  }

  return (
    <>
      <tr>
        <th>{num + 1}</th>
        <th 
          id={orderItem.itemNum} 
          className="mousePointer"
          onClick={handleItemNameClick}
        >
            {orderItem.itemName}
        </th>
        <th>{orderItem.quantity}</th>
        <th>{orderItem.itemPrice}</th>
      </tr>
    </>
  );
}

export default OrderItemList