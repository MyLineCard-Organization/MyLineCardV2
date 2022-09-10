package com.example.myapplication;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityDespligueOpcionesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Despligue_Opciones extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityDespligueOpcionesBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private DocumentReference Data;
    // Prueba
    TextView fullnameOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDespligueOpcionesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarDespligueOpciones.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        //cardRecarga = (CardView) findViewById(R.id.Recarga);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_lineas_afiliadas,R.id.nav_tarifas,R.id.nav_rutas,R.id.nav_pagos,R.id.nav_manual,R.id.nav_configuraciones, R.id.nav_sign_out)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_despligue_opciones);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        NavigationView SignOut = findViewById(R.id.nav_sign_out);
        navigationView.getMenu().findItem(R.id.nav_sign_out).setOnMenuItemClickListener(menuItem -> {
            finish();
            auth.signOut();
            startActivity(new Intent(Despligue_Opciones.this,MainActivity.class));
            Toast.makeText(Despligue_Opciones.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
            return true;
        });


        updateNavHeader();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.despligue__opciones, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_despligue_opciones);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    public void updateNavHeader() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View herderView = navigationView .getHeaderView(0);
        fullnameOptions = herderView.findViewById(R.id.text_fullname_options);
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
                                fullnameOptions.setText(name+" "+lastname);
                            } else {
                                Toast.makeText(Despligue_Opciones.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Despligue_Opciones.this, "Error!", Toast.LENGTH_SHORT).show();
                        }
                    });
            /// Data
        }
    }


}