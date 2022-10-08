package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.myapplication.Adapter.ListViewQuestions;
import com.example.myapplication.Adapter.ListViewTransport;
import com.example.myapplication.Models.Questions;
import com.example.myapplication.Models.Transportation;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Preguntas_frecuentes extends Fragment {
    ImageButton btnBack;
    RecyclerView recyclerView;
    ListViewQuestions Adapter;
    FirebaseFirestore firebaseFirestore;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_preguntas_frecuentes, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView=view.findViewById(R.id.recycler_preguntas_frecuentes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Query query = firebaseFirestore.collection("questions");
        FirestoreRecyclerOptions<Questions> firestoreRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<Questions>().setQuery(query,Questions.class).build();
        Adapter = new ListViewQuestions(firestoreRecyclerOptions);
        Adapter.notifyDataSetChanged();
        recyclerView.setAdapter(Adapter);
        Adapter.startListening();
        return view;
    }
}