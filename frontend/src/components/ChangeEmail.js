import React, {useEffect} from 'react';
import "../Authentication.css";
import http from "../http-common";

export default function ResetPassword(props) {

    const [state, setState] = React.useState({
        email: "",
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

    function validateEmail() {
        if (state.email === "") {
            setState({
                ...state,
                msg: "E-mail address is required"
            });
            return "error";
        } else if (!/^[a-zA-Z0-9._]+@[a-zA-Z0-9.]+.[a-zA-Z]+$/.test(state.email)) {
            setState({
                ...state,
                msg: "E-mail address has to have format id@example.com"
            });
            return "error";
        } else if (state.email.length < 6 || state.email.length > 40) {
            setState({
                ...state,
                msg: "E-mail address has to have min 6 and max 40 characters"
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
            console.log(state.email);

            if (validateEmail() !== "error") {
                credentials = {
                    "email": state.email
                };

                result = await http.post("/api/users/changeEmail", credentials);

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
                "msg": "user is not logged"
            });
            console.error(err);
        }

    };

    return (
        <div>
            {state.msg !== "" ? <div className="msg">{state.msg}</div> : null}

            <form className="form">
                <div className="setting_header">Change E-mail</div>
                <label htmlFor="email">Enter your new e-mail</label>
                <input
                    className="input_field"
                    id="email"
                    type="email"
                    name="email"
                    value={state.email}
                    autoComplete="off"
                    onChange={event => handleChange(event)}
                    required/>

                <button className="form_button" name="change_password_button" type="button"
                        onClick={event => handleSubmit(event)}>Change E-mail
                </button>

            </form>

        </div>
    );
}