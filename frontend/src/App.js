import React, {useEffect} from 'react';
import './App.css';
import Authentication from "./pages/Authentication";
import UserDashboard from "./UserDashboard";
import {BrowserRouter as Router, Redirect, Route, Switch, useHistory} from 'react-router-dom'
import http from "./http-common";

export default function App() {

    const history = useHistory();

    const [state, setState] = React.useState({
        loggedInStatus: "NOT_LOGGED_IN",
        user: {},
        admin: false,
        adminMode: false,
        loaded: false,
    });

    useEffect(
        () => {
            const getUser = async () => {
                let user = await http.get("/api/users/loggedUser");

                let user_data = user.data;

                if (user_data !== "") {
                    handleInit(user_data);
                } else {
                    setState({
                        ...state,
                        loaded: true
                    });
                }
            };
            getUser();
        }, []);

    const handleAdminMode = (event, adminMode) => {
        setState({
            ...state,
            adminMode: adminMode
        });
        if (state.admin) {
            sessionStorage.setItem('adminMode', adminMode ? 'yes' : 'no');
        }
    };

    const handleInit = (user) => {

        let mode, admin;
        if (user.role.name === "ADMIN") {
            mode = sessionStorage.getItem('adminMode') == null || sessionStorage.getItem('adminMode') === 'yes' ? true : false;
            admin = true;
        } else {
            mode = false;
            admin = false;
        }

        setState({
            ...state,
            loggedInStatus: "LOGGED_IN",
            user: user,
            admin: admin,
            adminMode: mode,
            loaded: true
        });

    };

    const handleLogout = () => {
        setState({
            ...state,
            loggedInStatus: "NOT_LOGGED_IN",
            user: null,
        });

    };

    const handleUseDietaryProgramme = (currentDietaryProgramme) => {

        let user = {...state.user, currentDietaryProgramme: currentDietaryProgramme};

        setState({
            ...state,
            user: user,
        });

    };

    return (

        <div className="App App_content">
            <Router>
                {state.loaded ?

                    <Switch>
                        <Route exact path="/login"
                               render={(props) => {

                                   return state.loggedInStatus === "NOT_LOGGED_IN"
                                       ? (<Authentication authentication_type="login" loaded={state.loaded}
                                                          loggedInStatus={state.loggedInStatus}
                                                          admin={state.admin} adminMode={state.adminMode}
                                                          handleInit={handleInit} {...props} />)
                                       :
                                       (<Redirect to={{pathname: "/dashboard", state: {from: props.location}}}/>)
                               }
                               }
                        />
                        <Route exact path="/register"
                               render={(props) => {

                                   return state.loggedInStatus === "NOT_LOGGED_IN"
                                       ? (<Authentication authentication_type="register" loaded={state.loaded}
                                                          loggedInStatus={state.loggedInStatus}
                                                          admin={state.admin} adminMode={state.adminMode}
                                                          handleInit={handleInit} {...props} />)
                                       :
                                       (<Redirect to={{pathname: "/dashboard", state: {from: props.location}}}/>)
                               }
                               }
                        />
                        <Route path="/">

                            {
                                state.loggedInStatus === "LOGGED_IN"
                                    ?
                                    (<UserDashboard userId={state.user.userId} name={state.user.name}
                                                    surname={state.user.surname}
                                                    loggedInStatus={state.loggedInStatus}
                                                    admin={state.admin} adminMode={state.adminMode}
                                                    currentDietaryProgramme={state.user.currentDietaryProgramme}
                                                    currentDietaryProgrammeDay={state.user.currentDietaryProgrammeDay}
                                                    dietaryProgrammeStartDate={state.user.dietaryProgrammeStartDate}
                                                    handleAdminMode={handleAdminMode}
                                                    handleLogout={handleLogout}
                                                    handleUseDietaryProgramme={handleUseDietaryProgramme}
                                    />)
                                    :
                                    (<Redirect to={{pathname: '/login'}}/>)

                            }
                        </Route>
                        <Route path="*">
                            <Redirect to={{pathname: '/login'}}/>
                        </Route>
                    </Switch>
                    :
                    <div className="loading">Loading</div>}
            </Router>
        </div>

    );
}
