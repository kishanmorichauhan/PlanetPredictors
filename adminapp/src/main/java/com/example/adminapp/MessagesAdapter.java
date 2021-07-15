package com.example.adminapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminapp.databinding.ItemReceiveGroupBinding;
import com.example.adminapp.databinding.ItemSentGroupBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Messages> messagesArrayList;

    int ITEM_SEND=1;
    int ITEM_RECIEVE=2;

    String senderRoom;
    String receiverRoom;

    int receive=0;

    public MessagesAdapter(Context context, ArrayList<Messages> messagesArrayList,String senderRoom , String receiverRoom) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
        this.senderRoom = senderRoom;
        this.receiverRoom = receiverRoom;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==ITEM_SEND)
        {
            //showing admin message layout
            View view= LayoutInflater.from(context).inflate(R.layout.item_sent_group,parent,false);
            return new SenderViewHolder(view);
        }
        else
        {
            //showing user message layout
            View view= LayoutInflater.from(context).inflate(R.layout.item_receive_group,parent,false);
            return new RecieverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Messages messages=messagesArrayList.get(position);
        if(FirebaseAuth.getInstance().getUid().equals(messages.getSenderId()))

        //checking if admin uid and sender uid are same
        {
            return  ITEM_SEND;
        }
        else
        {
            receive++;
            return ITEM_RECIEVE;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Messages messages=messagesArrayList.get(position);
        if(holder.getClass()==SenderViewHolder.class)
        {
            SenderViewHolder viewHolder=(SenderViewHolder)holder;
            viewHolder.textViewmessaage.setText(messages.getMessage());
            viewHolder.timeofmessage.setText(messages.getCurrenttime());
        }
        else
        {
            RecieverViewHolder viewHolder=(RecieverViewHolder)holder;
            viewHolder.textViewmessaage.setText(messages.getMessage());
            viewHolder.timeofmessage.setText(messages.getCurrenttime());
        }

    }


    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

   public class SenderViewHolder extends RecyclerView.ViewHolder
    {
        ItemSentGroupBinding binding;

        TextView textViewmessaage;
        TextView timeofmessage;


        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            //set time and message
            textViewmessaage=itemView.findViewById(R.id.sendermessage);
            timeofmessage=itemView.findViewById(R.id.timeofmessage);
            binding = ItemSentGroupBinding.bind(itemView);
        }
    }

   public class RecieverViewHolder extends RecyclerView.ViewHolder
    {
        ItemReceiveGroupBinding binding;
        TextView textViewmessaage;
        TextView timeofmessage;


        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);

            //find id of time and sender message
            textViewmessaage=itemView.findViewById(R.id.sendermessage);
            timeofmessage=itemView.findViewById(R.id.timeofmessage);
            binding = ItemReceiveGroupBinding.bind(itemView);
        }
    }

}
