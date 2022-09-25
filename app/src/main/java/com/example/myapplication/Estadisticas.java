package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.Calendar;

public class Estadisticas extends Fragment {

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private String Mes;
    TextView total_pagado, mes_de_pago;
    Button btn_seleccionar_mes;
    Double total;
    Spinner spinner_mes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_estadisticas, container, false);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        total_pagado = view.findViewById(R.id.txt_total_pagado);
        mes_de_pago = view.findViewById(R.id.txt_pagado_mes);
        btn_seleccionar_mes = view.findViewById(R.id.btn_seleccionar_mes);
        spinner_mes = view.findViewById(R.id.spinner_mes);
        Calendar calendario = Calendar.getInstance();
        Mes = String.valueOf(calendario.get(Calendar.MONTH)+1);
        String [] meses = {"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
        ArrayAdapter <String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, meses);
        spinner_mes.setAdapter(adapter);

        btn_seleccionar_mes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mes = spinner_mes.getSelectedItemPosition()+1;
                estadisticas(String.valueOf(mes));
                mesEscopido(String.valueOf(mes));
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        estadisticas(Mes);
    }

    public void estadisticas(String mes){
        total=0.0;
        db.collection("administrator")
                .document(auth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentUID = task.getResult();
                            if (documentUID.exists()) {
                                db.collection("transportation")
                                        .document(documentUID.getString("transport_id"))
                                        .collection("record")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    mesEscopido(mes);
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        db.collection("transportation")
                                                                .document(documentUID.getString("transport_id"))
                                                                .collection(document.getId())
                                                                .whereEqualTo("month", mes)
                                                                .get()
                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            for (QueryDocumentSnapshot documentTotal : task.getResult()) {
                                                                                DecimalFormat twoDForm = new DecimalFormat("#.##");
                                                                                Log.d("Total", documentTotal.getId() + " => " + documentTotal.getData());
                                                                                Log.d("Total", String.valueOf(total));
                                                                                total += Double.valueOf(twoDForm
                                                                                        .format(documentTotal.getDouble("earnings")));
                                                                            }
                                                                            total_pagado.setText(String.valueOf(total));
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
    }

    public void mesEscopido(String mes_escogido){
        switch (mes_escogido){
            case "1":
                mes_de_pago.setText("Enero");
                break;
            case "2":
                mes_de_pago.setText("Febrero");
                break;
            case "3":
                mes_de_pago.setText("Marzo");
                break;
            case "4":
                mes_de_pago.setText("Abril");
                break;
            case "5":
                mes_de_pago.setText("Mayo");
                break;
            case "6":
                mes_de_pago.setText("Junio");
                break;
            case "7":
                mes_de_pago.setText("Julio");
                break;
            case "8":
                mes_de_pago.setText("Agosto");
                break;
            case "9":
                mes_de_pago.setText("Septiembre");
                break;
            case "10":
                mes_de_pago.setText("Octubre");
                break;
            case "11":
                mes_de_pago.setText("Noviembre");
                break;
            case "12":
                mes_de_pago.setText("Diciembre");
                break;
        }
    }
}