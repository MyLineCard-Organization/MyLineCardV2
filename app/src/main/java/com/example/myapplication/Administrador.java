package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityAdministradorBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Administrador extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityAdministradorBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private NavigationView navigationView;
    TextView Option;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        binding = ActivityAdministradorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarAdministrador.toolbar2);
        DrawerLayout drawer = binding.drawerLayout2;
        navigationView = binding.navView2;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home2, R.id.nav_lineas_afiliadas,R.id.nav_tarifas,R.id.nav_configuraciones, R.id.nav_sugerencias_reclamos)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_administrador);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        // sign off
        navigationView.getMenu().findItem(R.id.nav_sign_out_admin).setOnMenuItemClickListener(menuItem -> {
            finish();
            auth.signOut();
            startActivity(new Intent(Administrador.this,MainActivity.class));
            Toast.makeText(Administrador.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
            return true;
        });
        updateNavHeader();
    }

    public void updateNavHeader() {
        FirebaseUser user = auth.getCurrentUser();
        View herderView = navigationView.getHeaderView(0);
        Option = herderView.findViewById(R.id.text_fullname_administrador);
        if(user != null){
            /// Data
            String doc = auth.getCurrentUser().getUid();
            db.collection("administrator").document(doc)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String name = documentSnapshot.getString("name");
                                String lastname = documentSnapshot.getString("surname");
                                String fullname = name + " " + lastname;
                                if (Option != null) {
                                    Option.setText(fullname);
                                }
                            } else {
                                Toast.makeText(Administrador.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Administrador.this, "Error!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.administrador, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_administrador);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}