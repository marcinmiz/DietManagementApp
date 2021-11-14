import React, {useEffect} from 'react'
import {Container, makeStyles, Tooltip} from "@material-ui/core";
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
import ViewerModal from "../components/ViewerModal";
import {useHistory, useParams} from "react-router-dom";
import Button from "@material-ui/core/Button";
import IconButton from "@material-ui/core/IconButton";
import DeleteIcon from '@material-ui/icons/Delete';
import CheckIcon from '@material-ui/icons/Check';
import ClearIcon from '@material-ui/icons/Clear';

const useStyles = makeStyles({
    meal_hour: {
        marginTop: 'auto',
        marginBottom: 'auto'
    },
    meal_description: {
        width: '100%',
        position: 'relative',
        marginTop: 'auto',
        marginBottom: 'auto',
        marginLeft: '1%',
        textAlign: 'left',
        backgroundColor: '#8a7666',
        borderRadius: '5px',
        padding: '1%'
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
    centralContainer: {
        width: '87%',
        marginLeft: '0'
    },
    past_meal: {
        backgroundColor: 'rgba(0, 0, 0, 0.5) !important',
        zIndex : '100',
        position: 'absolute',
        top: 0,
        right: 0,
        width: '100%',
        height: '100%',
        borderRadius: '5px',
        // textAlign: 'center'
    },
    consumedButtons: {
        display: 'flex',
        justifyContent: 'space-evenly',
        width: '45%',
        height: '45%',
        opacity: '0.7',
        position: 'absolute',
        top: '27.5%',
        right: '27.5%'
    },
    viewRecipe: {
        height: '30%',
        width: '30%',
        cursor: 'pointer',
        marginTop: '7%'
    },
    center: {
        textAlign: 'center'
    }
});

export default function Menus(props) {
    const classes = useStyles();
    const history = useHistory();
    let {recipeId} = useParams();

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
        menuRecipes: [],
        currentDietaryProgrammeDay: null,
        openViewerModal: false,
        recipe_index: 'new',
        msg: "",
        loaded: false
    });

    useEffect(
        async () => {
            if (props.currentDietaryProgramme) {
                try {
                    let response = await http.get("/api/menus/" + props.currentDietaryProgramme.dietaryProgrammeId);
                    let menus = [], menuRecipes = [];
                    let currentDietaryProgrammeDay = props.currentDietaryProgrammeDay - 1;

                    for (let x in response.data) {
                        menus[x] = createMenu(response.data, x);
                    }

                    console.log(currentDietaryProgrammeDay);

                    for (let i in menus[currentDietaryProgrammeDay].meals) {
                        menuRecipes[i] = menus[currentDietaryProgrammeDay].meals[i].recipe;
                    }

                    let recipe_index = 'new';
                    if (recipeId !== 'new') {
                        for (let i = 0; i < menuRecipes.length; i++) {
                            if (menuRecipes[i]) {
                                if (menuRecipes[i].recipe_id === Number(recipeId)) {
                                    console.log("dwd");
                                    recipe_index = i;
                                }
                            }
                        }
                    }
                    console.log(menus);

                    if (!Number.isInteger(Number(recipeId))) {
                        history.push('/menus');
                    }

                    setState({
                        ...state,
                        menus: menus,
                        menuRecipes: menuRecipes,
                        openViewerModal: Number.isInteger(Number(recipeId)),
                        recipe_index: recipe_index,
                        currentDietaryProgrammeDay: currentDietaryProgrammeDay,
                        loaded: true,
                    });
                } catch (e) {
                    console.log(e);
                }

            }
        }, [props.adminMode, recipeId]
    );

    const createMenu = (data, x) => {
        let menu = {};

        menu.dailyMenuId = data[x].dailyMenuId;
        menu.dailyMenuName = data[x].dailyMenuName;
        menu.dailyMenuDate = data[x].dailyMenuDate;
        menu.mealsQuantity = data[x].mealsQuantity;

        let meals = data[x].meals;
        menu.meals = [];

        for (let i in meals) {
            menu.meals[i] = {};
            menu.meals[i].mealId = meals[i].mealId;
            menu.meals[i].mealName = meals[i].mealName;
            menu.meals[i].mealTime = meals[i].mealHourTime;
            menu.meals[i].consumed = meals[i].consumed;

            menu.meals[i].recipe = {};
            menu.meals[i].recipe.recipe_id = meals[i].recipe.recipeId;
            menu.meals[i].recipe.recipe_name = meals[i].recipe.recipeName;
            menu.meals[i].recipe.creation_date = meals[i].recipe.creationDate;
            menu.meals[i].recipe.recipe_author_id = meals[i].recipe.recipeAuthorId;
            menu.meals[i].recipe.recipe_author = meals[i].recipe.recipeAuthor;
            menu.meals[i].recipe.recipe_author_image = meals[i].recipe.recipeAuthorImage;
            menu.meals[i].recipe.recipe_image = meals[i].recipe.recipeImage;
            menu.meals[i].recipe.recipe_calories = meals[i].recipe.recipeCalories;
            menu.meals[i].recipe.recipe_proteins = meals[i].recipe.recipeProteins;
            menu.meals[i].recipe.recipe_carbohydrates = meals[i].recipe.recipeCarbohydrates;
            menu.meals[i].recipe.recipe_fats = meals[i].recipe.recipeFats;
            menu.meals[i].recipe.in_collection = meals[i].recipe.inCollection;
            menu.meals[i].recipe.liked_in_preference = meals[i].recipe.likedInPreference;

            let ingredients_quantity = meals[i].recipe.recipeIngredients.length;
            menu.meals[i].recipe.recipe_ingredients = [];

            for (let j = 0; j < ingredients_quantity; j++) {
                menu.meals[i].recipe.recipe_ingredients[j] = {};
                menu.meals[i].recipe.recipe_ingredients[j].ingredient_name = meals[i].recipe.recipeIngredients[j].product.productName;
                menu.meals[i].recipe.recipe_ingredients[j].ingredient_amount = Number(meals[i].recipe.recipeIngredients[j].productAmount);
                menu.meals[i].recipe.recipe_ingredients[j].ingredient_unit = meals[i].recipe.recipeIngredients[j].productUnit;
            }

            let steps_quantity = meals[i].recipe.recipeSteps.length;
            menu.meals[i].recipe.recipe_steps = [];

            for (let j = 0; j < steps_quantity; j++) {
                menu.meals[i].recipe.recipe_steps[j] = {};
                menu.meals[i].recipe.recipe_steps[j].step_name = meals[i].recipe.recipeSteps[j].recipeStepDescription;
            }
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

    const handleOpenRecipe = (event, recipe_id) => {
        history.push("/menus/" + recipe_id);
    };

    const handleCloseViewerModal = () => {
        history.push("/menus");
    };

    const handlePrevRecipe = () => {

        let new_index = state.recipe_index;
        let index = new_index - 1;

        if (index < 0) {
            index = state.menuRecipes.length - 1;
        }

        new_index = index;

        history.push('/menus/' + state.menuRecipes[new_index].recipe_id);

    };

    const handleNextRecipe = () => {

        let new_index = state.recipe_index;
        let index = new_index + 1;

        if (index > state.menuRecipes.length - 1) {
            index = 0;
        }

        new_index = index;

        history.push('/menus/' + state.menuRecipes[new_index].recipe_id);

    };

    const handleMarkConsumed = async (event, index, mark) => {

        if (mark !== state.menus[state.currentDietaryProgrammeDay].meals[index].consumed) {
            let meal = {...state.menus[state.currentDietaryProgrammeDay].meals[index], consumed: mark};

            let meals = [...state.menus[state.currentDietaryProgrammeDay].meals];
            const oldMealIndex = meals.findIndex(m => m.mealId === meal.mealId);
            meals.splice(oldMealIndex, 1, meal);

            let menu = {...state.menus[state.currentDietaryProgrammeDay], meals};

            let menus = [...state.menus];
            const oldMenuIndex = menus.findIndex(m => m.dailyMenuId === menu.dailyMenuId);

            menus.splice(oldMenuIndex, 1, menu);

            try {
                const stringMark = mark ? "mark" : "unmark";

                let response = await http.get("/api/menus/markMealAsConsumed/" + state.menus[state.currentDietaryProgrammeDay].meals[index].mealId + "/" + stringMark);

                setState({
                    ...state,
                    menus: menus,
                    msg: response.data.message
                });

                setTimeout(()=>{
                    setState({
                        ...state,
                        menus: menus,
                        msg: ""
                    });
                }, 3000);

            } catch (e) {
                setState({
                    ...state,
                    msg: "Error during marking meal as consumed"
                });
                console.error(e)
            }

        }
    };

    return (
        <Container id="main_container" maxWidth="lg">
            <div className={state.openViewerModal ? "background_blur page_container" : "page_container"}>
                <div>
                    <h2>Daily Menus</h2>
                </div>
                {state.msg !== "" ? <div className="msg">{state.msg}</div> : null}
                {props.currentDietaryProgramme ?
                    <div className="dailyMenu">
                        <Fab aria-label="prev">
                            <NavigateBeforeRoundedIcon fontSize="large" onClick={handlePrevDailyMenu}/>
                        </Fab>
                        <div className="dailyMenuContent">
                            <div className={classes.centralContainer}>
                                <div>
                                    <h3>{state.menus[state.currentDietaryProgrammeDay] ? new Date(state.menus[state.currentDietaryProgrammeDay].dailyMenuDate).toLocaleDateString() + " (" + state.menus[state.currentDietaryProgrammeDay].dailyMenuName + ")" : ""}</h3>
                                </div>
                                <Timeline position="alternate" className="menuList">
                                    {state.menus[state.currentDietaryProgrammeDay] && state.menus[state.currentDietaryProgrammeDay].meals.map((meal, index) => (
                                        <TimelineItem key={index} className={classes.item}>
                                            <TimelineOppositeContent
                                                className={classes.meal_hour}
                                            >
                                                {new Date(meal.mealTime).toLocaleTimeString()}
                                            </TimelineOppositeContent>
                                            <TimelineSeparator>
                                                <TimelineConnector/>
                                                <TimelineDot>
                                                </TimelineDot>
                                                <TimelineConnector/>
                                            </TimelineSeparator>
                                            <TimelineContent>
                                                <div className={classes.meal_description}>
                                                    {new Date(meal.mealTime).setHours(new Date(meal.mealTime).getHours() - 2) < new Date() ? <div className={classes.past_meal}>
                                                        <div className={classes.viewRecipe} onClick={event => handleOpenRecipe(event, meal.recipe.recipe_id)}></div>
                                                        {/*<Button className={classes.button} variant="contained" color="primary" type="button">Save</Button>*/}
                                                        <div className={classes.consumedButtons}>
                                                            <Tooltip title="Consumed" aria-label="Consumed">
                                                                <IconButton aria-label="Consumed" className={meal.consumed ? "nonMarkedConsumeButton markedConsumeButton" : "nonMarkedConsumeButton"} onClick={event => handleMarkConsumed(event, index, true)}>
                                                                    <CheckIcon fontSize="large"/>
                                                                </IconButton>
                                                            </Tooltip>
                                                            <Tooltip title="Not consumed" aria-label="Not consumed">
                                                                <IconButton aria-label="Not consumed" className={!meal.consumed ? "nonMarkedConsumeButton markedConsumeButton" : "nonMarkedConsumeButton"} onClick={event => handleMarkConsumed(event, index, false)}>
                                                                    <ClearIcon fontSize="large"/>
                                                                </IconButton>
                                                            </Tooltip>

                                                        </div>
                                                    </div> : null}
                                                    <Typography variant="h6" component="div" className={classes.center}>
                                                        {meal.mealName}
                                                    </Typography>
                                                    <Typography>
                                                        <div className={classes.recipe_description}>
                                                            <div className={classes.recipe_row}>
                                                                {meal.recipe.recipe_image !== '' ?
                                                                    <Avatar className="menuImage"
                                                                            src={meal.recipe.recipe_image}
                                                                            onClick={event => handleOpenRecipe(event, meal.recipe.recipe_id)}/>
                                                                    : <ReceiptIcon className={classes.photo_placeholder}
                                                                                   onClick={event => handleOpenRecipe(event, meal.recipe.recipe_id)}/>}
                                                                <div className={classes.recipe_name}
                                                                     onClick={event => handleOpenRecipe(event, meal.recipe.recipe_id)}>
                                                                    {meal.recipe.recipe_name}
                                                                </div>
                                                            </div>
                                                            <div className={classes.recipe_row}>
                                                                {meal.recipe.recipe_calories} kCal
                                                                <div className={classes.recipe_nutrients}>
                                                                    <div>
                                                                        Proteins: {meal.recipe.recipe_proteins} g
                                                                    </div>
                                                                    <div className={classes.nutrient}>
                                                                        Carbohydrates: {meal.recipe.recipe_carbohydrates} g
                                                                    </div>
                                                                    <div className={classes.nutrient}>
                                                                        Fats: {meal.recipe.recipe_fats} g
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div className={classes.recipe_row}>
                                                                <div>
                                                                    belongs to collection:
                                                                    <span>{meal.recipe.in_collection}</span>
                                                                </div>
                                                                <div className={classes.recipe_name}>
                                                                    liked in preference:
                                                                    <span>{meal.recipe.liked_in_preference}</span>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </Typography>
                                                    </div>
                                            </TimelineContent>
                                        </TimelineItem>
                                    ))}
                                </Timeline>
                            </div>
                        </div>
                        <Fab className="next_button" aria-label="next">
                            <NavigateNextRoundedIcon fontSize="large" onClick={handleNextDailyMenu}/>
                        </Fab>
                    </div> :
                    <div className="menuNotUsedProgramme">No dietary programme is used</div>}
                {/*{state.menus.length === 0 && !state.loaded ?*/}
                {/*<div className="loading">{"Loading"}</div> : null}*/}
                {recipeId && state.recipe_index !== 'new' && state.menuRecipes.length > 0 && <ViewerModal
                    open={state.openViewerModal}
                    onClose={handleCloseViewerModal}
                    type="menu"
                    item_index={state.recipe_index}
                    items={state.menuRecipes}
                    mode="view"
                    loggedInStatus={props.loggedInStatus}
                    handlePrevProduct={handlePrevRecipe}
                    handleNextProduct={handleNextRecipe}
                    header={state.menus[state.currentDietaryProgrammeDay].dailyMenuName}
                    name={props.name}
                    surname={props.surname}
                    userId={props.userId}
                />}
            </div>
        </Container>
    );
}
