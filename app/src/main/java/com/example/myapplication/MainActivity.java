package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.MessageDigest;

public class MainActivity extends AppCompatActivity {
    private EditText editEmail, editPassword;
    private Button btn_login;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private ProgressBar progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        editEmail = findViewById(R.id.edit_login_email);
        editPassword = findViewById(R.id.edit_login_pass);
        progressBar2 = findViewById(R.id.progressBar2);
        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = editEmail.getText().toString().trim();
                String Password = editPassword.getText().toString().trim();

                if(Email.isEmpty() && Password.isEmpty()){
                    Toast.makeText(MainActivity.this, "Llene todas las casillas", Toast.LENGTH_SHORT).show();
                }else{
                    btn_login.setVisibility(View.GONE);
                    progressBar2.setVisibility(View.VISIBLE);
                    login(Email, Password);
                }
            }
        });
    }

    private void login(String email, String password){
        String passHash = sha256(password).toString();
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    isExist(auth.getCurrentUser().getUid());
                }else{
                    Toast.makeText(MainActivity.this, "Ingrese credenciales validas", Toast.LENGTH_SHORT).show();
                    progressBar2.setVisibility(View.GONE);
                    btn_login.setVisibility(View.VISIBLE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Cuenta no registrada", Toast.LENGTH_SHORT).show();
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

    public void isExist(String id){
        db.collection("passenger").document(id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            startActivity(new Intent(MainActivity.this, Despligue_Opciones.class));
                            Toast.makeText(MainActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            startActivity(new Intent(MainActivity.this, Administrador.class));
                            Toast.makeText(MainActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }



    public void Registrate(View v)
    {
        Intent intent= new Intent(this, Registro_pasajero.class);
        startActivity(intent);
    }

    public void recuperar(View v)
    {
        Intent intent= new Intent(this, Recuperar_password.class);
        startActivity(intent);
    }
}