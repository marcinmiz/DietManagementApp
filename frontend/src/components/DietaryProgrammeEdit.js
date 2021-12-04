import React, {useEffect} from 'react'
import {makeStyles} from "@material-ui/core";
import http from "../http-common";
import TextField from "@material-ui/core/TextField/TextField";
import ThumbDownRoundedIcon from '@material-ui/icons/ThumbDownRounded';
import ThumbUpRoundedIcon from '@material-ui/icons/ThumbUpRounded';
import Button from "@material-ui/core/Button";

const useStyles = makeStyles((theme) => ({
    formControl: {
        minWidth: 110,
    },
    add_programme_container: {
        display: "block",
        width: "100%"
    },
    none: {
        marginTop: "15%"
    }
}));

export default function DietaryProgrammeEdit(props) {
    const {mode, item, handleEdit, handleSetMsg, ...other} = props;

    const classes = useStyles();

    const [state, setState] = React.useState({
        programme: {
            dietaryProgrammeId: 'new',
            dietaryProgrammeName: "",
            dietaryProgrammeDays: 1,
            chosenPreferenceId: null,
        },
        preferences: []
    });

    useEffect(
        async () => {
            let preferences = [];

            try {
                let response2 = await http.get("/api/preferences");

                for (let x in response2.data) {
                    preferences[x] = createPreference(response2.data, x);
                }

                let programme = mode === "edit" ? item : state.programme;

                if (mode === "edit") {
                    for(let pref of preferences) {
                        if (pref.relatedDietaryProgramme.dietaryProgrammeId === programme.dietaryProgrammeId) {
                            programme.chosenPreferenceId = pref.preferenceId;
                        }
                    }
                } else {
                    programme.chosenPreferenceId = preferences[0].preferenceId;
                }
console.log(programme);
                setState({
                    ...state,
                    programme: programme,
                    preferences: preferences,
                    loaded: true,
                });

            } catch (e) {
                console.error(e);
            }

        }, []);

    const createPreference = (data, x) => {
        let preference = {};

        preference.preferenceId = data[x].dietaryPreferenceId;
        preference.totalDailyCalories = data[x].totalDailyCalories;
        preference.preferenceOwner = data[x].preferenceOwner.name + " " + data[x].preferenceOwner.surname;

        preference.preferenceDietType = {};
        preference.preferenceDietType.dietTypeName = "";

        if (data[x].dietType) {
            preference.preferenceDietType.dietTypeId = data[x].dietType.dietTypeId;
            preference.preferenceDietType.dietTypeName = data[x].dietType.dietTypeName;
            preference.preferenceDietType.proteinCoefficient = data[x].dietType.proteinCoefficient;
            preference.preferenceDietType.carbohydrateCoefficient = data[x].dietType.carbohydrateCoefficient;
            preference.preferenceDietType.fatCoefficient = data[x].dietType.fatCoefficient;
        }

        preference.caloriesPerMeal = data[x].caloriesPerMeal;
        preference.mealsQuantity = data[x].mealsQuantity;
        preference.targetWeight = data[x].targetWeight;
        let creationDate = new Date(data[x].creationDate);
        preference.creationDate = creationDate.toLocaleDateString() + " " + creationDate.toLocaleTimeString();

        let nutrients_quantity = data[x].nutrients.length;
        preference.preferenceNutrients = [];

        for (let i = 0; i < nutrients_quantity; i++) {
            preference.preferenceNutrients[i] = {};
            preference.preferenceNutrients[i].nutrientName = data[x].nutrients[i].nutrient.nutrientName;
            preference.preferenceNutrients[i].nutrientAmount = data[x].nutrients[i].nutrientAmount;
            preference.preferenceNutrients[i].nutrientRelation = data[x].nutrients[i].nutrientRelation;
        }

        let products_quantity = data[x].products.length;
        preference.preferenceProducts = [];

        for (let i = 0; i < products_quantity; i++) {
            preference.preferenceProducts[i] = {};
            preference.preferenceProducts[i].productName = data[x].products[i].product.productName;
            preference.preferenceProducts[i].productImage = data[x].products[i].product.productImage;
            preference.preferenceProducts[i].productPreferred = data[x].products[i].productPreferred;
        }

        let recipes_quantity = data[x].recipes.length;
        preference.preferenceRecipes = [];

        for (let i = 0; i < recipes_quantity; i++) {
            preference.preferenceRecipes[i] = {};
            preference.preferenceRecipes[i].recipeName = data[x].recipes[i].recipe.recipeName;
            preference.preferenceRecipes[i].recipeImage = data[x].recipes[i].recipe.recipeImage;
            preference.preferenceRecipes[i].recipePreferred = data[x].recipes[i].recipePreferred;
        }

        preference.relatedDietaryProgramme = {};
        preference.relatedDietaryProgramme.dietaryProgrammeName = "";

        if (data[x].relatedDietaryProgramme) {
            preference.relatedDietaryProgramme.dietaryProgrammeId = data[x].relatedDietaryProgramme.dietaryProgrammeId;
            preference.relatedDietaryProgramme.dietaryProgrammeName = data[x].relatedDietaryProgramme.dietaryProgrammeName;
            preference.relatedDietaryProgramme.dietaryProgrammeDays = data[x].relatedDietaryProgramme.dietaryProgrammeDays;
        }

        return preference;
    };

    const handleChangeBasicInfo = (event) => {

        let value;

        if (event.target.name === 'dietaryProgrammeDays') {
            if (event.target.value <= 0) {
                return;
            }
            value = Number(event.target.value);
        } else {
            value = event.target.value;
        }

        const programme = {...state.programme, [event.target.name]: value};

        setState({
            ...state,
            programme: programme
        });
    };

    const handlePreferenceChoose = (event, preferenceId) => {
        const programme = {...state.programme, chosenPreferenceId: preferenceId};

        setState({
            ...state,
            programme: programme
        });
    };

    const validateProgramme = () => {
        let programme = {};

        programme.dietaryProgrammeName = state.programme.dietaryProgrammeName;
        programme.dietaryProgrammeDays = state.programme.dietaryProgrammeDays;
        programme.preferenceId = state.programme.chosenPreferenceId;

        return programme;
    };

    const handleSave = async () => {

        let programme, item_id;

        programme = await validateProgramme();

        item_id = state.programme.dietaryProgrammeId;

        if (programme === "error")
            return;

        console.log(programme);

        if (mode === "add") {
            http.post("/api/programmes/add", programme)
                .then(resp => {
                    // item_id = Number(resp.data.message.split(" ")[0]);
                    handleSetMsg(resp.data.message);
                })
                .catch(error => {
                    handleSetMsg("Dietary Programme could not be added");
                    console.log(error)
                });
        } else if (mode === "edit") {
            http.put("/api/programmes/edit/" + item_id, programme)
                .then(resp => {
                    handleSetMsg(resp.data.message);
                })
                .catch(error => {
                    handleSetMsg("Dietary Programme could not be edited");
                    console.log(error)
                });
        } else {
            console.error("wrong mode");
        }

    };
console.log(state.preferences.filter(preference => !preference.relatedDietaryProgramme.dietaryProgrammeId).length >= 1);
    return (
        <div className={classes.add_programme_container}>
            <div className="add_programme_form">
                <div className="programme_basic">
                    <div className="programme_name">
                        <TextField className="category_select" id="programmeNameInput"
                                   name="dietaryProgrammeName"
                                   label="Programme name" variant="filled"
                                   value={state.programme.dietaryProgrammeName}
                                   autoComplete="off"
                                   onChange={event => handleChangeBasicInfo(event)}
                        />
                    </div>
                    <div className="programme_name">
                        <TextField className="category_select" id="programmeDaysInput"
                                   name="dietaryProgrammeDays"
                                   type="number"
                                   label="Programme days" variant="filled"
                                   value={state.programme.dietaryProgrammeDays}
                                   autoComplete="off"
                                   onChange={event => handleChangeBasicInfo(event)}
                        />
                    </div>
                </div>
                <div className="choose_preference_container">
                    <div>Click preference to choose</div>
                    <div className="choose_preference_list">
                        {state.preferences.filter(preference => !preference.relatedDietaryProgramme.dietaryProgrammeId).length >= 1 ?
                            (state.preferences.map((preference, index) => (
                            !preference.relatedDietaryProgramme.dietaryProgrammeId ?
                            <div key={index} id={"preference" + preference.preferenceId}
                                 className={state.programme.chosenPreferenceId === preference.preferenceId ? "dietary_preference chosenPreference" : "dietary_preference"}
                                 onClick={event => handlePreferenceChoose(event, preference.preferenceId)}
                            >
                                <div className="preference_basic_info">
                                    <div className="dietary_preference_diet_type">
                                        {preference.preferenceDietType.dietTypeName !== "" ? preference.preferenceDietType.dietTypeName : "CUSTOMIZED DIET"}
                                    </div>
                                    <div className="dietary_preference_total_daily_calories">
                                        {preference.totalDailyCalories} kcal daily
                                    </div>
                                    <div className="dietary_preference_meals_quantity">
                                        {preference.mealsQuantity} meals
                                    </div>
                                    <div className="dietary_preference_target_weight">
                                        target {preference.targetWeight} kg
                                    </div>
                                </div>
                                <div className="preference_nutrients">
                                    <div className="preference_nutrients_header">Nutrients</div>
                                    {preference.preferenceNutrients.length === 0 ? "No nutrients" :
                                        preference.preferenceNutrients.map((nutrient, index) => (
                                            <div key={index} className="preference_nutrient">
                                                <div>
                                                    {nutrient.nutrientName}s
                                                </div>
                                                <div>
                                                    {nutrient.nutrientAmount} g
                                                </div>
                                            </div>
                                        ))}
                                </div>
                                <div className="preference_products">
                                    <div className="preference_products_header">Products</div>
                                    {preference.preferenceProducts.length === 0 ? "No preferred products" :
                                        preference.preferenceProducts.map((product, index) => (
                                            <div key={index} className="preference_product">
                                                <div>
                                                    {product.productName}
                                                </div>
                                                <div>
                                                    {product.productPreferred ?
                                                        <ThumbUpRoundedIcon className="upThumb"/> :
                                                        <ThumbDownRoundedIcon
                                                            className="downThumb"/>}
                                                </div>
                                            </div>
                                        ))}
                                </div>
                                <div className="preference_recipes">
                                    <div className="preference_recipes_header">Recipes</div>
                                    {preference.preferenceRecipes.length === 0 ? "No preferred recipes" :
                                        preference.preferenceRecipes.map((recipe, index) => (
                                            <div key={index} className="preference_recipe">
                                                <div>
                                                    {recipe.recipeName}
                                                </div>
                                                <div>
                                                    {recipe.recipePreferred ?
                                                        <ThumbUpRoundedIcon className="upThumb"/> :
                                                        <ThumbDownRoundedIcon
                                                            className="downThumb"/>}
                                                </div>
                                            </div>
                                        ))}
                                </div>
                            </div> : null
                        ))) :
                                <div className={classes.none}>None</div>}
                    </div>
                </div>
            </div>
            <Button className={classes.programmeSaveButton} variant="contained" color="primary"
                    type="button"
                    onClick={() => handleSave()}
            >
                Save
            </Button>
        </div>
    );
}