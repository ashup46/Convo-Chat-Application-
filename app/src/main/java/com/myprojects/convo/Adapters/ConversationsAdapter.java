package com.myprojects.convo.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myprojects.convo.ChatPersonal;
import com.myprojects.convo.Modal.Conversation_List_Modal;
import com.myprojects.convo.R;

import java.util.List;

public class ConversationsAdapter extends RecyclerView.Adapter<ConversationsAdapter.HolderView> {

  private   Context context;

    private List<Conversation_List_Modal.DataBean.ConversationsBean> conversationList;

    private int currentUserid;

    private String currentUserName;

    private int user_id_2;

    public ConversationsAdapter(Context context, List<Conversation_List_Modal.DataBean.ConversationsBean> conversationList, int currentUserid, int user_id_2, String currentUserName) {
        this.context = context;
        this.conversationList = conversationList;
        this.currentUserid = currentUserid;
        this.currentUserName =currentUserName;
        this.user_id_2 = user_id_2;
    }

    @NonNull
    @Override
    public ConversationsAdapter.HolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.contact_chat,parent,false);

        HolderView hv = new HolderView(view);


        return hv;
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationsAdapter.HolderView holder, int position) {

      String username =conversationList.get(position).getChat_with_user();
      holder.getUserName().setText(username.toUpperCase());
        holder.getLastMessage().setText(conversationList.get(position).getLast_message_preview());
        holder.getLastMessageTime().setText(conversationList.get(position).getLast_message_timestamp());


        int conversation_id = conversationList.get(position).getConversation_id();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ChatPersonal.class);
                intent.putExtra("conversation_id",conversation_id);
                intent.putExtra("friend_name",username);
                intent.putExtra("currentUserId",currentUserid);
                intent.putExtra("currentUserName",currentUserName);
                intent.putExtra("user_id_2",user_id_2);
                context.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }

    public class HolderView extends RecyclerView.ViewHolder {

       private TextView userName,lastMessage,lastMessageTime;
        public HolderView(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.chat_friend_name);
            lastMessage = itemView.findViewById(R.id.last_friend_chat);
            lastMessageTime = itemView.findViewById(R.id.last_friend_chat_time);
        }

        public TextView getLastMessage() {
            return lastMessage;
        }

        public TextView getLastMessageTime() {
            return lastMessageTime;
        }

        public TextView getUserName() {
            return userName;
        }
    }
}
