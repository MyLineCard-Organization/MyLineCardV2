package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Adapter.ListViewCommentAdmin;
import com.example.myapplication.Adapter.ListViewTransport;
import com.example.myapplication.Models.Comment;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class Sugerencias_reclamos_admin extends Fragment {

    RecyclerView recyclerView;
    ListViewCommentAdmin Adapter;
    FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    public void onStart() {
        super.onStart();
       // Adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        //Adapter.stopListening();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        View view = inflater.inflate(R.layout.fragment_sugerencias_reclamos_admin, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView=view.findViewById(R.id.recycler_sugerencias);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db.collection("administrator").document(auth.getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Query query = firebaseFirestore.collection("comments").whereEqualTo("id_transport", documentSnapshot.getString("transport_id"));
                        FirestoreRecyclerOptions<Comment> firestoreRecyclerOptions = new FirestoreRecyclerOptions
                                .Builder<Comment>().setQuery(query,Comment.class).build();
                        Adapter = new ListViewCommentAdmin(firestoreRecyclerOptions);
                        Adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(Adapter);
                        Adapter.startListening();
                    }
                });

        return view;
    }
}