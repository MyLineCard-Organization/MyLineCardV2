package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.myapplication.Adapter.ListViewTransport;
import com.example.myapplication.Models.Transportation;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Lineas_Afiliadas extends Fragment {

    ImageButton btnBack;
    RecyclerView recyclerView;
    ListViewTransport Adapter;
    FirebaseFirestore firebaseFirestore;

    @Override
    public void onStart() {
        super.onStart();
        Adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        Adapter.stopListening();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_lineas__afiliadas, container, false);


        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView=view.findViewById(R.id.recyclerview_afiliados);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Query query = firebaseFirestore.collection("transportation");
        FirestoreRecyclerOptions<Transportation> firestoreRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<Transportation>().setQuery(query,Transportation.class).build();

        Adapter = new ListViewTransport(firestoreRecyclerOptions);
        Adapter.notifyDataSetChanged();
        recyclerView.setAdapter(Adapter);
        btnBack = (ImageButton) view.findViewById(R.id.lineas_Afiliadas_btn);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    NavController navController = Navigation.findNavController(view);
                    navController.navigate(R.id.nav_home);
            }
        });
        return view;
    }
}