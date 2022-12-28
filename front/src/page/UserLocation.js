import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import styled  from "styled-components";
import HeaderBack from "../components/HeaderBack";
import { getToken } from '../public/shared/localStorage';
import { instance } from "../public/api/axios";


function UserLocation () {
  const navigate = useNavigate();
  const [locations, SetLocations] = useState([]);
  const [state, setState] = useState();

  useEffect(() => {
    const token = getToken();
    if (!token) {
      navigate("/login");
    }else{
      instance.get(`api/v1/locations?state=서울`)
      .then((res) => {
        console.log(res.data.result)
        SetLocations(res.data.result.content)
      })
      .catch((err) => {
        console.log(err);
      })
    }
  }, [navigate]);

  const locationClick = (id) => {
    instance.post(`api/v1/users/location`, {
      regionId: id
    })
    .then((res) => {
      console.log(res.data.result)
      navigate("/main");
    })
    .catch((err) => {
      console.log(err);
    });
  }

  const changeLocation = (e) => {
    setState(e.target.value);
  }

  const locationSearchClick = () => {
    instance.get(`api/v1/locations?state=${state}`)
    .then((res) => {
      console.log(res.data.result)
      SetLocations(res.data.result.content)
    })
    .catch((err) => {
      console.log(err);
    })
  }

  return (
    <Box>
      <HeaderBack />
      <Content>
      <Form>
        <input type="text" placeholder="🔍 내 동내이름(동)으로 검색" onChange={changeLocation}/>
      </Form>
      <WrapBottom><button onClick={locationSearchClick}><a>동내 검색</a></button></WrapBottom>
        <h1>근처 동네</h1>
        <Locations>
          {locations?.map(
            (location, id) => {
              return (
                <Location key={id} onClick={() => {locationClick(location.id)}}>
                    {location.name}
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
    padding: 5px 40px;
    font-weight: bold;
  }
`;

const Locations = styled.div`
  margin-top: 20px;
  justify-content: center;
  position: absolute;
  width: 100%;
  left: 0;
`;

const Location = styled.div`
  padding: 0 60px;
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

const Form = styled.form`
  width: 100%;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  input {
    width: 85%;
    height: 50px;
    border: 1px solid #BBB;
    height: 50px;
    border-radius: 5px;
    padding: 0 10px;

    &::placeholder {
      color: #ccc;
    }
  }

  input + input {
    margin-top: 10px;
  }

`;

export default UserLocation;