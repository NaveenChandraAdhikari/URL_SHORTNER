
import { BrowserRouter as Router } from "react-router-dom";
import "./App.css";
// import LandingPage from "./components/LandingPage";
// import AboutPage from "./components/AboutPage";
// import Footer from "./components/Footer";
// import NavBar from "./components/NavBar";
// import RegisterPage from "./components/RegisterPage";
// import { Toaster } from "react-hot-toast";
// import LoginPage from "./components/LoginPage";
// import DashboardLayout from "./Dashboard/DashboardLayout";
import { getApps } from "./utils/helper";

function App() {

  const CurrentApp=getApps();

  return (
    <>
     

     <Router>
{/*      
<NavBar/>

<Toaster position="bottom-center" reverseOrder />

     <Routes>

      <Route path='/' element={<LandingPage/>}  />
      <Route path='/about' element={<AboutPage/>}  />
      <Route path='/register' element={<RegisterPage/>}  />
      <Route path='/login' element={<LoginPage/>}  />
      <Route path='/dashboard' element={<DashboardLayout/>}  />
     </Routes>

<Footer/> */}

<CurrentApp/>

     </Router>

    </>
  );
}

export default App; 

// we have 2 routing set up the approuter is the main one router is subdomainrouter...now based  on the subdomain we need to render the appropriate routes ..for that we have to know the host and that host has subdomain or not ..for that we ahve created helper.js and the config we make constant.js ..with help of getApps we are evaluating which router should be loaded 


