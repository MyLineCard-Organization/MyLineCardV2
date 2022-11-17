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
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Paga_seguro extends Fragment {
    Button Pagar;
    private String id;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private Double monto;
    private String Dia, Mes;
    private DocumentReference Wallet;
    private EditText editPagoCvv, editPagoNumber, editPagoName, editPagoMes, editPagoYear;
    private ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_paga_seguro, container, false);
        // Code
        editPagoCvv = view.findViewById(R.id.editPagoCvv);
        editPagoNumber = view.findViewById(R.id.editPagoNumber);
        editPagoName = view.findViewById(R.id.editPagoName);
        editPagoMes = view.findViewById(R.id.editPagoMes);
        editPagoYear = view.findViewById(R.id.editPagoYear);
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
        monto = Double.valueOf(Paga_seguroArgs.fromBundle(getArguments()).getPricedata());
        Pagar.setText("Pagar S/"+monto+" Soles");
        /////
        Pagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(editPagoCvv.getText().toString().isEmpty() || editPagoNumber.getText().toString().isEmpty() || editPagoName.getText().toString().isEmpty() || editPagoMes.getText().toString().isEmpty() || editPagoYear.getText().toString().isEmpty()){
                  Toast.makeText(getContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show();
              }else {
                  if(editPagoCvv.getText().toString().length() != 3 || editPagoMes.getText().toString().length() != 2 || editPagoNumber.getText().toString().length() != 16 || Double.valueOf(editPagoMes.getText().toString()) < 1 || Double.valueOf(editPagoMes.getText().toString()) > 12 || Double.valueOf(editPagoYear.getText().toString()) < 22 || editPagoYear.getText().toString().length() != 2){
                      Toast.makeText(getContext(), "Ingrese un formato válido", Toast.LENGTH_SHORT).show();
                  }else{
                      pagar(view);
                  }
              }
            }

        });
        return view;
    }

    public void pagar(View view){
        progressDialog.setMessage("Procesando pago...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        /* Firebase Se crea por primera vez*/
        id = auth.getCurrentUser().getUid();
                /*
               Firebase Se crea por primera vez*/
        /// Obtener Dinero///
        db.collection("passenger").document(id)
                .collection("wallet").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Toast.makeText(getContext(),"Recarga exitosa" , Toast.LENGTH_SHORT).show();
                            DecimalFormat twoDForm = new DecimalFormat("#.##");
                            monto = Double.valueOf(twoDForm.format(monto+documentSnapshot.getDouble("balance"))) ;
                            Map<String,Object> map = new HashMap<>();
                            map.put("balance", monto);
                            map.put("Dia", Dia);
                            map.put("Mes", Mes);
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
}