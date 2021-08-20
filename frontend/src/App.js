import React, {useEffect} from 'react';
import './App.css';
import Authentication from "./pages/Authentication";
import UserDashboard from "./UserDashboard";
import {BrowserRouter as Router, Switch, Route, Redirect, useHistory} from 'react-router-dom'
import http from "./http-common";
import CircularProgress from '@material-ui/core/CircularProgress';
import LinearProgress from '@material-ui/core/LinearProgress';

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

                setState({
                    ...state,
                    loaded: true
                });

                if (user_data !== "") {
                    handleInit(user_data);
                }

            };
            getUser();
    }, []);

    const handleAdminMode = () => {
        let newAdminMode = !state.adminMode;
        setState({
            ...state,
            adminMode: newAdminMode
        });
    };

    const handleLogin = (user) => {
        setState({
            ...state,
            loggedInStatus: "LOGGED_IN",
            user: user
        });
    };

    const handleInit = (user) => {

        setState({
            ...state,
            loggedInStatus: "LOGGED_IN",
            user: user,
            admin: user.role.name === "ADMIN",
            adminMode: user.role.name === "ADMIN",
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

            return (
                <Router>
                <div className="App App_content">
                    {state.loaded ? <Switch>
                        <Route path="/dashboard"
                               render={(props) =>
                                   state.loggedInStatus === "LOGGED_IN"
                                       ? (<UserDashboard history={history} name={state.user.name} surname={state.user.surname}
                                                          loaded={state.loaded} loggedInStatus={state.loggedInStatus}
                                                          admin={state.admin} adminMode={state.adminMode}
                                                          handleAdminMode={handleAdminMode}
                                                          handleLogout={handleLogout}
                                                         {...props} />)
                                       : (<Redirect to={{ pathname: '/login', state: { from: props.location } }} />)}
                        />
                        {/*<PrivateRoute path="/dashboard" component={UserDashboard} history={history} loggedInStatus={state.loggedInStatus} admin={state.admin} adminMode={state.adminMode} handleAdminMode={handleAdminMode} handleLogout={handleLogout}/>*/}
                            <Route path="/:authentication_type"
                                     render={(props) => {

                                         let path = "/dashboard";
                                         if (props.location.state !== undefined) {
                                             path = props.location.state.from.pathname;
                                         }
                                        return state.loggedInStatus === "NOT_LOGGED_IN"
                                         ? (<Authentication history={history} loaded={state.loaded}
                                                                        loggedInStatus={state.loggedInStatus}
                                                                        admin={state.admin} adminMode={state.adminMode}
                                                                        handleLogin={handleLogin} {...props} />)
                                         : (<Redirect to={{ pathname: path, state: { from: props.location } }} />)}
                                     }

                            />
                            {/*: <Redirect to="/dashboard"/>*/}
                    </Switch> : <div className="loading">Loading</div>}
                </div>
                </Router>
            );
}
