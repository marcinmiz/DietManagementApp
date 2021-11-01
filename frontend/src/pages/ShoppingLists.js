import React, {useEffect} from 'react'
import {Container, makeStyles} from "@material-ui/core";
import http from "../http-common";
import Fab from "@material-ui/core/Fab";
import NavigateBeforeRoundedIcon from '@material-ui/icons/NavigateBeforeRounded';
import NavigateNextRoundedIcon from '@material-ui/icons/NavigateNextRounded';

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
    }
});

export default function ShoppingLists(props) {
    const classes = useStyles();

    const [state, setState] = React.useState({
        shoppingLists: [],
        currentWeekShoppingList: null,
        startWeekDate: null,
        endWeekDate: null,
        msg: "",
        loaded: false
    });
    useEffect(
        async () => {
            if (props.currentDietaryProgramme) {
                let startWeekDate = new Date(props.dietaryProgrammeStartDate);
                let endWeekDate = new Date(props.dietaryProgrammeStartDate);
                startWeekDate.setDate(startWeekDate.getDate() + (7 * (Math.floor(props.currentDietaryProgrammeDay / 7))));
                endWeekDate.setDate(startWeekDate.getDate() + 6 + (7 * (Math.floor(props.currentDietaryProgrammeDay / 7))));
                startWeekDate = startWeekDate.toLocaleDateString();
                endWeekDate = endWeekDate.toLocaleDateString();

                http.get("/api/shopping/")
                    .then(async resp => {
                        let table = [];
                        let counter = 0;

                        for (let x in resp.data) {
                            table[x] = [];
                            for (let y = 0; y < resp.data[x].length; y = y + 2) {
                                table[x][counter] = createShoppingList(resp.data[x], y);
                                counter++;
                            }
                        }

                        setState({
                            ...state,
                            startWeekDate: startWeekDate,
                            endWeekDate: endWeekDate,
                            shoppingLists: table,
                            currentWeekShoppingList: Math.floor(props.currentDietaryProgrammeDay / 7),
                            loaded: true,
                        });
                    })
                    .catch(error => console.log(error))
            }
        }, [props.adminMode]
    );

    const createShoppingList = (data, y) => {
        let shoppingList = {};

        shoppingList.productName = data[y];
        let parts = data[y + 1].split(" ");
        shoppingList.productAmount = parts[0];
        shoppingList.productUnit = parts[1];

        return shoppingList;
    };

    const handlePrevShoppingList = () => {

        let week = (state.currentWeekShoppingList - 1);

        if (week < 0) {
            week = state.shoppingLists.length - 1;
        }

        setState({
            ...state,
            currentWeekShoppingList: week
        });
    };

    const handleNextShoppingList = () => {

        setState({
            ...state,
            currentWeekShoppingList: (state.currentWeekShoppingList + 1) % state.shoppingLists.length
        });
    };

    return (
        <Container id="main_container" maxWidth="lg">
            <div className="page_container">
                <div>
                    <h2>Shopping Lists</h2>
                </div>
                {props.currentDietaryProgramme ?
                    <div className="dailyMenu">
                        <Fab aria-label="prev">
                            <NavigateBeforeRoundedIcon fontSize="large" onClick={handlePrevShoppingList}/>
                        </Fab>
                        <div className="dailyMenuContent">
                            <div className={classes.centralContainer}>
                                <div>
                                    <h3>{state.startWeekDate != null && state.endWeekDate != null && state.startWeekDate + " - " + state.endWeekDate}</h3>
                                </div>
                                <div className="menuList">
                                    {state.shoppingLists[state.currentWeekShoppingList] && state.shoppingLists[state.currentWeekShoppingList].map((product, index) => (
                                        <div key={index} className={classes.product}>
                                            <div className={classes.productName}>
                                                {product.productName}
                                            </div>
                                            <div className={classes.productAmount}>
                                                <div>
                                                    {product.productAmount}
                                                </div>
                                                <div className={classes.productPart}>
                                                    {product.productUnit}
                                                </div>
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            </div>
                        </div>
                        <Fab className="next_button" aria-label="next">
                            <NavigateNextRoundedIcon fontSize="large" onClick={handleNextShoppingList}/>
                        </Fab>
                    </div> :
                    <div className="menuNotUsedProgramme">No dietary programme is used</div>}
            </div>
        </Container>
    );
}