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

const useStyles = makeStyles({
    paper: {
        width: '80%',
        maxHeight: 435,
    }
});

export default function Recipes(props) {

    const history = useHistory();

    const classes = useStyles();

    let { recipeId, mode } = useParams();

    console.log("recipeId " + recipeId);
    console.log("mode " + mode);

    const [state, setState] = React.useState({
        search: '',
        recipes_group: 0,//0: personal, 1: shared, 2: unconfirmed
        recipes: [{"recipe_id": 1, "recipe_name": "Salmon with green beans and bacon", "recipe_author": "John Smith", "recipe_favourite": false, "recipe_shared": false, "recipe_image": "http://localhost:8097/api/images/get/recipe/recipe1.jpg", "creation_date": "7.08.2021 21:11:14", "assessment_date": "9.08.2021 21:11:14", "rejectExplanation": "", "approval_status": "accepted", "ratings": [{"rating_author": "John Smith", "rating_value": 0.2}, {"rating_author": "Derek Johnson", "rating_value": 3.5}, {"rating_author": "Kate Bell", "rating_value": 2.0}, {"rating_author": "Jessica Wells", "rating_value": 5.0}], "ingredients": [{"ingredient_name": "salmon fillet", "ingredient_quantity": 4, "ingredient_unit": "pcs"}, {"ingredient_name": "green beans", "ingredient_quantity": 450, "ingredient_unit": "g"}, {"ingredient_name": "butter", "ingredient_quantity": 2, "ingredient_unit": "tablespoons"}, {"ingredient_name": "smoked bacon", "ingredient_quantity": 2, "ingredient_unit": "slices"}, {"ingredient_name": "breadcrumbs", "ingredient_quantity": 2, "ingredient_unit": "tablespoons"}, {"ingredient_name": "olive oil", "ingredient_quantity": 3, "ingredient_unit": "tablespoons"}, {"ingredient_name": "water", "ingredient_quantity": 3, "ingredient_unit": "tablespoons"}], "steps": [{"step_number": 1, "step_name": "Cut each salmon fillet into 4 pieces and place them in ovenproof dish lined with parchment. Pour it some olive oil. Leave it for several minutes, then roast at 160 Celsius degrees for 15 minutes."}, {"step_number": 2, "step_name": "Cube smoked bacon and fry it up on the frying pan."}, {"step_number": 3, "step_name": "Blanch green beans, then strain it and add to smoked bacon. Stop frying after 1 minute."}, {"step_number": 4, "step_name": "Add butter to green beans and fry it for a few minutes."}, {"step_number": 5, "step_name": "Before you finish frying sprinkle its content with breadcrumbs. Serve with roasted salmon. You might append boiled rice."}]}],
        hover_rating: -1,
        msg: "",
        loaded: false,
        open_confirmation_modal: false,
        complement: "",
        confirmation_recipe_id: null,
        confirmation_recipe_name: null,
        open_preference_suitability_popup: false,
        open_recipe_details_popup: false
    });

    useEffect(
        () => {
            setState({
                ...state,
                loaded: true
            });
        }, [state.msg, recipeId, mode]
    );

    const handleTab = (event, newValue) => {
        setState({
            ...state,
            recipes_group: newValue,
        });
    };

    const handleChange = (event) => {
        setState({
            ...state,
            [event.target.name]: event.target.value,
        });
    };

    const handleRecipe = (event, recipe_id) => {
        console.log(event);
        console.log("recipe");
        // let destination = "/products/" + recipe_id + "/view";
        // props.history.push(destination);
    };

    const handleAuthor = (event) => {
        console.info("Author");
    };

    const handleEdit = (event, product_id) => {
        event.cancelBubble = true;
        if (event.stopPropagation) event.stopPropagation();
        props.history.push('/products/' + product_id + '/edit');
    };

    const handleAddNewProduct = () => {
        props.history.push('/products/new/edit');
    };

    const handleRemove = (event, index) => {
        event.cancelBubble = true;
        if (event.stopPropagation) event.stopPropagation();
        const product_id = state.products[index].product_id;
        const product_name = state.products[index].product_name;

        http.delete("/api/products/remove/" + product_id)
            .then(resp => {
                if (resp.data.message !== "Product " + product_name + " has been removed successfully") {
                    setState({
                        ...state,
                        "msg": resp.data.message
                    });
                } else {

                    props.history.push("/products/" + product_name + "-removed")
                }
            })
            .catch(error => console.log(error));
    };

    const handleAssess = (event, product_id, product_name) => {
        // event.cancelBubble = true;
        // if (event.stopPropagation) event.stopPropagation();
        const name = event.target.innerText;
        setState({
            ...state,
            complement: name === "REJECT" ? "reject this product" : "accept this product",
            open_confirmation_modal: true,
            confirmation_product_id: product_id,
            confirmation_product_name: product_name
        });
    };

    const handleCloseConfirmationPopup = () => {
        setState({
            ...state,
            open_confirmation_modal: false,
        });

    };

    const handleFavouriteIcon = (index) => {
        if (state.recipes[index].recipe_favourite) {
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

    const handleAddToFavourite = (event, index) => {
        event.cancelBubble = true;
        if (event.stopPropagation) event.stopPropagation();
        const recipe_favourite = !state.recipes[index].recipe_favourite;
        const recipe = {
            ...state.recipes[index],
            "recipe_favourite": recipe_favourite
        };
        setState({
            ...state,
            "recipes": [...state.recipes.slice(0, index), recipe, ...state.recipes.slice(index + 1)]
        });
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
        let ratings = state.recipes[index].ratings;
        let average_rating = ratings.flatMap(rating => rating.rating_value).reduce((r1, r2) => r1 + r2) / ratings.length;
        return Math.round(average_rating * 1000) / 1000;
    };

    const handlePersonalRating = (index) => {
        return state.recipes[index].ratings.find(rating => rating.rating_author === "John Smith").rating_value;
    };

    const handlePersonalRatingEdit = (event, index, newValue) => {
        console.log(newValue);
        state.recipes[index].ratings.find(rating => rating.rating_author === "John Smith").rating_value = newValue;

        setState({
            ...state,
            "msg": "Recipe " + state.recipes[index].recipe_name + " has been rated"
        });

        setTimeout(() => {
            setState({
                ...state,
                "msg": ""
            });
        }, 3000);

    };

    const handleOpenPreferenceSuitabilityPopup = (event) => {
        event.cancelBubble = true;
        if (event.stopPropagation) event.stopPropagation();
        console.log(event);
        setState({
            ...state,
            open_preference_suitability_popup: true,
        });
        console.log(state.open_preference_suitability_popup);
    };

    const handleClosePreferenceSuitabilityPopup = (event) => {
        console.log(event);
        setState({
            ...state,
            open_preference_suitability_popup: false,
        });
        console.log(state.open_preference_suitability_popup);

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
                                    <Tooltip title="Remove" aria-label="remove">
                                        <IconButton aria-label="remove" className="recipe_icon_button"
                                                    onClick={event => handleRemove(event, index)}>
                                            <DeleteIcon fontSize="small"/>
                                        </IconButton>
                                    </Tooltip>
                                    <Tooltip title="Edit" aria-label="edit">
                                        <IconButton type="button" aria-label="edit" className="recipe_icon_button"
                                                    onClick={(event) => handleEdit(event, recipe.recipe_id)}>
                                            <EditIcon fontSize="small"/>
                                        </IconButton>
                                    </Tooltip>
                                </div>
                            </div>
                            <div className="creation_date" onClick={event => handleRecipe(event, recipe.recipe_id)}>
                                {"created " + recipe.creation_date}
                            </div>

                            <div className="recipe_content">
                                <div className="recipe_image_container" onClick={event => handleRecipe(event, recipe.recipe_id)}>
                                    <img src={recipe.recipe_image} alt={recipe.recipe_name} className="recipe_image"/>
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
                                        <Avatar/>
                                        <div className="recipe_author_name">{recipe.recipe_author}</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="recipe_status">
                            <div className="unconfirmed_header">Status</div>
                            <Divider variant="fullWidth"/>
                            <div className={recipe.approval_status === "accepted"?"accepted_recipe unconfirmed_body":(recipe.approval_status === "pending"?"pending_recipe unconfirmed_body":"rejected_recipe unconfirmed_body")}>
                                {recipe.approval_status.toUpperCase()}
                            </div>
                        </div>
                        {recipe.approval_status === "pending"
                            ?
                            <div className="assessment_details">
                                <div>
                                    <Button name="accept" variant="contained" className="accept_button" onClick={(event) => handleAssess(event, recipe.recipe_id, recipe.recipe_name)}>Accept</Button>
                                </div>
                                <div>
                                    <Button name="reject" variant="contained" className="reject_button" onClick={event => handleAssess(event, recipe.recipe_id, recipe.recipe_name)}>Reject</Button>
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

            <ConfirmationDialog
                classes={{
                    paper: classes.paper,
                }}
                id="confirmation_popup"
                open={state.open_confirmation_modal}
                onClose={handleCloseConfirmationPopup}
                complement = {state.complement}
                productId = {state.confirmation_product_id}
                productName = {state.confirmation_product_name}
                history = {props.history}
            />
        </div>;

    } else {
        switch (state.recipes_group) {
            case 0:
            case 1:
                tab = <div className="recipes_list">
                    <Grid container>
                        {state.recipes.map((recipe, index) => (
                            <Grid item key={index} id={"recipe" + recipe.recipe_id} className="recipe">
                                <div className="recipe_header">
                                    <div className="check_icons" onClick={handleOpenPreferenceSuitabilityPopup}>
                                        <div className="check_passed">
                                            3 <CheckIcon fontSize="small"/>
                                        </div>
                                        <div className="check_failed">
                                            2 <ClearIcon fontSize="small"/>
                                        </div>
                                    </div>
                                    <div className="recipe_name" onClick={event => handleRecipe(event, recipe.recipe_id)}>
                                        {recipe.recipe_name}
                                        <div className="creation_date" onClick={event => handleRecipe(event, recipe.recipe_id)}>
                                            {"created " + recipe.creation_date}
                                        </div>
                                    </div>
                                    <div className="recipe_buttons">
                                        <Tooltip title="Remove" aria-label="remove">
                                            <IconButton aria-label="remove" className="recipe_icon_button"
                                                        onClick={event => handleRemove(event, index)}>
                                                <DeleteIcon fontSize="small"/>
                                            </IconButton>
                                        </Tooltip>
                                        <Tooltip title="Edit" aria-label="edit">
                                            <IconButton type="button" aria-label="edit" className="recipe_icon_button"
                                                        onClick={(event) => handleEdit(event, recipe.recipe_id)}>
                                                <EditIcon fontSize="small"/>
                                            </IconButton>
                                        </Tooltip>
                                        {handleFavouriteIcon(index)}
                                    </div>
                                </div>

                                <div className="recipe_content">
                                    <div className="recipe_image_container" onClick={event => handleRecipe(event, recipe.recipe_id)}>
                                        <img src={recipe.recipe_image} alt={recipe.recipe_name} className="recipe_image"/>
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
                                            <Avatar/>
                                            <div className="recipe_author_name">{recipe.recipe_author}</div>
                                        </div>
                                        <div className="recipe_actions">
                                            <div className="recipe_ratings_header">
                                                <div className="recipe_ratings">
                                                    General: {handleGeneralRating(index)} ({state.recipes[index].ratings.length} ratings)
                                                </div>
                                                    <Rating name="read-only" value={handleGeneralRating(index)} precision={0.1} readOnly/>
                                                <div className="recipe_ratings_header">
                                                    Personal rating
                                                </div>
                                                <Tooltip title={"Value: " + state.hover_rating !== -1 ? state.hover_rating : handlePersonalRating(index)} aria-label="rate" placement="top">
                                                    <Rating name="half-rating" value={handlePersonalRating(index)} precision={0.1} onChange={(event, newValue) => handlePersonalRatingEdit(event, index, newValue)} onChangeActive={(event, newHover) => setState({...state, "hover_rating": newHover})}/>
                                                </Tooltip>
                                            </div>
                                            {state.recipes[index].recipe_shared ? <Tooltip title="Cancel sharing" aria-label="Cancel sharing recipe"><Button variant="contained" className="shared_button" size="medium" onClick={event => handleShare(event, index)}>Shared</Button></Tooltip> : <Tooltip title="Share recipe" aria-label="Share"><Button variant="contained" color="primary" size="medium" onClick={event => handleShare(event, index)}>Share</Button></Tooltip>}

                                        </div>
                                    </div>

                                </div>

                            </Grid>
                        ))}
                    </Grid>

                    <SuitableRecipeModal
                        open={state.open_preference_suitability_popup}
                        onClose={handleClosePreferenceSuitabilityPopup}
                    />
                </div>;
                break;
            case 2:
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
                                            <Tooltip title="Remove" aria-label="remove">
                                                <IconButton aria-label="remove" className="recipe_icon_button"
                                                            onClick={event => handleRemove(event, index)}>
                                                    <DeleteIcon fontSize="small"/>
                                                </IconButton>
                                            </Tooltip>
                                            <Tooltip title="Edit" aria-label="edit">
                                                <IconButton type="button" aria-label="edit" className="recipe_icon_button"
                                                            onClick={(event) => handleEdit(event, recipe.recipe_id)}>
                                                    <EditIcon fontSize="small"/>
                                                </IconButton>
                                            </Tooltip>
                                        </div>
                                    </div>
                                    <div className="creation_date" onClick={event => handleRecipe(event, recipe.recipe_id)}>
                                        {"created " + recipe.creation_date}
                                    </div>

                                    <div className="recipe_content">
                                        <div className="recipe_image_container" onClick={event => handleRecipe(event, recipe.recipe_id)}>
                                            <img src={recipe.recipe_image} alt={recipe.recipe_name} className="recipe_image"/>
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
                                                <Avatar/>
                                                <div className="recipe_author_name">{recipe.recipe_author}</div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div className="recipe_status">
                                    <div className="unconfirmed_header">Status</div>
                                    <Divider variant="fullWidth"/>
                                    <div className={recipe.approval_status === "pending"?"pending_recipe unconfirmed_body":"rejected_recipe unconfirmed_body"}>
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

                </div>;
                break;
        }

    }

    return (
        <Container id="main_container" maxWidth="lg">
            <div className="page_container">
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
                        </Tabs>
                    }
                    <div>
                        <FormControl variant="filled">
                            <InputLabel htmlFor="search" className="search_input">Search</InputLabel>
                            <FilledInput
                                id="search"
                                name="search"
                                className="search_input"
                                placeholder="Type product name"
                                value={state.search}
                                onChange={handleChange}
                            />
                        </FormControl>
                        {/*<FormControl variant="filled" className={classes.formControl}>*/}
                            {/*<InputLabel id="category_select_label" className="category_select">Category</InputLabel>*/}
                            {/*<Select*/}
                                {/*labelId="category_select_label"*/}
                                {/*id="category_select"*/}
                                {/*className="category_select"*/}
                                {/*name="category"*/}
                                {/*value={state.category}*/}
                                {/*onChange={handleChange}*/}
                            {/*>*/}
                                {/*{state.categories.map((category, index) => (*/}
                                    {/*<MenuItem key={index} value={category.category_name}>{category.category_name}</MenuItem>*/}
                                {/*))}*/}
                            {/*</Select>*/}
                        {/*</FormControl>*/}
                    </div>
                    <Fab className="add_button" aria-label="add" onClick={handleAddNewProduct}>
                        <AddIcon/>
                    </Fab>
                </div>
                {state.msg !== "" ? <div className="msg">{state.msg}</div> : null}
                {!state.loaded ? <div className="loading">Loading</div> : null}
                {tab}
            </div>
        </Container>
    );

}