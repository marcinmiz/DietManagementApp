import React, {Component} from 'react'
import AppBar from "./components/AppBar"
import PageRoutes from "./components/PageRoutes"
import UserBottomNavigation from "./components/UserBottomNavigation"
import {Container} from '@material-ui/core';

export default class UserDashboard extends Component {

    render() {
        return (
            <Container maxWidth="false">
                <AppBar/>
                <main>
                    <PageRoutes/>
                </main>
                <UserBottomNavigation/>
            </Container>

        );
    }
}
