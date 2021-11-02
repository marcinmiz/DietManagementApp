import React, {useEffect} from 'react';
import "../Authentication.css";
import http from "../http-common";

export default function ResetPassword(props) {

    const [state, setState] = React.useState({
        password: "",
        passwordConfirmation: "",
        msg: "",
    });

    useEffect(
        () => {
        }, [state.msg]
    );

    const handleChange = (event) => {
        setState({
            ...state,
            [event.target.name]: event.target.value
        });
    };

    function validatePassword() {
        if (state.password === "") {
            setState({
                ...state,
                msg: "Password is required"
            });
            return "error";
        } else if (!/^[a-zA-Z0-9]+$/.test(state.password)) {
            setState({
                ...state,
                msg: "Password has to contain only letters and digits"
            });
            return "error";
        } else if (state.password.length < 8 || state.password.length > 40) {
            setState({
                ...state,
                msg: "Password has to have min 8 and max 40 characters"
            });
            return "error";
        } else if (state.password !== state.passwordConfirmation) {
            setState({
                ...state,
                msg: "PasswordConfirmation has to equals with Password"
            });
            return "error";
        } else {
            setState({
                ...state,
                msg: ""
            });
            return "";
        }
    }

    const handleSubmit = async (event) => {
        try {
            let result, credentials;
            console.log(state.password);

            if (validatePassword() !== "error") {
                credentials = {
                    "resetType": "forgot",
                    "token": props.token,
                    "password": state.password,
                    "passwordConfirmation": state.passwordConfirmation
                };

                result = await http.post("/api/users/resetPassword", credentials);

                // if (result.data.message === "User " + state.name + " " + state.surname + " has been registered") {
                setState({
                    ...state,
                    msg: result.data.message
                });
                // }
            }
        } catch (err) {

            setState({
                ...state,
                "msg": "invalid token"
            });
            console.error(err);
        }

    };

    return (
        <div>
            {state.msg !== "" ? <div className="msg">{state.msg}</div> : null}

            <form className="form">
                <div className="setting_header">Reset Password</div>
                <label htmlFor="password">Enter your new password</label>
                <input
                    className="input_field"
                    id="password"
                    type="password"
                    name="password"
                    value={state.password}
                    autoComplete="off"
                    onChange={event => handleChange(event)}
                    required/>

                <label htmlFor="passwordConfirmation">Confirm your new password</label>
                <input
                    className="input_field"
                    id="passwordConfirmation"
                    type="password"
                    name="passwordConfirmation"
                    value={state.passwordConfirmation}
                    autoComplete="off"
                    onChange={event => handleChange(event)}
                    required/>

                <button className="form_button" name="change_password_button" type="button"
                        onClick={event => handleSubmit(event)}>Change Password
                </button>

            </form>

        </div>
    );
}