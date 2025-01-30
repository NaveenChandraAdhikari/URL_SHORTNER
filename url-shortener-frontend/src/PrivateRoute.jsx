// best practice if the user is not authenticaed route it to login page

//based on authentication we are allowing user to access public or private page

import { Navigate } from "react-router-dom";
import { useStoreContext } from "./contextApi/ContextApi";

// children is the nested content / component ...like registerPage is nested inside privateroute
export default function PrivateRoute({ children, publicPage}) {
    const { token } = useStoreContext();


    if (publicPage) {
        return token ? <Navigate to="/dashboard" /> : children;
    }


    
    return !token ? <Navigate to="/login" /> : children;
}