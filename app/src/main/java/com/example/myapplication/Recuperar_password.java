package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Recuperar_password extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;
    private EditText recuperarEmailTxt;
    private Button recuperarEmailBtn;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_password);

        recuperarEmailTxt = (EditText) findViewById(R.id.recuperar_email_editText);

        recuperarEmailBtn = (Button) findViewById(R.id.recuperar_pass_btn);
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        recuperarEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = recuperarEmailTxt.getText().toString();
                if(!email.isEmpty())
                {
                    progressDialog.setMessage("Espera un momento...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    resetPass();
                }else {
                    Toast.makeText(Recuperar_password.this, "Ingrese Email", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void back(View v){
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void resetPass(){
        auth.setLanguageCode("es");
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(Recuperar_password.this, "Se envio el correo para restablecer la contraseña", Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(Recuperar_password.this, "No se pudo enviar el correo para restablecer la contraseña", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }
}