package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.example.myapplication.Adapter.InstruccionesItemAdapter;
import com.example.myapplication.Adapter.RutasItemAdapter;
import com.example.myapplication.Models.InstruccionesItem;
import com.example.myapplication.Models.RutasItem;

import java.util.ArrayList;
import java.util.List;

public class Manual_Instrucciones extends Fragment {
    private ViewPager2 viewPager2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_manual__instrucciones, container, false);
        viewPager2 = view.findViewById(R.id.viewPagerImageManual);
        List<InstruccionesItem> instruccionesItems = new ArrayList<>();

        instruccionesItems.add(new InstruccionesItem(R.drawable.instrucion1));
        instruccionesItems.add(new InstruccionesItem(R.drawable.instrucion2));

        viewPager2.setAdapter(new InstruccionesItemAdapter(instruccionesItems, viewPager2));
        return view;


    }

}