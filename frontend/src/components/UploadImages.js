import React from "react";
import UploadService from "../services/UploadFiles";
import { Typography, Button} from '@material-ui/core';

export default function UploadImages ({submitted}) {
    const [state, setState] = React.useState({
        currentFile: undefined,
        previewImage: undefined,

        message: "",
        isError: false,
        imageInfos: [],
    })

    React.useEffect( () => {
        if (submitted) {
            console.log("submitted");
            //upload();
        }
        }
        , [submitted]
    )

    function selectFile(event) {
        setState({
            currentFile: event.target.files[0],
            previewImage: URL.createObjectURL(event.target.files[0]),
            message: ""
        });
        document.getElementById("upload_container").classList.add("upload_container_image_added")
    };

    function upload() {

        UploadService.upload(state.currentFile)
            .then((response) => {
                setState({
                    message: response.data.message,
                    isError: false
                });
                return UploadService.getFiles();
            })
            .then((files) => {
                setState({
                    imageInfos: files.data,
                });
            })
            .catch((err) => {
                setState({
                    message: "Could not upload the image!",
                    currentFile: undefined,
                    isError: true
                });
            });
    };
        // componentDidMount() {
        //     UploadService.getFiles().then((response) => {
        //         this.setState({
        //             imageInfos: response.data,
        //         });
        //     });
        // }
            const {
                currentFile,
                previewImage,
                message,
                imageInfos,
                isError
            } = state;

            return (
                <div id="upload_container" className="upload_container product_selected_element product_selected_element_moved">
                    <label htmlFor="btn-upload">
                        <input
                            id="btn-upload"
                            name="btn-upload"
                            style={{display: 'none'}}
                            type="file"
                            accept="image/*"
                            onChange={selectFile}/>
                        <Button
                            className="btn-choose"
                            variant="outlined"
                            component="span">
                            Choose Image
                        </Button>
                    </label>

                    {previewImage && (
                        <div>
                            <img className="upload_image" src={previewImage} alt=""/>
                        </div>
                    )}

                    <div>
                        {currentFile ? currentFile.name : null}
                    </div>

                    {message && (
                        <Typography variant="subtitle2" className={`upload-message ${isError ? "error" : ""}`}>
                            {message}
                        </Typography>
                    )}
                </div>
            );
}