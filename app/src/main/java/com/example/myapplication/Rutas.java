package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class Rutas extends Fragment {
    ViewFlipper viewFlipper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_rutas, container, false);
        viewFlipper = view.findViewById(R.id.viewFlipper);
        int images[] = {R.drawable.ruta1,R.drawable.ruta2,R.drawable.ruta3};
        for (int image:images){
            flipperImages(image);
        }
        return view;
    }

    public void flipperImages(int image){
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(image);
        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(5000);
        viewFlipper.setAutoStart(true);
        viewFlipper.setInAnimation(getContext(),android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(getContext(),android.R.anim.slide_out_right);
    }
}