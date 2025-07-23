package com.myprojects.convo.Modal;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Personal__message_List {

    private String status;
    private String message;
    private DataBean data;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public DataBean getData() { return data; }
    public void setData(DataBean data) { this.data = data; }

    public static class DataBean {
        private List<MessagesBean> messages;
        public List<MessagesBean> getMessages() { return messages; }
        public void setMessages(List<MessagesBean> messages) { this.messages = messages; }

        public static class MessagesBean {
            // PHP API से आने वाले फ़ील्ड्स
            @SerializedName("id")
            private int id;
            @SerializedName("sender_id")
            private int sender_id;
            @SerializedName("sender_username")
            private String sender_username;
            @SerializedName("message_text")
            private String message_text;

            // WebSocket Server JSON Format के अनुसार नए फ़ील्ड्स
            @SerializedName("msgContent")
            private String msgContent;
            @SerializedName("senderWsId")
            private int senderWsId;

            @SerializedName("recipientWsId")
            private int recipientWsId;

            @SerializedName("senderUsernameWs")
            private String senderUsernameWs;
            // WebSocket sender ID (e.g., "client_abc123" or username)
            @SerializedName("type")
            private String type; // e.g., "system", "message"

            // Timestamp Handling (PHP से string, WebSocket से long)
            @SerializedName("timestamp")
            private Object timestampRaw; // Raw timestamp (string or long)
            private transient long parsedTimestampSeconds; // Parsed long timestamp for internal use

            // Constructors
            public MessagesBean() {} // Default constructor for Gson

            // Constructor for PHP API messages
            public MessagesBean(int id, int sender_id, String sender_username, String message_text, String timestamp) {
                this.id = id;
                this.sender_id = sender_id;
                this.sender_username = sender_username;
                this.message_text = message_text;
                this.timestampRaw = timestamp;
                this.type = "message"; // Default type for PHP messages
                try { // Parse timestamp for internal use
                    SimpleDateFormat phpSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    Date date = phpSdf.parse((String) timestampRaw);
                    this.parsedTimestampSeconds = date.getTime() / 1000L;
                } catch (Exception e) {
                    this.parsedTimestampSeconds = System.currentTimeMillis() / 1000L; // Fallback
                }
            }
//===============================================================================================================================================
            // Constructor for WebSocket messages (या client-side पर मैसेज भेजने के लिए)
            public MessagesBean(String type,int sender_id,String senderUsernameWs, int senderWsId,int recipientWsId, String msgContent, long timestampSeconds) {
                this.type = type;
                this.senderWsId = senderWsId;
                this.msgContent = msgContent;
                this.parsedTimestampSeconds = timestampSeconds;
                this.timestampRaw = timestampSeconds;
                this.senderUsernameWs = senderUsernameWs;
                this.recipientWsId = recipientWsId;
                this.sender_id = sender_id;// Store raw long for Gson to serialize if needed
            }

            // Getters and Setters
            public int getId() { return id; }
            public void setId(int id) { this.id = id; }
            public int getSender_id() { return sender_id; }
            public void setSender_id(int sender_id) { this.sender_id = sender_id; }

            // Unified method to get sender's display name
            /*public String getUnifiedSender() { return String.valueOf(senderWsId)!= null) ? senderWsId : sender_username; }*/
            public String getSender_username() { return sender_username; } // PHP API specific
            public void setSender_username(String sender_username) { this.sender_username = sender_username; }

            // Unified method to get message msgContent
            public String getUnifiedContent() { return (msgContent != null) ? msgContent : message_text; }
            public String getMessage_text() { return message_text; } // PHP API specific
            public void setMessage_text(String message_text) { this.message_text = message_text; }

            // Unified method to get timestamp (हमेशा long seconds में)
            public long getUnifiedTimestampSeconds() {
                if (parsedTimestampSeconds != 0) { return parsedTimestampSeconds; }
                if (timestampRaw instanceof String) {
                    try {
                        SimpleDateFormat phpSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        Date date = phpSdf.parse((String) timestampRaw);
                        this.parsedTimestampSeconds = date.getTime() / 1000L;
                        return this.parsedTimestampSeconds;
                    } catch (Exception e) { return System.currentTimeMillis() / 1000L; }
                }
                if (timestampRaw instanceof Double) { return ((Double) timestampRaw).longValue(); }
                if (timestampRaw instanceof Long) { return (Long) timestampRaw; }
                return 0;
            }

            public String getType() { return type; }
            public void setType(String type) { this.type = type; }
            public Object getTimestampRaw() { return timestampRaw; }
            public void setTimestampRaw(Object timestampRaw) { this.timestampRaw = timestampRaw; }
            public int getSenderWsId() { return senderWsId; }
            public void setSenderWsId(int senderWsId) { this.senderWsId = senderWsId; }
        }
    }
}
