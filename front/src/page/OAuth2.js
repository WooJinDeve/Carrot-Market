import {React, useEffect} from 'react';
import { ACCESS_TOKEN, REFRESH_TOKEN } from '../public/api/oauth2';
import qs from "query-string"; 
import { useLocation } from 'react-router';
import { useNavigate } from "react-router-dom";

function OAuth2() {

    const navigate = useNavigate();
    const searchParams = useLocation().search;
    const query = qs.parse(searchParams);
    const refreshToken = query.refreshToken;
    const accessToken = query.accessToken;

    useEffect(() => {
        const loginSocial = async () => {
          if (accessToken && refreshToken) {
            localStorage.setItem(ACCESS_TOKEN, query.refreshToken);
            localStorage.setItem(REFRESH_TOKEN, query.accessToken);
            console.log(accessToken);
            console.log(refreshToken);
          }
        };
        loginSocial();
        navigate("/");
      }, []);
    
    return <div></div>;
};


export default OAuth2;