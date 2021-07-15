import React from 'react'
import {Switch, Route, Redirect} from 'react-router-dom'
import Products from "../pages/Products";
import ProductsDetails from "../pages/ProductsDetails";

export default function PageRoutes() {

    return (
        <div>
            <Switch>
                <Route exact path="/now">
                    Now
                </Route>
                <Route exact path="/products/:msg" component={Products}>
                </Route>
                <Route exact path="/products/:id/:mode" component={ProductsDetails}>
                </Route>
                <Route exact path="/recipes">
                    Recipes
                </Route>
                <Route exact path="/programmes">
                    Dietary Programmes
                </Route>
                <Route exact path="/preferences">
                    Dietary Preferences
                </Route>
                <Route exact path="/menus">
                    Daily menus
                </Route>
                <Route exact path="/shopping">
                    Shopping lists
                </Route>
                <Route exact path="/settings">
                    Settings
                </Route>
                <Route path="*">
                    <Redirect to="/now"/>
                </Route>
            </Switch>
        </div>
    );
}