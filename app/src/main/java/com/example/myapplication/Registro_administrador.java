package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class Registro_administrador extends AppCompatActivity {
    private EditText editName, editSurnames, editEmail, editPass, editConfirmPass, editDirection, editCode;
    private Button btnRegister;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser mFirebaseUser;
    private String id, code;
    private ImageButton btn_politicas_admin,btn_terminos_admin;
    private ProgressBar progressBar_admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_administrador);

        // Get start Firebase

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        mFirebaseUser= auth.getCurrentUser();

        // Information of EditText
        progressBar_admin = findViewById(R.id.progressBar_admin);
        editName = findViewById(R.id.edit_admin_names);
        editSurnames = findViewById(R.id.edit_admin_surnames);
        editEmail = findViewById(R.id.edit_admin_email);
        editPass = findViewById(R.id.edit_admin_password);
        editConfirmPass = findViewById(R.id.edit_admin_confirm_password);
        editDirection = findViewById(R.id.edit_admin_direction);
        editCode = findViewById(R.id.edit_admin_codigo);
        btn_politicas_admin = findViewById(R.id.btn_politicas_admin);
        btn_terminos_admin = findViewById(R.id.btn_terminos_admin);
        btnRegister = findViewById(R.id.btn_admin_register);
        // Button Policas
       btn_politicas_admin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
            Intent intent = new Intent(Registro_administrador.this,Politicas_registro.class);
            startActivity(intent);
           }
       });
        // Button Terminos
        btn_terminos_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registro_administrador.this,Terminos_Y_Condiciones_resgistro.class);
                startActivity(intent);
            }
        });

        // Button to register


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editCode.getText().toString().trim().isEmpty()){
                    Toast.makeText(Registro_administrador.this, "Registre el codigo", Toast.LENGTH_SHORT).show();
                }else {
                    validateCode(v);
                }

            }
        });


    }

    public void validateCode(View view){
        progressBar_admin.setVisibility(View.VISIBLE);
        btnRegister.setVisibility(View.INVISIBLE);
        db.collection("transportation")
                .whereEqualTo("check", editCode.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && !task.getResult().isEmpty()){
                            task.getResult().getDocuments().get(0).getReference().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if(documentSnapshot.exists()){
                                        code= documentSnapshot.getString("id");
                                        saveNewPassenger();
                                    }else {
                                        Toast.makeText(Registro_administrador.this, "No existe", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else {
                            progressBar_admin.setVisibility(View.INVISIBLE);
                            btnRegister.setVisibility(View.VISIBLE);
                            Toast.makeText(Registro_administrador.this, "El código no es válido", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void saveNewPassenger(){
        String Names = editName.getText().toString().trim();
        String Surnames = editSurnames.getText().toString().trim();
        String Email = editEmail.getText().toString().trim();
        String Pass = editPass.getText().toString().trim();
        String ConfirmPass = editConfirmPass.getText().toString().trim();
        String Direction = editDirection.getText().toString().trim();

        if(Names.isEmpty() && Surnames.isEmpty() && Email.isEmpty() && Pass.isEmpty() && ConfirmPass.isEmpty() && Direction.isEmpty()){
            Toast.makeText(this, "Complete todos los datos", Toast.LENGTH_SHORT).show();
        }
        else if(Pass.equals(ConfirmPass)){

            registerPassenger(Names,Surnames, Email, Pass, Direction);
        }
        else{
            Toast.makeText(this, Pass, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, ConfirmPass, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerPassenger(String name, String surname, String email, String pass, String Direction){
        String newPass = sha256(pass).toString();
        progressBar_admin.setVisibility(View.VISIBLE);
        btnRegister.setVisibility(View.INVISIBLE);
        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Map<String,Object> map = new HashMap<>();
                    map.put("id", auth.getCurrentUser().getUid());
                    map.put("name", name);
                    map.put("surname", surname);
                    map.put("email", email);
                    map.put("password", newPass);
                    map.put("direction",Direction);
                    map.put("transport_id", code);

                    db.collection("administrator").document(auth.getCurrentUser().getUid()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            finish();
                            startActivity(new Intent(Registro_administrador.this, MainActivity.class));
                            Toast.makeText(Registro_administrador.this, "Administrador registrado!!!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Registro_administrador.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    progressBar_admin.setVisibility(View.INVISIBLE);
                    btnRegister.setVisibility(View.VISIBLE);
                    Toast.makeText(Registro_administrador.this, "La contraseña debe contener una mayuscula", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Registro_administrador.this, "Error al registrar", Toast.LENGTH_SHORT).show();
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

    /// Other methods

    public void back(View v){
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void pasajero(View v){
        Intent intent= new Intent(this, Registro_pasajero.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}