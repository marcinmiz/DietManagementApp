import React, {useEffect} from 'react'
import {Container, makeStyles} from "@material-ui/core";
import http from "../http-common";
import Timeline from "@material-ui/lab/Timeline";
import TimelineItem from "@material-ui/lab/TimelineItem";
import TimelineOppositeContent from "@material-ui/lab/TimelineOppositeContent";
import TimelineSeparator from "@material-ui/lab/TimelineSeparator";
import TimelineConnector from "@material-ui/lab/TimelineConnector";
import TimelineDot from "@material-ui/lab/TimelineDot";
import TimelineContent from "@material-ui/lab/TimelineContent";
import Typography from "@material-ui/core/Typography";
import Avatar from '@material-ui/core/Avatar';
import ReceiptIcon from '@material-ui/icons/Receipt';
import Fab from "@material-ui/core/Fab";
import NavigateBeforeRoundedIcon from '@material-ui/icons/NavigateBeforeRounded';
import NavigateNextRoundedIcon from '@material-ui/icons/NavigateNextRounded';

const useStyles = makeStyles({
    meal_hour: {
        marginTop: 'auto',
        marginBottom: 'auto'
    },
    meal_description: {
        marginTop: 'auto',
        marginBottom: 'auto',
        textAlign: 'left'
    },
    timeline: {
        marginTop: 'auto',
        marginBottom: 'auto'
    },
    photo_placeholder: {
        fontSize: 40
    },
    recipe_description: {
        display: 'flex',
        flexDirection: 'column',
    },
    recipe_row: {
        display: 'flex',
        alignItems: 'center'
    },
    recipe_nutrients: {
        display: 'flex',
        flexDirection: 'row',
        marginLeft: '2%'
    },
    nutrient: {
        marginLeft: '10px'
    },
    recipe_name: {
        marginLeft: '2%'
    },
    item: {
        minHeight: '140px'
    },
    redFont: {
        color: 'darkred'
    },
    greenFont: {
        color: 'darkgreen'
    }
});

export default function Menus(props) {
    const classes = useStyles();

    const [state, setState] = React.useState({
        // menu: {
        //     "menuId": 1,
        //     "menuName": "Tuesday",
        //     "menuDate": "12.10.2021",
        //     "mealsQuantity": 4,
        //     "meals": [
        //         {
        //             "mealId": 1,
        //             "mealName": "breakfast",
        //             "mealHourTime": "8:00:00",
        //             "recipe": {
        //                 "recipeId": 618,
        //                 "recipeName": "quesadilla",
        //                 "recipeImage": "",
        //                 "calories": 250,
        //                 "protein": 20,
        //                 "carbohydrate": 15,
        //                 "fat": 12,
        //                 "belongsToCollection": true,
        //                 "likedInPreference": false
        //             }
        //         },
        //         {
        //             "mealId": 2,
        //             "mealName": "lunch",
        //             "mealHourTime": "11:20:00",
        //             "recipe": {
        //                 "recipeId": 623,
        //                 "recipeName": "risotto",
        //                 "recipeImage": "http://localhost:8097/api/images/get/recipe/recipe623.jpg",
        //                 "calories": 320,
        //                 "protein": 35,
        //                 "carbohydrate": 45,
        //                 "fat": 24,
        //                 "belongsToCollection": false,
        //                 "likedInPreference": true
        //             }
        //         }, {
        //             "mealId": 3,
        //             "mealName": "dinner",
        //             "mealHourTime": "14:30:00",
        //             "recipe": {
        //                 "recipeId": 618,
        //                 "recipeName": "quesadilla",
        //                 "recipeImage": "http://localhost:8097/api/images/get/recipe/recipe618.jpg",
        //                 "calories": 250,
        //                 "protein": 20,
        //                 "carbohydrate": 15,
        //                 "fat": 12,
        //                 "belongsToCollection": true,
        //                 "likedInPreference": false
        //             }
        //         },
        //         {
        //             "mealId": 4,
        //             "mealName": "supper",
        //             "mealHourTime": "18:45:00",
        //             "recipe": {
        //                 "recipeId": 623,
        //                 "recipeName": "risotto",
        //                 "recipeImage": "http://localhost:8097/api/images/get/recipe/recipe623.jpg",
        //                 "calories": 320,
        //                 "protein": 35,
        //                 "carbohydrate": 45,
        //                 "fat": 24,
        //                 "belongsToCollection": false,
        //                 "likedInPreference": true
        //             }
        //         }
        //     ]
        // },
        menus: [],
        currentDietaryProgrammeDay: null,
        msg: "",
        loaded: false
    });
    useEffect(
        async () => {
            if (props.currentDietaryProgramme) {
                http.get("/api/menus/" + props.currentDietaryProgramme.dietaryProgrammeId)
                    .then(async resp => {
                        let table = [];

                        for (let x in resp.data) {
                            table[x] = createMenu(resp.data, x);
                        }

                        setState({
                            ...state,
                            menus: table,
                            currentDietaryProgrammeDay: props.currentDietaryProgrammeDay - 1,
                            loaded: true,
                        });
                    })
                    .catch(error => console.log(error))
            }
        }, [props.adminMode]
    );

    const createMenu = (data, x) => {
        let menu = {};

        menu.dailyMenuId = data[x].dailyMenuId;
        menu.dailyMenuName = data[x].dailyMenuName;

        let menuDate = new Date(data[x].dailyMenuDate);
        menu.dailyMenuDate = menuDate.toLocaleDateString();

        menu.mealsQuantity = data[x].mealsQuantity;

        let meals = data[x].meals;
        menu.meals = [];

        for (let i in meals) {
            menu.meals[i] = {};
            let parts = meals[i].split(";");
            menu.meals[i].mealName = parts[0];
            let mealTime = new Date(parts[1]);
            menu.meals[i].mealTime = mealTime.toLocaleTimeString();
            menu.meals[i].recipeImage = parts[2];
            menu.meals[i].recipeName = parts[3];
            menu.meals[i].recipeCalories = parts[4];
            menu.meals[i].recipeProteins = parts[5];
            menu.meals[i].recipeCarbohydrates = parts[6];
            menu.meals[i].recipeFats = parts[7];
            menu.meals[i].belongsToCollection = parts[8];
            menu.meals[i].likedInPreference = parts[9];
        }

        return menu;
    };

    const handlePrevDailyMenu = () => {

        let day = (state.currentDietaryProgrammeDay - 1);

        if (day < 0) {
            day = state.menus.length - 1;
        }

        setState({
            ...state,
            currentDietaryProgrammeDay: day
        });
    };

    const handleNextDailyMenu = () => {
        setState({
            ...state,
            currentDietaryProgrammeDay: (state.currentDietaryProgrammeDay + 1) % state.menus.length
        });
    };

    return (
        <Container id="main_container" maxWidth="lg">
            <div className="page_container">
                <div>
                    <h2>Daily Menus</h2>
                </div>
                {props.currentDietaryProgramme ?
                    <div className="dailyMenu">
                        <Fab aria-label="prev">
                            <NavigateBeforeRoundedIcon fontSize="large" onClick={handlePrevDailyMenu}/>
                        </Fab>
                        <div className="dailyMenuContent">
                            <div>
                                <h3>{state.menus[state.currentDietaryProgrammeDay] && state.menus[state.currentDietaryProgrammeDay].dailyMenuName}</h3>
                            </div>
                            <Timeline position="alternate" className="menuList">
                                {state.menus[state.currentDietaryProgrammeDay] && state.menus[state.currentDietaryProgrammeDay].meals.map((meal, index) => (
                                    <TimelineItem key={index} className={classes.item}>
                                        <TimelineOppositeContent
                                            className={classes.meal_hour}
                                        >
                                            {meal.mealTime}
                                        </TimelineOppositeContent>
                                        <TimelineSeparator>
                                            <TimelineConnector/>
                                            <TimelineDot>
                                            </TimelineDot>
                                            <TimelineConnector/>
                                        </TimelineSeparator>
                                        <TimelineContent className={classes.meal_description}>
                                            <Typography variant="h6" component="span">
                                                {meal.mealName}
                                            </Typography>
                                            <Typography>
                                                <div className={classes.recipe_description}>
                                                    <div className={classes.recipe_row}>
                                                        {meal.recipeImage !== '' ?
                                                            <Avatar className="menuImage" src={meal.recipeImage}/>
                                                            : <ReceiptIcon className={classes.photo_placeholder}/>}
                                                        <div className={classes.recipe_name}>
                                                            {meal.recipeName}
                                                        </div>
                                                    </div>
                                                    <div className={classes.recipe_row}>
                                                        {meal.recipeCalories} kCal
                                                        <div className={classes.recipe_nutrients}>
                                                            <div>
                                                                Proteins: {meal.recipeProteins} g
                                                            </div>
                                                            <div className={classes.nutrient}>
                                                                Carbohydrates: {meal.recipeCarbohydrates} g
                                                            </div>
                                                            <div className={classes.nutrient}>
                                                                Fats: {meal.recipeFats} g
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div className={classes.recipe_row}>
                                                        <div>
                                                            belongs to collection:
                                                            <span
                                                                className={meal.belongsToCollection === "Yes" ? classes.greenFont : classes.redFont}>
                    {meal.belongsToCollection}
                    </span>
                                                        </div>
                                                        <div className={classes.recipe_name}>
                                                            liked in preference:
                                                            <span
                                                                className={meal.likedInPreference === "Yes" ? classes.greenFont : (meal.likedInPreference === "No" ? classes.redFont : "")}>
                    {meal.likedInPreference}
                    </span>
                                                        </div>
                                                    </div>
                                                </div>
                                            </Typography>
                                        </TimelineContent>
                                    </TimelineItem>
                                ))}
                            </Timeline>
                        </div>
                        <Fab className="next_button" aria-label="next">
                            <NavigateNextRoundedIcon fontSize="large" onClick={handleNextDailyMenu}/>
                        </Fab>
                    </div> :
                    <div className="menuNotUsedProgramme">No dietary programme is used</div>}
            </div>
        </Container>
    );
}
