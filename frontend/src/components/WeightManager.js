import React, {useEffect} from 'react';
import "../Authentication.css";
import http from "../http-common";
import {Divider, makeStyles} from "@material-ui/core";

const useStyles = makeStyles({
    weight_field: {
        display: 'flex',
        flexDirection: 'column',
        // alignItems: 'center',
        // justifyContent: 'center',
        // width: '50%',
        marginLeft: 'auto',
        marginRight: 'auto'
    },
    weight_input: {
        display: 'flex',
        flexDirection: 'row',
        alignItems: 'center',
    },
    addWeightButton: {
        margin: '10px'
    },
    weightsList: {
        maxHeight: '50vh',
        width: '80%',
        marginLeft: 'auto',
        marginRight: 'auto',
        overflowY: 'scroll',
        margin: '2%',
        backgroundColor: '#b59c88',
        borderRadius: '5px',
        padding: '1%'
    },
    weight: {
        display: 'flex',
        justifyContent: 'left',
        padding: '1%'
    },
    weightPart: {
        marginRight: '10%'
    }
});

export default function ResetPassword(props) {
    const classes = useStyles();

    const [state, setState] = React.useState({
        weight: 45.0,
        weights: [],
        msg: "",
    });

    useEffect(
        async () => {
            let response = await http.get("/api/users/getLoggedUserWeights");

            let weightsList = [], iterationLimit = response.data[0].length;

            if (props.currentDietaryProgramme != null) {
                iterationLimit--;
            }

            for (let x = 0; x < iterationLimit; x++) {
                weightsList[x] = createWeight(response.data[0], x);
            }

            setState({
                ...state,
                weights: weightsList,
            });

        }, [state.msg]
    );

    const createWeight = (data, x) => {
        let weight = {};
        weight.weightId = data[x].weightId;
        weight.weightValue = data[x].weightValue;
        weight.measureDate = data[x].measureDate;

        return weight;
    };

    const handleChange = (event) => {
        setState({
            ...state,
            [event.target.name]: event.target.value
        });
    };

    const handleSubmit = async (event) => {
        try {
            let result;
            console.log(state.weight);

            result = await http.get("/api/users/addWeight/" + state.weight);

            setState({
                ...state,
                msg: result.data.message
            });

        } catch (err) {

            setState({
                ...state,
                "msg": "user is not logged"
            });
            console.error(err);
        }

    };

    return (
        <div>
            {state.msg !== "" ? <div className="msg">{state.msg}</div> : null}

            <form>
                <div className="setting_header">Weights</div>

                <div className={classes.weightsList}>
                    {state.weights.map((weight, index) => (
                        <div>
                            <div key={"weight" + index} className={classes.weight}>
                                <div className={classes.weightPart}>{index + 1 + ". "}</div>
                                <div className={classes.weightPart}>{weight.weightValue + " kg"}</div>
                                <div>{new Date(weight.measureDate).toLocaleDateString()}</div>
                            </div>
                            <Divider variant="middle"/>
                        </div>
                    ))}
                </div>

                <div className={classes.weight_field}>
                    <label htmlFor="weight">Enter weight</label>
                    <div className={classes.weight_input}>
                        <input
                            className="input_field"
                            id="weight"
                            type="number"
                            name="weight"
                            value={state.weight}
                            autoComplete="off"
                            onChange={event => handleChange(event)}
                            required/>
                        <button className={classes.addWeightButton + " form_button"} name="change_password_button"
                                type="button"
                                onClick={event => handleSubmit(event)}>Save
                        </button>
                    </div>
                </div>
            </form>

        </div>
    );
}