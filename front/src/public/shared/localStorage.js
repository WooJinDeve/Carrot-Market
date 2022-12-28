
export function getToken() {
    return localStorage.getItem("accessToken");
}
  
export function removeToken() {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
}