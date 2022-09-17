package com.example.myapplication;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Recargar_saldo extends Fragment {
    Button enlace;
    EditText price;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recargar_saldo, container, false);

        enlace = (Button) view.findViewById(R.id.btn_enlace_pago);
        enlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                price = (EditText) view.findViewById(R.id.edit_recargar_saldo);
                    if(Double.valueOf(price.getText().toString())>0){
                        NavController navController = Navigation.findNavController(view);
                        NavDirections action = Recargar_saldoDirections.actionNavRecargaToNavPagaSeguro(price.getText().toString());
                        navController.navigate(action);
                    }else {
                        Toast.makeText(getContext(), "Por favor ingrese un número mayor a 0", Toast.LENGTH_SHORT).show();
                    }
                }
        });
        return view;
    }

}