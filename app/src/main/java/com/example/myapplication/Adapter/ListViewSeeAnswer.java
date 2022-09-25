package com.example.myapplication.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.Models.Comment;
import com.example.myapplication.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ListViewSeeAnswer extends FirestoreRecyclerAdapter<Comment, ListViewSeeAnswer.ViewHolder> {
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ListViewSeeAnswer(@NonNull FirestoreRecyclerOptions<Comment> options) {super(options);}

    @Override
    protected void onBindViewHolder(@NonNull ListViewSeeAnswer.ViewHolder holder, int position, @NonNull Comment model) {
        holder.author.setText(model.getAuthor());
        holder.message.setText(model.getMessage());

    }

    @NonNull
    @Override
    public ListViewSeeAnswer.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_ver_mensajes, parent, false);
        return new ListViewSeeAnswer.ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView author, message ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.txt_respuesta_name);
            message = itemView.findViewById(R.id.txt_respuesta_texto);
        }
    }
}