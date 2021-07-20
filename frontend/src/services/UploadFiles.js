import http from "../http-common";

class UploadFiles {

    upload(file, type, id) {
        let formData = new FormData();

        formData.append("file", file);

        return http.post("/api/images/upload/" + type + "/" + id, formData, {
            headers: {
                "Content-Type": "multipart/form-data",
            },
        });
    }

}

export default new UploadFiles();