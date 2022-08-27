package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    public void principal(View v)
    {
        Intent intent= new Intent(this, Despligue_Opciones.class);
        startActivity(intent);
    }

}