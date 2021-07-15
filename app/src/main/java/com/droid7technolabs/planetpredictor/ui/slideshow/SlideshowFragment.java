package com.droid7technolabs.planetpredictor.ui.slideshow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.droid7technolabs.planetpredictor.Astrologer;
import com.droid7technolabs.planetpredictor.AstrologerAdapter;
import com.droid7technolabs.planetpredictor.MyVedic;
import com.droid7technolabs.planetpredictor.databinding.FragmentSlideshowBinding;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SlideshowFragment extends Fragment {

    //for astrologer page
    private SlideshowViewModel slideshowViewModel;
    private FragmentSlideshowBinding binding;
    ArrayList<Astrologer> astrologerArrayList;
    AstrologerAdapter astrologerAdapter;
    FirebaseFirestore firestore;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);

        binding.recycleview9.setHasFixedSize(true);
        binding.recycleview9.setLayoutManager(new LinearLayoutManager(container.getContext()));
        firestore = FirebaseFirestore.getInstance();
        astrologerArrayList = new ArrayList<>();
        astrologerAdapter  = new AstrologerAdapter(SlideshowFragment.this.getContext(),astrologerArrayList);
        binding.recycleview9.setAdapter(astrologerAdapter);

        View root = binding.getRoot();

        binding.vedic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  going to why vedic
                Intent in = new Intent(SlideshowFragment.this.getContext(), MyVedic.class);
                startActivity(in);
            }
        });
        //Showing all the astrologer in recycleView
        EventDataChangeListner();
        return root;
    }



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
                        astrologerAdapter.notifyDataSetChanged();
                    }


                });
    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }
}