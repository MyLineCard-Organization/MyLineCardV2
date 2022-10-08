package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.myapplication.Adapter.ListViewQuestions;
import com.example.myapplication.Adapter.ListViewTransport;
import com.example.myapplication.Models.Questions;
import com.example.myapplication.Models.Transportation;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Arrays;

public class Preguntas_frecuentes extends Fragment {
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    RecyclerView recyclerView;
    ListViewQuestions Adapter;
    FirebaseFirestore firebaseFirestore;
    Button btn_buscar_palabra_clave;
    EditText edit_palabra_clave;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_preguntas_frecuentes, container, false);
        btn_buscar_palabra_clave = (Button) view.findViewById(R.id.btn_buscar_palabra_clave);
        edit_palabra_clave = (EditText) view.findViewById(R.id.edit_palabra_clave);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
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
        //
        btn_buscar_palabra_clave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String palabra_clave = edit_palabra_clave.getText().toString();
                if(palabra_clave.isEmpty()){
                    Query query = firebaseFirestore.collection("questions");
                    FirestoreRecyclerOptions<Questions> firestoreRecyclerOptions = new FirestoreRecyclerOptions
                            .Builder<Questions>().setQuery(query,Questions.class).build();
                    Adapter = new ListViewQuestions(firestoreRecyclerOptions);
                    Adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(Adapter);
                    Adapter.startListening();
                }
                //Searching for a word between the beginning and the end of a string

                Query query = firebaseFirestore.collection("questions")
                        .whereGreaterThanOrEqualTo("questions",palabra_clave)
                        .whereLessThanOrEqualTo("questions",palabra_clave+"\uf8ff");
                        //.whereGreaterThanOrEqualTo("questions",palabra_clave).whereLessThanOrEqualTo("questions",palabra_clave+"\uf8ff");
                FirestoreRecyclerOptions<Questions> firestoreRecyclerOptions = new FirestoreRecyclerOptions
                        .Builder<Questions>().setQuery(query,Questions.class).build();
                Adapter = new ListViewQuestions(firestoreRecyclerOptions);
                Adapter.notifyDataSetChanged();
                recyclerView.setAdapter(Adapter);
                Adapter.startListening();
            }
        });
        return view;
    }
}