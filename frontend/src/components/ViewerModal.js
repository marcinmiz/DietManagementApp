import React, {useEffect} from 'react';
import Modal from "@material-ui/core/Modal";
import Fade from "@material-ui/core/Fade";
import Backdrop from "@material-ui/core/Backdrop";
import makeStyles from "@material-ui/core/styles/makeStyles";
import Fab from "@material-ui/core/Fab";
import NavigateBeforeRoundedIcon from '@material-ui/icons/NavigateBeforeRounded';
import NavigateNextRoundedIcon from '@material-ui/icons/NavigateNextRounded';
import Chip from "@material-ui/core/Chip";
import Avatar from "@material-ui/core/Avatar";
import {Grid, MenuItem, Tooltip, Divider} from "@material-ui/core";
import IconButton from "@material-ui/core/IconButton";
import EditIcon from '@material-ui/icons/Edit';
import CloseIcon from '@material-ui/icons/Close';
import DeleteIcon from '@material-ui/icons/Delete';
import CategoryIcon from '@material-ui/icons/Category';
import http from "../http-common";
import FormControl from "@material-ui/core/FormControl";
import InputLabel from "@material-ui/core/InputLabel/InputLabel";
import Select from "@material-ui/core/Select/Select";
import TextField from "@material-ui/core/TextField/TextField";
import InputAdornment from "@material-ui/core/InputAdornment";
import UploadImages from "./UploadImages";
import {useHistory} from "react-router-dom";
import Button from "@material-ui/core/Button";
import SpaIcon from '@material-ui/icons/Spa';
import ReceiptIcon from '@material-ui/icons/Receipt';
import ExistedProductsPopup from "./ExistedProductsPopup";
import FavoriteBorderIcon from '@material-ui/icons/FavoriteBorder';
import FavoriteIcon from '@material-ui/icons/Favorite';
import AddIcon from '@material-ui/icons/Add';
import Rating from "@material-ui/lab/Rating/Rating";

const useStyles = makeStyles((theme) => ({
    modal: {
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'stretch',
        color: '#fff'
    },
    header: {
        display: 'flex',
        justifyContent: 'flex-end',
        alignItems: 'center',
        padding: 10
    },
    headerTitle: {
        marginLeft: 'auto',
        marginRight: 'auto',
        textTransform: 'capitalize'
    },
    bottom_item_rep: {
        marginLeft: 5,
        marginRight: 5
    },
    button: {
        margin: 5,
    },
    category: {
        margin: 5
    },
    photo_placeholder: {
        fontSize: 200
    },
    ingredient_part: {
        margin: 5
    },
    center_button_group: {
        textAlign: 'center'
    }
}));

export default function ViewerModal(props) {

    const {type, onClose, open, items, item_index, header, ...other} = props;

    const classes = useStyles();

    const history = useHistory();

    const [state, setState] = React.useState({
        submitted: false,
        image_upload_item_id: -1,
        selected_item: type === "product" ? {
            product_id: 'new',
            product_name: "",
            product_image: "",
            product_category: "",
            product_author: "",
            product_calories: 0,
            product_nutrients: [{nutrient_name: "Protein", nutrient_amount: 0, nutrient_unit: "mg/100g"}, {
                nutrient_name: "Carbohydrates",
                nutrient_amount: 0,
                nutrient_unit: "mg/100g"
            }, {nutrient_name: "Fat", nutrient_amount: 0, nutrient_unit: "mg/100g"}, {
                nutrient_name: "Salt",
                nutrient_amount: 0,
                nutrient_unit: "mg/100g"
            }],
            creation_date: "",
            approval_status: "pending",
            assessment_date: "",
            reject_explanation: ""
        } : {
            recipe_id: 'new',
            recipe_name: "",
            recipe_image: "",
            recipe_author: "",
            recipe_ingredients: [],
            recipe_steps: [],
            recipe_customer_satisfactions: [{
                customer_satisfaction_author: "",
                customer_satisfaction_rating: 0,
                customer_satisfaction_favourite: false
            }],
            recipe_shared: false,
            creation_date: "",
            approval_status: "pending",
            assessment_date: "",
            reject_explanation: ""
        },
        msg: "",
        existed_products: [],
        loaded: false,
        open_existed_products_popup: false,
        all_products: [],
        all_units: [],
        recipe_add_ingredient_name: "",
        recipe_add_ingredient_amount: 0,
        recipe_add_ingredient_unit: "",
        recipe_add_step_name: "",
        hover_rating: -1
    });

    useEffect(
        async () => {

            let products = [];
            let units = [];

            if (type === 'recipe') {

                let products_parameters = {};
                products_parameters.productsGroup = "all";
                products_parameters.phrase = "";
                products_parameters.category = "";

                let resp = await http.post("/api/products", products_parameters);

                for (let x in resp.data) {
                    products[x] = resp.data[x].productName;
                }

                let resp2 = await http.get("/api/recipes/productUnits");

                for (let x in resp2.data) {
                    units[x] = resp2.data[x];
                }
            }

            await setState({
                ...state,
                all_products: products,
                all_units: units,
                recipe_add_ingredient_name: products[0],
                recipe_add_ingredient_amount: 1,
                recipe_add_ingredient_unit: units[0],
                selected_item: item_index !== 'new' ? items[item_index] : state.selected_item,
                loaded: true
            });
        }, [item_index]);

    const handleChangeItemId = (event) => {

        let value;

        if (type === "product" && event.target.name === 'product_calories') {
            if (event.target.value <= 0) {
                return;
            }
            value = Number(event.target.value);
        } else {
            value = event.target.value;
        }
        const values = {
            ...state.selected_item,
            [event.target.name]: value
        };
        setState({
            ...state,
            "selected_item": values,
            "open_existed_products_popup": type === "product" && event.target.name === "product_name"
        });
    };

    const handleChangeNutrient = (index, event) => {
        const values = [...state.selected_item.product_nutrients];
        if (event.target.value > 0) {
            values[index].nutrient_amount = Number(event.target.value);
            let selected_item = {
                ...state.selected_item,
                "product_nutrients": values
            };

            setState({
                ...state,
                "selected_item": selected_item
            });
        }
    };

    const handleChangeIngredient = (event, index) => {
        if (event.target.name === "recipe_add_ingredient_name") {

            setState({
                ...state,
                "recipe_add_ingredient_name": event.target.value
            });
        } else if (event.target.name === "recipe_add_ingredient_amount") {

            if (event.target.value > 0) {
                setState({
                    ...state,
                    "recipe_add_ingredient_amount": Number(event.target.value)
                });
            }
        } else if (event.target.name === "recipe_add_ingredient_unit") {

            setState({
                ...state,
                "recipe_add_ingredient_unit": event.target.value
            });
        } else {
            const values = [...state.selected_item.recipe_ingredients];
            const amount = Number(event.target.value);
            if (amount > 0) {
                values[index].ingredient_amount = amount;
                const item = {
                    ...state.selected_item,
                    "recipe_ingredients": values
                };

                setState({
                    ...state,
                    "selected_item": item
                });
            }
        }
    };

    const handleAddIngredient = () => {
        const item = {
            ...state.selected_item,
            "recipe_ingredients": [...state.selected_item.recipe_ingredients, {
                ingredient_name: state.recipe_add_ingredient_name,
                ingredient_amount: state.recipe_add_ingredient_amount,
                ingredient_unit: state.recipe_add_ingredient_unit
            }]
        };

        setState({
            ...state,
            "selected_item": item
        });
    };

    const handleDeleteIngredient = (index) => {
        const values = [...state.selected_item.recipe_ingredients];
        values.splice(index, 1);
        const item = {
            ...state.selected_item,
            "recipe_ingredients": values
        };
        setState({
            ...state,
            "selected_item": item
        });
    };

    const handleChangeStep = (event, index) => {
        if (event.target.name === "recipe_add_step_name") {

            setState({
                ...state,
                "recipe_add_step_name": event.target.value
            });
        } else {
            const values = [...state.selected_item.recipe_steps];
            const step_name = event.target.value;
                values[index].step_name = step_name;
                const item = {
                    ...state.selected_item,
                    "recipe_steps": values
                };

                setState({
                    ...state,
                    "selected_item": item
                });
            }
    };

    const handleAddStep = () => {
        let number;
        let steps_quantity = state.selected_item.recipe_steps.length;
        if (steps_quantity <= 0) {
            number = 1;
        } else {
            number = steps_quantity + 1;
        }
        const item = {
            ...state.selected_item,
            "recipe_steps": [...state.selected_item.recipe_steps, {
                step_number: number,
                step_name: state.recipe_add_step_name
            }]
        };

        setState({
            ...state,
            "selected_item": item,
            "recipe_add_step_name": ""
        });
    };

    const handleDeleteStep = (index) => {
        const values = [...state.selected_item.recipe_steps];
        values.splice(index, 1);
        const item = {
            ...state.selected_item,
            "recipe_steps": values
        };
        setState({
            ...state,
            "selected_item": item
        });
    };

    const handleClose = () => {
        onClose();
    };

    const handleEdit = () => {
        if (props.mode === "view") {
            if (type === "product")
                history.push('/products/' + state.selected_item.product_id + '/edit');
            else
                history.push('/recipes/' + state.selected_item.recipe_id + '/edit');
        } else {
            if (type === "product")
                history.push('/products/' + state.selected_item.product_id + '/view');
            else
                history.push('/recipes/' + state.selected_item.recipe_id + '/view');
        }

    };

    const handleRemove = () => {
        if (type === "product") {
            http.delete("/api/products/remove/" + state.selected_item.product_id)
                .then(resp => {
                    if (resp.data.message !== "Product " + state.selected_item.product_name + " has been removed successfully") {
                        setState({
                            ...state,
                            "msg": resp.data.message
                        });
                    } else {
                        props.handleOperationMessage("Product " + state.selected_item.product_name + " has been removed");
                        onClose();
                    }
                })
                .catch(error => console.log(error));
        } else {
            http.delete("/api/recipes/remove/" + state.selected_item.recipe_id)
                .then(resp => {
                    if (resp.data.message !== "Recipe " + state.selected_item.recipe_name + " has been removed successfully") {
                        setState({
                            ...state,
                            "msg": resp.data.message
                        });
                    } else {
                        props.handleOperationMessage("Recipe " + state.selected_item.recipe_name + " has been removed");
                        onClose();
                    }
                })
                .catch(error => console.log(error));
        }
    };

    const handleCategory = (event) => {
        event.cancelBubble = true;
        if (event.stopPropagation) event.stopPropagation();
        console.info("Category");
    };

    const handleAuthor = (event) => {
        event.cancelBubble = true;
        if (event.stopPropagation) event.stopPropagation();
        console.info("Author");
    };

    const handleFavouriteIcon = (index) => {

        let recipe_favourite = false;

        let satisfaction = state.selected_item.recipe_customer_satisfactions.filter(satisfaction => satisfaction.customer_satisfaction_author === props.name + " " + props.surname);

        if (!satisfaction)
            recipe_favourite = satisfaction[0].customer_satisfaction_favourite;

        if (recipe_favourite) {
            return (
                <Tooltip title="Remove from favourite" aria-label="Remove from favourite">
                    <IconButton aria-label="Remove from favourite" className="recipe_icon_button"
                                onClick={event => handleAddToFavourite(event, index)}>
                        <FavoriteIcon fontSize="small"/>
                    </IconButton>
                </Tooltip>
            );
        } else {
            return (
                <Tooltip title="Add to favourite" aria-label="Add to favourite">
                    <IconButton aria-label="Add to favourite" className="recipe_icon_button"
                                onClick={event => handleAddToFavourite(event, index)}>
                        <FavoriteBorderIcon fontSize="small"/>
                    </IconButton>
                </Tooltip>
            )
        }
    };

    const handleAddToFavourite = async (event, index) => {
        event.cancelBubble = true;
        if (event.stopPropagation) event.stopPropagation();
        const recipeId = state.recipes[index].recipe_id;
        const recipe_favourite = state.recipes[index].customer_satisfactions.filter(satisfaction => satisfaction.customer_satisfaction_author === props.name + " " + props.surname)[0].customer_satisfaction_favourite;

        let response = await http.put("/api/recipes/markFavourite/" + recipeId);

        if (response.data.message.startsWith("Recipe with id")) {
            setState({
                ...state,
                "msg": response.data.message
            });

            setTimeout(() => {
                state.recipes[index].customer_satisfactions.find(customer_satisfaction => customer_satisfaction.customer_satisfaction_author === props.name + " " + props.surname).customer_satisfaction_favourite = !recipe_favourite;
                setState({
                    ...state,
                    "msg": ""
                });
            }, 3000);
        } else {
            console.error(response.data.message);
        }

    };

    const handleShare = (event, index) => {
        event.cancelBubble = true;
        if (event.stopPropagation) event.stopPropagation();
        console.log("share");

        const current_recipe_shared = !state.recipes[index].recipe_shared;

        const recipe = {
            ...state.recipes[index],
            "recipe_shared": current_recipe_shared
        };

        setState({
            ...state,
            "recipes": [...state.recipes.slice(0, index), recipe, ...state.recipes.slice(index + 1)],
            "msg": current_recipe_shared ? "Recipe " + state.recipes[index].recipe_name + " has been shared" : "Recipe " + state.recipes[index].recipe_name + " has been cancelled sharing"
        });

        setTimeout(() => {
            setState({
                ...state,
                "msg": ""
            });
        }, 3000);
    };

    const handleGeneralRating = (index) => {
        let ratings = state.recipes[index].recipe_customer_satisfactions;
        let average_rating = ratings.flatMap(rating => rating.customer_satisfaction_rating).reduce((r1, r2) => r1 + r2) / ratings.length;
        return Math.round(average_rating * 1000) / 1000;
    };

    const handlePersonalRating = (index) => {
        return state.recipes[index].recipe_customer_satisfactions.find(customer_satisfaction => customer_satisfaction.customer_satisfaction_author === props.name + " " + props.surname).customer_satisfaction_rating;
    };

    const handlePersonalRatingEdit = async (event, index, newValue) => {

        const recipeId = state.recipes[index].recipe_id;

        if (recipeId == null) {
            console.error("recipeId cannot be null");
            return;
        }

        let response = await http.put("/api/recipes/rate/" + recipeId + "/" + newValue);

        if (response.data.message.startsWith("Recipe with id")) {
            setState({
                ...state,
                "msg": "Recipe " + state.recipes[index].recipe_name + " has been rated"
            });

            setTimeout(() => {
                state.recipes[index].recipe_customer_satisfactions.find(customer_satisfaction => customer_satisfaction.customer_satisfaction_author === props.name + " " + props.surname).customer_satisfaction_rating = newValue;
                setState({
                    ...state,
                    "msg": ""
                });
            }, 3000);
        } else {
            console.error(response.data.message);
        }

    };

    const validateProduct = () => {
        let product = {};
        let productName = state.selected_item.product_name;
        if (productName.length < 2 || productName.length > 40) {
            setState({
                ...state,
                "msg": "Product name has to have min 2 and max 40 characters"
            });
            return "error";
        } else if (!(/^[a-zA-Z ]+$/.test(productName))) {
            setState({
                ...state,
                "msg": "Product name has to contain only letters and spaces"
            });
            return "error";
        }
        product.productName = productName;

        let calories = state.selected_item.product_calories;
        if (calories.length < 1 || calories.length > 20) {
            setState({
                ...state,
                "msg": "Product calories has to have min 1 and max 20 characters"
            });
            return "error";
        } else if (!(/^0$/.test(calories) || /^(-)?[1-9]\d*$/.test(calories))) {
            setState({
                ...state,
                "msg": "Product calories has to contain only digits"
            });
            return "error";
        } else if (calories < 0) {
            setState({
                ...state,
                "msg": "Product calories has to be greater or equal 0"
            });
            return "error";
        }
        product.calories = calories;

        let category = state.selected_item.product_category;
        if (category === "") {
            setState({
                ...state,
                "msg": "Product category has to be chosen"
            });
            return "error";
        }
        product.category = category;

        let nutrient_amount = state.selected_item.product_nutrients.length;

        product.nutrients = [];

        for (let i = 0; i < nutrient_amount; i++) {
            let nutrient = state.selected_item.product_nutrients[i];
            if (nutrient.nutrient_amount.length < 1 || nutrient.nutrient_amount.length > 20) {
                setState({
                    ...state,
                    "msg": "Product nutrient has to have min 1 and max 20 characters"
                });
                return "error";
            } else if (!(/^0$/.test(nutrient.nutrient_amount) || /^(-)?[1-9]\d*$/.test(nutrient.nutrient_amount))) {
                setState({
                    ...state,
                    "msg": "Product nutrient has to contain only digits"
                });
                return "error";
            } else if (nutrient.nutrient_amount < 0) {
                setState({
                    ...state,
                    "msg": "Product nutrient has to be greater or equal 0"
                });
                return "error";
            }

            product.nutrients[i] = nutrient.nutrient_name + ";" + nutrient.nutrient_amount;
        }

        return product;
    };

    const validateRecipe = () => {
        let recipe = {};
        let recipeName = state.selected_item.recipe_name;
        if (recipeName.length < 2 || recipeName.length > 40) {
            setState({
                ...state,
                "msg": "Recipe name has to have min 2 and max 40 characters"
            });
            return "error";
        } else if (!(/^[a-zA-Z ]+$/.test(recipeName))) {
            setState({
                ...state,
                "msg": "Recipe name has to contain only letters and spaces"
            });
            return "error";
        }
        recipe.recipeName = recipeName;

        let recipe_products_amount = state.selected_item.recipe_ingredients.length;

        if (recipe_products_amount < 1) {
            setState({
                ...state,
                "msg": "Recipe has to include min 1 ingredient"
            });
            return "error";
        }

        recipe.recipeProducts = [];

        for (let i = 0; i < recipe_products_amount; i++) {
            let ingredient = state.selected_item.recipe_ingredients[i];
            if (ingredient.ingredient_amount.length < 1 || ingredient.ingredient_amount.length > 20) {
                setState({
                    ...state,
                    "msg": "Recipe ingredient has to have min 1 and max 20 characters"
                });
                return "error";
            } else if (!(/^0$/.test(ingredient.ingredient_amount) || /^(-)?[1-9]\d*$/.test(ingredient.ingredient_amount))) {
                setState({
                    ...state,
                    "msg": "Recipe ingredient has to contain only digits"
                });
                return "error";
            } else if (ingredient.ingredient_amount < 0) {
                setState({
                    ...state,
                    "msg": "Recipe ingredient has to be greater or equal 0"
                });
                return "error";
            }

            recipe.recipeProducts[i] = ingredient.ingredient_name + ";" + ingredient.ingredient_amount + ";" + ingredient.ingredient_unit;
        }

        let recipe_steps_amount = state.selected_item.recipe_steps.length;

        if (recipe_steps_amount < 1) {
            setState({
                ...state,
                "msg": "Recipe has to include min 1 step"
            });
            return "error";
        }

        recipe.recipeSteps = [];

        for (let i = 0; i < recipe_steps_amount; i++) {

            let step = state.selected_item.recipe_steps[i];

            if (step.step_number < 1 || (i > 0 && step.step_number < state.selected_item.recipeSteps[i - 1].step_number)) {
                setState({
                    ...state,
                    "msg": "Recipe step number has to be greater than 0 and previous step number."
                });
                return "error";
            } else if (step.step_name.length < 3 || step.step_name.length > 50) {
                setState({
                    ...state,
                    "msg": "Recipe step description has to have min 3 and max 50 characters"
                });
                return "error";
            } else if (!/^[a-zA-Z,. 0-9]+$/.test(step.step_name)) {
                setState({
                    ...state,
                    "msg": "Recipe step description has to contain only uppercase and lowercase letters, digits, commas, dots and spaces."
                });
                return "error";
            }

            recipe.recipeSteps[i] = step.step_name;
        }

        return recipe;
    };

    const handleSave = async () => {

        let item, item_id, item_name, capitalized_type;

        switch (type) {
            case "product":
                item = await validateProduct();
                item_id = state.selected_item.product_id;
                item_name = state.selected_item.product_name;
                capitalized_type = "Product";
                break;
            case "recipe":
                item = await validateRecipe();
                item_id = state.selected_item.recipe_id;
                item_name = state.selected_item.recipe_name;
                capitalized_type = "Recipe";
                break;
            default:
                console.error("bad type of viewer modal item");
                return;
        }

        if (item === "error")
            return;

        console.log(item);

        if (item_id === "new") {
            http.post("/api/" + type + "s/add", item)
                .then(resp => {
                    item_id = Number(resp.data.message.split(" ")[0]);

                    if (resp.data.message !== item_id + " " + capitalized_type + " " + item_name + " has been added successfully") {
                        setState({
                            ...state,
                            "msg": resp.data.message
                        });
                    } else {
                        setState({
                            ...state,
                            "submitted": true,
                            "image_upload_item_id": item_id
                        });

                        props.handleOperationMessage(capitalized_type + " " + item_name + " has been added");
                        onClose();
                    }
                })
                .catch(error => console.log(error));
        } else {
            http.put("/api/" + type + "s/update/" + item_id, item)
                .then(resp => {

                    if (resp.data.message !== capitalized_type + " " + item_name + " has been updated successfully") {
                        setState({
                            ...state,
                            "msg": resp.data.message
                        });
                    } else {
                        setState({
                            ...state,
                            "submitted": true,
                            "image_upload_item_id": item_id
                        });

                        props.handleOperationMessage(capitalized_type + " " + item_name + " has been updated");
                        onClose();
                    }
                })
                .catch(error => console.log(error));
        }

    };

    const handleBottomItemRep = (event, item_id) => {
        history.push('/' + type + 's/' + item_id + '/' + props.mode);
    };

    const handleCloseExistedProductsPopup = (event) => {
        setState({
            ...state,
            open_existed_products_popup: false
        });
    };

    let item;

    switch (type) {
        case "product":
            item = <Grid item key={state.selected_item.product_id} id={"product" + state.selected_item.product_id}
                         className="product_edit">
                <div className="product_header product_header_buttons">
                    {props.mode === 'view' ?
                        <div className="product_name">
                            {(state.selected_item.product_id !== 'new' ?
                                state.selected_item.product_name :
                                "No product name")}
                        </div> :
                        <div>
                            {state.msg}
                            <div className="product_header product_header_inputs">

                                <div className="product_name">
                                    <TextField className="category_select" id="product_name_input" name="product_name"
                                               label="Product name" variant="filled"
                                               value={state.selected_item.product_name}
                                               autoComplete="off" onChange={event => handleChangeItemId(event)}
                                               onBlur={event => handleCloseExistedProductsPopup(event)}/>
                                </div>
                                <div className="product_category">
                                    <FormControl variant="filled" className="form_control">
                                        <InputLabel id="category_select_label">Category</InputLabel>
                                        <Select
                                            labelId="category_select_label"
                                            id="category_select"
                                            name="product_category"
                                            value={state.selected_item.product_category}
                                            size="small"
                                            onChange={event => handleChangeItemId(event)}
                                        >
                                            {props.categories.map((category, index) => (
                                                <MenuItem key={index}
                                                          value={category.category_name}>{category.category_name}</MenuItem>
                                            ))}
                                        </Select>
                                    </FormControl>
                                </div>
                                <div>
                                    <TextField
                                        name="product_calories"
                                        label="Calories"
                                        type="number"
                                        variant="filled"
                                        value={state.selected_item.product_calories}
                                        size="small"
                                        aria-label="calories"
                                        InputProps={{
                                            endAdornment: <InputAdornment
                                                position="end">
                                                <div>kcal</div>
                                            </InputAdornment>,
                                        }}
                                        onChange={event => handleChangeItemId(event)}
                                    />
                                </div>
                            </div>
                        </div>}

                    <div className="product_buttons">
                        <Tooltip title="Delete" aria-label="delete">
                            <IconButton aria-label="delete" className="product_icon_button"
                                        onClick={handleRemove} disabled={state.selected_item.product_id === 'new'}>
                                <DeleteIcon fontSize="small"/>
                            </IconButton>
                        </Tooltip>
                        <Tooltip title="Edit" aria-label="edit">
                            <IconButton type="button" aria-label="edit" className="product_icon_button"
                                        onClick={(event) => handleEdit(event, state.selected_item.product_id)}>
                                <EditIcon fontSize="small"/>
                            </IconButton>
                        </Tooltip>
                    </div>
                </div>
                <div className="creation_date">
                    {state.selected_item.product_id === "new" ? "No creation date" : "created " + state.selected_item.creation_date}
                </div>

                <div className="product_content">
                    <div className="product_image_container">

                        {props.mode === 'view' ?
                            (state.selected_item.product_id !== 'new' ?
                                (state.selected_item.product_image !== '' ?
                                    <img src={state.selected_item.product_image} alt={state.selected_item.product_name}
                                         className="product_image"/> :
                                    <div><SpaIcon className={classes.photo_placeholder}/></div>) :
                                <div><SpaIcon className={classes.photo_placeholder}/></div>) :
                            <UploadImages submitted={state.submitted}
                                          currentImage={state.selected_item.product_image}
                                          type="product" id={state.image_upload_item_id}/>}
                    </div>
                    <div className="product_description">
                        {props.mode === 'view' ?
                            <div className="product_category">
                                {(state.selected_item.product_id !== 'new' ?
                                    <Chip
                                        name="category"
                                        size="small"
                                        avatar={<CategoryIcon/>}
                                        label={state.selected_item.product_category}
                                        onClick={event => handleCategory(event)}
                                    /> : <div className={classes.category}>No category</div>)}
                            </div> :
                            null}
                        {props.mode === 'view' &&
                        (state.selected_item.product_id !== 'new' ?
                                <div className="product_author" onClick={event => handleAuthor(event)}>
                                    <Avatar/>
                                    <div
                                        className="product_author_name">{state.selected_item.product_author}</div>
                                </div> :
                                <div>
                                    No author
                                </div>
                        )}
                        {props.mode === 'view' ?
                            <div className="product_nutrients_header">
                                {state.selected_item.product_calories} kcal
                            </div> :
                            null}
                        {props.mode === 'view' ?
                            <div className="product_nutrients">
                                <div className="product_nutrients_header">Nutrients</div>
                                {state.selected_item.product_nutrients.map((nutrient, index) =>
                                    <div key={index} className="product_nutrient">
                                        <div
                                            className="product_nutrient_name">{nutrient.nutrient_name}</div>
                                        <div
                                            className="product_nutrient_amount">{nutrient.nutrient_amount}</div>
                                        <div
                                            className="product_nutrient_unit">{nutrient.nutrient_unit}</div>
                                    </div>
                                )}
                            </div> :
                            <div className="product_nutrients">
                                <h4>Nutrients</h4>
                                {state.selected_item.product_nutrients.map((nutrient, index) => (
                                    <div key={index}>
                                        <TextField
                                            name={nutrient.nutrient_name}
                                            label={nutrient.nutrient_name}
                                            type="number"
                                            variant="filled"
                                            value={nutrient.nutrient_amount}
                                            size="small"
                                            aria-label={nutrient.nutrient_name}
                                            InputProps={{
                                                endAdornment: <InputAdornment
                                                    position="end">
                                                    <div>{nutrient.nutrient_unit}</div>
                                                </InputAdornment>,
                                            }}
                                            onChange={event => handleChangeNutrient(index, event)}
                                        />
                                    </div>
                                ))}
                            </div>}
                        {props.mode === 'edit' ?
                            <div>
                                <Button className={classes.button} variant="contained" color="primary"
                                        onClick={() => handleClose()}>Cancel</Button>
                                <Button className={classes.button} variant="contained" color="primary" type="button"
                                        onClick={() => handleSave()}>Save</Button>
                            </div> :
                            null}
                    </div>
                </div>
            </Grid>;
            break;
        case "recipe":
            item = <Grid item key={state.selected_item.recipe_id} id={"recipe" + state.selected_item.recipe_id}
                         className="product_edit recipe_edit">
                <div className="product_header product_header_buttons">
                    {props.mode === 'view' ?
                        <div className="product_name">
                            {(state.selected_item.recipe_id !== 'new' ?
                                state.selected_item.recipe_name :
                                "No recipe name")}
                        </div> :
                        <div>
                            {state.msg}
                            <div className="product_header product_header_inputs">

                                <div className="product_name">
                                    <TextField className="category_select" id="recipe_name_input" name="recipe_name"
                                               label="Recipe name" variant="filled"
                                               value={state.selected_item.recipe_name}
                                               autoComplete="off" onChange={event => handleChangeItemId(event)}
                                    />
                                </div>
                            </div>
                        </div>}

                    <div className="product_buttons">
                        <Tooltip title="Delete" aria-label="delete">
                            <IconButton aria-label="delete" className="product_icon_button"
                                        onClick={handleRemove} disabled={state.selected_item.recipe_id === 'new'}>
                                <DeleteIcon fontSize="small"/>
                            </IconButton>
                        </Tooltip>
                        <Tooltip title="Edit" aria-label="edit">
                            <IconButton type="button" aria-label="edit" className="product_icon_button"
                                        onClick={(event) => handleEdit(event, state.selected_item.recipe_id)}>
                                <EditIcon fontSize="small"/>
                            </IconButton>
                        </Tooltip>
                        {state.loaded && handleFavouriteIcon(item_index)}
                    </div>
                </div>
                <div className="creation_date">
                    {state.selected_item.recipe_id === "new" ? "No creation date" : "created " + state.selected_item.creation_date}
                </div>

                <div className="recipe_content2">
                    <div className="recipe_image_author_ratings">
                        <div className="product_image_container">

                            {props.mode === 'view' ?
                                (state.selected_item.recipe_id !== 'new' ?
                                    (state.selected_item.recipe_image !== '' ?
                                        <img src={state.selected_item.recipe_image}
                                             alt={state.selected_item.recipe_name}
                                             className="product_image"/> :
                                        <div><ReceiptIcon className={classes.photo_placeholder}/></div>) :
                                    <div><ReceiptIcon className={classes.photo_placeholder}/></div>) :
                                <UploadImages submitted={state.submitted}
                                              currentImage={state.selected_item.recipe_image}
                                              type="recipe" id={state.image_upload_item_id}/>}
                        </div>
                        <div className="product_description">
                            {props.mode === 'view' &&
                            (state.selected_item.recipe_id !== 'new' ?
                                    <div className="product_author" onClick={event => handleAuthor(event)}>
                                        <Avatar/>
                                        <div
                                            className="product_author_name">{state.selected_item.recipe_author}</div>
                                    </div> :
                                    <div>
                                        No author
                                    </div>
                            )}
                            <div className="recipe_actions">
                                <div className="recipe_ratings_header">
                                    <div className="recipe_ratings">
                                        General: {5.0} ({2} {"ratings"})
                                    </div>
                                    <Rating name="read-only" value={5.0} precision={0.1} readOnly/>
                                    <div className="recipe_ratings_header">
                                        Personal rating
                                    </div>
                                    <Tooltip title={"Value: " + state.hover_rating !== -1 ? state.hover_rating : 4.0}
                                             aria-label="rate" placement="top">
                                        <Rating name="half-rating" value={4.0} precision={0.1}
                                                onChangeActive={(event, newHover) => setState({
                                                    ...state,
                                                    "hover_rating": newHover
                                                })}/>
                                    </Tooltip>
                                </div>
                                {/*{state.recipes[index].recipe_shared ? <Tooltip title="Cancel sharing" aria-label="Cancel sharing recipe"><Button variant="contained" className="shared_button" size="medium" onClick={event => handleShare(event, index)}>Shared</Button></Tooltip> : <Tooltip title="Share recipe" aria-label="Share"><Button variant="contained" color="primary" size="medium" onClick={event => handleShare(event, index)}>Share</Button></Tooltip>}*/}

                            </div>
                        </div>
                    </div>
                    <div className="recipe_ingredients_steps">
                        {props.mode === 'view' ?
                            <div className="ingredients">
                                <div className="product_nutrients_header">Ingredients</div>
                                {state.selected_item.recipe_ingredients.length === 0 ? "No ingredients added. Add new ingredient" :
                                    state.selected_item.recipe_ingredients.map((ingredient, index) =>
                                    <div key={index} className="product_nutrient">
                                        <div
                                            className="product_nutrient_name">{ingredient.ingredient_name}</div>
                                        <div
                                            className="product_nutrient_amount">{ingredient.ingredient_amount}</div>
                                        <div
                                            className="product_nutrient_unit">{ingredient.ingredient_unit}</div>
                                    </div>
                                )}
                            </div> :
                            <div className="ingredients">
                                <div id="ingredients_list">
                                    <h4>Ingredients</h4>
                                    {state.selected_item.recipe_ingredients.length === 0 ? "No ingredients added. Add new ingredient" :
                                        state.selected_item.recipe_ingredients.map((ingredient, index) => (
                                        <div key={index} className="ingredient_edit">
                                            <TextField
                                                name={ingredient.ingredient_name}
                                                label={ingredient.ingredient_name}
                                                type="number"
                                                variant="filled"
                                                value={ingredient.ingredient_amount}
                                                size="small"
                                                InputProps={{
                                                    endAdornment: <InputAdornment
                                                        position="end">
                                                        <div>{ingredient.ingredient_unit}</div>
                                                    </InputAdornment>,
                                                }}
                                                onChange={event => handleChangeIngredient(event, index)}
                                            />
                                            <Tooltip title="Delete ingredient" aria-label="delete ingredient">
                                                <IconButton aria-label="delete ingredient"
                                                            className="product_icon_button"
                                                            onClick={() => handleDeleteIngredient(index)}>
                                                    <DeleteIcon fontSize="small"/>
                                                </IconButton>
                                            </Tooltip>
                                        </div>
                                    ))}
                                </div>
                                <div className="add_ingredient">
                                    <div className={classes.ingredient_part}>
                                        <div>Product</div>
                                        <Select
                                            id="product_ingredient_select"
                                            name="recipe_add_ingredient_name"
                                            label="Product"
                                            value={state.recipe_add_ingredient_name}
                                            size="small"
                                            onChange={event => handleChangeIngredient(event)}
                                        >
                                            {state.all_products.map((product, index) => (
                                                <MenuItem key={index}
                                                          value={product}>{product}</MenuItem>
                                            ))}
                                        </Select>
                                    </div>
                                    <div className={classes.ingredient_part}>
                                        <div>Amount</div>
                                        <TextField
                                            name="recipe_add_ingredient_amount"
                                            type="number"
                                            variant="standard"
                                            value={state.recipe_add_ingredient_amount}
                                            size="small"
                                            onChange={event => handleChangeIngredient(event)}
                                        />
                                    </div>
                                    <div className={classes.ingredient_part}>
                                        <div>Unit</div>
                                        <Select
                                            id="unit_ingredient_select"
                                            name="recipe_add_ingredient_unit"
                                            value={state.recipe_add_ingredient_unit}
                                            size="small"
                                            onChange={event => handleChangeIngredient(event)}
                                        >
                                            {state.all_units.map((unit, index) => (
                                                <MenuItem key={index}
                                                          value={unit}>{unit}</MenuItem>
                                            ))}
                                        </Select>
                                    </div>
                                    <Tooltip title="Add ingredient" aria-label="add ingredient">
                                        <IconButton aria-label="add ingredient" className="product_icon_button"
                                                    onClick={() => handleAddIngredient()}
                                        >
                                            <AddIcon fontSize="large"/>
                                        </IconButton>
                                    </Tooltip>
                                </div>
                            </div>}
                        <Divider orientation="vertical" flexItem variant='middle'/>
                        {props.mode === 'view' ?
                            <div className="steps">
                                <div className="product_nutrients_header">Steps</div>
                                {state.selected_item.recipe_steps.length === 0 ? "No steps added. Add new step" :
                                    state.selected_item.recipe_steps.map((step, index) =>
                                    <div key={index} className="recipe_step">
                                        <div>{step.step_number+". "}</div>
                                        <div>{step.step_name}</div>
                                    </div>
                                )}
                            </div> :
                            <div className="steps">
                                <div id="ingredients_list">
                                    <h4>Steps</h4>
                                    {state.selected_item.recipe_steps.length === 0 ? "No steps added. Add new step" :
                                        state.selected_item.recipe_steps.map((step, index) => (
                                        <div key={index} className="ingredient_edit">
                                            <TextField
                                                name={index + 1 + ". Step"}
                                                label={index + 1 + ". Step"}
                                                variant="filled"
                                                value={step.step_name}
                                                size="small"
                                                onChange={event => handleChangeStep(event, index)}
                                            />
                                            <Tooltip title="Delete step" aria-label="delete step">
                                                <IconButton aria-label="delete step"
                                                            className="product_icon_button"
                                                            onClick={() => handleDeleteStep(index)}>
                                                    <DeleteIcon fontSize="small"/>
                                                </IconButton>
                                            </Tooltip>
                                        </div>
                                    ))}
                                    <div className="add_ingredient">
                                        <TextField
                                            name="recipe_add_step_name"
                                            label={state.selected_item.recipe_steps.length + 1 + ". Step"}
                                            variant="filled"
                                            value={state.recipe_add_step_name}
                                            size="small"
                                            onChange={event => handleChangeStep(event)}
                                        />
                                        <Tooltip title="Add step" aria-label="add step">
                                            <IconButton aria-label="add step" className="product_icon_button"
                                                        onClick={() => handleAddStep()}
                                                        disabled={state.recipe_add_step_name === ""}
                                            >
                                                <AddIcon fontSize="large"/>
                                            </IconButton>
                                        </Tooltip>
                                    </div>
                                </div>
                            </div>}
                    </div>

                    {props.mode === 'edit' ?
                            <div className={classes.center_button_group}>
                                <Button className={classes.button} variant="contained" color="primary"
                                        onClick={() => handleClose()}>Cancel</Button>
                                <Button className={classes.button} variant="contained" color="primary" type="button"
                                        onClick={() => handleSave()}>Save</Button>
                            </div> :
                            null}
                </div>
            </Grid>;
            break;
    }

    let bottom_items_reps = [];

    const items_quantity = items.length;
    let current_index = item_index, selected_item_index;

    switch (items_quantity) {
        case 1:
            selected_item_index = 0;
            bottom_items_reps = [items[item_index]];
            break;
        case 2:
            selected_item_index = 0;
            bottom_items_reps = [items[item_index], items[(item_index + 1) % items_quantity]];
            break;
        case 3:
            selected_item_index = 1;
            for (let i = 1; i >= -1; i--) {
                if ((item_index + i) < 0) {
                    current_index = items_quantity - 1;
                    bottom_items_reps[0] = items[current_index];
                } else {
                    current_index = (item_index + i) % items_quantity;
                    bottom_items_reps[i + 1] = items[current_index];
                }
            }
            break;
        case 4:
            selected_item_index = 1;
            for (let i = 2; i >= -1; i--) {
                if ((item_index + i) < 0) {
                    current_index = items_quantity - 1;
                    bottom_items_reps[0] = items[current_index];
                } else {
                    current_index = (item_index + i) % items_quantity;
                    bottom_items_reps[i + 1] = items[current_index];
                }
            }
            break;
        case 5:
            selected_item_index = 2;
            for (let i = 2; i >= -2; i--) {
                if ((item_index + i) < 0) {
                    if (i === -1) {
                        current_index = items_quantity - 2;
                        bottom_items_reps[0] = items[current_index];
                    }
                    if (i === -2) {
                        current_index = items_quantity - 1;

                        if (bottom_items_reps[0]) {
                            bottom_items_reps[1] = items[current_index];
                        } else {
                            bottom_items_reps[0] = items[current_index];
                        }
                    }

                } else {
                    current_index = (item_index + i) % items_quantity;
                    bottom_items_reps[i + 2] = items[current_index];
                }
            }
            break;
        case 6:
            selected_item_index = 2;
            for (let i = 3; i >= -2; i--) {
                if ((item_index + i) < 0) {
                    if (i === -1) {
                        current_index = items_quantity - 2;
                        bottom_items_reps[0] = items[current_index];
                    }
                    if (i === -2) {
                        current_index = items_quantity - 1;

                        if (bottom_items_reps[0]) {
                            bottom_items_reps[1] = items[current_index];
                        } else {
                            bottom_items_reps[0] = items[current_index];
                        }
                    }

                } else {
                    current_index = (item_index + i) % items_quantity;
                    bottom_items_reps[i + 2] = items[current_index];
                }
            }
            break;
        case 7:
        default:
            selected_item_index = 3;
            for (let i = 3; i >= -3; i--) {
                if ((item_index + i) < 0) {

                    if (i === -1) {
                        current_index = items_quantity - 3;
                        bottom_items_reps[0] = items[current_index];
                    }

                    if (i === -2) {
                        current_index = items_quantity - 2;

                        if (bottom_items_reps[0]) {
                            bottom_items_reps[1] = items[current_index];
                        } else {
                            bottom_items_reps[0] = items[current_index];
                        }
                    }

                    if (i === -3) {
                        current_index = items_quantity - 1;

                        if (bottom_items_reps[0] && bottom_items_reps[1]) {
                            bottom_items_reps[2] = items[current_index];
                        } else if (bottom_items_reps[0]) {
                            bottom_items_reps[1] = items[current_index];
                        } else {
                            bottom_items_reps[0] = items[current_index];
                        }
                    }
                } else {
                    current_index = (item_index + i) % items_quantity;
                    bottom_items_reps[i + 3] = items[current_index];

                }

            }
    }

    return (
        <div>
            {state.loaded && <Modal
                aria-labelledby="transition-modal-title"
                aria-describedby="transition-modal-description"
                className={classes.modal}
                open={open}
                onClose={handleClose}
                closeAfterTransition
                BackdropComponent={Backdrop}
                BackdropProps={{
                    timeout: 500,
                }}
            >
                <Fade in={open}>
                    <div className="viewer">
                        {type === 'product' && <ExistedProductsPopup
                            open={state.open_existed_products_popup}
                            product_name={state.selected_item.product_name}
                        />}
                        <div className={classes.header}>
                            <div className={classes.headerTitle}>
                                {state.selected_item.product_id === 'new' || state.selected_item.recipe_id === 'new' ?
                                    <h2>new</h2> : <h2>{header} ({item_index + 1}/{items_quantity})</h2>}
                            </div>
                            <Fab aria-label="close">
                                <CloseIcon fontSize="large" onClick={handleClose}/>
                            </Fab>
                        </div>
                        <div className="product_selected">
                            <Fab aria-label="prev">
                                <NavigateBeforeRoundedIcon fontSize="large" onClick={props.handlePrevProduct}/>
                            </Fab>

                            {item}

                            <Fab className="next_button" aria-label="next">
                                <NavigateNextRoundedIcon fontSize="large" onClick={props.handleNextProduct}/>
                            </Fab>
                        </div>
                        {state.loaded && <div className="product_selected_bottom">
                            {bottom_items_reps.map((item, index) => (
                                item && <div key={index} className={classes.bottom_item_rep}>
                                    <Chip
                                        className={index === selected_item_index ? "selected_bottom_product_rep" : (Math.abs(selected_item_index - index) === 1 ? "first_middle_bottom_product_rep" : (Math.abs(selected_item_index - index) === 2 ? "second_middle_bottom_product_rep" : "third_middle_bottom_product_rep"))}
                                        size={index === selected_item_index ? "medium" : "small"}
                                        label={type === 'product' ? item.product_name : (type === 'recipe' ? item.recipe_name : "")}
                                        color="primary"
                                        onClick={index !== selected_item_index ? event => handleBottomItemRep(event, type === 'product' ? item.product_id : (type === 'recipe' ? item.recipe_id : -1)) : null}
                                    />
                                </div>
                            ))}
                        </div>}

                    </div>
                </Fade>
            </Modal>}
        </div>
    );
}