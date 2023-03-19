import React from 'react';
import {Route, Routes} from 'react-router-dom';
import CustomerServiceListForm from '../../components/customerservice/CustomerServiceListForm';

/**
 * CustomerService list page
 * writer : 이호진
 * init : 2023.02.21
 * updated by writer :
 * update :
 * description : 고객지원글 목록 page
 */
function CustomerServiceList() {

    return (
        <CustomerServiceListForm />
    );
        
}

export default CustomerServiceList;