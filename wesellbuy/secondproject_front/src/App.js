import React, { createContext, useState } from 'react';
import {Route, Routes} from 'react-router-dom';
import Header from './pages/Header';
import Footer from './pages/Footer';

// import item
import Home from './pages/item/Home';
import ItemDetail from './pages/item/ItemDetail';
import ItemUpdate from './pages/item/ItemUpdate';
import ItemRank from './pages/item/ItemRank';

// import member
import Login from './pages/member/Login';
import Join from './pages/member/Join';
import MemberDetail from './pages/member/MemberDetail';
import IdSearch from './pages/member/help/IdSearch'
import PwdSearch from './pages/member/help/PwdSearch'

// import customerservice
import CustomerServiceList from './pages/customerservice/CustomerServiceList';

// import recommendation
import RecommendationList from './pages/recommendation/RecommendationList';
import RecommendationDetail from './pages/recommendation/RecommendationDetail';
import RecommendationUpdate from './pages/recommendation/RecommendationUpdate';
import RecommendationSave from './pages/recommendation/RecommendationSave'

// import error page
import NotFound from './pages/error/NotFound';
import IdSearchAnswer from './pages/member/help/IdSearchAnswer';
import PwdSearchAnswer from './pages/member/help/PwdSearchAnswer';
import MemberUpdate from './pages/member/MemberUpdte';
import CustomerServiceDetail from './pages/customerservice/CustomerServiceDetail';
import ItemSave from './pages/item/ItemSave';
import OrderSave from './pages/order/OrderSave';
import OrderList from './pages/order/OrderList';
import OrderDetail from './pages/order/OrderDetail';
import OrderListForSeller from './pages/order/OrderListForSeller';
import OrderListForDeliver from './pages/order/OrderListForDeliver';
import CustomerServiceSave from './pages/customerservice/CustomerServiceSave';
import Loding from './components/Loding';

// context 만들기
export const CustomContext = createContext(null);

function App() {

  /// 상태 모음
  // 로그인/비로그인 확인을 위한 상태
  const [sessionForm, setSessionForm] = useState(null); // 로그인한 사용자 데이터 담는 상태
  const [loding, setLoding] = useState(false); // 요청 처리 상태

  // 메서드 모음
  /// 로그인 데이터 sessionForm에 담기
  function handleSessionFormDataInput() { 
    // sessionForm에 넣어주기
    setSessionForm(() => {
      // session에서 로그인한 사용자 데이터 불러오기
      const sessionId = "LOGIN_MEMBER";
      // 회원정보 가져오기
      return JSON.parse(sessionStorage.getItem(sessionId));
    });
  }
  /// sessionForm의 데이터 바꾸기
  function handleSessionFormChangeData(value) {
    setSessionForm(value);
  }
  /// input type:date 현재 날짜로 만들기
  function getCurrentDate() {
    return new Date().toISOString().slice(0, 10);
  }
  // change loding value
  function changeLoding(result) {
    setLoding(() => result);
  }


  /// view 모음
  // 요청 처리 view

  if(loding) return (<Loding />);

  return (
    <>
      <CustomContext.Provider value={{sessionForm, handleSessionFormDataInput, handleSessionFormChangeData, getCurrentDate, setLoding, changeLoding}}>
        <Header />

        <Routes>
          {/* item list */}
          <Route path="/" element={<Home />} />
          <Route path="/item/new" element={<ItemSave />} />
          <Route path="/item/:num" element={<ItemDetail />}></Route>
          <Route path="/item/:num/update" element={<ItemUpdate />}></Route>
          <Route path="/item/rank" element={<ItemRank />}></Route>
          
          {/* login */}
          <Route path="/login" element={<Login />} />
          {/* help */}
          <Route path="/help/search/id" element={<IdSearch />} />
          <Route path="/help/search/id/answer" element={<IdSearchAnswer />} />
          <Route path="/help/search/pwd" element={<PwdSearch />} />
          <Route path="/help/search/pwd/answer" element={<PwdSearchAnswer />} />
          {/* Join */}
          <Route path="/join" element={<Join />} />
          {/* member detail info */}
          <Route path="/mydetail" element={<MemberDetail />} />
          <Route path="/mydetail/update" element={<MemberUpdate />} />

          {/* customerservice list */}
          <Route path="/cs/new" element={<CustomerServiceSave />} />
          <Route path="/cs/list" element={<CustomerServiceList />} />
          <Route path="/cs/:num" element={<CustomerServiceDetail />} />

          {/* recommendation list */}
          <Route path="/recommendation/new" element={<RecommendationSave />} />
          <Route path="/recommendation/list" element={<RecommendationList />}  />
          <Route path="/recommendation/:num" element={<RecommendationDetail />} />
          <Route path="/recommendation/:num/update" element={<RecommendationUpdate />}  /> 

          {/* order list */}
          <Route path="/order/new" element={<OrderSave />} />
          <Route path="/order/list" element={<OrderList />} />
          <Route path="/order/:num" element={<OrderDetail />} />
          <Route path="/order/list/seller" element={<OrderListForSeller />} />

          {/* delivery list for deliver */}
          {/* 서버를 따로 만들어야 하지 않을까? */}
          <Route path="/order/list/deliver" element={<OrderListForDeliver />} />

          {/* admin list for admin */}
          {/* 서버를 따로 만들어야 하지 않을까? */}

          {/* 404 page */}
          <Route path="/*" element={<NotFound />} />
          {/* 클라이언트의 잘못된 요청 */}
          <Route path="/errors/notfound" element={<NotFound />} />
        </Routes>

        <Footer />
      </CustomContext.Provider>
    </>
  );
}

export default App;
