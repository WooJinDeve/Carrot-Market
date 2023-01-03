import '../public/css/listForm.css';
import { AiFillHome } from 'react-icons/ai';
import { BiUser, BiMap } from 'react-icons/bi';
import { FaPlus } from "react-icons/fa";
import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import styled from "styled-components";
import map from "../public/images/map.png";
import HeaderBack from "../components/HeaderBack";
import { instance } from "../public/api/axios";
import { getToken } from '../public/shared/localStorage';


function Location () {
    const navigate = useNavigate();
    const [locations, SetLocations] = useState([
]);

    useEffect(() => {
        const token = getToken();
        if (!token) {
          navigate("/login");
        }else{
          instance.get(`api/v1/users/location`)
          .then((res) => {
            console.log(res.data.result)
            if(!res.data.result){
                navigate("/location");
            }
            SetLocations(res.data.result)
          })
          .catch((err) => {
                console.log(err)
                navigate("/main");
          })
        }
      }, [navigate]);
  
    return (
      <div className="Wrap">
        <div className="TMenuBar"> 
            <HeaderBack />
        </div>
        <div className="topView">
        <Box>
            <Intro>
                <h2>동네 선택</h2>
                <p>지역은 최소 1개 이상 최대 2개까지 설정할 수 있어요.</p>
             </Intro>
                {locations?.map(
                    (location, id) => {
                    return (
                        <WrapBottom>
                        <button key={id} onClick={() => {}}>
                            <a>{location.name}</a>
                        </button>
                        </WrapBottom>
                    )
                }
                )}
             <Intro>
                <h1><img src={map} alt="당근마켓 로고" /></h1>
            </Intro>

            <FixedButton>
                <FaPlus
                    onClick={() => {
                    navigate("/location");
                    }}
                />
            </FixedButton>
            </Box>

        </div>
        <div className="bottomView">
          <div className="BMenuBar"> 
            <div className="BMenuBox" onClick={() => { navigate("/main") }}>
              <AiFillHome size="30px" color={"#AAAAAA"}/>
              <p style={{color: "#AAAAAA"}}></p>홈</div>
            <div className="BMenuBox" onClick={() => { navigate("/me/location") }}>
              <BiMap size="40px" color={"black"}/>
                <p style={{color: "black"}}>내 지역</p> 
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
  
  export default Location;

  const Box = styled.div`
    height: 100%;
    margin-top: 20px;
    height: 65vh;
    display: flex;
    flex-direction: column;
  `;

  const Intro = styled.div`
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  
    h1 {
     margin-top: 10px;

      display: flex;
      justify-content: center;
      img {
        width: 40%;
      }
    }
  
    h2 {
      font-size: 20px;

      font-weight: bold;
      margin-top: 10px;
    }
  
    p {
      font-size: 15px;

      text-align: center;
      margin-top: 20px;
    }
  `

const WrapBottom = styled.div`
width: 100%;
height: 100px;
flex-shrink: 0;
display: flex;
flex-direction: column;
justify-content: center;
align-items: center;

button {
  width: 85%;
  height: 50px;
  border: none;
  border-radius: 5px;
  color: ${props => props.theme.color.white};
  font-weight: bold;

  a {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: ${props => props.theme.color.orange};
    transition: background .3s;

    &:hover {
      background-color: ${props => props.theme.hoverColor.orange};
    }
  }
}

p {
  margin-top: 30px;
  span {
    color: ${props => props.theme.color.orange};
  }
}
`;

const FixedButton = styled.div`
  display: flex;
  position: fixed;
  bottom: 120px;
  right: 30px;
  width: 70px;
  height: 70px;
  font-size: 30px;
  background-color: ${(props) => props.theme.color.orange};
  color: ${(props) => props.theme.color.white};
  border-radius: 50%;
  justify-content: center;
  align-items: center;
  box-shadow: 0 0 6px 0 #999;
`;