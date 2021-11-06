import React, {useEffect} from 'react';
import "../Authentication.css";
import {useHistory} from "react-router-dom";
import http from "../http-common";

export default function Authentication(props) {

    const history = useHistory();

    const [state, setState] = React.useState({
        name: "",
        surname: "",
        username: "",
        email: "",
        password: "",
        passwordConfirmation: "",
        msg: "",
        mode: ""
    });

    useEffect(
        () => {
            console.log(state.msg);
        }, [state.msg]
    );

    const validateRegistrationData = async () => {

        if (state.username === "") {
            setState({
                ...state,
                msg: "Username is required"
            });
            return "error";
        }

        const occupiedUsername = await http.get("/api/users/existsUser/" + state.username);

        if (!/^[a-zA-Z 0-9]+$/.test(state.username)) {
            setState({
                ...state,
                msg: "Username has to contain only letters, digits and spaces"
            });
            return "error";
        } else if (state.username.trim().length < 6 || state.username.length > 32) {
            setState({
                ...state,
                msg: "Username has to have min 6 and max 32 characters"
            });
            return "error";
        } else if (occupiedUsername.data) {
            setState({
                ...state,
                msg: "Username is occupied yet"
            });
            return "error";
        } else if (state.name === "") {
            setState({
                ...state,
                msg: "Name is required"
            });
            return "error";
        } else if (!/^[a-zA-Z ]+$/.test(state.name)) {
            setState({
                ...state,
                msg: "Name has to contain only letters and spaces"
            });
            return "error";
        } else if (state.name.trim().length < 2 || state.name.length > 40) {
            setState({
                ...state,
                msg: "Name has to have min 2 and max 40 characters"
            });
            return "error";
        } else if (state.surname === "") {
            setState({
                ...state,
                msg: "Surname is required"
            });
            return "error";
        } else if (!/^[a-zA-Z ]+$/.test(state.surname)) {
            setState({
                ...state,
                msg: "Surname has to contain only letters and spaces"
            });
            return "error";
        } else if (state.surname.trim().length < 2 || state.surname.length > 40) {
            setState({
                ...state,
                msg: "Surname has to have min 2 and max 40 characters"
            });
            return "error";
        } else if (state.email === "") {
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
        } else if (state.password === "") {
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

    };

    const handleSubmit = async (event) => {
        try {
            let result, credentials, user;

            if (event.target.name === "register_button") {

                credentials = {
                    "name": state.name,
                    "surname": state.surname,
                    "password": state.password,
                    "passwordConfirmation": state.passwordConfirmation,
                    "username": state.username,
                    "email": state.email
                };

                if (validateRegistrationData() !== "error") {

                    result = await http.post("/api/users/register", credentials);

                    console.log(result);
                    setState({
                        ...state,
                        msg: result.data.message
                    });

                    if (result.data.message === "User " + state.name + " " + state.surname + " has been registered") {
                        setTimeout(() => history.push('/login'), 3000);
                        setTimeout(() => {
                            setState({
                                ...state,
                                msg: ""
                            });
                        }, 5000);
                    }
                }

            } else {

                credentials = {
                    "username": state.username,
                    "password": state.password,
                };

                console.log(state.password);

                http.post("/api/users/login", credentials)
                    .then(
                        async () => {

                            user = await http.get("/api/users/loggedUser");

                            let user_data = user.data;

                            if (user_data != null) {
                                props.handleInit(user_data);
                                const state = props.location.state;

                                history.push(state === undefined ? '/dashboard' : state.from.pathname);
                            }

                        }
                    )
                    .catch(() => {
                        setState({
                            ...state,
                            "msg": "bad username or password"
                        });
                    })

            }
        } catch (err) {
            console.error(err);
        }

    };

    const handleChange = (event) => {
        setState({
            ...state,
            [event.target.name]: event.target.value
        });
    };

    const handleTab = (event) => {
        if (event.target.name === "register_tab") {
            history.push('/register');
        } else {
            history.push('/login');
        }
    };

    const type = props.authentication_type;
    type === "register" ? state.mode = "register" : state.mode = "login";

    if (props.loggedInStatus === "LOGGED_IN") {
        return <div>Loading</div>;
    }

    if (state.mode === "register") {

        return (
            <div className="auth_container auth_container_register">
                <div className="card auth_card">
                    <div className="logo">
                        DIETIX
                    </div>
                    <div className="tabs_area">
                        <button
                            type="button"
                            className="tab first_tab"
                            name="login_tab"
                            onClick={event => handleTab(event)}>
                            Log in
                        </button>
                        <button
                            type="button"
                            className="tab last_tab current_tab"
                            name="register_tab"
                            onClick={event => handleTab(event)}>
                            Register
                        </button>
                    </div>

                    {state.msg !== "" ? <div className="msg">{state.msg}</div> : null}

                    <form className="form">

                        <label htmlFor="username">Username</label>
                        <input
                            className="input_field"
                            id="username"
                            type="text"
                            name="username"
                            autoComplete="off"
                            value={state.username}
                            onChange={event => handleChange(event)}
                            required/>

                        <label htmlFor="name">Name</label>
                        <input
                            className="input_field"
                            id="name"
                            type="text"
                            name="name"
                            autoComplete="off"
                            value={state.name}
                            onChange={event => handleChange(event)}
                            required/>

                        <label htmlFor="surname">Surname</label>
                        <input
                            className="input_field"
                            id="surname"
                            type="text"
                            name="surname"
                            autoComplete="off"
                            value={state.surname}
                            onChange={event => handleChange(event)}
                            required/>

                        <label htmlFor="email">E-mail</label>
                        <input
                            className="input_field"
                            id="email"
                            type="email"
                            name="email"
                            autoComplete="off"
                            value={state.email}
                            onChange={event => handleChange(event)}
                            required/>

                        <label htmlFor="password">Password</label>
                        <input
                            className="input_field"
                            id="password"
                            type="password"
                            name="password"
                            autoComplete="off"
                            value={state.password}
                            onChange={event => handleChange(event)}
                            required/>

                        <label htmlFor="passwordConfirmation">Password Confirmation</label>
                        <input
                            className="input_field"
                            id="passwordConfirmation"
                            type="password"
                            name="passwordConfirmation"
                            autoComplete="off"
                            value={state.passwordConfirmation}
                            onChange={event => handleChange(event)}
                            required/>

                        <button className="form_button" name="register_button" type="button"
                                onClick={event => handleSubmit(event)}>Register
                        </button>

                    </form>
                </div>
            </div>
        )
    } else {
        return (<div className="auth_container auth_container_login">
                <div className="card auth_card">
                    <div className="logo">
                        DIETIX
                    </div>
                    <div className="tabs_area">
                        <button
                            type="button"
                            className="tab first_tab current_tab"
                            name="login_tab"
                            onClick={event => handleTab(event)}>
                            Log in
                        </button>
                        <button
                            type="button"
                            className="tab last_tab"
                            name="register_tab"
                            onClick={event => handleTab(event)}>
                            Register
                        </button>
                    </div>

                    {state.msg !== "" ? <div className="msg">{state.msg}</div> : null}

                    <form className="form">

                        <label htmlFor="username">Username</label>
                        <input
                            className="input_field"
                            id="username"
                            type="text"
                            name="username"
                            value={state.username}
                            autoComplete="off"
                            onChange={event => handleChange(event)}
                            required/>

                        <label htmlFor="password">Password</label>
                        <input
                            className="input_field"
                            id="password"
                            type="password"
                            name="password"
                            value={state.password}
                            autoComplete="off"
                            onChange={event => handleChange(event)}
                            required/>

                        <button className="form_button" name="login_button" type="button"
                                onClick={event => handleSubmit(event)}>Log in
                        </button>

                    </form>
                    <a href="http://localhost:3000/forgotPassword">Forgot password?</a>
                </div>
            </div>
        );
    }

}
