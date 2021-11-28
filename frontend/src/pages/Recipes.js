import React, {useEffect} from 'react'
import {Container, Divider, Grid, makeStyles, Tooltip} from "@material-ui/core";
import Tabs from "@material-ui/core/Tabs";
import Tab from "@material-ui/core/Tab";
import FormControl from "@material-ui/core/FormControl";
import InputLabel from '@material-ui/core/InputLabel';
import FilledInput from '@material-ui/core/FilledInput';
import Fab from "@material-ui/core/Fab";
import AddIcon from '@material-ui/icons/Add';
import Avatar from '@material-ui/core/Avatar';
import IconButton from '@material-ui/core/IconButton';
import DeleteIcon from '@material-ui/icons/Delete';
import EditIcon from '@material-ui/icons/Edit';
import Button from "@material-ui/core/Button";
import ConfirmationDialog from "../components/ConfirmationDialog";
import http from "../http-common";
import Rating from '@material-ui/lab/Rating';
import FavoriteBorderIcon from '@material-ui/icons/FavoriteBorder';
import FavoriteIcon from '@material-ui/icons/Favorite';
import SuitableRecipeModal from "../components/SuitableRecipeModal";
import CheckIcon from '@material-ui/icons/Check';
import ClearIcon from '@material-ui/icons/Clear';
import {useHistory, useParams} from "react-router-dom";
import ViewerModal from "../components/ViewerModal";
import ArrowDownwardRoundedIcon from '@material-ui/icons/ArrowDownwardRounded';
import CloseRoundedIcon from '@material-ui/icons/CloseRounded';
import ReceiptIcon from '@material-ui/icons/Receipt';
import CloseIcon from '@material-ui/icons/Close';
import Snackbar from "@material-ui/core/Snackbar";

const useStyles = makeStyles({
    paper: {
        width: '80%',
        maxHeight: 435,
    },
    photo_placeholder: {
        fontSize: 200
    },
    searchContainer: {
        display: 'flex',
        flexDirection: 'row',
        alignItems: 'center'
    },
    searchButton: {
        marginLeft: '2%',
    },
    loadMore: {
        width: '10%',
        marginLeft: 'auto',
        marginRight: 'auto'
    }
});

export default function Recipes(props) {

    const history = useHistory();

    const classes = useStyles();

    let {recipeId, mode} = useParams();

    let recipes_parameters = {};

    // console.log("recipeId " + recipeId);
    // console.log("mode " + mode);

    const [state, setState] = React.useState({
        searchPhrase: '',
        groupNumber: 0,
        recipes_group: 0,//0: personal, 1: shared, 2: unconfirmed
        recipes: [
            // {"recipe_id": 1, "recipe_name": "Salmon with green beans and bacon", "recipe_author": "John Smith", "recipe_favourite": false, "recipe_shared": false, "recipe_image": "http://localhost:8097/api/images/get/recipe/recipe1.jpg", "creation_date": "7.08.2021 21:11:14", "assessment_date": "9.08.2021 21:11:14", "rejectExplanation": "", "approval_status": "accepted", "recipeCustomerSatisfactions": [{"rating_author": "John Smith", "rating_value": 0.2}, {"rating_author": "Derek Johnson", "rating_value": 3.5}, {"rating_author": "Kate Bell", "rating_value": 2.0}, {"rating_author": "Jessica Wells", "rating_value": 5.0}], "ingredients": [{"ingredient_name": "salmon fillet", "ingredient_quantity": 4, "ingredient_unit": "pcs"}, {"ingredient_name": "green beans", "ingredient_quantity": 450, "ingredient_unit": "g"}, {"ingredient_name": "butter", "ingredient_quantity": 2, "ingredient_unit": "tablespoons"}, {"ingredient_name": "smoked bacon", "ingredient_quantity": 2, "ingredient_unit": "slices"}, {"ingredient_name": "breadcrumbs", "ingredient_quantity": 2, "ingredient_unit": "tablespoons"}, {"ingredient_name": "olive oil", "ingredient_quantity": 3, "ingredient_unit": "tablespoons"}, {"ingredient_name": "water", "ingredient_quantity": 3, "ingredient_unit": "tablespoons"}], "steps": [{"step_number": 1, "step_name": "Cut each salmon fillet into 4 pieces and place them in ovenproof dish lined with parchment. Pour it some olive oil. Leave it for several minutes, then roast at 160 Celsius degrees for 15 minutes."}, {"step_number": 2, "step_name": "Cube smoked bacon and fry it up on the frying pan."}, {"step_number": 3, "step_name": "Blanch green beans, then strain it and add to smoked bacon. Stop frying after 1 minute."}, {"step_number": 4, "step_name": "Add butter to green beans and fry it for a few minutes."}, {"step_number": 5, "step_name": "Before you finish frying sprinkle its content with breadcrumbs. Serve with roasted salmon. You might append boiled rice."}]}
        ],
        hover_rating: -1,
        recipe_index: 'new',
        msg: "",
        loaded: false,
        open_confirmation_modal: false,
        complement: "",
        confirmation_recipe_id: null,
        confirmation_recipe_name: null,
        open_preference_suitability_popup: false,
        suitabilityRecipeId: null,
        suitabilityRecipeName: null,
        open_viewer_modal: false,
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

    switch (state.recipes_group) {
        case 0:
            if (props.admin === true && props.adminMode === true) {
                recipes_parameters.recipesGroup = "accepted";
            } else {
                recipes_parameters.recipesGroup = "personal";
            }
            break;
        case 1:
            if (props.admin === true && props.adminMode === true) {
                recipes_parameters.recipesGroup = "pending";
            } else {
                recipes_parameters.recipesGroup = "shared";
            }
            break;
        case 2:
            if (props.admin === true && props.adminMode === true) {
                recipes_parameters.recipesGroup = "rejected";
            } else {
                recipes_parameters.recipesGroup = "unconfirmed";
            }
            break;
        case 3:
            recipes_parameters.recipesGroup = "favourite";
            break;
        default:
            recipes_parameters.recipesGroup = "";
            console.error("Wrong recipes group");
    }

    useEffect(
        async () => {
            handleSearch();
        }, [state.recipes_group, props.adminMode, recipeId, mode]
    );

    const createRecipe = async (data, x, recipesGroup) => {
        let recipe = {};
        let info;
        if (recipesGroup === "personal" || recipesGroup === "shared" || recipesGroup === "favourite") {
            recipe.positiveSuitabilities = data[x].positiveSuitabilities;
            recipe.negativeSuitabilities = data[x].negativeSuitabilities;
            info = data[x].recipe;
        } else {
            info = data[x];
        }
        recipe.recipe_id = info.recipeId;
        recipe.recipe_name = info.recipeName;
        recipe.recipe_author = info.recipeOwner.name + " " + info.recipeOwner.surname;
        recipe.recipe_author_id = info.recipeOwner.userId;
        recipe.recipe_author_image = info.recipeOwner.avatarImage;

        let ingredients_quantity = info.recipeProducts.length;
        recipe.recipe_ingredients = [];

        for (let i = 0; i < ingredients_quantity; i++) {
            recipe.recipe_ingredients[i] = {};
            recipe.recipe_ingredients[i].ingredient_name = info.recipeProducts[i].product.productName;
            recipe.recipe_ingredients[i].ingredient_amount = Number(info.recipeProducts[i].productAmount);
            recipe.recipe_ingredients[i].ingredient_unit = info.recipeProducts[i].productUnit;
        }

        let steps_quantity = info.recipeSteps.length;
        recipe.recipe_steps = [];

        for (let i = 0; i < steps_quantity; i++) {
            recipe.recipe_steps[i] = {};
            recipe.recipe_steps[i].step_number = i + 1;
            recipe.recipe_steps[i].step_name = info.recipeSteps[i].recipeStepDescription;
        }

        let customer_satisfactions_quantity = info.recipeCustomerSatisfactions.length;
        recipe.recipe_customer_satisfactions = [];

        for (let i = 0; i < customer_satisfactions_quantity; i++) {
            recipe.recipe_customer_satisfactions[i] = {};
            recipe.recipe_customer_satisfactions[i].customer_satisfaction_author = info.recipeCustomerSatisfactions[i].customerSatisfactionOwner.name + " " + info.recipeCustomerSatisfactions[i].customerSatisfactionOwner.surname;
            recipe.recipe_customer_satisfactions[i].customer_satisfaction_rating = Number(info.recipeCustomerSatisfactions[i].recipeRating);
            recipe.recipe_customer_satisfactions[i].customer_satisfaction_favourite = info.recipeCustomerSatisfactions[i].recipeFavourite;
        }

        recipe.recipe_shared = info.recipeShared;
        recipe.recipe_image = info.recipeImage;

        let creationDate = new Date(info.creationDate);
        recipe.creation_date = creationDate.toLocaleDateString() + " " + creationDate.toLocaleTimeString();
        recipe.approval_status = info.approvalStatus;

        if (info.assessmentDate !== null) {
            let assessmentDate = new Date(info.assessmentDate);
            recipe.assessment_date = assessmentDate.toLocaleDateString() + " " + assessmentDate.toLocaleTimeString();
            recipe.reject_explanation = info.rejectExplanation;
        }
        const response = await http.get("/api/recipes/checkIfInCollection/" + recipe.recipe_id);

        recipe.in_collection = response.data.message.endsWith("is in collection");

        return recipe;
    };

    const handleTab = (event, newValue) => {
        setState({
            ...state,
            groupNumber: 0,
            recipes_group: newValue,
            recipes: []
        });
    };

    const handleChange = (event) => {
        const phraseChange = state.searchPhrase !== event.target.value;
        setState({
            ...state,
            [event.target.name]: event.target.value,
            groupNumber: phraseChange ? 0 : state.groupNumber
        });
    };

    const handleRecipe = (event, recipe_id) => {
        event.cancelBubble = true;
        if (event.stopPropagation) event.stopPropagation();
        history.push('/recipes/' + recipe_id + '/view');
    };

    const handleAuthor = (event) => {
        console.info("Author");
    };

    const handleEdit = (event, recipe_id) => {
        event.cancelBubble = true;
        if (event.stopPropagation) event.stopPropagation();
        history.push('/recipes/' + recipe_id + '/edit');
    };

    const handleAddNewProduct = () => {
        history.push('/recipes/new/edit');
    };

    const handleRemove = (event, index) => {
        event.cancelBubble = true;
        if (event.stopPropagation) event.stopPropagation();
        const recipe_id = state.recipes[index].recipe_id;

        http.delete("/api/recipes/remove/" + recipe_id)
            .then(resp => {
                handleOperationMessage(resp.data.message);
            })
            .catch(error => console.log(error));
    };

    const handleAssess = (event, recipe_id, recipe_name) => {
        // event.cancelBubble = true;
        // if (event.stopPropagation) event.stopPropagation();
        const name = event.target.innerText;
        setState({
            ...state,
            complement: name === "REJECT" ? "reject recipe " + recipe_name : "accept recipe " + recipe_name,
            open_confirmation_modal: true,
            confirmation_recipe_id: recipe_id,
            confirmation_recipe_name: recipe_name
        });
    };

    const handleCloseConfirmationPopup = () => {
        setState({
            ...state,
            open_confirmation_modal: false,
        });

    };

    const handleCloseViewerModal = () => {
        console.log("reloading");
        setState({
            ...state,
            groupNumber: 0
        });
        history.push("/recipes");
    };

    const handleFavouriteIcon = (index) => {

        let recipe_favourite = false;

        let satisfaction = state.recipes[index].recipe_customer_satisfactions;

        if (satisfaction) {
            satisfaction = satisfaction.filter(satisfaction => satisfaction.customer_satisfaction_author === props.name + " " + props.surname);
        }

        if (satisfaction && satisfaction[0])
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

        let response = await http.put("/api/recipes/markFavourite/" + recipeId);

        let msg = response.data.message;

        if (!msg.startsWith("Recipe with id")) {

            console.error(msg);

            setState({
                ...state,
                "msg": msg
            });

            handleOpen();

        } else {

            let sat = {
                customer_satisfaction_author: props.name + " " + props.surname,
                customer_satisfaction_rating: null,
                customer_satisfaction_favourite: false
            };

            let currentSatisfaction = state.recipes[index].recipe_customer_satisfactions.find(satisfaction => satisfaction.customer_satisfaction_author === (props.name + " " + props.surname));

            console.log(currentSatisfaction);

            if (currentSatisfaction) {
                sat = {
                    ...currentSatisfaction
                };
            }

            console.log(sat);

            sat.customer_satisfaction_favourite = !sat.customer_satisfaction_favourite;

            const mark = sat.customer_satisfaction_favourite ? "marked" : "unmarked";

            msg = "Recipe " + state.recipes[index].recipe_name + " has been " + mark + " as favourite";

            const sats = [...state.recipes[index].recipe_customer_satisfactions.filter(satisfaction => satisfaction.customer_satisfaction_author !== (props.name + " " + props.surname)), sat];

            const recipe = {...state.recipes[index]};
            recipe.recipe_customer_satisfactions = sats;

            let recipe_index = state.recipes.findIndex(currentRecipe => currentRecipe.recipe_id === recipe.recipe_id);

            const recipes = [...state.recipes];
            recipes.splice(recipe_index, 1, recipe);

            await setState({
                ...state,
                recipes: recipes,
                msg: msg
            });

            handleOpen();

        }

    };

    const handleShare = async (event, index) => {
        event.cancelBubble = true;
        if (event.stopPropagation) event.stopPropagation();

        const recipeId = state.recipes[index].recipe_id;

        const response = await http.get("/api/recipes/share/" + recipeId);

        let msg = response.data.message;

        if (!(response.data.message.endsWith("has been shared") || response.data.message.endsWith("has been unshared"))) {
            console.error(msg);

            setState({
                ...state,
                "msg": msg
            });

            handleOpen();

        } else {

            const recipe_index = state.recipes.findIndex(currentRecipe => currentRecipe.recipe_id === recipeId);
            let currentRecipe = state.recipes[recipe_index];

            const recipe = {...currentRecipe};

            recipe.recipe_shared = !recipe.recipe_shared;

            const recipe_shared = recipe.recipe_shared ? "shared" : "unshared";

            msg = "Recipe " + currentRecipe.recipe_name + " has been " + recipe_shared;

            const recipes = [...state.recipes];
            recipes.splice(recipe_index, 1, recipe);

            await setState({
                ...state,
                recipes: recipes,
                msg: msg
            });

            handleOpen();

        }
    };

    const handleAddToCollection = async (event, recipeId) => {
        event.cancelBubble = true;
        if (event.stopPropagation) event.stopPropagation();

        const response = await http.get("/api/recipes/addToCollection/" + recipeId);

        let msg = response.data.message;

        if (!(response.data.message.endsWith("has been added to collection") || response.data.message.endsWith("has been removed from collection"))) {
            console.error(msg);

            setState({
                ...state,
                "msg": msg
            });

            handleOpen();

        } else {

            const recipe_index = state.recipes.findIndex(currentRecipe => currentRecipe.recipe_id === recipeId);
            let currentRecipe = state.recipes[recipe_index];

            const recipe = {...currentRecipe};

            recipe.in_collection = !recipe.in_collection;

            const in_collection = recipe.in_collection ? "added to" : "removed from";

            msg = "Recipe " + currentRecipe.recipe_name + " has been " + in_collection + " collection";

            const recipes = [...state.recipes];
            recipes.splice(recipe_index, 1, recipe);

            await setState({
                ...state,
                recipes: recipes,
                msg: msg
            });

            handleOpen();

        }
    };

    const handleGeneralRating = (index) => {

        let ratings = state.recipes[index].recipe_customer_satisfactions;

        if (!ratings) {
            return 0;
        }

        const ratings_length = ratings.filter(rating => rating.customer_satisfaction_rating != null).length;

        if (ratings_length < 1) {
            return 0;
        }

        let average_rating = ratings.flatMap(rating => rating.customer_satisfaction_rating).reduce((r1, r2) => r1 + r2) / ratings_length;
        return Math.round(average_rating * 1000) / 1000;
    };

    const handlePersonalRating = (index) => {

        let satisfaction = state.recipes[index].recipe_customer_satisfactions;

        if (!satisfaction)
            return 0;

        satisfaction = satisfaction.filter(satisfaction => satisfaction.customer_satisfaction_author === props.name + " " + props.surname);

        if (!satisfaction || !satisfaction[0] || satisfaction[0].customer_satisfaction_rating == null)
            return 0;

        return satisfaction[0].customer_satisfaction_rating;
    };

    const handlePersonalRatingEdit = async (event, index, newValue) => {

        const recipeId = state.recipes[index].recipe_id;

        if (recipeId == null) {
            console.error("recipeId cannot be null");
            return;
        }

        let response = await http.put("/api/recipes/rate/" + recipeId + "/" + newValue);

        let msg = response.data.message;

        if (!msg.startsWith("Recipe with id")) {
            console.error(msg);
            await setState({
                ...state,
                msg: msg
            });

            handleOpen();

        } else {
            msg = "Recipe " + state.recipes[index].recipe_name + " has been rated as " + newValue;

            let sat = {
                customer_satisfaction_author: props.name + " " + props.surname,
                customer_satisfaction_rating: null,
                customer_satisfaction_favourite: false
            };

            let currentSatisfaction = state.recipes[index].recipe_customer_satisfactions.find(satisfaction => satisfaction.customer_satisfaction_author === (props.name + " " + props.surname));

            console.log(currentSatisfaction);

            if (currentSatisfaction) {
                sat = {
                    ...currentSatisfaction
                };
            }

            console.log(sat);

            sat.customer_satisfaction_rating = newValue;

            const sats = [...state.recipes[index].recipe_customer_satisfactions.filter(satisfaction => satisfaction.customer_satisfaction_author !== (props.name + " " + props.surname)), sat];

            const recipe = {...state.recipes[index]};
            recipe.recipe_customer_satisfactions = sats;

            let recipe_index = state.recipes.findIndex(currentRecipe => currentRecipe.recipe_id === recipe.recipe_id);

            const recipes = [...state.recipes];
            recipes.splice(recipe_index, 1, recipe);

            await setState({
                ...state,
                recipes: recipes,
                msg: msg
            });

            handleOpen();

        }
    };

    const handleOpenPreferenceSuitabilityPopup = (event, suitabilityRecipeId, suitabilityRecipeName) => {
        event.cancelBubble = true;
        if (event.stopPropagation) event.stopPropagation();
        setState({
            ...state,
            open_preference_suitability_popup: true,
            suitabilityRecipeId: suitabilityRecipeId,
            suitabilityRecipeName: suitabilityRecipeName
        });
    };

    const handleClosePreferenceSuitabilityPopup = (event) => {
        setState({
            ...state,
            open_preference_suitability_popup: false
        });

    };

    const handleOperationMessage = async (message, reload) => {

        console.log(message);

        setState({
            ...state,
            msg: message,
            open_viewer_modal: false,
            open_confirmation_modal: false
        });

        handleOpen();

        if (reload) {
            setTimeout(() => history.push("/recipes"),1000);
        }
    };

    const handlePrevRecipe = () => {

        let new_index = state.recipe_index;
        let index = new_index - 1;

        while (true) {

            if (index < 0) {
                index = state.recipes.length;
            }

            if (state.recipes[index]) {
                new_index = index;
                break;
            }
            index--;
        }

        history.push('/recipes/' + state.recipes[new_index].recipe_id + '/' + mode);

    };

    const handleNextRecipe = () => {

        let new_index = state.recipe_index;
        let index = new_index + 1;

        while (true) {

            if (index > state.recipes.length) {
                index = 0;
            }

            if (state.recipes[index]) {
                new_index = index;
                break;
            }
            index++;
        }

        history.push('/recipes/' + state.recipes[new_index].recipe_id + '/' + mode);

    };

    const handleSearch = async () => {

        recipes_parameters.all = "no";
        recipes_parameters.groupNumber = state.groupNumber;
        recipes_parameters.phrase = state.searchPhrase;
        let response;

        console.log(recipes_parameters.groupNumber);
        console.log(recipes_parameters.phrase);

        try {
            if (recipes_parameters.recipesGroup === "personal" || recipes_parameters.recipesGroup === "shared" || recipes_parameters.recipesGroup === "favourite") {
                response = await http.post("/api/recipes/getRecipesSuitabilities", recipes_parameters)
            } else {
                response = await http.post("/api/recipes", recipes_parameters)
            }
            let table = [];

            for (let x in response.data) {
                table[x] = await createRecipe(response.data, x, recipes_parameters.recipesGroup);
            }

            console.log(table);
            let recipes = state.groupNumber === 0 ? [...table] : [...state.recipes, ...table];
            console.log(recipes);

            let recipe_index = 'new';

            if (recipeId !== 'new' && (mode === 'view' || mode === 'edit')) {

                for (let i = 0; i < recipes.length; i++) {
                    if (recipes[i]) {
                        if (recipes[i].recipe_id === Number(recipeId)) {
                            recipe_index = i;
                            console.log(recipe_index);
                        }
                    }
                }

                // let resp = await http.get("/api/recipes/checkRecipeApprovalStatus/" + recipeId);
                // let approvalStatus = resp.data.message;
                //
                // if (recipe_index === 'new') {
                //     switch (approvalStatus) {
                //         case 'accepted':
                //             document.getElementById('first').click();
                //             break;
                //         case 'pending':
                //             document.getElementById('second').click();
                //             break;
                //         case 'rejected':
                //             if (props.adminMode) {
                //                 document.getElementById('third').click();
                //             } else {
                //                 document.getElementById('second').click();
                //             }
                //             break;
                //     }
                // }
            }

            if ((!Number.isInteger(Number(recipeId)) && recipeId !== 'new') || (mode !== 'view' && mode !== 'edit')) {
                history.push('/recipes');
            }

            setState({
                ...state,
                recipes: recipes,
                loaded: true,
                recipe_index: recipe_index,
                groupNumber: state.groupNumber + 1,
                open_viewer_modal: recipeId && mode && state.msg === "" ? true : false
            });

        } catch (error) {
            console.log(error);
        }

    };

    let tab = null;
    if (props.admin === true && props.adminMode === true) {
        tab = <div className="unconfirmed_recipes_list">
            {state.recipes.map((recipe, index) => (
                <div key={index}>
                    <div id={"recipe" + recipe.recipe_id} className="unconfirmed_recipe">
                        <div className="recipe">
                            <div className="recipe_header">
                                <div className="recipe_name" onClick={event => handleRecipe(event, recipe.recipe_id)}>
                                    {recipe.recipe_name}
                                </div>
                                <div className="recipe_buttons">
                                    {state.recipes[index].recipe_author_id === props.userId ?
                                        <Tooltip title="Remove" aria-label="remove">
                                            <IconButton aria-label="remove" className="recipe_icon_button"
                                                        onClick={event => handleRemove(event, index)}>
                                                <DeleteIcon fontSize="small"/>
                                            </IconButton>
                                        </Tooltip> : null}
                                    {state.recipes[index].recipe_author_id === props.userId ?
                                        <Tooltip title="Edit" aria-label="edit">
                                            <IconButton type="button" aria-label="edit" className="recipe_icon_button"
                                                        onClick={(event) => handleEdit(event, recipe.recipe_id)}>
                                                <EditIcon fontSize="small"/>
                                            </IconButton>
                                        </Tooltip> : null}
                                </div>
                            </div>
                            <div className="creation_date" onClick={event => handleRecipe(event, recipe.recipe_id)}>
                                {"created " + recipe.creation_date}
                            </div>

                            <div className="recipe_content">
                                <div className="recipe_image_container"
                                     onClick={event => handleRecipe(event, recipe.recipe_id)}>
                                    {recipe.recipe_image !== "" ?
                                        <img src={recipe.recipe_image} alt={recipe.recipe_name}
                                             className="recipe_image"/> :
                                        <ReceiptIcon className={classes.photo_placeholder}/>}
                                </div>
                                <div className="recipe_description">
                                    {/*<div className="recipe_category">*/}
                                    {/*<Chip*/}
                                    {/*name="category"*/}
                                    {/*size="small"*/}
                                    {/*avatar={<CategoryIcon/>}*/}
                                    {/*label={recipe.recipe_category}*/}
                                    {/*onClick={handleCategory}*/}
                                    {/*/>*/}
                                    {/*</div>*/}
                                    <div className="recipe_author" onClick={handleAuthor}>
                                        <Avatar src={recipe.recipe_author_image}/>
                                        <div className="recipe_author_name">{recipe.recipe_author}</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="recipe_status">
                            <div className="unconfirmed_header">Status</div>
                            <Divider variant="fullWidth"/>
                            <div
                                className={recipe.approval_status === "accepted" ? "accepted_recipe unconfirmed_body" : (recipe.approval_status === "pending" ? "pending_recipe unconfirmed_body" : "rejected_recipe unconfirmed_body")}>
                                {recipe.approval_status.toUpperCase()}
                            </div>
                        </div>
                        {recipe.approval_status === "pending"
                            ?
                            <div className="assessment_details">
                                <div>
                                    <Button name="accept" variant="contained" className="accept_button"
                                            onClick={(event) => handleAssess(event, recipe.recipe_id, recipe.recipe_name)}>Accept</Button>
                                </div>
                                <div>
                                    <Button name="reject" variant="contained" className="reject_button"
                                            onClick={event => handleAssess(event, recipe.recipe_id, recipe.recipe_name)}>Reject</Button>
                                </div>

                            </div>
                            :
                            <div className="assessment_details">
                                <div className="assessment_date">
                                    <div className="unconfirmed_header">Assessment Date</div>
                                    <Divider variant="fullWidth"/>
                                    <div className="unconfirmed_body">{recipe.assessment_date}</div>
                                </div>
                                {recipe.approval_status !== "accepted"
                                    ?
                                    <div className="reject_explanation">
                                        <div className="unconfirmed_header">Rejection explanation</div>
                                        <Divider variant="fullWidth"/>
                                        <div className="unconfirmed_body">{recipe.reject_explanation}</div>
                                    </div>
                                    :
                                    null}
                            </div>
                        }
                    </div>
                    <Divider variant="middle"/>
                </div>
            ))}

            {state.recipes.length > 0 && state.recipes.length % 10 === 0 ?
                <Button className={classes.loadMore} variant="contained" color="primary" type="button"
                        onClick={() => handleSearch()}>Load more</Button> : null}

            <ConfirmationDialog
                classes={{
                    paper: classes.paper,
                }}
                id="confirmation_popup"
                type="recipe"
                open={state.open_confirmation_modal && state.msg === ''}
                onClose={handleCloseConfirmationPopup}
                complement={state.complement}
                itemId={state.confirmation_recipe_id}
                itemName={state.confirmation_recipe_name}
                handleOperationMessage={handleOperationMessage}
            />
        </div>;

    } else {
        switch (state.recipes_group) {
            case 0:
            case 1:
            case 3:
                tab = <div className="recipes_list">
                    <Grid container>
                        {state.recipes.map((recipe, index) => (
                            <Grid item key={index} id={"recipe" + recipe.recipe_id} className="recipe">
                                <div className="recipe_header">
                                    <div className="check_icons"
                                         onClick={event => handleOpenPreferenceSuitabilityPopup(event, recipe.recipe_id, recipe.recipe_name)}>
                                        <div className="check_passed">
                                            {recipe.positiveSuitabilities} <CheckIcon fontSize="small"/>
                                        </div>
                                        <div className="check_failed">
                                            {recipe.negativeSuitabilities} <ClearIcon fontSize="small"/>
                                        </div>
                                    </div>
                                    <div className="recipe_name"
                                         onClick={event => handleRecipe(event, recipe.recipe_id)}>
                                        {recipe.recipe_name}
                                        <div className="creation_date"
                                             onClick={event => handleRecipe(event, recipe.recipe_id)}>
                                            {"created " + recipe.creation_date}
                                        </div>
                                    </div>
                                    <div className="recipe_buttons">
                                        {handleFavouriteIcon(index)}
                                        {state.recipes[index].recipe_author_id === props.userId ?
                                            <Tooltip title="Remove" aria-label="remove">
                                                <IconButton aria-label="remove" className="recipe_icon_button"
                                                            onClick={event => handleRemove(event, index)}>
                                                    <DeleteIcon fontSize="small"/>
                                                </IconButton>
                                            </Tooltip> : null}
                                        {state.recipes[index].recipe_author_id === props.userId ?
                                            <Tooltip title="Edit" aria-label="edit">
                                                <IconButton type="button" aria-label="edit"
                                                            className="recipe_icon_button"
                                                            onClick={(event) => handleEdit(event, recipe.recipe_id)}>
                                                    <EditIcon fontSize="small"/>
                                                </IconButton>
                                            </Tooltip> : null}
                                        {state.recipes[index].recipe_author_id !== props.userId ? <Tooltip
                                            title={recipe.in_collection ? "Remove from collection" : "Add to collection"}
                                            aria-label={recipe.in_collection ? "Remove from collection" : "Add to collection"}>
                                            <IconButton type="button"
                                                        aria-label={recipe.in_collection ? "Remove from collection" : "Add to collection"}
                                                        className="recipe_icon_button"
                                                        onClick={(event) => handleAddToCollection(event, recipe.recipe_id)}
                                            >
                                                {recipe.in_collection ? <CloseRoundedIcon fontSize="small"/> :
                                                    <ArrowDownwardRoundedIcon fontSize="small"/>}
                                            </IconButton>
                                        </Tooltip> : null}

                                    </div>
                                </div>

                                <div className="recipe_content">
                                    <div className="recipe_image_container"
                                         onClick={event => handleRecipe(event, recipe.recipe_id)}>
                                        {recipe.recipe_image !== "" ?
                                            <img src={recipe.recipe_image} alt={recipe.recipe_name}
                                                 className="recipe_image"/> :
                                            <ReceiptIcon className={classes.photo_placeholder}/>}
                                    </div>
                                    <div className="recipe_description">
                                        {/*<div className="recipe_category">*/}
                                        {/*<Chip*/}
                                        {/*name="category"*/}
                                        {/*size="small"*/}
                                        {/*avatar={<CategoryIcon/>}*/}
                                        {/*label={recipe.recipe_category}*/}
                                        {/*onClick={handleCategory}*/}
                                        {/*/>*/}
                                        {/*</div>*/}
                                        <div className="recipe_author" onClick={handleAuthor}>
                                            <Avatar src={recipe.recipe_author_image}/>
                                            <div className="recipe_author_name">{recipe.recipe_author}</div>
                                        </div>
                                        <div className="recipe_actions">
                                            <div className="recipe_ratings_header">
                                                <div className="recipe_ratings">
                                                    General: {handleGeneralRating(index)} ({state.recipes[index].recipe_customer_satisfactions.filter(rating => rating.customer_satisfaction_rating != null) ? state.recipes[index].recipe_customer_satisfactions.filter(rating => rating.customer_satisfaction_rating != null).length : 0} {state.recipes[index].recipe_customer_satisfactions.filter(rating => rating.customer_satisfaction_rating != null) ? (state.recipes[index].recipe_customer_satisfactions.filter(rating => rating.customer_satisfaction_rating != null).length === 1 ? "rating" : "ratings") : "ratings"})
                                                </div>
                                                <Rating name="read-only" value={handleGeneralRating(index)}
                                                        precision={0.1} readOnly/>
                                                <div className="recipe_ratings_header">
                                                    Personal rating
                                                </div>
                                                <Tooltip
                                                    title={"Value: " + state.hover_rating !== -1 ? state.hover_rating : handlePersonalRating(index)}
                                                    aria-label="rate" placement="top">
                                                    <Rating name={`rating${index}`} value={handlePersonalRating(index)}
                                                            precision={0.1}
                                                            onChange={(event, value) => handlePersonalRatingEdit(event, index, value)}
                                                            onChangeActive={(event, newHover) => {
                                                                // console.log(index);
                                                                // console.log(recipe);
                                                                setState({...state, "hover_rating": newHover})
                                                            }}/>
                                                </Tooltip>
                                            </div>
                                            {state.recipes[index].recipe_shared ? <Tooltip
                                                    title={state.recipes[index].recipe_author_id === props.userId ? "Cancel sharing" : "Only recipe owner can share recipe"}
                                                    aria-label="Cancel sharing recipe"><Button variant="contained"
                                                                                               className="shared_button"
                                                                                               size="medium"
                                                                                               onClick={event => handleShare(event, index)}
                                                                                               disabled={state.recipes[index].recipe_author_id !== props.userId}>Shared</Button></Tooltip> :
                                                <Tooltip title="Share recipe" aria-label="Share"><Button
                                                    variant="contained" color="primary" size="medium"
                                                    onClick={event => handleShare(event, index)}>Share</Button></Tooltip>}

                                        </div>
                                    </div>

                                </div>

                            </Grid>
                        ))}
                    </Grid>

                    {state.recipes.length > 0 && state.recipes.length % 10 === 0 ?
                        <Button className={classes.loadMore} variant="contained" color="primary" type="button"
                                onClick={() => handleSearch()}>Load more</Button> : null}

                    {state.loaded && state.suitabilityRecipeId && <SuitableRecipeModal
                        open={state.open_preference_suitability_popup}
                        onClose={handleClosePreferenceSuitabilityPopup}
                        recipeId={state.suitabilityRecipeId}
                        recipeName={state.suitabilityRecipeName}
                    />}
                </div>;
                break;
            case 2:
                tab = <div className="unconfirmed_recipes_list">
                    {state.recipes.map((recipe, index) => (
                        <div key={index}>
                            <div id={"recipe" + recipe.recipe_id} className="unconfirmed_recipe">
                                <div className="recipe">
                                    <div className="recipe_header">
                                        <div className="recipe_name"
                                             onClick={event => handleRecipe(event, recipe.recipe_id)}>
                                            {recipe.recipe_name}
                                        </div>
                                        <div className="recipe_buttons">
                                            {state.recipes[index].recipe_author_id === props.userId ?
                                                <Tooltip title="Remove" aria-label="remove">
                                                    <IconButton aria-label="remove" className="recipe_icon_button"
                                                                onClick={event => handleRemove(event, index)}>
                                                        <DeleteIcon fontSize="small"/>
                                                    </IconButton>
                                                </Tooltip> : null}
                                            {state.recipes[index].recipe_author_id === props.userId ?
                                                <Tooltip title="Edit" aria-label="edit">
                                                    <IconButton type="button" aria-label="edit"
                                                                className="recipe_icon_button"
                                                                onClick={(event) => handleEdit(event, recipe.recipe_id)}>
                                                        <EditIcon fontSize="small"/>
                                                    </IconButton>
                                                </Tooltip> : null}
                                        </div>
                                    </div>
                                    <div className="creation_date"
                                         onClick={event => handleRecipe(event, recipe.recipe_id)}>
                                        {"created " + recipe.creation_date}
                                    </div>

                                    <div className="recipe_content">
                                        <div className="recipe_image_container"
                                             onClick={event => handleRecipe(event, recipe.recipe_id)}>
                                            {recipe.recipe_image !== "" ?
                                                <img src={recipe.recipe_image} alt={recipe.recipe_name}
                                                     className="recipe_image"/> :
                                                <ReceiptIcon className={classes.photo_placeholder}/>}
                                        </div>
                                        <div className="recipe_description">
                                            {/*<div className="recipe_category">*/}
                                            {/*<Chip*/}
                                            {/*name="category"*/}
                                            {/*size="small"*/}
                                            {/*avatar={<CategoryIcon/>}*/}
                                            {/*label={recipe.recipe_category}*/}
                                            {/*onClick={handleCategory}*/}
                                            {/*/>*/}
                                            {/*</div>*/}
                                            <div className="recipe_author" onClick={handleAuthor}>
                                                <Avatar src={recipe.recipe_author_image}/>
                                                <div className="recipe_author_name">{recipe.recipe_author}</div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div className="recipe_status">
                                    <div className="unconfirmed_header">Status</div>
                                    <Divider variant="fullWidth"/>
                                    <div
                                        className={recipe.approval_status === "pending" ? "pending_recipe unconfirmed_body" : "rejected_recipe unconfirmed_body"}>
                                        {recipe.approval_status.toUpperCase()}
                                    </div>
                                </div>
                                {recipe.approval_status === "pending"
                                    ?
                                    <div className="assessment_details">
                                        This product has not been assessed yet
                                    </div>
                                    :
                                    <div className="assessment_details">
                                        <div className="assessment_date">
                                            <div className="unconfirmed_header">Assessment Date</div>
                                            <Divider variant="fullWidth"/>
                                            <div className="unconfirmed_body">{recipe.assessment_date}</div>
                                        </div>
                                        <div className="reject_explanation">
                                            <div className="unconfirmed_header">Rejection explanation</div>
                                            <Divider variant="fullWidth"/>
                                            <div className="unconfirmed_body">{recipe.reject_explanation}</div>
                                        </div>
                                    </div>
                                }
                            </div>
                            <Divider variant="middle"/>
                        </div>
                    ))}

                    {state.recipes.length > 0 && state.recipes.length % 10 === 0 ?
                        <Button className={classes.loadMore} variant="contained" color="primary" type="button"
                                onClick={() => handleSearch()}>Load more</Button> : null}
                </div>;
                break;
        }

    }

    return (
        <Container id="main_container" maxWidth="lg">
            <div
                className={(state.open_viewer_modal || state.open_confirmation_modal) && state.msg === '' ? "background_blur page_container" : "page_container"}>
                <h2>Recipes</h2>
                <div className="toolbar_container">

                    {props.admin === true && props.adminMode === true ?
                        <Tabs
                            name="recipes_group"
                            value={state.recipes_group}
                            indicatorColor="primary"
                            textColor="inherit"
                            onChange={handleTab}
                            aria-label="recipe groups buttons"
                        >
                            <Tab className="recipe_group_tab" label="Accepted"/>
                            <Tab className="recipe_group_tab" label="Pending"/>
                            <Tab className="recipe_group_tab" label="Rejected"/>
                        </Tabs>
                        :
                        <Tabs
                            name="recipes_group"
                            value={state.recipes_group}
                            indicatorColor="primary"
                            textColor="inherit"
                            onChange={handleTab}
                            aria-label="recipe groups buttons"
                        >
                            <Tab className="recipe_group_tab" label="Personal"/>
                            <Tab className="recipe_group_tab" label="Shared"/>
                            <Tab className="recipe_group_tab" label="Unconfirmed"/>
                            <Tab className="recipe_group_tab" label="Favourite"/>
                        </Tabs>
                    }
                    <div className={classes.searchContainer}>
                        <FormControl variant="filled">
                            <InputLabel htmlFor="searchPhrase" className="search_input">Type recipe name</InputLabel>
                            <FilledInput
                                id="searchPhrase"
                                name="searchPhrase"
                                className="search_input"
                                value={state.searchPhrase}
                                onChange={handleChange}
                            />
                        </FormControl>
                        <Button className={classes.searchButton} variant="contained" color="primary" type="button"
                                onClick={() => handleSearch()}>Search</Button>
                    </div>
                    <Fab className="add_button" aria-label="add" onClick={handleAddNewProduct}>
                        <AddIcon/>
                    </Fab>
                </div>
                {state.msg !== "" ? <Snackbar
                    anchorOrigin={{vertical: 'top', horizontal: 'center'}}
                    open={open}
                    autoHideDuration={5000}
                    onClose={handleClose}
                    message={state.msg}
                    action={action}
                /> : null}
                {/*{state.msg !== "" ? <div className="msg">{state.msg}</div> : null}*/}
                {state.recipes.length === 0 ?
                    <div className="loading">{!state.loaded ? "Loading" : "No recipes found"}</div> : null}
                {tab}
                {recipeId && state.recipes.length >= 0 && mode && <ViewerModal
                    open={state.open_viewer_modal}
                    onClose={handleCloseViewerModal}
                    type="recipe"
                    item_index={state.recipe_index}
                    items={state.recipes}
                    mode={mode}
                    loggedInStatus={props.loggedInStatus}
                    handleOperationMessage={handleOperationMessage}
                    handlePrevProduct={handlePrevRecipe}
                    handleNextProduct={handleNextRecipe}
                    header={recipes_parameters.recipesGroup}
                    name={props.name}
                    surname={props.surname}
                    userId={props.userId}
                />}
            </div>
        </Container>
    );

}