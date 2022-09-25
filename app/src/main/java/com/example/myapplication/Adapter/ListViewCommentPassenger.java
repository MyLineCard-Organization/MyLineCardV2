package com.example.myapplication.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Comment;
import com.example.myapplication.R;
import com.example.myapplication.Sugerencias_reclamos_adminDirections;
import com.example.myapplication.Sugerencias_reclamos_pasajeroDirections;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ListViewCommentPassenger extends FirestoreRecyclerAdapter<Comment, ListViewCommentPassenger.ViewHolder> {
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ListViewCommentPassenger(@NonNull FirestoreRecyclerOptions<Comment> options) {super(options);}

    @Override
    protected void onBindViewHolder(@NonNull ListViewCommentPassenger.ViewHolder holder, int position, @NonNull Comment model) {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(position);
        holder.author.setText(model.getAuthor());
        holder.message.setText(model.getMessage());
        holder.VerRespuestas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                NavDirections action = Sugerencias_reclamos_pasajeroDirections
                        .actionNavSugerenciasReclamosPasajeroToNavRespuestasComentariosPasajero(documentSnapshot.getId());
                navController.navigate(action);
            }
        });
    }

    @NonNull
    @Override
    public ListViewCommentPassenger.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_ver_comentarios, parent, false);
        return new ListViewCommentPassenger.ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView author, message ;
        Button VerRespuestas;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.txt_comentario_name_pasajero);
            message = itemView.findViewById(R.id.txt_comentario_texto_pasajero);
            VerRespuestas = itemView.findViewById(R.id.btn_mostrar_respuesta_pasajero);
        }
    }
}
