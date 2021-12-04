import React from 'react';
import PropTypes from 'prop-types';
import Button from '@material-ui/core/Button';
import DialogTitle from '@material-ui/core/DialogTitle';
import DialogContent from '@material-ui/core/DialogContent';
import DialogActions from '@material-ui/core/DialogActions';
import Dialog from '@material-ui/core/Dialog';
import TextareaAutosize from "@material-ui/core/TextareaAutosize/TextareaAutosize";
import http from "../http-common";

export default function ConfirmationDialog(props) {
    const { type, onClose, open, complement, itemId, itemName, ...other } = props;
    const textareaRef = React.useRef(null);
    const [rejectExplanation, setRejectExplanation] = React.useState("");
    const [msg, setMsg] = React.useState("");

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
            let assessment_parameters = {};

            assessment_parameters.itemId = itemId;

            if(complement.startsWith("reject")) {
                assessment_parameters.assessment = "reject";
                assessment_parameters.rejectExplanation = rejectExplanation;
            } else if(complement.startsWith("accept")){
                assessment_parameters.assessment = "accept";
            } else {
                throw Error("Bad complement");
            }

            if (type === "product") {

                const res = await http.post("/api/products/assess", assessment_parameters);
                const data = res.data;
                if ((data.message !== "Product " + itemName + " has been accepted") && (data.message !== "Product " + itemName + " has been rejected")) {
                    setMsg(data.message);
                } else {

                    if (data.message === "Product " + itemName + " has been accepted") {
                        // onClose();
                        props.handleOperationMessage("Product " + itemName + " has been accepted", true);
                        setTimeout(() => onClose(),1000);
                    } else {
                        // onClose();
                        props.handleOperationMessage("Product " + itemName + " has been rejected", true);
                        setTimeout(() => onClose(),1000);
                    }
                }
            } else if (type === "recipe") {

                const res = await http.post("/api/recipes/assess", assessment_parameters);
                const data = res.data;
                if ((data.message !== "Recipe " + itemName + " has been accepted") && (data.message !== "Recipe " + itemName + " has been rejected")) {
                    setMsg(data.message);
                } else {

                    if (data.message === "Recipe " + itemName + " has been accepted") {
                        // onClose();
                        props.handleOperationMessage("Recipe " + itemName + " has been accepted", true);
                        // setTimeout(() => onClose(),1000);
                    } else {
                        // onClose();
                        props.handleOperationMessage("Recipe " + itemName + " has been rejected", true);
                        // setTimeout(() => onClose(),500);
                    }
                }
            } else {
                throw Error("Bad type");
            }

        } catch (err) {
            console.log(err);
        }
    };

    const handleRejectExplanation = (event) => {
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
                    <DialogTitle id="confirmation-dialog-title" className="confirmation_el">{ complement.startsWith("reject") ? "Rejection Explanation" : "Confirmation"}</DialogTitle>
                    <DialogContent dividers className="confirmation_el">
                        {
                            msg !== "" ? <div>{msg}</div> : null
                        }
                        {
                            complement.startsWith("reject") ? <div><TextareaAutosize
                                ref={textareaRef}
                                aria-label="reject_explanation"
                                className="reject_explanation_textarea"
                                rowsMin={12}
                                cols={50}
                                placeholder={type === "product" ? "Why product should be rejected?" : "Why recipe should be rejected?"}
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
                        <Button onClick={handleOk} color="primary" disabled={complement.startsWith("reject") && rejectExplanation === ""}>
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