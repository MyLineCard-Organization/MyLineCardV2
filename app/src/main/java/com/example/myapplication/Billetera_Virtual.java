package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Billetera_Virtual extends Fragment {
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private DocumentReference Data;
    private TextView fullnameMain, fullnameOptions;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_billetera__virtual, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Inflate the layout for this fragment
        // Data User
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        fullnameMain = getView().findViewById(R.id.text_fullname_bv);
        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            /// Data
            String doc = auth.getCurrentUser().getUid();
            Data = db.collection("passenger").document(doc);
            Data.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String name = documentSnapshot.getString("name");
                                String lastname = documentSnapshot.getString("surname");
                                fullnameMain.setText(name+" "+lastname);
                            } else {
                                Toast.makeText(getContext(), "Document does not exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
                        }
                    });
            /// Data
        }

    }
}