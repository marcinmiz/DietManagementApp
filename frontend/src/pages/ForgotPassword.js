import React, {useEffect} from 'react';
import "../Authentication.css";
import {useHistory} from "react-router-dom";
import http from "../http-common";
import Snackbar from "@material-ui/core/Snackbar/Snackbar";
import IconButton from "@material-ui/core/IconButton";
import CloseIcon from "@material-ui/core/SvgIcon/SvgIcon";

export default function ForgotPassword(props) {
    const history = useHistory();

    const [state, setState] = React.useState({
        email: "",
        msg: "",
    });

    const [open, setOpen] = React.useState(false);

    const handleOpen = () => {
        setOpen(true);
    };

    const handleClose = async (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        await setState({
            ...state,
            msg: ""
        });
        setOpen(false);
    };

    const action = (
        <React.Fragment>
            <IconButton
                size="small"
                aria-label="close"
                color="inherit"
                onClick={handleClose}
            >
                <CloseIcon fontSize="small"/>
            </IconButton>
        </React.Fragment>
    );

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

    const handleSubmit = async (event) => {
        try {
            let result, credentials;

            credentials = {
                "email": state.email
            };

            result = await http.post("/api/users/forgotPassword", credentials);

            // if (result.data.message === "User " + state.name + " " + state.surname + " has been registered") {
            setState({
                ...state,
                msg: result.data.message
            });
            // }

            handleOpen();

        } catch (err) {

            setState({
                ...state,
                "msg": "bad email"
            });
            handleOpen();

            console.error(err);
        }

    };

    return (
        <div className="auth_container auth_container_login">
            <div className="card auth_card">
                <div className="logo">
                    DIETIX
                </div>

                {state.msg !== "" ? <Snackbar
                    anchorOrigin={{vertical: 'top', horizontal: 'center'}}
                    open={open}
                    autoHideDuration={5000}
                    onClose={handleClose}
                    message={state.msg}
                    action={action}
                /> : null}
                {/*{state.msg !== "" ? <div className="msg">{state.msg}</div> : null}*/}

                <form className="form">
                    <div className="setting_header">Forgot Password</div>
                    <div>
                        <p>We will be sending a reset password link to your email.</p>
                    </div>
                    <label htmlFor="email">E-mail</label>
                    <input
                        className="input_field"
                        id="email"
                        type="email"
                        name="email"
                        value={state.email}
                        autoComplete="off"
                        onChange={event => handleChange(event)}
                        required/>

                    <button className="form_button" name="forgot_password_button" type="button"
                            onClick={event => handleSubmit(event)}>Send
                    </button>

                </form>

            </div>
        </div>
    );
}