import React from 'react';
import "../App.css"
import Avatar from '@material-ui/core/Avatar';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Switch from '@material-ui/core/Switch';
import http from "../http-common";
import {useHistory} from "react-router-dom";

export default function Appbar(props) {

    const history = useHistory();

    const logoutUser = async () => {
        props.handleLogout();

        setTimeout(() => history.push('/login'), 10);

        await http.post("/api/users/logout");

    };

    return (
        <div className="app_bar">
            <div className="logo">
                DIETIX
            </div>
            <div>
                <Avatar className="user_avatar" alt="template user avatar" src=""/>
                <div>{props.name + " " + props.surname}</div>
            </div>
            {(props.admin === true) ?
                <FormControlLabel
                    control={
                        <Switch
                            checked={props.adminMode}
                            onChange={event => props.handleAdminMode(event, !props.adminMode)}
                            name="adminMode"
                            color="primary"
                        />
                    }
                    //switch is showed if user is admin, second variable defines if admin or user mode is on
                    label="Admin mode"
                    labelPlacement="top"
                /> : ""}

            <div>
                <button className="form_button" onClick={logoutUser}>Log out</button>
            </div>
        </div>
    );
}