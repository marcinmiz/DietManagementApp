import React, {useEffect} from 'react'
import CategoryIcon from '@material-ui/icons/Category';
import {MenuItem, Grid, Container, makeStyles, Tooltip, Divider} from '@material-ui/core';
import Select from '@material-ui/core/Select';
import InputLabel from '@material-ui/core/InputLabel';
import FormControl from '@material-ui/core/FormControl';
import Fab from '@material-ui/core/Fab';
import AddIcon from '@material-ui/icons/Add';
import Avatar from '@material-ui/core/Avatar';
import Chip from '@material-ui/core/Chip';
import IconButton from '@material-ui/core/IconButton';
import DeleteIcon from '@material-ui/icons/Delete';
import EditIcon from '@material-ui/icons/Edit';
import FavoriteBorderIcon from '@material-ui/icons/FavoriteBorder';
import FavoriteIcon from '@material-ui/icons/Favorite';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import FilledInput from '@material-ui/core/FilledInput';
import http from "../http-common";

const useStyles = makeStyles({
    formControl: {
        minWidth: 110,
    },
});

export default function Products(props) {

    const classes = useStyles();

    const [state, setState] = React.useState({
        search: '',
        category: '',
        products_group: 0,//0: all, 1: unconfirmed, 2: new
        products: [],
        categories: [],
        msg: "",
        loaded: false
    });

    useEffect(
        () => {
            if ((/^product-\d*$/.test(props.match.params.msg)) || (/^product-new$/.test(props.match.params.msg))) {
                console.log(props.match.params.msg);
                let parts = props.match.params.msg.split('-');
                props.history.push('/products/' + parts[1] + '/edit');
            }
            state.loaded = false;
            document.getElementsByClassName("loading").item(0).innerHTML = "Loading";
            document.getElementsByClassName("loading").item(0).removeAttribute("hidden");
            let msg_container = document.getElementsByClassName("msg").item(0);
            msg_container.style.display = "none";
            let categoriesTable = [];

            http.get("/api/categories")
                .then(resp => {
                    for (let x in resp.data){
                        categoriesTable[x] = {};
                        categoriesTable[x].category_id = resp.data[x].categoryId;
                        categoriesTable[x].category_name = resp.data[x].categoryName;
                    }
                })
                .catch(error => console.log(error));

                //retrieve all products and set its values to product state field

                let search_parameters = {};
                search_parameters.phrase = state.search;
                search_parameters.category = state.category;
                console.log(search_parameters);
                http.post("/api/products/search", search_parameters)
                    .then(resp => {
                        console.log(resp.data);
                        let table = [];
                        if (state.products_group === 0) {
                            console.log("first");

                            for (let x in resp.data) {
                                if (resp.data[x].approvalStatus === "accepted") {
                                    table[x] = {};
                                    table[x].product_id = resp.data[x].productId;
                                    table[x].product_name = resp.data[x].productName;
                                    table[x].product_category = resp.data[x].category.categoryName;
                                    table[x].product_author = resp.data[x].owner.firstName + " " + resp.data[x].owner.lastName;
                                    table[x].product_favourite = resp.data[x].productFavourite;
                                    table[x].product_image = resp.data[x].productImage;
                                    let creationDate = new Date(resp.data[x].creationDate)
                                    table[x].creation_date = creationDate.toLocaleDateString() + " " + creationDate.toLocaleTimeString();
                                }
                            }

                        } else if (state.products_group === 1) {
                            //retrieve unconfirmed products and set its values to product state field
                            console.log("second");

                            for (let x in resp.data) {
                                if (resp.data[x].approvalStatus !== "accepted") {
                                    table[x] = {};
                                    table[x].product_id = resp.data[x].productId;
                                    table[x].product_name = resp.data[x].productName;
                                    table[x].product_category = resp.data[x].category.categoryName;
                                    table[x].product_author = resp.data[x].owner.firstName + " " + resp.data[x].owner.lastName;
                                    table[x].product_favourite = resp.data[x].productFavourite;
                                    table[x].product_image = resp.data[x].productImage;
                                    let creationDate = new Date(resp.data[x].creationDate);
                                    table[x].creation_date = creationDate.toLocaleDateString() + " " + creationDate.toLocaleTimeString();
                                    table[x].approval_status = resp.data[x].approvalStatus;
                                    if (resp.data[x].assessmentDate !== null) {
                                        let assessmentDate = new Date(resp.data[x].assessmentDate);
                                        table[x].assessment_date = assessmentDate.toLocaleDateString() + " " + assessmentDate.toLocaleTimeString();
                                        table[x].reject_explanation = resp.data[x].rejectExplanation;
                                    }
                                }
                            }

                        } else {
                            //retrieve new products and set its values to product state field
                            console.log("third");

                        }
                        setState({
                            ...state,
                            products: table,
                            categories: categoriesTable,
                        });

                        if ((/^[a-z_A-Z]+(-)[a-zA-Z]+$/.test(props.match.params.msg))) {
                            let parts = props.match.params.msg.split("-");
                            switch (parts[1]) {
                                case "added":
                                    msg_container.style.display = "block";
                                    msg_container.innerHTML = "Product " + parts[0].replaceAll("_", " ") + " has been successfully added";
                                    break;
                                case "updated":
                                    msg_container.style.display = "block";
                                    msg_container.innerHTML = "Product " + parts[0].replaceAll("_", " ") + "  has been successfully updated";
                                    break;
                                case "removed":
                                    msg_container.style.display = "block";
                                    msg_container.innerHTML = "Product  " + parts[0].replaceAll("_", " ") + " has been successfully removed";
                                    break;
                            }
                            setTimeout(() => props.history.push("/products/main"), 3000);
                        }
                        state.loaded = true;
                        if (table.length === 0) {
                            document.getElementsByClassName("loading").item(0).innerHTML = "No products found";
                        } else {
                            document.getElementsByClassName("loading").item(0).setAttribute("hidden", true);
                        }
                })
                    .catch(error => console.log(error))

        }, [state.products_group, props.match.params.msg, state.search, state.category]
    );

    const handleChange = (event) => {
        setState({
            ...state,
            [event.target.name]: event.target.value,
        });
    };

    const handleTab = (event, newValue) => {
        setState({
            ...state,
            products_group: newValue,
        });
    };

    const handleProduct = (event, product_id) => {
        let destination = "/products/" + product_id + "/view";
        props.history.push(destination);
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

    // const handleFavouriteIcon = (product_id) => {
    //     let index = 0, products_quantity = state.products.length;
    //     while (index < products_quantity) {
    //         if (state.products[index].product_id === product_id) {
    //             break;
    //         }
    //         index += 1;
    //     }
    //     if (state.products[index].product_favourite) {
    //         return (
    //             <Tooltip title="Remove from favourite" aria-label="Remove from favourite">
    //                 <IconButton aria-label="Remove from favourite" className="product_icon_button"
    //                             onClick={event => handleAddToFavourite(event, index, product_id)}>
    //                     <FavoriteIcon fontSize="small"/>
    //                 </IconButton>
    //             </Tooltip>
    //         );
    //     } else {
    //         return (
    //             <Tooltip title="Add to favourite" aria-label="Add to favourite">
    //                 <IconButton aria-label="Add to favourite" className="product_icon_button"
    //                             onClick={event => handleAddToFavourite(event, index, product_id)}>
    //                     <FavoriteBorderIcon fontSize="small"/>
    //                 </IconButton>
    //             </Tooltip>
    //         )
    //     }
    // };
    //
    // const handleAddToFavourite = (event, index, product_id) => {
    //     event.cancelBubble = true;
    //     if (event.stopPropagation) event.stopPropagation();
    //     const product_favourite = !state.products[index].product_favourite;
    //     const product = {
    //         ...state.products[index],
    //         "product_favourite": product_favourite
    //     };
    //     setState({
    //         ...state,
    //         "products": [...state.products.slice(0, index), product, ...state.products.slice(index + 1)]
    //     });
    // };

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

    let tab;

    switch (state.products_group) {
        case 0:
        case 2:
            tab = <div className="products_list">
                    <Grid container>
                        {state.products.map((product, index) => (
                            <Grid item key={index} id={"product" + product.product_id} className="product"
                                  onClick={event => handleProduct(event, product.product_id)}>
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
                                            onClick={handleCategory}
                                        />
                                    </div>
                                    <div className="product_author" onClick={handleAuthor}>
                                        <Avatar/>
                                        <div className="product_author_name">{product.product_author}</div>
                                    </div>
                                    <div>
                                        Creation date:
                                    </div>
                                    <div>
                                        {product.creation_date}
                                    </div>
                                </div>
                                <div className="product_buttons">
                                    <Tooltip title="Delete" aria-label="delete">
                                        <IconButton aria-label="delete" className="product_icon_button"
                                                    onClick={event => handleRemove(event, index)}>
                                            <DeleteIcon fontSize="small"/>
                                        </IconButton>
                                    </Tooltip>
                                    <Tooltip title="Edit" aria-label="edit">
                                        <IconButton type="button" aria-label="edit" className="product_icon_button"
                                                    onClick={(event) => handleEdit(event, product.product_id)}>
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
        case 1:
            tab = <div className="unconfirmed_products_list">
                {state.products.map((product, index) => (
                        <div key={index}>
                            <div id={"product" + product.product_id} className="unconfirmed_product">
                                <div className="product" onClick={event => handleProduct(event, product.product_id)}>
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
                                                onClick={handleCategory}
                                            />
                                        </div>
                                        <div className="product_author" onClick={handleAuthor}>
                                            <Avatar/>
                                            <div className="product_author_name">{product.product_author}</div>
                                        </div>
                                        <div>
                                            Creation date:
                                        </div>
                                        <div>
                                            {product.creation_date}
                                        </div>
                                    </div>
                                    <div className="product_buttons">
                                        <Tooltip title="Delete" aria-label="delete">
                                            <IconButton aria-label="delete" className="product_icon_button"
                                                        onClick={event => handleRemove(event, index)}>
                                                <DeleteIcon fontSize="small"/>
                                            </IconButton>
                                        </Tooltip>
                                        <Tooltip title="Edit" aria-label="edit">
                                            <IconButton type="button" aria-label="edit" className="product_icon_button"
                                                        onClick={(event) => handleEdit(event, product.product_id)}>
                                                <EditIcon fontSize="small"/>
                                            </IconButton>
                                        </Tooltip>
                                        {/*{handleFavouriteIcon(product.product_id)}*/}
                                    </div>
                                </div>
                                <div className="product_status">
                                    <div className="unconfirmed_header">Status</div>
                                    <Divider variant="fullWidth"/>
                                    <div className={product.approval_status === "pending"?"pending_product unconfirmed_body":"rejected_product unconfirmed_body"}>
                                        {product.approval_status.toUpperCase()}
                                    </div>
                                </div>
                                    {product.approval_status === "pending"
                                        ?
                                        <div className="assessment_details">
                                            This product has not been assessed yet
                                        </div>
                                        :
                                        <div className="assessment_details">
                                            <div className="assessment_date">
                                                <div className="unconfirmed_header">Assessment Date</div>
                                                <Divider variant="fullWidth"/>
                                                <div className="unconfirmed_body">{product.assessment_date}</div>
                                            </div>
                                            <div className="reject_explanation">
                                                <div className="unconfirmed_header">Rejection explanation</div>
                                                <Divider variant="fullWidth"/>
                                                <div className="unconfirmed_body">{product.reject_explanation}</div>
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

    return (
        <Container id="main_container" maxWidth="lg">
            <div className="page_container">
                <h2>Products</h2>
                <div className="toolbar_container">
                    <Tabs
                        name="products_group"
                        value={state.products_group}
                        indicatorColor="primary"
                        textColor="inherit"
                        onChange={handleTab}
                        aria-label="product groups buttons"
                    >
                        <Tab className="product_group_tab" label="All"/>
                        <Tab className="product_group_tab" label="Unconfirmed"/>
                        <Tab className="product_group_tab" label="New"/>
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
                                onChange={handleChange}
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
                                onChange={handleChange}
                            >
                                {state.categories.map((category, index) => (
                                    <MenuItem key={index} value={category.category_name}>{category.category_name}</MenuItem>
                                 ))}
                            </Select>
                        </FormControl>
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