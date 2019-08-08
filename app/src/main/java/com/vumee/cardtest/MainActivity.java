package com.vumee.cardtest;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.vumee.cardtest.Utils.BottomNavigationViewHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity  {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Context mContext = MainActivity.this;
    private NoteAdapter adapter;
    private static final String TAG = "MainActivity";
    private  static  final int ACTIVITY_NUM = 0;
    private FirebaseAuth mAuth;
    //Boolean variable to mark if the transaction is safe
    private boolean isTransactionSafe;
    //Boolean variable to mark if there is any transaction pending
    private boolean isTransactionPending;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setUpRecyclerView();
        setupBottomNavigation();
        checkpermission();
        setupnotification();

    }

    private void setupnotification() {
        GlobalClass globalClass = (GlobalClass) MainActivity.this.getApplicationContext();
        String company = globalClass.getCompanyname();
        String group = globalClass.getGroupname();

        String Subscription = company+group;

        FirebaseMessaging.getInstance().subscribeToTopic(Subscription);


    }

    private void checkpermission() {
        final int Permission_All = 1;

        final String[] Permissions = {
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.SEND_SMS,
        };

        if(!MainActivity.hasPermissions(this, Permissions)){
            ActivityCompat.requestPermissions(this, Permissions, Permission_All);
        }
    }

    private static boolean hasPermissions(final Context context, final String... permissions) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && context!=null && permissions!=null){
            for(final String permission: permissions){
                if(ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
                    return  false;
                }
            }
        }
        return true;
    }


    private void setupBottomNavigation() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    private void setUpRecyclerView() {
        GlobalClass globalClass = (GlobalClass) MainActivity.this.getApplicationContext();
        String company  = globalClass.getCompanyname();
        String user = globalClass.getUser_id();
        String group = globalClass.getGroupname();

        String alpha = company+"/"+group+"/landingpage";

        Map<String, Object> data = new HashMap<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());
        String deviceAppUID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        data.put(deviceAppUID, "");


        DocumentReference deviceid = db.collection(company).document("users").collection(user).document("login_activity");
        deviceid.set(data, SetOptions.merge());

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

                GlobalClass globalClass = (GlobalClass) MainActivity.this.getApplicationContext();
                globalClass.setLplink(note.getLplink());
            }

            @Override
            public void onShareClick(DocumentSnapshot documentSnapshot, int position) {
                Note note = documentSnapshot.toObject(Note.class);
                GlobalClass globalClass = (GlobalClass) MainActivity.this.getApplicationContext();
                globalClass.setLplink(note.getLplink());

                openDialog();
            }
        });
    }

    private void openDialog() {
        if(isTransactionSafe){
        ExampleDialog exampledialog = new ExampleDialog();
        exampledialog.show(getSupportFragmentManager(), "example dialog");
        }
        else {
/*
If any transaction is not done because the activity is in background. We set the isTransactionPending variable to true so that we can pick this up when we come back to foreground
*/
            isTransactionPending = true;
        }
    }
    /*
    onPostResume is called only when the activity's state is completely restored. In this we will
    set our boolean variable to true. Indicating that transaction is safe now
    */
    public void onPostResume() {
        super.onPostResume();
        isTransactionSafe = true;
        /* Here after the activity is restored we check if there is any transaction pending from the last restoration
         */
        if (isTransactionPending) {
            openDialog();
        }

    }
    /*
    onPause is called just before the activity moves to background and also before onSaveInstanceState. In this
    we will mark the transaction as unsafe
    */
    public void onPause() {
        super.onPause();
        isTransactionSafe = false;
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