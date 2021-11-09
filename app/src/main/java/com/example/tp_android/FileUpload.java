package com.example.tp_android;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.net.URI;

public class FileUpload extends AppCompatActivity {

    protected Button selectFile, upload;
    protected TextView notification;
    Uri pdfUri;

    protected FirebaseStorage storage;
    protected FirebaseDatabase database;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.files_uploader);

        storage = FirebaseStorage.getInstance();
//        storageRef = storage.getReferenceFromUrl("gs://file-manage-3ee57.appspot.com");
        database = FirebaseDatabase.getInstance();

        selectFile = findViewById(R.id.selectFiles);
        upload = findViewById(R.id.uploadFiles);
        notification = findViewById(R.id.notification);

        selectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(
                        FileUpload.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED){
                    selectPdf();
                }else{
                    ActivityCompat.requestPermissions(
                            FileUpload.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            9);
                }
            }
        });

        upload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(pdfUri != null){
                    uploadFile(pdfUri);
                }else{
                    Toast.makeText(
                            FileUpload.this,
                            "Seleciona un archivo",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
    }
    private void uploadFile(Uri pdfUri){

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Cargando archivo...");
        progressDialog.setProgress(0);
        progressDialog.show();
        final String fileName = System.currentTimeMillis()+"";
        StorageReference storageReference = storage.getReference();


        storageReference.child("Uploads").child(fileName).putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String url = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                        DatabaseReference reference = database.getReference();
                        reference.child(fileName).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(
                                            FileUpload.this,
                                            "El archivo se cargo correctamente",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }else{
                                    Toast.makeText(
                                            FileUpload.this,
                                            "Error al cargar el archivo",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(
                        FileUpload.this,
                        "Error al cargar el archivo",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                int currentProgress = (int) (100*taskSnapshot.getBytesTransferred()/
                        taskSnapshot.getTotalByteCount());

                progressDialog.setProgress(currentProgress);
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if(requestCode==9 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            selectPdf();
        }else{
            Toast.makeText(
                    FileUpload.this,
                    "Asignar permiso",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void selectPdf(){
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 86);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 86 && resultCode == RESULT_OK && data != null) {
            pdfUri = data.getData();
            notification.setText(
                    "Un archivo ha sido seleccionado : "+data.getData().getLastPathSegment());
        }else{
            Toast.makeText(
                    FileUpload.this,
                    "Debes seleccionar un archivo",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}

