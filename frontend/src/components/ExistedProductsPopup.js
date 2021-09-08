import React, {useEffect} from 'react';
import {Divider} from "@material-ui/core";
import http from "../http-common";

export default function ExistedProductsPopup(props) {

    const {open, product_name, ...other} = props;

    const [state, setState] = React.useState({
        existed_products: [],
    });

    useEffect(
        async () => {
            if (product_name !== undefined && product_name !== "") {
                let search_parameters = {};
                search_parameters.phrase = product_name;
                search_parameters.category = "";

                http.post("/api/products/search", search_parameters)
                    .then(resp => {
                        let search = [];
                        for (let x in resp.data) {
                            search[x] = resp.data[x].productName;
                        }

                        setState({
                            ...state,
                            existed_products: search,
                        });

                    })
                    .catch(error => console.log(error))
            } else {
                setState({
                    ...state,
                    existed_products: [],
                });
            }
        }, [product_name]);

    return (
        <div>
            {state.existed_products.length > 0 && open && <div className="products_existed_container">
                <h4>Products yet existed</h4>
                {state.existed_products.map((product, index) => (
                    <div key={index}>
                        <div className="product_existed">{product}</div>
                        {index !== state.existed_products.length - 1 ? <Divider variant="middle"/> : null}
                    </div>
                ))}
            </div>}
        </div>
    );
}