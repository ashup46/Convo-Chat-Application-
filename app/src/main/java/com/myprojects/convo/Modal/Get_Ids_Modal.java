package com.myprojects.convo.Modal;

public class Get_Ids_Modal {

    /**
     * status : success
     * message : User IDs retrieved successfully.
     * data : {"user1_id":5,"user2_id":10}
     */

    private String status;
    private String message;
    private data_id data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public data_id getData() {
        return data;
    }

    public void setData(data_id data) {
        this.data = data;
    }

}
