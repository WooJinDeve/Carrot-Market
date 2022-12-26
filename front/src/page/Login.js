import styled from "styled-components";
import { KAKAO_AUTH_URL } from '../public/api/oauth2';
import HeaderBack from "../components/HeaderBack";
import kakao from "../public/images/kakao.png";

function Login () {

    return (
        <Box>
          <HeaderBack />
          <Content>
            <Form>
                <a className="btn btn-block social-btn kakao" href={KAKAO_AUTH_URL}>
                    <img src={kakao} alt="카카오 로그인" /></a>
            </Form>
          </Content>
        </Box>
      );
}

const Box = styled.div``;

const Content = styled.div`
  padding: 40px 20px 0;

  em {
    font-size: 20px;
    font-weight: bold;
    line-height: 1.4;
  }

  p {
    font-size: 12px;
    margin-top: 50px;
  }
`;

const Form = styled.form`
  display: flex;
  flex-direction: column;
  margin-top: 250px;
  align-items: center;
  justify-content: center;
  
`;

export default Login;