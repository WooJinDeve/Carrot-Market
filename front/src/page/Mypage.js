import '../public/css/listForm.css';
import { AiFillHome } from 'react-icons/ai';
import { BiUser, BiMap } from 'react-icons/bi';
import React from "react";
import { useNavigate } from "react-router-dom";
import {removeToken} from "../public/shared/localStorage"

function Location () {
    const navigate = useNavigate();
  
    const logout = (e) => {
        removeToken();
        navigate("/");
      }

    return (
      <div className="Wrap">
        <div className="TMenuBar"> 
            <p>나의 당근</p>
            <p onClick={logout} style={{cursor: "pointer"}}> 로그아웃 </p> 
        </div>
        <div className="topView">

        </div>
        <div className="bottomView">
          <div className="BMenuBar"> 
            <div className="BMenuBox" onClick={() => { navigate("/main") }}>
              <AiFillHome size="30px" color={"#AAAAAA"}/>
              <p style={{color: "#AAAAAA"}}></p>홈</div>
            <div className="BMenuBox" onClick={() => { navigate("/me/location") }}>
              <BiMap size="40px" color={"#AAAAAA"}/>
                <p style={{color: "#AAAAAA"}}>내 지역</p> 
            </div>
            <div className="BMenuBox" onClick={() => { navigate("/mypage") }}>
              <BiUser size="40px" color={"black"}/>
                <p style={{color: "black"}}>나의 당근</p> 
            </div>          
          </div>
        </div>
      </div>
    )
  }
  
  export default Location;
