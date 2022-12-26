import { createGlobalStyle, ThemeProvider } from "styled-components";
import './App.css';
import reset from "./public/css/reset.css";
import { defaultTheme } from "./public/shared/theme";
import Router from "./Router";


function App() {

  return (
    <ThemeProvider theme={defaultTheme}>
      <GlobalStyle />
      <Router />
    </ThemeProvider>
  );
}


const GlobalStyle = createGlobalStyle`
${reset}; // Reset CSS

body, button, input, textarea {
  color: #444444;
  font-family: ${(props) => props.theme.fontFamily.default}, sans-serif;
}

a {
  text-decoration: none;
  color: inherit;
}
`;

export default App;
