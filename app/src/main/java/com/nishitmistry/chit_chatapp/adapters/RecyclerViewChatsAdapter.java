package com.nishitmistry.chit_chatapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nishitmistry.chit_chatapp.Controller.FirebaseController;
import com.nishitmistry.chit_chatapp.R;
import com.nishitmistry.chit_chatapp.model.Messages;

import java.util.ArrayList;

public class RecyclerViewChatsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    ArrayList<Messages> messagesArrayList;
    final int sender_flag=1;
    final int sender_flag_small=-1;
    final int receiver_flag=2;
    final int receiver_flag_small=-2;
    private  onChatLongClickListener onChatLongClickListener;

    public RecyclerViewChatsAdapter(Context context, ArrayList<Messages> messagesArrayList,onChatLongClickListener onChatLongClickListener) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
        this.onChatLongClickListener=onChatLongClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case sender_flag:
                View v = LayoutInflater.from(context)
                        .inflate(R.layout.sender_layout, parent, false);
                SentViewHolder viewHolder = new SentViewHolder(v,onChatLongClickListener);
                return viewHolder;
            case receiver_flag:
                View v1 = LayoutInflater.from(context)
                        .inflate(R.layout.receiver_layout, parent, false);
                ReceivedViewHolder viewHolder1 = new ReceivedViewHolder(v1,onChatLongClickListener);
                return viewHolder1;
            case receiver_flag_small:
                View v2 = LayoutInflater.from(context)
                        .inflate(R.layout.receiver_layout_small, parent, false);
                ReceivedViewHolder viewHolder2 = new ReceivedViewHolder(v2,onChatLongClickListener);
                return viewHolder2;
            case sender_flag_small:
                Log.d("RecyclerViewChatsAdapt", "onCreateViewHolder: sender flag small");
                View v3 = LayoutInflater.from(context)
                        .inflate(R.layout.sender_layout_small, parent, false);
                SentViewHolder viewHolder3 = new SentViewHolder(v3,onChatLongClickListener);
                return viewHolder3;

            default:
                System.out.println(viewType);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        Messages currentMessage=messagesArrayList.get(position);
        switch (viewType) {
            case sender_flag_small:
            case sender_flag:
                SentViewHolder sentViewHolder = (SentViewHolder) holder;
                sentViewHolder.messageField.setText(currentMessage.getMessage());
                sentViewHolder.timeStamp.setText(currentMessage.getTimeStamp());
                break;
            case receiver_flag_small:
            case receiver_flag:
                ReceivedViewHolder receivedViewHolder =(ReceivedViewHolder) holder;
                receivedViewHolder.messageField.setText(currentMessage.getMessage());
                receivedViewHolder.timeStamp.setText(currentMessage.getTimeStamp());
                break;
        }
    }


    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(messagesArrayList.get(position).getSenderUid().equals(
                FirebaseController.getFirebaseController().getAuth().getCurrentUser().getUid()))
        {
            if(messagesArrayList.get(position).getMessage().length()>25)
                return sender_flag;
            else
                return sender_flag_small;
        }
        else
        {
            if(messagesArrayList.get(position).getMessage().length()>25)
                return receiver_flag;
            else
                return receiver_flag_small;
        }
    }

    public class SentViewHolder extends RecyclerView.ViewHolder {
        TextView messageField;
        TextView timeStamp;
        onChatLongClickListener onChatLongClickListener;
        public SentViewHolder(@NonNull View itemView,onChatLongClickListener onChatLongClickListener) {
            super(itemView);
            messageField=itemView.findViewById(R.id.senderMessageBox);
            timeStamp=itemView.findViewById(R.id.timeStamp);
            this.onChatLongClickListener=onChatLongClickListener;
            itemView.setOnLongClickListener(v -> {
                onChatLongClickListener.onChatLongClick(getAdapterPosition(),v);
                return false;
            });
        }

    }
    public class ReceivedViewHolder extends  RecyclerView.ViewHolder{
        TextView messageField;
        TextView timeStamp;
        onChatLongClickListener onChatLongClickListener;
        public ReceivedViewHolder(@NonNull View itemView,onChatLongClickListener onChatLongClickListener) {
            super(itemView);
            messageField=itemView.findViewById(R.id.senderMessageBox);
            timeStamp=itemView.findViewById(R.id.timeStamp);
            this.onChatLongClickListener=onChatLongClickListener;
            itemView.setOnLongClickListener(v -> {
                onChatLongClickListener.onChatLongClick(getAdapterPosition(),v);
                return false;
            });
        }
    }
    public interface  onChatLongClickListener{
        void onChatLongClick(int position,View v);
    }
}
