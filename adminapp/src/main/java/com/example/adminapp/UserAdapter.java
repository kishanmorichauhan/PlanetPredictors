package com.example.adminapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.adminapp.databinding.RowConversationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UsersViewHolder> {

    Context context;
    ArrayList<UserData> userDataArrayList;

    public UserAdapter(Context context, ArrayList<UserData> userDataArrayList) {
        this.context = context;
        this.userDataArrayList = userDataArrayList;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_conversation,parent,false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UsersViewHolder holder, int position) {
        UserData users = userDataArrayList.get(position);
        String senderId = FirebaseAuth.getInstance().getUid();
        String senderRoom = senderId+users.getUid();

        holder.fullname.setText(users.getFullname());
        Glide.with(context).load(users.getProfile_image())
                .placeholder(R.drawable.profile_image)
                .into(holder.binding.profile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ChatActivity.class);
                intent.putExtra("name",users.getFullname());
                intent.putExtra("image",users.getProfile_image());
                intent.putExtra("uid",users.getUid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userDataArrayList.size();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder {
       RowConversationBinding binding;
       TextView fullname;
       ImageView userImage;

        public UsersViewHolder(@NonNull  View itemView) {
            super(itemView);
            binding = RowConversationBinding.bind(itemView);
            fullname = itemView.findViewById(R.id.username);
            userImage = itemView.findViewById(R.id.profile);
        }
    }
}
