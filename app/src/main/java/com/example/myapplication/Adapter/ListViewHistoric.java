package com.example.myapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Historic;
import com.example.myapplication.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ListViewHistoric extends FirestoreRecyclerAdapter<Historic, ListViewHistoric.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ListViewHistoric(@NonNull FirestoreRecyclerOptions<Historic> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Historic model) {
        holder.date.setText(model.getDate());
        holder.hour.setText(model.getHour());
        holder.price.setText(String.valueOf(model.getPrice()));
        holder.transportation.setText(model.getTransportation());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_historial, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, hour, price, transportation ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.historial_fecha_txt);
            hour = itemView.findViewById(R.id.historial_hora_txt);
            price = itemView.findViewById(R.id.historial_price_txt);
            transportation = itemView.findViewById(R.id.historial_nombre_emprese_txt);
        }
    }
}
