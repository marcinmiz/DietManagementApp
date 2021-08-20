import React, {Component} from 'react'
import AppBar from "./components/AppBar"
import PageRoutes from "./components/PageRoutes"
import UserBottomNavigation from "./components/UserBottomNavigation"
import {Container} from '@material-ui/core';

export default class UserDashboard extends Component {

    render() {

        return (
            <Container maxWidth="false">
                <AppBar history={this.props.history} name={this.props.name} surname={this.props.surname} admin={this.props.admin} adminMode={this.props.adminMode} handleAdminMode={this.props.handleAdminMode} handleLogout={this.props.handleLogout}/>
                <main>
                    <PageRoutes history={this.props.history} admin={this.props.admin} adminMode={this.props.adminMode} loaded={this.props.loaded}/>
                </main>
                <UserBottomNavigation history={this.props.history} url={this.props.location.pathname}/>
            </Container>

        );
    }
}
