package com.pondokit.jadwalpiket.developer.notefirebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class EditData extends MainActivity {

    EditText etJudulNote, etIsiNote;
    Button btnTambah;
    TextView token;

    //Firebase
    protected DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_data);
        setView();
        setOneClick();

        etJudulNote.setText(getIntent().getStringExtra("judul"));
        etIsiNote.setText(getIntent().getStringExtra("isi"));
        token.setText(getIntent().getStringExtra("token"));

    }

    private void setView() {

        btnTambah = findViewById(R.id.btnTambahData);
        etJudulNote = findViewById(R.id.et_judul);
        etIsiNote = findViewById(R.id.et_isi);
        token = findViewById(R.id.id);

    }

    private void setOneClick(){

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String judul = "-";
                String isi = "-";
                String time = "-";

                judul = etJudulNote.getText().toString().trim();
                isi = etIsiNote.getText().toString().trim();
                time = getTanggal();

                upload_data(judul, isi, time);


            }
        });

    }

    private String getTanggal() {
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void upload_data(String judul, String isi, String time) {

        mDatabase = FirebaseDatabase.getInstance().getReference().child("simpan").child(token.getText().toString());

        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("judul_note", judul);
        userMap.put("isi_note", isi);
        userMap.put("date_note", time);

        mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    Intent pindah_layout = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(pindah_layout);
                    finish();
                    Toast.makeText(EditData.this, "Alhamdulillah DaTa BeRhAsIl Di EdIt", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(EditData.this, "DatA bElUm TeRsImPaN, SiLaHkAn pErIkSa KoNeKsI AnDa", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

}
