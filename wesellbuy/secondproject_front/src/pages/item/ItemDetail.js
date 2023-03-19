import React from 'react';
import {Route, Routes} from 'react-router-dom';
import ItemDetailForm from '../../components/item/ItemDetailForm';
import ItemUpdate from './ItemUpdate'

/**
 * Item detail page
 * writer : 이호진
 * init : 2023.02.22
 * updated by writer :
 * update :
 * description : 상품 상세보기 page
 */
function ItemDetail() {

    return (
        <>
            <ItemDetailForm />
        </>
    );
}

export default ItemDetail;