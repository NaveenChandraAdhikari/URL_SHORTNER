
import axios from "axios";


// i have created the custom access instance of axios instead of direct calling in register page

export default axios.create({
    baseURL: import.meta.env.VITE_BACKEND_URL,
});