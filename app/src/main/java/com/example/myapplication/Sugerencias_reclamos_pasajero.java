package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.Adapter.ListViewCommentAdmin;
import com.example.myapplication.Adapter.ListViewCommentPassenger;
import com.example.myapplication.Models.Comment;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Sugerencias_reclamos_pasajero extends Fragment {
    RecyclerView recyclerView;
    ListViewCommentPassenger Adapter;
    FirebaseFirestore firebaseFirestore;
    EditText edit_comentario_pasajero;
    Button btn_seleccionar_empresa, btn_comentar_pasajero;
    Spinner spinner_empresas;
    String [] empresas = {};
    String [] id_empresa={};
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_sugerencias_reclamos_pasajero, container, false);
        // Initialize
        edit_comentario_pasajero = view.findViewById(R.id.edit_comentario_pasajero);
        btn_seleccionar_empresa = view.findViewById(R.id.btn_seleccionar_empresa);
        btn_comentar_pasajero = view.findViewById(R.id.btn_comentar_pasajero);
        spinner_empresas = view.findViewById(R.id.spinner_empresas);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        ///
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView=view.findViewById(R.id.recycler_respuestas_pasajero);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Query query = firebaseFirestore.collection("comments");
        FirestoreRecyclerOptions<Comment> firestoreRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<Comment>().setQuery(query,Comment.class).build();
        Adapter = new ListViewCommentPassenger(firestoreRecyclerOptions);
        Adapter.notifyDataSetChanged();
        recyclerView.setAdapter(Adapter);
        Adapter.startListening();
        empresasExistentes();
        btn_seleccionar_empresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int empresa_ok = spinner_empresas.getSelectedItemPosition();
                empresaSeleccionada(id_empresa[empresa_ok], view);
            }
        });
        btn_comentar_pasajero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.collection("passenger").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                                String fullname = documentSnapshot.getString("name")+ " " + documentSnapshot.getString("surname");
                                Map<String,Object> map = new HashMap<>();
                                map.put("author",fullname);
                                map.put("message",edit_comentario_pasajero.getText().toString());
                                map.put("id_transport",id_empresa[spinner_empresas.getSelectedItemPosition()]);

                                if(edit_comentario_pasajero.getText().toString().length() >= 10){
                                    edit_comentario_pasajero.setText("");
                                    db.collection("comments").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(getContext(), "Comentario enviado", Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(getContext(), "Error al enviar comentario", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }else{
                                    Toast.makeText(getContext(), "Agregar un comentario de al menos 10 caracteres", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                });
            }
        });
        return view;
    }

    public void empresaSeleccionada(String empresa_id, View view){
        // Restart the adapter
        Query query = firebaseFirestore.collection("comments").whereEqualTo("id_transport", empresa_id);
        FirestoreRecyclerOptions<Comment> firestoreRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<Comment>().setQuery(query,Comment.class).build();
        Adapter = new ListViewCommentPassenger(firestoreRecyclerOptions);
        Adapter.notifyDataSetChanged();
        recyclerView.setAdapter(Adapter);
        Adapter.startListening();
    }

    public void empresasExistentes(){
        db.collection("transportation").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    empresas = new String[task.getResult().size()];
                    id_empresa = new String[task.getResult().size()];
                    int i = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        empresas[i] = document.getString("name");
                        id_empresa[i] = document.getId();
                        i++;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, empresas);
                    spinner_empresas.setAdapter(adapter);
                }else {
                    Toast.makeText(getContext(), "Error al cargar las empresas", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}