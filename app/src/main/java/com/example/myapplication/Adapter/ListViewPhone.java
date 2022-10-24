package com.example.myapplication.Adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Transportation;
import com.example.myapplication.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ListViewPhone extends FirestoreRecyclerAdapter<Transportation, ListViewPhone.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ListViewPhone(@NonNull FirestoreRecyclerOptions<Transportation> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ListViewPhone.ViewHolder holder, int position, @NonNull Transportation model) {
        holder.name.setText(model.getName());
        holder.phone.setText(model.getPhone());
        holder.linearLayoutPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_VIEW);
                String uri = "whatsapp://send?phone=" +"+51"+holder.phone.getText().toString()+"&text=Hola, tengo una consulta";
                sendIntent.setData(Uri.parse(uri));
                v.getContext().startActivity(sendIntent);
            }
        });
    }

    @NonNull
    @Override
    public ListViewPhone.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_phone, parent, false);
        return new ListViewPhone.ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone;
        LinearLayout linearLayoutPhone;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_bus_name_phone);
            phone = itemView.findViewById(R.id.text_bus_phone);
            linearLayoutPhone = itemView.findViewById(R.id.linearLayoutTransportPhone);
        }
    }
}
