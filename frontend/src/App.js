import React, {Component}  from 'react';
import './App.css';
import Authentication from "./pages/Authentication";
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
        if(this.state.loggedInStatus === "NOT_LOGGED_IN") {
            return(
                <div className="App App_content">
                    <Authentication loggedInStatus={this.state.loggedInStatus}/>
                </div>
            );
        } else {
                return(
                    <div className="App App_content">
                        <UserDashboard props={this.props} loggedInStatus={this.state.loggedInStatus}/>
                    </div>
                    );
        }
  }
}
