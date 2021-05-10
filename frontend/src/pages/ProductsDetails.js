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

    let current_product;

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
    }
}