import React, {Component}  from 'react';
import '../App.css';
import {BrowserRouter, Switch, Route} from "react-router-dom";
import Authentication from "./Authentication";
import Dashboard from "./Dashboard";
export default class App extends Component{
  render() {
      return (
          <div className="App">
              <BrowserRouter>
                  <Switch>
                      <Route exact path={"/"} component={Authentication}/>
                      <Route exact path={"/dashboard"} component={Dashboard}/>
                  </Switch>
              </BrowserRouter>
          </div>
      );
  }
}
