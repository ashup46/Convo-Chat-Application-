package com.myprojects.convo.Modal;

public class Get_BothId_Modal {

    /**
     * status : success
     * message : User IDs fetched successfully.
     * user1_id : 6
     * user2_id : 10
     */

    private String status;
    private String message;
    private int user1_id;
    private int user2_id;

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

    public int getUser1_id() {
        return user1_id;
    }

    public void setUser1_id(int user1_id) {
        this.user1_id = user1_id;
    }

    public int getUser2_id() {
        return user2_id;
    }

    public void setUser2_id(int user2_id) {
        this.user2_id = user2_id;
    }
}
