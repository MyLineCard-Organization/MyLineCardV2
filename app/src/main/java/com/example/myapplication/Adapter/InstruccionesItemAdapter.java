package com.example.myapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.Models.InstruccionesItem;
import com.example.myapplication.R;

import java.util.List;

public class InstruccionesItemAdapter extends RecyclerView.Adapter<InstruccionesItemAdapter.InstruccionesItemViewHolder> {

    private List<InstruccionesItem> instruccionesItems;
    private ViewPager2 viewPager2;

    public InstruccionesItemAdapter(List<InstruccionesItem> instruccionesItems, ViewPager2 viewPager2) {
        this.instruccionesItems = instruccionesItems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public InstruccionesItemAdapter.InstruccionesItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InstruccionesItemAdapter.InstruccionesItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.instructions_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InstruccionesItemAdapter.InstruccionesItemViewHolder holder, int position) {
        holder.setImage(instruccionesItems.get(position));
    }

    @Override
    public int getItemCount() {
        return instruccionesItems.size();
    }

    class InstruccionesItemViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView imageView;

        InstruccionesItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView= itemView.findViewById(R.id.imageViewInstrucciones);
        }
        void setImage(InstruccionesItem instruccionesItems){
            imageView.setImageResource(instruccionesItems.getImage());
        }
    }

}
