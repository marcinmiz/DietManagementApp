import React, {useEffect} from 'react';
import "../App.css"
import Avatar from '@material-ui/core/Avatar';
import Button from "@material-ui/core/Button";
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Switch from '@material-ui/core/Switch';

export default function Appbar(props) {

    useEffect(
    () => {
        console.log("adminMode: " + props.adminMode)
        console.log("admin: " + props.admin)
    }, [props.adminMode]
    );

    return (
        <div className="app_bar">
            <div className="logo">
                DIETIX
            </div>
            <div>
                <Avatar className="user_avatar" alt="template user avatar" src=""/>
                <div>John Smith</div>
            </div>
            {(props.admin === true) ?
                <FormControlLabel
                control={
                <Switch
                    checked={props.adminMode}
                    onChange={props.handleAdminMode}
                    name="adminMode"
                    color="primary"
                />
                }
                //switch is showed if user is admin, second variable defines if admin or user mode is on
                label="Admin mode"
                labelPlacement="top"
                /> : ""}
            {/*<div>*/}
            {/*<Button variant="text">User</Button>*/}
            {/*</div>*/}

            <div>
                <button className="form_button">Log out</button>
            </div>
        </div>
    );
}