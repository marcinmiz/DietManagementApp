import React, {useEffect} from 'react'
import CategoryIcon from '@material-ui/icons/Category';
import {Container, Divider, Grid, makeStyles, MenuItem, Tooltip, withStyles} from '@material-ui/core';
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
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import FilledInput from '@material-ui/core/FilledInput';
import http from "../http-common";
import Button from "@material-ui/core/Button";
import ConfirmationDialog from "../components/ConfirmationDialog";
import LinearProgress from '@material-ui/core/LinearProgress';

const BorderLinearProgress = withStyles((theme) => ({
    root: {
        height: 10,
        width: 400,
        borderRadius: 5,
        marginLeft: 300,
    },
    colorPrimary: {
        backgroundColor: theme.palette.grey[theme.palette.type === 'light' ? 200 : 700],
    },
    bar: {
        borderRadius: 5,
        backgroundColor: '#1a90ff',
    },
}))(LinearProgress);

const useStyles = makeStyles((theme) => ({
    formControl: {
        minWidth: 110,
    },
    paper: {
        width: '80%',
        maxHeight: 435,
    },
    backdrop: {
        zIndex: theme.zIndex.drawer + 1,
        color: '#fff',
        backgroundColor: '#282c34'
    },
}));

export default function Products(props) {

    const classes = useStyles();

    const [state, setState] = React.useState({
        search: '',
        category: '',
        products_group: 0,//0: all, 1: unconfirmed, 2: new
        products: [],
        categories: [],
        msg: "",
        loaded: false,
        open_confirmation_modal: false,
        complement: "",
        confirmation_product_id: null,
        confirmation_product_name: null,
        open_viewer_modal: false,
    });

    useEffect(
        () => {
            // if ((/^product-\d*$/.test(props.match.params.msg)) || (/^product-new$/.test(props.match.params.msg))) {
            //     let parts = props.match.params.msg.split('-');
            //     props.history.push('/products/' + parts[1] + '/edit');
            // }
            let categoriesTable = [];

            http.get("/api/categories")
                .then(resp => {
                    for (let x in resp.data) {
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
            http.post("/api/products/search", search_parameters)
                .then(resp => {
                    let table = [];
                    if (state.products_group === 0) {

                        for (let x in resp.data) {
                            if (resp.data[x].approvalStatus === "accepted") {
                                table[x] = createProduct(resp.data, x);
                            }
                        }

                    } else if (state.products_group === 1) {
                        //retrieve unconfirmed products and set its values to product state field

                        for (let x in resp.data) {
                            if (props.admin === true && props.adminMode === true) {
                                if (resp.data[x].approvalStatus === "pending") {
                                    table[x] = createProduct(resp.data, x);
                                }
                            } else {
                                if (resp.data[x].approvalStatus !== "accepted") {
                                    table[x] = createProduct(resp.data, x);
                                }
                            }
                        }

                    } else {
                        //retrieve new products and set its values to product state field

                        for (let x in resp.data) {
                            if (props.admin === true && props.adminMode === true) {
                                if (resp.data[x].approvalStatus === "rejected") {
                                    table[x] = createProduct(resp.data, x);
                                }
                            } else {
                                let lastWeekBefore = new Date();
                                lastWeekBefore.setDate(lastWeekBefore.getDate() - 7);
                                let creationDate = new Date(resp.data[x].creationDate);

                                if (creationDate >= lastWeekBefore && resp.data[x].approvalStatus === "accepted") {
                                    table[x] = createProduct(resp.data, x);
                                }
                            }
                        }

                    }
                    setState({
                        ...state,
                        products: table,
                        categories: categoriesTable,
                        loaded: true
                    });

                })
                .catch(error => console.log(error))

        }, [state.products_group, props.match.params.msg, state.search, state.category, props.adminMode]
    );

    const createProduct = (data, x) => {
        let product = {};
        product.product_id = data[x].productId;
        product.product_name = data[x].productName;
        product.product_category = data[x].category.categoryName;
        product.product_author = data[x].owner.name + " " + data[x].owner.surname;
        product.product_favourite = data[x].productFavourite;
        product.product_image = data[x].productImage;
        let creationDate = new Date(data[x].creationDate);
        product.creation_date = creationDate.toLocaleDateString() + " " + creationDate.toLocaleTimeString();
        product.approval_status = data[x].approvalStatus;
        if (product.assessmentDate !== null) {
            let assessmentDate = new Date(data[x].assessmentDate);
            product.assessment_date = assessmentDate.toLocaleDateString() + " " + assessmentDate.toLocaleTimeString();
            product.reject_explanation = data[x].rejectExplanation;
        }
        return product;
    };

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

    const handleCloseConfirmationModal = () => {
        setState({
            ...state,
            open_confirmation_modal: false,
        });

    };

    let tab;

    if (props.admin === true && props.adminMode === true) {
        tab = <div className="unconfirmed_products_list">
            {state.products.map((product, index) => (
                <div key={index}>
                    <div id={"product" + product.product_id} className="unconfirmed_product">
                        <div className="product">
                            <div className="product_header">
                                <div className="product_name"
                                     onClick={event => handleProduct(event, product.product_id)}>
                                    {product.product_name}
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
                                </div>
                            </div>
                            <div className="creation_date" onClick={event => handleProduct(event, product.product_id)}>
                                {"created " + product.creation_date}
                            </div>

                            <div className="product_content">
                                <div className="product_image_container">
                                    <img src={product.product_image} alt={product.product_name}
                                         className="product_image"/>
                                </div>
                                <div className="product_description">

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
                                </div>
                            </div>
                        </div>
                        <div className="product_status">
                            <div className="unconfirmed_header">Status</div>
                            <Divider variant="fullWidth"/>
                            <div
                                className={product.approval_status === "accepted" ? "accepted_product unconfirmed_body" : (product.approval_status === "pending" ? "pending_product unconfirmed_body" : "rejected_product unconfirmed_body")}>
                                {product.approval_status.toUpperCase()}
                            </div>
                        </div>
                        {product.approval_status === "pending"
                            ?
                            <div className="assessment_details">
                                <div>
                                    <Button name="accept" variant="contained" className="accept_button"
                                            onClick={(event) => handleAssess(event, product.product_id, product.product_name)}>Accept</Button>
                                </div>
                                <div>
                                    <Button name="reject" variant="contained" className="reject_button"
                                            onClick={event => handleAssess(event, product.product_id, product.product_name)}>Reject</Button>
                                </div>

                            </div>
                            :
                            <div className="assessment_details">
                                <div className="assessment_date">
                                    <div className="unconfirmed_header">Assessment Date</div>
                                    <Divider variant="fullWidth"/>
                                    <div className="unconfirmed_body">{product.assessment_date}</div>
                                </div>
                                {product.approval_status !== "accepted"
                                    ?
                                    <div className="reject_explanation">
                                        <div className="unconfirmed_header">Rejection explanation</div>
                                        <Divider variant="fullWidth"/>
                                        <div className="unconfirmed_body">{product.reject_explanation}</div>
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
                onClose={handleCloseConfirmationModal}
                complement={state.complement}
                productId={state.confirmation_product_id}
                productName={state.confirmation_product_name}
                history={props.history}
            />
        </div>;

    } else {
        switch (state.products_group) {
            case 0:
            case 2:
                tab = <div className="products_list">
                    <Grid container>
                        {state.products.map((product, index) => (
                            <Grid item key={index} id={"product" + product.product_id} className="product">
                                <div className="product_header">
                                    <div className="product_name"
                                         onClick={event => handleProduct(event, product.product_id)}>
                                        {product.product_name}
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
                                    </div>
                                </div>
                                <div className="creation_date"
                                     onClick={event => handleProduct(event, product.product_id)}>
                                    {"created " + product.creation_date}
                                </div>

                                <div className="product_content">
                                    <div className="product_image_container">
                                        <img src={product.product_image} alt={product.product_name}
                                             className="product_image"/>
                                    </div>
                                    <div className="product_description">

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
                                    </div>
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
                                <div className="product">
                                    <div className="product_header">
                                        <div className="product_name"
                                             onClick={event => handleProduct(event, product.product_id)}>
                                            {product.product_name}
                                        </div>
                                        <div className="product_buttons">
                                            <Tooltip title="Delete" aria-label="delete">
                                                <IconButton aria-label="delete" className="product_icon_button"
                                                            onClick={event => handleRemove(event, index)}>
                                                    <DeleteIcon fontSize="small"/>
                                                </IconButton>
                                            </Tooltip>
                                            <Tooltip title="Edit" aria-label="edit">
                                                <IconButton type="button" aria-label="edit"
                                                            className="product_icon_button"
                                                            onClick={(event) => handleEdit(event, product.product_id)}>
                                                    <EditIcon fontSize="small"/>
                                                </IconButton>
                                            </Tooltip>
                                        </div>
                                    </div>
                                    <div className="creation_date"
                                         onClick={event => handleProduct(event, product.product_id)}>
                                        {"created " + product.creation_date}
                                    </div>

                                    <div className="product_content">
                                        <div className="product_image_container">
                                            <img src={product.product_image} alt={product.product_name}
                                                 className="product_image"/>
                                        </div>
                                        <div className="product_description">

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
                                        </div>
                                    </div>
                                </div>
                                <div className="product_status">
                                    <div className="unconfirmed_header">Status</div>
                                    <Divider variant="fullWidth"/>
                                    <div
                                        className={product.approval_status === "pending" ? "pending_product unconfirmed_body" : "rejected_product unconfirmed_body"}>
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

    }

    return (
        <Container id="main_container" maxWidth="lg">
            <div className="page_container">
                <h2>Products</h2>
                <div className="toolbar_container">

                    {props.admin === true && props.adminMode === true ?
                        <Tabs
                            name="products_group"
                            value={state.products_group}
                            indicatorColor="primary"
                            textColor="inherit"
                            onChange={handleTab}
                            aria-label="product groups buttons"
                        >
                            <Tab className="product_group_tab" label="Accepted"/>
                            <Tab className="product_group_tab" label="Pending"/>
                            <Tab className="product_group_tab" label="Rejected"/>
                        </Tabs>
                        :
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
                                    <MenuItem key={index}
                                              value={category.category_name}>{category.category_name}</MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                    </div>
                    <Fab className="add_button" aria-label="add" onClick={handleAddNewProduct}>
                        <AddIcon/>
                    </Fab>
                </div>
                {state.msg !== "" ? <div className="msg">{state.msg}</div> : null}
                {state.products.length === 0 ?
                    <div className="loading">{!state.loaded ? "Loading" : "No products found"}</div> : null}
                {/*<Backdrop className={classes.backdrop} open={!props.loaded}>*/}
                {/*<CircularProgress color="inherit" />*/}
                {/*</Backdrop>*/}
                <BorderLinearProgress variant="determinate" value={90}/>
                {tab}
            </div>
        </Container>
    );
}