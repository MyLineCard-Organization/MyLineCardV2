package com.example.myapplication;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Paga_seguro extends Fragment {
    Button Pagar;
    private String id;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String monto;
    private String Dia, Mes;
    private DocumentReference Wallet;
    private ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_paga_seguro, container, false);
        // Code
        // Componentes
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(getContext());
        /// Funcionalidades


        Calendar calendario = Calendar.getInstance();
        Dia =String.valueOf( calendario.get(Calendar.DAY_OF_MONTH));
        Mes = String.valueOf(calendario.get(Calendar.MONTH));

        ////
        Pagar = (Button) view.findViewById(R.id.paga_seguro_btn);
        monto = Paga_seguroArgs.fromBundle(getArguments()).getPricedata();
        Pagar.setText("Pagar S/"+monto+" Soles");
        /////
        Pagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Procesando pago...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                /* Firebase Se crea por primera vez*/
                id = auth.getCurrentUser().getUid();
                /*
               Firebase Se crea por primera vez*/
                /// Obtener Dinero///
                Wallet = db.collection("passenger").document(id)
                        .collection("wallet").document(id);




                    Wallet.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                //Toast.makeText(getContext(), "Existe", Toast.LENGTH_SHORT).show();
                                if(documentSnapshot.getString("balance")!=null){
                                    Toast.makeText(getContext(),"Pago exitoso" , Toast.LENGTH_SHORT).show();
                                    monto = String.valueOf(Integer.parseInt(monto)+Integer.parseInt(documentSnapshot.getString("balance")));
                                    Map<String,Object> map = new HashMap<>();
                                    map.put("balance", monto);
                                    db.collection("passenger").document(id)
                                            .collection("wallet").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    // Lo que pasa cuando se realiza correctamente el pago
                                                    progressDialog.dismiss();
                                                    NavController navController = Navigation.findNavController(view);
                                                    navController.navigate(R.id.nav_home);
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getContext(), "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }else {
                                    Toast.makeText(getContext(), "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }else {
                                //Toast.makeText(getContext(), "No existe", Toast.LENGTH_SHORT).show();
                                Map<String,Object> map = new HashMap<>();
                                map.put("id", id);
                                map.put("balance", monto);
                                map.put("Dia", Dia);
                                map.put("Mes", Mes);
                                db.collection("passenger").document(id)
                                        .collection("wallet").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                // Lo que pasa cuando se realiza correctamente el pago
                                                progressDialog.dismiss();
                                                NavController navController = Navigation.findNavController(view);
                                                navController.navigate(R.id.nav_home);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getContext(), "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Null?", Toast.LENGTH_SHORT).show();
                        }
                    });

                    }

        });

        return view;
    }

}