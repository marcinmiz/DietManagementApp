import React, {useEffect} from 'react';
import "../Authentication.css";
import http from "../http-common";
import {makeStyles} from "@material-ui/core";
import IconButton from "@material-ui/core/IconButton";
import CloseIcon from "@material-ui/core/SvgIcon/SvgIcon";
import Snackbar from "@material-ui/core/Snackbar/Snackbar";

const useStyles = makeStyles({
    passwordForm: {
        minHeight: 'min-content !important',
    },
    currentPassword: {
        width: '87%'
    }
});

export default function ChangePassword(props) {
    const classes = useStyles();

    const [state, setState] = React.useState({
        currentPassword: "",
        password: "",
        passwordConfirmation: "",
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
            console.log(state.currentPassword);
            console.log(state.password);
            console.log(props.resetType);

            if (validatePassword() !== "error") {
                credentials = {
                    "resetType": props.resetType,
                    "token": props.token,
                    "currentPassword": state.currentPassword,
                    "password": state.password,
                    "passwordConfirmation": state.passwordConfirmation
                };

                result = await http.post("/api/users/changePassword", credentials);

                console.log(result.data.message);

                // if (result.data.message === "User " + state.name + " " + state.surname + " has been registered") {
                setState({
                    ...state,
                    msg: result.data.message === "Bad credentials" ? "Wrong current password" : result.data.message
                });

                handleOpen();
                // }
            } else {
                handleOpen();
            }
        } catch (err) {

            // setState({
            //     ...state,
            //     "msg": "invalid token"
            // });
            handleOpen();

            console.error(err);
        }

    };

    return (
        <div>
            {state.msg !== "" ? <Snackbar
                anchorOrigin={{vertical: 'top', horizontal: 'center'}}
                open={open}
                autoHideDuration={5000}
                onClose={handleClose}
                message={state.msg}
                action={action}
            /> : null}
            {/*{state.msg !== "" ? <div className="msg">{state.msg}</div> : null}*/}

            <form className={classes.passwordForm + " form"}>
                <div className="setting_header">Change Password</div>
                {props.resetType === "settings" ? <div>
                    <label htmlFor="password" className={classes.currentPassword}>Enter your current password</label>
                    <input
                        className={classes.currentPassword + " input_field"}
                        id="currentPassword"
                        type="password"
                        name="currentPassword"
                        value={state.currentPassword}
                        autoComplete="off"
                        onChange={event => handleChange(event)}
                        required/>
                </div> : null}

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