package com.example.adminapp.Astrologers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.adminapp.R;
import com.example.adminapp.databinding.RowAstrologersBinding;

import java.util.ArrayList;

public class AstrologerAdapter extends RecyclerView.Adapter<AstrologerAdapter.AstrlogerViewHolder> {

    Context context;
    ArrayList<Astrologer> astrologerArrayList;

    public AstrologerAdapter(Context context , ArrayList<Astrologer> astrologerArrayList) {
        this.context = context;
        this.astrologerArrayList = astrologerArrayList;
    }

    @NonNull
    @Override
    public AstrlogerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_astrologers,parent,false);
        return new AstrlogerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  AstrologerAdapter.AstrlogerViewHolder holder, int position) {
        Astrologer astrologer = astrologerArrayList.get(position);


        //fatch data and set textView
        holder.binding.atlName.setText(astrologer.getName());
        holder.binding.atlRating.setText(astrologer.getRatings());
        holder.binding.atlSpecification.setText(astrologer.getSpecialisation());

        //load image into astrologer image
        Glide.with(context).load(astrologer.getAstrologer_image())
                .placeholder(R.drawable.user)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return astrologerArrayList.size();
    }


    public class AstrlogerViewHolder extends RecyclerView.ViewHolder  {
        RowAstrologersBinding binding;
        TextView name;
        ImageView imageView;
        public AstrlogerViewHolder(View itemView) {
            super(itemView);
            binding = RowAstrologersBinding.bind(itemView);

            //id
            name = itemView.findViewById(R.id.atl_name);
            imageView = itemView.findViewById(R.id.astrologer_images);

        }
    }
}
