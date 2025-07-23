package com.myprojects.convo.Adapters;

import android.content.Context;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myprojects.convo.Modal.Personal__message_List;
import com.myprojects.convo.databinding.RecieveChatModalBinding;
import com.myprojects.convo.databinding.SendChatModalBinding;

import java.util.List;

public class PersonalConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder > {

    private Context context;

    private List<Personal__message_List.DataBean.MessagesBean> messagesBeanList;

    private int user_id;

    private static int VIEW_TYPE_SENDER = 1;
    private static int VIEW_TYPE_RECIEVER = 2;

    public PersonalConversationAdapter(Context context, List<Personal__message_List.DataBean.MessagesBean> messagesBeanList, int user_id) {
        this.context = context;
        this.messagesBeanList = messagesBeanList;
        this.user_id = user_id;
    }

    @Override
    public int getItemViewType(int position) {

        int sender_id = messagesBeanList.get(position).getSender_id();

        if (sender_id == user_id)
        {
            return VIEW_TYPE_SENDER;
        }
        else
        {
            return VIEW_TYPE_RECIEVER;
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder  onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        if (viewType == VIEW_TYPE_SENDER)
        {
            SendChatModalBinding sendChatModalBinding = SendChatModalBinding.inflate(layoutInflater,parent,false);
            return new SentMessageViewHolder(sendChatModalBinding);
        }
        else
        {
            RecieveChatModalBinding recieveChatModalBinding = RecieveChatModalBinding.inflate(layoutInflater,parent,false);
            return new ReceivedMessageViewHolder(recieveChatModalBinding);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (!messagesBeanList.isEmpty())
        {
            Personal__message_List.DataBean.MessagesBean messagesBean = messagesBeanList.get(position);

            if (holder.getItemViewType() ==  VIEW_TYPE_SENDER)
            {
                ((SentMessageViewHolder)holder).bind(messagesBean);
            }
            else
            {
                ((ReceivedMessageViewHolder)holder).bind(messagesBean);
            }


        }




    }

    @Override
    public int getItemCount() {
        return messagesBeanList.size();
    }



    public static class SentMessageViewHolder extends RecyclerView.ViewHolder
    {
        private final SendChatModalBinding binding ;
        public SentMessageViewHolder(@NonNull SendChatModalBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(Personal__message_List.DataBean.MessagesBean messagesBean)
        {
            binding.messageSendTxt.setText(messagesBean.getUnifiedContent());
            String time = String.valueOf(messagesBean.getTimestampRaw());
            binding.timeStampId.setText(time);
        }
    }

    public static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder
    {

        private final RecieveChatModalBinding binding;

        public ReceivedMessageViewHolder(RecieveChatModalBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Personal__message_List.DataBean.MessagesBean messagesBean)
        {
            binding.messageContentTextView.setText(messagesBean.getUnifiedContent());
            String time = String.valueOf(messagesBean.getTimestampRaw());
            binding.timestampTextView.setText(time);
        }

    }

}
