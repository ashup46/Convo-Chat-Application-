package com.myprojects.convo;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.myprojects.convo.Adapters.ConversationsAdapter;
import com.myprojects.convo.Database_Setup.Api_Client;
import com.myprojects.convo.Database_Setup.Retrofit_init;
import com.myprojects.convo.Modal.Conversation_List_Modal;
import com.myprojects.convo.Modal.Get_Ids_Modal;
import com.myprojects.convo.Modal.Start_Conversation_Responser_Modal;
import com.myprojects.convo.Modal.UserIdModal;
import com.myprojects.convo.databinding.ActivityMainBinding;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private RecyclerView recyclerView;

    private FloatingActionButton floatingActionButton;


    private EditText enterRecipentId,enterNewMessage;

    private MaterialButton sendMessage,cancelMessage;


    private  String currentUsername;

    private int user_id_1,user_id_2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        currentUsername = getIntent().getStringExtra("useremail");
        recyclerView = binding.allChatRecyclerViewId;
        floatingActionButton = binding.floatingActionButton;

//        This is Initialization of the ApiClient
        Api_Client apiClient = Retrofit_init.getRetrofit().create(Api_Client.class);
//        This is is for getting the userId and then further conversation list
        getCurrentUserId(currentUsername,apiClient);

        Log.d("mainActivityContext","Current User Email : " + currentUsername);





        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomeDialog(MainActivity.this,apiClient);
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

//    This is Method is for getting the Current User UserId
    private void getCurrentUserId(String currentUsername, Api_Client apiClient) {

            Call<UserIdModal> callGetCurrentUserId = apiClient.getCurrentUserid(currentUsername);

        final int[] userid = new int[1];
            callGetCurrentUserId.enqueue(new Callback<UserIdModal>() {
                @Override
                public void onResponse(Call<UserIdModal> call, Response<UserIdModal> response) {

                    if (response.isSuccessful())
                    {
                       int userid = response.body().getData().getUser_id();
                        Log.d("mainActivityContext","Current User Id In onResponse : " + userid);

                        showCoversationList(userid,apiClient);

                    }



                }

                @Override
                public void onFailure(Call<UserIdModal> call, Throwable t) {

                }
            });

    }

    private void showCoversationList(int userid, Api_Client apiClient) {

        Call<Conversation_List_Modal> callConversationList = apiClient.getConversationList(userid);

        callConversationList.enqueue(new Callback<Conversation_List_Modal>() {
            @Override
            public void onResponse(Call<Conversation_List_Modal> call, Response<Conversation_List_Modal> response) {
                if (response.isSuccessful())
                {
                    Log.d("mainActivityContext","Conversation OnResponse ");

                    Conversation_List_Modal conversationListModal = response.body();



                    Log.d("mainActivityContext"," " + conversationListModal.getData().getConversations().get(0).getChat_with_user());
                    Log.d("mainActivityContext"," " + conversationListModal.getData().getConversations().get(0).getLast_message_preview());
                    Log.d("mainActivityContext"," " + conversationListModal.getData().getConversations().get(0).getLast_message_timestamp());

                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                    ConversationsAdapter conversationsAdapter =  new ConversationsAdapter(MainActivity.this
                                                                                        ,conversationListModal.getData().getConversations()
                                                                                            ,userid,user_id_2,currentUsername);

                            recyclerView.setAdapter(conversationsAdapter);

                }
            }

            @Override
            public void onFailure(Call<Conversation_List_Modal> call, Throwable t) {
                Log.d("mainActivityContext","Conversation OnFailure ");
            }
        });

    }

    //    This Method is for Showing the Dailog Box of Sending new message,Start conversation.
    private void showCustomeDialog(Context context, Api_Client apiClient) {

        AlertDialog.Builder alertDailogBuilder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View dailogView = layoutInflater.inflate(R.layout.new_message_dialogbox,null);
        alertDailogBuilder.setView(dailogView);

        AlertDialog dialogalert = alertDailogBuilder.create();

        dialogalert.setCancelable(false);

        enterRecipentId = dailogView.findViewById(R.id.recipent_id_dailog);
        enterNewMessage = dailogView.findViewById(R.id.enter_starting_message_dailog);
        sendMessage = dailogView.findViewById(R.id.send_message_btn_dailog);
        cancelMessage = dailogView.findViewById(R.id.canel_button_dailog);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


// sbse phle di hui recipent id se ID nikaal k fir isme pass krni h dono ki sendr and reciever

//                Call<Get_Ids_Modal> call_getIds = apiClient.getBothIds(currentUsername,enterRecipentId.getText().toString());
                Log.d("mainActivityContext",currentUsername);

//                This Call is for Getting the Ids of the Reciver and Sender
                Call<Get_Ids_Modal> call_getIds = apiClient.getBothIds(currentUsername,enterRecipentId.getText().toString());

                call_getIds.enqueue(new Callback<Get_Ids_Modal>() {
                    @Override
                    public void onResponse(Call<Get_Ids_Modal> call, Response<Get_Ids_Modal> response) {

                        if (response.isSuccessful())
                        {
                            Get_Ids_Modal getIdsModal = response.body();

                            user_id_1 =  getIdsModal.getData().getUser1_id();
                            user_id_2 = getIdsModal.getData().getUser2_id();


//                            This call is for adding the conversation and getting the Conversation Id
                            Call<Start_Conversation_Responser_Modal> callstartConversation = apiClient.addindConversationTable(user_id_1,user_id_2);

                            callstartConversation.enqueue(new Callback<Start_Conversation_Responser_Modal>() {
                                @Override
                                public void onResponse(Call<Start_Conversation_Responser_Modal> call, Response<Start_Conversation_Responser_Modal> response) {


                                    if (response.isSuccessful())
                                    {
                                       Start_Conversation_Responser_Modal startConversationResponserModal = response.body();


                                       int conversation_id = startConversationResponserModal.getData().getConversation_id();

//                                        Toast.makeText(MainActivity.this, "OnResponse : " + conversation_id , Toast.LENGTH_SHORT).show();

//                                       This Call is for Adding the Send message in the Table
                                        Call<ResponseBody> responseBodyCall = apiClient.sendMessage(conversation_id,user_id_1,enterNewMessage.getText().toString());

                                        responseBodyCall.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                Toast.makeText(MainActivity.this, "============================" , Toast.LENGTH_LONG).show();
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                Log.d("mainActivityContext",t.getMessage());
                                                Toast.makeText(MainActivity.this, "+++++++++++++++++++++++" , Toast.LENGTH_LONG).show();
                                            }
                                        });

                                    }
                                }

                                @Override
                                public void onFailure(Call<Start_Conversation_Responser_Modal> call, Throwable t) {
                                    Toast.makeText(MainActivity.this, "Not OnResponse", Toast.LENGTH_SHORT).show();
                                }
                            });

                            dialogalert.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<Get_Ids_Modal> call, Throwable t) {


                        Toast.makeText(context, "Unable to Get Ids", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

        cancelMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogalert.dismiss();
            }
        });

        dialogalert.show();
    }
}