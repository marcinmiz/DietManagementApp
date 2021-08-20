import React, {useEffect} from 'react'
import {Switch, Route, useRouteMatch, Redirect} from 'react-router-dom'
import Products from "../pages/Products";
import ProductsDetails from "../pages/ProductsDetails";
import Recipes from "../pages/Recipes";

export default function PageRoutes(props) {

    let admin = props.admin;
    let adminMode = props.adminMode;
    let loaded = props.loaded;

    let { path, url } = useRouteMatch();

    return (
        <div>
            <Switch>
                <Route exact path={path}>
                    Now
                </Route>
                <Route exact path={`${path}/products/:msg`} render={(props) => <Products history={props.history} loaded={loaded} admin={admin} adminMode={adminMode} {...props} /> }>
                </Route>
                <Route exact path={`${path}/products/:id/:mode`} component={ProductsDetails}>
                </Route>
                <Route path={`${path}/recipes/:recipe_id?/:type?`} render={(props) => <Recipes history={props.history} admin={admin} adminMode={adminMode} {...props} /> }>
                </Route>
                <Route exact path={`${path}/programmes`}>
                    Dietary Programmes
                </Route>
                <Route exact path={`${path}/preferences`}>
                    Dietary Preferences
                </Route>
                <Route exact path={`${path}/menus`}>
                    Daily menus
                </Route>
                <Route exact path={`${path}/shopping`}>
                    Shopping lists
                </Route>
                <Route exact path={`${path}/settings`}>
                    Settings
                </Route>
                <Route path="*">
                    <Redirect to={path} />
                </Route>
            </Switch>
        </div>
    );
}