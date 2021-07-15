package com.example.adminapp.Astrologers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.example.adminapp.databinding.ActivityAstrologerDetailBinding;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AstrologerDetail extends AppCompatActivity {

    ActivityAstrologerDetailBinding binding;
    ArrayList<Astrologer> astrologerArrayList;
    AstrologerAdapter astrologerAdapter;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAstrologerDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Astrologers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        binding.AstrologerRecyclerView.setAdapter(astrologerAdapter);
        binding.AstrologerRecyclerView.setHasFixedSize(true);
        binding.AstrologerRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        firestore = FirebaseFirestore.getInstance();
        astrologerArrayList = new ArrayList<>();
        astrologerAdapter  = new AstrologerAdapter(AstrologerDetail.this,astrologerArrayList);

        binding.AstrologerRecyclerView.setAdapter(astrologerAdapter);


        //method
        EventDataChangeListner();

    }


    //fetch all the astrologer data from firestore and showing recyclerview
    private void EventDataChangeListner() {

        firestore.collection("Astrologers")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        astrologerArrayList.clear();

                        if (error != null) {
                            Log.e("firebase error", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) ;

                            astrologerArrayList.add(dc.getDocument().toObject(Astrologer.class));
                        }
                        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                        itemTouchHelper.attachToRecyclerView(binding.AstrologerRecyclerView);
                       astrologerAdapter.notifyDataSetChanged();
                    }


                });
    }
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull  RecyclerView.ViewHolder viewHolder, @NonNull  RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull  RecyclerView.ViewHolder viewHolder, int direction) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AstrologerDetail.this);
            builder.setTitle("Delete Astrologer");
            builder.setMessage("Are you sure?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int position = viewHolder.getAdapterPosition();
                    astrologerArrayList.remove(position);
                    astrologerAdapter.notifyItemRemoved(position);
                    astrologerAdapter.notifyItemChanged(position);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    astrologerAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            });
            builder.show();
            new ItemTouchHelper(simpleCallback).attachToRecyclerView(binding.AstrologerRecyclerView);

        }

    };


}