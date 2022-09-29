package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Configuraciones extends Fragment {
    private TextView txt_config_phone;
    private EditText edit_config_nombres, edit_config_surnames,
                    edit_config_phone,edit_config_address;
    private Button btn_config_save;
    private LinearLayout Layout_perfil;
    private ProgressBar progressBar_perfil;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_configuraciones, container, false);
        // Initialize
        txt_config_phone = view.findViewById(R.id.txt_config_phone);
        edit_config_nombres=view.findViewById(R.id.edit_config_nombres);
        edit_config_surnames=view.findViewById(R.id.edit_config_surnames);
        edit_config_phone=view.findViewById(R.id.edit_config_phone);
        edit_config_address=view.findViewById(R.id.edit_config_address);
        btn_config_save=view.findViewById(R.id.btn_config_save);
        Layout_perfil=view.findViewById(R.id.Layout_perfil);
        progressBar_perfil=view.findViewById(R.id.progressBar_perfil);
        auth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        // Load data
        Layout_perfil.setVisibility(View.GONE);
        db.collection("passenger").document(auth.getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            edit_config_nombres.setText(documentSnapshot.getString("name"));
                            edit_config_surnames.setText(documentSnapshot.getString("surname"));
                            edit_config_phone.setText(documentSnapshot.getString("phone"));
                            edit_config_address.setText(documentSnapshot.getString("direction"));
                            Layout_perfil.setVisibility(View.VISIBLE);
                            progressBar_perfil.setVisibility(View.GONE);
                        }else {
                            db.collection("administrator").document(auth.getCurrentUser().getUid())
                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            txt_config_phone.setVisibility(View.GONE);
                                            edit_config_phone.setVisibility(View.GONE);
                                            edit_config_nombres.setText(documentSnapshot.getString("name"));
                                            edit_config_surnames.setText(documentSnapshot.getString("surname"));
                                            edit_config_address.setText(documentSnapshot.getString("direction"));
                                            Layout_perfil.setVisibility(View.VISIBLE);
                                            progressBar_perfil.setVisibility(View.GONE);
                                        }
                                    });
                        }
                    }
                });

        btn_config_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar_perfil.setVisibility(View.VISIBLE);
                db.collection("passenger").document(auth.getCurrentUser().getUid())
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()){
                                        if (edit_config_nombres.getText().toString().isEmpty() ||
                                                edit_config_surnames.getText().toString().isEmpty() ||
                                                edit_config_phone.getText().toString().isEmpty() ||
                                                edit_config_address.getText().toString().isEmpty()){
                                            Toast.makeText(getContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                                        }else {
                                            progressBar_perfil.setVisibility(View.VISIBLE);
                                            Map<String,Object> map = new HashMap<>();
                                            map.put("name",edit_config_nombres.getText().toString());
                                            map.put("surname",edit_config_surnames.getText().toString());
                                            map.put("phone",edit_config_phone.getText().toString());
                                            map.put("direction",edit_config_address.getText().toString());
                                            db.collection("passenger").document(auth.getCurrentUser().getUid())
                                                    .update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(getContext(), "Datos actualizados", Toast.LENGTH_SHORT).show();
                                                            progressBar_perfil.setVisibility(View.GONE);
                                                        }
                                                    });
                                        }
                                    }else{
                                        db.collection("administrator").document(auth.getCurrentUser().getUid())
                                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if (edit_config_nombres.getText().toString().isEmpty() ||
                                                        edit_config_surnames.getText().toString().isEmpty() ||
                                                        edit_config_address.getText().toString().isEmpty()){
                                                    Toast.makeText(getContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                                                }else {
                                                    progressBar_perfil.setVisibility(View.VISIBLE);
                                                    Map<String,Object> map = new HashMap<>();
                                                    map.put("name",edit_config_nombres.getText().toString());
                                                    map.put("surname",edit_config_surnames.getText().toString());
                                                    map.put("direction",edit_config_address.getText().toString());
                                                    db.collection("administrator").document(auth.getCurrentUser().getUid())
                                                            .update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(getContext(), "Datos actualizados", Toast.LENGTH_SHORT).show();
                                                            progressBar_perfil.setVisibility(View.GONE);
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                }
                            });

            }
        });


        return view;
    }
}