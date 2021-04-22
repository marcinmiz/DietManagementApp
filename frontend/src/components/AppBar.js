import React from 'react';
import "../App.css"
import Avatar from '@material-ui/core/Avatar';

export default function Appbar() {

    return (
        <div className="app_bar">
            <div className="logo">
                DIETIX
            </div>
            <div>
                <Avatar className="user_avatar" alt="template user avatar" src=""/>
                <div>John Smith</div>
            </div>
            <div>
                <button className="form_button">Log out</button>
            </div>
        </div>
    );
}