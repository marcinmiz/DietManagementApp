import React, {useEffect} from 'react'
import {Container, makeStyles, Tooltip} from "@material-ui/core";
import AddIcon from '@material-ui/icons/Add';
import http from "../http-common";
import ThumbDownRoundedIcon from '@material-ui/icons/ThumbDownRounded';
import ThumbUpRoundedIcon from '@material-ui/icons/ThumbUpRounded';
import IconButton from "@material-ui/core/IconButton";
import DeleteIcon from '@material-ui/icons/Delete';
import Accordion from '@material-ui/core/Accordion';
import AccordionSummary from '@material-ui/core/AccordionSummary';
import AccordionDetails from '@material-ui/core/AccordionDetails';
import Chip from "@material-ui/core/Chip";
import Button from "@material-ui/core/Button";
import EditIcon from '@material-ui/icons/Edit';
import CloseIcon from "@material-ui/core/SvgIcon/SvgIcon";
import Snackbar from "@material-ui/core/Snackbar/Snackbar";
import DietaryProgrammeEdit from "../components/DietaryProgrammeEdit";

const useStyles = makeStyles((theme) => ({
    formControl: {
        minWidth: 110,
    },
    add_programme_container: {
        display: "block",
        width: "100%"
    }
}));

export default function Programmes(props) {
    const classes = useStyles();

    const [state, setState] = React.useState({
        programmes: [],
        preferences: [],
        programmeName: "",
        programmeDays: 1,
        chosenPreferenceId: null,
        msg: "",
        loaded: false
    });

    const [open, setOpen] = React.useState(false);

    const handleOpen = () => {
        setOpen(true);
    };

    const handleClose = async (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        await setState({
            ...state,
            msg: ""
        });
        setOpen(false);
    };

    const action = (
        <React.Fragment>
            <IconButton
                size="small"
                aria-label="close"
                color="inherit"
                onClick={handleClose}
            >
                <CloseIcon fontSize="small"/>
            </IconButton>
        </React.Fragment>
    );

    useEffect(
        async () => {
            let programmes = [];
            let preferences = [];

            try {
                let response = await http.get("/api/programmes");
                for (let x in response.data) {
                    programmes[x] = createProgramme(response.data, x);
                }

                let response2 = await http.get("/api/preferences");

                for (let x in response2.data) {
                    preferences[x] = createPreference(response2.data, x);
                }

                setState({
                    ...state,
                    programmes: programmes,
                    preferences: preferences,
                    loaded: true,
                });

            } catch (e) {
                console.error(e);
            }

        }, [state.msg, props.adminMode]
    );

    const createProgramme = (data, x) => {
        let programme = {};

        programme.dietaryProgrammeId = data[x].dietaryProgrammeId;
        programme.dietaryProgrammeName = data[x].dietaryProgrammeName;
        programme.dietaryProgrammeDays = data[x].dietaryProgrammeDays;

        return programme;
    };

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

    const handleEdit = (event, preferenceId) => {
        if (!state.chosenPreferenceId) {
            setState({
                ...state,
                chosenPreferenceId: preferenceId
            });
        } else {
            setState({
                ...state,
                chosenPreferenceId: null
            });
        }
    };

    const handleSetMsg = (newMsg) => {
        setState({
            ...state,
            msg: newMsg
        });

        handleOpen();
    };

    const handleRemove = (event, programmeId) => {
        http.delete("/api/programmes/remove/" + programmeId)
            .then(resp => {
                handleSetMsg(resp.data.message);
            })
            .catch(error => {
                handleSetMsg("Programme with id " + programmeId + " has not been removed");
                console.log(error);
            });
    };

    const handleUsingDietaryProgramme = (event, index) => {

        let object;
        if (props.currentDietaryProgramme == null || props.currentDietaryProgramme.dietaryProgrammeId !== state.programmes[index].dietaryProgrammeId) {
            object = "start";
        } else {
            object = "abandon";
        }

        http.put("/api/programmes/" + object + "/" + state.programmes[index].dietaryProgrammeId)
            .then(resp => {

                handleSetMsg(resp.data.message);

                if (object === "start") {
                    props.handleUseDietaryProgramme(state.programmes[index]);
                } else {
                    props.handleUseDietaryProgramme(null);
                }
            })
            .catch(error => console.log(error));
    };

    return (
        <Container id="main_container" maxWidth="lg">
            <div className="page_container">
                <h2>Dietary Programmes</h2>
                {state.msg !== "" ? <Snackbar
                    anchorOrigin={{vertical: 'top', horizontal: 'center'}}
                    open={open}
                    autoHideDuration={5000}
                    onClose={handleClose}
                    message={state.msg}
                    action={action}
                /> : null}
                {/*{state.msg !== "" ? <div className="msg">{state.msg}</div> : null}*/}
                <div className="toolbar_container">
                    <Accordion className="add_button add_preference">
                        <AccordionSummary
                            expandIcon={<AddIcon/>}
                            aria-controls="panel1a-content"
                            id="panel1a-header"
                        >
                            Add new dietary programme
                        </AccordionSummary>
                        <AccordionDetails>
                            <DietaryProgrammeEdit mode='add' handleSetMsg={handleSetMsg}/>
                        </AccordionDetails>
                    </Accordion>
                </div>
                {state.programmes.length === 0 ?
                    <div className="loading">{!state.loaded ? "Loading" : "No dietary programmes found"}</div> : null}
                <div className="dietary_programmes_list">

                    {state.programmes.map((programme, index) => (
                        <div key={index}>
                            <div id={"programme" + programme.dietaryProgrammeId} className="programme">
                                <div className="programme_header">
                                    <div className="programme_header_part">
                                        <div>
                                            <div className="programme_name">
                                                {programme.dietaryProgrammeName}
                                            </div>
                                            <div className="programme_ordinal">
                                                {index + 1}. Dietary Programme
                                            </div>
                                        </div>
                                    </div>
                                    <div className="programme_header_part">
                                        <Chip
                                            name="dietaryProgrammeDays"
                                            size="small"
                                            color="primary"
                                            label={programme.dietaryProgrammeDays + " days"}
                                        />
                                    </div>
                                    <div className="product_buttons programme_header_part">
                                        <Tooltip title="Edit" aria-label="edit">
                                            <IconButton aria-label="edit" className="product_icon_button"
                                                        onClick={event => handleEdit(event, programme.dietaryProgrammeId)}
                                            >
                                                <EditIcon fontSize="small"/>
                                            </IconButton>
                                        </Tooltip>
                                        <Tooltip title="Delete" aria-label="delete">
                                            <IconButton aria-label="delete" className="product_icon_button"
                                                        onClick={event => handleRemove(event, programme.dietaryProgrammeId)}
                                            >
                                                <DeleteIcon fontSize="small"/>
                                            </IconButton>
                                        </Tooltip>
                                        {state.chosenPreferenceId !== programme.dietaryProgrammeId ?
                                            (props.currentDietaryProgramme == null || props.currentDietaryProgramme.dietaryProgrammeId !== programme.dietaryProgrammeId ?
                                                <Tooltip title="Start dietary programme"
                                                         aria-label="Start dietary programme"><Button
                                                    variant="contained"
                                                    color="primary"
                                                    size="medium"
                                                    onClick={event => handleUsingDietaryProgramme(event, index)}>Start</Button></Tooltip> :
                                                <Tooltip title="Abandon dietary programme"
                                                         aria-label="Abandon dietary programme"><Button
                                                    variant="contained"
                                                    className="shared_button"
                                                    size="medium"
                                                    onClick={event => handleUsingDietaryProgramme(event, index)}>Abandon</Button></Tooltip>) : null}
                                    </div>

                                </div>
                                {state.chosenPreferenceId === programme.dietaryProgrammeId ?
                                    <DietaryProgrammeEdit mode='edit' item={programme} handleEdit={handleEdit}
                                                          handleSetMsg={handleSetMsg}/>
                                    :
                                    <div>
                                        <div>Based on</div>
                                        <div className="programme_basic">
                                            {state.preferences.filter(preference => preference.relatedDietaryProgramme.dietaryProgrammeId === programme.dietaryProgrammeId).map((preference, index) => (
                                                <div key={index} id={"preference" + preference.preferenceId}
                                                     className="dietary_preference">
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
                                                </div>
                                            ))}
                                        </div>
                                    </div>}
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </Container>
    );
}