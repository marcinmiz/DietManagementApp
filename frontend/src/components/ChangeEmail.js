import React, {useEffect} from 'react';
import "../Authentication.css";
import http from "../http-common";
import {makeStyles} from "@material-ui/core";
import IconButton from "@material-ui/core/IconButton";
import CloseIcon from "@material-ui/core/SvgIcon/SvgIcon";
import Snackbar from "@material-ui/core/Snackbar/Snackbar";

const useStyles = makeStyles({
    emailForm: {
        minHeight: 'min-content !important',
    }
});

export default function ChangeEmail(props) {
    const classes = useStyles();

    const [state, setState] = React.useState({
        currentPassword: "",
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
            console.log(state.currentPassword);

            if (validateEmail() !== "error") {
                credentials = {
                    "currentPassword": state.currentPassword,
                    "email": state.email
                };

                result = await http.post("/api/users/changeEmail", credentials);

                // if (result.data.message === "User " + state.name + " " + state.surname + " has been registered") {
                setState({
                    ...state,
                    msg: result.data.message === "Bad credentials" ? "Wrong current password" : result.data.message
                });

                handleOpen();
                // }
            }
        } catch (err) {

            setState({
                ...state,
                "msg": "user is not logged"
            });
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

            <form className={classes.emailForm + " form"}>
                <div className="setting_header">Change E-mail</div>

                <label htmlFor="password" className={classes.currentPassword}>Enter your current password</label>
                <input
                    className={classes.currentPassword + " input_field"}
                    id="currentPassword2"
                    type="password"
                    name="currentPassword"
                    value={state.currentPassword}
                    autoComplete="off"
                    onChange={event => handleChange(event)}
                    required/>

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