import React, {useEffect} from 'react';
import {Container, makeStyles} from "@material-ui/core";
import CanvasJS from "../canvasjs.min";
import CanvasJSReact from '../canvasjs.react';

var CanvasJSChart = CanvasJSReact.CanvasJSChart;

const useStyles = makeStyles({
    chartsContainer: {
        display: 'flex',
        // alignItems: 'center',
        justifyContent: 'space-around',
        // marginLeft: 'auto',
        // marginRight: 'auto',
        // marginTop: '2%',
        // marginBottom: '2%'
    },
    chartsContainerPart: {
        width: '48%',
        // boxShadow: 'inset 0px 0px 40px 40px #DBA632'
    },
    label: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        // backgroundColor: '#8a7666',
        borderRadius: '5px',
        width: '20%',
        minHeight: '10%',
        padding: '2%',
        marginLeft: 'auto',
        marginRight: 'auto',
        marginBottom: '1%',
        // boxShadow: 'inset 0px 0px 4px 4px dimgrey'
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

    const [initialized, setInitialized] = React.useState(false);

    const [dataPoints, setDataPoints] = React.useState([]);
    const [dataPoints2, setDataPoints2] = React.useState([]);

    const options = {
        theme: "dark2",
        backgroundColor: "transparent",
        animationEnabled: true,
        exportEnabled: true,
        zoomEnabled: true,
        title: {
            text: "Ice Cream Sales vs Temperature"
        },
        axisX: {
            title: "Temperature (in °C)",
            suffix: "°C",
            crosshair: {
                enabled: true,
                snapToDataPoint: true
            }
        },
        axisY: {
            title: "Sales",
            includeZero: false,
            crosshair: {
                enabled: true,
                snapToDataPoint: true
            }
        },
        data: [{
            type: "scatter",
            markerSize: 10,
            markerColor: "blue",
            toolTipContent: "<b>Temperature: </b>{x}°C<br/><b>Sales: </b>{y}",
            dataPoints: dataPoints
        }]
    };

    const options2 = {
        animationEnabled: true,
        exportEnabled: true,
        theme: "dark2", // "light1", "dark1", "dark2"
        backgroundColor: "transparent",
        title: {
            text: "Trip Expenses"
        },
        data: [{
            type: "pie",
            indexLabel: "{label}: {y}%",
            startAngle: -90,
            dataPoints: dataPoints2
        }]
    };

    useEffect(() => {
        if (!initialized) {
            setTimeout(() => {
                setDataPoints([
                    {x: 14.2, y: 215},
                    {x: 12.9, y: 175},
                    {x: 16.4, y: 325},
                    {x: 26.9, y: 635},
                    {x: 32.5, y: 464},
                    {x: 22.1, y: 522},
                    {x: 19.4, y: 412},
                    {x: 25.1, y: 614},
                    {x: 34.9, y: 374},
                    {x: 28.7, y: 625},
                    {x: 23.4, y: 544},
                    {x: 31.4, y: 502},
                    {x: 40.8, y: 262},
                    {x: 37.4, y: 312},
                    {x: 42.3, y: 202},
                    {x: 39.1, y: 302},
                    {x: 17.2, y: 408}
                ]);

                setDataPoints2([
                    {y: 20, label: "Airfare"},
                    {y: 24, label: "Food & Drinks"},
                    {y: 10, label: "Accomodation"},
                    {y: 14, label: "Transportation"},
                    {y: 22, label: "Activities"},
                    {y: 10, label: "Misc"}
                ]);
                // setTitle("Update animates if first render deferred");
                //Calling render makes no difference
                setInitialized(true);
            }, 1);
        }
    }, [initialized]);

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
                                    <div>80 kg</div>
                                    <div className={classes.greenFont}>-2%</div>
                                </div>
                                <div>
                                    <CanvasJSChart options={options}/>
                                </div>
                            </div>
                            <div className={classes.chartsContainerPart}>
                                <div className={classes.label}>
                                    <div>1548/2220 kcal</div>
                                </div>
                                <div>
                                    <CanvasJSChart options={options2}/>
                                </div>
                            </div>
                        </div>)}
                </div>
            </div>
        </Container>
    );
}