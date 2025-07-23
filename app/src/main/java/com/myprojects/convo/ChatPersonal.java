package com.myprojects.convo;

import static com.myprojects.convo.Database_Setup.WebSocketManager.getWebSocketManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.myprojects.convo.Adapters.PersonalConversationAdapter;
import com.myprojects.convo.Database_Setup.Api_Client;
import com.myprojects.convo.Database_Setup.Retrofit_init;
import com.myprojects.convo.Database_Setup.WebSocketManager;
import com.myprojects.convo.Modal.Get_BothId_Modal;
import com.myprojects.convo.Modal.Personal__message_List;
import com.myprojects.convo.Values.Values;
import com.myprojects.convo.databinding.ActivityChatPersonalBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatPersonal extends AppCompatActivity implements WebSocketManager.WebSocketListner {


    private static final String TAG = "ChatPersonal";
    private ActivityChatPersonalBinding binding;

    private RecyclerView recyclerViewList;

    private TextInputEditText writeMessage;
    private MaterialButton Message_send_button;

    private String friend_name,currentUsername;

    private int conversation_id,currentUserId,user_id_2;

    private MaterialToolbar actionToolbar;

    private PersonalConversationAdapter personalConversationAdapter;

private WebSocketManager webSocketManager;

    private List<Personal__message_List.DataBean.MessagesBean> messagesBeanList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityChatPersonalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        recyclerViewList = binding.chatReyclerViewId;
        writeMessage = binding.messageEdtTxt;
        Message_send_button = binding.sendBtn;
        actionToolbar=binding.actionToolbarId;
        Intent intent = getIntent();
        messagesBeanList =  new ArrayList<>();



        friend_name = intent.getStringExtra("friend_name");
        conversation_id = intent.getIntExtra("conversation_id",0);
        currentUserId=intent.getIntExtra("currentUserId",0);
        currentUsername=intent.getStringExtra("currentUserName");
        Log.d(TAG, "WebSocket Server URL being used: " + Values.WS_SERVER_URL);
        getBothUserIds(conversation_id);

//        For Setting Action bar
        setSupportActionBar(actionToolbar);

        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(friend_name.toUpperCase().toString());
        }

        Log.d("ChatPersonal","Friend Name : " + friend_name);
        Log.d("ChatPersonal","conversation_id : " + conversation_id);
        Log.d("ChatPersonal","currentUserId : " + currentUserId);



//        Showing The list of the Personal Conversation
        webSocketManager = getWebSocketManager(Values.WS_SERVER_URL);
        webSocketManager.setListner(this);
        Api_Client apiClient = Retrofit_init.getRetrofit().create(Api_Client.class);
        fetchPersonalConversation(apiClient);

// This is For Sending Message to the Friend

        Message_send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String messageText = writeMessage.getText().toString();


                if (messageText.isEmpty())
                {
                    return;
                }


                Call<ResponseBody> callSendMessage = apiClient.sendMessage(conversation_id,currentUserId,messageText);

                callSendMessage.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful())
                        {
                            String messageContent = messageText;
                            long timestamp = System.currentTimeMillis()/1000L;
                            String timeFormat = convertUnixTimestampToDateTime(timestamp);
                            Toast.makeText(ChatPersonal.this, "Message Sent", Toast.LENGTH_SHORT).show();
                            Personal__message_List.DataBean.MessagesBean chatmessage = new Personal__message_List.DataBean.MessagesBean(0,currentUserId,currentUsername,messageText,timeFormat);
                            messagesBeanList.add(chatmessage);
                            personalConversationAdapter.notifyItemChanged(messagesBeanList.size()-1);
                            recyclerViewList.scrollToPosition(messagesBeanList.size()-1);
                            sendMessageViaWebsocket(messageText);
//                            fetchPersonalConversation(apiClient);
                            writeMessage.setText("");

                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(ChatPersonal.this, "Message Not Sent", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void getBothUserIds(int conversationId) {

        Api_Client apiClient = Retrofit_init.getRetrofit().create(Api_Client.class);
        Call<Get_BothId_Modal> callBothIds = apiClient.getBothIds(conversationId);

        callBothIds.enqueue(new Callback<Get_BothId_Modal>() {
            @Override
            public void onResponse(Call<Get_BothId_Modal> call, Response<Get_BothId_Modal> response) {
                if (response.isSuccessful())
                {

                    Get_BothId_Modal getBothIdModal = response.body();



                    if (response.body().getUser1_id() == currentUserId )
                    {
                        user_id_2 = response.body().getUser2_id();
                    }
                    else
                    {
                        user_id_2 = response.body().getUser1_id();
                    }


                }
            }

            @Override
            public void onFailure(Call<Get_BothId_Modal> call, Throwable t) {

            }
        });


    }

//    Sending message Via Websocket

    private void sendMessageViaWebsocket(String messageContent)
    {
//        Message_send_button.setEnabled(false);
        Toast.makeText(this, "Sending Message.....", Toast.LENGTH_SHORT).show();
        if (webSocketManager.isConnected())
        {
            Log.e("ChatPersonal", "Message Sent:  " + messageContent);
            long timestamp = System.currentTimeMillis()/1000L;
            Personal__message_List.DataBean.MessagesBean chatmessage = new Personal__message_List.DataBean.MessagesBean("message",currentUserId,currentUsername,currentUserId,user_id_2,messageContent,timestamp);
            Log.d("ChatPersonal","OnResponse user_id_2 : " + user_id_2);
            webSocketManager.sendMessage(chatmessage);
            writeMessage.setText("");
        }
        else
        {
            Toast.makeText(this, "Not Connected to chat Server. Please wait.", Toast.LENGTH_SHORT).show();
            Log.e("ChatPersonal", "Attempted to send message while disconnected.");
            Message_send_button.setEnabled(true);
            webSocketManager.connect();
        }


    }


//    This Method is for Fetching the Personal Conversation
    private void fetchPersonalConversation(Api_Client apiClient) {
        Call<Personal__message_List> callGetPersonalMessage = apiClient.getPersonalMessage(conversation_id);
        callGetPersonalMessage.enqueue(new Callback<Personal__message_List>() {
            @Override
            public void onResponse(Call<Personal__message_List> call, Response<Personal__message_List> response) {
                if (response.isSuccessful() & response.body()!=null & response.body().getData() != null )
                {
                    messagesBeanList = response.body().getData().getMessages();

                    showingPersonalConversaton(recyclerViewList,currentUserId,messagesBeanList);
                }
            }

            @Override
            public void onFailure(Call<Personal__message_List> call, Throwable t) {
                Toast.makeText(ChatPersonal.this, "I On Response Not Successful", Toast.LENGTH_SHORT).show();
            }
        });

    }

//    This is for Showing the Personal Conversation
    private void showingPersonalConversaton(RecyclerView recyclerViewList, int currentUserId, List<Personal__message_List.DataBean.MessagesBean> messages) {

        recyclerViewList.setLayoutManager(new LinearLayoutManager(ChatPersonal.this));

        personalConversationAdapter = new PersonalConversationAdapter(ChatPersonal.this,messages,currentUserId);

        recyclerViewList.setAdapter(personalConversationAdapter);
    }

    //    This method is When the message is Recived
    @Override
    public void onMessageReceived(Personal__message_List.DataBean.MessagesBean messagesBean) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!messagesBeanList.isEmpty() && (messagesBean.getSender_id()!=currentUserId))
                {
                    Message_send_button.setEnabled(true);
                    messagesBeanList.add(messagesBean);
                    personalConversationAdapter.notifyItemChanged(messagesBeanList.size()-1);
                    recyclerViewList.scrollToPosition(messagesBeanList.size()-1);

                }

            }
        });

    }


//    Websocket Listner Method +++++++++++++++++++++++++++++++++++++++++++++
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ChatPersonal.this, "Connected to chat server!", Toast.LENGTH_SHORT).show();
                webSocketManager.sendAuthMessage(currentUserId);
            }
        });

    }



    @Override
    public void onDisconnected(String reason) {
        runOnUiThread(() -> {
//            connectionStatusText.setText("Disconnected");
            Toast.makeText(ChatPersonal.this, "Disconnected: " + reason, Toast.LENGTH_SHORT).show();
        });


    }

    @Override
    public void onError(String message) {
        runOnUiThread(() -> {
//            connectionStatusText.setText("Error");
            Toast.makeText(ChatPersonal.this, "WebSocket Error: " + message, Toast.LENGTH_LONG).show();
            Log.e(TAG, "WebSocket Error: " + message);
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        // App foreground में आने पर WebSocket connection check करें और कनेक्ट करें
        if (!webSocketManager.isConnected()) {
            webSocketManager.connect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // App background में जाने पर WebSocket connection को डिस्कनेक्ट कर सकते हैं
        // या इसे खुला रख सकते हैं अगर आप बैकग्राउंड में भी मैसेज प्राप्त करना चाहते हैं (इसके लिए और कॉन्फ़िगरेशन की आवश्यकता होगी)
        // webSocketManager.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webSocketManager.disconnect(); // Activity destroy होने पर WebSocket connection बंद करें
    }


//Date Formate Change

    private String convertUnixTimestampToDateTime(long unixTimestamp) {
        // Unix timestamp seconds mein hota hai, lekin Date class milliseconds expect karti hai.
        // Isliye, timestamp ko 1000 se multiply karein.
        Date date = new Date(unixTimestamp * 1000L);

        // SimpleDateFormat ka instance banayein desired format ke saath
        // Locale.getDefault() ka upyog karein user ke device ki default locale ke liye
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        // Date object ko formatted string mein convert karein
        return sdf.format(date);
    }
}