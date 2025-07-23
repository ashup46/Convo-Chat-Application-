package com.myprojects.convo.Database_Setup;

import com.myprojects.convo.Modal.Conversation_List_Modal;
import com.myprojects.convo.Modal.Get_BothId_Modal;
import com.myprojects.convo.Modal.Get_Ids_Modal;
import com.myprojects.convo.Modal.Personal__message_List;
import com.myprojects.convo.Modal.Start_Conversation_Responser_Modal;
import com.myprojects.convo.Modal.UserIdModal;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api_Client {

    @FormUrlEncoded
    @POST("convo_db/update_userinfo.php")
    Call<Response_Modal> addUserInfo(@Field("username") String username
                                    ,@Field("email") String email
                                    ,@Field("password") String password);

    @FormUrlEncoded
    @POST("convo_db/verify_login_detail.php")
    Call<Response_Modal> verifyLogin(@Field("email") String email
                                    ,@Field("password") String password);

    @GET("convo_db/get_id_from_userinfo.php")
    Call<Get_Ids_Modal> getBothIds(@Query("user1_username") String user1_username
                                    ,@Query("user2_username") String user2_username);
    @FormUrlEncoded
    @POST("convo_db/start_conversation.php")
    Call<Start_Conversation_Responser_Modal> addindConversationTable(@Field("user1_id") int user1_id
                                                                        ,@Field("user2_id") int user2_id);


    @FormUrlEncoded
    @POST("convo_db/send_message.php")
    Call<ResponseBody> sendMessage(@Field("conversation_id") int conversation_id
                                    ,@Field("sender_id") int sender_id
                                    ,@Field("message_text") String message_text);



    @GET("convo_db/get_conversations.php")
    Call<Conversation_List_Modal> getConversationList(@Query("user_id") int user_id);



    @GET("convo_db/get_currentUser_id.php")
    Call<UserIdModal> getCurrentUserid(@Query("current_user_email") String current_user_email);



    @GET("convo_db/get_messages.php")
    Call<Personal__message_List> getPersonalMessage(@Query("conversation_id") int conversation_id);


    @GET("convo_db/get_userIdsFromConversationId.php")
    Call<Get_BothId_Modal> getBothIds(@Query("conversation_id") int conversation_id);
}
