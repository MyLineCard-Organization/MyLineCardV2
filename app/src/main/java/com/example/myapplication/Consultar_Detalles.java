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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;

public class Consultar_Detalles extends Fragment {
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    TextView txt_fullname_detalles, txt_id_usuario, txt_saldo_actual, txt_total_gastado;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_consultar__detalles, container, false);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        // Inizialize Views
        txt_fullname_detalles = (TextView) view.findViewById(R.id.txt_fullname_detalles);
        txt_id_usuario = (TextView) view.findViewById(R.id.txt_id_usuario);
        txt_saldo_actual = (TextView) view.findViewById(R.id.txt_saldo_actual);
        txt_total_gastado = (TextView) view.findViewById(R.id.txt_total_gastado);
        //
        fullnameAndId();
        SaldoActual();
        TotalGastado();
        return view;
    }

    private void TotalGastado() {
        db.collection("passenger")
                .document(auth.getCurrentUser().getUid())
                .collection("historic")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            Double total = 0.0;
                            for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                total += documentSnapshot.getDouble("price");
                            }
                            DecimalFormat twoDForm = new DecimalFormat("#.##");
                            Double ganacias = Double.valueOf(twoDForm
                                    .format(total));
                            String total_gastado = "S/"+String.valueOf(ganacias);
                            txt_total_gastado.setText(total_gastado);
                        }
                    }
                });
    }

    private void SaldoActual() {
        db.collection("passenger")
                .document(auth.getCurrentUser().getUid())
                .collection("wallet")
                .document(auth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            String saldo ="S/"+String.valueOf(documentSnapshot.getDouble("balance"));
                            txt_saldo_actual.setText(saldo);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error al obtener el saldo actual", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fullnameAndId() {
        db.collection("passenger").document(auth.getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("name");
                            String lastname = documentSnapshot.getString("surname");
                            txt_fullname_detalles.setText(name+" "+lastname);
                            txt_id_usuario.setText(auth.getCurrentUser().getUid());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}