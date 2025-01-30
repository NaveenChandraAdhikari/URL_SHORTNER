import { createContext, useContext, useState } from "react";

const ContextApi = createContext();



export const ContextProvider = ({ children }) => {
    const getToken = localStorage.getItem("JWT_TOKEN")
        ? JSON.parse(localStorage.getItem("JWT_TOKEN"))
        : null;

    const [token, setToken] = useState(getToken);

    // data to share across the application
    const sendData = {
        token,
        setToken,
    };

    return <ContextApi.Provider value={sendData}>{children}</ContextApi.Provider>
};

// custom hook to access the context because prior we have to import useContext (ContextApi) those both 

export const useStoreContext = () => {
    const context = useContext(ContextApi);
    return context;
}