import styled from "styled-components";
import logo from "../public/images/logo.png";
import { Link } from "react-router-dom";


function Start () {
    return (
      <Box>
        <Intro>
          <h1><img src={logo} alt="당근마켓 로고" /></h1>
          <h2>당신 근처의 당근 마켓</h2>
          <p>중고 거래부터 동네 정보까지,<br/>지금 내 동네를 선택하고 시작해보세요!</p>
        </Intro>
        <Select>
          <WrapBottom>
            <button><Link to="/main">시작하기</Link></button>
            <p>이미 계정이 있나요? <span><Link to="/login">로그인</Link></span></p>
          </WrapBottom>
        </Select>
      </Box>
    );
  }
  
  const Box = styled.div`
    width: 100%;
    height: 100vh;
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
      display: flex;
      justify-content: center;
      img {
        width: 40%;
      }
    }
  
    h2 {
      font-weight: bold;
      margin-top: 10px;
    }
  
    p {
      text-align: center;
      margin-top: 20px;
    }
  `
  
  const Select = styled.div`
    position: relative;
  `;
  
  const WrapBottom = styled.div`
    width: 100%;
    height: 140px;
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
  
  export default Start;