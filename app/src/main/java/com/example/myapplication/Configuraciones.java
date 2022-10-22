package com.example.myapplication;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class Configuraciones extends Fragment {
    private TextView txt_config_phone;
    private EditText edit_config_nombres, edit_config_surnames,
            edit_config_phone,edit_config_address;
    private Button btn_config_save;
    private LinearLayout Layout_perfil;
    private ProgressBar progressBar_perfil;
    private ImageView photo_profile;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    Button btn_photo;
    Button btn_delete;
    StorageReference storageReference;
    String storage_path = "profile/*";

    private static final int COD_SEL_STORAGE=200;
    private static final int COD_SEL_IMAGE=300;

    Uri image_url;
    String photo = "photo";
    String idd;

    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_configuraciones, container, false);
        // Initialize
        txt_config_phone = view.findViewById(R.id.txt_config_phone);
        edit_config_nombres=view.findViewById(R.id.edit_config_nombres);
        edit_config_surnames=view.findViewById(R.id.edit_config_surnames);
        edit_config_phone=view.findViewById(R.id.edit_config_phone);
        edit_config_address=view.findViewById(R.id.edit_config_address);
        btn_config_save=view.findViewById(R.id.btn_config_save);
        btn_photo=view.findViewById(R.id.btn_photo);
        btn_delete=view.findViewById(R.id.btn_delete);
        Layout_perfil=view.findViewById(R.id.Layout_perfil);
        progressBar_perfil=view.findViewById(R.id.progressBar_perfil);
        photo_profile=view.findViewById(R.id.imageView11);
        auth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        // Load data
        Layout_perfil.setVisibility(View.GONE);

        db.collection("passenger").document(auth.getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            edit_config_nombres.setText(documentSnapshot.getString("name"));
                            edit_config_surnames.setText(documentSnapshot.getString("surname"));
                            edit_config_phone.setText(documentSnapshot.getString("phone"));
                            edit_config_address.setText(documentSnapshot.getString("direction"));
                            Layout_perfil.setVisibility(View.VISIBLE);
                            progressBar_perfil.setVisibility(View.GONE);
                        }else {
                            db.collection("administrator").document(auth.getCurrentUser().getUid())
                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            txt_config_phone.setVisibility(View.GONE);
                                            edit_config_phone.setVisibility(View.GONE);
                                            edit_config_nombres.setText(documentSnapshot.getString("name"));
                                            edit_config_surnames.setText(documentSnapshot.getString("surname"));
                                            edit_config_address.setText(documentSnapshot.getString("direction"));
                                            Layout_perfil.setVisibility(View.VISIBLE);
                                            progressBar_perfil.setVisibility(View.GONE);
                                        }
                                    });
                        }
                    }
                });

        btn_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto();
            }
        });

        btn_config_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar_perfil.setVisibility(View.VISIBLE);
                db.collection("passenger").document(auth.getCurrentUser().getUid())
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()){
                                    if (edit_config_nombres.getText().toString().isEmpty() ||
                                            edit_config_surnames.getText().toString().isEmpty() ||
                                            edit_config_phone.getText().toString().isEmpty() ||
                                            edit_config_address.getText().toString().isEmpty()){
                                        Toast.makeText(getContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                                    }else {
                                        progressBar_perfil.setVisibility(View.VISIBLE);
                                        Map<String,Object> map = new HashMap<>();
                                        map.put("name",edit_config_nombres.getText().toString());
                                        map.put("surname",edit_config_surnames.getText().toString());
                                        map.put("phone",edit_config_phone.getText().toString());
                                        map.put("direction",edit_config_address.getText().toString());
                                        db.collection("passenger").document(auth.getCurrentUser().getUid())
                                                .update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(getContext(), "Datos actualizados", Toast.LENGTH_SHORT).show();
                                                        progressBar_perfil.setVisibility(View.GONE);
                                                    }
                                                });
                                    }
                                }else{
                                    db.collection("administrator").document(auth.getCurrentUser().getUid())
                                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    if (edit_config_nombres.getText().toString().isEmpty() ||
                                                            edit_config_surnames.getText().toString().isEmpty() ||
                                                            edit_config_address.getText().toString().isEmpty()){
                                                        Toast.makeText(getContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                                                    }else {
                                                        progressBar_perfil.setVisibility(View.VISIBLE);
                                                        Map<String,Object> map = new HashMap<>();
                                                        map.put("name",edit_config_nombres.getText().toString());
                                                        map.put("surname",edit_config_surnames.getText().toString());
                                                        map.put("direction",edit_config_address.getText().toString());
                                                        db.collection("administrator").document(auth.getCurrentUser().getUid())
                                                                .update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        Toast.makeText(getContext(), "Datos actualizados", Toast.LENGTH_SHORT).show();
                                                                        progressBar_perfil.setVisibility(View.GONE);
                                                                    }
                                                                });
                                                    }
                                                }
                                            });
                                }
                            }
                        });

            }
        });


        return view;
    }

    private void uploadPhoto(){
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");

        startActivityForResult(i,COD_SEL_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("image_url","requestCode - RESULT_OK: "+requestCode+" "+ RESULT_OK);
        if (resultCode == Activity.RESULT_OK) {
            if(requestCode==COD_SEL_IMAGE){
                image_url = data.getData();
                subirPhoto(image_url);
            }
        }
        super.onActivityResult(requestCode,resultCode,data);
    }

    private void subirPhoto(Uri image_url){
        progressDialog.setMessage("Actualizando Foto");
        progressDialog.show();
        String rute_storage_photo = storage_path + "" + photo + "" + auth.getUid() + "" + idd;
        StorageReference reference = storageReference.child(rute_storage_photo);
        reference.putFile(image_url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isSuccessful());
                if(uriTask.isSuccessful()){
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String download_uri = uri.toString();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("photo",download_uri);
                            db.collection("passenger").document(idd).update(map);
                            Toast.makeText(getContext(), "Foto Actualizada", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Error al cargar foto",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAdministrador(String id){
        db.collection("passenger").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                    //DecimalFormat format = new DecimalFormat("0.00");
                    String direction = documentSnapshot.getString("direction");
                    String email = documentSnapshot.getString("email");
                    String name = documentSnapshot.getString("name");
                    String password = documentSnapshot.getString("password");
                    String surname = documentSnapshot.getString("surname");
                    String status = documentSnapshot.getString("status");
                    String photo = documentSnapshot.getString("photo");
                    String phone = documentSnapshot.getString("phone");

                    edit_config_nombres.setText(name);
                    edit_config_surnames.setText(surname);
                    edit_config_phone.setText(phone);
                    edit_config_address.setText(direction);

                    try{
                        if(!photo.equals("")){
                            Toast toast = Toast.makeText(getContext(),"Cargando Foto",Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP,0,200);
                            toast.show();
                            Picasso.with(getContext())
                                    .load(photo)
                                    .resize(150,150)
                                    .into(photo_profile);
                        }
                    }catch (Exception e){
                     Log.v("Error","e: " + e);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Error al obterner los datos", Toast.LENGTH_SHORT).show();
            }
        });
        }
}