package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.navigation.NavigationView;

public class Home_admin extends Fragment{

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
                //NavController navController = Navigation.findNavController(getActivity(), R.id.nav_home2);
                //return NavigationUI.onNavDestinationSelected(v, navController);
                //Navigation.findNavController(view).navigate(R.id.nav_home2);
                //NavHostFragment.findNavController(Home_admin.this).navigate(R.id.nav_home2);
                //navigationView.getMenu().getItem(1).setChecked(true);
                Fragment estadistica = new Estadisticas();
                FragmentTransaction fn = getActivity().getSupportFragmentManager().beginTransaction();
                fn.replace(R.id.container, estadistica).commit();
            }
        });

        btn_control_icon = (ImageButton) view.findViewById(R.id.btn_control_icon);
        btn_control_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //NavController navController = Navigation.findNavController(view);
                //navController.navigate(R.id.nav_configuraciones);
            }
        });

        btn_quejas_icon = (ImageButton) view.findViewById(R.id.btn_quejas_icon);
        btn_quejas_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //NavController navController = Navigation.findNavController(view);
                //navController.navigate(R.id.nav_sugerencias_reclamos_pasajero);
                Fragment reclamos = new Sugerencias_reclamos_admin();
                FragmentTransaction fn = getActivity().getSupportFragmentManager().beginTransaction();
                fn.replace(R.id.container, reclamos).commit();
            }
        });

        btn_bus_icon = (ImageButton) view.findViewById(R.id.btn_bus_icon);
        btn_bus_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //NavController navController = Navigation.findNavController(view);
                //navController.navigate(R.id.nav_lineas_afiliadas);
                Fragment bus = new Lineas_Afiliadas();
                FragmentTransaction fn = getActivity().getSupportFragmentManager().beginTransaction();
                fn.replace(R.id.container, bus).commit();
            }
        });

        return view;
    }
}