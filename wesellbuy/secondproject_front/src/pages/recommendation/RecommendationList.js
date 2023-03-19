import React from 'react';
import {Route, Routes} from 'react-router-dom';
import RecommendationListForm from '../../components/recommendation/RecommendationListForm';

/**
 * Recommendation list page
 * writer : 이호진
 * init : 2023.02.21
 * updated by writer :
 * update :
 * description : 추천합니다글 목록 page
 */
function RecommendationList() {

    return (
        <>
            <RecommendationListForm />
        </>
    );
}

export default RecommendationList;