import React, { useContext} from 'react'
import { Table } from 'react-bootstrap';
import "../../../css/form.css";
import {OrderListContext} from '../OrderListForm';
import OrderForm from './OrderForm';

/**
 * Order list box component
 * writer : 이호진
 * init : 2023.03.11
 * updated by writer :
 * update :
 * description : 주문 목록 box component
 */
const OrderListBoxForm = () => {
  
  /// 변수 모음
  // 외부의 변수 불러오기
  const {listDatas, data: searchCond, totalPages} = useContext(OrderListContext);

  /// 상태 모음

  /// 메서드 모음

  /// view 모음
  let view = null;// 태그를 담아준다.
  // tbody에 들어갈 데이터 생성
  // 데이터가 있으면 생성한다
  if(listDatas) {
    // 데이터 만들기
    view = listDatas.map((data, i) => {
      return (
        <OrderForm 
          key={i} 
          data={data} 
          numPosition={i} 
          datasLength={listDatas.length} 
          totalPages={totalPages} 
          searchCond={searchCond} />
      );
    });
  // 없으면 데이터가 존재하지 않는다고 알려주기
  } else {
    view = (
      <tr>
        <th colSpan={10}>
          주문서가 없습니다.
        </th>
      </tr>
    );
  }

  return (
    <Table hover>
      <thead className="table-primary">
        <tr>
          <th>주문순서</th>
          <th>주문날짜</th>
          <th>주문상태</th>
          <th>배달상태</th>
          <th></th>{/* 취소 버튼 */}
        </tr>
      </thead>
      <tbody>
        {view}
      </tbody>
    </Table>
  )
}

export default OrderListBoxForm;