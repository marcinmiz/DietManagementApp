import React from 'react'
import AppBar from "./components/AppBar"
import UserBottomNavigation from "./components/UserBottomNavigation"
import {Container} from '@material-ui/core';
import Products from "./pages/Products";
import Recipes from "./pages/Recipes";
import {Redirect, Route, Switch, useHistory, useRouteMatch} from 'react-router-dom'
import Preferences from "./pages/Preferences";
import Menus from "./pages/Menus";
import Programmes from "./pages/Programmes";
import ShoppingLists from "./pages/ShoppingLists";
import Settings from "./pages/Settings";

export default function UserDashboard(props) {

    let admin = props.admin;
    let adminMode = props.adminMode;
    let loaded = props.loaded;
    const history = useHistory();

    let {path} = useRouteMatch();

    return (
        <Container maxWidth="false">
            <AppBar name={props.name} surname={props.surname} admin={props.admin} adminMode={props.adminMode} avatarImage={props.avatarImage}
                    handleAdminMode={props.handleAdminMode} handleLogout={props.handleLogout}/>
            <main>
                <div>
                    <Switch>
                        <Route path={`${path}dashboard`}>
                            <div>Dashboard</div>
                        </Route>
                        <Route path={`${path}products/:productId?/:mode?`}>
                            <Products userId={props.userId} admin={admin} adminMode={adminMode}
                                      loggedInStatus={props.loggedInStatus} handleAdminMode={props.handleAdminMode}/>
                        </Route>
                        <Route path={`${path}recipes/:recipeId?/:mode?`}>
                            <Recipes userId={props.userId} name={props.name} surname={props.surname} admin={admin}
                                     adminMode={adminMode}/>
                        </Route>
                        <Route path={`${path}preferences`}>
                            <Preferences admin={admin} adminMode={adminMode}/>
                        </Route>
                        <Route path={`${path}programmes`}>
                            <Programmes currentDietaryProgramme={props.currentDietaryProgramme}
                                        handleUseDietaryProgramme={props.handleUseDietaryProgramme}/>
                        </Route>
                        <Route path={`${path}menus`}>
                            <Menus currentDietaryProgramme={props.currentDietaryProgramme}
                                   currentDietaryProgrammeDay={props.currentDietaryProgrammeDay}/>
                        </Route>
                        <Route path={`${path}shopping`}>
                            <ShoppingLists currentDietaryProgramme={props.currentDietaryProgramme}
                                           currentDietaryProgrammeDay={props.currentDietaryProgrammeDay}
                                           dietaryProgrammeStartDate={props.dietaryProgrammeStartDate}
                            />
                        </Route>
                        <Route path={`${path}settings`}>
                            <Settings userId={props.userId} name={props.name} surname={props.surname} username={props.username}
                                      avatarImage={props.avatarImage}
                            />
                        </Route>
                        <Route path="*">
                            <Redirect to={"/dashboard"}/>
                        </Route>
                    </Switch>
                </div>
            </main>
            <UserBottomNavigation history={history}/>
        </Container>

    );
}
