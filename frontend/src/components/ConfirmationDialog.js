import React, {useEffect} from 'react';
import PropTypes from 'prop-types';
import { makeStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import DialogTitle from '@material-ui/core/DialogTitle';
import DialogContent from '@material-ui/core/DialogContent';
import DialogActions from '@material-ui/core/DialogActions';
import Dialog from '@material-ui/core/Dialog';
import RadioGroup from '@material-ui/core/RadioGroup';
import Radio from '@material-ui/core/Radio';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import TextareaAutosize from "@material-ui/core/TextareaAutosize/TextareaAutosize";
import http from "../http-common";

export default function ConfirmationDialog(props) {
    const { onClose, open, complement, history, productId, productName, ...other } = props;
    const textareaRef = React.useRef(null);
    const [rejectExplanation, setRejectExplanation] = React.useState("");
    const [msg, setMsg] = React.useState("");

    useEffect(
        () => console.log(props)
    );

    const handleEntering = () => {
        if (textareaRef.current != null) {
            textareaRef.current.focus();
        }
    };

    const handleCancel = () => {
        onClose();
        setRejectExplanation("");
    };

    const handleOk = async () => {
        try {
            console.log(props.productId + " " + props.productName);
            let assessment_parameters = {};
            assessment_parameters.productId = productId;
            complement === "reject this product" ? assessment_parameters.assessment = "reject" : assessment_parameters.assessment = "accept";
            if (complement === "reject this product") {
                assessment_parameters.rejectExplanation = rejectExplanation;
            }
            console.log(assessment_parameters);
            const res = await http.post("/api/products/assess", assessment_parameters);
            const data = res.data;
            if ((data.message !== "Product " + productName + " has been accepted") && (data.message !== "Product " + productName + " has been rejected")) {
                setMsg(data.message);
            } else {
                onClose();
                if (data.message === "Product " + productName + " has been accepted") {
                    history.push("/products/" + productName.replace(" ", "_") + "-accepted");
                } else {
                    history.push("/products/" + productName.replace(" ", "_") + "-rejected");
                }
            }

        } catch (err) {
            console.log(err);
        }
    };

    const handleRejectExplanation = (event) => {
        console.log(event.target.value);
        setRejectExplanation(event.target.value);
    };

    return (

                <Dialog
                    maxWidth="xs"
                    onEntering={handleEntering}
                    aria-labelledby="confirmation-dialog-title"
                    open={open}
                    {...other}
                >
                    <DialogTitle id="confirmation-dialog-title" className="confirmation_el">{ complement === "reject this product" ? "Rejection Explanation" : "Confirmation"}</DialogTitle>
                    <DialogContent dividers className="confirmation_el">
                        {
                            msg !== "" ? <div>{msg}</div> : null
                        }
                        {
                            complement === "reject this product" ? <div><TextareaAutosize
                                ref={textareaRef}
                                aria-label="reject_explanation"
                                className="reject_explanation_textarea"
                                rowsMin={12}
                                cols={50}
                                placeholder="Why product should be rejected?"
                                onChange={event => handleRejectExplanation(event)}
                                value={rejectExplanation}
                            /><DialogTitle id="confirmation-dialog-title" className="confirmation_el">Confirmation</DialogTitle></div> : null
                        }

                        <div className="confirmation_el">Are you sure you want to {complement} ?</div>
                    </DialogContent>
                    <DialogActions>
                        <Button autoFocus onClick={handleCancel} color="primary">
                            Cancel
                        </Button>
                        <Button onClick={handleOk} color="primary" disabled={complement === "reject this product" && rejectExplanation === ""}>
                            Ok
                        </Button>
                    </DialogActions>
                </Dialog>

    );
}

ConfirmationDialog.propTypes = {
    onClose: PropTypes.func.isRequired,
    open: PropTypes.bool.isRequired,
    complement: PropTypes.string.isRequired,
};