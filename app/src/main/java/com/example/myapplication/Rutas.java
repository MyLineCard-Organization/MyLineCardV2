package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.example.myapplication.Adapter.RutasItemAdapter;
import com.example.myapplication.Models.RutasItem;

import java.util.ArrayList;
import java.util.List;

public class Rutas extends Fragment {
    private ViewPager2 viewPager2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_rutas, container, false);
        viewPager2 = view.findViewById(R.id.viewPagerImageSlider);
        List<RutasItem> rutasItems = new ArrayList<>();

        rutasItems.add(new RutasItem(R.drawable.ruta1));
        rutasItems.add(new RutasItem(R.drawable.ruta2));
        rutasItems.add(new RutasItem(R.drawable.ruta3));

        viewPager2.setAdapter(new RutasItemAdapter(rutasItems, viewPager2));
        return view;
    }
}