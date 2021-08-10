import React, {useEffect} from 'react'
import {Container, Divider, Grid, MenuItem, Tooltip, makeStyles} from "@material-ui/core";
import Tabs from "@material-ui/core/Tabs";
import Tab from "@material-ui/core/Tab";
import FormControl from "@material-ui/core/FormControl";
import InputLabel from '@material-ui/core/InputLabel';
import FilledInput from '@material-ui/core/FilledInput';
import Select from "@material-ui/core/Select/Select";
import Fab from "@material-ui/core/Fab";
import AddIcon from '@material-ui/icons/Add';
import Avatar from '@material-ui/core/Avatar';
import Chip from '@material-ui/core/Chip';
import IconButton from '@material-ui/core/IconButton';
import DeleteIcon from '@material-ui/icons/Delete';
import EditIcon from '@material-ui/icons/Edit';
import Button from "@material-ui/core/Button";
import ConfirmationDialog from "../components/ConfirmationDialog";
import http from "../http-common";

const useStyles = makeStyles({
    paper: {
        width: '80%',
        maxHeight: 435,
    },
});

export default function Recipes(props) {

    const classes = useStyles();

    const [state, setState] = React.useState({
        search: '',
        recipes_group: 0,//0: personal, 1: shared, 2: unconfirmed
        recipes: [{"recipe_id": 1, "recipe_name": "Salmon with green beans and bacon", "recipe_author": "Steve Kaminski", "recipe_image": "http://localhost:8097/api/images/get/recipe/recipe1.jpg", "creation_date": "7.08.2021 21:11:14", "assessment_date": "9.08.2021 21:11:14", "rejectExplanation": "", "approval_status": "accepted", "ingredients": [{"ingredient_name": "salmon fillet", "ingredient_quantity": 4, "ingredient_unit": "pcs"}, {"ingredient_name": "green beans", "ingredient_quantity": 450, "ingredient_unit": "g"}, {"ingredient_name": "butter", "ingredient_quantity": 2, "ingredient_unit": "tablespoons"}, {"ingredient_name": "smoked bacon", "ingredient_quantity": 2, "ingredient_unit": "slices"}, {"ingredient_name": "breadcrumbs", "ingredient_quantity": 2, "ingredient_unit": "tablespoons"}, {"ingredient_name": "olive oil", "ingredient_quantity": 3, "ingredient_unit": "tablespoons"}, {"ingredient_name": "water", "ingredient_quantity": 3, "ingredient_unit": "tablespoons"}], "steps": [{"step_number": 1, "step_name": "Cut each salmon fillet into 4 pieces and place them in ovenproof dish lined with parchment. Pour it some olive oil. Leave it for several minutes, then roast at 160 Celsius degrees for 15 minutes."}, {"step_number": 2, "step_name": "Cube smoked bacon and fry it up on the frying pan."}, {"step_number": 3, "step_name": "Blanch green beans, then strain it and add to smoked bacon. Stop frying after 1 minute."}, {"step_number": 4, "step_name": "Add butter to green beans and fry it for a few minutes."}, {"step_number": 5, "step_name": "Before you finish frying sprinkle its content with breadcrumbs. Serve with roasted salmon. You might append boiled rice."}]}],
        msg: "",
        loaded: false,
        open_confirmation_popup: false,
        complement: "",
        confirmation_recipe_id: null,
        confirmation_recipe_name: null,
    });

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
        let destination = "/products/" + recipe_id + "/view";
        props.history.push(destination);
    };

    const handleAuthor = (event) => {
        event.cancelBubble = true;
        if (event.stopPropagation) event.stopPropagation();
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
            open_confirmation_popup: true,
            confirmation_product_id: product_id,
            confirmation_product_name: product_name
        });
    };

    const handleCloseConfirmationPopup = () => {
        setState({
            ...state,
            open_confirmation_popup: false,
        });

    };

    let tab = null;

    if (props.admin === true && props.adminMode === true) {
        tab = <div className="unconfirmed_recipes_list">
            {state.recipes.map((recipe, index) => (
                <div key={index}>
                    <div id={"recipe" + recipe.recipe_id} className="unconfirmed_recipe">
                        <div className="recipe" onClick={event => handleRecipe(event, recipe.recipe_id)}>
                            {console.log("unconfirmed " + recipe)}
                            <div className="recipe_image_container">
                                <img src={recipe.recipe_image} alt={recipe.recipe_name} className="recipe_image"/>
                            </div>
                            <div className="recipe_description">
                                <div className="recipe_name">
                                    {recipe.recipe_name}
                                </div>
                                {/*<div className="recipe_category">*/}
                                    {/*<Chip*/}
                                        {/*name="category"*/}
                                        {/*size="small"*/}
                                        {/*avatar={<CategoryIcon/>}*/}
                                        {/*label={recipe.recipe_category}*/}
                                        {/*onClick={handleRecipe}*/}
                                    {/*/>*/}
                                {/*</div>*/}
                                <div className="recipe_author" onClick={handleAuthor}>
                                    <Avatar/>
                                    <div className="recipe_author_name">{recipe.recipe_author}</div>
                                </div>
                                <div>
                                    Creation date:
                                </div>
                                <div>
                                    {recipe.creation_date}
                                </div>
                            </div>
                            <div className="recipe_buttons">
                                <Tooltip title="Delete" aria-label="delete">
                                    <IconButton aria-label="delete" className="recipe_icon_button"
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
                                {/*{handleFavouriteIcon(product.product_id)}*/}
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
                open={state.open_confirmation_popup}
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
                            <Grid item key={index} id={"recipe" + recipe.recipe_id} className="recipe"
                                  onClick={event => handleRecipe(event, recipe.recipe_id)}>
                                {console.log("personal or shared" + recipe)}
                                <div className="recipe_image_container">
                                    <img src={recipe.recipe_image} alt={recipe.recipe_name} className="recipe_image"/>
                                </div>
                                <div className="recipe_description">
                                    <div className="recipe_name">
                                        {recipe.recipe_name}
                                    </div>
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
                                    <div>
                                        Creation date:
                                    </div>
                                    <div>
                                        {recipe.creation_date}
                                    </div>
                                </div>
                                <div className="product_buttons">
                                    <Tooltip title="Delete" aria-label="delete">
                                        <IconButton aria-label="delete" className="recipe_icon_button"
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
                                    {/*{handleFavouriteIcon(product.product_id)}*/}
                                </div>
                            </Grid>
                        ))}
                    </Grid>
                </div>;
                break;
            case 2:
                tab = <div className="unconfirmed_recipes_list">
                    {state.recipes.map((recipe, index) => (
                        <div key={index}>
                            <div id={"recipe" + recipe.recipe_id} className="unconfirmed_recipe">
                                <div className="recipe" onClick={event => handleRecipe(event, recipe.recipe_id)}>
                                    <div className="recipe_image_container">
                                        <img src={recipe.recipe_image} alt={recipe.recipe_name} className="recipe_image"/>
                                    </div>
                                    <div className="recipe_description">
                                        <div className="recipe_name">
                                            {recipe.recipe_name}
                                        </div>
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
                                        <div>
                                            Creation date:
                                        </div>
                                        <div>
                                            {recipe.creation_date}
                                        </div>
                                    </div>
                                    <div className="recipe_buttons">
                                        <Tooltip title="Delete" aria-label="delete">
                                            <IconButton aria-label="delete" className="recipe_icon_button"
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
                                        {/*{handleFavouriteIcon(product.product_id)}*/}
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
                <div className="msg"></div>
                <div className="loading">Loading</div>
                {tab}
            </div>
        </Container>
    );

}