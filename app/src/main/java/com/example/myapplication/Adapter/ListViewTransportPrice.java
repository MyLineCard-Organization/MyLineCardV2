package com.example.myapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Transportation;
import com.example.myapplication.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ListViewTransportPrice extends FirestoreRecyclerAdapter<Transportation, ListViewTransportPrice.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ListViewTransportPrice(@NonNull FirestoreRecyclerOptions<Transportation> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ListViewTransportPrice.ViewHolder holder, int position, @NonNull Transportation model) {
        holder.name.setText(model.getName());
        holder.price.setText(model.getPrice().toString());
    }

    @NonNull
    @Override
    public ListViewTransportPrice.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_lineas_price, parent, false);
        return new ListViewTransportPrice.ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_bus_name_tarifa);
            price = itemView.findViewById(R.id.text_bus_price_tarifa);
        }
    }
}
