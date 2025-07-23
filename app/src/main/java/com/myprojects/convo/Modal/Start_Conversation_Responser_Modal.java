package com.myprojects.convo.Modal;

public class Start_Conversation_Responser_Modal {


    /**
     * status : success
     * message : New conversation started.
     * data : {"conversation_id":105}
     */

    private String status;
    private String message;
    private data_return data;

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

    public data_return getData() {
        return data;
    }

    public void setData(data_return data) {
        this.data = data;
    }

    public static class data_return {
        /**
         * conversation_id : 105
         */

        private int conversation_id;

        public int getConversation_id() {
            return conversation_id;
        }

        public void setConversation_id(int conversation_id) {
            this.conversation_id = conversation_id;
        }
    }
}
