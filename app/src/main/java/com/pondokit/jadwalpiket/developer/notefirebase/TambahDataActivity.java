package com.pondokit.jadwalpiket.developer.notefirebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class TambahDataActivity extends AppCompatActivity {

    EditText etJudulNote, etIsiNote;
    Button btnTambah;

    //Firebase
    protected DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_data);
        setView();
        setOneClick();

        //Syscron untuk ke firebase
        mDatabase = FirebaseDatabase.getInstance().getReference().child("simpan");
        mDatabase.keepSynced(true);

    }

    private void setView() {

        btnTambah = findViewById(R.id.btnTambahData);
        etJudulNote = findViewById(R.id.et_judul);
        etIsiNote = findViewById(R.id.et_isi);

    }

    private void setOneClick() {

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

            private String getTanggal() {
                DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
                Date date = new Date();
                return dateFormat.format(date);
            }

            private void upload_data(String judul, String isi, String date) {

                //memberikan arahan simpan firebase
                mDatabase = FirebaseDatabase.getInstance().getReference().child("simpan").child(String.valueOf(mDatabase.push().getKey()));

                HashMap<String, String> useMap = new HashMap<>();
                useMap.put("judul_note", judul);
                useMap.put("isi_note", isi);
                useMap.put("date_note", date);

                mDatabase.setValue(useMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            Intent pindah_layout = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(pindah_layout);
                            finish();
                            Toast.makeText(TambahDataActivity.this, "Alhamdulillah DaTa BeRhAsIl Di SiMpAn", Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(TambahDataActivity.this, "DatA bElUm TeRsImPaN, SiLaHkAn pErIkSa KoNeKsI AnDa", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
    }
}
