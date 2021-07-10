import axios from "axios";

export default axios.create({
    baseURL: "http://localhost:8097",
    headers: {
        "Content-type": "application/json"
    }
});