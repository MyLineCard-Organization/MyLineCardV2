package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class Registro_pasajero extends AppCompatActivity {

    private EditText editName, editSurnames, editEmail, editPass, editConfirmPass, editDirection, editPhone;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser mFirebaseUser;
    private String id;
    private Button btn_register_pasajero;
    private ProgressBar progressBar_pasajero;
    private ImageButton btn_politicas_pasajero,btn_terminos_pasajero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_pasajero);

        // Get start Firebase

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        mFirebaseUser= auth.getCurrentUser();

        // Information of EditText
        btn_register_pasajero = findViewById(R.id.btn_register_pasajero);
        progressBar_pasajero = findViewById(R.id.progressBar_pasajero);
        editName = findViewById(R.id.edit_passenger_names);
        editSurnames = findViewById(R.id.edit_passenger_surnames);
        editEmail = findViewById(R.id.edit_passenger_email);
        editPass = findViewById(R.id.edit_passenger_password);
        editConfirmPass = findViewById(R.id.edit_passenger_confirm_password);
        editDirection = findViewById(R.id.edit_passenger_direction);
        editPhone = findViewById(R.id.edit_passeger_phone);
        btn_politicas_pasajero = findViewById(R.id.btn_politicas_pasajero);
        btn_terminos_pasajero = findViewById(R.id.btn_terminos_pasajero);


        // Button Policas
        btn_politicas_pasajero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateSharePreferenceRegister();
                Intent intent = new Intent(Registro_pasajero.this,Politicas_registro.class);
                startActivity(intent);
            }
        });
        // Button Terminos

        //CreateSharePreferenceRegister();
        ReadSharePreferenceRegister();

        btn_terminos_pasajero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateSharePreferenceRegister();
                Intent intent = new Intent(Registro_pasajero.this,Terminos_Y_Condiciones_resgistro.class);
                startActivity(intent);
            }
        });

        btn_register_pasajero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Names = editName.getText().toString().trim();
                String Surnames = editSurnames.getText().toString().trim();
                String Email = editEmail.getText().toString().trim();
                String Pass = editPass.getText().toString().trim();
                String ConfirmPass = editConfirmPass.getText().toString().trim();
                String Direction = editDirection.getText().toString().trim();
                String Phone = editPhone.getText().toString().trim();

                if(Names.isEmpty() && Surnames.isEmpty() && Email.isEmpty() && Pass.isEmpty() && ConfirmPass.isEmpty() && Direction.isEmpty() && Phone.isEmpty()){
                    Toast.makeText(Registro_pasajero.this, "Complete todos los datos", Toast.LENGTH_SHORT).show();
                }
                else if(Pass.equals(ConfirmPass)){

                    registerPassenger(Names,Surnames, Email, Pass, Direction, Phone);
                }
                else{
                    Toast.makeText(Registro_pasajero.this, Pass, Toast.LENGTH_SHORT).show();
                    Toast.makeText(Registro_pasajero.this, ConfirmPass, Toast.LENGTH_SHORT).show();
                    Toast.makeText(Registro_pasajero.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void ReadSharePreferenceRegister() {
        SharedPreferences preferences = getSharedPreferences("register_pasajero",MODE_PRIVATE);
        editName.setText(preferences.getString("name",""));
        editSurnames.setText(preferences.getString("surnames",""));
        editEmail.setText(preferences.getString("email",""));
        editPass.setText(preferences.getString("pass",""));
        editConfirmPass.setText(preferences.getString("confirm_pass",""));
        editDirection.setText(preferences.getString("direction",""));
        editPhone.setText(preferences.getString("phone",""));
    }

    private void CreateSharePreferenceRegister() {
        SharedPreferences sharedPref = getSharedPreferences("register_pasajero", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("name", editName.getText().toString());
        editor.putString("surnames", editSurnames.getText().toString());
        editor.putString("email", editEmail.getText().toString());
        editor.putString("pass", editPass.getText().toString());
        editor.putString("confirm_pass", editConfirmPass.getText().toString());
        editor.putString("direction", editDirection.getText().toString());
        editor.putString("phone", editPhone.getText().toString());
        editor.apply();
    }

    private void registerPassenger(String name, String surname, String email, String pass, String Direction, String Phone){
        String newPass = sha256(pass).toString();
        progressBar_pasajero.setVisibility(View.VISIBLE);
        btn_register_pasajero.setVisibility(View.GONE);

        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    id = auth.getCurrentUser().getUid();
                    Map<String,Object> map = new HashMap<>();
                    map.put("id", id);
                    map.put("name", name);
                    map.put("surname", surname);
                    map.put("email", email);
                    map.put("password", newPass);
                    map.put("direction",Direction);
                    map.put("phone",Phone);
                    map.put("status",true);

                    db.collection("passenger").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            DeleteSharePreferenceRegister();

                            finish();
                            startActivity(new Intent(Registro_pasajero.this, MainActivity.class));
                            Toast.makeText(Registro_pasajero.this, "Pasajero registrado!!!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Registro_pasajero.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    btn_register_pasajero.setVisibility(View.VISIBLE);
                    progressBar_pasajero.setVisibility(View.GONE);
                    Toast.makeText(Registro_pasajero.this, "La contraseña debe contener una mayuscula", Toast.LENGTH_SHORT).show();
                }

            }



        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Registro_pasajero.this, "Error al registrar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void DeleteSharePreferenceRegister() {
        SharedPreferences preferences = getSharedPreferences("register_pasajero",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public static String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public void back(View v){
        CreateSharePreferenceRegister();
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void administrador(View v){
        CreateSharePreferenceRegister();
        Intent intent= new Intent(this, Registro_administrador.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}