import { useRef, useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import styled from "styled-components";
import HeaderBack from "../components/HeaderBack";
import { getToken } from '../public/shared/localStorage';


function UserLocation () {
  const [locations] = useState([
    { fullName: "서울시 강남구 삼성동" },
    { fullName: "서울시 강남구 대치동" },
    { fullName: "서울시 강남구 역삼동" },
    { fullName: "서울시 강남구 일원동" }
  ]);

  const ref = {
    location: useRef(null)
  }

  const navigate = useNavigate();

  const locationClick = (name) => {
    navigate("/register", { state : name });
  }

  useEffect(() => {
    const token = getToken();
    if (!token) {
      navigate("/login");
    }
  }, [navigate]);

  return (
    <Box>
      <HeaderBack />
      <Content>
        <h1>근처 동네</h1>
        <Locations>
          {locations?.map(
            (location, index) => {
              return (
                <Location key={index} onClick={() => {locationClick(location.fullName)}}>
                    {location.fullName}
                </Location>
              )
            }
          )}
        </Locations>
      </Content>
    </Box>
  )
}

const Box = styled.div`
  display: flex;
  flex-direction: column;
`;

const Content = styled.div`
  padding: 10px 20px 0;
  h1 {
    font-weight: bold;
  }
`;

const Locations = styled.div`
  margin-top: 20px;
  position: absolute;
  width: 100%;
  left: 0;
`;

const Location = styled.div`
  padding: 0 20px;
  width: 100%;
  height: 60px;
  display: flex;
  align-items: center;
  border-bottom: 1px solid #ddd;
  cursor: pointer;
  transition: background-color .3s;

  &:hover {
    background-color: #eee;
  }
`;

export default UserLocation;