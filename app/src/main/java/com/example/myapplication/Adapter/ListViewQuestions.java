package com.example.myapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Comment;
import com.example.myapplication.Models.Questions;
import com.example.myapplication.R;
import com.example.myapplication.Sugerencias_reclamos_pasajeroDirections;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ListViewQuestions extends FirestoreRecyclerAdapter<Questions, ListViewQuestions.ViewHolder> {
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ListViewQuestions(@NonNull FirestoreRecyclerOptions<Questions> options) {super(options);}

    @Override
    protected void onBindViewHolder(@NonNull ListViewQuestions.ViewHolder holder, int position, @NonNull Questions model) {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(position);
        holder.answer.setText(model.getAnswer());
        holder.questions.setText(model.getQuestions());
        holder.verRespuestas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.respuestas.setVisibility(View.VISIBLE);
            }
        });
        holder.cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.respuestas.setVisibility(View.GONE);
            }
        });
    }

    @NonNull
    @Override
    public ListViewQuestions.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_preguntas_frecuentes, parent, false);
        return new ListViewQuestions.ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView answer, questions ;
        Button verRespuestas, cerrar;
        LinearLayout respuestas;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            answer = itemView.findViewById(R.id.txt_respuesta_pregunta);
            questions = itemView.findViewById(R.id.txt_pregunta);
            verRespuestas = itemView.findViewById(R.id.btn_seleccionar_pregunta);
            cerrar = itemView.findViewById(R.id.btn_cerrar_respuesta);
            respuestas= itemView.findViewById(R.id.linear_respuesta);
        }
    }
}
