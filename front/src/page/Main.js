import '../public/css/listForm.css';
import { AiFillHome } from 'react-icons/ai';
import { IoIosNotificationsOutline } from "react-icons/io";
import { BiUser, BiMap } from 'react-icons/bi';
import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { getToken } from '../public/shared/localStorage';

function Main () {
    const navigate = useNavigate();
    const [pageState, setState] = useState([]);

    useEffect(() => {
        const token = getToken();
        if (!token) {
          navigate("/login");
        }
      }, [navigate]);


    const clickNotification = (e) => {
        navigate("/alarm");
    }
  
    return (
      <div className="Wrap">
        <div className="TMenuBar"> 
            <span> {"대전광역시 유성구 노은동"} </span>
            <IoIosNotificationsOutline onClick={clickNotification} size={"30px"}></IoIosNotificationsOutline>
        </div>
        <div className="topView">
          {pageState}
        </div>
        <div className="bottomView">
          <div className="BMenuBar"> 
            <div className="BMenuBox" onClick={() => { navigate("/main") }}>
              <AiFillHome size="30px" color={"black"}/>
              <p style={{color: "black"}}></p>홈</div>

            <div className="BMenuBox" onClick={() => { navigate("/me/location") }}>
              <BiMap size="40px" color={"#AAAAAA"}/>
                <p style={{color: "#AAAAAA"}}>내 지역</p> 
            </div>

            <div className="BMenuBox" onClick={() => { navigate("/mypage") }}>
              <BiUser size="40px" color={"#AAAAAA"}/>
                <p style={{color: "#AAAAAA"}}>나의 당근</p> 
            </div>          
          </div>
        </div>
      </div>
    )
  }
  
  export default Main;