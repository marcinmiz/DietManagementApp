import React, {useEffect} from 'react';
import Button from '@material-ui/core/Button';
import DialogTitle from '@material-ui/core/DialogTitle';
import DialogContent from '@material-ui/core/DialogContent';
import DialogActions from '@material-ui/core/DialogActions';
import Dialog from '@material-ui/core/Dialog';
import http from "../http-common";

export default function FinishedDietaryProgrammeDialog(props) {
    const {onClose, open, currentDietaryProgramme, daysDifference, ...other} = props;
    const [msg, setMsg] = React.useState("Loading");

    useEffect(
        async () => {
            try {
                if (currentDietaryProgramme != null && daysDifference > 0) {
                    const res = await http.put("/api/programmes/finishDietaryProgramme/" + currentDietaryProgramme.dietaryProgrammeId);
                    const data = res.data;
                    setMsg(data.message);
                }

            } catch (err) {
                console.log(err);
                if (currentDietaryProgramme != null) {
                    setMsg("Error while finishing Dietary programme named " + currentDietaryProgramme.dietaryProgrammeName);
                }
            }
        }, [props.adminMode, daysDifference]
    );

    return (

        <Dialog
            maxWidth="xs"
            aria-labelledby="confirmation-dialog-title"
            open={open}
            {...other}
        >
            <DialogTitle id="confirmation-dialog-title" className="confirmation_el">Congratulations</DialogTitle>
            <DialogContent dividers className="confirmation_el">
                {
                    <div>{msg}</div>
                }
            </DialogContent>
            <DialogActions>
                <Button onClick={onClose} color="primary">
                    Close
                </Button>
            </DialogActions>
        </Dialog>

    );
}