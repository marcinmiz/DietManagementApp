import React, {useEffect} from 'react'
import {Container, makeStyles} from "@material-ui/core";
import Timeline from '@material-ui/lab/Timeline';
import TimelineItem from '@material-ui/lab/TimelineItem';
import TimelineSeparator from '@material-ui/lab/TimelineSeparator';
import TimelineConnector from '@material-ui/lab/TimelineConnector';
import TimelineContent from '@material-ui/lab/TimelineContent';
import TimelineOppositeContent from '@material-ui/lab/TimelineOppositeContent';
import TimelineDot from '@material-ui/lab/TimelineDot';
import FastfoodIcon from '@material-ui/icons/Fastfood';
import LaptopMacIcon from '@material-ui/icons/LaptopMac';
import HotelIcon from '@material-ui/icons/Hotel';
import RepeatIcon from '@material-ui/icons/Repeat';
import Typography from '@material-ui/core/Typography';
import ReceiptIcon from '@material-ui/icons/Receipt';
import Avatar from '@material-ui/core/Avatar';

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
        menu: {
            "menuId": 1,
            "menuName": "Tuesday",
            "menuDate": "12.10.2021",
            "mealsQuantity": 4,
            "meals": [
                {
                    "mealId": 1,
                    "mealName": "breakfast",
                    "mealHourTime": "8:00:00",
                    "recipe": {
                        "recipeId": 618,
                        "recipeName": "quesadilla",
                        "recipeImage": "",
                        "calories": 250,
                        "protein": 20,
                        "carbohydrate": 15,
                        "fat": 12,
                        "belongsToCollection": true,
                        "likedInPreference": false
                    }
                },
                {
                    "mealId": 2,
                    "mealName": "lunch",
                    "mealHourTime": "11:20:00",
                    "recipe": {
                        "recipeId": 623,
                        "recipeName": "risotto",
                        "recipeImage": "http://localhost:8097/api/images/get/recipe/recipe623.jpg",
                        "calories": 320,
                        "protein": 35,
                        "carbohydrate": 45,
                        "fat": 24,
                        "belongsToCollection": false,
                        "likedInPreference": true
                    }
                }, {
                    "mealId": 3,
                    "mealName": "dinner",
                    "mealHourTime": "14:30:00",
                    "recipe": {
                        "recipeId": 618,
                        "recipeName": "quesadilla",
                        "recipeImage": "http://localhost:8097/api/images/get/recipe/recipe618.jpg",
                        "calories": 250,
                        "protein": 20,
                        "carbohydrate": 15,
                        "fat": 12,
                        "belongsToCollection": true,
                        "likedInPreference": false
                    }
                },
                {
                    "mealId": 4,
                    "mealName": "supper",
                    "mealHourTime": "18:45:00",
                    "recipe": {
                        "recipeId": 623,
                        "recipeName": "risotto",
                        "recipeImage": "http://localhost:8097/api/images/get/recipe/recipe623.jpg",
                        "calories": 320,
                        "protein": 35,
                        "carbohydrate": 45,
                        "fat": 24,
                        "belongsToCollection": false,
                        "likedInPreference": true
                    }
                },
                {
                    "mealId": 4,
                    "mealName": "supper",
                    "mealHourTime": "18:45:00",
                    "recipe": {
                        "recipeId": 623,
                        "recipeName": "risotto",
                        "recipeImage": "http://localhost:8097/api/images/get/recipe/recipe623.jpg",
                        "calories": 320,
                        "protein": 35,
                        "carbohydrate": 45,
                        "fat": 24,
                        "belongsToCollection": false,
                        "likedInPreference": true
                    }
                }
            ]
        },
        msg: "",
        loaded: false
    });

    console.log(state.menu);

    return (
        <Container id="main_container" maxWidth="lg">
            <div className="page_container">
                <div>
                    <h2>Daily Menus</h2>
                </div>
                <div className="dailyMenu">
                    <div><h3>Day 5 of 10</h3></div>
                    <Timeline position="alternate" className="menuList">
                        {state.menu.meals.map((meal, index) => (
                            <TimelineItem key={index} className={classes.item}>
                                <TimelineOppositeContent
                                    className={classes.meal_hour}
                                >
                                    {meal.mealHourTime}
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
                                                {meal.recipe.recipeImage !== '' ?
                                                    <Avatar className="menuImage" src={meal.recipe.recipeImage}/>
                                                    : <ReceiptIcon className={classes.photo_placeholder}/>}
                                                <div className={classes.recipe_name}>
                                                    {meal.recipe.recipeName}
                                                </div>
                                            </div>
                                            <div className={classes.recipe_row}>
                                                {meal.recipe.calories} kCal
                                                <div className={classes.recipe_nutrients}>
                                                    <div>
                                                        Protein: {meal.recipe.protein} g
                                                    </div>
                                                    <div className={classes.nutrient}>
                                                        Carbohydrate: {meal.recipe.carbohydrate} g
                                                    </div>
                                                    <div className={classes.nutrient}>
                                                        Fat: {meal.recipe.fat} g
                                                    </div>
                                                </div>
                                            </div>
                                            <div className={classes.recipe_row}>
                                                <div>
                                                    belongs to collection:
                                                    <span
                                                        className={meal.recipe.belongsToCollection ? classes.greenFont : classes.redFont}>
                                                        {meal.recipe.belongsToCollection ? "Yes" : "No"}
                                                    </span>
                                                </div>
                                                <div className={classes.recipe_name}>
                                                    liked in preference:
                                                    <span
                                                        className={meal.recipe.likedInPreference ? classes.greenFont : classes.redFont}>
                                                    {meal.recipe.likedInPreference ? "Yes" : "No"}
                                                    </span>
                                                </div>
                                            </div>
                                        </div>
                                        {/*<img src={meal.recipe.recipeImage} alt={meal.recipe.recipeName}*/}
                                        {/*className="product_image"/> :*/}
                                        {/*<div><ReceiptIcon className={classes.photo_placeholder}/></div>}*/}
                                    </Typography>
                                </TimelineContent>
                            </TimelineItem>
                        ))}
                    </Timeline>
                </div>
            </div>
        </Container>
    );
}
