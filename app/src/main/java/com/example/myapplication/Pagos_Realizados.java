package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.Adapter.ListViewHistoric;
import com.example.myapplication.Adapter.ListViewTransport;
import com.example.myapplication.Models.Historic;
import com.example.myapplication.Models.Transportation;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Pagos_Realizados extends Fragment {
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    RecyclerView recyclerView;
    ListViewHistoric Adapter;
    FirebaseFirestore firebaseFirestore;
    /*
    @Override
    public void onStart() {
        super.onStart();
        Adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        Adapter.stopListening();
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pagos__realizados, container, false);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView=view.findViewById(R.id.recyclerview_pago_realizados);
        String id = auth.getCurrentUser().getUid();
        Query query = firebaseFirestore.collection("passenger").document(id).collection("historic");
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Inflate the layout for this fragment
        FirestoreRecyclerOptions<Historic> firestoreRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<Historic>().setQuery(query,Historic.class).build();
        Adapter = new ListViewHistoric(firestoreRecyclerOptions);
        Adapter.notifyDataSetChanged();
        recyclerView.setAdapter(Adapter);
        Adapter.startListening();

        return view;
    }
}