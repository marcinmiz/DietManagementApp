import React, {ChangeEvent, useEffect} from 'react';
import BottomNavigation from '@material-ui/core/BottomNavigation';
import BottomNavigationAction from '@material-ui/core/BottomNavigationAction';
import SettingsIcon from '@material-ui/icons/Settings';
import TrackChangesIcon from '@material-ui/icons/TrackChanges';
import FingerprintIcon from '@material-ui/icons/Fingerprint';
import LocalMallIcon from '@material-ui/icons/LocalMall';
import NearMeIcon from '@material-ui/icons/NearMe';
import ReceiptIcon from '@material-ui/icons/Receipt';
import RestaurantIcon from '@material-ui/icons/Restaurant';
import SpaIcon from '@material-ui/icons/Spa';
import {useHistory} from "react-router-dom";


export default function UserBottomNavigation(props) {

    const [value, setValue] = React.useState('dashboard');

    useEffect(
        () => {
            switch (props.url) {
                case "/dashboard":
                    document.getElementById("dashboard").click();
                    break;
                case "/dashboard/products/main":
                    document.getElementById("products").click();
                    break;
                case "/dashboard/recipes":
                    document.getElementById("recipes").click();
                    break;
                case "/dashboard/preferences":
                    document.getElementById("preferences").click();
                    break;
                case "/dashboard/programmes":
                    document.getElementById("programmes").click();
                    break;
                case "/dashboard/menus":
                    document.getElementById("menus").click();
                    break;
                case "/dashboard/shopping":
                    document.getElementById("shopping").click();
                    break;
                case "/dashboard/settings":
                    document.getElementById("settings").click();
                    break;
                default:
                    document.getElementById("dashboard").click();
            }
        },[props.url]
    );

    const handleChange = (event, newValue) => {
        setValue(newValue);
        let destination = "/dashboard";
        if (newValue === "products") {
            destination += "/products/main";
        } else if (newValue !== "dashboard") {
            destination += "/" + newValue;
        }
        props.history.push(destination);
    };

    return (
        <div>
            <BottomNavigation value={value} onChange={handleChange} className="user_bottom_navigation">
                <BottomNavigationAction id="dashboard" label="Dashboard" value="dashboard" icon={<NearMeIcon/>}/>
                <BottomNavigationAction id="products" label="Products" value="products" icon={<SpaIcon/>}/>
                <BottomNavigationAction id="recipes" label="Recipes" value="recipes" icon={<ReceiptIcon/>}/>
                <BottomNavigationAction id="preferences" label="Dietary preferences" value="preferences" icon={<FingerprintIcon/>}/>
                <BottomNavigationAction id="programmes" id="programmes" label="Dietary programmes" value="programmes" icon={<TrackChangesIcon/>}/>
                <BottomNavigationAction id="menus" label="Daily menus" value="menus" icon={<RestaurantIcon/>}/>
                <BottomNavigationAction id="shopping" label="Shopping lists" value="shopping" icon={<LocalMallIcon/>}/>
                <BottomNavigationAction id="settings" label="Settings" value="settings" icon={<SettingsIcon/>}/>
            </BottomNavigation>
        </div>

    );
}