package com.droid7technolabs.planetpredictor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.droid7technolabs.planetpredictor.UserDetails.BirthProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


import static com.droid7technolabs.planetpredictor.ui.home.HomeFragment.rImage;


public class MessagesAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Messages> messagesArrayList;

   final int ITEM_SEND=1;
   final int ITEM_RECIEVE=2;

    String senderRoom;
    String receiverRoom;

    public MessagesAdapter(Context context, ArrayList<Messages> messagesArrayList,String senderRoom , String receiverRoom) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
        this.senderRoom = senderRoom;
        this.receiverRoom = receiverRoom;
    }

    //calling layout(sender/receiver)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==ITEM_SEND)
        {
            View view= LayoutInflater.from(context).inflate(R.layout.item_sent_group,parent,false);
            return new SenderViewHolder(view);
        }
        else
        {
            View view= LayoutInflater.from(context).inflate(R.layout.item_receive_group,parent,false);
            return new RecieverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        //setting time and message in layout
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


            //taking admin name in admin message and showing it
            DocumentReference docRef = FirebaseFirestore.getInstance().collection("Admin").document("RB1AwI7NHZOsg3Y5IePe9yuZabM2");
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            String eName = document.getString("adminName");
                            viewHolder.adminname.setText("@" + eName);

                        } else {
                            Log.d("LOGGER", "No such document");
                        }
                    } else {
                        Log.d("LOGGER", "get failed with ", task.getException());
                    }
                }
            });

            //setting profile image
            Glide.with(context).load(rImage)
                    .placeholder(R.drawable.profile_image)
                    .into(viewHolder.circleImageView);

        }

    }

    //validation of layout
    @Override
    public int getItemViewType(int position) {
        Messages messages=messagesArrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId()))

        {
            return  ITEM_SEND;
        }
        else
        {
            return  ITEM_RECIEVE;
        }
    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    //for sender
    class SenderViewHolder extends RecyclerView.ViewHolder
    {
        TextView textViewmessaage;
        TextView timeofmessage;


        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewmessaage=itemView.findViewById(R.id.sendermessage);
            timeofmessage=itemView.findViewById(R.id.timeofmessage);
        }
    }

    //for reciver
    class RecieverViewHolder extends RecyclerView.ViewHolder
    {

        TextView textViewmessaage,adminname;
        TextView timeofmessage;
        CircleImageView circleImageView;

        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewmessaage=itemView.findViewById(R.id.sendermessage);
            timeofmessage=itemView.findViewById(R.id.timeofmessage);
            circleImageView = itemView.findViewById(R.id.admin_img);
            adminname = itemView.findViewById(R.id.adminName);
        }
    }

}
