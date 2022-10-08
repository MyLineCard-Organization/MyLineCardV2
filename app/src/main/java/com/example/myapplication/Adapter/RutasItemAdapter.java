package com.example.myapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.Models.RutasItem;
import com.example.myapplication.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class RutasItemAdapter extends RecyclerView.Adapter<RutasItemAdapter.RutasItemViewHolder> {

    private List<RutasItem> rutasItems;
    private ViewPager2 viewPager2;

    public RutasItemAdapter(List<RutasItem> rutasItems, ViewPager2 viewPager2) {
        this.rutasItems = rutasItems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public RutasItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RutasItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_rutas, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RutasItemViewHolder holder, int position) {
        holder.setImage(rutasItems.get(position));
    }

    @Override
    public int getItemCount() {
        return rutasItems.size();
    }

    class RutasItemViewHolder extends RecyclerView.ViewHolder {
       private RoundedImageView imageView;

         RutasItemViewHolder(@NonNull View itemView) {
              super(itemView);
              imageView= itemView.findViewById(R.id.imageViewRutas);
         }
         void setImage(RutasItem rutasItem){
              imageView.setImageResource(rutasItem.getImage());
         }
    }
}
