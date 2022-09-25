package com.example.myapplication.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.Models.Comment;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.Recargar_saldoDirections;
import com.example.myapplication.Sugerencias_reclamos_adminDirections;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ListViewCommentAdmin extends FirestoreRecyclerAdapter<Comment, ListViewCommentAdmin.ViewHolder> {
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ListViewCommentAdmin(@NonNull FirestoreRecyclerOptions<Comment> options) {super(options);}

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Comment model) {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(position);
        holder.author.setText(model.getAuthor());
        holder.message.setText(model.getMessage());
        holder.Responder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d("id_transport", documentSnapshot.getString("id_transport"));
                    // see auth and db
                    db.collection("administrator")
                            .document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshotAdmin) {
                                    if (documentSnapshotAdmin.exists()){
                                       String fullname = documentSnapshotAdmin.getString("name")+ " "
                                               + documentSnapshotAdmin.getString("surname");
                                        Map<String,Object> map = new HashMap<>();
                                        map.put("author",fullname);
                                        map.put("message",holder.respuesta.getText().toString());
                                        holder.respuesta.setText("");
                                        db.collection("comments")
                                                .document(documentSnapshot.getId())
                                                .collection("responses")
                                                .add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Log.d("respuesta","respuesta enviada");
                                                    }
                                                });
                                        Log.d("id_transport", auth.getUid());
                                    }
                                }
                            });
                }
            });

        holder.VerRespuestas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                NavDirections action = Sugerencias_reclamos_adminDirections
                        .actionNavSugerenciasReclamosToNavRespuestasComentarios2(documentSnapshot.getId());
                navController.navigate(action);
                }
            });
        }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_comment_admin, parent, false);
        return new ListViewCommentAdmin.ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView author, message ;
        Button Responder, VerRespuestas;
        EditText respuesta;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.txt_comentario_name);
            message = itemView.findViewById(R.id.txt_comentario_texto);
            Responder = itemView.findViewById(R.id.btn_responder);
            respuesta = itemView.findViewById(R.id.edit_respuesta);
            VerRespuestas = itemView.findViewById(R.id.btn_mostrar_respuesta);
        }
    }
}
