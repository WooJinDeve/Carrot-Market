import { BrowserRouter, Routes, Route } from "react-router-dom";
import Start from "./page/Start";
import NotFound from "./page/NotFound";
import Login from "./page/Login";
import Main from "./page/Main";
import UserLocation from "./page/UserLocation";
import OAuth2 from "./page/OAuth2"
import Location from "./page/Location"
import Mypage from "./page/Mypage"
function Router(){
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/location" element={<UserLocation />} />
                <Route path="/" element={<Start />} />
                <Route path="*" element={<NotFound />} />
                <Route path="/login" element={<Login />} />
                <Route path="/main" element={<Main />} />
                <Route path="/me/location" element={<Location />} />
                <Route path="/mypage" element={<Mypage />} />
                <Route path="/oauth2/redirect" element={<OAuth2 />} />
            </Routes>
        </BrowserRouter>
    )
}

export default Router;
