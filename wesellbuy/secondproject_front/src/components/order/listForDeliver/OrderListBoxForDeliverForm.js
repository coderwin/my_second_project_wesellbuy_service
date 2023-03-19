import React, { useContext} from 'react'
import { Table } from 'react-bootstrap';
import "../../../css/form.css";
import { OrderListForDeliverContext } from '../OrderListForDeliverForm';
import OrderForDeliverForm from './OrderForDeliverForm';

/**
 * Order list box for deliver component
 * writer : 이호진
 * init : 2023.03.12
 * updated by writer :
 * update :
 * description : 주문 목록 box 배달원용 component
 */
const OrderListBoxForDeliverForm = () => {
  
  /// 변수 모음
  // 외부의 변수 불러오기
  const {listDatas, data: searchCond, totalPages} = useContext(OrderListForDeliverContext);

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
        <OrderForDeliverForm 
          key={i} 
          data={data} 
          numPosition={i}
          datasLength={listDatas.length} 
          totalPages={totalPages} 
          searchCond={searchCond} 
        />
      );
    });
  // 없으면 데이터가 존재하지 않는다고 알려주기
  } else {
    view = (
      <tr>
        <th>
          주문서가 없습니다.
        </th>
      </tr>
    );
  }

  return (
    <Table hover>
      <thead class="table-primary">
        <tr>
          <th>주문순서</th>
          <th>ID</th>
          <th>주문날짜</th>
          <th>주문상태</th>
          <th>배달상태</th>
          <th></th>{/* 배달 버튼 */}
        </tr>
      </thead>
      <tbody>
        {view}
      </tbody>
    </Table>
  )
}

export default OrderListBoxForDeliverForm;