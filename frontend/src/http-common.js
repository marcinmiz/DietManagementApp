import axios from "axios";

export default axios.create({
    baseURL: "http://localhost:8067",
    headers: {
        "Content-type": "application/json"
    }
});