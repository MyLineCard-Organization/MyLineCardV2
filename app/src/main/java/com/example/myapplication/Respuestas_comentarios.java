package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class Respuestas_comentarios extends Fragment {
    String id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_respuestas_comentarios, container, false);
        id = Respuestas_comentariosArgs.fromBundle(getArguments()).getId();
        Toast.makeText(getContext(), "El id es "+id, Toast.LENGTH_SHORT).show();
        return view;
    }
}