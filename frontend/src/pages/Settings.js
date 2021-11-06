import React from 'react'
import {Container, makeStyles} from "@material-ui/core";
import ChangePassword from "../components/ChangePassword";
import UploadImages from "../components/UploadImages";
import Button from "@material-ui/core/Button";
import ResetEmail from "../components/ChangeEmail";

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
        alignItems: 'center',
        width: '100%',
        // height: '90% !important',
        // overflowY: 'scroll'
    },
    avatarId: {
        width: '20%',
    },
    emailPasswordContainer: {
        display: 'flex',
        width: '50%',
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
                        <div className={classes.emailPasswordContainer}>
                            <div className={classes.emailPassword}>
                                <ChangePassword resetType="settings"/>
                            </div>
                            <div className={classes.emailPassword}>
                                <ResetEmail/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </Container>
    );
}