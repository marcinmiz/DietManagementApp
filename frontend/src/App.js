import React, {Component} from 'react';
import './App.css';
import Authentication from "./pages/Authentication";
import UserDashboard from "./UserDashboard";

export default class App extends Component {
    constructor() {
        super();

        this.state = {
            loggedInStatus: "LOGGED_IN",
            user: {},
            admin: false,
            adminMode: false
        }

        this.handleAdminMode = this.handleAdminMode.bind(this);
    }

    handleAdminMode() {
        let newAdminMode = !this.state.adminMode;
        this.setState({
            ...this.state,
            adminMode: newAdminMode
        });
    }

    render() {
        if (this.state.loggedInStatus === "NOT_LOGGED_IN") {
            return (
                <div className="App App_content">
                    <Authentication loggedInStatus={this.state.loggedInStatus}/>
                </div>
            );
        } else {
            return (
                <div className="App App_content">
                    <UserDashboard props={this.props} loggedInStatus={this.state.loggedInStatus} admin={this.state.admin} adminMode={this.state.adminMode} handleAdminMode={this.handleAdminMode}/>
                </div>
            );
        }
    }
}
