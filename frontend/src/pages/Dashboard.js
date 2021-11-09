import React, {useEffect} from 'react';
import {Container, makeStyles} from "@material-ui/core";
import CanvasJSReact from '../canvasjs.react';
import http from "../http-common";

let CanvasJSChart = CanvasJSReact.CanvasJSChart;

const useStyles = makeStyles({
    chartsContainer: {
        display: 'flex',
        justifyContent: 'space-around',
    },
    chartsContainerPart: {
        width: '48%',
    },
    label: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        borderRadius: '5px',
        width: '60%',
        minHeight: '10%',
        padding: '2%',
        marginLeft: 'auto',
        marginRight: 'auto',
    },
    currentWeight: {
        fontSize: 'xx-large'
    },
    greenFont: {
        color: 'darkgreen'
    },
    redFont: {
        color: 'darkred'
    }
});

export default function Dashboard(props) {
    const classes = useStyles();
    let lastWeight;

    const [initialized, setInitialized] = React.useState(false);

    const [dataPoints, setDataPoints] = React.useState([]);
    const [dataPoints2, setDataPoints2] = React.useState([]);
    const [dataPoints3, setDataPoints3] = React.useState([]);
    const [weightTrend, setWeightTrend] = React.useState(false);
    const [weightTrendCondition, setWeightTrendCondition] = React.useState(false);
    const [weightTrendCondition2, setWeightTrendCondition2] = React.useState(true);
    const [dataPoints4, setDataPoints4] = React.useState([]);
    const [consumedCalories, setConsumedCalories] = React.useState(0);
    const [totalCalories, setTotalCalories] = React.useState(0);

    const options = {
        theme: "dark2",
        backgroundColor: "transparent",
        animationEnabled: true,
        exportEnabled: true,
        zoomEnabled: true,
        title: {
            text: "Weight diagram"
        },
        axisX: {
            crosshair: {
                enabled: true,
                snapToDataPoint: true
            }
        },
        axisY: {
            title: "Weight",
            suffix: "kg",
            includeZero: false,
            crosshair: {
                enabled: true,
                snapToDataPoint: true
            }
        },
        data: [{
            type: "scatter",
            name: "Real weight",
            showInLegend: true,
            markerSize: 10,
            markerColor: "blue",
            xValueFormatString: "DD MM YYYY",
            toolTipContent: "<b>Date: </b>{x}<br/><b>Weight: </b>{y} kg",
            dataPoints: dataPoints
        }, {
            type: "scatter",
            name: "Target weight",
            showInLegend: true,
            markerSize: 10,
            markerColor: "green",
            xValueFormatString: "DD MM YYYY",
            toolTipContent: "<b>Date: </b>{x}<br/><b>Weight: </b>{y} kg",
            dataPoints: dataPoints2
        }, {
            type: "line",
            name: "Trend line",
            showInLegend: true,
            markerSize: 0,
            lineThickness: 5,
            xValueFormatString: "DD MM YYYY",
            toolTipContent: "<b>Date: </b>{x}<br/><b>Weight: </b>{y} kg",
            dataPoints: dataPoints3
        }]
    };

    const options2 = {
        animationEnabled: true,
        exportEnabled: true,
        theme: "dark2", // "light1", "dark1", "dark2"
        backgroundColor: "transparent",
        title: {
            text: "Calories consumed"
        },
        data: [{
            type: "pie",
            indexLabel: "{label}: {y}%",
            startAngle: -90,
            dataPoints: dataPoints4
        }]
    };

    useEffect(() => {
        if (!initialized) {
            setTimeout(async () => {
                let response = await http.get("/api/users/getLoggedUserWeights");

                let weightsList = [];

                for (let x = 0; x < response.data[0].length; x++) {
                    weightsList[x] = createWeight(response.data[0], x);
                }

                let response2 = await http.get("/api/users/getWeightTrend");

                setWeightTrend(response2.data);
                let trend = response2.data;

                if (props.currentDietaryProgramme != null) {

                    let date = new Date(weightsList[weightsList.length - 1].x);
                    let targetWeight = weightsList[weightsList.length - 1].y;
                    setDataPoints2([
                        {x: date, y: targetWeight}
                    ]);

                    weightsList.splice(weightsList.length - 1, 1);

                    // let mapping = weightsList.map(weight => {
                    //     const diff = Math.abs(new Date(props.dietaryProgrammeStartDate) - weight.x);
                    //     return {weight: weight, diff: diff};
                    // });
                    //
                    // let min = Math.min.apply(null, mapping.map(weight => weight.diff));
                    //
                    // let nearestProgrammeWeight = mapping.find(m => m.diff === min);

                    lastWeight = weightsList[weightsList.length - 1].y;

                    if (lastWeight > targetWeight) {
                        setWeightTrendCondition(trend < 0.0);
                        setWeightTrendCondition2(trend < 0.0);
                    } else if (lastWeight === targetWeight) {
                        setWeightTrendCondition(trend === 0.0);
                        setWeightTrendCondition2(trend === 0.0);
                    } else {
                        setWeightTrendCondition(trend > 0.0);
                        setWeightTrendCondition2(trend > 0.0);
                    }

                }
                console.log(weightTrendCondition);
                console.log(weightTrendCondition2);

                setDataPoints(weightsList
                    //     [
                    //     {x: new Date("10 20 2021"), y: 100},
                    //     {x: new Date("10 21 2021"), y: 98},
                    //     {x: new Date("10 23 2021"), y: 95},
                    //     {x: new Date("10 26 2021"), y: 98},
                    //     {x: new Date("10 29 2021"), y: 93},
                    //     {x: new Date("11 01 2021"), y: 86}
                    // ]
                );
                let trendList = [];

                for (let x = 0; x < response.data[1].length; x++) {
                    trendList[x] = createWeight(response.data[1], x);
                }

                setDataPoints3(trendList);

                let response3 = await http.get("/api/menus/getCaloriesConsumed");

                let data = response3.data;

                setConsumedCalories(data[0]);
                setTotalCalories(data[1]);

                setDataPoints4([
                    {y: data[2], label: "Consumed"},
                    {y: data[3], label: "Not consumed"}
                ]);
                // Calling render makes no difference
                setInitialized(true);
            }, 1);
        }
    }, [initialized]);

    const createWeight = (data, x) => {
        return {x: new Date(data[x].measureDate), y: data[x].weightValue};
    };

    return (
        <Container id="main_container" maxWidth="lg">
            <div className="page_container">
                <div>
                    <h2>Dashboard</h2>
                    {!initialized ? (
                        <div className="loading">Loading</div>
                    ) : (
                        <div className={classes.chartsContainer}>
                            <div className={classes.chartsContainerPart}>
                                <div className={classes.label}>
                                    <div className={classes.currentWeight}>
                                        {dataPoints[dataPoints.length - 1] && dataPoints[dataPoints.length - 1].y + " kg"}
                                    </div>
                                    {dataPoints.length >= 1 ? <div className={weightTrendCondition && weightTrendCondition2 ? classes.greenFont : (!weightTrendCondition && !weightTrendCondition2 ? classes.redFont : "")}>
                                            {weightTrend >= 0 ? "+" + weightTrend + " %" : weightTrend + " %"}
                                        </div> :
                                        null}
                                </div>
                                <div>
                                    {dataPoints.length >= 1 ?
                                        <CanvasJSChart options={options}/>
                                        :
                                        "There are no weights to display. Add new weight in settings"
                                    }
                                </div>
                            </div>
                            <div className={classes.chartsContainerPart}>
                                <div className={classes.label}>
                                    <div className={classes.currentWeight}>{consumedCalories + " kcal of " + totalCalories + " kcal"}</div>
                                </div>
                                <div>
                                    <CanvasJSChart options={options2}
                                        // onRef={ref => this.chart = ref}
                                    />
                                </div>
                            </div>
                        </div>)}
                </div>
            </div>
        </Container>
    );
}