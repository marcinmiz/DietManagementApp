import React, {useEffect} from 'react'
import CategoryIcon from '@material-ui/icons/Category';
import {MenuItem, Container, Tooltip, Divider, Grid} from '@material-ui/core';
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
import TextField from '@material-ui/core/TextField';
import InputAdornment from '@material-ui/core/InputAdornment';
import UploadImages from "../components/UploadImages";
import http from "../http-common";

export default function ProductsDetails(props) {

    const [state, setState] = React.useState({
        products_group: 0,//0: all, 1: unconfirmed, 2: new
        mode: '',
        submitted: false,
        products: [],
        selected_product: {
            product_id: 'new',
            product_name: "",
            product_image: "",
            product_category: "",
            product_author: "",
            // product_favourite: false
        },
        product_nutrients: [{
            nutrient_name: "Calories",
            nutrient_amount: 0,
            nutrient_unit: "kcal"
        }, {nutrient_name: "Protein", nutrient_amount: 0, nutrient_unit: "mg/100g"}, {
            nutrient_name: "Carbohydrates",
            nutrient_amount: 0,
            nutrient_unit: "mg/100g"
        }, {nutrient_name: "Fat", nutrient_amount: 0, nutrient_unit: "mg/100g"}, {
            nutrient_name: "Salt",
            nutrient_amount: 0,
            nutrient_unit: "mg/100g"
        }],
        product_ingredients: [],
        product_add_ingredient: "",
        msg: "",
        existed_products: [],
        categories: [],
    });

        useEffect(
        () => {

            const product_id = props.match.params.id;
            const mode = props.match.params.mode;
            let categoriesTable = [];

            http.get("/api/categories")
                .then(resp => {

                    for (let x in resp.data){
                        categoriesTable[x] = {};
                        categoriesTable[x].category_id = resp.data[x].categoryId;
                        categoriesTable[x].category_name = resp.data[x].categoryName;
                    }
                    setState({
                        ...state,
                        "categories": categoriesTable,
                    });

                })
                .catch(error => console.log(error));

            state.mode = mode;

            if (product_id !== 'new') {
                //retrieve product specified by id data
                http.get("/api/products/" + product_id)
                    .then(response => {
                        let data = response.data;
                        console.log(data);
                        let product = {};
                        product.product_id = data.productId;
                        product.product_name = data.productName;
                        product.product_category = data.category.categoryName;
                        product.product_author = data.owner.firstName + " " + data.owner.lastName;
                        // product.product_favourite = data.productFavourite;
                        product.product_image = data.productImage;

                        let productNutrients = [];
                        productNutrients[0] = {};
                        productNutrients[0].nutrient_name = "Calories";
                        productNutrients[0].nutrient_amount = data.calories;
                        productNutrients[0].nutrient_unit = "kcal";

                        let nutrients_quantity = state.product_nutrients.length;
                        for (let i = 1; i < nutrients_quantity; i++) {
                            productNutrients[i] = {};
                            productNutrients[i].nutrient_name = data.nutrients[i-1].nutrient.nutrientName;
                            productNutrients[i].nutrient_amount = data.nutrients[i-1].nutrientAmount;
                            productNutrients[i].nutrient_unit = "mg/100g";
                        }

                        setState({
                            ...state,
                            "mode": mode,
                            "categories": categoriesTable,
                            "selected_product": product,
                            "product_nutrients": productNutrients,
                            // "product_ingredients": [["Water", 85], ["Fructose", 10], ["Fibre and Pectin", 3.5]],
                        });

                    })
                    .catch(error => console.log(error));

            }
            document.getElementById("product" + state.selected_product.product_id).classList.add("product_selected_moved");

            if (mode === "edit") {
                const products_existed = document.getElementsByClassName("products_existed_container")[0];
                products_existed.style.display = "block";

                document.getElementById("product_name_value").addEventListener('focus', () => {
                    products_existed.style.display = "block";
                });
                document.getElementById("product_name_value").addEventListener('blur', () => {
                    products_existed.style.display = "none";
                });
            }
        }, [props.match.params.mode, props.match.params.id]
    );

    useEffect(
        () => {
            const products_existed = document.getElementsByClassName("products_existed_container")[0];
            const name_input = document.getElementById("product_name_value");

            if (state.mode === "edit" && document.activeElement === name_input) {
                products_existed.style.display = "block";
            }

            if (state.selected_product.product_name !== undefined && state.selected_product.product_name !== "") {
                let search_parameters = {};
                search_parameters.phrase = state.selected_product.product_name;
                search_parameters.category = "";

                http.post("/api/products/search", search_parameters)
                    .then(resp => {
                        let search = [];
                        for (let x in resp.data){
                            search[x] = resp.data[x].productName;
                        }

                        setState({
                            ...state,
                            existed_products: search,
                        });

                        if (search.length <= 0) {
                            products_existed.style.display = "none";
                        }

                    })
                    .catch(error => console.log(error))
            } else {
                products_existed.style.display = "none";
            }
        }, [state.selected_product.product_name]
    );

    const handleChangeProductId = (event) => {

        const values = {
            ...state.selected_product,
            [event.target.name]: event.target.value
        };
        setState({
            ...state,
            "selected_product": values
        });
    };

    const handleEdit = () => {
        if (state.mode === "view") {
            props.history.push('/products/product-'+state.selected_product.product_id);
        } else {
            props.history.push('/products/' + state.selected_product.product_id + '/view');
        }

    }

    const handleClose = () => {
        document.getElementById("product" + state.selected_product.product_id).classList.remove("product_selected_moved");
        setTimeout(() => props.history.push("/products/main"), 2000);
    }

    const handleChangeIngredient = (event, index) => {
        if (event.target.name === "product_add_ingredient") {
            setState({
                ...state,
                "product_add_ingredient": event.target.value
            });
        } else {
            const values = [...state.product_ingredients];
            values[index][1] = Number(event.target.value);
            setState({
                ...state,
                "product_ingredients": values
            });
        }
    }

    const handleChangeNutrient = (index, event) => {
        const values = [...state.product_nutrients];
        if (event.target.value > 0){
            values[index].nutrient_amount = Number(event.target.value);
            setState({
                ...state,
                "product_nutrients": values
            });
        }
    };

    const handleAddIngredient = () => {
        setState({
            ...state,
            "product_ingredients": [...state.product_ingredients, [state.product_add_ingredient, ""]]
        });
    }

    const handleDeleteIngredient = (index) => {
        const values = [...state.product_ingredients];
        values.splice(index, 1);
        setState({
            ...state,
            "product_ingredients": values
        });
    }

    const handleRemove = () => {
        http.delete("/api/products/remove/" + state.selected_product.product_id)
            .then(resp => {
                if (resp.data.message !== "Product " + state.selected_product.product_name + " has been removed successfully") {
                    setState({
                        ...state,
                        "msg": resp.data.message
                    });
                } else {

                    props.history.push("/products/" + state.selected_product.product_name.replace(" ", "_") + "-removed")
                }
            })
            .catch(error => console.log(error));
        handleClose();
    };

    // const handleFavouriteIcon = () => {
    //     if (state.selected_product.product_favourite) {
    //         return (<Tooltip title="Remove from favourite" aria-label="Remove from favourite">
    //             <IconButton aria-label="Remove from favourite" className="product_icon_button"
    //                         onClick={() => handleAddToFavourite()}>
    //                 <FavoriteIcon fontSize="small"/>
    //             </IconButton>
    //         </Tooltip>);
    //     } else {
    //         return (<Tooltip title="Add to favourite" aria-label="Add to favourite">
    //             <IconButton aria-label="Add to favourite" className="product_icon_button"
    //                         onClick={() => handleAddToFavourite()}>
    //                 <FavoriteBorderIcon fontSize="small"/>
    //             </IconButton>
    //         </Tooltip>);
    //     }
    // }
    //
    // const handleAddToFavourite = () => {
    //     const product_favourite = !state.selected_product.product_favourite;
    //     const values = [{
    //         ...state.selected_product,
    //         "product_favourite": product_favourite
    //     }];
    //     setState({
    //         ...state,
    //         "selected_product": values
    //     });
    // }

    const handleSave = () => {
        let product = {};
        let productName = state.selected_product.product_name;
        if (productName.length < 2 || productName.length > 40) {
            setState({
                ...state,
                "msg": "Product name has to have min 2 and max 40 characters"
            });
            return;
        } else if (!(/^[a-zA-Z ]+$/.test(productName))) {
            setState({
                ...state,
                "msg": "Product name has to contain only letters and spaces"
            });
            return;
        }
        product.productName = productName;

        let calories = state.product_nutrients[0].nutrient_amount;
        if (calories.length < 1 || calories.length > 20) {
            setState({
                ...state,
                "msg": "Product calories has to have min 1 and max 20 characters"
            });
            return;
        } else if (!(/^0$/.test(calories) || /^(-)?[1-9]\d*$/.test(calories))) {
            setState({
                ...state,
                "msg": "Product calories has to contain only digits"
            });
            return;
        } else if (calories < 0) {
            setState({
                ...state,
                "msg": "Product calories has to be greater or equal 0"
            });
            return;
        }
        product.calories = calories;

        let category = state.selected_product.product_category;
        if (category === "") {
            setState({
                ...state,
                "msg": "Product category has to be chosen"
            });
            return;
        }
        product.category = category;

        let nutrient_amount = state.product_nutrients.length;

        product.nutrients = [];

        for (let i = 1; i < nutrient_amount; i++) {
            let nutrient = state.product_nutrients[i];
            if (nutrient.nutrient_amount.length < 1 || nutrient.nutrient_amount.length > 20) {
                setState({
                    ...state,
                    "msg": "Product nutrient has to have min 1 and max 20 characters"
                });
                return;
            } else if (!(/^0$/.test(nutrient.nutrient_amount) || /^(-)?[1-9]\d*$/.test(nutrient.nutrient_amount))) {
                setState({
                    ...state,
                    "msg": "Product nutrient has to contain only digits"
                });
                return;
            } else if (nutrient.nutrient_amount < 0) {
                setState({
                    ...state,
                    "msg": "Product nutrient has to be greater or equal 0"
                });
                return;
            }

            product.nutrients[i-1] = nutrient.nutrient_name + ";" + nutrient.nutrient_amount;
        }

        console.log(product);

        if (state.selected_product.product_id === "new") {
            http.post("/api/products/add", product)
                .then(resp => {
                    let product_id = resp.data.message.split(" ")[0];

                    if (resp.data.message !== product_id + " Product " + state.selected_product.product_name + " has been added successfully") {
                        setState({
                            ...state,
                            "msg": resp.data.message
                        });
                    } else {
                        let selected_product = {... state.selected_product, "product_id": product_id};
                        setState({
                            ...state,
                            "submitted": true,
                            "selected_product": selected_product
                        });

                        props.history.push("/products/" + product.productName.replace(" ", "_") + "-added")
                    }
                })
                .catch(error => console.log(error));
        } else {
            http.put("/api/products/update/" + state.selected_product.product_id, product)
                .then(resp => {
                    if (resp.data.message !== "Product " + state.selected_product.product_name + " has been updated successfully") {
                        setState({
                            ...state,
                            "msg": resp.data.message
                        });
                    } else {
                        setState({
                            ...state,
                            "submitted": true
                        });

                        props.history.push("/products/" + product.productName.replace(" ", "_") + "-updated")
                    }
                })
                .catch(error => console.log(error));
        }

    };

    let current_product, tab;

    if (state.mode === 'view') {
        current_product = <div id={"product" + state.selected_product.product_id} className="product product_selected">
            <div className="product_image_container product_selected_element ">
                <img className="product_image" src={state.selected_product.product_image}
                     alt={state.selected_product.product_name}/>
            </div>

            <div className="product_description product_selected_element">
                <div className="product_id">
                    <div className="product_name product_selected_element">
                        {state.selected_product.product_name}
                    </div>
                    <div className="product_category product_selected_element">
                        <Chip
                            name="category"
                            size="small"
                            avatar={<CategoryIcon/>}
                            label={state.selected_product.product_category}
                        />
                    </div>
                    <div className="product_author product_selected_element">
                        <Avatar/>
                        <div className="product_author_name">{state.selected_product.product_author}</div>
                    </div>
                </div>
                <div className="product_details product_selected_element">
                    <div className="nutrients">
                        <h4>Nutrients</h4>
                        {state.product_nutrients.map((nutrient, index) => (
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
                        {state.product_ingredients.map((ingredient, index) => (
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
                    <IconButton aria-label="close" className="product_icon_button" onClick={handleClose}>
                        <CloseIcon fontSize="small"/>
                    </IconButton>
                </Tooltip>
                <Tooltip title="Remove" aria-label="remove">
                    <IconButton aria-label="remove" className="product_icon_button" onClick={handleRemove}>
                        <DeleteIcon fontSize="small"/>
                    </IconButton>
                </Tooltip>
                <Tooltip title="Edit" aria-label="edit">
                    <IconButton aria-label="edit" className="product_icon_button" onClick={handleEdit}>
                        <EditIcon fontSize="small"/>
                    </IconButton>
                </Tooltip>
                {/*{handleFavouriteIcon()}*/}
            </div>
        </div>;
    } else {
        current_product = <div id={"product" + state.selected_product.product_id} className="product product_selected">
            <form className="product_edit_form">
                <UploadImages submitted={state.submitted} currentImage={state.selected_product.product_image} type="product" id={state.selected_product.product_id}/>

                <div className="product_description product_selected_element product_selected_element_moved">
                    {state.msg}
                    <div className="product_id">
                        <div className="product_name product_selected_element">
                            <FormControl>
                                <InputLabel id="product_name_label" className="product_name">Product name</InputLabel>
                                <FilledInput
                                    name="product_name"
                                    className="product_name"
                                    id="product_name_value"
                                    value={state.selected_product.product_name}
                                    onChange={event => handleChangeProductId(event)}
                                />
                            </FormControl>
                        </div>
                        <div className="product_category product_selected_element">
                            <FormControl variant="filled" className="form_control">
                                <InputLabel id="category_select_label" className="category_select">Category</InputLabel>
                                <Select
                                    labelId="category_select_label"
                                    id="category_select"
                                    className="category_select"
                                    name="product_category"
                                    value={state.selected_product.product_category}
                                    size="small"
                                    onChange={event => handleChangeProductId(event)}
                                >
                                    {state.categories.map((category, index) => (
                                    <MenuItem key={index} value={category.category_name}>{category.category_name}</MenuItem>
                                    ))}
                                </Select>
                            </FormControl>
                        </div>
                    </div>
                    <div className="product_details product_selected_element product_details_edit">
                        <div className="nutrients">
                            <h4>Nutrients</h4>
                            {state.product_nutrients.map((nutrient, index) => (
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
                        </div>
                        <Divider orientation="vertical" flexItem variant='middle'/>
                        <div className="ingredients">
                            <div id="ingredients_list">
                                <h4>Ingredients</h4>
                                {state.product_ingredients.map((ingredient, index) => (
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
                                            onChange={event => handleChangeIngredient(event, index)}
                                        />
                                        <Tooltip title="Delete ingredient" aria-label="delete ingredient">
                                            <IconButton aria-label="delete ingredient" className="product_icon_button"
                                                        onClick={() => handleDeleteIngredient(index)}>
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
                                    onChange={event => handleChangeIngredient(event)}
                                />
                                <Tooltip title="Add ingredient" aria-label="add ingredient">
                                    <IconButton aria-label="add ingredient" className="product_icon_button"
                                                onClick={() => handleAddIngredient()}
                                                disabled={state.product_add_ingredient === ""}>
                                        <AddIcon fontSize="large"/>
                                    </IconButton>
                                </Tooltip>
                            </div>
                        </div>
                    </div>
                    <div>
                        <Button variant="text" color="inherit" onClick={() => handleClose()}>Cancel</Button>
                        <Button variant="text" color="inherit" type="button" onClick = {() => handleSave()}>Save</Button>
                    </div>
                </div>
                <div className="product_buttons">
                    <Tooltip title="Close" aria-label="close">
                        <IconButton aria-label="close" className="product_icon_button" onClick={() => handleClose()}>
                            <CloseIcon fontSize="small"/>
                        </IconButton>
                    </Tooltip>
                    <Tooltip title="Remove" aria-label="remove">
                        <IconButton aria-label="remove" className="product_icon_button" onClick={() => handleRemove()}>
                            <DeleteIcon fontSize="small"/>
                        </IconButton>
                    </Tooltip>
                    <Tooltip title="Edit" aria-label="edit">
                        <IconButton aria-label="edit" className="product_icon_button edit_mode"
                                    onClick={() => handleEdit()}>
                            <EditIcon fontSize="small"/>
                        </IconButton>
                    </Tooltip>
                    {/*{handleFavouriteIcon()}*/}
                </div>
            </form>
        </div>;
    }

    if (state.products_group === 0) {
        //retrieve all products and set its values to product state field
    } else if (state.products_group === 1) {
        //retrieve unconfirmed products and set its values to product state field
    } else {
        //retrieve new products and set its values to product state field
    }

    tab = <Grid container className="products_list" spacing={1}>
        {state.products.map((product, index) => (
            <Grid item key={index} id={product.product_id} className="product">
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
                    <Tooltip title="Remove" aria-label="remove">
                        <IconButton aria-label="remove" className="product_icon_button">
                            <DeleteIcon fontSize="small"/>
                        </IconButton>
                    </Tooltip>
                    <Tooltip title="Edit" aria-label="edit">
                        <IconButton aria-label="edit" className="product_icon_button">
                            <EditIcon fontSize="small"/>
                        </IconButton>
                    </Tooltip>
                    {/*<Tooltip title="Change favourite status" aria-label="Change favourite status">*/}
                        {/*<IconButton aria-label="Change favourite status" className="product_icon_button" disabled>*/}
                        {/*</IconButton>*/}
                    {/*</Tooltip>*/}
                </div>
            </Grid>
        ))}
    </Grid>;

    return (
        <Container maxWidth="xl">
            <div className="products_existed_container product_selected_element_moved">
                <h3>Products yet existed</h3>
                {state.existed_products.map((product, index) => (
                    <div key={index}>
                        <div  className="product_existed">{product}</div>
                        <Divider variant="middle"/>
                    </div>
                ))}
            </div>
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
                        <Tab className="product_group_tab" label="All"/>
                        <Tab className="product_group_tab" label="Unconfirmed"/>
                        <Tab className="product_group_tab" label="New"/>
                    </Tabs>
                    <div>
                        <FormControl variant="filled" className="form_control">
                            <InputLabel htmlFor="search" className="search_input">Search</InputLabel>
                            <FilledInput
                                id="search"
                                name="search"
                                className="search_input"
                                placeholder="Type product name"
                                value={state.search}
                            />
                        </FormControl>
                        <FormControl variant="filled" className="form_control">
                            <InputLabel id="category_select_label" className="category_select">Category</InputLabel>
                            <Select
                                labelId="category_select_label"
                                id="category_select"
                                className="category_select"
                                name="category"
                                value={state.category}
                            >
                                {state.categories.map((category, index) => (
                                    <MenuItem key={index} value={category.category_name}>{category.category_name}</MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                    </div>
                    <Fab className="add_button" aria-label="add">
                        <AddIcon/>
                    </Fab>
                </div>
                <div className="products_list">
                    {tab}
                </div>
            </div>
        </Container>
    );

}