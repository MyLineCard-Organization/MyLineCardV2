package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class Home_admin extends Fragment {

    ImageButton btn_bus_icon;
    ImageButton btn_estadisticas_icon;
    ImageButton btn_control_icon;
    ImageButton btn_quejas_icon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home_admin, container, false);
        // Inflate the layout for this fragment

        btn_estadisticas_icon = (ImageButton) view.findViewById(R.id.btn_estadisticas_icon);
        btn_estadisticas_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.nav_home2);
            }
        });

        btn_control_icon = (ImageButton) view.findViewById(R.id.btn_control_icon);
        btn_control_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.nav_configuraciones);
            }
        });

        btn_quejas_icon = (ImageButton) view.findViewById(R.id.btn_quejas_icon);
        btn_quejas_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.nav_sugerencias_reclamos_pasajero);
            }
        });

        btn_bus_icon = (ImageButton) view.findViewById(R.id.btn_bus_icon);
        btn_bus_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.nav_lineas_afiliadas);
            }
        });

        return view;
    }
}