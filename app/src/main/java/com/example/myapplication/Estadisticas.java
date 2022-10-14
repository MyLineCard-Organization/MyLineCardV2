package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    private String Year;
    TextView total_pagado, mes_de_pago, total_pagado_all, txt_estado_mes;
    Button btn_seleccionar_mes;
    Double total, retornado, total_pasado;
    Double total_year;
    Calendar calendario;
    Spinner spinner_mes, spinner_year;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_estadisticas, container, false);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        total_pagado = view.findViewById(R.id.txt_total_pagado);
        mes_de_pago = view.findViewById(R.id.txt_pagado_mes);
        retornado = 0.0;
        txt_estado_mes = view.findViewById(R.id.txt_estado_mes);
        total_pagado_all = view.findViewById(R.id.txt_pagado_year_all);
        btn_seleccionar_mes = view.findViewById(R.id.btn_seleccionar_mes);
        spinner_mes = view.findViewById(R.id.spinner_mes);
        spinner_year = view.findViewById(R.id.spinner_year);
        calendario = Calendar.getInstance();
        Mes = String.valueOf(calendario.get(Calendar.MONTH)+1);
        Year = String.valueOf(calendario.get(Calendar.YEAR));
        String [] years = {"2022","2023","2024","2025","2026","2027","2028","2029","2030"};
        ArrayAdapter<String> adapter_year = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, years);
        spinner_year.setAdapter(adapter_year);

        String [] meses = {"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
        ArrayAdapter <String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, meses);
        spinner_mes.setAdapter(adapter);
        spinner_mes.setSelection(Integer.parseInt(Mes)-1);
        btn_seleccionar_mes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mes = spinner_mes.getSelectedItemPosition()+1;
                String year = spinner_year.getSelectedItem().toString();
                estadisticas(String.valueOf(mes), year);
                mesEscopido(String.valueOf(mes));
                estadisticas_year(year);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        estadisticas(Mes, Year);
        estadisticas_year(Year);
        compararMesAnterior(calendario.get(Calendar.MONTH)+1,Year);
    }

    public void compararMesAnterior(int mesActual, String year){
        if (mesActual == 1) {
            txt_estado_mes.setText("El mes anterior pertenece al año anterior");
        }else
        {
            total_pasado=0.0;
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
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            db.collection("transportation")
                                                                    .document(documentUID.getString("transport_id"))
                                                                    .collection(document.getId())
                                                                    .whereEqualTo("month", String.valueOf(mesActual-1))
                                                                    .whereEqualTo("year", year)
                                                                    .get()
                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                            if (task.isSuccessful()) {
                                                                                for (QueryDocumentSnapshot documentTotal : task.getResult()) {
                                                                                        DecimalFormat twoDForm = new DecimalFormat("#.##");
                                                                                        total_pasado += Double.valueOf(twoDForm
                                                                                                .format(documentTotal.getDouble("earnings")));
                                                                                }
                                                                            }
                                                                        }
                                                                    });
                                                        }
                                                        if (retornado ==0.0){
                                                            txt_estado_mes.setText("No hay datos del mes actual");
                                                        }
                                                        else if (total_pasado == 0.0){
                                                            txt_estado_mes.setText("No hay datos del mes anterior");
                                                        } else if (total_pasado > retornado) {
                                                            txt_estado_mes.setText("El mes anterior fue más rentable");
                                                            txt_estado_mes.setTextColor(Color.RED);
                                                        } else if (total_pasado < retornado) {
                                                            txt_estado_mes.setText("El mes anterior fue menos rentable");
                                                            txt_estado_mes.setTextColor(Color.GREEN);
                                                        } else if (total_pasado == retornado) {
                                                            txt_estado_mes.setText("El mes fue igual al anterior");
                                                        }
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                    });
        }
    }

    public void estadisticas(String mes, String year){

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
                                                    total=0.0;
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        db.collection("transportation")
                                                                .document(documentUID.getString("transport_id"))
                                                                .collection(document.getId())
                                                                .whereEqualTo("month", mes)
                                                                .whereEqualTo("year", year)
                                                                .get()
                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        if (task.isSuccessful()) {

                                                                            for (QueryDocumentSnapshot documentTotal : task.getResult()) {
                                                                                DecimalFormat twoDForm = new DecimalFormat("#.##");
                                                                                total += Double.valueOf(twoDForm
                                                                                        .format(documentTotal.getDouble("earnings")));
                                                                            }
                                                                            DecimalFormat twoDForm = new DecimalFormat("#.##");
                                                                            retornado = total;
                                                                            total_pagado.setText(String.valueOf(Double.valueOf(twoDForm
                                                                                    .format(total))));
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                    compararMesAnterior(Integer.parseInt(mes),year);
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
    }

    public void estadisticas_year(String year){
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
                                                    total_year=0.0;
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        db.collection("transportation")
                                                                .document(documentUID.getString("transport_id"))
                                                                .collection(document.getId())
                                                                .whereEqualTo("year", year)
                                                                .get()
                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            for (QueryDocumentSnapshot documentTotal : task.getResult()) {
                                                                                DecimalFormat twoDForm = new DecimalFormat("#.##");
                                                                                Log.d("Total", documentTotal.getId() + " => " + documentTotal.getData());
                                                                                Log.d("Total", String.valueOf(total));
                                                                                total_year += Double.valueOf(twoDForm
                                                                                        .format(documentTotal.getDouble("earnings")));
                                                                            }
                                                                            DecimalFormat twoDForm = new DecimalFormat("#.##");
                                                                            total_pagado_all.setText(String.valueOf(Double.valueOf(twoDForm
                                                                                    .format(total_year))));
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