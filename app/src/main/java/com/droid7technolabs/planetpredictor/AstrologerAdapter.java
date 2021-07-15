package com.droid7technolabs.planetpredictor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.droid7technolabs.planetpredictor.databinding.RowAstrologersBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AstrologerAdapter extends RecyclerView.Adapter<AstrologerAdapter .ViewHolder>
{

    Context context;
    ArrayList<Astrologer> astrologerArrayList;
    FirebaseFirestore firestore;

    public AstrologerAdapter(Context context, ArrayList<Astrologer> astrologerArrayList) {
        this.context = context;
        this.astrologerArrayList = astrologerArrayList;
        firestore = FirebaseFirestore.getInstance();
    }
    public AstrologerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_astrologers,parent,false);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Astrologer astrologer = astrologerArrayList.get(position);
        //set value of astrologer
        holder.binding.atlName.setText(astrologer.getName());
        holder.binding.atlSpecification.setText(astrologer.getSpecialisation());
        holder.binding.atlRating.setText(astrologer.getRatings());

        Glide.with(context).load(astrologer.getAstrologer_image())
                .placeholder(R.drawable.user)
                .into(holder.binding.atlimg);
    }

    @Override
    public int getItemCount() {
        return astrologerArrayList.size();

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        RowAstrologersBinding binding;

        public ViewHolder(View view) {
            super(view);
            binding = RowAstrologersBinding.bind(view);

        }
    }
}