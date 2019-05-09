package com.pondokit.jadwalpiket.developer.notefirebase;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fbTambahData;

    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private DatabaseReference mDatabase;

    public FirebaseRecyclerAdapter<ModelGetNote, NewsViewHolder> mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setView();
        setOneClick();
        setGetNote();

    }

    private void setView() {

        fbTambahData = findViewById(R.id.fb_tambah_data);

    }

    private void setOneClick() {

        fbTambahData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent layouttambahdata = new Intent(getApplicationContext(), TambahDataActivity.class);
                startActivity(layouttambahdata);
                finish();

            }
        });

    }

    private void setGetNote() {

        mDatabase = FirebaseDatabase.getInstance().getReference().child("simpan");
        mDatabase.keepSynced(true);

        recyclerView = findViewById(R.id.rc_note_list);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("simpan");
        Query personsQuery = mDatabase.orderByKey();

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mLayoutManager.setReverseLayout(false);
        mLayoutManager.setStackFromEnd(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);

        final FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<ModelGetNote>().setQuery(personsQuery, ModelGetNote.class).build();

        mAdapter = new FirebaseRecyclerAdapter<ModelGetNote, NewsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull NewsViewHolder holder, int position, @NonNull ModelGetNote model) {

                holder.setJudul_note(model.getJudul_note());
                holder.setIsi_note(model.getIsi_note());
                holder.setTanggal_note(model.getDate_note());
                holder.setContainer(position);

            }

            @NonNull
            @Override
            public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_note, viewGroup, false);
                return new NewsViewHolder(view);
            }
        };

        swipeRefreshLayout = findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);

                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

//                        recyclerView.setAdapter(mAdapter);
//                        mAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);

                    }
                }, 3000);
            }
        });

        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        View mView;

        protected NewsViewHolder(android.view.View itemView) {
            super(itemView);
            mView = itemView;
        }

        private void setJudul_note(String Judul_note) {
            TextView tvJudul = mView.findViewById(R.id.tv_judul_note);
            tvJudul.setText(Judul_note);
        }

        private void setIsi_note(String Isi_note) {
            TextView tvJudul = mView.findViewById(R.id.tv_isi);
            tvJudul.setText(Isi_note);
        }

        private void setTanggal_note(String Tanggal_note) {
            TextView tvJudul = mView.findViewById(R.id.tv_tanggal);
            tvJudul.setText(Tanggal_note);
        }

        public void setContainer(final int id) {

            CardView container = mView.findViewById(R.id.container);
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FirebaseDatabase.getInstance().getReference().child("simpan").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String asd = mAdapter.getRef(id).getKey();
                            String judul = dataSnapshot.child(asd).child("judul_note").getValue().toString();
                            String isi = dataSnapshot.child(asd).child("isi_note").getValue().toString();

                            Intent pindah = new Intent(getApplicationContext(), EditData.class);
                            pindah.putExtra("judul", judul);
                            pindah.putExtra("isi", isi);
                            pindah.putExtra("token", asd);
                            startActivity(pindah);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });

            container.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage("Apakah Anda Mau Menghapus Jadwal Ini ?")
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mAdapter.getRef(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()){

                                                Toast.makeText(MainActivity.this, "dAtA BeRhAsIl Di hApUs", Toast.LENGTH_SHORT).show();

                                            }

                                        }
                                    });
                                }
                            })
                            .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();

                                }
                            })
                            .show();

                    return false;
                }
            });

        }

    }
}
