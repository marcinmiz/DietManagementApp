import React from 'react'
import {Container, Divider, makeStyles} from "@material-ui/core";
import ChangePassword from "../components/ChangePassword";
import UploadImages from "../components/UploadImages";
import Button from "@material-ui/core/Button";
import ChangeEmail from "../components/ChangeEmail";
import WeightManager from "../components/WeightManager";

const useStyles = makeStyles({
    product: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        marginLeft: 'auto',
        marginRight: 'auto',
        marginTop: '2%',
        marginBottom: '2%'
    },
    centralContainer: {
        width: '22%',
        marginLeft: 'auto',
        marginRight: 'auto',
        backgroundColor: '#8a7666',
        borderRadius: '2%',
        padding: '2%'
    },
    productName: {
        fontWeight: 'bold'
    },
    productAmount: {
        display: 'flex',
        marginRight: '0'
    },
    productPart: {
        marginLeft: '10%'
    },
    settingsContainer: {
        display: 'flex',
        justifyContent: 'space-around',
        // alignItems: 'center',
        width: '100%',
        // height: '90% !important',
        // overflowY: 'scroll'
    },
    avatarId: {
        width: '20%',
    },
    settingsBody: {
        width: '60%',
    },
    emailPasswordContainer: {
        // height: '30%',
        display: 'flex',
        // alignItems: 'center',
        justifyContent: 'space-between',
        // marginLeft: 'auto',
        // marginRight: 'auto',
    },
    emailPassword: {
        width: '40%'
    },
    saveButton: {
        marginTop: '2%'
    },
    username: {
        marginBottom: '10px',
        // padding: '2%',
        fontSize: 'medium'
    }
});

export default function Settings(props) {
    const classes = useStyles();

    const [state, setState] = React.useState({
        submitted: false,
        weight: 45.0,
        msg: "",
        loaded: false
    });

    const handleSave = () => {
        setState({
            ...state,
            submitted: true
        });
    };

    return (
        <Container id="main_container" maxWidth="lg">
            <div className="page_container">
                <div>
                    <h2>Settings</h2>
                    <div className={classes.settingsContainer}>
                        <div className={classes.avatarId}>
                            <div className="userName">{props.name + " " + props.surname}</div>
                            <div className={classes.username}>{props.username}</div>
                            <UploadImages className="settingsPhoto" submitted={state.submitted}
                                          currentImage={props.avatarImage}
                                          type="avatar" id={props.userId}/>
                            <Button className={classes.saveButton} variant="contained" color="primary" type="button"
                                    onClick={() => handleSave()}>Save</Button>
                        </div>
                        <div>
                        <Divider variant="fullWidth" orientation="vertical"/>
                        </div>
                        <div className={classes.settingsBody}>
                            <div className={classes.emailPasswordContainer}>
                                <div className={classes.emailPassword}>
                                    <ChangePassword resetType="settings"/>
                                </div>
                                <div className={classes.emailPassword}>
                                    <ChangeEmail/>
                                </div>
                                <div>
                                    <WeightManager currentDietaryProgramme={props.currentDietaryProgramme}/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </Container>
    );
}