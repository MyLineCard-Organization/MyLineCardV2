package com.example.myapplication;

import android.app.PendingIntent;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Despligue_Opciones extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityDespligueOpcionesBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private DocumentReference Data;
    private NavigationView navigationView;
    // Prueba
    TextView fullnameOptions;
    // NFC
    static final String TAG = "Read Data Activity";
    boolean writeMode;
    String UID;
    NfcAdapter nfcAdapter;
    IntentFilter writingTagFilters[];
    PendingIntent pendingIntent;
    Context context;
    String Mes, Year;
    NavController navController;
    Calendar calendario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDespligueOpcionesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarDespligueOpciones.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        navigationView = binding.navView;
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        calendario = Calendar.getInstance();
        Year = String.valueOf(calendario.get(Calendar.YEAR));
        Mes = String.valueOf(calendario.get(Calendar.MONTH)+1);
        context = this;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_lineas_afiliadas,R.id.nav_tarifas,R.id.nav_rutas,R.id.nav_pagos,R.id.nav_manual,R.id.nav_configuraciones, R.id.nav_sugerencias_reclamos_pasajero, R.id.nav_preguntas_frecuentes, R.id.nav_sign_out)
                .setOpenableLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_despligue_opciones);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        // NFC SYSTEM
        nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        if(nfcAdapter == null){
            Toast.makeText(context, "This device does not support NFC", Toast.LENGTH_SHORT).show();
        }
        readFromIntent(getIntent());
        pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writingTagFilters = new IntentFilter[] { tagDetected };
        //
        NavigationView SignOut = findViewById(R.id.nav_sign_out);
        navigationView.setItemIconTintList(null);

        navigationView.getMenu().findItem(R.id.nav_sign_out).setOnMenuItemClickListener(menuItem -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Despligue_Opciones.this);
            builder.setMessage("¿Desea salir de la aplicación?")
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            auth.signOut();
                            startActivity(new Intent(Despligue_Opciones.this,MainActivity.class));
                            Toast.makeText(Despligue_Opciones.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();

                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setTitle("Cerrar Sesión");
            alert.show();
            return true;
        });
        updateNavHeader();
        // Llamas a la funcion para q se ejecute
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (navigationView.getMenu().findItem(R.id.nav_home).isChecked()) {
                readFromIntent(intent);
        }
    }

    ///NFC
    private void readFromIntent(Intent intent){

        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)){
            Tag tag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
            //byte[] tagId = getActivity().getIntent().getByteArrayExtra(NfcAdapter.EXTRA_ID);
            UID = bin2hex(tag.getId());
            //Toast.makeText(context, "UID: " + bin2hex(tag.getId()), Toast.LENGTH_SHORT).show();
            Toast.makeText(context, "Procesando pago...", Toast.LENGTH_SHORT).show();
            // Query para buscar uid
            db.collection("transportation")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                // Las empresas registradas
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    db.collection("transportation")
                                            .document(document.getId())
                                            .collection("record")
                                            .document(UID)
                                            .get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    if(documentSnapshot.exists() && documentSnapshot.getBoolean("value")){
                                                        db.collection("transportation")
                                                                .document(document.getId())
                                                                .collection(UID)
                                                                .whereEqualTo("month",Mes)
                                                                .get()
                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        if(task.isSuccessful() && !task.getResult().isEmpty()){
                                                                            // Si exite FDE200D0
                                                                            ///// Usuario
                                                                            String id = auth.getCurrentUser().getUid();
                                                                            db.collection("passenger")
                                                                                    .document(id)
                                                                                    .collection("wallet")
                                                                                    .document(id)
                                                                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                                        @Override
                                                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                           //Entro
                                                                                            if(documentSnapshot.exists()&&(documentSnapshot.getDouble("balance"))>=document.getDouble("price")){

                                                                                                ///
                                                                                                DecimalFormat twoDForm = new DecimalFormat("#.##");
                                                                                                Double descuento = Double.valueOf(twoDForm
                                                                                                        .format(documentSnapshot.getDouble("balance")-document.getDouble("price")));
                                                                                                Map<String,Object> map = new HashMap<>();
                                                                                                map.put("balance", descuento);
                                                                                                db.collection("passenger")
                                                                                                        .document(id)
                                                                                                        .collection("wallet")
                                                                                                        .document(id)
                                                                                                        .update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onSuccess(Void unused) {
                                                                                                                //Toast.makeText(Despligue_Opciones.this, "Entro wallet", Toast.LENGTH_SHORT).show();
                                                                                                                DocumentSnapshot resultado = task.getResult().getDocuments().get(0);
                                                                                                                DecimalFormat twoDForm = new DecimalFormat("#.##");
                                                                                                                Double ganacias = Double.valueOf(twoDForm
                                                                                                                        .format(resultado.getDouble("earnings")+ document.getDouble("price").floatValue())) ;
                                                                                                                Map<String,Object> map = new HashMap<>();
                                                                                                                map.put("earnings", ganacias);
                                                                                                                db.collection("transportation")
                                                                                                                        .document(document.getId())
                                                                                                                        .collection(UID).document(resultado.getId())
                                                                                                                        .update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                            @Override
                                                                                                                            public void onSuccess(Void unused) {
                                                                                                                                Historial(document, id);
                                                                                                                            }
                                                                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                                                                            @Override
                                                                                                                            public void onFailure(@NonNull Exception e) {
                                                                                                                                // Fallo
                                                                                                                            }
                                                                                                                        });
                                                                                                            }
                                                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                                                            @Override
                                                                                                            public void onFailure(@NonNull Exception e) {
                                                                                                               //fallo
                                                                                                            }
                                                                                                        });
                                                                                                ///
                                                                                            }else {
                                                                                                MediaPlayer mp = MediaPlayer.create(Despligue_Opciones.this,R.raw.soundfalse);
                                                                                                mp.start();
                                                                                                Toast.makeText(Despligue_Opciones.this, "No dispone de saldo suficiente. Recargue saldo", Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        }
                                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                                        @Override
                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                            //fallo
                                                                                        }
                                                                                    });
                                                                            ///////
                                                                        }else {
                                                                            // No existe
                                                                            String id = auth.getCurrentUser().getUid();
                                                                            db.collection("passenger")
                                                                                    .document(id)
                                                                                    .collection("wallet")
                                                                                    .document(id)
                                                                                    .get()
                                                                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                                        @Override
                                                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                                                            if(documentSnapshot.exists()&&documentSnapshot.getDouble("balance")>=document.getDouble("price")){
                                                                                                // Existe
                                                                                                DecimalFormat twoDForm = new DecimalFormat("#.##");
                                                                                                Double descuento = Double.valueOf(twoDForm
                                                                                                        .format(documentSnapshot.getDouble("balance")-document.getDouble("price")));
                                                                                                Map<String,Object> map = new HashMap<>();
                                                                                                map.put("balance", descuento);
                                                                                                db.collection("passenger")
                                                                                                        .document(id)
                                                                                                        .collection("wallet")
                                                                                                        .document(id)
                                                                                                        .update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onSuccess(Void unused) {
                                                                                                                DecimalFormat twoDForm = new DecimalFormat("#.##");
                                                                                                                Double ganacias = Double.valueOf(twoDForm
                                                                                                                        .format(document.getDouble("price"))) ;
                                                                                                                Map<String,Object> map = new HashMap<>();
                                                                                                                map.put("earnings", ganacias);
                                                                                                                map.put("month", Mes);
                                                                                                                map.put("year", Year);
                                                                                                                db.collection("transportation")
                                                                                                                        .document(document.getId())
                                                                                                                        .collection(UID)
                                                                                                                        .document().set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                            @Override
                                                                                                                            public void onSuccess(Void unused) {
                                                                                                                               Historial(document, id);
                                                                                                                            }
                                                                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                                                                            @Override
                                                                                                                            public void onFailure(@NonNull Exception e) {
                                                                                                                                // Error al crear documento
                                                                                                                            }
                                                                                                                        });
                                                                                                            }
                                                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                                                            @Override
                                                                                                            public void onFailure(@NonNull Exception e) {
                                                                                                                // fallo
                                                                                                            }
                                                                                                        });

                                                                                            }else{
                                                                                                MediaPlayer mp = MediaPlayer.create(Despligue_Opciones.this,R.raw.soundfalse);
                                                                                                mp.start();
                                                                                                // No existe
                                                                                                Toast.makeText(Despligue_Opciones.this, "No dispone de saldo", Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        }
                                                                                    });
                                                                        }
                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        // Error
                                                                    }
                                                                });
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // fallo
                                                }
                                            });
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error
                        }
                    });

        }else {
            //Toast.makeText(context, "No detecta", Toast.LENGTH_SHORT).show();
        }
    }///

    public void Historial(QueryDocumentSnapshot document,String id){
        // Funtion autoincrement
        db.collection("passenger")
                .document(id)
                .collection("position")
                .document("pos")
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshotNumber) {
                        if(documentSnapshotNumber.exists()){
                            /// Historial
                            SimpleDateFormat Fecha = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat Hora = new SimpleDateFormat("HH:mm:ss");
                            String FechaFormat = Fecha.format(new Date());
                            String HoraFormat = Hora.format(new Date());

                            Map<String,Object> map = new HashMap<>();
                            map.put("transportation", document.getString("name"));
                            map.put("date", FechaFormat);
                            map.put("hour", HoraFormat);
                            map.put("price", document.getDouble("price"));

                            db.collection("passenger")
                                    .document(id)
                                    .collection("historic")
                                    .document(String.valueOf(Integer.parseInt(documentSnapshotNumber.getString("position"))+1))
                                    .set(map);
                            Map<String,Object> posi = new HashMap<>();
                            posi.put("position", String.valueOf(Integer.parseInt(documentSnapshotNumber.getString("position"))+1));
                            db.collection("passenger")
                                    .document(id)
                                    .collection("position")
                                    .document("pos")
                                    .update(posi);
                        }else {
                            Map<String,Object> posi = new HashMap<>();
                            posi.put("position", "1");
                            db.collection("passenger")
                                    .document(id)
                                    .collection("position")
                                    .document("pos")
                                    .set(posi);
                            /// Historial
                            SimpleDateFormat Fecha = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat Hora = new SimpleDateFormat("HH:mm:ss");
                            String FechaFormat = Fecha.format(new Date());
                            String HoraFormat = Hora.format(new Date());

                            Map<String,Object> map2 = new HashMap<>();
                            map2.put("transportation", document.getString("name"));
                            map2.put("date", FechaFormat);
                            map2.put("hour", HoraFormat);
                            map2.put("price", document.getDouble("price"));

                            db.collection("passenger")
                                    .document(id)
                                    .collection("historic")
                                    .document("1")
                                    .set(map2);
                        }
                    }
                });

        // Se actualizo correctamente
        MediaPlayer mp = MediaPlayer.create(Despligue_Opciones.this,R.raw.sound);
        mp.start();
        navController.navigate(R.id.nav_pago_exitoso);
    }

    static String bin2hex(byte[] data) {
        return String.format("%0" + (data.length * 2) + "X", new BigInteger(1,data));
    }

    @Override
    public void onPause(){
        super.onPause();
        WriteModeOff();
    }
    @Override
    public void onResume(){
        super.onResume();
        WriteModeOn();
    }

    private void WriteModeOn(){
        writeMode = true;
        nfcAdapter.enableForegroundDispatch(this,pendingIntent,writingTagFilters,null);
    }

    private void WriteModeOff(){
        writeMode = false;
        nfcAdapter.disableForegroundDispatch(this);
    }
    ///

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.despligue__opciones, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_despligue_opciones);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    public void updateNavHeader() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View herderView = navigationView.getHeaderView(0);
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