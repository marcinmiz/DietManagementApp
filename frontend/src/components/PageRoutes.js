import React from 'react'
import {Switch, Route, Redirect} from 'react-router-dom'

export default function PageRoutes () {

        return (
            <div>
                <Switch>
                    <Route exaxct path="/now">
                        Now
                    </Route>
                    <Route path="/products">
                        Products
                    </Route>
                    <Route path="/recipes">
                        Recipes
                    </Route>
                    <Route path="/programmes">
                        Dietary Programmes
                    </Route>
                    <Route path="/preferences">
                        Dietary Preferences
                    </Route>
                    <Route path="/menus">
                        Daily menus
                    </Route>
                    <Route path="/shopping">
                        Shopping lists
                    </Route>
                    <Route path="/settings">
                        Settings
                    </Route>
                    <Route path="*">
                        <Redirect to="/now" />
                    </Route>
                </Switch>
            </div>
    );
    // }
}