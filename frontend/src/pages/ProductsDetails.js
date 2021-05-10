import React, {useEffect} from 'react'
import CategoryIcon from '@material-ui/icons/Category';
import { MenuItem, Container, makeStyles, Tooltip, Divider, Input, Grid} from '@material-ui/core';
import red_apple from "../images/red_apple.jpg"
import banana from "../images/banana.jpg"
import orange from "../images/orange.jpg"
import Avatar from '@material-ui/core/Avatar';
import Chip from '@material-ui/core/Chip';
import IconButton from '@material-ui/core/IconButton';
import Button from '@material-ui/core/Button';
import EditIcon from '@material-ui/icons/Edit';
import CloseIcon from '@material-ui/icons/Close';
import DeleteIcon from '@material-ui/icons/Delete';
import FavoriteBorderIcon from '@material-ui/icons/FavoriteBorder';
import FavoriteIcon from '@material-ui/icons/Favorite';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import FilledInput from '@material-ui/core/FilledInput';
import Select from '@material-ui/core/Select';
import InputLabel from '@material-ui/core/InputLabel';
import FormControl from '@material-ui/core/FormControl';
import Fab from '@material-ui/core/Fab';
import AddIcon from '@material-ui/icons/Add';
import { useHistory } from "react-router-dom";
import TextField from '@material-ui/core/TextField';
import InputAdornment from '@material-ui/core/InputAdornment';

const useStyles = makeStyles( {
    formControl: {
        minWidth:110,
    },
});

export default function ProductsDetails () {
    let history = useHistory();

    const classes = useStyles();

    const [state, setState] = React.useState({
        products_group: 0,//0: all, 1: new, 2: favourite
        mode: 'edit',
        products:[{product_id: 1, product_name: "Red apple", product_image: red_apple, product_category: "Fruit", product_author: "Daniel Fog", product_favourite: false},
            {product_id: 2, product_name: "Banana", product_image: banana, product_category: "Fruit", product_author: "Sara Bedrock", product_favourite: false},
            {product_id: 3, product_name: "Orange", product_image: orange, product_category: "Fruit", product_author: "Paul Weasley", product_favourite: false}],
        selected_product:[{product_id: 1, product_name: "Red apple", product_image: red_apple, product_category: "Fruit", product_author: "Daniel Fog", product_favourite: false}],
        product_nutrients: [{nutrient_name: "Calories",nutrient_amount: 20, nutrient_unit: "kcal"}, {nutrient_name: "Protein", nutrient_amount: 5, nutrient_unit: "mg/100g"}, {nutrient_name: "Carbohydrates", nutrient_amount: 12, nutrient_unit: "mg/100g"}, {nutrient_name: "Fat", nutrient_amount: 2, nutrient_unit: "mg/100g"}, {nutrient_name: "Salt", nutrient_amount: 0.4, nutrient_unit: "mg/100g"}],
        product_ingredients: [["Water", 85], ["Fructose", 10], ["Fibre and Pectin", 3.5]],
        product_add_ingredient: "",
    });

    let current_product, tab;

    if (state.mode === 'view') {
        current_product = <div id={state.selected_product[0].product_id} className="product product_selected">
            <div className="product_image_container product_selected_element">
                <img src={state.selected_product[0].product_image} alt={state.selected_product[0].product_name} className="product_image"/>
            </div>

            <div className="product_description product_selected_element">
                <div className="product_id">
                    <div className="product_name product_selected_element">
                        {state.selected_product[0].product_name}
                    </div>
                    <div className="product_category product_selected_element">
                        <Chip
                            name="category"
                            size="small"
                            avatar={<CategoryIcon/>}
                            label={state.selected_product[0].product_category}
                        />
                    </div>
                    <div className="product_author product_selected_element">
                        <Avatar/>
                        <div className="product_author_name">{state.selected_product[0].product_author}</div>
                    </div>
                </div>
                <div className="product_details product_selected_element">
                    <div className="nutrients">
                        <h4>Nutrients</h4>
                        { state.product_nutrients.map((nutrient, index) => (
                            <div key={index} className="nutrient">
                                <div className="nutrient_name">{nutrient.nutrient_name}:</div>
                                <div className="nutrient_amount">{nutrient.nutrient_amount}</div>
                                <div className="nutrient_unit">{nutrient.nutrient_unit}</div>
                            </div>
                        ))}
                    </div>
                    <Divider orientation="vertical" flexItem variant='middle'/>
                    <div className="ingredients">
                        <h4>Ingredients</h4>
                        { state.product_ingredients.map((ingredient, index) => (
                            <div key={index} className="ingredient">
                                <div className="ingredient_name">{ingredient[0]}:</div>
                                <div className="ingredient_amount">{ingredient[1]}</div>
                                <div className="ingredient_unit">%</div>
                            </div>
                        ))}
                    </div>
                </div>
            </div>
            <div className="product_buttons">
                <Tooltip title="Close" aria-label="close">
                    <IconButton aria-label="close" className="product_icon_button">
                        <CloseIcon fontSize="small"/>
                    </IconButton>
                </Tooltip>
                <Tooltip title="Delete" aria-label="delete">
                    <IconButton aria-label="delete" className="product_icon_button">
                        <DeleteIcon fontSize="small"/>
                    </IconButton>
                </Tooltip>
                <Tooltip title="Edit" aria-label="edit">
                    <IconButton aria-label="edit" className="product_icon_button">
                        <EditIcon fontSize="small"/>
                    </IconButton>
                </Tooltip>
            </div>
        </div>;
    } else {
        current_product = <div id={state.selected_product[0].product_id} className="product product_selected">
            <form className="product_edit_form" >
            <div className="product_image_container product_selected_element product_selected_element_moved">
                <Input
                    type="file"
                    name="product_image"
                    value={state.selected_product[0].product_image}
                ></Input>
                {/*<img src={state.selected_product[0].product_image} alt={state.selected_product[0].product_name} className="product_image"/>*/}
            </div>

            <div className="product_description product_selected_element product_selected_element_moved">
                <div className="product_id">
                    <div className="product_name product_selected_element">
                        <FormControl>
                            <InputLabel id="product_name_label" className="product_name">Product name</InputLabel>
                            <FilledInput
                                name="product_name"
                                className="product_name"
                                value={state.selected_product[0].product_name}
                        />
                        </FormControl>
                    </div>
                    <div className="product_category product_selected_element">
                        <FormControl variant="filled">
                            <InputLabel id="category_select_label" className="category_select">Category</InputLabel>
                            <Select
                                labelId="category_select_label"
                                id="category_select"
                                className="category_select"
                                name="product_category"
                                value={state.selected_product[0].product_category}
                                size="small"
                            >
                                <MenuItem value="Fruit">Fruit</MenuItem>
                                <MenuItem value="Vegetables">Vegetables</MenuItem>
                                <MenuItem value="Dairy">Dairy</MenuItem>
                            </Select>
                        </FormControl>
                    </div>
                    <div className="product_author product_selected_element">
                        <FormControl variant="filled">
                            <InputLabel id="author_select_label" className="author_select">Category</InputLabel>
                            <Select
                                labelId="author_select_label"
                                id="author_select"
                                className="author_select"
                                name="product_author"
                                value={state.selected_product[0].product_author}
                                size="small"
                            >
                                <MenuItem value="Daniel Fog">Daniel Fog</MenuItem>
                                <MenuItem value="Sara Bedrock">Sara Bedrock</MenuItem>
                                <MenuItem value="Paul Weasley">Paul Weasley</MenuItem>
                            </Select>
                        </FormControl>
                    </div>
                </div>
                <div className="product_details product_selected_element product_details_edit">
                    <div className="nutrients">
                        <h4>Nutrients</h4>
                        { state.product_nutrients.map((nutrient, index) => (
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
                                />
                            </div>
                        ))}
                    </div>
                    <Divider orientation="vertical" flexItem variant='middle'/>
                    <div className="ingredients">
                        <div id="ingredients_list">
                            <h4>Ingredients</h4>
                            { state.product_ingredients.map((ingredient, index) => (
                                <div key={index} className="ingredient_edit">
                                    <TextField
                                        name={ingredient[0]}
                                        label={ingredient[0]}
                                        type="number"
                                        variant="filled"
                                        value={ingredient[1]}
                                        size="small"
                                        InputProps={{
                                            endAdornment: <InputAdornment
                                                position="end">
                                                    <div>%</div>
                                                </InputAdornment>,
                                        }}
                                    />
                                    <Tooltip title="Delete ingredient" aria-label="delete ingredient">
                                        <IconButton aria-label="delete ingredient" className="product_icon_button">
                                            <DeleteIcon fontSize="small"/>
                                        </IconButton>
                                    </Tooltip>
                                </div>
                            ))}
                        </div>
                        <div className="add_ingredient">
                            <TextField
                                name="product_add_ingredient"
                                label="Type ingredient name"
                                variant="standard"
                                value={state.product_add_ingredient}
                                size="small"
                            />
                            <Tooltip title="Add ingredient" aria-label="add ingredient">
                                <IconButton aria-label="add ingredient" className="product_icon_button" disabled={state.product_add_ingredient === ""}>
                                    <AddIcon fontSize="large"/>
                                </IconButton>
                            </Tooltip>
                        </div>
                    </div>
                </div>
                <div>
                    <Button variant="text" color="inherit">Cancel</Button>
                    <Button variant="text" color="inherit" type="submit">Save</Button>
                </div>
            </div>
            <div className="product_buttons">
                <Tooltip title="Close" aria-label="close">
                    <IconButton aria-label="close" className="product_icon_button">
                        <CloseIcon fontSize="small"/>
                    </IconButton>
                </Tooltip>
                <Tooltip title="Delete" aria-label="delete">
                    <IconButton aria-label="delete" className="product_icon_button">
                        <DeleteIcon fontSize="small"/>
                    </IconButton>
                </Tooltip>
                <Tooltip title="Edit" aria-label="edit">
                    <IconButton aria-label="edit" className="product_icon_button edit_mode">
                        <EditIcon fontSize="small"/>
                    </IconButton>
                </Tooltip>
            </div>
            </form>
        </div>;
    }

    if (state.products_group === 0) {
        //retrieve all products and set its values to product state field
    } else if (state.products_group === 1) {
        //retrieve new products and set its values to product state field
    } else {
        //retrieve favourite products and set its values to product state field
    }

    tab = <Grid container className="products_list" spacing={1}>
        { state.products.map((product, index) => (
            <Grid item id={product.product_id} className="product">
                <div className="product_image_container">
                    <img src={product.product_image} alt={product.product_name} className="product_image"/>
                </div>
                <div className="product_description">
                    <div className="product_name">
                        {product.product_name}
                    </div>
                    <div className="product_category">
                        <Chip
                            name="category"
                            size="small"
                            avatar={<CategoryIcon/>}
                            label={product.product_category}
                        />
                    </div>
                    <div className="product_author">
                        <Avatar/>
                        <div className="product_author_name">{product.product_author}</div>
                    </div>
                </div>
                <div className="product_buttons">
                    <Tooltip title="Delete" aria-label="delete">
                        <IconButton aria-label="delete" className="product_icon_button">
                            <DeleteIcon fontSize="small"/>
                        </IconButton>
                    </Tooltip>
                    <Tooltip title="Edit" aria-label="edit">
                        <IconButton aria-label="edit" className="product_icon_button">
                            <EditIcon fontSize="small"/>
                        </IconButton>
                    </Tooltip>
                    <Tooltip title="Change favourite status" aria-label="Change favourite status">
                        <IconButton aria-label="Change favourite status" className="product_icon_button" disabled>
                        </IconButton>
                    </Tooltip>
                </div>
            </Grid>
        ))}
    </Grid>;

    return(
        <Container maxWidth="xl">
            {current_product}
            <div className="page_container background_blur">
                <h2>Products</h2>
                <div className="toolbar_container">
                    <Tabs
                        name="products_group"
                        value={state.products_group}
                        indicatorColor="primary"
                        textColor="inherit"
                        aria-label="product groups buttons"
                    >
                        <Tab className="product_group_tab" label="All" />
                        <Tab className="product_group_tab" label="New" />
                        <Tab className="product_group_tab" label="Favourite" />
                    </Tabs>
                    <div>
                        <FormControl variant="filled">
                            <InputLabel htmlFor="search" className="search_input">Search</InputLabel>
                            <FilledInput
                                id="search"
                                name="search"
                                className="search_input"
                                placeholder="Type product name"
                                value={state.search}
                            />
                        </FormControl>
                        <FormControl variant="filled" className={classes.formControl}>
                            <InputLabel id="category_select_label" className="category_select">Category</InputLabel>
                            <Select
                                labelId="category_select_label"
                                id="category_select"
                                className="category_select"
                                name="category"
                                value={state.category}
                            >
                                <MenuItem value="Fruit">Fruit</MenuItem>
                                <MenuItem value="Vegetables">Vegetables</MenuItem>
                                <MenuItem value="Dairy">Dairy</MenuItem>
                            </Select>
                        </FormControl>
                    </div>
                    <Fab className="add_button" aria-label="add">
                        <AddIcon />
                    </Fab>
                </div>
                <div className="products_list" spacing={3}>
                    {tab}
                </div>
            </div>
        </Container>
    );
}