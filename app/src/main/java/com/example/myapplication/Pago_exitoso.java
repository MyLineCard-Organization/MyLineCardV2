package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class Pago_exitoso extends Fragment {
    Button inicio;
    TextView hora;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pago_exitoso, container, false);
        inicio = (Button) view.findViewById(R.id.pago_exitoso_btn);
        hora = (TextView) view.findViewById(R.id.hora);
        Calendar calendario = Calendar.getInstance();
        hora.setText(calendario.get(Calendar.HOUR_OF_DAY)+ ":"+calendario.get(Calendar.MINUTE)+":"+calendario.get(Calendar.SECOND));
        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.nav_home);
            }
        });
        return view;
    }
}