import styled from "styled-components";
import { FaArrowLeft } from "react-icons/fa";
import { useNavigate } from "react-router-dom";

function HeaderBack () {
  const navigate = useNavigate();

  return (
    <Box>
      <FaArrowLeft onClick={() => {
        navigate(-1); // Go Backward, 1이면 Forward
      }} />
    </Box>
  );
}

const Box = styled.div`
  width: 100%;
  height: 60px;
  display: flex;
  align-items: center;
  font-size: 24px;
  padding: 0 20px;
`;

export default HeaderBack;