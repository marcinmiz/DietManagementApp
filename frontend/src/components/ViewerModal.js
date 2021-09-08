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
import {Grid, MenuItem, Tooltip} from "@material-ui/core";
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
import {useHistory, useParams} from "react-router-dom";
import Button from "@material-ui/core/Button";
import SpaIcon from '@material-ui/icons/Spa';
import ExistedProductsPopup from "./ExistedProductsPopup";

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
    bottom_product_rep: {
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
    }

}));

export default function ViewerModal(props) {

    const {onClose, open, products, product_index, header, ...other} = props;

    const classes = useStyles();

    const history = useHistory();

    const [state, setState] = React.useState({
        submitted: false,
        image_upload_product_id: -1,
        selected_product: {
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
        },
        msg: "",
        existed_products: [],
        loaded: false,
        open_existed_products_popup: false
    });

    useEffect(
        async () => {
            await setState({
                ...state,
                selected_product: product_index !== 'new' ? products[product_index] : state.selected_product,
                loaded: true
            });
        }, [ product_index]);

    const handleChangeProductId = (event) => {

        let value;

        if (event.target.name === 'product_calories') {
            if (event.target.value <= 0) {
                return;
            }
            value = Number(event.target.value);
        } else {
            value = event.target.value;
        }
            const values = {
            ...state.selected_product,
            [event.target.name]: value
        };
        setState({
            ...state,
            "selected_product": values,
            "open_existed_products_popup": event.target.name === "product_name"
        });
    };

    const handleChangeNutrient = (index, event) => {
        const values = [...state.selected_product.product_nutrients];
        if (event.target.value > 0) {
            values[index].nutrient_amount = Number(event.target.value);
            let selected_product = {
                ...state.selected_product,
                "product_nutrients": values
            };

            setState({
                ...state,
                "selected_product": selected_product
            });
        }
    };

    const handleClose = () => {
        onClose();
    };

    const handleEdit = () => {
        if (props.mode === "view") {
            history.push('/products/' + state.selected_product.product_id + '/edit');
        } else {
            history.push('/products/' + state.selected_product.product_id + '/view');
        }

    };

    const handleRemove = () => {
        http.delete("/api/products/remove/" + state.selected_product.product_id)
            .then(resp => {
                if (resp.data.message !== "Product " + state.selected_product.product_name + " has been removed successfully") {
                    setState({
                        ...state,
                        "msg": resp.data.message
                    });
                } else {
                    props.handleOperationMessage("Product " + state.selected_product.product_name + " has been removed");
                    onClose();
                }
            })
            .catch(error => console.log(error));
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

    const validateProduct = () => {
        let product = {};
        let productName = state.selected_product.product_name;
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

        let calories = state.selected_product.product_calories;
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

        let category = state.selected_product.product_category;
        if (category === "") {
            setState({
                ...state,
                "msg": "Product category has to be chosen"
            });
            return "error";
        }
        product.category = category;

        let nutrient_amount = state.selected_product.product_nutrients.length;

        product.nutrients = [];

        for (let i = 0; i < nutrient_amount; i++) {
            let nutrient = state.selected_product.product_nutrients[i];
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

    const handleSave = async () => {

        let product = await validateProduct();

        if (product === "error")
            return;

        if (state.selected_product.product_id === "new") {
            http.post("/api/products/add", product)
                .then(resp => {
                    let product_id = Number(resp.data.message.split(" ")[0]);

                    if (resp.data.message !== product_id + " Product " + state.selected_product.product_name + " has been added successfully") {
                        setState({
                            ...state,
                            "msg": resp.data.message
                        });
                    } else {
                        setState({
                            ...state,
                            "submitted": true,
                            "image_upload_product_id": product_id
                        });

                        props.handleOperationMessage("Product " + state.selected_product.product_name + " has been added");
                        onClose();
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
                            "submitted": true,
                            "image_upload_product_id": state.selected_product.product_id
                        });

                        props.handleOperationMessage("Product " + state.selected_product.product_name + " has been updated");
                        onClose();
                    }
                })
                .catch(error => console.log(error));
        }

    };

    const handleBottomProductRep = (event, product_id) => {
        history.push('/products/' + product_id + '/' + props.mode);
    };

    const handleCloseExistedProductsPopup = (event) => {
        setState({
            ...state,
            open_existed_products_popup: false
        });
    };

    let bottom_products_reps = [];

    const products_quantity = products.length;
    let current_index = product_index, selected_product_index;

    switch(products_quantity) {
        case 1:
            selected_product_index = 0;
            bottom_products_reps = [products[product_index]];
            break;
        case 2:
            selected_product_index = 0;
            bottom_products_reps = [products[product_index], products[(product_index + 1) % products_quantity]];
            break;
        case 3:
            selected_product_index = 1;
            for (let i = 1; i >= -1; i--) {
                if ((product_index + i) < 0) {
                    current_index = products_quantity - 1;
                    bottom_products_reps[0] = products[current_index];
                } else {
                    current_index = (product_index + i) % products_quantity;
                    bottom_products_reps[i + 1] = products[current_index];
                }
            }
            break;
        case 4:
            selected_product_index = 1;
            for (let i = 2; i >= -1; i--) {
                if ((product_index + i) < 0) {
                    current_index = products_quantity - 1;
                    bottom_products_reps[0] = products[current_index];
                } else {
                    current_index = (product_index + i) % products_quantity;
                    bottom_products_reps[i + 1] = products[current_index];
                }
            }
            break;
        case 5:
            selected_product_index = 2;
            for (let i = 2; i >= -2; i--) {
                if ((product_index + i) < 0) {
                    if (i === -1) {
                        current_index = products_quantity - 2;
                        bottom_products_reps[0] = products[current_index];
                    }
                    if (i === -2) {
                        current_index = products_quantity - 1;

                        if (bottom_products_reps[0]) {
                            bottom_products_reps[1] = products[current_index];
                        } else {
                            bottom_products_reps[0] = products[current_index];
                        }
                    }

                } else {
                    current_index = (product_index + i) % products_quantity;
                    bottom_products_reps[i + 2] = products[current_index];
                }
            }
            break;
        case 6:
            selected_product_index = 2;
            for (let i = 3; i >= -2; i--) {
                if ((product_index + i) < 0) {
                    if (i === -1) {
                        current_index = products_quantity - 2;
                        bottom_products_reps[0] = products[current_index];
                    }
                    if (i === -2) {
                        current_index = products_quantity - 1;

                        if (bottom_products_reps[0]) {
                            bottom_products_reps[1] = products[current_index];
                        } else {
                            bottom_products_reps[0] = products[current_index];
                        }
                    }

                } else {
                    current_index = (product_index + i) % products_quantity;
                    bottom_products_reps[i + 2] = products[current_index];
                }
            }
            break;
        case 7:
        default:
            selected_product_index = 3;
            for (let i = 3; i >= -3; i--) {
                if ((product_index + i) < 0) {

                    if (i === -1) {
                        current_index = products_quantity - 3;
                        bottom_products_reps[0] = products[current_index];
                    }

                    if (i === -2) {
                        current_index = products_quantity - 2;

                        if (bottom_products_reps[0]) {
                            bottom_products_reps[1] = products[current_index];
                        } else {
                            bottom_products_reps[0] = products[current_index];
                        }
                    }

                    if (i === -3) {
                        current_index = products_quantity - 1;

                        if (bottom_products_reps[0] && bottom_products_reps[1]) {
                            bottom_products_reps[2] = products[current_index];
                        } else if (bottom_products_reps[0]) {
                            bottom_products_reps[1] = products[current_index];
                        } else {
                            bottom_products_reps[0] = products[current_index];
                        }
                    }
                } else {
                    current_index = (product_index + i) % products_quantity;
                    bottom_products_reps[i + 3] = products[current_index];

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
                        <ExistedProductsPopup
                            open={state.open_existed_products_popup}
                            product_name = {state.selected_product.product_name}
                        />
                        <div className={classes.header}>
                            <div className={classes.headerTitle}>
                                {state.selected_product.product_id !== 'new'? <h2>{header} ({product_index+1}/{products_quantity})</h2> : <h2>new</h2>}
                            </div>
                            <Fab aria-label="close">
                                <CloseIcon fontSize="large" onClick={handleClose}/>
                            </Fab>
                        </div>
                        <div className="product_selected">
                            <Fab aria-label="prev">
                                <NavigateBeforeRoundedIcon fontSize="large" onClick={props.handlePrevProduct}/>
                            </Fab>
                            <Grid item key={state.selected_product.product_id}
                                  id={"product" + state.selected_product.product_id} className="product_edit">
                                <div className="product_header product_header_buttons">
                                    {props.mode === 'view' ?
                                        <div className="product_name">
                                            {(state.selected_product.product_id !== 'new' ?
                                                state.selected_product.product_name :
                                            "No product name")}
                                        </div> :
                                        <div>
                                            {state.msg}
                                            <div className="product_header product_header_inputs">

                                                <div className="product_name">
                                                    <TextField className="category_select" id="product_name_input" name="product_name"
                                                               label="Product name" variant="filled" value={state.selected_product.product_name}
                                                               autoComplete="off" onChange={event => handleChangeProductId(event)} onBlur={event => handleCloseExistedProductsPopup(event)}/>
                                                </div>
                                                <div className="product_category">
                                                    <FormControl variant="filled" className="form_control">
                                                        <InputLabel id="category_select_label">Category</InputLabel>
                                                        <Select
                                                            labelId="category_select_label"
                                                            id="category_select"
                                                            name="product_category"
                                                            value={state.selected_product.product_category}
                                                            size="small"
                                                            onChange={event => handleChangeProductId(event)}
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
                                                        value={state.selected_product.product_calories}
                                                        size="small"
                                                        aria-label="calories"
                                                        InputProps={{
                                                            endAdornment: <InputAdornment
                                                                position="end">
                                                                <div>kcal</div>
                                                            </InputAdornment>,
                                                        }}
                                                        onChange={event => handleChangeProductId(event)}
                                                    />
                                                </div>
                                            </div>
                                        </div>}

                                    <div className="product_buttons">
                                        <Tooltip title="Delete" aria-label="delete">
                                            <IconButton aria-label="delete" className="product_icon_button"
                                                        onClick={handleRemove} disabled={state.selected_product.product_id === 'new'}>
                                                <DeleteIcon fontSize="small"/>
                                            </IconButton>
                                        </Tooltip>
                                        <Tooltip title="Edit" aria-label="edit">
                                            <IconButton type="button" aria-label="edit" className="product_icon_button"
                                                        onClick={(event) => handleEdit(event, state.selected_product.product_id)}>
                                                <EditIcon fontSize="small"/>
                                            </IconButton>
                                        </Tooltip>
                                    </div>
                                </div>
                                <div className="creation_date">
                                    {state.selected_product.product_id === "new" ? "No creation date" : "created " + state.selected_product.creation_date}
                                </div>

                                <div className="product_content">
                                    <div className="product_image_container">

                                        {props.mode === 'view' ?
                                            (state.selected_product.product_id !== 'new' ?
                                                (state.selected_product.product_image !== '' ?
                                                    <img src={state.selected_product.product_image} alt={state.selected_product.product_name} className="product_image"/> :
                                                    <div><SpaIcon className={classes.photo_placeholder}/></div>) :
                                                <div><SpaIcon className={classes.photo_placeholder}/></div>) :
                                                    <UploadImages submitted={state.submitted}
                                                          currentImage={state.selected_product.product_image}
                                                          type="product" id={state.image_upload_product_id}/>}
                                    </div>
                                    <div className="product_description">
                                        {props.mode === 'view' ?
                                            <div className="product_category">
                                                {(state.selected_product.product_id !== 'new' ?
                                                <Chip
                                                    name="category"
                                                    size="small"
                                                    avatar={<CategoryIcon/>}
                                                    label={state.selected_product.product_category}
                                                    onClick={event => handleCategory(event)}
                                                /> : <div className={classes.category}>No category</div>)}
                                            </div> :
                                            null}
                                        {props.mode === 'view' &&
                                        (state.selected_product.product_id !== 'new' ?
                                            <div className="product_author" onClick={event => handleAuthor(event)}>
                                            <Avatar/>
                                            <div
                                                className="product_author_name">{state.selected_product.product_author}</div>
                                            </div> :
                                                <div>
                                                    No author
                                                </div>
                                        )}
                                        {props.mode === 'view' ?
                                            <div className="product_nutrients_header">
                                                {state.selected_product.product_calories} kcal
                                            </div> :
                                            null}
                                        {props.mode === 'view' ?
                                            <div className="product_nutrients">
                                                <div className="product_nutrients_header">Nutrients</div>
                                                {state.selected_product.product_nutrients.map((nutrient, index) =>
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
                                                {state.selected_product.product_nutrients.map((nutrient, index) => (
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
                                            <Button className={classes.button} variant="contained" color="primary" onClick={() => handleClose()}>Cancel</Button>
                                            <Button className={classes.button} variant="contained" color="primary" type="button" onClick = {() => handleSave()}>Save</Button>
                                        </div> :
                                        null}
                                    </div>
                                </div>
                            </Grid>

                            <Fab className="next_button" aria-label="next">
                                <NavigateNextRoundedIcon fontSize="large" onClick={props.handleNextProduct}/>
                            </Fab>
                        </div>
                        {state.loaded && <div className="product_selected_bottom">
                            {bottom_products_reps.map( (product, index) => (
                                product && <div key={index} className={classes.bottom_product_rep}>
                                    <Chip
                                        className={index === selected_product_index ? "selected_bottom_product_rep" : (Math.abs(selected_product_index - index) === 1 ? "first_middle_bottom_product_rep" : (Math.abs(selected_product_index - index) === 2 ? "second_middle_bottom_product_rep" : "third_middle_bottom_product_rep"))}
                                        size={index === selected_product_index ? "medium" : "small"}
                                        label={product.product_name}
                                        color="primary"
                                        onClick={index !== selected_product_index ? event => handleBottomProductRep(event, product.product_id) : null}
                                    />
                                </div>
                            ) )}
                        </div>}

                    </div>
                </Fade>
            </Modal>}
        </div>
    );
}