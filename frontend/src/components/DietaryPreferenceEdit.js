import React, {useEffect} from 'react'
import FormControlLabel from "@material-ui/core/FormControlLabel/FormControlLabel";
import Switch from "@material-ui/core/Switch/Switch";
import FormControl from "@material-ui/core/FormControl";
import InputLabel from "@material-ui/core/InputLabel/InputLabel";
import Select from "@material-ui/core/Select/Select";
import {makeStyles, MenuItem, Tooltip} from "@material-ui/core";
import http from "../http-common";
import TextField from "@material-ui/core/TextField/TextField";
import InputAdornment from "@material-ui/core/InputAdornment";
import IconButton from "@material-ui/core/IconButton";
import DeleteIcon from '@material-ui/icons/Delete';
import AddIcon from '@material-ui/icons/Add';
import ThumbDownRoundedIcon from '@material-ui/icons/ThumbDownRounded';
import ThumbUpRoundedIcon from '@material-ui/icons/ThumbUpRounded';
import Button from "@material-ui/core/Button";

const useStyles = makeStyles((theme) => ({
    formControl: {
        minWidth: 110,
    },
    nutrient_part: {
        margin: 5,
    }
}));

export default function DietaryPreferenceEdit(props) {
    const {mode, item, handleEdit, handleSetMsg, ...other} = props;

    const classes = useStyles();

    const [state, setState] = React.useState({
        dietTypes: [],
        allProducts: [],
        allRecipes: [],
        dietTypeSelected: true, //false - customized diet type, true - defined diet type
        preference: {
            preferenceId: 'new',
            preferenceDietType: {
                dietTypeId: 0,
                dietTypeName: '',
                proteinCoefficient: 1,
                carbohydrateCoefficient: 1,
                fatCoefficient: 1
            },
            totalDailyCalories: 2394,
            caloriesPerMeal: 1,
            mealsQuantity: 4,
            targetWeight: 45,
            preferenceNutrients: [
                {nutrientName: "Protein", nutrientAmount: 108, nutrientRelation: "<"},
                {nutrientName: "Carbohydrate", nutrientAmount: 288, nutrientRelation: "<"},
                {nutrientName: "Fat", nutrientAmount: 90, nutrientRelation: "<"}
            ],
            preferenceProducts: [],
            preferenceRecipes: []
        },
        preferenceAddProductName: '',
        preferenceAddRecipeName: '',
        addPreferredProduct: false,
        addPreferredRecipe: false
    });

    useEffect(
        async () => {
            try {
                let preference, products = [], recipes = [];

                setState({
                    ...state,
                    loaded: false
                });

                let dietTypesTable = [];

                let dietTypesResponse = await http.get("/api/dietTypes");

                for (let x in dietTypesResponse.data) {
                    dietTypesTable[x] = {};
                    dietTypesTable[x].dietTypeId = dietTypesResponse.data[x].dietTypeId;
                    dietTypesTable[x].dietTypeName = dietTypesResponse.data[x].dietTypeName;
                    dietTypesTable[x].proteinCoefficient = dietTypesResponse.data[x].proteinCoefficient;
                    dietTypesTable[x].carbohydrateCoefficient = dietTypesResponse.data[x].carbohydrateCoefficient;
                    dietTypesTable[x].fatCoefficient = dietTypesResponse.data[x].fatCoefficient;
                }

                let products_parameters = {};
                products_parameters.productsGroup = "all";
                products_parameters.phrase = "";
                products_parameters.category = "";

                let resp = await http.post("/api/products", products_parameters);

                for (let x in resp.data) {
                    products[x] = resp.data[x].productName;
                }

                let recipes_parameters = {};
                recipes_parameters.all = "yes";
                recipes_parameters.groupNumber = 0;
                recipes_parameters.recipesGroup = "personal";
                recipes_parameters.phrase = "";

                let resp2 = await http.post("/api/recipes", recipes_parameters);

                for (let x in resp2.data) {
                    recipes[x] = resp2.data[x].recipeName;
                }

                if (mode === 'edit') {
                    state.dietTypeSelected = item.dietTypeSelected;
                    preference = item;
                } else {
                    preference = state.preference;
                }

                console.log(item);

                if (mode === 'add') {
                    preference.preferenceDietType = dietTypesTable[0];
                }

                console.log(!(mode === 'edit' && preference.preferenceDietType.dietTypeName === ''));

                if (!(mode === 'edit' && preference.preferenceDietType.dietTypeName === '')) {
                    preference = await handleNutrientsComputing(preference);
                }

                await setState({
                    ...state,
                    preferenceAddProductName: products > 0 ? products[0] : '',
                    preferenceAddRecipeName: recipes.length > 0 ? recipes[0] : '',
                    allProducts: products,
                    allRecipes: recipes,
                    dietTypes: dietTypesTable,
                    preference: preference,
                    loaded: true
                });

            } catch (error) {
                console.error(error);
            }

        }, []);

    const handleChangeBasic = (event) => {

        let name = event.target.name;
        let value, valid = true;

        if (name !== 'preferenceDietType') {
            value = Number(event.target.value);
            if (name === 'targetWeight') {
                if (value < 45 || value > 120)
                    valid = false;
            } else {
                if (value < 1)
                    valid = false;
            }
        } else {
            value = event.target.value;
        }

        if (valid) {
            let preference;
            if (name === 'preferenceDietType') {
                const dietType = state.dietTypes.filter(type => type.dietTypeName === value)[0];

                preference = {
                    ...state.preference,
                    preferenceDietType: dietType
                };

            } else {
                preference = {
                    ...state.preference,
                    [name]: Number(value)
                };
            }

            if (name === 'preferenceDietType' || (name === 'targetWeight' && state.dietTypeSelected)) {
                preference = handleNutrientsComputing(preference, "all");
            }

            setState({
                ...state,
                preference: preference
            });
        }
    };

    const handleChangeNutrient = (event, index) => {
        let valid = true;
        const values = [...state.preference.preferenceNutrients];
        if (event.target.value > 0) {
            values[index].nutrientAmount = Number(event.target.value);
        } else {
            valid = false;
        }

        if (valid) {
            let preference = {
                ...state.preference,
                "preferenceNutrients": values
            };

            if (!state.dietTypeSelected) {
                preference = handleNutrientsComputing(preference, "calories");
            }

            setState({
                ...state,
                "preference": preference
            });
        }
    };

    const handleChangePreferredItem = (event) => {

        setState({
            ...state,
            [event.target.name]: event.target.value
        });
    };

    const handleAddPreferredItem = (itemType) => {

        let item;
        if (itemType === 'product') {
            item = {
                ...state.preference,
                "preferenceProducts": [...state.preference.preferenceProducts, {
                    productName: state.preferenceAddProductName,
                    productPreferred: state.addPreferredProduct,
                }]
            };
        } else if (itemType === 'recipe') {
            item = {
                ...state.preference,
                "preferenceRecipes": [...state.preference.preferenceRecipes, {
                    recipeName: state.preferenceAddRecipeName,
                    recipePreferred: state.addPreferredRecipe,
                }]
            };
        } else {
            console.error("Wrong item type");
        }

        setState({
            ...state,
            "preference": item
        });
    };

    const handleDeletePreferredItem = (index, itemType) => {

        let values, item;
        if (itemType === 'product') {
            values = [...state.preference.preferenceProducts];
            values.splice(index, 1);
            item = {
                ...state.preference,
                "preferenceProducts": values
            };
        } else if (itemType === 'recipe') {
            values = [...state.preference.preferenceRecipes];
            values.splice(index, 1);
            item = {
                ...state.preference,
                "preferenceRecipes": values
            };
        } else {
            console.error("Wrong item type");
        }

        setState({
            ...state,
            "preference": item
        });
    };

    const handleNutrientsComputing = (preference, type) => {
        let calories, targetWeight, proteins, carbohydrates, fats;
        targetWeight = preference.targetWeight;

        if (type != null) {

            switch (type) {
                case "calories":
                    proteins = preference.preferenceNutrients[0].nutrientAmount;
                    carbohydrates = preference.preferenceNutrients[1].nutrientAmount;
                    fats = preference.preferenceNutrients[2].nutrientAmount;
                    calories = 4 * proteins + 4 * carbohydrates + 9 * fats;
                    break;
                case "all":
                    proteins = targetWeight * preference.preferenceDietType.proteinCoefficient;
                    let averageProteins = Math.round(proteins * 100);
                    proteins = (averageProteins) / 100;
                    carbohydrates = targetWeight * preference.preferenceDietType.carbohydrateCoefficient;
                    let averageCarbohydrates = Math.round(carbohydrates * 100);
                    carbohydrates = (averageCarbohydrates) / 100;
                    fats = targetWeight * preference.preferenceDietType.fatCoefficient;
                    let averageFats = Math.round(fats * 100);
                    fats = (averageFats) / 100;
                    calories = 4 * proteins + 4 * carbohydrates + 9 * fats;
                    break;
                default:
            }
        }

        calories = Math.round(calories);

        let nutrients = [
            {
                nutrientName: "Protein",
                nutrientAmount: type === "all" ? proteins : preference.preferenceNutrients[0].nutrientAmount,
                nutrientRelation: "="
            },
            {
                nutrientName: "Carbohydrate",
                nutrientAmount: type === "all" ? carbohydrates : preference.preferenceNutrients[1].nutrientAmount,
                nutrientRelation: "="
            },
            {
                nutrientName: "Fat",
                nutrientAmount: type === "all" ? fats : preference.preferenceNutrients[2].nutrientAmount,
                nutrientRelation: "="
            }
        ];

        return {
            ...preference,
            targetWeight: targetWeight,
            totalDailyCalories: type === "all" || type === "calories" ? calories : preference.totalDailyCalories,
            preferenceNutrients: nutrients
        };
    };

    const validatePreference = () => {
        let preference = {};
        preference.dietTypeSelected = state.dietTypeSelected;
        preference.totalDailyCalories = state.preference.totalDailyCalories;
        preference.dietType = state.preference.preferenceDietType.dietTypeName;
        preference.mealsQuantity = state.preference.mealsQuantity;
        preference.targetWeight = state.preference.targetWeight;
        preference.nutrients = [];
        for (let i in state.preference.preferenceNutrients) {
            preference.nutrients[i] = state.preference.preferenceNutrients[i].nutrientName + ";";
            preference.nutrients[i] += state.preference.preferenceNutrients[i].nutrientAmount + ";";
            preference.nutrients[i] += state.preference.preferenceNutrients[i].nutrientRelation;
        }
        preference.products = [];
        for (let i in state.preference.preferenceProducts) {
            preference.products[i] = state.preference.preferenceProducts[i].productName + ";";
            preference.products[i] += state.preference.preferenceProducts[i].productPreferred;
        }
        preference.recipes = [];
        for (let i in state.preference.preferenceRecipes) {
            preference.recipes[i] = state.preference.preferenceRecipes[i].recipeName + ";";
            preference.recipes[i] += state.preference.preferenceRecipes[i].recipePreferred;
        }
        return preference;
    };

    const handleSave = async () => {

        let preference, item_id;

        preference = await validatePreference();

        item_id = state.preference.preferenceId;

        if (preference === "error")
            return;

        console.log(preference);

        if (mode === "add") {
            http.post("/api/preferences/add", preference)
                .then(resp => {
                    // item_id = Number(resp.data.message.split(" ")[0]);
                    handleSetMsg(resp.data.message);
                    setTimeout(() => handleSetMsg(""), 3000);
                })
                .catch(error => console.log(error));
        } else if (mode === "edit") {
            http.put("/api/preferences/update/" + item_id, preference)
                .then(resp => {
                    handleSetMsg(resp.data.message);
                    setTimeout(() => handleSetMsg(""), 3000);
                })
                .catch(error => console.log(error));
        } else {
            console.error("wrong mode");
        }

    };

    return (
        <div className="preference_edit">
            <div>
                <FormControl variant="filled" className="preferenceDietType">
                    <InputLabel id="diet_type_select_label" className="diet_type_select preferenceDietTypePart">Diet
                        type</InputLabel>
                    <Select
                        labelId="diet_type_select_label"
                        id="preferenceDietType"
                        className="diet_type_select preferenceDietTypePart"
                        name="preferenceDietType"
                        value={state.preference.preferenceDietType.dietTypeName}
                        disabled={!state.dietTypeSelected}
                        onChange={event => handleChangeBasic(event)}
                    >
                        {state.dietTypes.map((dietType, index) => (
                            <MenuItem key={index}
                                      value={dietType.dietTypeName}>{dietType.dietTypeName}</MenuItem>
                        ))}
                    </Select>
                </FormControl>
                <FormControlLabel
                    control={
                        <Switch
                            checked={state.dietTypeSelected}
                            onChange={() => {
                                let newType = !state.dietTypeSelected, newPreference;
                                // if (!newType)
                                //     newPreference = handleNutrientsComputing(state.preference);
                                let preference = {...state.preference};
                                if (newType) {
                                    preference.preferenceDietType = state.dietTypes[0];
                                } else {
                                    preference.preferenceDietType.dietTypeName = '';
                                }
                                setState({
                                    ...state,
                                    preference: preference,
                                    dietTypeSelected: newType
                                })

                            }}
                            name="dietTypeSelected"
                            color="primary"
                        />
                    }
                    label={state.dietTypeSelected ? "Defined" : "Customized"}
                    labelPlacement="top"
                />
            </div>
            <div className="preferenceLayout">
                <div className="preferenceDetailsEdit">
                    <FormControl variant="filled" className="preference_input">
                        <InputLabel id="diet_type_select_label" className="diet_type_select preferenceDietTypePart">Meals
                            quantity</InputLabel>
                        <Select
                            labelId="diet_type_select_label"
                            id="mealsQuantity"
                            className="diet_type_select preferenceDietTypePart preference_input"
                            name="mealsQuantity"
                            value={state.preference.mealsQuantity}
                            onChange={event => handleChangeBasic(event)}
                        >
                            <MenuItem className={classes.mealsQuantitySelectItem} key="meals4" value={4}>4</MenuItem>
                            <MenuItem key="meals5" value={5}>5</MenuItem>
                        </Select>
                    </FormControl>
                    <TextField name="totalDailyCalories" label="Total daily calories" variant="filled" type="number"
                               className="preference_input"
                               value={state.preference.totalDailyCalories}
                               autoComplete="off" disabled={true}
                               InputProps={{
                                   endAdornment: <InputAdornment
                                       position="end">
                                       <div>kcal</div>
                                   </InputAdornment>,
                               }}
                               onChange={event => handleChangeBasic(event)}/>
                    <TextField name="targetWeight" label="Target weight" variant="filled" type="number"
                               className="preference_input"
                               value={state.preference.targetWeight}
                               autoComplete="off"
                               InputProps={{
                                   endAdornment: <InputAdornment
                                       position="end">
                                       <div>kg</div>
                                   </InputAdornment>,
                               }} onChange={event => handleChangeBasic(event)}/>
                </div>
                <div className="nutrients">
                    <div id="nutrients_list">
                        <h4>Preferred nutrients</h4>
                        {state.preference.preferenceNutrients.length === 0 ? "No preference nutrients added. Add new preference nutrient" :
                            state.preference.preferenceNutrients.map((preferenceNutrient, index) => (
                                <div key={index} className="preferenceNutrient_edit preference_input">
                                    <TextField
                                        name={preferenceNutrient.nutrientName}
                                        label={preferenceNutrient.nutrientName}
                                        type="number"
                                        variant="filled"
                                        value={preferenceNutrient.nutrientAmount}
                                        size="small"
                                        disabled={state.dietTypeSelected}
                                        InputProps={{
                                            endAdornment: <InputAdornment
                                                position="end">
                                                <div>g</div>
                                            </InputAdornment>,
                                        }}
                                        onChange={event => handleChangeNutrient(event, index)}
                                    />
                                </div>
                            ))}
                    </div>
                </div>
                <div className="preferredProducts">
                    <div id="preferredProducts_list">
                        <h4>Preferred products</h4>
                        {state.preference.preferenceProducts.length === 0 ? "No preferred products added" :
                            state.preference.preferenceProducts.map((preferenceProduct, index) => (
                                <div key={index} className="preferenceProduct_edit">
                                    <div className={classes.nutrient_part}>
                                        {preferenceProduct.productName}
                                    </div>
                                    <div className={classes.nutrient_part}>
                                        {preferenceProduct.productPreferred ?
                                            <ThumbUpRoundedIcon className="upThumb"/> :
                                            <ThumbDownRoundedIcon className="downThumb"/>}
                                    </div>
                                    <Tooltip title="Delete product" aria-label="delete product"
                                             className={classes.nutrient_part}>
                                        <IconButton aria-label="delete product"
                                                    className="preferenceProduct_button"
                                                    onClick={() => handleDeletePreferredItem(index, "product")}
                                        >
                                            <DeleteIcon fontSize="small"/>
                                        </IconButton>
                                    </Tooltip>
                                </div>
                            ))}
                    </div>
                    <div className="add_preferred_product">
                        <div className={classes.nutrient_part}>
                            <div>Product</div>
                            <Select
                                id="preferred_product_select"
                                name="preferenceAddProductName"
                                label="Product"
                                value={state.preferenceAddProductName}
                                size="small"
                                className={classes.formControl}
                                onChange={event => handleChangePreferredItem(event)}

                            >
                                {state.allProducts.map((product, index) => (
                                    <MenuItem key={index}
                                              value={product}>{product}</MenuItem>
                                ))}
                            </Select>
                        </div>
                        <div className={classes.nutrient_part}>
                            <div>Preferred</div>
                            <ThumbDownRoundedIcon name="addNotPreferred" value={false}
                                                  className={!state.addPreferredProduct ? "downThumb" : ""}
                                                  onClick={() => setState({...state, "addPreferredProduct": false})}/>
                            <ThumbUpRoundedIcon name="addPreferred" value={true}
                                                className={state.addPreferredProduct ? "upThumb" : ""}
                                                onClick={() => setState({...state, "addPreferredProduct": true})}/>
                        </div>
                        <Tooltip title="Add preferred product" aria-label="add preferred product">
                            <IconButton name="preferredProductAddButton" aria-label="add preferred product"
                                        className="preferenceProduct_button"
                                        onClick={() => handleAddPreferredItem("product")}
                                        disabled={state.preference.preferenceProducts.map(preferredProduct => preferredProduct.productName).filter(productName => productName === state.preferenceAddProductName).length > 0}
                            >
                                <AddIcon fontSize="large"/>
                            </IconButton>
                        </Tooltip>
                    </div>
                </div>
                <div className="preferredRecipes">
                    <div id="preferredRecipes_list">
                        <h4>Preferred recipes</h4>
                        {state.preference.preferenceRecipes.length === 0 ? "No preferred recipes added" :
                            state.preference.preferenceRecipes.map((preferenceRecipe, index) => (
                                <div key={index} className="preferenceRecipe_edit">
                                    <div className={classes.nutrient_part}>
                                        {preferenceRecipe.recipeName}
                                    </div>
                                    <div className={classes.nutrient_part}>
                                        {preferenceRecipe.recipePreferred ?
                                            <ThumbUpRoundedIcon className="upThumb"/> :
                                            <ThumbDownRoundedIcon className="downThumb"/>}
                                    </div>
                                    <Tooltip title="Delete recipe" aria-label="delete recipe"
                                             className={classes.nutrient_part}>
                                        <IconButton aria-label="delete recipe"
                                                    className="preferenceRecipe_button"
                                                    onClick={() => handleDeletePreferredItem(index, "recipe")}
                                        >
                                            <DeleteIcon fontSize="small"/>
                                        </IconButton>
                                    </Tooltip>
                                </div>
                            ))}
                    </div>
                    <div className="add_preferred_recipe">
                        <div className={classes.nutrient_part}>
                            <div>Recipe</div>
                            <Select
                                id="preferred_recipe_select"
                                name="preferenceAddRecipeName"
                                label="Recipe"
                                value={state.preferenceAddRecipeName}
                                size="small"
                                className={classes.formControl}
                                onChange={event => handleChangePreferredItem(event)}
                            >
                                {state.allRecipes.map((recipe, index) => (
                                    <MenuItem key={index}
                                              value={recipe}>{recipe}</MenuItem>
                                ))};
                            </Select>
                        </div>
                        <div className={classes.nutrient_part}>
                            <div>Preferred</div>
                            <ThumbDownRoundedIcon name="addNotPreferred" value={false}
                                                  className={!state.addPreferredRecipe ? "downThumb" : ""}
                                                  onClick={() => setState({...state, "addPreferredRecipe": false})}/>
                            <ThumbUpRoundedIcon name="addPreferred" value={true}
                                                className={state.addPreferredRecipe ? "upThumb" : ""}
                                                onClick={() => setState({...state, "addPreferredRecipe": true})}/>
                        </div>
                        <Tooltip title="Add preferred recipe" aria-label="add preferred recipe">
                            <IconButton name="preferredRecipeAddButton" aria-label="add preferred recipe"
                                        className="preferenceRecipe_button"
                                        onClick={() => handleAddPreferredItem("recipe")}
                                        disabled={state.preference.preferenceRecipes.map(preferredRecipe => preferredRecipe.recipeName).filter(recipeName => recipeName === state.preferenceAddRecipeName).length > 0}
                            >
                                <AddIcon fontSize="large"/>
                            </IconButton>
                        </Tooltip>
                    </div>
                </div>

            </div>
            <div>
                <Button className={classes.button} variant="contained" color="primary" type="button"
                        onClick={() => handleSave()}
                >
                    Save
                </Button>
            </div>
        </div>
    );
}