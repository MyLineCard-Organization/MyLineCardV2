package com.example.myapplication;

import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Adapter.ListViewCommentAdmin;
import com.example.myapplication.Adapter.ListViewSeeAnswer;
import com.example.myapplication.Models.Comment;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class Respuestas_comentarios extends Fragment {
    String id;
    RecyclerView recyclerView;
    ListViewSeeAnswer Adapter;
    FirebaseFirestore firebaseFirestore;
    LinearLayout noFound;
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_respuestas_comentarios, container, false);
        id = Respuestas_comentariosArgs.fromBundle(getArguments()).getId();
        //Toast.makeText(getContext(), "El id es "+id, Toast.LENGTH_SHORT).show();
        noFound = view.findViewById(R.id.noFound);
        // COMMENTED OUT
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView=view.findViewById(R.id.recycler_respuestas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Query query = firebaseFirestore.collection("comments")
                .document(id)
                .collection("responses");

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult().isEmpty()){
                    noFound.setVisibility(View.VISIBLE);
                }
            }
        });
        FirestoreRecyclerOptions<Comment> firestoreRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<Comment>().setQuery(query,Comment.class).build();
        Adapter = new ListViewSeeAnswer(firestoreRecyclerOptions);
        Adapter.notifyDataSetChanged();
        recyclerView.setAdapter(Adapter);
        Adapter.startListening();
        return view;
    }
}