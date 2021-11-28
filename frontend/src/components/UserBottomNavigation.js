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

export default function UserBottomNavigation(props) {

    const [value, setValue] = React.useState('dashboard');

    let url = props.history.location.pathname;
    let page = url.split("/")[1];

    useEffect(
        () => {
            switch (page) {
                case "dashboard":
                    if (props.adminMode === false) {
                        document.getElementById("dashboard").click();
                    }
                    break;
                case "products":
                    document.getElementById("products").click();
                    break;
                case "recipes":
                    document.getElementById("recipes").click();
                    break;
                case "preferences":
                    if (props.adminMode === false) {
                        document.getElementById("preferences").click();
                    }
                    break;
                case "programmes":
                    if (props.adminMode === false) {
                        document.getElementById("programmes").click();
                    }
                    break;
                case "menus":
                    if (props.adminMode === false) {
                        document.getElementById("menus").click();
                    }
                    break;
                case "shopping":
                    if (props.adminMode === false) {
                        document.getElementById("shopping").click();
                    }
                    break;
                case "settings":
                    document.getElementById("settings").click();
                    break;
                default:
                    if (props.adminMode === false) {
                        document.getElementById("dashboard").click();
                    } else {
                        document.getElementById("products").click();
                    }
            }
        },[]
    );

    const handleChange = (event, newValue) => {
        setValue(newValue);

        let basic_url = '/' + newValue;

        if (url.split('/')[1] !== newValue)
            url = basic_url;

        props.history.push(url);
    };

    return (
        <div>
            <BottomNavigation value={value} onChange={handleChange} className="user_bottom_navigation">
                {props.adminMode === false ? <BottomNavigationAction id="dashboard" label="Dashboard" value="dashboard" icon={<NearMeIcon/>}/> : null}
                <BottomNavigationAction id="products" label="Products" value="products" icon={<SpaIcon/>}/>
                <BottomNavigationAction id="recipes" label="Recipes" value="recipes" icon={<ReceiptIcon/>}/>
                {props.adminMode === false ? <BottomNavigationAction id="preferences" label="Dietary preferences" value="preferences" icon={<FingerprintIcon/>}/> : null}
                {props.adminMode === false ? <BottomNavigationAction id="programmes" label="Dietary programmes" value="programmes" icon={<TrackChangesIcon/>}/> : null}
                {props.adminMode === false ? <BottomNavigationAction id="menus" label="Daily menus" value="menus" icon={<RestaurantIcon/>}/> : null}
                {props.adminMode === false ? <BottomNavigationAction id="shopping" label="Shopping lists" value="shopping" icon={<LocalMallIcon/>}/> : null}
                <BottomNavigationAction id="settings" label="Settings" value="settings" icon={<SettingsIcon/>}/>
            </BottomNavigation>
        </div>

    );
}