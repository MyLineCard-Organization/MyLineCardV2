package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Adapter.ListViewPhone;
import com.example.myapplication.Models.Transportation;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Phone extends Fragment {

    RecyclerView recyclerView;
    ListViewPhone Adapter;
    FirebaseFirestore firebaseFirestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_phone, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView=view.findViewById(R.id.recyclerview_phone);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        try {
            Query query = firebaseFirestore.collection("transportation");
            FirestoreRecyclerOptions<Transportation> firestoreRecyclerOptions = new FirestoreRecyclerOptions
                    .Builder<Transportation>().setQuery(query,Transportation.class).build();

            Adapter = new ListViewPhone(firestoreRecyclerOptions);
            Adapter.notifyDataSetChanged();
            recyclerView.setAdapter(Adapter);
            Adapter.startListening();
        }catch (Exception e){
            e.printStackTrace();
            Log.e("Error",e.getMessage());
        }

        return view;
    }
}