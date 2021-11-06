import React, {useEffect} from 'react';
import "../Authentication.css";
import {useHistory} from "react-router-dom";
import ChangePassword from "../components/ChangePassword";
import http from "../http-common";

export default function ResetPasswordPage(props) {
    const history = useHistory();
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const token = urlParams.get('token');

    const [state, setState] = React.useState({
        msg: ""
    });

    useEffect(
        async () => {
            try {
                let result, credentials;

                credentials = {
                    "token": token
                };

                result = await http.post("/api/users/checkTokenValidity", credentials);

                // if (result.data.message === "User " + state.name + " " + state.surname + " has been registered") {
                setState({
                    ...state,
                    "msg": result.data.message,
                    "loaded": true
                });
                // }

                return result.data.message;

            } catch (err) {

                setState({
                    ...state,
                    "msg": "bad email",
                    "loaded": true
                });
                console.error(err);
            }
        }, [state.msg]
    );

    if (state.msg === "Token is valid") {
        return (
            <div className="auth_container auth_container_login">
                <div className="card auth_card">
                    <div className="logo">
                        DIETIX
                    </div>
                    <ChangePassword resetType="forgot" token={token}/>
                    <button className="form_button" name="back_login_button" type="button"
                            onClick={() => history.push('/login')}>
                        Back to login page
                    </button>
                </div>
            </div>
        );
    } else {
        return (
            <div className="auth_container auth_container_login">
                <div className="card auth_card">
                    <div className="logo">
                        DIETIX
                    </div>
                    <div className="form setting_header">{state.msg}</div>
                </div>
            </div>
        );
    }
}