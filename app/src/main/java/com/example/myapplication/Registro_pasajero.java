package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
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

        editName = findViewById(R.id.edit_passager_names);
        editSurnames = findViewById(R.id.edit_passeger_surnames);
        editEmail = findViewById(R.id.edit_passeger_email);
        editPass = findViewById(R.id.edit_passeger_password);
        editConfirmPass = findViewById(R.id.edit_passeger_confirm_password);
        editDirection = findViewById(R.id.edit_passeger_direction);
        editPhone = findViewById(R.id.edit_passeger_phone);

    }

    public void saveNewPassenger(View v){
        String Names = editName.getText().toString().trim();
        String Surnames = editSurnames.getText().toString().trim();
        String Email = editEmail.getText().toString().trim();
        String Pass = editPass.getText().toString().trim();
        String ConfirmPass = editConfirmPass.getText().toString().trim();
        String Direction = editDirection.getText().toString().trim();
        String Phone = editPhone.getText().toString().trim();

        if(Names.isEmpty() && Surnames.isEmpty() && Email.isEmpty() && Pass.isEmpty() && ConfirmPass.isEmpty() && Direction.isEmpty() && Phone.isEmpty()){
            Toast.makeText(this, "Complete todos los datos", Toast.LENGTH_SHORT).show();
        }
        else if(Pass.equals(ConfirmPass)){
            String newPass = sha256(Pass).toString();
                registerPassenger(Names,Surnames, Email, newPass, Direction, Phone);
        }
        else{
            Toast.makeText(this, Pass, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, ConfirmPass, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerPassenger(String name, String surname, String email, String pass, String Direction, String Phone){
        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                id = auth.getCurrentUser().getUid();
                Map<String,Object> map = new HashMap<>();
                map.put("id", id);
                map.put("name", name);
                map.put("surname", surname);
                map.put("email", email);
                map.put("password", pass);
                map.put("direction",Direction);
                map.put("phone",Phone);

                db.collection("passenger").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
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
            }



        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Registro_pasajero.this, "Error al registrar", Toast.LENGTH_SHORT).show();
            }
        });
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
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void administrador(View v){
        Intent intent= new Intent(this, Registro_administrador.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}