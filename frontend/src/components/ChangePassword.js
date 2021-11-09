import React, {useEffect} from 'react';
import "../Authentication.css";
import http from "../http-common";
import {makeStyles} from "@material-ui/core";

const useStyles = makeStyles({
    passwordForm: {
        minHeight: 'min-content !important',
    }
});

export default function ChangePassword(props) {
    const classes = useStyles();

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
            console.log(props.resetType);

            if (validatePassword() !== "error") {
                credentials = {
                    "resetType": props.resetType,
                    "token": props.token,
                    "password": state.password,
                    "passwordConfirmation": state.passwordConfirmation
                };

                result = await http.post("/api/users/changePassword", credentials);

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

            <form className={classes.passwordForm + " form"}>
                <div className="setting_header">Change Password</div>
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