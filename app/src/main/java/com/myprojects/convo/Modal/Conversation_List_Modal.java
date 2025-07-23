package com.myprojects.convo.Modal;

import java.util.List;

public class Conversation_List_Modal {

    /**
     * status : success
     * message : Conversations fetched successfully.
     * data : {"conversations":[{"conversation_id":1,"chat_with_user":"golu","last_message_preview":"hello diksha kaise ho","last_message_timestamp":"2025-07-12 20:13:35"}]}
     */

    private String status;
    private String message;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<ConversationsBean> conversations;

        public List<ConversationsBean> getConversations() {
            return conversations;
        }

        public void setConversations(List<ConversationsBean> conversations) {
            this.conversations = conversations;
        }

        public static class ConversationsBean {
            /**
             * conversation_id : 1
             * chat_with_user : golu
             * last_message_preview : hello diksha kaise ho
             * last_message_timestamp : 2025-07-12 20:13:35
             */

            private int conversation_id;
            private String chat_with_user;
            private String last_message_preview;
            private String last_message_timestamp;

            public int getConversation_id() {
                return conversation_id;
            }

            public void setConversation_id(int conversation_id) {
                this.conversation_id = conversation_id;
            }

            public String getChat_with_user() {
                return chat_with_user;
            }

            public void setChat_with_user(String chat_with_user) {
                this.chat_with_user = chat_with_user;
            }

            public String getLast_message_preview() {
                return last_message_preview;
            }

            public void setLast_message_preview(String last_message_preview) {
                this.last_message_preview = last_message_preview;
            }

            public String getLast_message_timestamp() {
                return last_message_timestamp;
            }

            public void setLast_message_timestamp(String last_message_timestamp) {
                this.last_message_timestamp = last_message_timestamp;
            }
        }
    }
}
