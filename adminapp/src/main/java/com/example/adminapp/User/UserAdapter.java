package com.example.adminapp.User;

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
import com.example.adminapp.ChatActivity;
import com.example.adminapp.R;
import com.example.adminapp.databinding.RowConversationBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

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

//      settext
        holder.binding.userName.setText(users.getFullname());

        //set profile image
        Glide.with(context).load(users.getProfile_image())
                .placeholder(R.drawable.profile_image)
                .into(holder.binding.img);


        //name and image pass in chatActivity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("name",users.getFullname());
                intent.putExtra("image",users.getProfile_image());
                intent.putExtra("uid",users.getUid());
                context.startActivity(intent);
            }
        });

        //long press to show user profile (All details)
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(context,UserProfilr_Activity.class);
                intent.putExtra("uName",users.getFullname());
                intent.putExtra("uImage",users.getProfile_image());
                intent.putExtra("date",users.getDate());
                intent.putExtra("time",users.getTime());
                intent.putExtra("country",users.getCountry());
                intent.putExtra("state",users.getState());
                intent.putExtra("city",users.getCity());
                intent.putExtra("deviceId",users.getDeviceid());
                intent.putExtra("gender",users.getGender());
                context.startActivity(intent);
                return false;
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
            fullname = itemView.findViewById(R.id.name);
            userImage = itemView.findViewById(R.id.img);
        }
    }
}
