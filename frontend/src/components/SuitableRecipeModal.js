import React, {useEffect} from 'react';
import Modal from "@material-ui/core/Modal";
import Fade from "@material-ui/core/Fade";
import Backdrop from "@material-ui/core/Backdrop";
import makeStyles from "@material-ui/core/styles/makeStyles";
import http from "../http-common";
import {Divider, Tooltip} from "@material-ui/core";
import Button from "@material-ui/core/Button";
import Fab from "@material-ui/core/Fab";
import CloseIcon from '@material-ui/icons/Close';

const useStyles = makeStyles((theme) => ({
    modal: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        color: 'white'
    },
    paper: {
        display: 'flex',
        flexDirection: 'column',
        backgroundColor: "dimgrey",
        // border: '2px solid #000',
        boxShadow: theme.shadows[5],
        // padding: theme.spacing(2, 4, 3),
        // width: '120rem',
        width: '100%',
        // marginLeft: '1rem',
        // marginRight: '1rem',
    },
    header: {
        display: 'flex',
        padding: '5px'
    },
    title: {
        fontSize: 'x-large',
        marginLeft: 'auto',
        marginRight: 'auto',
        marginTop: '1%'
    },
    preference: {
        fontSize: 'large',
        marginLeft: 'auto',
        marginRight: 'auto'
    },
    suitabilities: {
        display: 'flex',
        flexDirection: 'row',
        overflowX: 'scroll',
        // marginLeft: '10%',
        // marginRight: '10%',
        // justifyContent: 'space-evenly',
        // width: '50%',
    },
    suitability: {
        display: 'flex',
        flexDirection: 'column',
        backgroundColor: 'gray',
        padding: '1%',
        margin: '1%',
        borderRadius: '5px',
        textAlign: 'center',
        minWidth: '25rem !important'
    },
    mealSuitability: {
        display: 'flex',
        flexDirection: 'row',
        minHeight: '40px'
    },
    mealName: {
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        fontWeight: 'bold'
    },
    mealName2: {
        height: '50%',
    },
    property: {
        marginLeft: 'auto',
        marginRight: 'auto',
        // height: '50%'
    },
    explanation: {
        fontSize: 'small'
    },
    onlySupper: {
        height: '50%',
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center'
    },
    doubleMeal: {
        width: '50%'
    },
    highPossibility: {
        color: 'navy'
    },
    lowPossibility: {
        color: 'orange'
    },
    firstRow: {
        marginLeft: '15%',
        display: 'flex',
        alignItems: 'center',
        fontWeight: 'bold'
    }
}));

export default function SuitableRecipeModal(props) {

    const {onClose, open, recipeId, recipeName, ...other} = props;

    const classes = useStyles();

    const [state, setState] = React.useState({
        suitabilities: [],
        msg: "",
        loaded: false
    });

    useEffect(
        async () => {
            if (recipeId != null) {
                http.get("/api/recipes/checkRecipeSuitability/" + recipeId)
                    .then(async resp => {
                        let table = [];

                        for (let x in resp.data) {
                            table[x] = createRecipeSuitability(resp.data[x], x);
                        }

                        setState({
                            ...state,
                            suitabilities: table,
                            loaded: true,
                        });
                    })
                    .catch(error => console.log(error))
            }
        }, [state.msg, props.adminMode, recipeId]
    );

    const createRecipeSuitability = (data, x) => {
        let suitability = [];
        for (let x in data) {
            suitability[x] = {};
            suitability[x].preferenceCalories = data[x].preferenceCalories;
            suitability[x].suitable = data[x].suitable;
            if (data[x].notSuitableExplanation.includes("(")) {
                const index = data[x].notSuitableExplanation.indexOf("(");
                const length = data[x].notSuitableExplanation.length;
                suitability[x].notSuitableExplanation = data[x].notSuitableExplanation.substring(index + 1, length - 1);
            } else {
                suitability[x].notSuitableExplanation = data[x].notSuitableExplanation;
            }
        }
        return suitability;
    };

    const handleClose = () => {
        onClose();
    };

    return (
        <div>
            <Modal
                aria-labelledby="transition-modal-title"
                aria-describedby="transition-modal-description"
                className={classes.modal}
                open={open}
                onClose={handleClose}
                closeAfterTransition
                BackdropComponent={Backdrop}
                BackdropProps={{
                    timeout: 500,
                }}
            >
                <Fade in={open}>
                    <div className={classes.paper}>
                        <div className={classes.header}>
                            <div className={classes.title}>{recipeName + "'s suitability"}</div>
                            <Tooltip title="Close" aria-label="Close">
                                <Fab aria-label="close">
                                    <CloseIcon fontSize="large" onClick={handleClose}/>
                                </Fab>
                            </Tooltip>
                        </div>
                        <div className={classes.suitabilities}>
                            {state.suitabilities.length > 0 && state.suitabilities.map((suitability, index) => (
                                <div key={"suitability" + index} className={classes.suitability}>
                                    <div className={classes.preference}>
                                        {"Preference " + state.suitabilities[index][0].preferenceCalories + " kcal"}
                                    </div>
                                    <div className={classes.mealSuitability + " " + classes.firstRow}>
                                        <div className={classes.doubleMeal}>
                                            <div className={classes.property}>
                                                4 meals
                                            </div>
                                        </div>
                                        <Divider variant="fullWidth" orientation="vertical"/>
                                        <div className={classes.doubleMeal}>
                                            <div className={classes.property}>
                                                5 meals
                                            </div>
                                        </div>
                                    </div>
                                    <div className={classes.mealSuitability}>
                                        <div className={classes.mealName}>
                                            breakfast
                                        </div>
                                        <div className={classes.property}>
                                            <div
                                                className={suitability[0].suitable ? classes.highPossibility : classes.lowPossibility}>
                                                {suitability[0].suitable ? "High possibility" : "Low possibility"}
                                            </div>
                                            {suitability[0].notSuitableExplanation !== "" ?
                                                <div className={classes.explanation}>
                                                    {"(" + suitability[0].notSuitableExplanation + ")"}
                                                </div> :
                                                null}
                                        </div>
                                    </div>
                                    <div className={classes.mealSuitability}>
                                        <div className={classes.mealName}>
                                            lunch
                                        </div>
                                        <div className={classes.property}>
                                            <div
                                                className={suitability[1].suitable ? classes.highPossibility : classes.lowPossibility}>
                                                {suitability[1].suitable ? "High possibility" : "Low possibility"}
                                            </div>
                                            {suitability[1].notSuitableExplanation !== "" ?
                                                <div className={classes.explanation}>
                                                    {"(" + suitability[1].notSuitableExplanation + ")"}
                                                </div> :
                                                null}
                                        </div>
                                    </div>
                                    <div className={classes.mealSuitability}>
                                        <div className={classes.mealName}>
                                            dinner
                                        </div>
                                        <div className={classes.property}>
                                            <div
                                                className={suitability[2].suitable ? classes.highPossibility : classes.lowPossibility}>
                                                {suitability[2].suitable ? "High possibility" : "Low possibility"}
                                            </div>
                                            {suitability[2].notSuitableExplanation !== "" ?
                                                <div className={classes.explanation}>
                                                    {"(" + suitability[2].notSuitableExplanation + ")"}
                                                </div> :
                                                null}
                                        </div>
                                    </div>
                                    <div className={classes.mealSuitability}>
                                        <div>
                                            <div className={classes.mealName + " " + classes.mealName2}>
                                                tea
                                            </div>
                                            <div className={classes.mealName + " " + classes.mealName2}>
                                                supper
                                            </div>
                                        </div>
                                        <div className={classes.doubleMeal}>
                                            <div className={classes.property + " " + classes.onlySupper}>
                                                -
                                            </div>
                                            <div className={classes.onlySupper}>
                                                <div
                                                    className={suitability[5].suitable ? classes.highPossibility + " " + classes.property : classes.lowPossibility + " " + classes.property}>
                                                    {suitability[5].suitable ? "High possibility" : "Low possibility"}
                                                </div>
                                                {suitability[5].notSuitableExplanation !== "" ?
                                                    <div className={classes.explanation}>
                                                        {"(" + suitability[5].notSuitableExplanation + ")"}
                                                    </div> :
                                                    null}
                                            </div>
                                        </div>
                                        <Divider variant="fullWidth" orientation="vertical"/>
                                        <div className={classes.doubleMeal}>
                                            <div className={classes.property + " " + classes.onlySupper}>
                                                <div
                                                    className={suitability[3].suitable ? classes.highPossibility : classes.lowPossibility}>
                                                    {suitability[3].suitable ? "High possibility" : "Low possibility"}
                                                </div>
                                                {suitability[3].notSuitableExplanation !== "" ?
                                                    <div className={classes.explanation}>
                                                        {"(" + suitability[3].notSuitableExplanation + ")"}
                                                    </div> :
                                                    null}
                                            </div>
                                            <div className={classes.property + " " + classes.onlySupper}>
                                                <div
                                                    className={suitability[4].suitable ? classes.highPossibility : classes.lowPossibility}>
                                                    {suitability[4].suitable ? "High possibility" : "Low possibility"}
                                                </div>
                                                {suitability[4].notSuitableExplanation !== "" ?
                                                    <div className={classes.explanation}>
                                                        {"(" + suitability[4].notSuitableExplanation + ")"}
                                                    </div> :
                                                    null}
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                </Fade>
            </Modal>
        </div>
    );
}