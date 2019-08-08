package com.vumee.cardtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.vumee.cardtest.Utils.BottomNavigationViewHelper;
import com.vumee.cardtest.Utils.BottomNavigationViewHelper_train;

public class train extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("notebook2");
    private NoteAdapter adapter;
    private static final String TAG = "TrainActivity";
    private  static  final int ACTIVITY_NUM = 1;
    private Context mContext = train.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);
        setUpRecyclerView();
        setupBottomNavigation();
    }

    private void setUpRecyclerView() {
        GlobalClass globalClass = (GlobalClass) train.this.getApplicationContext();
        String company  = globalClass.getCompanyname();
        String group = globalClass.getTraininggroup();

        String alpha = company+"/"+group+"/landingpage";



        CollectionReference notebookRef = db.collection(alpha);

        Query query = notebookRef.orderBy("priority", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();
        adapter = new NoteAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Note note = documentSnapshot.toObject(Note.class);
                String id = documentSnapshot.getId();


                Intent intent = new Intent(getApplicationContext(),  category_lp.class);
                startActivity(intent);

                GlobalClass globalClass = (GlobalClass) train.this.getApplicationContext();
                globalClass.setLplink(note.getLplink());
            }

            @Override
            public void onShareClick(DocumentSnapshot documentSnapshot, int position) {
                Note note = documentSnapshot.toObject(Note.class);
                GlobalClass globalClass = (GlobalClass) train.this.getApplicationContext();
                globalClass.setLplink(note.getLplink());

                openDialog();
            }
        });
    }

    private void openDialog() {
        ExampleDialog exampledialog = new ExampleDialog();
        exampledialog.show(getSupportFragmentManager(), "example dialog");

    }

    private void setupBottomNavigation() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewExT = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper_train.setupBottomNavigationView(bottomNavigationViewExT);
        BottomNavigationViewHelper_train.enableNavigation(mContext, bottomNavigationViewExT);
        Menu menu = bottomNavigationViewExT.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
