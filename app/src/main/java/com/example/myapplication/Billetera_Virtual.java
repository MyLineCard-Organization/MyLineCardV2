package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
    private DocumentReference Wallet;
    private TextView fullnameMain, Balance, Dia, Mes;
    private CardView Recarga_saldo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_billetera__virtual, container, false);
        Recarga_saldo = (CardView) view.findViewById(R.id.Recarga);
        Recarga_saldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "Holi we", Toast.LENGTH_SHORT).show();
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.nav_Recarga);
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Inflate the layout for this fragment
        // Data User
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        fullnameMain = getView().findViewById(R.id.text_fullname_bv);
        Balance = getView().findViewById(R.id.balance_txt);
        Dia = getView().findViewById(R.id.Dia_txt);
        Mes = getView().findViewById(R.id.Mes_txt);
        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            /// Data
            String doc = auth.getCurrentUser().getUid();

            Wallet = db.collection("passenger").document(doc)
                    .collection("wallet").document(doc);
            Wallet.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        Balance.setText(documentSnapshot.getString("balance"));
                        Dia.setText(documentSnapshot.getString("Dia"));
                        Mes.setText(documentSnapshot.getString("Mes"));
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
                }
            });

            Data = db.collection("passenger").document(doc);

            Data.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String name = documentSnapshot.getString("name");
                                String lastname = documentSnapshot.getString("surname");
                                fullnameMain.setText(name+" "+lastname);
                                String datoFail = documentSnapshot.getString("fail");
                                if(datoFail==null||datoFail==""){
                                    //Toast.makeText(getContext(), "Dato no existe", Toast.LENGTH_SHORT).show();
                                }
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