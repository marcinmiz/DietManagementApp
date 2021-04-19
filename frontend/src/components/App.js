import React, {Component}  from 'react';
import '../App.css';
import {BrowserRouter, Switch, Route} from "react-router-dom";
import Authentication from "./Authentication";
import UserDashboard from "./UserDashboard";
export default class App extends Component{
    constructor() {
        super();

        this.state = {
            loggedInStatus: "NOT_LOGGED_IN",
            user: {}
        }
    }
  render() {
      return (
          <div className="App App-content">
              <BrowserRouter>
                  <Switch>
                      <Route
                          exact
                          path={"/"}
                          render={props => (
                              <Authentication {...props} loggedInStatus={this.state.loggedInStatus}/>
                          )}
                      />
                      <Route
                          exact
                          path={"/dashboard"}
                          render={props => (
                              <UserDashboard {...props} loggedInStatus={this.state.loggedInStatus}/>
                          )}
                      />
                  </Switch>
              </BrowserRouter>
          </div>
      );
  }
}
